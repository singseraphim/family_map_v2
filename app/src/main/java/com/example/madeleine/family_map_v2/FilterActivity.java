package com.example.madeleine.family_map_v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.madeleine.family_map_v2.Model.CurrentSession;
import com.example.madeleine.family_map_v2.Model.ListEntry;

public class FilterActivity extends AppCompatActivity {
    private static Button backButton;

    private CurrentSession session = CurrentSession.getInstance();
    private ListEntry[] filterOptions = new ListEntry[session.filters.size()];
    private RecyclerView recyclerView;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        backButton = findViewById(R.id.up_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        setFilterOptions();

        recyclerView = (RecyclerView) findViewById(R.id.filter_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI(filterOptions);


    }

    void updateUI(ListEntry[] items) {

        adapter = new Adapter(this, items);
        recyclerView.setAdapter(adapter);

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
            View view = inflater.inflate(R.layout.filter_item, parent, false);
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
        private Switch toggle;
        private ListEntry item;

        public Holder(View view) {
            super(view);
            header = view.findViewById(R.id.filter_text);
            toggle = view.findViewById(R.id.filter_switch);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        session.filters.put(header.getText().toString(), true);
                    }
                    else {
                        session.filters.put(header.getText().toString(), false);
                    }
                    session.filter();
                }
            });
        }

        void bind(ListEntry item) {
            this.item = item;
            header.setText(item.header);
            boolean switchOn = session.filters.get(item.header);
            if (switchOn) {
                toggle.setChecked(true);
            }
            else {
                toggle.setChecked(false);
            }

        }

        @Override
        public void onClick(View view) {


        }



    }

    void setFilterOptions() {

        int i = 0;
        for (String option : session.filters.keySet()) {
            ListEntry entry = new ListEntry();
            entry.header = option;
            filterOptions[i] = entry;
            ++i;
        }
    }

}
