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
import com.example.madeleine.family_map_v2.Model.Location;
import com.example.madeleine.family_map_v2.Model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TreeMap<Location, String> markerMap = new TreeMap<>();

    private ArrayList<Event> eventList;
    private CurrentSession session = CurrentSession.getInstance();

    private TextView personName;
    private TextView eventData;
    private ImageView personIcon;
    private LinearLayout eventLayout;
    private Event activeEvent;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


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
        personName = rootView.findViewById(R.id.person_name);
        eventData = rootView.findViewById(R.id.map_event_details);
        eventLayout = rootView.findViewById(R.id.event_layout);
        eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                Intent intent = new Intent(MapFragment.this.getActivity(), PersonActivity.class);
                intent.putExtra("personID", activeEvent.person);
                startActivity(intent);
            }
        });

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

        //FIXME
        Resources res = getResources();
        String mapType = res.getTextArray(R.array.map_options)[session.mapTypeIndex].toString();

        if (mapType.equals("Hybrid")) mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else if (mapType.equals("Normal")) mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else if (mapType.equals("Satellite")) mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else if (mapType.equals("Terrain")) mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        for (Event event : eventList) {
            LatLng location = new LatLng(event.latitude, event.longitude);
            Location loc = new Location(event.latitude, event.longitude);

            markerMap.put(loc, event.eventID);

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
                Location loc = new Location(latitude, longitude);
                updateUI(markerMap.get(loc));
                return true;
            }
        });

        //I need to be able to generate lines, based on the criteria


    }

    void updateUI(String eventID) {
        activeEvent = session.getEventByID(eventID);
        Person activePerson = session.getPersonByID(activeEvent.person);
        personName.setText(activePerson.firstName + " " + activePerson.lastName);
        eventData.setText(activeEvent.eventType + ": " + activeEvent.city + ", " + activeEvent.country
         + " (" + activeEvent.year + ")");
        //set person icon

    }
}

/*
ISSUE:
LatLng does not implement comparable so I can't map things.
Model class isn't working for some reason.


 */
