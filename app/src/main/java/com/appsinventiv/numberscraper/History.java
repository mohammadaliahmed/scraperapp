package com.appsinventiv.numberscraper;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.appsinventiv.numberscraper.Adapters.HistoryFilesAdapter;
import com.appsinventiv.numberscraper.Interface.UserClient;
import com.appsinventiv.numberscraper.Utils.CommonUtils;
import com.appsinventiv.numberscraper.Utils.DownloadFile;
import com.appsinventiv.numberscraper.Utils.SharedPrefs;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class History extends AppCompatActivity implements SearchView.OnQueryTextListener {
    HistoryFilesAdapter adapter;
    ArrayList<String> itemList=new ArrayList<>();
    ArrayList<String> itemList1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.setTitle("History");

        RecyclerView recyclerViewApple = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager horizontalLayoutManagaerCars
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewApple.setLayoutManager(horizontalLayoutManagaerCars);
        adapter = new HistoryFilesAdapter(this, itemList, new HistoryFilesAdapter.WhichFileToDownload() {
            @Override
            public void onClick(String url) {
                DownloadFile.fromUrl(url,CommonUtils.getFilename(url));
            }
        });
        recyclerViewApple.setAdapter(adapter);

        getDataFromServer();
    }

    private void getDataFromServer() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://getnumbers.co/app/")
                .addConverterFactory(GsonConverterFactory.create());
        final HashMap<String, String> map = new HashMap<>();
        map.put("userId", "4");
        map.put("createdAt", "0");
        JSONObject json = new JSONObject(map);
        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<FilesHistoryModel> call = client.createTask(json);
        call.enqueue(new Callback<FilesHistoryModel>() {
            @Override
            public void onResponse(Call<FilesHistoryModel> call, Response<FilesHistoryModel> response) {

                Gson gson = new Gson();

                for (int i=0;i<response.body().getFiles().size();i++){
                    if(response.body().getFiles().get(i).contains(SharedPrefs.getUsername())) {
                        itemList.add(response.body().getFiles().get(i));
                        itemList1.add(response.body().getFiles().get(i));

                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFailure(Call<FilesHistoryModel> call, Throwable t) {
//                Log.d("abcc",""+t);
                CommonUtils.showToast(t+"");
//                Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        itemList.clear();
        for(String item:itemList1){
            if(item.contains(newText)){
                itemList.add(item);
                adapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
