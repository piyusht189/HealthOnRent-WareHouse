package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String fetchedstring;
    RequestQueue requestQueue;
    private IntentIntegrator qrScan;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //intializing scan object
        qrScan = new IntentIntegrator(this);
        qrScan.setBeepEnabled(false);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }
    public void scan(View view){

        if(isNetworkAvailable()){
            flag="scan";
            qrScan.initiateScan();}else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void warehouseout(View view){
        if(isNetworkAvailable()){
            Intent intent = new Intent(MainActivity.this, WareHouseOut.class);
            startActivity(intent);

        }else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
    public void warehousein(View view){
        if(isNetworkAvailable()){

            flag="warehousein";
            qrScan.initiateScan();


        }else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendrepair(View view){
        if(isNetworkAvailable()){
            flag="repair";
            qrScan.initiateScan();
        }else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
    public void warehousetransfer(View view){
        if(isNetworkAvailable()){
               flag="warehousetransfer";
            qrScan.initiateScan();
        }else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
    public void storepickup(View view){
        if(isNetworkAvailable()){

            startActivity(new Intent(this,StorePickup.class));


        }else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
    public void rentcollection(View view){
        if(isNetworkAvailable()){

            startActivity(new Intent(this,RentCollection.class));


        }else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }

    }

    public void logout(View view){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Logout...");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want Log Out?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.delete);
        // Setting Icon to Dialog

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                editor.clear();
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

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

                    if(flag.equals("scan")) {
                        JSONObject params = new JSONObject();
                        try {
                            params.put("skuid", fetchedstring);

                        } catch (JSONException e) {

                        }
                        String load_url = "http://139.59.34.12/admin/app/warehouse/getavailability.php";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("data").equals("failed")) {
                                        Toast.makeText(MainActivity.this, "SKUID Not Found ! ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        JSONArray ar = response.getJSONArray("data");
                                        Intent intent = new Intent(MainActivity.this, Details.class);
                                        intent.putExtra("jsonArray", ar.toString());
                                        startActivity(intent);

                                    }
                                } catch (Exception e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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


                    if(flag.equals("warehousetransfer")){


                                        Intent intent = new Intent(MainActivity.this, WareHouseTransfer.class);

                                        intent.putExtra("skuid", fetchedstring);
                                        startActivity(intent);




                    }
                    if(flag.equals("repair")){
                        Intent intent = new Intent(MainActivity.this, SendRepair.class);

                        intent.putExtra("skuid", fetchedstring);
                        startActivity(intent);




                    }
                   if(flag.equals("warehousein")){

                       JSONObject params = new JSONObject();
                       try {
                           params.put("skuid", fetchedstring);

                       } catch (JSONException e) {

                       }
                       String load_url = "http://139.59.34.12/admin/app/warehouse/warehousein.php";

                       JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                           @SuppressLint("SetTextI18n")
                           @Override
                           public void onResponse(JSONObject response) {

                               try {
                                   if (response.getString("data").equals("warehousein")) {
                                       Toast.makeText(MainActivity.this, "Equipment Successfully IN !!", Toast.LENGTH_SHORT).show();
                                   } else if(response.getString("data").equals("orderin")){
                                       Toast.makeText(MainActivity.this, "Order Successfully Completed !", Toast.LENGTH_SHORT).show();

                                   }else if(response.getString("data").equals("failed")){
                                       Toast.makeText(MainActivity.this, "SKUID is IDLE !", Toast.LENGTH_SHORT).show();
                                   }else {
                                       Toast.makeText(MainActivity.this, "Server Error !", Toast.LENGTH_SHORT).show();
                                   }
                               } catch (Exception e) {

                               }
                           }
                       }, new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                               Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
