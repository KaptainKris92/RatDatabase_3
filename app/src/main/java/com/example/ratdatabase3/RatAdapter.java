package com.example.ratdatabase3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

public class RatAdapter extends ArrayAdapter<Rat>
{

    public RatAdapter(Context context, List<Rat> rats)
    {
        super(context, 0, rats);
    }

    static class ViewHolder {
        Button nextButton, previousButton;
    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Rat rat = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rat_cell, parent, false);
            ViewHolder h = new ViewHolder();
            h.nextButton = convertView.findViewById(R.id.nextButton);
            h.previousButton = convertView.findViewById(R.id.prevButton);

            TextView subject = convertView.findViewById(R.id.cellSubject);
            TextView weight = convertView.findViewById(R.id.cellWeight);
            TextView notes = convertView.findViewById(R.id.cellNotes);
            TextView added = convertView.findViewById(R.id.cellAdded);


            subject.setText(rat.getSubject());
            weight.setText(rat.getWeight());
            notes.setText(rat.getNotes());
            added.setText(rat.getAdded());

            convertView.setTag(h);

        }

        ViewHolder h = (ViewHolder) convertView.getTag();


        return convertView;
    }

}
