package com.example.madeleine.family_map_v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madeleine.family_map_v2.Model.CurrentSession;
import com.example.madeleine.family_map_v2.Model.Event;
import com.example.madeleine.family_map_v2.Model.Person;
import com.example.madeleine.family_map_v2.Model.ListEntry;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView resultsRecyclerView;
    private Adapter adapter;
    private CurrentSession session = CurrentSession.getInstance();
    private static EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultsRecyclerView = SearchActivity.this.findViewById(R.id.results_recycler_view);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchField = findViewById(R.id.search_field);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String searchTerm = searchField.getText().toString();
                    ListEntry[] results = search(searchTerm);
                    updateUI(results);
                }
                return true;
            }
        });

    }

    ListEntry[] search(String searchTerm) {
        ArrayList<ListEntry> results = new ArrayList<>();

        ArrayList<Person> personResults = session.searchPersons(searchTerm);
        ArrayList<Event> eventResults = session.searchEvents(searchTerm);

        for (Person person : personResults) {
            ListEntry result = new ListEntry();
            result.header = person.firstName + " " + person.lastName;
            result.type = "person";
            result.subHeader = "";
            result.itemID = person.personID;
            results.add(result);
        }
        for (Event event : eventResults) {
            ListEntry result = new ListEntry();
            Person eventPerson = session.getPersonByID(event.person);
            result.header = event.eventType + ": " + event.city + ", " + event.country;
            result.subHeader =  eventPerson.firstName + " " + eventPerson.lastName + " (" + event.year + ")";
            result.type = "event";
            result.itemID = event.eventID;
            results.add(result);
        }

        ListEntry[] resultArray = new ListEntry[results.size()];
        for (int i = 0; i < results.size(); ++i) {
            resultArray[i] = results.get(i);
        }
        return resultArray;
    }

    void updateUI(ListEntry[] items) {

        adapter = new Adapter(this, items);
        resultsRecyclerView.setAdapter(adapter);

    }

    class Adapter extends RecyclerView.Adapter<Holder> {

        private ListEntry[] items;
        private LayoutInflater inflater;

        public Adapter(Context context, ListEntry[] items) {
            this.items = items;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.child_list_item, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ListEntry item = items[position];
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

    }
    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView header;
        private TextView subHeader;
        private ListEntry item;
        private LinearLayout itemLayout;
        private ImageView icon;


        public Holder(View view) {
            super(view);
            header = view.findViewById(R.id.header);
            subHeader = view.findViewById(R.id.subheader);
            itemLayout = view.findViewById(R.id.result_layout);
            icon = view.findViewById(R.id.icon);
            itemLayout.setOnClickListener(this);
            //checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            //checkBox.setOnClickListener(this); where is this?
        }

        void bind(ListEntry item) {
            this.item = item;
            header.setText(item.header);
            subHeader.setText(item.subHeader);


            if (item.type == "event") {
                //event icon
                //icon.setBackground("@drawable/"); something something, drawables are hard
            }
            else {
                Person person = session.getPersonByID(item.itemID);
                if (person.gender == "f") {
                    //female icon
                }
                else {
                    //male icon
                }

            }
        }

        @Override
        public void onClick(View view) {

           if (item.type == "event") {
               Intent intent = new Intent(SearchActivity.this, EventActivity.class);
               intent.putExtra("eventID", item.itemID);
               startActivity(intent);

           }
           else {

               Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
               intent.putExtra("personID", item.itemID);
               startActivity(intent);

           }
        }

    }




}

/*
CAPTAIN'S LOG:
Some things are going good!
Current issues:
Only showing one events result sometimes
Person names are not working
Scrolling up is weird



 */
