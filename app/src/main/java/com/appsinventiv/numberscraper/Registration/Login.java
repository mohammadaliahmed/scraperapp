package com.appsinventiv.numberscraper.Registration;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.numberscraper.Option;
import com.appsinventiv.numberscraper.PrefManager;
import com.appsinventiv.numberscraper.R;
import com.appsinventiv.numberscraper.UserDetails;
import com.appsinventiv.numberscraper.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;

public class Login extends AppCompatActivity {
    EditText username, password;
    TextView register;
    Button login;
    ProgressDialog pd;
    String user,pass;

    private SharedPreferences userPref;

    DatabaseReference mDatabase;
    private PrefManager prefManager;
    String demoPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);

        register = (TextView) findViewById(R.id.register);

        pd = new ProgressDialog(Login.this);

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        demoPref = userPref.getString("demo", "yes");

        prefManager = new PrefManager(Login.this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();

        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    pd.setMessage("Loging in..");
                    pd.setTitle("Please Wait..");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                    pd.show();
                    if (username.getText().toString().length() == 0) {
                        password.setError("Field cannot be left blank");
                    } else if (password.getText().toString().length() == 0) {
                        password.setError("Field cannot be left blank");
                    } else {

                        user = username.getText().toString();
                        pass = password.getText().toString();
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child("" + user);
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserDetails userDetails = dataSnapshot.child("userDetails").getValue(UserDetails.class);
                                if (userDetails != null) {
                                    String passwordFromDb = userDetails.getPassword();
                                    pd.dismiss();
                                    if (pass.equals(passwordFromDb)) {

                                        SharedPrefs.setUsername(user);
                                        SharedPrefs.setIsLoggedIn("yes");
                                        SharedPrefs.setDemoCount(""+5);
                                        SharedPrefs.setIsDemo(userDetails.getIsDemo());
                                        launchHomeScreen();

                                    } else {
                                        Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    pd.dismiss();
                                    Toast.makeText(Login.this, "No user found\nPlease signup", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }



            }
        });


    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent i=new Intent(Login.this, Option.class);
        startActivity(i);

        finish();


    }


}
