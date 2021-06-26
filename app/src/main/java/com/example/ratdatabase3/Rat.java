package com.example.ratdatabase3;

import java.util.ArrayList;
import java.util.Date;

public class Rat {


    public static final String TABLE_NAME = "rats";

    public static ArrayList<Rat> ratArrayList = new ArrayList<>();
    public static String RAT_EDIT_EXTRA = "ratEdit";

    private int id;
    private String subject;
    private String weight;
    private String notes;
    private Date deleted;
    private String added;

    public Rat(int id, String subject, String weight, String notes, String added, Date deleted)
    {
        this.id = id;
        this.subject = subject;
        this.weight = weight;
        this.notes = notes;
        this.added = added;
        this.deleted = deleted;
    }

    public Rat(int id, String subject, String weight, String notes, String added)
    {
        this.id = id;
        this.subject = subject;
        this.weight = weight;
        this.notes = notes;
        this.added = added;
        this.deleted = null;
    }


    public static Rat getRatForId(int passedRatID)
    {
        for (Rat rat : ratArrayList)
        {
            if(rat.getId() == passedRatID)
                return rat;
        }
        return null;
    }

    public static ArrayList<Rat> nonDeletedRats()
    {
        ArrayList<Rat> nonDeleted = new ArrayList<>();
        for (Rat rat : ratArrayList)
        {
            if(rat.getDeleted() == null)
                nonDeleted.add(rat);
        }
        return nonDeleted;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String date){
        this.added = added;
    }
}
