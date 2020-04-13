package app.com.learnfromblogs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.R;


/**
 * Created by Champ on 17-07-2018.
 */

public class MyPointAdapter extends RecyclerView.Adapter<MyPointAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<app.com.learnfromblogs.Model.Point.Datum> pointList;
    String strUid, strTokenId;
    String mSerialized;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String type;

    public MyPointAdapter(Context context, List<app.com.learnfromblogs.Model.Point.Datum> pointList) {
        this.context = context;
        this.pointList = pointList;


    }


    public int getItemCount() {
        return pointList.size();
    }


    public MyPointAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new MyPointAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.point_item, parent, false), this);
    }

    public void onBindViewHolder(final MyPointAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.txtActivity.setText("Activity : "+pointList.get(position).getActivity());
        viewHolder.txtPoint.setText("Point : "+pointList.get(position).getPoints());
        viewHolder.txtDate.setText("Date : "+pointList.get(position).getCreatedAt());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtActivity, txtPoint, txtDate;

        public ViewHolder(View itemView, MyPointAdapter adapter) {
            super(itemView);

            txtActivity = itemView.findViewById(R.id.point_item_txt_activity);
            txtPoint = itemView.findViewById(R.id.point_item_txt_point);
            txtDate = itemView.findViewById(R.id.point_item_txt_date);


        }


    }


}