package com.androidbroadcast.reciever;

import android.content.Context;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Shish on 4/11/2019.
 */

public class MyAdapter extends ArrayAdapter<GooglePlace> {

    ArrayList<GooglePlace> placesList = new ArrayList<GooglePlace>();

    public MyAdapter(Context context, int textViewResourceId, ArrayList<GooglePlace> objects) {
        super(context, textViewResourceId, objects);
        placesList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row_layout, null);
        TextView textView1 = (TextView) v.findViewById(R.id.textView1);
        /*TextView textView2 = (TextView) v.findViewById(R.id.textView2);
        */TextView textView3 = (TextView) v.findViewById(R.id.textView3);
        textView1.setText(placesList.get(position).getName());
        //textView2.setText(placesList.get(position).getFormatted_address());
        textView3.setText(placesList.get(position).getCategory());
        return v;

    }

}