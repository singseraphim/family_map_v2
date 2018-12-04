package com.example.madeleine.family_map_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.madeleine.family_map_v2.Model.CurrentSession;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener {
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

        filterButton = findViewById(R.id.filter_button);
        searchButton = findViewById(R.id.search_button);
        settingsButton = findViewById(R.id.settings_button);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.loggedIn) {
            loadMap();
        }
        else {
            loadLogin();
        }
    }

    public void loadLogin() {
        FragmentManager fm = this.getSupportFragmentManager();

        filterButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        settingsButton.setVisibility(View.GONE);

        loginFragment = new LoginFragment();

        fm.beginTransaction()
                .replace(R.id.fragmentFrameLayout, loginFragment)
                .commit();
    }

    @Override
    public void loadMap() {
        filterButton.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);

        FragmentManager fm = this.getSupportFragmentManager();

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

        mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("eventID", "");
        mapFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.fragmentFrameLayout, mapFragment)
                .commit();
    }


}


/*
CAPTAIN'S LOG:

TA QUESTIONS:
Walk through downloading an icon to use in project
Do my lists need to collapse in person activity?

*/
