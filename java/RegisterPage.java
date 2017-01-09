package com.example.munna.shopkeeperapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.toolbox.Volley;

    import org.json.JSONException;
    import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class RegisterPage extends AppCompatActivity {

        private EditText rmail;
        private EditText rpass;
        private EditText rcontact;
        private EditText rshopName;
        private Button rbtn;
    private Button rgps;
    Double lat;
    Double lng;
    private  EditText rzip;
    SessionManager session;
    EditText rloc;
    AlertDialog.Builder builder;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater blowup=getMenuInflater();
        blowup.inflate(R.menu.menu1, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.AboutUs:
                Intent i= new Intent(RegisterPage.this,aboutus.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_page);
        builder = new AlertDialog.Builder(RegisterPage.this);

            rmail = (EditText) findViewById(R.id.rmail);
            rpass = (EditText) findViewById(R.id.rpass);
            rcontact = (EditText) findViewById(R.id.rphone);
            rshopName = (EditText) findViewById(R.id.Shopname);
            rzip=(EditText)findViewById(R.id.zipcodeText);
            rloc=(EditText)findViewById(R.id.rloc);
        rbtn = (Button) findViewById(R.id.rbtn);
        session = new SessionManager(getApplicationContext());
        rmail.setText("");
        rpass.setText("");
        rcontact.setText("");
        rzip.setText("");
        rshopName.setText("");
        i=0;


    }
   int i=0;
    public void CheckEditText(View v){
        if((rmail.getText().toString()).matches("")){
            i=1;
        }
        if((rpass.getText().toString()).matches("")){
            i=1;
        }
        if((rcontact.getText().toString()).matches("")){
            i=1;
        }
        if((rshopName.getText().toString()).matches("")){
            i=1;
        }
        if((rzip.getText().toString()).matches("")){
            i=1;
        }
        Register();
    }


    public void getGpsLoc(){
        final Geocoder geocoder = new Geocoder(this);
        final String zip =rzip.getText().toString();
        try {
            List<Address> addresses = geocoder.getFromLocationName(zip, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                 lat = address.getLatitude();
                 lng=address.getLongitude();

                } else {
                builder.setMessage("Unable to get exact location from zipcode... Please enter a valid zipcode ")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
                       }
        } catch (IOException e) {}
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile =false ;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    ProgressDialog prg;
        public void Register() {

            getGpsLoc();
            final String email = rmail.getText().toString();
            final String password = rpass.getText().toString();
            final String number = rcontact.getText().toString();
            final String shopname = rshopName.getText().toString();
            final String loc = rloc.getText().toString();

            if (i == 0) {
                boolean network = haveNetworkConnection();
                if (network) {

                    prg = ProgressDialog.show(RegisterPage.this, "sending data", "Contacting Server please wait .... ", true);
                    final Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            response = response.replaceFirst("<font>.*?</font>", "");
                            int jsonStart = response.indexOf("{");
                            int jsonEnd = response.lastIndexOf("}");

                            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                                response = response.substring(jsonStart, jsonEnd + 1);
                            } else {
                                // deal with the absence of JSON content here
                            }

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {

                                    session.createLoginSession(email, number, shopname);

                                    prg.dismiss();
                                    Intent intent = new Intent(RegisterPage.this, UserAreaActivity.class);
                                    startActivity(intent);

                                } else {
                                    prg.dismiss();
                                    builder.setMessage("Register Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(email, password, number, shopname, lat, lng, loc, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterPage.this);
                    queue.add(registerRequest);


                } else {
                    builder.setMessage("Please Check Your Network ")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }

            } else {
                builder.setMessage("Please fill all fields ")
                        .create()
                        .show();
            }

        }

}



