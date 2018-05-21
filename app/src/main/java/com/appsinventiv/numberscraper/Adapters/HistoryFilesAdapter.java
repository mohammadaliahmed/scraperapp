package com.appsinventiv.numberscraper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.numberscraper.R;
import com.appsinventiv.numberscraper.Utils.CommonUtils;
import com.appsinventiv.numberscraper.Utils.Constants;
import com.appsinventiv.numberscraper.Utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AliAh on 03/05/2018.
 */

public class HistoryFilesAdapter extends RecyclerView.Adapter<HistoryFilesAdapter.ViewHolder> {
    ArrayList<String> itemList = new ArrayList<>();
    Context context;
    private LayoutInflater mInflater;
    WhichFileToDownload whichFileToDownload;

    public HistoryFilesAdapter(Context context, ArrayList<String> itemList,WhichFileToDownload whichFileToDownload) {
        this.mInflater = LayoutInflater.from(context);

        this.itemList = itemList;
        this.context = context;
        this.whichFileToDownload=whichFileToDownload;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.files_layout, parent, false);
        HistoryFilesAdapter.ViewHolder viewHolder = new HistoryFilesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        final String model = itemList.get(position);

        String abc = model.replace(Constants.TEXT_FILES, "");
        abc = abc.replace("-", " ");
        abc = abc.replace("olx", "-olx");
        abc = abc.replace("/", "").replace(" ","");
        abc = abc.replace(SharedPrefs.getUsername(), "");


        holder.title.setText((position+1)+". "+abc);

        holder.viewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model));
                context.startActivity(i);

            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                whichFileToDownload.onClick(model);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public Button viewFile,download;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            viewFile = itemView.findViewById(R.id.viewFile);

            download = itemView.findViewById(R.id.download);

        }
    }
    public interface WhichFileToDownload{
        public void onClick(String url);
    }
}
