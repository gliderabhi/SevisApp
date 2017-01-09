package com.example.munna.shopkeeperapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AbhishekSharma on 10/3/2016.
 */

public class AcceptNotification extends StringRequest {
    private Map<String, String> params;
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/GCM_Scripts/ShopkeeperSendNotification.php";



    public AcceptNotification(String email,String umail, Response.Listener<String> listener) {
        super(Method.POST,REGISTER_REQUEST_URL , listener, null);

        params = new HashMap<>();
        params.put("email", email);
        params.put("username",umail);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}