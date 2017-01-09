package com.example.munna.shopkeeperapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AbhishekhKumarShamra on 9/17/2016.
 */
public class ServiceListSubmitPage extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/shopkeeper/twoWheelerLogin.php";
    private Map<String, String> params;

    public ServiceListSubmitPage(String url,String mail, int m0,int m1,int m2,int m3,int m4,int m5,int m6,int m7,int m8, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);

        params = new HashMap<>();
        params.put("mail", mail);
        params.put("m0",String.valueOf(m0));
        params.put("m1",String.valueOf(m1));
        params.put("m2",String.valueOf(m2));
        params.put("m3",String.valueOf(m3));
        params.put("m4",String.valueOf(m4));
        params.put("m5",String.valueOf(m5));
        params.put("m6",String.valueOf(m6));
        params.put("m7",String.valueOf(m7));
        params.put("m8",String.valueOf(m8));


    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
