package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class WareHouseTransfer extends AppCompatActivity {
JSONArray array;
    String skuid;
    Spinner spinner;
    RequestQueue requestQueue;
    ProgressDialog pDialog;
    TextView sk;
    JSONArray ar;
    String result;
    JSONObject oo;
    String[] myarr;
   String warehouse;
      String wh;
    String load_url = "http://139.59.34.12/admin/app/warehouse/warehouses.php";
    String load_url2 = "http://139.59.34.12/admin/app/warehouse/warehousetransfer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_transfer);

        Intent g=getIntent();
        skuid=g.getStringExtra("skuid");
        sk=(TextView) findViewById(R.id.skuid);
        sk.setText(skuid);
        requestQueue = Volley.newRequestQueue(this);
        spinner=(Spinner) findViewById(R.id.myspinner);
        JSONObject params = new JSONObject();
        try {
            params.put("skuid", skuid);

        } catch (JSONException e) {

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {
                   ar = response.getJSONArray("data");
                    myarr=new String[ar.length()];
                    for(int i=0;i<ar.length();i++){
                        JSONObject on=ar.getJSONObject(i);
                        myarr[i]=on.getString("wname");
                    }

                } catch (Exception e) {

                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WareHouseTransfer.this, android.R.layout.simple_spinner_item, myarr);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);


                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                               wh=parent.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        wh=parent.getSelectedItem().toString();
                    }


                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WareHouseTransfer.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);









    }

   public void transfer(View view){
       JSONObject params = new JSONObject();
       try {
           params.put("skuid", skuid);
           params.put("warehouse",wh);

       } catch (JSONException e) {

       }

       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url2, params, new Response.Listener<JSONObject>() {
           @SuppressLint("SetTextI18n")
           @Override
           public void onResponse(JSONObject response) {

               try {
                  if(response.getString("data").equals("booked")){
                      result="Product Already Booked";
                  }else if (response.getString("data").equals("transferring")){
                      result="Product Already Set for a transfer";
                  }else if(response.getString("data").equals("done")){
                      result="Product Successfully Applied for Transfer to "+wh;
                  }else {
                      result="Skuid Not Correct, Check Your Eyes !";
                  }

               } catch (Exception e) {

               }
               Toast.makeText(WareHouseTransfer.this, result, Toast.LENGTH_SHORT).show();

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Toast.makeText(WareHouseTransfer.this, error.toString(), Toast.LENGTH_SHORT).show();
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> headers = new HashMap<>();
               headers.put("Content-Type", "application/json; charset=utf-8");
               return headers;
           }
       };
       requestQueue.add(jsonObjectRequest);




   }


}
