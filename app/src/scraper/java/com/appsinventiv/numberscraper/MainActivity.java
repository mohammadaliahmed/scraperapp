package com.appsinventiv.numberscraper;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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


public class MainActivity extends AppCompatActivity {
    EditText getUrl, start, end, filen;
    Button search;
    String urlphp;

    static String filename;
      static int st, en;
     static  int i;
    private SharedPreferences userPref;
    String username;
    int versionCode;
DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase=FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
             versionCode=pInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppVersion appVersion = dataSnapshot.child("appVersion").getValue(AppVersion.class);
                if (appVersion != null) {
                    if (appVersion.getAppVersion() > versionCode) {
                        Toast.makeText(MainActivity.this, "Please update to new version", Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails userDetails=dataSnapshot.child("users").child(""+username).child("userDetails").getValue(UserDetails.class);
                if(userDetails!=null){
                    if(userDetails.getIsDemo().equals("yes")){

                        SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = pref.edit();
                        editor2.putString("demo", "yes");
                        editor2.apply();
//                        Toast.makeText(MainActivity.this, "Premium time over\nPlease purchase :)", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(MainActivity.this,DemoAccount.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        username=userPref.getString("username","");
        getUrl = (EditText) findViewById(R.id.geturl);
        search = (Button) findViewById(R.id.search);

        start = (EditText) findViewById(R.id.start);
        filen = (EditText) findViewById(R.id.filen);
        end = (EditText) findViewById(R.id.end);

        startSearch();

    }

    public void phpRequest(String pg) {
        urlphp = getUrl.getText().toString();
        //page=start.getText().toString();
        filename = filen.getText().toString();
        filename=filename+"-olx-"+username;
        String type = "login";
        String linksFile = username+"-olx-linksFile";
        BackgroundWorker backgroundworker = new BackgroundWorker(MainActivity.this);
        backgroundworker.execute(type, urlphp, pg, filename,linksFile);

    }

    public void startSearch(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isConnected()){
                        if (getUrl.getText().toString().length() == 0|| getUrl.getText().equals("")) {
                            getUrl.setError("Url is required!");
                        }
                    else if (start.getText().toString().length() == 0 ||start.getText().equals("")) {

                        start.setError("Start page is required!");
                    }
                    else if (end.getText().toString().length() == 0||end.getText().equals("") ){
                        end.setError("End page is required!");
                    }
                    else if (filen.getText().toString().length() == 0||filen.getText().equals("")) {

                        filen.setError("File name is required!");
                    }

                    else if(getUrl.getText().toString().contains("?page=")) {
                            getUrl.setError("Url is not correct");
                        }
                       else if(!getUrl.getText().toString().contains("olx.com.pk")) {
                            getUrl.setError("This is not an OLX Url");
                        }
                    else {

                        st = Integer.parseInt(start.getText().toString());
                        en = Integer.parseInt(end.getText().toString());

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (i = st; i <= en; i++) {

                                    phpRequest(String.valueOf(i));

                                }
                            }
                        });
                        t.start();

                        Intent i = new Intent(MainActivity.this, Done.class);
                        startActivity(i);

                    }

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
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}

