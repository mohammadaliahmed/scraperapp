package com.appsinventiv.numberscraper.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.appsinventiv.numberscraper.NumberScraperApplication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by AliAh on 29/03/2018.
 */

public class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }
    public static void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                Toast.makeText(NumberScraperApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String getFilename(String url){
        String abc = url.replace("http://getnumbers.co/withnames/", "");
        abc = abc.replace("-", " ");
        abc = abc.replace(".txt", "");
        abc = abc.replace("/", "");
        abc = abc.replace(SharedPrefs.getUsername(), "");
        abc=abc.replace(" ","");
        abc=abc.trim();
        abc=abc+".txt";
        return abc;
    }


}
