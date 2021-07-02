//Used for editing and creating a new rat

package com.example.ratdatabase3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RatDetailActivity extends AppCompatActivity {

    private EditText subjectEditText, weightEditText, notesEditText;
    private Button deleteButton, nextButton, previousButton;
    private Rat selectedRat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_detail);
        initWidgets();
        checkForEditRat();
    }

    private void initWidgets() {
        subjectEditText = findViewById(R.id.subjectEditText);
        weightEditText = findViewById(R.id.weightEditText);
        notesEditText = findViewById(R.id.notesEditText);
        deleteButton = findViewById(R.id.deleteRatButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.prevButton);
    }

    private void checkForEditRat() {
        Intent previousIntent = getIntent();

        int passedRatID = previousIntent.getIntExtra(Rat.RAT_EDIT_EXTRA, -1);
        selectedRat = Rat.getRatForId(passedRatID);

        if (selectedRat != null) {
            subjectEditText.setText(selectedRat.getSubject());
            weightEditText.setText(selectedRat.getWeight());
            notesEditText.setText(selectedRat.getNotes());
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int passedRatIDNew = passedRatID + 1;
                Rat selectedRatNext = Rat.getRatForId(passedRatIDNew);
                Intent editRatIntent = new Intent(getApplicationContext(), RatDetailActivity.class);
                editRatIntent.putExtra(Rat.RAT_EDIT_EXTRA, selectedRatNext.getId());
                startActivity(editRatIntent);
                finish();

            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int passedRatIDNew = passedRatID - 1;
                Rat selectedRatNext = Rat.getRatForId(passedRatIDNew);
                Intent editRatIntent = new Intent(getApplicationContext(), RatDetailActivity.class);
                editRatIntent.putExtra(Rat.RAT_EDIT_EXTRA, selectedRatNext.getId());
                startActivity(editRatIntent);
                finish();
            }
        });

    }


    public void saveRat(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        String subject = String.valueOf(subjectEditText.getText());
        String weight = String.valueOf(weightEditText.getText());
        String notes = String.valueOf(notesEditText.getText());
        @SuppressLint("SimpleDateFormat") String added = new SimpleDateFormat("dd-MM-yy").format(new Date());

        if (selectedRat == null) {
            int id = Rat.ratArrayList.size();
            Rat newRat = new Rat(id, subject, weight, notes, added);
            Rat.ratArrayList.add(newRat);
            sqLiteManager.addRatToDatabase(newRat);
        } else {
            selectedRat.setSubject(subject);
            selectedRat.setWeight(weight);
            selectedRat.setNotes(notes);
            selectedRat.setAdded(added);
            sqLiteManager.updateRatInDB(selectedRat);
        }
        finish();
    }

    public void deleteRat(View view) {
        selectedRat.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateRatInDB(selectedRat);
        finish();
    }



}