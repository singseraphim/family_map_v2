package com.example.madeleine.family_map_v2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.madeleine.family_map_v2.Model.CurrentSession;
import com.example.madeleine.family_map_v2.Model.Event;

public class EventActivity extends AppCompatActivity {

    private Event selectedEvent;
    private CurrentSession session = CurrentSession.getInstance();
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = this.getSupportFragmentManager();
        Bundle extras = getIntent().getExtras();
        String eventID = extras.getString("eventID");
        //selectedEvent = session.getEventByID(eventID);
        mapFragment = (MapFragment) fm.findFragmentById(R.id.fragmentFrameLayout); //make sure this ID is fine



        if (mapFragment == null) {

            mapFragment = new MapFragment();
            Bundle bundle = new Bundle();
            bundle.putString("eventID", eventID);
            mapFragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.fragmentFrameLayout, mapFragment)
                    .commit();
        }
        //I need to talk to my map fragment.
    }

}
