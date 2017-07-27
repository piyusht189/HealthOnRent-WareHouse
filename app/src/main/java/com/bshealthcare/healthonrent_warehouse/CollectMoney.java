package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;



public class CollectMoney extends AppCompatActivity {
TextView cm;
    Spinner spn;
    private RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;
    EditText given;
    View customView;
    private static final int CAMERA_REQUEST = 1888;
    private IntentIntegrator qrScan;
    String fetchedstring;
    EditText givenam;
    TextView discount,photo;
    Bitmap photor;
    String invno;
    TextView okbutton,cancelbutton;
    TextView tot;
    String oid;
    RequestQueue requestQueue;
    String paymentphoto="no";
    String[] spinnerArray={"-Payments-","Cash","Card","Cheque"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_money);
        Intent g=getIntent();
        oid=g.getStringExtra("oid");
        invno=g.getStringExtra("invno");
        cm=(TextView) findViewById(R.id.typo);
        given=(EditText) findViewById(R.id.given);
        discount=(TextView) findViewById(R.id.discount);
        tot=(TextView) findViewById(R.id.total);
      photo=(TextView) findViewById(R.id.photo);
        requestQueue = Volley.newRequestQueue(this);
        spn=(Spinner) findViewById(R.id.spinner);
        mRelativeLayout=(RelativeLayout) findViewById(R.id.ls);
        tot.setText(g.getStringExtra("rs"));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(spinnerArrayAdapter);

        if(g.getStringExtra("type").equals("collection")){
            cm.setText("Collect Money");

        }else {
            cm.setText("Refund Money");
        }
        given.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(LAYOUT_INFLATER_SERVICE);

                customView = inflater.inflate(R.layout.customgiven,null);


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
                givenam=(EditText) customView.findViewById(R.id.givenamt);
                okbutton=(TextView) customView.findViewById(R.id.set);
                cancelbutton=(TextView) customView.findViewById(R.id.cancelbutton);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });

                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);



                okbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //   Toast.makeText(CreateZone.this,zonename.getText().toString(), Toast.LENGTH_SHORT).show();
                        given.setText(givenam.getText().toString());
                        int a=Integer.parseInt(givenam.getText().toString());
                        int b=Integer.parseInt(tot.getText().toString());
                        int c=b-a;
                        discount.setText(String.valueOf(c));
                         given.setEnabled(false);
                        mPopupWindow.dismiss();
                    }

                });


                cancelbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();

                    }
                });



            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photor = (Bitmap) data.getExtras().get("data");
            photo.setText("Uploaded");
            photo.setBackgroundColor(Color.BLUE);

        }
    }
    private String getStringFromBitmap(Bitmap bitmapPicture) {
 /*
 * This functions converts Bitmap picture to a string which can be
 * JSONified.
 * */
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
    public void done(View view){
        if(spn.getSelectedItem().toString().equals("-Payments-")){
            Toast.makeText(this, "Set Payments Method First!", Toast.LENGTH_SHORT).show();
        }else {
            if(given.getText().toString().equals("Set")){
                Toast.makeText(this, "Input Received Money !", Toast.LENGTH_SHORT).show();
            }else {
                if (photo.getText().toString().equals("Uploaded")) {
                  paymentphoto=getStringFromBitmap(photor);
                }

                JSONObject params = new JSONObject();
                try {
                    params.put("oid",oid);
                    params.put("received",given.getText().toString());
                    params.put("invno",invno);
                    params.put("paymentmethod",spn.getSelectedItem().toString());
                    params.put("photo",paymentphoto);

                } catch (JSONException e) {

                }
                String load_url = "http://139.59.34.12/admin/app/warehouse/collectrefund.php";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("data").equals("done")) {
                                Toast.makeText(CollectMoney.this, "Successfully Transacted!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CollectMoney.this,MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(CollectMoney.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CollectMoney.this, error.toString(), Toast.LENGTH_SHORT).show();
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
    }

}
