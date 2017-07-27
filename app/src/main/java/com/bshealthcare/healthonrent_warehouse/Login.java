package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class Login extends AppCompatActivity {
    TextView username,pass;
    RequestQueue requestQueue;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(TextView) findViewById(R.id.username);
        pass=(TextView) findViewById(R.id.password);
        requestQueue = Volley.newRequestQueue(this);
        sharedpreferences = getSharedPreferences("auth", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        if(sharedpreferences.contains("hid")){

            startActivity(new Intent(this,MainActivity.class));
            finish();

        }
    }

    public void login(View view){
        if(!pass.getText().toString().equals("") && !username.getText().toString().equals("")) {
            JSONObject params = new JSONObject();
            try {
                params.put("password", pass.getText().toString());
                params.put("username", username.getText().toString());

            } catch (JSONException e) {

            }
            String load_url = "http://139.59.34.12/admin/app/warehouse/warehouselogin.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getString("data").equals("done")) {
                            editor.putString("hid","1");
                            editor.commit();
                            startActivity(new Intent(Login.this,MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Invalid Phone/Password !", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        }else {
            Toast.makeText(this, "Phone/Password Empty!", Toast.LENGTH_SHORT).show();
        }



    }
}
