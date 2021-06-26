package com.example.ratdatabase3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView ratListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadFromDBToMemory();
        setRatAdapter();
        setOnClickListener();
    }



    private void initWidgets()
    {
        ratListView = findViewById(R.id.ratListView);
    }

    private void loadFromDBToMemory() {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateRatListArray();
    }

    private void setRatAdapter() {
        RatAdapter ratAdapter = new RatAdapter(getApplicationContext(), Rat.nonDeletedRats());
        ratListView.setAdapter(ratAdapter);
    }

    private void setOnClickListener() {
        ratListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Rat selectedRat = (Rat) ratListView.getItemAtPosition(position);
                Intent editRatIntent = new Intent(getApplicationContext(), RatDetailActivity.class);
                editRatIntent.putExtra(Rat.RAT_EDIT_EXTRA, selectedRat.getId());
                startActivity(editRatIntent);
            }
        });
    }
    public void newRat(View view) {
        Intent newRatIntent = new Intent(this, RatDetailActivity.class);
        startActivity(newRatIntent);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setRatAdapter();
    }

}