package com.appsinventiv.numberscraper.Olx;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.numberscraper.R;
import com.appsinventiv.numberscraper.UserDetails;
import com.appsinventiv.numberscraper.Utils.CommonUtils;
import com.appsinventiv.numberscraper.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    EditText getUrl, start, end, filen;
    Button search;
    String urlphp;

    static String filename;
    static int st, en;
    static int i;

    DatabaseReference mDatabase;
    String isDemo;
    TextView demoText;
    int demoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demoText = (TextView) findViewById(R.id.demoText);
        demoCount = Integer.parseInt(SharedPrefs.getDemoCount());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(SharedPrefs.getUsername()).child("userDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserDetails user = dataSnapshot.getValue(UserDetails.class);
                    if (user != null) {
                        isDemo = user.getIsDemo();
                        if (isDemo.equals("yes")) {
                            SharedPrefs.setIsDemo("yes");
                            demoText.setVisibility(View.VISIBLE);
                            demoText.setText("You can use " + SharedPrefs.getDemoCount() + " times more");
                        }else{
                            SharedPrefs.setIsDemo("no");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


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
        filename = filename + "-olx-" + SharedPrefs.getUsername();
        String type = "login";
        String linksFile = SharedPrefs.getUsername() + "-olx-linksFile";
        BackgroundWorker backgroundworker = new BackgroundWorker(MainActivity.this, new OlxScraperObserver() {
            @Override
            public void onPageDone(int page) {
                Intent intent = new Intent("custom-event-name");
                // You can also include some extra data.
                intent.putExtra("currentPage", page);
                intent.putExtra("endPage", en);

                LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(intent);
            }

            @Override
            public void onError(String error) {

            }
        });
        backgroundworker.execute(type, urlphp, pg, filename, linksFile);

    }

    public void startSearch() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getUrl.getText().toString().length() == 0 || getUrl.getText().equals("")) {
                    getUrl.setError("Url is required!");
                } else if (start.getText().toString().length() == 0 || start.getText().equals("")) {

                    start.setError("Start page is required!");
                } else if (end.getText().toString().length() == 0 || end.getText().equals("")) {
                    end.setError("End page is required!");
                }

                else if ((Integer.parseInt(end.getText().toString()) -Integer.parseInt(start.getText().toString()))<0) {

                   CommonUtils.showToast("Start page cannot be greater than end page");
                }

                else if (filen.getText().toString().length() == 0 || filen.getText().equals("")) {

                    filen.setError("File name is required!");
                } else if (getUrl.getText().toString().contains("?page=")) {
                    getUrl.setError("Url is not correct");
                } else if (!getUrl.getText().toString().contains("olx.com.pk")) {
                    getUrl.setError("This is not an OLX Url");
                } else if (isDemo.equals("yes")) {
                    if ((Integer.parseInt(end.getText().toString()) - Integer.parseInt(start.getText().toString())) > 4) {
                        CommonUtils.showToast("You can scrape only 5 pages in demo");
                    } else {
                        demoCount--;
                        SharedPrefs.setDemoCount(""+demoCount);
                        startWorking();
                    }
                } else {
                    startWorking();

                }


            }

        });


    }

    public void startWorking() {

        st = Integer.parseInt(start.getText().toString());
        en = Integer.parseInt(end.getText().toString());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (i = st; i <= en; i++) {
//                                CommonUtils.showToast("out "+i);
                    phpRequest(String.valueOf(i));

                }
            }
        });
        t.start();

        Intent i = new Intent(MainActivity.this, Done.class);
        startActivity(i);
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

