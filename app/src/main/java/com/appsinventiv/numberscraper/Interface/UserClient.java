package com.appsinventiv.numberscraper.Interface;

import com.appsinventiv.numberscraper.FilesHistoryModel;
import com.appsinventiv.numberscraper.History;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by AliAh on 03/05/2018.
 */

public interface UserClient {
    @POST("files.php")
    Call<FilesHistoryModel> createTask(@Body JSONObject json);
}
