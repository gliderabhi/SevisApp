package com.example.munna.shopkeeperapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginPageUser extends AppCompatActivity {

    EditText lmail;
    EditText lpass;
    Button lbtn;
    SessionManager session;
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
                Intent i= new Intent(LoginPageUser.this,aboutus.class);
                startActivity(i);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_user);
        lmail = (EditText) findViewById(R.id.lmail);
        lpass = (EditText) findViewById(R.id.lpass);
        lbtn = (Button) findViewById(R.id.blogin);
        session = new SessionManager(getApplicationContext());
       builder = new AlertDialog.Builder(LoginPageUser.this);

        lmail.setText("");
        lpass.setText("");
        if(session.isLoggedIn()){
            Intent intent = new Intent(LoginPageUser.this, UserAreaActivity.class);
            startActivity(intent);
        }

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

ProgressDialog prg;
    public void loginButton(View view) {
        boolean network = haveNetworkConnection();
        if (!network) {

            builder.setMessage("Please check your internet connection ")
                    .create()
                    .show();
        }

        if (network) {

            final String lemail = lmail.getText().toString();
            final String lpassword = lpass.getText().toString();

            if (lemail.matches("") && lpassword.matches("")) {
                Toast.makeText(LoginPageUser.this, "Please fill both the email and password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (lemail.matches("")) {
                Toast.makeText(this, "You did not enter a email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (lpassword.matches("")) {
                Toast.makeText(this, "Please fill the  password", Toast.LENGTH_SHORT).show();
                return;
            }


            prg=ProgressDialog.show(LoginPageUser.this,"Logging in","Please wait while we log you in... ",true);

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
                        boolean success = jsonResponse.getBoolean("success");


                        if (success) {
                            String email = jsonResponse.getString("email");
                            String shopName= jsonResponse.getString("shopname");
                            String contact = jsonResponse.getString("contact");
                            prg.dismiss();
                            Intent intent = new Intent(LoginPageUser.this, UserAreaActivity.class);
                            session.createLoginSession(email,contact,shopName);
                            startActivity(intent);

                        } else {
                            prg.dismiss();
                             builder.setMessage("Login Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(lemail, lpassword, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginPageUser.this);
            queue.add(loginRequest);

        }

    }


    public void registerButton(View v){
        Intent i= new Intent(LoginPageUser.this,RegisterPage.class);
        startActivity(i);
    }
    public void Logout(View v){
        session.logoutUser();
    }
}
