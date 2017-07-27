package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by SuperNova on 10-06-2017.
 */

public class MyOrdersListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private String[] oid,skuid,db;
    private TextView oidv,skuidv,dbv;


    public MyOrdersListAdapter(Activity context,String[] oid, String[] skuid,String[] db) {
        super(context, R.layout.singlewarehouseout, oid);
        this.context = context;
        this.oid = oid;
        this.skuid = skuid;
        this.db= db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"InflateParams", "ViewHolder"}) View view = inflater.inflate(R.layout.singlewarehouseout, null, true);
        //listview implementation
        oidv = (TextView) view.findViewById(R.id.oid);
        skuidv = (TextView) view.findViewById(R.id.skuid);
        dbv=(TextView) view.findViewById(R.id.db);

        oidv.setText(oid[position]);
        skuidv.setText(skuid[position]);
        dbv.setText(db[position]);


        return view;
    }
}
