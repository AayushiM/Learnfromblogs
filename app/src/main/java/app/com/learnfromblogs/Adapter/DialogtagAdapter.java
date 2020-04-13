package app.com.learnfromblogs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.Model.Tag.Datum;
import app.com.learnfromblogs.R;


/**
 * Created by Champ on 17-07-2018.
 */

public class DialogtagAdapter extends RecyclerView.Adapter<DialogtagAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<Datum> tagList;




    public DialogtagAdapter(Context context, List<Datum> tagList) {
        this.context = context;
        this.tagList = tagList;


    }

    public int getItemCount() {
        return tagList.size();
    }


    public DialogtagAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new DialogtagAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dialoge_tag_item, parent, false), this);
    }

    public void onBindViewHolder(final DialogtagAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtTitle.setText(tagList.get(position).getTitle());



    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtTitle;
        DialogtagAdapter adapter;
        public ViewHolder(View itemView, DialogtagAdapter adapter) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.dialog_tag_iten_txt_name);
            this.adapter = adapter;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            this.adapter.setItemClick(this);
        }


    }

    private void setItemClick(ViewHolder viewHolder) {
        this.onItemClickListener.onItemClick(null, viewHolder.itemView, viewHolder.getAdapterPosition(), viewHolder.getItemId());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}