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

import com.appsinventiv.numberscraper.MainActivity;
import com.appsinventiv.numberscraper.Option;
import com.appsinventiv.numberscraper.PrefManager;
import com.appsinventiv.numberscraper.R;
import com.appsinventiv.numberscraper.UserDetails;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Signup extends AppCompatActivity {

    EditText username, email, phone, password;
    TextView login;
    Button signup;

    DatabaseReference mDatabase;
    String currentDateTimeString;
//    private PrefManager prefManager;
    ProgressDialog pd;

    private PrefManager prefManager;


    List<String> userNameList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        pd = new ProgressDialog(Signup.this);

        username = (EditText) findViewById(R.id.username_signup_field);
        email = (EditText) findViewById(R.id.email_signup_field);
        phone = (EditText) findViewById(R.id.phone_signup_field);
        password = (EditText) findViewById(R.id.password_signup_field);

        signup = (Button) findViewById(R.id.signup_button);


        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        login = (TextView) findViewById(R.id.signin_text);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this, Login.class);
                startActivity(i);
                finish();
            }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isConnected();
                    if (isConnected()) {
                        //Toast.makeText(Signup.this, "connected", Toast.LENGTH_SHORT).show();
                        if (username.getText().toString().length() == 0) {
                            username.setError("A username is required!");
                        } else if (username.getText().toString().length() > 0 && username.getText().toString().length() < 6) {
                            username.setError("Atleast 6 characters required");
                        } else if (email.getText().toString().length() == 0) {
                            email.setError("Email adress cannot be left blank!");
                        } else if (password.getText().toString().length() == 0) {
                            password.setError("Password cannot be left blank!");
                        } else if (password.getText().toString().length() > 0 && password.getText().toString().length() < 8) {
                            password.setError("Atleast set 8 character password");
                        } else if (phone.getText().toString().length() == 0) {
                            phone.setError("Phone number cannot be left blank!");
                        } else {
                            pd.setMessage("Loging in..");
                            pd.setTitle("Please Wait..");
                            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                            pd.show();
                            setUserDetails();


                        }
                    } else {
                        Toast.makeText(Signup.this, "No Internet Connection\nPlease turn on Wifi", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    public boolean isConnected() throws InterruptedException, IOException {
        String command = "ping -c 1 google.com";
        return (Runtime.getRuntime().exec(command).waitFor() == 0);
    }
    private void setUserDetails() {

        String userName, emailId, phoneNumber, pass;
        userName = username.getText().toString().toLowerCase();
        emailId = email.getText().toString();

        pass = password.getText().toString();
        phoneNumber = phone.getText().toString();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userNameList.add(dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (userNameList != null) {
            if (userNameList.contains(userName)) {
                Toast.makeText(this, "Username is taken\nPlease choose another", Toast.LENGTH_SHORT).show();

            } else {
                String string= Long.toHexString(Double.doubleToLongBits(Math.random()));
                mDatabase.child(userName).child("userDetails").setValue(new UserDetails(userName,emailId,pass,phoneNumber,"yes",string));

                SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("username", userName);
                editor.putString("demo", "yes");
                editor.apply();

                Toast.makeText(this, "Sign up Successful", Toast.LENGTH_SHORT).show();
                launchHomeScreen();


                Intent i=new Intent(Signup.this,MainActivity.class);
                finish();

            }
        }
    }
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        pd.dismiss();
        startActivity(new Intent(Signup.this, Option.class));

        finish();
    }

}
