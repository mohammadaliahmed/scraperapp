package com.appsinventiv.numberscraper;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

public class PakwheelsDemo extends AppCompatActivity {
    EditText getUrl, start, end, filen;
    //static TextView pg;
    Button search, pro;
    String urlphp;
    DownloadManager downloadManager;
    HttpURLConnection urlConnection;
    String page;
    static String filename;
    private static final String FORMAT = "%02d:%02d:%02d";
    int rem;
    TextView countDown;

    TextView remainingcount;

    static int st, en;
    static  int i;
    private SharedPreferences userPref;
    private SharedPreferences timer;
    long prefTime;
    String username;

    DatabaseReference mDatabse;
    long currentTime;

    int canClick=1;

    int versionCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakwheels_demo);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionCode=pInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        countDown=(TextView)findViewById(R.id.countdown);
        timer = getSharedPreferences("timer", Context.MODE_PRIVATE);
        prefTime = timer.getLong("timer", 0);
        currentTime = System.currentTimeMillis();
        if(prefTime!=0) {
            Long timeDifference=prefTime-currentTime;
            new CountDownTimer(timeDifference, 1000) { // adjust the milli seconds here

                public void onTick(long millisUntilFinished) {
                    canClick=0;
                    countDown.setText("" + String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {

                    countDown.setText("Done!");
                    countDown.setText("");
                    canClick=1;

                }
            }.start();
        }


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        pro=(Button)findViewById(R.id.pro);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PakwheelsDemo.this,ProActivity.class);
                startActivity(i);

            }
        });

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        username=userPref.getString("username","");

        mDatabse= FirebaseDatabase.getInstance().getReference();
        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AppVersion appVersion = dataSnapshot.child("appVersion").getValue(AppVersion.class);
                if (appVersion != null) {
                    if (appVersion.getAppVersion() > versionCode) {
                        Toast.makeText(PakwheelsDemo.this, "Please update to new version", Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse("market://details?id=" + PakwheelsDemo.this.getPackageName());
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
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + PakwheelsDemo.this.getPackageName())));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails userDetails=dataSnapshot.child("users").child(""+username).child("userDetails").getValue(UserDetails.class);
                if(userDetails!=null){
                    if(userDetails.getIsDemo().equals("no")){

                        SharedPreferences pref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = pref.edit();
                        editor2.putString("demo", "no");
                        editor2.apply();
//                        Toast.makeText(PakwheelsDemo.this, "Thankyou for purchasing :)", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(PakwheelsDemo.this,Pakwheels.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
        getUrl = (EditText) findViewById(R.id.geturl);


        //pg=(TextView)findViewById(R.id.pg);
        search = (Button) findViewById(R.id.search);
        // download = (Button) findViewById(R.id.download);
        start = (EditText) findViewById(R.id.start);
        filen = (EditText) findViewById(R.id.filen);

        startSearch();

    }

    public void phpRequest(String pg) {
        urlphp = getUrl.getText().toString();
        //page=start.getText().toString();
        filename = filen.getText().toString();
        filename=filename+"-pakwheels-"+username;
        String type = "login";
        String linksFile = username+"-pakwheels-linksFile";
        BackgroundPakwheels backgroundPakwheels = new BackgroundPakwheels(PakwheelsDemo.this);
        backgroundPakwheels.execute(type, urlphp, pg, filename,linksFile);

    }

    public void startSearch(){
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(canClick==1){
                if (start.getText().toString().length() == 0) {
                    start.setError("Start page is required!");
                }

                if (filen.getText().toString().length() == 0) {
                    filen.setError("File name is required!");
                }
                if (getUrl.getText().toString().length() == 0) {
                    getUrl.setError("Url is required!");

                }
                if(getUrl.getText().toString().contains("?page=")) {
                    getUrl.setError("Url is not correct");
                }
                else {


                    st = Integer.parseInt(start.getText().toString());
//                    en = Integer.parseInt(end.getText().toString());

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {


                            phpRequest(String.valueOf(st));


                        }
                    });
                    t.start();
//                    long millis = System.currentTimeMillis() + 20000L;
                    long millis = System.currentTimeMillis() + 21600000L;
                    SharedPreferences pref2 = getSharedPreferences("timer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = pref2.edit();
                    editor2.putLong("timer", millis);
                    editor2.apply();
                    Intent i = new Intent(PakwheelsDemo.this, DonePakwheels.class);
                    startActivity(i);

                    }
                }
                else {
                Toast.makeText(PakwheelsDemo.this, "Please wait for timer to be over!", Toast.LENGTH_SHORT).show();
            }
            }

        });




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

