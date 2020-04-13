package app.com.learnfromblogs.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.are.render.AreTextView;
import com.github.irshulx.Editor;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.BottomActivity;
import app.com.learnfromblogs.Activity.CommentActivity;
import app.com.learnfromblogs.Activity.EditPostFragment;
import app.com.learnfromblogs.Fragment.CommentsFragment;
import app.com.learnfromblogs.Model.AddFav.ModelAddFav;
import app.com.learnfromblogs.Model.PersonalUserPost.Datum;
import app.com.learnfromblogs.Model.PostDelete.ModelDelete;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Champ on 17-07-2018.
 */

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<Datum> feedList;
    String strUid,strTokenId;
    String mSerialized;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String type;

    public MyFeedAdapter(Context context, List<Datum> feedList, String type) {
        this.context = context;
        this.feedList = feedList;
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");
        this.type = type;



    }

    public int getItemCount() {
        return feedList.size();
    }


    public MyFeedAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new MyFeedAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false), this);
    }

    public void onBindViewHolder(final MyFeedAdapter.ViewHolder viewHolder, final int position) {


        viewHolder.setIsRecyclable(false);
        if(feedList.get(position).getImage() == null){
            viewHolder.imgPost.setVisibility(View.GONE);
        }else {

            viewHolder.imgPost.setVisibility(View.VISIBLE);
            Picasso.with(context).load(feedList.get(position).getImageThumb()).into(viewHolder.imgPost);
        }

        if(feedList.get(position).getUserThumbImg() == null){
            viewHolder.imgProfile.setVisibility(View.GONE);
        }else {
            viewHolder.imgProfile.setVisibility(View.VISIBLE);
            Picasso.with(context).load(feedList.get(position).getUserThumbImg()).into(viewHolder.imgProfile);
        }
        viewHolder.txtCat.setText(feedList.get(position).getCategorySlug());
        viewHolder.txtTitle.setText(feedList.get(position).getTitle());
//
        viewHolder.txtUserName.setText(feedList.get(position).getFirstName()+" "+feedList.get(position).getLastName());

//        if(feedList.get(position).getIs_boosted().equals("0")){
//            viewHolder.tvslash1.setVisibility(View.GONE);
//            viewHolder.ivBoost.setVisibility(View.GONE);
//        }
//        else {
//            viewHolder.tvslash1.setVisibility(View.VISIBLE);
//            viewHolder.ivBoost.setVisibility(View.VISIBLE);
//        }
//        if(feedList.get(position).getPost_link()==null){
//            viewHolder.tvslash2.setVisibility(View.GONE);
//            viewHolder.ivLink.setVisibility(View.GONE);
//        }
//        else {
//            viewHolder.tvslash2.setVisibility(View.VISIBLE);
//            viewHolder.ivLink.setVisibility(View.VISIBLE);
//        }
//


//        viewHolder.ivLink.setOnClickListener(view -> {
////                Toast.makeText(context, feedList.get(position).getPost_link(), Toast.LENGTH_SHORT).show();
//            Uri uri = Uri.parse("https://learnfromblogs.com/"+feedList.get(position).getSlug()+"/"); // missing 'http://' will cause crashed
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            context.startActivity(intent);
//        });

        viewHolder.txtTitle.setOnClickListener(view -> {

            Uri uri = Uri.parse("https://learnfromblogs.com/"+feedList.get(position).getSlug()+"/"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

        });
        if(feedList.get(position).getContent() != null && feedList.get(position).getContent().length() > 0) {
            viewHolder.areTextView.fromHtml(feedList.get(position).getContent());
        }


        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiDelete(position,feedList.get(position).getId());
            }
        });



        viewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EditPostFragment.class);
                i.putExtra("id",""+feedList.get(position).getId());
                context.startActivity(i);
            }
        });

        viewHolder.imgAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedList.get(position).getIsFavourite() != 1){
                    apiFav(feedList.get(position).getId(), viewHolder.imgAddFav);
                }

            }
        });

        if (String.valueOf(feedList.get(position).getIsFavourite()).equals("1")){
            viewHolder.imgAddFav.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        }



        viewHolder.btnPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPromote(""+feedList.get(position).getTitle(),""+feedList.get(position).getId());
            }
        });


        viewHolder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("id",""+feedList.get(position).getId());
                context.startActivity(intent);
