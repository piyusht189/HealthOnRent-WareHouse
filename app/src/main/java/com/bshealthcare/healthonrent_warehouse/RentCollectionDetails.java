package com.bshealthcare.healthonrent_warehouse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RentCollectionDetails extends AppCompatActivity {
TextView oidt,due,type,name,address,phone,rs,invno;
    Button drop;
    String rss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_collection_details);
        Intent g=getIntent();
        oidt=(TextView) findViewById(R.id.oid);
        type=(TextView) findViewById(R.id.type);
        name=(TextView) findViewById(R.id.name);
        due=(TextView) findViewById(R.id.due);
        address=(TextView) findViewById(R.id.address);
        phone=(TextView) findViewById(R.id.phone);
        rs=(TextView) findViewById(R.id.rs);
        invno=(TextView) findViewById(R.id.invno);
        drop=(Button) findViewById(R.id.drop);
        if(g.getStringExtra("type").equals("collection")){
            type.setBackgroundColor(Color.GREEN);
            type.setTextColor(Color.BLACK);
            drop.setText("Collect Money");
            drop.setTextColor(Color.BLACK);
            drop.setBackgroundColor(Color.GREEN);
        }else {
            type.setBackgroundColor(Color.BLUE);
            type.setTextColor(Color.BLACK);
            drop.setText("Refund Money");
            drop.setTextColor(Color.BLACK);
            drop.setBackgroundColor(Color.BLUE);
        }
         oidt.setText(g.getStringExtra("oid"));
         type.setText(g.getStringExtra("type"));
         name.setText(g.getStringExtra("name"));
        rss=g.getStringExtra("rs");
         address.setText(g.getStringExtra("address"));
         phone.setText(g.getStringExtra("phone"));
        int a=Integer.parseInt(g.getStringExtra("rs"));
        int b=Integer.parseInt(g.getStringExtra("due"));
        int c=a-b;
         rs.setText("Rs. "+String.valueOf(c));
        due.setText("Rs."+g.getStringExtra("due"));
         invno.setText(g.getStringExtra("invno"));
    }

    public void drop(View view){

        Intent g=new Intent(RentCollectionDetails.this,CollectMoney.class);
        g.putExtra("rs",rss);
        g.putExtra("invno",invno.getText().toString());
        g.putExtra("oid",oidt.getText().toString());
        g.putExtra("address",address.getText().toString());
        g.putExtra("type",type.getText().toString());
        g.putExtra("phone",phone.getText().toString());
        g.putExtra("name",name.getText().toString());
        startActivity(g);

    }
}
