package com.appsinventiv.numberscraper;

/**
 * Created by maliahmed on 11/18/2017.
 */

public class AppVersion {
    int appVersion;

    public AppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public AppVersion() {
    }

    public double getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }
}
