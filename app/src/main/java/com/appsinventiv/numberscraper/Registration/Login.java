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
                try {
                    pd.setMessage("Loging in..");
                    pd.setTitle("Please Wait..");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                    pd.show();
                    if (isConnected()) {
                        if (username.getText().toString().length() == 0) {
                            password.setError("Field cannot be left blank");
                        } else if (password.getText().toString().length() == 0) {
                            password.setError("Field cannot be left blank");
                        } else {

                            user=username.getText().toString();
                            pass=password.getText().toString();
                            mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(""+user);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    UserDetails userDetails=dataSnapshot.child("userDetails").getValue(UserDetails.class);
                                    if(userDetails!=null){
                                        String passwordFromDb=userDetails.getPassword();
                                        pd.dismiss();
                                        if(pass.equals(passwordFromDb)){

                                            String demo=userDetails.getIsDemo();
                                            SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = pref.edit();
                                            editor2.putString("demo", demo);
                                            editor2.putString("username",userDetails.getUsername());
                                            editor2.apply();
//                                            Toast.makeText(Login.this, demo, Toast.LENGTH_SHORT).show();

                                            launchHomeScreen();

                                        }
                                        else {
                                            Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        pd.dismiss();
                                        Toast.makeText(Login.this, "No user found\nPlease signup", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }else {
                        pd.dismiss();
                        Toast.makeText(Login.this, "No Internet Connection\nPlease turn on Wifi", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }
    private void launchHomeScreen() {
        Intent i=new Intent(Login.this, Option.class);
        startActivity(i);
        prefManager.setFirstTimeLaunch(false);
        finish();
//
//        if(demo.equals("no")) {
//            Intent i = new Intent(Login.this, MainActivity.class);
//            startActivity(i);
//            prefManager.setFirstTimeLaunch(false);
//            finish();
//        }else{
//            Intent i = new Intent(Login.this, DemoAccount.class);
//            startActivity(i);
//            prefManager.setFirstTimeLaunch(false);
//            finish();
//        }

    }

    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }
}
