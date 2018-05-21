package com.appsinventiv.numberscraper;

import android.app.Application;

/**
 * Created by AliAh on 02/05/2018.
 */

public class NumberScraperApplication extends Application{
    private static NumberScraperApplication instance;


    public static NumberScraperApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
