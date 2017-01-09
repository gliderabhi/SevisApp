package com.example.munna.shopkeeperapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberLogin extends AppCompatActivity {

    EditText otp;
    TextView verifymsg;
    TextView resendmsg;
    Button check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_login);

        otp=(EditText) findViewById(R.id.otpfield);
        verifymsg=(TextView)findViewById(R.id.txtverify);
        resendmsg=(TextView)findViewById(R.id.resendtxt);
        check=(Button)findViewById(R.id.registerPartButton);

    }
   public void  checkPass(View v){

    }
}
