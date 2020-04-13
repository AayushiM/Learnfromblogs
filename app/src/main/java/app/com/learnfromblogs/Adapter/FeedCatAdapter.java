package app.com.learnfromblogs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.Model.TopicCategory.Datum;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.Constants;


/**
 * Created by Champ on 17-07-2018.
 */

public class FeedCatAdapter extends RecyclerView.Adapter<FeedCatAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    String strUid,strToken;
    List<Datum> catList;

    public FeedCatAdapter(Context context, List<Datum> catList) {
        this.context = context;
        this.catList = catList;
        strToken = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public int getItemCount() {
        return catList.size();
    }


    public FeedCatAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new FeedCatAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item, parent, false), this);
    }

    public void onBindViewHolder(final FeedCatAdapter.ViewHolder viewHolder, final int position) {


        viewHolder.txtName.setText(catList.get(position).getName());


    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView txtName;
        FeedCatAdapter feedCatAdapter;


        public ViewHolder(View itemView, FeedCatAdapter feedCatAdapter) {
            super(itemView);



            txtName = itemView.findViewById(R.id.cat_item_txt_cat_item);
            this.feedCatAdapter = feedCatAdapter;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            this.feedCatAdapter.setItemClick(this);
        }




    }

    private void setItemClick(ViewHolder viewHolder) {
        this.onItemClickListener.onItemClick(null, viewHolder.itemView, viewHolder.getAdapterPosition(), viewHolder.getItemId());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}