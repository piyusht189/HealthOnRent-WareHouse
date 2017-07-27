package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendRepair extends AppCompatActivity {
TextView sk;
    String skuid;
    RequestQueue requestQueue;
    String load_url = "http://139.59.34.12/admin/app/warehouse/sendrepair.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_repair);
        Intent g=getIntent();
        skuid=g.getStringExtra("skuid");
        sk=(TextView) findViewById(R.id.skuid);
        sk.setText(skuid);

        requestQueue = Volley.newRequestQueue(this);

    }

    public void cancel(View view){
        startActivity(new Intent(SendRepair.this,MainActivity.class));
        finish();
    }


    public void send(View view){


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
               if (response.getString("data").equals("done")) {
                   Toast.makeText(SendRepair.this, "Success, Sent for repair !", Toast.LENGTH_SHORT).show();

               }else{
                   Toast.makeText(SendRepair.this, "Skuid Not Found! Be Aware ! Dont Sleep !", Toast.LENGTH_SHORT).show();
               }
           }catch (JSONException e){

           }
            startActivity(new Intent(SendRepair.this,MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SendRepair.this, error.toString(), Toast.LENGTH_SHORT).show();
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
