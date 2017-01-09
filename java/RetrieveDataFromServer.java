package com.example.munna.shopkeeperapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AbhishekhKumarShamra on 9/18/2016.
 */
public class RetrieveDataFromServer extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/shopkeeper/detailGetTwoWheeler.php";
    private Map<String, String> params;

    public RetrieveDataFromServer(String mail, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("mail", mail);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
