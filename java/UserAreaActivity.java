package com.example.munna.shopkeeperapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
public class UserAreaActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    String email;
    String token;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    Button ubtn;
    Button lbtn;
    AlertDialog.Builder builder;
    SessionManager session;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
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
                Intent i= new Intent(UserAreaActivity.this,aboutus.class);
                startActivity(i);
                break;
            case R.id.Logout:
              Logout();

                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbHelper=new DBHelper(this);

        setContentView(R.layout.activity_user_area);
        img1 = (ImageView) findViewById(R.id.imageView3);
        img2 = (ImageView) findViewById(R.id.imageView4);
        img3 = (ImageView) findViewById(R.id.imageView5);
        img4 = (ImageView) findViewById(R.id.imageView6);
        ubtn = (Button) findViewById(R.id.btnFinal);
        builder = new AlertDialog.Builder(UserAreaActivity.this);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        String ShopName = user.get(SessionManager.KEY_NAME);
        email = user.get(SessionManager.KEY_EMAIL);
        String cntct = user.get(SessionManager.KEY_CONTACT);
        boolean network = haveNetworkConnection();
        if (network) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                        token = intent.getStringExtra("token");
                          } else {

                    }
                }

            };
            Intent i =new Intent( this,GCMRegistrationIntentService.class);
            startService(i);
        }

        ProgressDialog prg=ProgressDialog.show(UserAreaActivity.this,"","",true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                  tokenSend();

            }
        }, 3000);
     prg.dismiss();
    }

    @Override
    protected  void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
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


    public void GetServices(View v) {
        switch (v.getId()) {
            case R.id.imageView3:
                Intent i1 = new Intent(getApplicationContext(), ServiceList.class);
                i1.putExtra("valuePair", "twoWheeler");
                startActivity(i1);
                break;
            case R.id.imageView4:
                Intent i2 = new Intent(getApplicationContext(), ServiceList.class);
                i2.putExtra("valuePair", "FourWheelerPetrol");
                startActivity(i2);
                 break;
            case R.id.imageView5:
                Intent i3 = new Intent(getApplicationContext(), ServiceList.class);
                i3.putExtra("valuePair", "forWheelerDiesel");
                  startActivity(i3);

                break;
            case R.id.imageView6:
                Intent i4 = new Intent(getApplicationContext(), ServiceList.class);
                i4.putExtra("valuePair", "FourWheelerHeavy");

                startActivity(i4);
                break;
        }


    }

    public void Logout() {
        session.logoutUser();
        Intent i = new Intent(getApplicationContext(), LoginPageUser.class);
        startActivity(i);
    }

    public void tokenSend() {
                 boolean network = haveNetworkConnection();
          if (network) {

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

                          } else {
                                builder.setMessage("Token register failed")
                                      .setNegativeButton("Retry", null)
                                      .create()
                                      .show();       }
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                  }
              };

              SendToken sendToken = new SendToken(email, token, responseListener);
              RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
              queue.add(sendToken);


          } else {
              builder.setMessage("Check your internet connection")
                      .create()
                      .show();
               }


      }

public void changepage(View v){
    Intent i = new Intent(getApplicationContext(), FinalPage.class);
    Bundle b=new Bundle();
    tokenSend();
    i.putExtras(b);
    startActivity(i);

}


}
