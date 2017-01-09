package com.example.munna.shopkeeperapp;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by AbhishekSharma on 10/2/2016.
 */

public class GCMTokenRefreshListenerService extends InstanceIDListenerService{
    @Override
    public void onTokenRefresh() {
        Intent i=new Intent(this,GCMRegistrationIntentService.class);
        startService(i);
    }
}
