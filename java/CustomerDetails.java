package com.example.munna.shopkeeperapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomerDetails extends AppCompatActivity {
    String uname, umail, uctc, email;
    ImageView rejectImg, acceptImg;
    DBHelper db;

    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    SessionManager session;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater blowup=getMenuInflater();
        blowup.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.AboutUs:
                Intent i= new Intent(CustomerDetails.this,aboutus.class);
                startActivity(i);
                break;
            case R.id.Logout:
                Logout();

                finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void Logout() {
        session.logoutUser();
        Intent i = new Intent(getApplicationContext(), LoginPageUser.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        TextView unameHint = (TextView) findViewById(R.id.unameText);
        TextView umailHint = (TextView) findViewById(R.id.umailText);
        builder = new AlertDialog.Builder(CustomerDetails.this);
        rejectImg = (ImageView) findViewById(R.id.rejectImg);
        acceptImg = (ImageView) findViewById(R.id.acceptImg);
         session=new SessionManager(getApplicationContext());
        Bundle b = getIntent().getExtras();
        uname = b.getString("username");
        umail = b.getString("Email");

        unameHint.setText(uname);
        umailHint.setText(umail);
        db = new DBHelper(getApplicationContext());
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

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

    public void delete(View v) {
        deleteUser();
    }

    public void deleteUser() {
        db.deleteRow(umail);
        builder.setMessage("Deleted this user request")
                .create()
                .show();
        Intent i = new Intent(this, FinalPage.class);
        startActivity(i);
    }


    public void sendAccept(View v) {
        boolean network = haveNetworkConnection();
        if (!network) {

            builder.setMessage("Please check your internet connection ")
                    .create()
                    .show();
        }

        if (network) {
            progressDialog = ProgressDialog.show(CustomerDetails.this, "", "Working on it ... Please wait .... ", true);

            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = response.replaceFirst("<font>.*?</font>", "");
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    } else {

                    }

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        progressDialog.dismiss();
                        builder.setMessage("Sent your accept request  to user ")
                                .create()
                                .show();
                        deleteUser();

                    } catch
                            (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            SessionManager sm = new SessionManager(getApplicationContext());
            HashMap<String, String> user = sm.getUserDetails();
            email = user.get(SessionManager.KEY_EMAIL);
            String smail = user.get(SessionManager.KEY_EMAIL);
            String sname = user.get(SessionManager.KEY_NAME);
             PutNotifiationAlert putNotifiationAlert = new PutNotifiationAlert(smail, uname,sname, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(putNotifiationAlert);


        }
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 15000);
    }
}

