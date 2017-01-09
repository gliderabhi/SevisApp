package com.example.munna.shopkeeperapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceList extends AppCompatActivity {

    String url1= "http://thinkers.890m.com/shopkeeper/twoWheelerLogin.php";
    String url2= "http://thinkers.890m.com/shopkeeper/fourPetroLogin.php";
    String url3 ="http://thinkers.890m.com/shopkeeper/fourDieselLogin.php";
    String url4= "http://thinkers.890m.com/shopkeeper/heavyWheelerLogin.php";

    String email;
    SessionManager sess;
    ListView lst;
    String r;
    String[] servicing;
    AlertDialog.Builder builder;
    ArrayAdapter<String> adapter;
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
                Intent i= new Intent(ServiceList.this,aboutus.class);
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
    }@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        builder = new AlertDialog.Builder(ServiceList.this);
        session=new SessionManager(getApplicationContext());
        sess = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sess.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);


        Bundle b = getIntent().getExtras();
        r = b.getString("valuePair");

        if (r.matches("twoWheeler")) {
            servicing = new String[]{
                    "Dismantle, clean, check Engine components & assemble",
                    "Adjust clutch plate",
                    "Adjust, remove links & lubricate drive chain",
                    "Repair of Auto Electrical & Electronics Systems",
                    "Clean/replace air cleaner, fuel strainers and oil filters",
                    "Remove, clean, check, refit/replace – fuel tank, fuel pipes, fuel tap operation",
                    "Replace brake components, adjust brake top-up brake fluid",
                    "Check pressure, inflate, measure tread depth,inspect for damage, do Wheel truing, Repair tyre puncture & Tuffe-up tube",
                    "Water washing / cleaning of 2 & 3 wheelers",};

        }
        if (r.matches("FourWheelerPetrol")) {
            servicing = new String[]{
                    "Repair & Overhauling of Engine Systems(Petrol)",
                    "Repair & Overhauling of Chassis System(Petrol Engine) ",
                    "Repair of Auto Electrical & Electronics Systems",
                    "Repairing of Auto Air Conditioning System",
                    "Clean / replace – air cleaner, oil filter & fuel filter",
                    "Apply Grease to parts / through greasing points",
                    "Wheel alignment & balancing",
                    "Minor repair of Auto body",
                    "Adjust Hand brake and replace hand brake cable",

            };
        }
        if (r.matches("forWheelerDiesel")) {
            servicing = new String[]{
                    "Repair & Overhauling of Engine Systems(Diesel)",
                    "Repair & Overhauling of Chassis System(Diesel Vehicle)",
                    "Repair of Auto Electrical & Electronics Systems",
                    "Repairing of Auto Air Conditioning System",
                    "Clean / replace – air cleaner, oil filter & fuel filter",
                    "Apply Grease to parts / through greasing points",
                    "Wheel alignment & balancing",
                    "Minor repair of Auto body",
                    "Adjust Hand brake and replace hand brake cable",

            };
        }
        if (r.matches("FourWheelerHeavy")) {
            servicing = new String[]{
                    "Repair & Overhauling of Engine Systems(Heavy Diesel)",
                    "Repair & Overhauling of Chassis System(Heavy  Vehicle)",
                    "Repair of Auto Electrical & Electronics Systems",
                    "Repairing of Auto Air Conditioning System",
                    "Clean / replace – air cleaner, oil filter & fuel filter",
                    "Apply Grease to parts / through greasing points",
                    "Wheel alignment & balancing",
                    "Minor repair of Auto body",
                    "Adjust Hand brake and replace hand brake cable",

            };
        }


        lst = (ListView) findViewById(R.id.serviceList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, servicing);
        lst.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lst.setAdapter(adapter);
    }


    int[] m = new int[10];


    public void submitData(View v) {
        if (r.matches("twoWheeler")) {
            SparseBooleanArray checked = lst.getCheckedItemPositions();

            if (checked.get(0)) {
                m[0] = 1;
            } else {
                m[0] = 0;
            }
            if (checked.get(1)) {
                m[1] = 1;
            } else {
                m[1] = 0;
            }
            if (checked.get(2)) {
                m[2] = 1;
            } else {
                m[2] = 0;
            }
            if (checked.get(3)) {
                m[3] = 1;
            } else {
                m[3] = 0;
            }
            if (checked.get(4)) {
                m[4] = 1;
            } else {
                m[4] = 0;
            }
            if (checked.get(5)) {
                m[5] = 1;
            } else {
                m[5] = 0;
            }
            if (checked.get(6)) {
                m[6] = 1;
            } else {
                m[6] = 0;
            }
            if (checked.get(7)) {
                m[7] = 1;
            } else {
                m[7] = 0;
            }
            if (checked.get(8)) {
                m[8] = 1;
            } else {
                m[8] = 0;
            }
            SendData(url1);

        }


        if (r.matches("FourWheelerPetrol")) {
            SparseBooleanArray checked = lst.getCheckedItemPositions();

            if (checked.get(0)) {
                m[0] = 1;
            } else {
                m[0] = 0;
            }
            if (checked.get(1)) {
                m[1] = 1;
            } else {
                m[1] = 0;
            }
            if (checked.get(2)) {
                m[2] = 1;
            } else {
                m[2] = 0;
            }
            if (checked.get(3)) {
                m[3] = 1;
            } else {
                m[3] = 0;
            }
            if (checked.get(4)) {
                m[4] = 1;
            } else {
                m[4] = 0;
            }
            if (checked.get(5)) {
                m[5] = 1;
            } else {
                m[5] = 0;
            }
            if (checked.get(6)) {
                m[6] = 1;
            } else {
                m[6] = 0;
            }
            if (checked.get(7)) {
                m[7] = 1;
            } else {
                m[7] = 0;
            }
            if (checked.get(8)) {
                m[8] = 1;
            } else {
                m[8] = 0;
            }


            SendData(url2);
        }
        if (r.matches("forWheelerDiesel")) {
            SparseBooleanArray checked = lst.getCheckedItemPositions();

            if (checked.get(0)) {
                m[0] = 1;
            } else {
                m[0] = 0;
            }
            if (checked.get(1)) {
                m[1] = 1;
            } else {
                m[1] = 0;
            }
            if (checked.get(2)) {
                m[2] = 1;
            } else {
                m[2] = 0;
            }
            if (checked.get(3)) {
                m[3] = 1;
            } else {
                m[3] = 0;
            }
            if (checked.get(4)) {
                m[4] = 1;
            } else {
                m[4] = 0;
            }
            if (checked.get(5)) {
                m[5] = 1;
            } else {
                m[5] = 0;
            }
            if (checked.get(6)) {
                m[6] = 1;
            } else {
                m[6] = 0;
            }
            if (checked.get(7)) {
                m[7] = 1;
            } else {
                m[7] = 0;
            }
            if (checked.get(8)) {
                m[8] = 1;
            } else {
                m[8] = 0;
            }


            SendData(url3);
        }
        if (r.matches("FourWheelerHeavy")) {
            SparseBooleanArray checked = lst.getCheckedItemPositions();

            if (checked.get(0)) {
                m[0] = 1;
            } else {
                m[0] = 0;
            }
            if (checked.get(1)) {
                m[1] = 1;
            } else {
                m[1] = 0;
            }
            if (checked.get(2)) {
                m[2] = 1;
            } else {
                m[2] = 0;
            }
            if (checked.get(3)) {
                m[3] = 1;
            } else {
                m[3] = 0;
            }
            if (checked.get(4)) {
                m[4] = 1;
            } else {
                m[4] = 0;
            }
            if (checked.get(5)) {
                m[5] = 1;
            } else {
                m[5] = 0;
            }
            if (checked.get(6)) {
                m[6] = 1;
            } else {
                m[6] = 0;
            }
            if (checked.get(7)) {
                m[7] = 1;
            } else {
                m[7] = 0;
            }
            if (checked.get(8)) {
                m[8] = 1;
            } else {
                m[8] = 0;
            }


            SendData(url4);
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
    public void SendData(String url) {
        boolean network = haveNetworkConnection();
        if (network) {
            prg=ProgressDialog.show(ServiceList.this,"Details upload","Contacting Server Please Wait ....",true);


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
                            prg.dismiss();
                            builder.setMessage("Updated your details ")
                                    .create()
                                    .show();
                            Intent intent = new Intent(ServiceList.this, UserAreaActivity.class);
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


            ServiceListSubmitPage serviceListSubmitPage = new ServiceListSubmitPage(url,email, m[0], m[1], m[2], m[3], m[4], m[5], m[6], m[7], m[8], responseListener);
            RequestQueue queue = Volley.newRequestQueue(ServiceList.this);
            queue.add(serviceListSubmitPage);
        }   else{
              builder.setMessage("Please Check Your Network ")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }

    }
}


