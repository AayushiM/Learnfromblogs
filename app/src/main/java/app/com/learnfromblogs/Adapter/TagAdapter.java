package app.com.learnfromblogs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.R;


/**
 * Created by Champ on 17-07-2018.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<String> tagList;




    public TagAdapter(Context context, List<String> tagList) {
        this.context = context;
        this.tagList = tagList;


    }

    public int getItemCount() {
        return tagList.size();
    }


    public TagAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new TagAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false), this);
    }

    public void onBindViewHolder(final TagAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtTitle.setText(tagList.get(position));

        viewHolder.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagList.remove(position);
                notifyDataSetChanged();
            }
        });



    }
    public class ViewHolder extends RecyclerView.ViewHolder  {


        TextView txtTitle;
        ImageView imgClose;
        TagAdapter adapter;
        public ViewHolder(View itemView, TagAdapter adapter) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.tag_item_txt_tagname);
            imgClose = itemView.findViewById(R.id.tag_item_img_close);


        }


//        @Override
//        public void onClick(View v) {
//
//            this.adapter.setItemClick(this);
//        }


    }

    private void setItemClick(ViewHolder viewHolder) {
        this.onItemClickListener.onItemClick(null, viewHolder.itemView, viewHolder.getAdapterPosition(), viewHolder.getItemId());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}