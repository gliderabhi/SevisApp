package com.example.munna.shopkeeperapp;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/shopkeeper/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String mail, String password,String contact, String shopname,Double lat,Double lng,String loc, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("mail", mail);
        params.put("Shopname", shopname);
        params.put("contact", contact);
        params.put("password",password);
        params.put("latitude",String.valueOf(lat));
        params.put("longitude",String.valueOf(lng));
        params.put("localAddress",loc);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}