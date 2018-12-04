package com.example.madeleine.family_map_v2;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madeleine.family_map_v2.Model.CurrentSession;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    CurrentSession session = CurrentSession.getInstance();
    private static Spinner lifeStorySpinner;
    private static Spinner familyTreeSpinner;
    private static Spinner spouseSpinner;
    private static Spinner mapSpinner;

    private static Switch lifeStorySwitch;
    private static Switch familyTreeSwitch;
    private static Switch spouseSwitch;

    private static TextView logoutTextView;
    private static TextView syncDataTextView;

    /*private ArrayList<String> colorOptions = new ArrayList<>();
    private ArrayList<String> mapOptions = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lifeStorySwitch = findViewById(R.id.life_lines_switch);
        familyTreeSwitch = findViewById(R.id.family_tree_lines_switch);
        spouseSwitch = findViewById(R.id.spouse_lines_switch);
        lifeStorySwitch.setChecked(session.lifeStoryLines);
        familyTreeSwitch.setChecked(session.familyTreeLines);
        spouseSwitch.setChecked(session.spouseLines);

        lifeStorySpinner = findViewById(R.id.life_lines_spinner);
        familyTreeSpinner = findViewById(R.id.family_tree_lines_spinner);
        spouseSpinner = findViewById(R.id.spouse_line_spinner);
        mapSpinner = findViewById(R.id.map_spinner);

        lifeStorySpinner.setSelection(session.lifeColorIndex);
        familyTreeSpinner.setSelection(session.familyColorIndex);
        spouseSpinner.setSelection(session.spouseColorIndex);
        mapSpinner.setSelection(session.mapTypeIndex);

        logoutTextView = findViewById(R.id.logout_textview);
        syncDataTextView = findViewById(R.id.resync_textview);

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                finish();
            }
        });

        syncDataTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SyncDataAsync().execute();
            }
        });

        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) session.lifeStoryLines = true;
                else session.lifeStoryLines = false;
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) session.familyTreeLines = true;
                else session.familyTreeLines = false;
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) session.spouseLines = true;
                else session.spouseLines = false;
            }
        });

        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session.lifeColorIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session.familyColorIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session.spouseColorIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session.mapTypeIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private class SyncDataAsync extends AsyncTask <String, Void, Integer>{
        protected Integer doInBackground(String...params) { //feeds array params
            if (session.syncServerData()) return 1;
            return 0;
        }
        protected void onPostExecute(Integer success) {
            if (success == 1) {
                finish();
            }
            else {
                Toast.makeText(SettingsActivity.this, "Error syncing data, please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

}