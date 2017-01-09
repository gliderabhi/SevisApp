package com.example.munna.shopkeeperapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class FinalPage extends AppCompatActivity {

    String email;
    TextView getMsg;
    ProgressDialog prg;
    SessionManager sessionManager;
    int i=1;
    ArrayList<String > mail;
    ArrayList<String > ctc;
    ArrayList<String > name;
    ListView userList;
    List<String > contact;
    AlertDialog.Builder build;
    DBHelper db;

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
                Intent i= new Intent(FinalPage.this,aboutus.class);
                startActivity(i);
                break;
            case R.id.Logout:
                Logout();

                finish();
        }
        return super.onOptionsItemSelected(item);
    }



    public void Logout() {
        sessionManager.logoutUser();
        Intent i = new Intent(getApplicationContext(), LoginPageUser.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {

        Intent i=new Intent(getApplicationContext(),UserAreaActivity.class);
        startActivity(i);
    }
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

        db=new DBHelper(getApplicationContext());

       getMsg=(TextView)findViewById(R.id.MsgIntent);
       userList=(ListView)findViewById(R.id.userList);

       build=new AlertDialog.Builder(FinalPage.this);

       OpenCreation();

   }
    public  void  OpenCreation(){
         sessionManager=new SessionManager(getApplicationContext());
        mail=new ArrayList<String>();
        ctc=new ArrayList<String>();
        name=new ArrayList<String>();
        prg= ProgressDialog.show(FinalPage.this,"Please wait","",true);

        name=db.EmailRecord();
        mail=db.UsernameRecord();
        contact=db.ContactRecord();
        if(mail.size()==0 && contact.size()==0 && name.size()==0){
            getMsg.setText("We will notify when some one accepts your request ");

        }else {
            ListView lst = (ListView) findViewById(R.id.userList);
            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listlayout, mail);
            lst.setAdapter(adapter);
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                               Intent i = new Intent(getApplicationContext(), CustomerDetails.class);
                                               Bundle b = new Bundle();
                                               b.putString("Email",   mail.get(position));
                                               b.putString("username", name.get(position));
                                               i.putExtras(b);
                                               startActivity(i);
                                           }

                                       }
            );

        }
        prg.dismiss();

    }

}

