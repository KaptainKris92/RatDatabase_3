package com.example.ratdatabase3;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLiteManager extends SQLiteOpenHelper {

    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "RatDB";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "Rat3";
    private static final String COUNTER = "Counter";

    private static final String ID_FIELD = "id";
    private static final String SUBJECT_FIELD = "subject";
    private static final String WEIGHT_FIELD = "weight";
    private static final String NOTES_FIELD = "notes";
    private static final String ADDED_FIELD = "added";
    private static final String DELETED_FIELD = "deleted";

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");


    public SQLiteManager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteManager instanceOfDatabase(Context context)
    {
        if(sqLiteManager == null)
            sqLiteManager = new SQLiteManager(context);

        return sqLiteManager;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        StringBuilder sql;
        sql = new StringBuilder()
                .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COUNTER)
                .append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
                .append(ID_FIELD)
                .append(" INT, ")
                .append(SUBJECT_FIELD)
                .append(" TEXT, ")
                .append(WEIGHT_FIELD)
                .append(" TEXT, ")
                .append(NOTES_FIELD)
                .append(" TEXT, ")
                .append(ADDED_FIELD)
                .append(" DATETIME DEFAULT CURRENT_TIMESTAMP, ")
                .append(DELETED_FIELD)
                .append(" TEXT)");

        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Rat.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void addRatToDatabase (Rat rat)
    {
    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, rat.getId());
        contentValues.put(SUBJECT_FIELD, rat.getSubject());
        contentValues.put(WEIGHT_FIELD, rat.getWeight());
        contentValues.put(NOTES_FIELD, rat.getNotes());
        contentValues.put(ADDED_FIELD, rat.getAdded());
        contentValues.put(DELETED_FIELD, getStringFromDate(rat.getDeleted()));

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    public void populateRatListArray()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null))
        {
            if(result.getCount() != 0)
            {
                while (result.moveToNext())
                {
                    int id = result.getInt(1);
                    String subject = result.getString(2);
                    String weight = result.getString(3);
                    String notes = result.getString(4);
                    String added = result.getString(5);
                    String stringDeleted = result.getString(6);
                    Date deleted = getDateFromString(stringDeleted);
                    Rat rat = new Rat(id,subject,weight,notes,added,deleted);
                    Rat.ratArrayList.add(rat);
                }
            }
        }
    }

    public void updateRatInDB(Rat rat) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_FIELD, rat.getId());
        contentValues.put(SUBJECT_FIELD, rat.getSubject());
        contentValues.put(WEIGHT_FIELD, rat.getWeight());
        contentValues.put(NOTES_FIELD, rat.getNotes());
        contentValues.put(ADDED_FIELD, rat.getAdded());
        contentValues.put(DELETED_FIELD, getStringFromDate(rat.getDeleted()));

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID_FIELD + " =? ", new String[]{String.valueOf(rat.getId())});
    }


    private String getStringFromDate(Date date)
    {
        if(date == null)
         return null;
        return dateFormat.format(date);
    }

    private java.util.Date getDateFromString(String string) {
        try {
            return dateFormat.parse(string);
        } catch (ParseException | NullPointerException e) {
            return null;
        }

    }

}
