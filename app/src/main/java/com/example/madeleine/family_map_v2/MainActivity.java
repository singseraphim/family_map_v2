package com.example.madeleine.family_map_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.madeleine.family_map_v2.Model.CurrentSession;

public class MainActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private MapFragment mapFragment;
    private CurrentSession session = CurrentSession.getInstance();
    private static Button filterButton;
    private static Button searchButton;
    private static Button settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        FragmentManager fm = this.getSupportFragmentManager();

        filterButton = findViewById(R.id.filter_button);
        searchButton = findViewById(R.id.search_button);
        settingsButton = findViewById(R.id.settings_button);

        if (!session.loggedIn) {
            filterButton.setVisibility(View.GONE);
            searchButton.setVisibility(View.GONE);
            settingsButton.setVisibility(View.GONE);
           loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragmentFrameLayout); //what does this do?
           if (loginFragment == null) {
               loginFragment = new LoginFragment();
               fm.beginTransaction()
                       .add(R.id.fragmentFrameLayout, loginFragment)
                       .commit();
           }
       }
       else {
            filterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, FilterActivity.class);
                    startActivity(intent);
                }
            });

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            });

            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
            });


            mapFragment = (MapFragment) fm.findFragmentById(R.id.fragmentFrameLayout);

           if (mapFragment == null) {
               mapFragment = new MapFragment();
               fm.beginTransaction()
                       .add(R.id.fragmentFrameLayout, mapFragment)
                       .commit();
           }

       }

    }
}


/*
CAPTAIN'S LOG:

Settings is not done, I thought it was but it isn't. I need to update session based on what's selected.
I am working on the map markers, I need to tie data and onclick methods to them.

To do:
Map markers data
Event data section
Lines between events

TA QUESTIONS:
Up arrow isn't supposed to start a new main activity right? If it isn't, then I need to update map data another way.
Walk through downloading an icon to use in project

 */

/*
AN EXPANDED LIST OF THINGS TO DO:
Fix icons
Event section of map fragment
-Connect markers to event data
Display lines on map
Get lists of lines to display on map
Event activity
Fix collapsible lists in person activity

 */

/*
VIEWS:
Login- done, passed off
Main- in progress
Event-
Settings- done, can't pass off
Filter- done, ready to be passed off
Search- done, needs icons
Person- done, needs icons

 */