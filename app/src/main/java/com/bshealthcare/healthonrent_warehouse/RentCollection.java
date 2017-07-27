package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class RentCollection extends AppCompatActivity {
 EditText oidsearch;
    ListView lv;
    RequestQueue requestQueue;
    private String[] oid,rs,invno,address,type,phone,name,due;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_collection);
        oidsearch=(EditText) findViewById(R.id.oidsearch);
        lv=(ListView) findViewById(R.id.lv);
        requestQueue = Volley.newRequestQueue(RentCollection.this);



    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }
    public void search(View view){

        if(oidsearch.getText().toString().equals("")){
            Toast.makeText(this, "Enter Oid First !", Toast.LENGTH_SHORT).show();
        }else {

            if(isNetworkAvailable()){



                JSONObject params = new JSONObject();
                try {
                    params.put("oid", oidsearch.getText().toString());
                } catch (JSONException e) {

                }
                String load_url = "http://139.59.34.12/admin/app/warehouse/oidgetrentcollection.php";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getString("data").equals("failed")){
                                Toast.makeText(RentCollection.this, "Entered O-id didnt found !", Toast.LENGTH_SHORT).show();
                            }else{
                            JSONArray arr=response.getJSONArray("data");
                            oid=new String[arr.length()];
                            rs=new String[arr.length()];
                            invno=new String[arr.length()];
                            address=new String[arr.length()];
                            due=new String[arr.length()];
                            type=new String[arr.length()];
                            phone=new String[arr.length()];
                            name=new String[arr.length()];
                            for(int k=0;k<arr.length();k++){
                                JSONObject obj=arr.getJSONObject(k);
                                rs[k]=obj.getString("total");
                                invno[k]=obj.getString("invoice_no");
                                oid[k]=obj.getString("oid");
                                address[k]=obj.getString("address");
                                type[k]=obj.getString("type");
                                phone[k]=obj.getString("phone");
                                name[k]=obj.getString("name");
                                due[k]=obj.getString("due");
                            }
                            MyRentCollection adp=new MyRentCollection(RentCollection.this,oid,rs,invno,address,type,name);
                            lv.setAdapter(adp);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent g = new Intent(RentCollection.this, RentCollectionDetails.class);
                                    g.putExtra("rs", rs[position]);
                                    g.putExtra("invno", invno[position]);
                                    g.putExtra("oid", oid[position]);
                                    g.putExtra("address", address[position]);
                                    g.putExtra("type", type[position]);
                                    g.putExtra("phone", phone[position]);
                                    g.putExtra("name", name[position]);
                                    g.putExtra("due", due[position]);
                                    startActivity(g);
                                }

                            });
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RentCollection.this, error.toString(), Toast.LENGTH_SHORT).show();
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






            }else{
                Toast.makeText(this, "Internet Not Connected!", Toast.LENGTH_SHORT).show();
            }


        }

    }
    public class MyRentCollection extends ArrayAdapter<String> {

        private Activity context;
        private String[] oid,rs,invno,address,type;
        private TextView oidv,rsv,invnov,addressv,typev;


        public MyRentCollection(Activity context, String[] oid, String[] rs,String[] invno,String[] address,String[] type,String[] name) {
            super(context, R.layout.singlerentcollection, name);
            this.context = context;
            this.oid = oid;
            this.rs=rs;
            this.invno=invno;
            this.address=address;
            this.type=type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"}) View view = inflater.inflate(R.layout.singlerentcollection, null, true);
            //listview implementation
            oidv = (TextView) view.findViewById(R.id.oid);
            rsv=(TextView) view.findViewById(R.id.rs);
            invnov=(TextView) view.findViewById(R.id.invno);
            addressv=(TextView) view.findViewById(R.id.address);
            typev=(TextView) view.findViewById(R.id.type);

            oidv.setText(oid[position]);


            rsv.setText("Rs. "+rs[position]);
            invnov.setText(invno[position]);
            addressv.setText(address[position]);
            typev.setText(type[position]);
            if(type[position].equals("collection")){
                typev.setBackgroundColor(Color.GREEN);
                typev.setTextColor(Color.BLACK);
            }else {
                typev.setBackgroundColor(Color.BLUE);
                typev.setTextColor(Color.BLACK);
            }
            return view;
        }
    }

}
