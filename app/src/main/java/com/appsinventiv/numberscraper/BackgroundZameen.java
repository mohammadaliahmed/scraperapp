package com.appsinventiv.numberscraper;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by AliAh on 25/02/2018.
 */

public class BackgroundZameen extends AsyncTask<String, Void, String> {
    Context context;


    public BackgroundZameen(Context ctx) {
        context = ctx;

    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String webUrl = "http://getnumbers.co/app/zameen.php";
        if (type.equals("login")) {
            try {
                URL url = new URL(webUrl);
                String url_g = params[1];
                String page = params[2];
                String file = params[3];
                String links = params[4];
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("url", "UTF-8") + "=" + URLEncoder.encode(url_g, "UTF-8") + "&"
                        + URLEncoder.encode("page", "UTF-8") + "=" + URLEncoder.encode(page, "UTF-8") + "&"
                        + URLEncoder.encode("links", "UTF-8") + "=" + URLEncoder.encode(links, "UTF-8") + "&"
                        + URLEncoder.encode("filename", "UTF-8") + "=" + URLEncoder.encode(file, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String res = "", line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    res += line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return res;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}