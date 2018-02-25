package com.appsinventiv.numberscraper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Option extends AppCompatActivity {
Button olx,pakwheels;
    private SharedPreferences userPref;
    String demoPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        demoPref = userPref.getString("demo", "yes");

        olx= (Button) findViewById(R.id.olx);
        pakwheels= (Button) findViewById(R.id.pakwheels);

        olx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen(demoPref);
            }
        });
        pakwheels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen2(demoPref);
            }
        });
    }
    private void launchHomeScreen(String demo) {
        if(demo.equals("no")) {
            Intent i = new Intent(Option.this, MainActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(Option.this, DemoAccount.class);
            startActivity(i);
            finish();
        }

    }
    private void launchHomeScreen2(String demo) {
        if(demo.equals("no")) {
            Intent i = new Intent(Option.this, Pakwheels.class);
            startActivity(i);
            finish();
        }else{
            Intent i = new Intent(Option.this, PakwheelsDemo.class);
            startActivity(i);
            finish();
        }

    }
}
