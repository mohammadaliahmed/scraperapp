package com.appsinventiv.numberscraper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ProActivity extends AppCompatActivity {
Button verifiy, call;
    EditText code;
    DatabaseReference mDatabase;
    private SharedPreferences userPref;
    String username,codeEntered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro);

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        username=userPref.getString("username","");
        verifiy=(Button)findViewById(R.id.verify);
        call=(Button)findViewById(R.id.dial);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+923158000333"));
                startActivity(i);
            }
        });

        code= (EditText) findViewById(R.id.code);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(""+username);


        verifiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isConnected()) {
                        codeEntered = code.getText().toString();
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserDetails user = dataSnapshot.child("userDetails").getValue(UserDetails.class);
                                if (user != null) {
                                    if (codeEntered.equals(user.getCode())) {
                                        mDatabase.child("userDetails").child("isDemo").setValue("no");
                                        Toast.makeText(ProActivity.this, "Thank you for purchasing", Toast.LENGTH_SHORT).show();
                                        SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor2 = pref.edit();
                                        editor2.apply();
                                        Intent i = new Intent(ProActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(ProActivity.this, "Wrong code", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else {
                        Toast.makeText(ProActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
}
