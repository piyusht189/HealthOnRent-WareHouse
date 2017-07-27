package com.bshealthcare.healthonrent_warehouse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class StorePickupDetails extends AppCompatActivity {
    RadioGroup rg;
    TextView description,scan,skip;
    String fetchedstring;
    String skuid,oid;
    RadioButton type;
    TextView done;
    private PopupWindow mPopupWindow;
    int h,c;
    String givenas;
    Bitmap photor;
    Spinner spn;
    String status="unpaid";
    EditText givenam;
    View customView;
    String[] skuidl,depositl;
    String flag;
    List<Integer> mutex;
    String photopayment;
    int pos=0;
    int semantic=0;
    EditText given;
    TextView okbutton,cancelbutton,discount;
    int minVal=0;
    int tot=0;
    String did="2";
    ListView lv;
    private RelativeLayout mRelativeLayout;
    private static final int CAMERA_REQUEST = 1888;
    TextView photo,total;
    RequestQueue requestQueue;
    String[] spinnerArray={"-Payments-","Cash","Card","Cheque"};
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_pickup_details);
        qrScan = new IntentIntegrator(this);
        qrScan.setBeepEnabled(false);
        requestQueue = Volley.newRequestQueue(this);
        Intent g=getIntent();
        total=(TextView) findViewById(R.id.total);
        oid=g.getStringExtra("oid");
        skuid=g.getStringExtra("skuid");
        lv=(ListView) findViewById(R.id.lv);
        mutex=new ArrayList<Integer>();

        spn=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(spinnerArrayAdapter);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.ls);
        photo=(TextView) findViewById(R.id.photo);
        discount=(TextView) findViewById(R.id.discount);
        given=(EditText) findViewById(R.id.given);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });


        JSONObject params = new JSONObject();
        try {
            params.put("oid", oid);
        } catch (JSONException e) {

        }
        String load_url = "http://139.59.34.12/admin/app/warehouse/oidgetall.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray arr=response.getJSONArray("data");
                    skuidl=new String[arr.length()];
                    depositl=new String[arr.length()];
                    for(int k=0;k<arr.length();k++){
                        JSONObject ob=arr.getJSONObject(k);
                        skuidl[k]=ob.getString("skuid");
                        depositl[k]=ob.getString("deposit");
                        tot=tot+Integer.valueOf(ob.getString("deposit"));
                    }

                    Arrays.sort(depositl, Collections.reverseOrder());
                    MySkuidAreanListAdapter adp=new MySkuidAreanListAdapter(StorePickupDetails.this,skuidl,depositl);
                    lv.setAdapter(adp);

                    lv.setFastScrollEnabled(true);
                    adp.notifyDataSetChanged();
                    total.setText(String.valueOf(tot));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View vv,int position, long arg3)
                        {

                            CheckBox cb=(CheckBox) vv.findViewById(R.id.skuidcheck);
                            TextView dep=(TextView) vv.findViewById(R.id.deposit);
                            TextView skuid=(TextView) vv.findViewById(R.id.skuid);
                            if(!cb.isChecked()){
                                if(Integer.parseInt(dep.getText().toString()) == 0){

                                    flag=skuid.getText().toString();
                                    mutex.add(position);

                                    pos=position;
                                    qrScan.initiateScan();


                                }else {
                                    if (given.getText().toString().equals("Set")) {
                                        Toast.makeText(StorePickupDetails.this, "Enter Given Amount First !", Toast.LENGTH_SHORT).show();
                                        cb.setChecked(false);
                                    } else {
                                        int a = Integer.parseInt(given.getText().toString());
                                        int b = Integer.parseInt(dep.getText().toString());
                                        c = a - b;
                                        if (c < 0) {
                                            Toast.makeText(StorePickupDetails.this, "Given Amount Should be more than Delivery !", Toast.LENGTH_SHORT).show();
                                            cb.setChecked(false);
                                            //given.setText("0");
                                        } else {
                                            given.setText(String.valueOf(c));
                                            flag=skuid.getText().toString();
                                            mutex.add(position);
                                            pos=position;
                                            qrScan.initiateScan();
                                        }
                                    }
                                }
                            }else {
                                if(Integer.parseInt(dep.getText().toString()) == 0){

                                    dep.setTextColor(Color.RED);

                                }else {
                                    int a = Integer.parseInt(given.getText().toString());
                                    int b = Integer.parseInt(dep.getText().toString());
                                    c = a + b;
                                    given.setText(String.valueOf(c));
                                    dep.setTextColor(Color.RED);
                                }
                                cb.setChecked(false);
                            }



                        }
                    });

                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StorePickupDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    /*    skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Payments.this);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Skip ?");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to Skip this Payment?");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.skip);
                // Setting Icon to Dialog

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Skip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        startActivity(new Intent(Payments.this,Signature.class));
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
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId=rg.getCheckedRadioButtonId();
                type=(RadioButton) findViewById(selectedId);
            if(!String.valueOf(selectedId).equals("-1")) {

                qrScan.initiateScan();

            }else {
                Toast.makeText(Payments.this, "Payment Maethod Not Selected!", Toast.LENGTH_SHORT).show();
            }
            }
        });
*/



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
                        int b=Integer.parseInt(total.getText().toString());
                        int c=b-a;
                        discount.setText(String.valueOf(c));

                        View vs=getViewByPosition(depositl.length-1,lv);
                        TextView dep=(TextView) vs.findViewById(R.id.deposit);
                        int s=Integer.parseInt(dep.getText().toString());
                        dep.setText(String.valueOf(s-c));
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

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photor = (Bitmap) data.getExtras().get("data");
            photo.setText("Uploaded");
            photo.setBackgroundColor(Color.BLUE);

        }

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {
                    fetchedstring=  result.getContents();

                    if(fetchedstring.equals(flag)) {
                        Toast.makeText(this, "Matched", Toast.LENGTH_SHORT).show();


                        View v=getViewByPosition(pos,lv);
                        TextView sku=(TextView) v.findViewById(R.id.skuid);
                        TextView depo=(TextView) v.findViewById(R.id.deposit);

                        JSONObject params = new JSONObject();
                        try {
                            params.put("skuid", sku.getText().toString());
                            params.put("depo",depo.getText().toString());

                        } catch (JSONException e) {

                        }
                        String load_url = "http://139.59.34.12/admin/app/warehouse/skuidstart.php";
                        Toast.makeText(this, sku.getText().toString()+depo.getText().toString(), Toast.LENGTH_SHORT).show();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("data").equals("done")) {
                                        Toast.makeText(StorePickupDetails.this, "Successfully Started", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mutex.remove(mutex.size()-1);
                                        Toast.makeText(StorePickupDetails.this, "Mutex removed ", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(StorePickupDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mutex.remove(mutex.size()-1);
                                Toast.makeText(StorePickupDetails.this, "Mutex removed error ", Toast.LENGTH_SHORT).show();
                                Toast.makeText(StorePickupDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Skuid Not Matched ! Check Your Eyes !", Toast.LENGTH_SHORT).show();
                        mutex.remove(mutex.size()-1);
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


    public class MySkuidAreanListAdapter extends ArrayAdapter<String> {

        private Activity context;
        private String[] skuid,deposit;
        private TextView skuidv,depositv;
        private CheckBox checkdeposit;

        public MySkuidAreanListAdapter(Activity context, String[] skuid, String[] deposit) {
            super(context, R.layout.singleskuidpayment, deposit);
            this.context = context;
            this.skuid = skuid;
            this.deposit= deposit;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"}) View view = inflater.inflate(R.layout.singleskuidpayment, null, true);
            //listview implementation

            skuidv = (TextView) view.findViewById(R.id.skuid);
            depositv = (TextView) view.findViewById(R.id.deposit);
            checkdeposit = (CheckBox) view.findViewById(R.id.skuidcheck);


            skuidv.setText(skuid[position]);
            depositv.setText(deposit[position]);
            if(mutex.contains(position)){
                depositv.setTextColor(Color.BLUE);
                checkdeposit.setChecked(true);
            }



            return view;

        }
    }

    public void done(View view){
        if(spn.getSelectedItem().toString().equals("-Payments-")){
            Toast.makeText(this, "Kindly Set Payment method!", Toast.LENGTH_SHORT).show();
        }else{
                if(photo.getText().toString().equals("Uploaded")){
                    photopayment=getStringFromBitmap(photor);
                }else {
                    photopayment="no";
                }

                JSONObject params = new JSONObject();
                try {
                    params.put("photo", photopayment);
                    params.put("oid", oid);
                    params.put("payment",spn.getSelectedItem().toString());

                } catch (JSONException e) {

                }
                String load_url = "http://139.59.34.12/admin/app/warehouse/done.php";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, load_url, params, new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("data").equals("done")) {
                                Toast.makeText(StorePickupDetails.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(StorePickupDetails.this,MainActivity.class));
                                finish();
                            } else {

                                Toast.makeText(StorePickupDetails.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StorePickupDetails.this, error.toString(), Toast.LENGTH_SHORT).show();
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
