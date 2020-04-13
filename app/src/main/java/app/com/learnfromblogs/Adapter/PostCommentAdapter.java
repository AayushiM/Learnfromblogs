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
import app.com.learnfromblogs.Utils.Constants;


/**
 * Created by Champ on 17-07-2018.
 */

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<app.com.learnfromblogs.Model.PostCommentList.Datum> commentList;
    private String strTokenId,strUid;


    public PostCommentAdapter(Context context, List<app.com.learnfromblogs.Model.PostCommentList.Datum> commentList) {
        this.context = context;
        this.commentList = commentList;
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");




    }


    public int getItemCount() {
        return commentList.size();
    }


    public PostCommentAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new PostCommentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_message_item, parent, false), this);
    }

    public void onBindViewHolder(final PostCommentAdapter.ViewHolder viewHolder, final int position) {

            viewHolder.txtTitle.setText(commentList.get(position).getTitle());
            viewHolder.txtComment.setText(commentList.get(position).getMessage());


    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtComment;
        ImageView imgDelete;
        public ViewHolder(View itemView, PostCommentAdapter adapter) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.profile_message_item_txt_title);
            txtComment = itemView.findViewById(R.id.profile_message_item_txt_comment);
            imgDelete = itemView.findViewById(R.id.profile_message_item_img_delete);
            imgDelete.setVisibility(View.GONE);

        }


    }




}