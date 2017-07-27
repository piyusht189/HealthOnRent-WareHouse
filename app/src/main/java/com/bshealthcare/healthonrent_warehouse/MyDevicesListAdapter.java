package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by 1415044 on 26-03-2017.
 */

public class MyDevicesListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] dname;
    private TextView textViewdname;


    public MyDevicesListAdapter(Activity context, String[] dname) {
        super(context, R.layout.singledevice, dname);
        this.context = context;
        this.dname = dname;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"InflateParams", "ViewHolder"}) View view = inflater.inflate(R.layout.singledevice, null, true);
        //listview implementation
        textViewdname = (TextView) view.findViewById(R.id.dname);


        textViewdname.setText(dname[position]);


        return view;
    }
}
