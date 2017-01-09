package com.example.munna.shopkeeperapp;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;


public class  GCMPushReceiverService extends GcmListenerService {
     DBHelper db;
    String uname;
    String umail;
    String uctc;
Context ctx;
    @Override
    public void onCreate() {

        ctx=getApplicationContext();
    }

    @Override
    public void onMessageReceived(String s, Bundle bundle) {

        db=new DBHelper(ctx);
        String message=bundle.getString("message");
        String title=bundle.getString("title");
         uname=bundle.getString("username");
         umail=bundle.getString("umail");
         uctc=bundle.getString("contact");
        db.addRow(umail,uname,uctc);
        if(uname.isEmpty()){
            Log.v("Empty","yes");
        }

      Log.v("Message receieved","Message"+message);
        Wakelock(message,title);

    }

    private void Wakelock(String message,String title){
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();


        if(isScreenOn==false)
        {

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }
        sendNotification(message,title);
    }
    private void sendNotification(String message,String title){
        Intent i =new Intent( this, FinalPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode=0;
        PendingIntent pendingIntent=PendingIntent.getActivity(this,requestCode,i,PendingIntent.FLAG_ONE_SHOT);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mbuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.sevis)
                .setContentText(message)
                .setContentTitle(title)
                .setSound(sound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mbuilder.build());

    }

}
