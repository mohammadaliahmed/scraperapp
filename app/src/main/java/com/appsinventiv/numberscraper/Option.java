package com.appsinventiv.numberscraper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.numberscraper.Olx.MainActivity;
import com.appsinventiv.numberscraper.Registration.Login;
import com.appsinventiv.numberscraper.Utils.CommonUtils;
import com.appsinventiv.numberscraper.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Option extends AppCompatActivity {
    Button olx, pakwheels, zameen, history;
    private SharedPreferences userPref;
    String demoPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);


        olx = (Button) findViewById(R.id.olx);
        history = (Button) findViewById(R.id.history);
        pakwheels = (Button) findViewById(R.id.pakwheels);

        zameen = (Button) findViewById(R.id.zameen);

        olx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPrefs.getIsLoggedIn().equals("yes")) {
                    if (SharedPrefs.getIsDemo().equals("yes")) {
                        if (Integer.parseInt(SharedPrefs.getDemoCount()) <= 0) {
                            CommonUtils.showToast("Your demo is over\nPlease purchase. Thanks!");
                            Intent i = new Intent(Option.this, ProActivity.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(Option.this, MainActivity.class);
                            startActivity(i);
                        }
                    } else {
                        Intent i = new Intent(Option.this, MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    Intent i = new Intent(Option.this, Login.class);
                    startActivity(i);
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefs.getIsLoggedIn().equals("yes")) {
                    Intent i = new Intent(Option.this, History.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Option.this, Login.class);
                    startActivity(i);
                }


            }
        });
    }

    private void launchHomeScreen(String demo) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }

    private void launchHomeScreen2(String demo) {


    }
}
