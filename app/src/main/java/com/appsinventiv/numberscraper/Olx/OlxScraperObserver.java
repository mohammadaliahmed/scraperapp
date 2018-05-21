package com.appsinventiv.numberscraper.Olx;

/**
 * Created by AliAh on 02/05/2018.
 */

public interface OlxScraperObserver {
    public void onPageDone(int page);
    public void onError(String error);

}
