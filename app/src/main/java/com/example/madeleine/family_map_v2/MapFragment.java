package com.example.madeleine.family_map_v2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madeleine.family_map_v2.Model.CurrentSession;
import com.example.madeleine.family_map_v2.Model.Event;
import com.example.madeleine.family_map_v2.Model.Person;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Event> eventList;
    private CurrentSession session = CurrentSession.getInstance();
    private TextView personName;
    private TextView eventData;
    private ImageView personIcon;
    private LinearLayout eventLayout;
    private Event activeEvent;
    private ArrayList<Polyline> activeLines = new ArrayList<>();
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        refreshMapData();

        personIcon = rootView.findViewById(R.id.map_event_icon);
        personName = rootView.findViewById(R.id.map_person_name);
        eventData = rootView.findViewById(R.id.map_event_details);
        eventLayout = rootView.findViewById(R.id.event_layout);
        eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapFragment.this.getActivity(), PersonActivity.class);
                intent.putExtra("personID", activeEvent.person);
                startActivity(intent);
            }
        });
        eventLayout.setClickable(false);


        return rootView;
    }

    void refreshMapData() {
        eventList = session.filteredEventList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //this is probably important later
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Resources res = getResources();
        String mapType = res.getTextArray(R.array.map_options)[session.mapTypeIndex].toString();

        if (mapType.equals("Hybrid")) mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else if (mapType.equals("Normal")) mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else if (mapType.equals("Satellite")) mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else if (mapType.equals("Terrain")) mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        Bundle bundle = this.getArguments();
        String activeEventID = bundle.getString("eventID");
        if (!activeEventID.equals("")) {
           activeEvent = session.getEventByID(activeEventID);
           mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(activeEvent.latitude, activeEvent.longitude)));
           updateUI(session.getEventByID(activeEventID));
        }

        for (Event event : eventList) {
            LatLng location = new LatLng(event.latitude, event.longitude);

            if (event.eventType.equals("Birth")) {
                mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            }
            else if (event.eventType.equals("Marriage")) {
                mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            else {
                mMap.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                double latitude = marker.getPosition().latitude;
                double longitude = marker.getPosition().longitude;
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                Event currentEvent = session.getEventByCoordinates(latitude, longitude);
                updateUI(currentEvent);
                return true;
            }
        });

        //I need to be able to generate lines, based on the criteria


    }

    public void addLines(Event event) {
        Person person = session.getPersonByID(event.person);
        if (session.lifeStoryLines) {
            ArrayList<Event> lifeEvents = session.getEventsForPerson(person.personID);
            for (int i = 0; i < lifeEvents.size() - 1; ++i) {
                drawLine(lifeEvents.get(i), lifeEvents.get(i + 1), session.lifeColorIndex, 12);
            }
        }
        if (session.spouseLines) {
            Person spouse = session.getPersonByID(person.spouse);
            if (spouse.personID != null) {
                ArrayList<Event> spouseEvents = session.getEventsForPerson(spouse.personID);
                if (spouseEvents.size() > 0) {
                    Event spouseEvent = spouseEvents.get(0);
                    drawLine(event, spouseEvent, session.spouseColorIndex, 12);
                }
            }

        }
        if (session.familyTreeLines) {
            drawAncestorLines(event, 12);
        }
    }

    void drawAncestorLines(Event personEvent, float lineSize) {
        lineSize -= 2;
        Person person = session.getPersonByID(personEvent.person);
        if (person.mother != null) {
            ArrayList<Event> momEvents = session.getEventsForPerson(person.mother);
            if (momEvents.size() > 0) {
                Event momEvent = momEvents.get(0);
                drawLine(personEvent, momEvent, session.familyColorIndex, lineSize);
                drawAncestorLines(momEvent, lineSize);
            }

        }
        if (person.father != null) {
            ArrayList<Event> dadEvents = session.getEventsForPerson(person.father);
            if (dadEvents.size() > 0) {
                Event dadEvent = dadEvents.get(0);
                drawLine(personEvent, dadEvent, session.familyColorIndex, lineSize);
                drawAncestorLines(dadEvent, lineSize);
            }
        }
    }

    void drawLine(Event a, Event b, int colorIndex, float width) {
        Resources res = getResources();
        String[] colorOptions = res.getStringArray(R.array.color_options);
        String color = colorOptions[colorIndex];

        if (color.equals("Red")) {
            activeLines.add(mMap.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(a.latitude, a.longitude),
                            new LatLng(b.latitude, b.longitude)
                    )
                    .color(res.getColor(R.color.red))
                    .width(width)
            ));
        }
        else if (color.equals("Orange")) {

            activeLines.add(mMap.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(a.latitude, a.longitude),
                            new LatLng(b.latitude, b.longitude)
                    )
                    .color(res.getColor(R.color.orange))
                    .width(width)
            ));

        }
        else if (color.equals("Blue")) {

            activeLines.add(mMap.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(a.latitude, a.longitude),
                            new LatLng(b.latitude, b.longitude)
                    )
                    .color(res.getColor(R.color.blue))
                    .width(width)
            ));

        }
        else {
            activeLines.add(mMap.addPolyline(new PolylineOptions()
                    .add(
                            new LatLng(a.latitude, a.longitude),
                            new LatLng(b.latitude, b.longitude)
                    )
                    .color(res.getColor(R.color.purple))
                    .width(width)
            ));

        }


    }

    void updateUI(Event event) {
        eventLayout.setClickable(true);
        activeEvent = event;
        Person activePerson = session.getPersonByID(activeEvent.person);
        personName.setText(activePerson.firstName + " " + activePerson.lastName);
        eventData.setText(activeEvent.eventType + ": " + activeEvent.city + ", " + activeEvent.country
         + " (" + activeEvent.year + ")");
        //set person icon
        clearLines();
        addLines(event);

    }

    void clearLines() {
        for (Polyline line : activeLines) {
            line.remove();
        }
        activeLines.clear();
    }
}
