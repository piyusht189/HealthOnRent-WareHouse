package com.bshealthcare.healthonrent_warehouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class Details extends AppCompatActivity {
    TextView skuid,availability,end,start,warehouse,custphone,custaddress,custemail,custname,oid,s_p,s_l,st_p,st_l,l_p,l_l,salesprice,deposit,pcategory,pname;
    JSONArray array;
    ProgressDialog pDialog;
    RelativeLayout orderwala;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        orderwala=(RelativeLayout) findViewById(R.id.orderwala);

        Intent g = getIntent();
        String jsonArray = g.getStringExtra("jsonArray");
        skuid = (TextView) findViewById(R.id.skuid);
        availability=(TextView) findViewById(R.id.availability);
        end=(TextView) findViewById(R.id.end);
        start=(TextView) findViewById(R.id.start);
        custphone=(TextView) findViewById(R.id.custphone);
        custaddress=(TextView) findViewById(R.id.custaddress);
        custemail=(TextView) findViewById(R.id.custemail);
        custname=(TextView) findViewById(R.id.custname);
        oid=(TextView) findViewById(R.id.oid);
        s_p=(TextView) findViewById(R.id.s_p);
        s_l=(TextView) findViewById(R.id.s_l);
        st_l=(TextView) findViewById(R.id.st_l);
        st_p=(TextView) findViewById(R.id.st_p);
        l_p=(TextView) findViewById(R.id.l_p);
        l_l=(TextView) findViewById(R.id.l_l);
        salesprice=(TextView) findViewById(R.id.salesprice);
        deposit=(TextView) findViewById(R.id.deposit);
        pcategory=(TextView) findViewById(R.id.pcategory);
        pname=(TextView) findViewById(R.id.pname);
        warehouse=(TextView) findViewById(R.id.warehouse);



        try {
            array = new JSONArray(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
      new AttemptProcess().execute();

    }


    class AttemptProcess extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Details.this);
            pDialog.setMessage("Logging in....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

       @Override
        protected String doInBackground(String... args) {




            return null;
        }

        protected void onPostExecute(String message) {
           if(String.valueOf(array.length()).equals("3")){
                orderwala.setVisibility(View.GONE);
               availability.setText("Available");
               skuid.setBackgroundColor(getResources().getColor(R.color.mine));
               availability.setBackgroundColor(getResources().getColor(R.color.green));
               try{
                    JSONObject ob=array.getJSONObject(0);
                    skuid.setText(ob.getString("skuid"));
                    warehouse.setText(ob.getString("warehouse"));
                    ob=array.getJSONObject(1);
                    pname.setText(ob.getString("pname"));
                    pcategory.setText(ob.getString("pcategory"));
                   ob=array.getJSONObject(2);
                    salesprice.setText(ob.getString("psales_price"));
                    deposit.setText(ob.getString("pdeposit"));
                    s_p.setText(ob.getString("pshort"));
                    s_l.setText(ob.getString("plockin_s"));
                    st_p.setText(ob.getString("pstandard"));
                     st_l.setText(ob.getString("plockin_m"));
                    l_p.setText(ob.getString("plong"));
                    l_l.setText(ob.getString("plockin_l"));

               }catch (JSONException e){}



           }else if(String.valueOf(array.length()).equals("4")){
                 warehouse.setVisibility(View.GONE);
               availability.setText("Booked");
               availability.setBackgroundColor(getResources().getColor(R.color.red));
               skuid.setBackgroundColor(getResources().getColor(R.color.mine));
              try{
                  JSONObject ob=array.getJSONObject(0);
                  skuid.setText(ob.getString("skuid"));
                  ob=array.getJSONObject(1);
                  oid.setText(ob.getString("oid"));
                  custname.setText(ob.getString("name"));
                  custemail.setText(ob.getString("email"));
                  custphone.setText(ob.getString("phone"));
                  custaddress.setText(ob.getString("saddress"));
                  start.setText(ob.getString("start"));
                  end.setText(ob.getString("next"));
                  ob=array.getJSONObject(2);
                  pname.setText(ob.getString("pname"));
                  pcategory.setText(ob.getString("pcategory"));
                  ob=array.getJSONObject(3);
                  salesprice.setText(ob.getString("psales_price"));
                  deposit.setText(ob.getString("pdeposit"));
                  s_p.setText(ob.getString("pshort"));
                  s_l.setText(ob.getString("plockin_s"));
                  st_p.setText(ob.getString("pstandard"));
                  st_l.setText(ob.getString("plockin_m"));
                  l_p.setText(ob.getString("plong"));
                  l_l.setText(ob.getString("plockin_l"));
              }catch (JSONException e){}
           }
            pDialog.dismiss();


        }
    }
}
