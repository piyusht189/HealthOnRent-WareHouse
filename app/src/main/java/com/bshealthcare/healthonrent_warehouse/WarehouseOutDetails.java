package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseOutDetails extends AppCompatActivity {
TextView oid,uname,uphone,uemail,uaddress,pskuid,pname,db;
    private RequestQueue requestQueue;
    JSONObject ob;
    private PopupWindow mPopupWindow;
    ListView ls;
    private IntentIntegrator qrScan;
    MyDevicesListAdapter adp;
    String fetchedstring;
    String[] dbname;
    View customView;

    private LinearLayout mRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_out_details);
        Intent g=getIntent();
        requestQueue = Volley.newRequestQueue(WarehouseOutDetails.this);
        oid=(TextView) findViewById(R.id.oid);
        uname=(TextView) findViewById(R.id.uname);
        uphone=(TextView) findViewById(R.id.uphone);
        uemail=(TextView) findViewById(R.id.uemail);
        uaddress=(TextView) findViewById(R.id.uaddress);
        pskuid=(TextView) findViewById(R.id.pskuid);
        pname=(TextView) findViewById(R.id.pname);
        db=(TextView) findViewById(R.id.db);
        mRelativeLayout = (LinearLayout) findViewById(R.id.ls);
        oid.setText(g.getStringExtra("oid"));
        db.setText(g.getStringExtra("db"));
        pskuid.setText(g.getStringExtra("skuid"));
        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setBeepEnabled(false);
        JSONObject params = new JSONObject();
        try{
            params.put("skuid",g.getStringExtra("skuid"));

        }catch (JSONException e){}
        String load_url = "http://139.59.34.12/admin/app/warehouse/fetchordersskuid.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("data").equals("failed")){
                        Toast.makeText(WarehouseOutDetails.this, "Order is Invalid", Toast.LENGTH_SHORT).show();
                     }else{

                        JSONArray arr=response.getJSONArray("data");
                        ob=arr.getJSONObject(0);
                      //  Toast.makeText(WarehouseOutDetails.this, ob.toString(), Toast.LENGTH_LONG).show();

                        uname.setText("Name : "+ob.getString("name"));
                        uphone.setText(ob.getString("phone"));
                        uemail.setText(ob.getString("email"));
                        uaddress.setText("Address : "+ob.getString("saddress"));
                        pname.setText("Product : "+ob.getString("pname"));
                        db.setText(ob.getString("db"));

                    }

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WarehouseOutDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    public void scanme(View view){

        if(db.getText().toString().equals("")){
            Toast.makeText(this, "<- First Assign Delivery Boy !", Toast.LENGTH_SHORT).show();
        }else{

            qrScan.initiateScan();

        }
    }

    public  void assign(View view){
        LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);

        customView = inflater.inflate(R.layout.devicespopup,null);


        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        ImageButton closeButton=(ImageButton) customView.findViewById(R.id.ib_close);


        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });


        ls=(ListView) customView.findViewById(R.id.lv);


        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

        String load_url = "http://139.59.34.12/admin/app/warehouse/fetchdb.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {


                    JSONArray arr=response.getJSONArray("data");
                    dbname=new String[arr.length()];
                    for(int i=0;i<arr.length();i++){
                        JSONObject ob=arr.getJSONObject(i);
                        dbname[i]= ob.getString("dname");

                    }

                    adp=new MyDevicesListAdapter(WarehouseOutDetails.this,dbname);


                    ls.setAdapter(adp);
                    ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            final TextView dbnm=(TextView) view.findViewById(R.id.dname);
                            db.setText(dbnm.getText().toString());

                            JSONObject params = new JSONObject();
                            try{
                                params.put("db",dbnm.getText().toString());
                                params.put("skuid",pskuid.getText().toString());
                                params.put("oid",oid.getText().toString());

                            }catch (JSONException e){}
                            String load_url = "http://139.59.34.12/admin/app/warehouse/assigndeliveryboy.php";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if(response.getString("data").equals("done")) {
                                            Toast.makeText(WarehouseOutDetails.this, "Delivery Boy "+dbnm.getText().toString()+" assigned !", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(WarehouseOutDetails.this, "Internet Not Good", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {

                                    }
                                    mPopupWindow.dismiss();
                                    startActivity(new Intent(WarehouseOutDetails.this,WareHouseOut.class));
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(WarehouseOutDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                    });

                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(WarehouseOutDetails.this, error.toString(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WarehouseOutDetails.this,WareHouseOut.class));
        finish();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json

                    //setting values to textviews
                    fetchedstring=  result.getContents();
              if(fetchedstring.equals(pskuid.getText().toString())) {
                  JSONObject params = new JSONObject();
                  try {
                      params.put("oid", oid.getText().toString());
                      params.put("skuid", pskuid.getText().toString());
                  } catch (JSONException e) {
                  }
                  String load_url = "http://139.59.34.12/admin/app/warehouse/verifyorderout.php";
                  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                      @SuppressLint("SetTextI18n")
                      @Override
                      public void onResponse(JSONObject response) {
                          try {
                              if (response.getString("data").equals("done")) {
                                  Toast.makeText(WarehouseOutDetails.this, "Product Approved ! Product is ready for Take Away !", Toast.LENGTH_LONG).show();
                              } else {



                              }

                          } catch (Exception e) {

                          }

                      }
                  }, new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                          Toast.makeText(WarehouseOutDetails.this, error.toString(), Toast.LENGTH_LONG).show();
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
                  Toast.makeText(this, "SKUID Not Matched! Check Your Eyes !", Toast.LENGTH_SHORT).show();
              }

                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }


                //  Intent g=new Intent(this,Details.class);
                //     g.putExtra("skuid",fetchedstring);
                //  startActivity(g);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
