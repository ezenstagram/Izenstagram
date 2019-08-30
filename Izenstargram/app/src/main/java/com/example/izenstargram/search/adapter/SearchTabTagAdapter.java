package com.example.izenstargram.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.izenstargram.R;
import com.example.izenstargram.search.model.AllTagDTO;

import java.util.List;

public class SearchTabTagAdapter extends ArrayAdapter<AllTagDTO> {


    Activity activity;
    int resource;

    public SearchTabTagAdapter(@NonNull Context context, int resource, @NonNull List<AllTagDTO> objects) {
        super(context, resource, objects);
        activity = (Activity) context;
        this.resource = resource;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(resource, null);

        }
        AllTagDTO item = getItem(position);
        if (item != null) {
            TextView textView = convertView.findViewById(R.id.textViewTagName);

            item.getTag_id();////////////
            textView.setText("#" + item.getTag_name());


        }


        return convertView;
    }



}

