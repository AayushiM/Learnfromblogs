package app.com.learnfromblogs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import app.com.learnfromblogs.R;

public class UploadListImage extends RecyclerView.Adapter<UploadListImage.MyViewHolder> {
    private Context context;
    private ArrayList<File> files;


    public UploadListImage(Context context, ArrayList<File> files) {
        super();
        this.context = context;
        this.files = files;

    }

    @NonNull
    @Override
    public UploadListImage.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_report_post,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UploadListImage.MyViewHolder holder, final int position) {
        if (files.size()!=0) {
//            Glide.with(context)
//                    .load(files.get(position))
//                    .into(holder.ivExtra);
        }


        holder.layoutClearImg.setOnClickListener(view -> {
            files.remove(position);
            notifyDataSetChanged();
        });


    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivExtra,layoutClearImg;
        MyViewHolder(View itemView) {
            super(itemView);


        }
    }
}
