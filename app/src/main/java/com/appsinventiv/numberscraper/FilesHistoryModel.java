package com.appsinventiv.numberscraper;


import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by AliAh on 03/05/2018.
 */

public class FilesHistoryModel {
    @SerializedName("files")
    @Expose
    private ArrayList<String> files = null;

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

}
