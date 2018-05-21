package com.appsinventiv.numberscraper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class DoneZameen extends AppCompatActivity {
    TextView text1,t4;
    Button btn,btn2,pro;

    private SharedPreferences userPref;
    String demo;

    private static final String FORMAT = "%02d:%02d:%02d";

    int seconds , minutes;
    DownloadManager downloadManager;
    String filename;
    int start,end;
    int canClick=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_zameen);
        btn=(Button)findViewById(R.id.button3);
        btn2=(Button)findViewById(R.id.vcfdownload);

        pro=(Button)findViewById(R.id.pro);

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        demo=userPref.getString("demo","");

        if(demo.equals("yes")){
            filename=Zameen.filename;
            btn.setText("Download "+filename+".txt");
            btn2.setText("Download "+filename+".vcf");
            start=1;
            end=2;
        }
        if(demo.equals("no")){
            filename=Zameen.filename;
            btn.setText("Download "+filename+".txt");
            btn2.setText("Download "+filename+".vcf");
            start=Zameen.st;
            end=Zameen.en;
            pro.setVisibility(View.GONE);
        }


        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DoneZameen.this,ProActivity.class);
                startActivity(i);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClick == 0) {
                    Toast.makeText(DoneZameen.this, "Please wait for some time", Toast.LENGTH_SHORT).show();
                } else {


                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("http://getnumbers.co/app/files/" + filename + ".txt");
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename + ".txt");
                    Long referene = downloadManager.enqueue(request);

                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canClick == 0) {
                    Toast.makeText(DoneZameen.this, "Please wait for some time", Toast.LENGTH_SHORT).show();
                } else {

                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse("http://getnumbers.co/app/vcf/" + filename + ".vcf");
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename + ".vcf");
                    Long referene = downloadManager.enqueue(request);
                }
            }
        });

        text1=(TextView)findViewById(R.id.textView3);
        t4=(TextView)findViewById(R.id.textView4);

        new CountDownTimer((end-start+1)*5*1000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                text1.setText(""+ String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {

                text1.setText("Done!");
                t4.setText("");
                canClick=1;
            }
        }.start();

    }


}
