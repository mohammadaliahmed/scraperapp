package com.appsinventiv.numberscraper;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.numberscraper.Olx.Done;
import com.appsinventiv.numberscraper.Olx.MainActivity;
import com.appsinventiv.numberscraper.Utils.CommonUtils;
import com.appsinventiv.numberscraper.Utils.DownloadFile;

import java.util.concurrent.TimeUnit;

public class DonePakwheels extends AppCompatActivity {
    TextView text1, t4;
    Button btn, btn2, cancel;
    String filename;

    int cPage, ePage;
    ProgressBar progressBar2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_pakwheels);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.VISIBLE);
        cancel = (Button) findViewById(R.id.cancel);


        btn = (Button) findViewById(R.id.button3);
        btn2 = (Button) findViewById(R.id.vcfdownload);
        filename = Pakwheels.filename;
        btn.setText("Download " + filename + ".txt");
        btn2.setText("Download " + filename + ".vcf");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name-pak"));


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://getnumbers.co/app/files/" + filename + ".txt";
                DownloadFile.fromUrl(url, filename + ".txt");

            }
//            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://getnumbers.co/app/vcf/" + filename + ".vcf";
                DownloadFile.fromUrl(url, filename + ".vcf");
            }
        });

        text1 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        text1 = (TextView) findViewById(R.id.textView3);
        t4 = (TextView) findViewById(R.id.textView4);
        text1.setText("Pages scraped 0 out of " + Pakwheels.en);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalBroadcastManager.getInstance(DonePakwheels.this).unregisterReceiver(mMessageReceiver);
                Intent i = new Intent(DonePakwheels.this, Option.class);
                startActivity(i);
                finish();
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int currentPage = intent.getIntExtra("currentPage", -1);
            int endPage = intent.getIntExtra("endPage", -1);

            text1.setText("Pages scraped " + currentPage + " out of " + endPage);
            cPage = currentPage;
            ePage = endPage;
            if (currentPage == endPage) {
                progressBar2.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (cPage < ePage) {
            CommonUtils.showToast("Process Terminated!");
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        CommonUtils.showToast("Please cancel and then exit");
    }
}

