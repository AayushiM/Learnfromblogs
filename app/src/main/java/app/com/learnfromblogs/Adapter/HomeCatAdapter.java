package app.com.learnfromblogs.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Model.Login.ModelLogin;
import app.com.learnfromblogs.Model.TopicCategory.Datum;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Champ on 17-07-2018.
 */

public class HomeCatAdapter extends RecyclerView.Adapter<HomeCatAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    String strUid,strToken;
    List<Datum> catList;

    public HomeCatAdapter(Context context, List<Datum> catList) {
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


    public HomeCatAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new HomeCatAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_item, parent, false), this);
    }

    public void onBindViewHolder(final HomeCatAdapter.ViewHolder viewHolder, final int position) {



        viewHolder.txtName.setText(catList.get(position).getName());
        Picasso.with(context).load(catList.get(position).getImageLarge()).into(viewHolder.imgUser);


        viewHolder.lvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(catList.get(position).getIsSelected() == 1) {
                    viewHolder.imgSelect.setVisibility(View.GONE);
                    catList.get(position).setIsSelected(0);
                }else {
                    viewHolder.imgSelect.setVisibility(View.VISIBLE);
                    catList.get(position).setIsSelected(1);

                }
                apiselectUnselect(catList.get(position).getId(),position);



            }
        });

//
        if(catList.get(position).getIsSelected() == 1){
            viewHolder.imgSelect.setVisibility(View.VISIBLE);
        }else {
            viewHolder.imgSelect.setVisibility(View.GONE);
        }



    }

    public void updateList(List<Datum> mList){
        this.catList = mList;
        notifyDataSetChanged();
    }

    private void apiselectUnselect(String id, final int position) {

        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelLogin> call1 = apiService.api_selected_cat(strToken,strUid,id);
        call1.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, retrofit2.Response<ModelLogin> response) {
                loading.dismiss();
                if(response.isSuccessful()) {
                    if (response.body().getSuccess() == true) {


                    }
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_LONG).show();

                }
            }


            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        HomeCatAdapter adapter;
        ImageView imgUser;
        TextView txtName,txtNoOfFollowers;
        RelativeLayout lvMain;
        ImageView imgSelect;

        public ViewHolder(View itemView, HomeCatAdapter adapter) {
            super(itemView);


            imgUser = itemView.findViewById(R.id.home_cat_item_img_cat);
            txtName = itemView.findViewById(R.id.home_cat_item_txt_cat);
            lvMain = itemView.findViewById(R.id.home_cat_item_lv_main);
            imgSelect = itemView.findViewById(R.id.home_cat_img_select);


//
        }


    }


}