package com.example.ratdatabase3;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.ajts.androidmads.library.SQLiteToExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BodyweightActivity extends AppCompatActivity {

    private static final String EMAIL = "krisadamatzky@yahoo.co.uk" ;
    private ListView ratListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rat_bodyweights);
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

    public void exportTable(View v){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source;
        FileChannel destination;
        String currentDBPath = "/data/"+ "com.example.ratdatabase3" +"/databases/"+"RatDB";
        String backupDBPath = "/Backup/"+ "RatDB";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File (sd, backupDBPath);

        String directory_path = sd.getPath() + "/Backup";

        File ratXLS = new File(directory_path, "Rats.xls");
        if (!ratXLS.exists()) {
            ratXLS.mkdirs();
        }

        // Export SQLite DB as EXCEL FILE
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), SQLiteManager.DATABASE_NAME, directory_path);
        sqliteToExcel.exportAllTables("Rats.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {            }
            @Override
            public void onCompleted(String filePath) {            }
            @Override
            public void onError(Exception e) {            }
        });

        try{
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            source.close();
            destination.close();
            Toast.makeText(this, "Rat table exported as .xls file", Toast.LENGTH_LONG).show();

            Calendar cal = Calendar.getInstance();
            DateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            String currentTime = sdf.format(cal.getTime());

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"krisadamatzky@yahoo.co.uk"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rat database for " + currentTime);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "The rat database exported on " + currentTime + " is attached");

            emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", ratXLS));
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(emailIntent, "Select App"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}