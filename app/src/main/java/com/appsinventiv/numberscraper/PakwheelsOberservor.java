package com.appsinventiv.numberscraper;

/**
 * Created by AliAh on 28/05/2018.
 */

public interface PakwheelsOberservor {
    public void onPageDone(int page);
    public void onError(String error);
}
