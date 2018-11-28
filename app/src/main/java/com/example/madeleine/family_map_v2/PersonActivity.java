package com.example.madeleine.family_map_v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.example.madeleine.family_map_v2.Model.CurrentSession;
import com.example.madeleine.family_map_v2.Model.Event;
import com.example.madeleine.family_map_v2.Model.ListEntry;
import com.example.madeleine.family_map_v2.Model.Person;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class PersonActivity extends AppCompatActivity {

    private static TextView name;
    private static ImageView icon;
    private CurrentSession session = CurrentSession.getInstance();
    private Person currentPerson;
    private ListEntry[] events;
    private ListEntry[] people;

    private RecyclerView recyclerView;
    private Adapter adapter;

    List<Group> groups;

    public class Group implements Parent<ListEntry> {

        String name;
        ListEntry[] values;

        Group(String name, ListEntry[] values) {
            this.name = name;
            this.values = values;
        }
        @Override
        public List<ListEntry> getChildList() {
            return Arrays.asList(values);
        }
        @Override
        public boolean isInitiallyExpanded() {
            return true;
        }
    }

    class GroupHolder extends ParentViewHolder {

        private TextView textView;
        private LinearLayout parentLayout;

        public GroupHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.parent_text);
            parentLayout = view.findViewById(R.id.parent_layout);
            //parentLayout.setOnClickListener(this);
        }

        void bind(Group group) {
            textView.setText(group.name);
        }

        /*public void onClick(View view) {
            //expand thing
        }*/

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return true;
        }

    }

    class Holder extends ChildViewHolder implements View.OnClickListener {

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

        }

        void bind(ListEntry item) {
            this.item = item;
            header.setText(item.header);
            subHeader.setText(item.subHeader);


            if (item.type == "event") {
                //event icon
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
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                intent.putExtra("eventID", item.itemID);
                startActivity(intent);

            }
            else {

                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra("personID", item.itemID);
                startActivity(intent);

            }
        }

    }



    class Adapter extends ExpandableRecyclerAdapter<Group, ListEntry, GroupHolder, Holder> {

        private LayoutInflater inflater;

        public Adapter(Context context, List<Group> groups) {
            super(groups);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public GroupHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.parent_list_item, viewGroup, false);
            return new GroupHolder(view);
        }

        @Override
        public Holder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.child_list_item, viewGroup, false);
            return new Holder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull GroupHolder holder, int i, Group group) {
            holder.bind(group);
        }

        @Override
        public void onBindChildViewHolder(@NonNull Holder holder, int i, int j, ListEntry item) {
            holder.bind(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        Bundle extras = getIntent().getExtras();
        String personID = extras.getString("personID");
        currentPerson = session.getPersonByID(personID);

        name = findViewById(R.id.person_name);
        name.setText(currentPerson.firstName + " " + currentPerson.lastName);

        icon = findViewById(R.id.person_icon);
        if (currentPerson.gender == "f") {
            //female icon
        }
        else {
            //male icon
        }
        setData();

        groups = Arrays.asList(
                new Group("Events", events),
                new Group("People", people)
        );
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

    }

    void updateUI() {

        adapter = new Adapter(this, groups);
        recyclerView.setAdapter(adapter);

    }

    void setData() {

        //PERSON DATA
        TreeMap<String, Person> personMap = session.getFamily(currentPerson);

        Person child = personMap.get("child");
        Person spouse = personMap.get("spouse");
        Person father = personMap.get("father");
        Person mother = personMap.get("mother");

        ArrayList<ListEntry> personData = new ArrayList<>();

        if (child.personID != null) {
            ListEntry entry = new ListEntry();
            entry.itemID = child.personID;
            entry.header = child.firstName + " " + child.lastName;
            entry.subHeader = "Child";
            entry.type = "person";
            personData.add(entry);

        }
        if (spouse.personID != null) {
            ListEntry entry = new ListEntry();
            entry.itemID = spouse.personID;
            entry.header = spouse.firstName + " " + spouse.lastName;
            entry.subHeader = "Spouse";
            entry.type = "person";
            personData.add(entry);

        }
        if (father.personID != null) {
            ListEntry entry = new ListEntry();
            entry.itemID = father.personID;
            entry.header = father.firstName + " " + father.lastName;
            entry.subHeader = "Father";
            entry.type = "person";
            personData.add(entry);


        }
        if (mother.personID != null) {
            ListEntry entry = new ListEntry();
            entry.itemID = mother.personID;
            entry.header = mother.firstName + " " + mother.lastName;
            entry.subHeader = "Mother";
            entry.type = "person";
            personData.add(entry);
        }

        people = new ListEntry[personData.size()];
        for (int i = 0; i < personData.size(); ++i) {
            people[i] = personData.get(i);
        }

        //EVENT DATA
        ArrayList<Event> eventList = session.getEventsForPerson(currentPerson.personID);
        events = new ListEntry[eventList.size()];
        for (int i = 0; i < eventList.size(); ++i) {
            Event event = eventList.get(i);
            ListEntry entry = new ListEntry();
            entry.type = "event";
            entry.itemID = event.eventID;
            entry.header = event.eventType + ": " + event.city + ", " + event.country;
            entry.subHeader = Integer.toString(event.year);
            events[i] = entry;
        }



    }

}


/*
OK LETS DO SOME HECKIN COLLAPSIBLE WHATEVERS
It's going good!
I need an onclick listener for the parent group somewhere.
And the current text sizes are really bad.


My two problems are connected! how, I have no earthly idea!


 */

/*
getItemViewType,
onbindviewholder
check against dr barkers
 */