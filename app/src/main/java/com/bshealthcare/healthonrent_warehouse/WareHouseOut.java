package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WareHouseOut extends AppCompatActivity {
ListView lv;
    private RequestQueue requestQueue;
    String[] skuid,oid,db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_house_out);
        lv=(ListView) findViewById(R.id.lv);
        requestQueue = Volley.newRequestQueue(WareHouseOut.this);

        JSONObject params = new JSONObject();
        String load_url = "http://139.59.34.12/admin/app/warehouse/fetchorders.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                  JSONArray arr=response.getJSONArray("data");
                    skuid=new String[arr.length()];
                    oid=new  String[arr.length()];
                    db=new String[arr.length()];
                    for(int i=0;i<arr.length();i++)
                    {
                        JSONObject ob=arr.getJSONObject(i);
                        skuid[i]=ob.getString("skuid");
                        oid[i]=ob.getString("oid");
                        db[i]=ob.getString("db");
                    }
                  MyOrdersListAdapter adp=new MyOrdersListAdapter(WareHouseOut.this,oid,skuid,db);
                    lv.setAdapter(adp);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            TextView oidtv=(TextView) view.findViewById(R.id.oid);
                            TextView skuidtv=(TextView) view.findViewById(R.id.skuid);
                            TextView dbtv=(TextView) view.findViewById(R.id.db);
                            Intent g=new Intent(WareHouseOut.this,WarehouseOutDetails.class);
                            g.putExtra("oid",oidtv.getText().toString());
                            g.putExtra("skuid",skuidtv.getText().toString());
                            g.putExtra("db",dbtv.getText().toString());
                            startActivity(g);
                            finish();
                        }
                    });

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WareHouseOut.this, error.toString(), Toast.LENGTH_SHORT).show();
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
