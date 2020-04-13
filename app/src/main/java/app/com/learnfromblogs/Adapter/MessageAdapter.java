package app.com.learnfromblogs.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Model.DeleteMyCommnent.ModelDeleteComment;
import app.com.learnfromblogs.Model.MyComment.Datum;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Champ on 17-07-2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<Datum> commentList;
    private String strTokenId,strUid;


    public MessageAdapter(Context context, List<Datum> commentList) {
        this.context = context;
        this.commentList = commentList;
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");




    }

    public int getItemCount() {
        return commentList.size();
    }


    public MessageAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new MessageAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_message_item, parent, false), this);
    }

    public void onBindViewHolder(final MessageAdapter.ViewHolder viewHolder, final int position) {

            viewHolder.txtTitle.setText(commentList.get(position).getTitle());
            viewHolder.txtComment.setText(commentList.get(position).getMessage());

            viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    apiDelete(commentList.get(position).getId(),position);
                }
            });
    }

    private void apiDelete(String id, final int position) {

        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelDeleteComment> call1 = apiService.api_delete_my_comment(strTokenId,strUid,""+id);
        call1.enqueue(new Callback<ModelDeleteComment>() {
            @Override
            public void onResponse(Call<ModelDeleteComment> call, retrofit2.Response<ModelDeleteComment> response) {
                loading.dismiss();

//                {"success":true,"data":[],"message":"Comment has been deleted successfully!","status":200}

                if(response.body().getSuccess()== true){
                    commentList.remove(position);
                    notifyDataSetChanged();
                }
                Toast.makeText(context,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<ModelDeleteComment> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtComment;
        ImageView imgDelete;
        public ViewHolder(View itemView, MessageAdapter adapter) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.profile_message_item_txt_title);
            txtComment = itemView.findViewById(R.id.profile_message_item_txt_comment);
            imgDelete = itemView.findViewById(R.id.profile_message_item_img_delete);

        }


    }




}