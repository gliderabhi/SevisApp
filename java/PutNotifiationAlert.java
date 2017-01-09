package com.example.munna.shopkeeperapp;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class PutNotifiationAlert extends StringRequest {
    private Map<String, String> params;
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/GCM_Scripts/ServerSendNotification.php";



    public PutNotifiationAlert(String smail,String cmail,String sname, Response.Listener<String> listener) {
        super(Method.POST,REGISTER_REQUEST_URL , listener, null);
       params = new HashMap<>();
        params.put("mail", smail);
        params.put("username",sname);
        params.put("umail",cmail);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}