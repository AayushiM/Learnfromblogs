package app.com.learnfromblogs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.Fragment.FeedFragment;
import app.com.learnfromblogs.R;


/**
 * Created by Champ on 17-07-2018.
 */

public class FeedTypeAdapter extends RecyclerView.Adapter<FeedTypeAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<String> feedType;
    String mSerialized;





    public FeedTypeAdapter(Context context,  List<String> feedType) {
        this.context = context;
        this.feedType = feedType;
    }

    public int getItemCount() {
        return feedType.size();
    }


    public FeedTypeAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new FeedTypeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_type_item, parent, false), this);
    }

    public void onBindViewHolder(final FeedTypeAdapter.ViewHolder viewHolder, final int position) {

            viewHolder.txtTitle.setText(feedType.get(position));
//            viewHolder.lvMain.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    check = position;
//                    notifyDataSetChanged();
//                }
//            });


        if(FeedFragment.check == position){
            viewHolder.lvMain.setBackgroundResource(R.drawable.border_round_white_bc_white);
            viewHolder.txtTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            viewHolder.lvMain.setBackgroundResource(R.drawable.border_round_white);
            viewHolder.txtTitle.setTextColor(context.getResources().getColor(R.color.white));

        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle;
        LinearLayout lvMain;
        FeedTypeAdapter adapter;
        public ViewHolder(View itemView, FeedTypeAdapter adapter) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.feed_type_txt_type_name);
            lvMain = itemView.findViewById(R.id.feed_type_lv_main);
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