//                fragment = new CommentsFragment();
//                BottomActivity bottomActivity = (BottomActivity)context;
//                fragmentManager = bottomActivity.getSupportFragmentManager();
//                fragmentTransaction = fragmentManager.beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putString("id",""+feedList.get(position).getId());
//                fragment.setArguments(bundle);
//                fragmentTransaction.replace(R.id.content_frame, fragment);
//                fragmentTransaction.addToBackStack("Result");
//                fragmentTransaction.commit();
            }
        });


        viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, feedList.get(position).getTitle()+"\n\n"+"https://learnfromblogs.com/"+feedList.get(position).getSlug()+"/");
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, "Share");
                    context.startActivity(shareIntent);
                }

        });





    }

    private void apiFav(Integer id, final ImageView imgAddFav) {

        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelAddFav> call1 = apiService.api_add_fav_post(strTokenId,strUid,""+id);
        call1.enqueue(new Callback<ModelAddFav>() {
            @Override
            public void onResponse(Call<ModelAddFav> call, retrofit2.Response<ModelAddFav> response) {
                loading.dismiss();
                if(response.body().getSuccess()== true) {
                    Toast.makeText(context, "Successfully added to favorite", Toast.LENGTH_SHORT).show();
                    imgAddFav.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                    if(response.body().getData().getTitle().equals("favourite")){

                    }
                }


            }


            @Override
            public void onFailure(Call<ModelAddFav> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });

    }

    private void DialogPromote(String title, final String postid) {
        final Dialog alertDialog = new Dialog(context);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        alertDialog.getWindow().setAttributes(lp);
        final View rootView = LayoutInflater.from(context).inflate(R.layout.dialoge_promote, null);

        Button btnClose = rootView.findViewById(R.id.dialoge_promote_btn_close);
        Button btnSubmit = rootView.findViewById(R.id.dialoge_promote_btn_submit);
        TextView txtTitle = rootView.findViewById(R.id.dialoge_promote_txt_title);
        final TextView txtDescription = rootView.findViewById(R.id.dialoge_promote_txt_description);
        final RadioButton rb7Day = rootView.findViewById(R.id.dialoge_promote_rb_7day);
        final RadioButton rb15Day = rootView.findViewById(R.id.dialoge_promote_rb_15day);
        RadioButton rb30Day = rootView.findViewById(R.id.dialoge_promote_rb_30day);

        rb7Day.setChecked(true);
        txtDescription.setText("Promote your post on the first page and show your skill to other member for 7pts in 1500");

        rb7Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDescription.setText("Promote your post on the first page and show your skill to other member for 7pts in 1500");
            }
        });
        rb15Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDescription.setText("Promote your post on the first page and show your skill to other member for 15pts in 2500");
            }
        });
        rb30Day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDescription.setText("Promote your post on the first page and show your skill to other member for 30pts in 4000");
            }
        });




        txtTitle.setText(title);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strDay ;
                if(rb7Day.isChecked()){
                    strDay  = "7";
                }else if(rb15Day.isChecked()){
                    strDay  = "15";
                }else {
                    strDay  = "30";
                }
                apiBoostPost(strDay,postid);
            }
        });




        alertDialog.setContentView(rootView);

        alertDialog.show();
    }

    private void apiBoostPost(String strDay, String postid) {

        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ResponseBody> call1 = apiService.api_post_boost(strTokenId,strUid,""+postid,strDay);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                loading.dismiss();
                try {
                    Log.e("ResponseBody", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                {"success":false,"message":"Post pending approval","status":200}

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });

    }

    private void apiDelete(final int position, Integer id) {

        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelDelete> call1 = apiService.api_post_delete(strTokenId,strUid,""+id);
        call1.enqueue(new Callback<ModelDelete>() {
            @Override
            public void onResponse(Call<ModelDelete> call, retrofit2.Response<ModelDelete> response) {
                loading.dismiss();

//                {"success":true,"data":[],"message":"Post has been deleted successfully!","status":200}
                if(response.body().getSuccess()== true){
                    feedList.remove(position);
                    notifyDataSetChanged();
                }
                Toast.makeText(context,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<ModelDelete> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView imgPost,imgProfile;
        TextView txtCat,txtTitle,txtDesc,txtUserName,tvslash1,tvslash2;
        Editor renderer;
        AreTextView areTextView;
        ImageView imgDelete,imgEdit,imgAddFav,imgComment,imgShare,ivLink,ivBoost;
        Button btnPromote;


        public ViewHolder(View itemView, MyFeedAdapter adapter) {
            super(itemView);

            imgPost = itemView.findViewById(R.id.feed_item_img_post);
            imgProfile = itemView.findViewById(R.id.feed_item_img_profile);
            txtCat = itemView.findViewById(R.id.feed_item_txt_category);
            txtTitle = itemView.findViewById(R.id.feed_item_txt_title);
            txtDesc = itemView.findViewById(R.id.feed_item_txt_description);
            txtUserName = itemView.findViewById(R.id.feed_item_txt_user_name);
            renderer = (Editor)itemView.findViewById(R.id.feed_item_txt_renderer);
            imgDelete = (ImageView) itemView.findViewById(R.id.feed_item_img_delete);
            imgShare = (ImageView) itemView.findViewById(R.id.feed_item_img_share);
            imgEdit = (ImageView) itemView.findViewById(R.id.feed_item_img_edit);
            imgAddFav = (ImageView) itemView.findViewById(R.id.feed_item_img_fav);
            imgComment = (ImageView) itemView.findViewById(R.id.feed_item_img_comment);
            areTextView = itemView.findViewById(R.id.areTextView);
            btnPromote = itemView.findViewById(R.id.feed_item_btn_promote);
//            ivLink = itemView.findViewById(R.id.ivLink);
//            ivBoost = itemView.findViewById(R.id.ivBoost);
//            tvslash1 = itemView.findViewById(R.id.tvslash1);
//            tvslash2 = itemView.findViewById(R.id.tvslash2);
            if(type.equals("other")){
                btnPromote.setVisibility(View.GONE);
            }



        }




    }


}