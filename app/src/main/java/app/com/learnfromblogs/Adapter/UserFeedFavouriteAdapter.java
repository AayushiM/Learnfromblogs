package app.com.learnfromblogs.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chinalwb.are.render.AreTextView;
import com.github.irshulx.Editor;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.BottomActivity;
import app.com.learnfromblogs.Activity.CommentActivity;
import app.com.learnfromblogs.Fragment.CommentsFragment;
import app.com.learnfromblogs.Fragment.OtherUserProfile;
import app.com.learnfromblogs.Model.AddFav.ModelAddFav;
import app.com.learnfromblogs.Model.AllFeed.Datum;
import app.com.learnfromblogs.Model.AllFeed.Tagged;
import app.com.learnfromblogs.Model.repoer.ReportModel;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Champ on 17-07-2018.
 */

public class UserFeedFavouriteAdapter extends RecyclerView.Adapter<UserFeedFavouriteAdapter.ViewHolder> {

    Context context;
    public AdapterView.OnItemClickListener onItemClickListener;
    List<Datum> feedList;
    private  String strTokenId, strUid;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    String mSerialized;
    private LayoutInflater layoutInflater;

    public UserFeedFavouriteAdapter(Context context, List<Datum> feedList) {
        this.context = context;
        this.feedList = feedList;
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface MasterClickListner {
        void onMasterClick(String  pos);

    }
    private FeedAdapter.MasterClickListner listner;

    public void setListner(FeedAdapter.MasterClickListner listner) {
        this.listner = listner;
    }


    public int getItemCount() {
        return feedList.size();
    }


    public UserFeedFavouriteAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        return new UserFeedFavouriteAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_data_item, parent, false), this);
    }

    public void onBindViewHolder(final UserFeedFavouriteAdapter.ViewHolder viewHolder, final int position) {


        if(feedList.get(position).getPostType().equals("ads")){
            viewHolder.cardAdvertisement.setVisibility(View.VISIBLE);
            viewHolder.cardPost.setVisibility(View.GONE);
            Picasso.with(context).load(feedList.get(position).getImageLarge()).into(viewHolder.imgAdvertisement);

        }else {

        viewHolder.cardAdvertisement.setVisibility(View.GONE);
        viewHolder.cardPost.setVisibility(View.VISIBLE);
        Log.e("feedListId",feedList.get(position).getId());
        viewHolder.txtCategory.setText(feedList.get(position).getCategorySlug());
        viewHolder.txtTitle.setText(feedList.get(position).getTitle());
        if(feedList.get(position).getIs_boosted().equals("1")){
            viewHolder.tvslash1.setVisibility(View.VISIBLE);
            viewHolder.ivBoost.setVisibility(View.VISIBLE);

        }
        else {
            viewHolder.tvslash1.setVisibility(View.GONE);
            viewHolder.ivBoost.setVisibility(View.GONE);
        }
        if(feedList.get(position).getPost_link()==null){
            viewHolder.tvslash2.setVisibility(View.GONE);
            viewHolder.ivLink.setVisibility(View.GONE);
        }
        else {
            viewHolder.tvslash2.setVisibility(View.VISIBLE);
            viewHolder.ivLink.setVisibility(View.VISIBLE);
        }



        viewHolder.ivLink.setOnClickListener(view -> {
//                Toast.makeText(context, feedList.get(position).getPost_link(), Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https://learnfromblogs.com/"+feedList.get(position).getSlug()+"/"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

        viewHolder.txtTitle.setOnClickListener(view -> {

            Uri uri = Uri.parse("https://learnfromblogs.com/"+feedList.get(position).getSlug()+"/"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

        });
        viewHolder.txtUserProfile.setText(feedList.get(position).getFirstName() + " " + feedList.get(position).getLastName());
        Picasso.with(context).load(feedList.get(position).getUserThumbImg()).into(viewHolder.circleUserProfile);
            StringBuilder tags = new StringBuilder();
            viewHolder.flowLayout.removeAllViews();
            for (Tagged tagged: feedList.get(position).getTagged()) {
                View view = layoutInflater.inflate(R.layout.tag_layout, null);
                TextView textView = view.findViewById(R.id.tag_textview);
                textView.setText(tagged.getTitle());
                viewHolder.flowLayout.addView(view);
                textView.setOnClickListener(v -> {
                    if (listner!=null)
                        listner.onMasterClick(tagged.getSlug());
                });
            }
        if(feedList.get(position).getImage() == null){
            viewHolder.imgMain.setVisibility(View.GONE);
        }else {
            viewHolder.imgMain.setVisibility(View.VISIBLE);
            Picasso.with(context).load(feedList.get(position).getImageThumb()).into(viewHolder.imgMain);
        }

        if(feedList.get(position).getUserId().equals(strUid)){
            viewHolder.imgReport.setVisibility(View.GONE);
        }

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

        viewHolder.txtUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new OtherUserProfile();
                BottomActivity bottomActivity = (BottomActivity)context;
                fragmentManager = bottomActivity.getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("id",""+feedList.get(position).getUserId());
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("Result");
                fragmentTransaction.commit();
            }
        });


//
//        if(feedList.get(position).getContent() != null && feedList.get(position).getContent().length() > 0) {
////            String mSerializedHtml= viewHolder.renderer.getContentAsHTML(feedList.get(position).getContent());
//            viewHolder.renderer.render(feedList.get(0).getContent());
//        }
//        myTextview.setText(Html.fromHtml(myHtml, new ImageGetter(), null);
//        viewHolder.txtContent.setText(Html.fromHtml(feedList.get(position).getContent()));
//        Log.e("Content",feedList.get(position).getContent());
//
//        Spanned formattedHtml = HtmlFormatter.formatHtml(new HtmlFormatterBuilder().setHtml(feedList.get(position).getContent()).setImageGetter(new HtmlResImageGetter( viewHolder.htmlTextView.getContext())));
//        viewHolder.htmlTextView.setText(formattedHtml);


        if (feedList.get(position).getContent() != null && feedList.get(position).getContent().length() > 0) {
            viewHolder.areTextView.fromHtml(feedList.get(position).getContent());
        }

        viewHolder.imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPost(feedList.get(position));
            }
        });

        viewHolder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedList.get(position).getIsFavourite() != 1) {
                    apiFav(feedList.get(position).getId(), viewHolder.imgFav);
                }
            }
        });

        if (feedList.get(position).getIsFavourite() == 1){
            viewHolder.imgFav.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        viewHolder.imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("id",""+feedList.get(position).getId());
                context.startActivity(intent);
//                    fragment = new CommentsFragment();
//                    BottomActivity bottomActivity = (BottomActivity)context;
//                    fragmentManager = bottomActivity.getSupportFragmentManager();
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id",""+feedList.get(position).getId());
//                    fragment.setArguments(bundle);
//                    fragmentTransaction.replace(R.id.content_frame, fragment);
//                    fragmentTransaction.addToBackStack("Result");
//                    fragmentTransaction.commit();
            }
        });
        }





    }

    private void reportPost(final Datum datum) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_report_post);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Spinner spinner = dialog.findViewById(R.id.dialog_report_spinner_category);
        final EditText editText = dialog.findViewById(R.id.dialog_report_edittext);
        List<String> list = new ArrayList<>();
        list.add("Abusive");
        list.add("Spam");
        list.add("Inappropriate");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(dataAdapter);
        Button send = dialog.findViewById(R.id.dialog_report_button_send);
        dialog.findViewById(R.id.dialog_report_img_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = new ProgressDialog(context);
                loading.setMessage("Please Wait..");
                loading.setCancelable(false);
                loading.setCanceledOnTouchOutside(false);
                loading.show();
                API apiService = APIClient.getClient().create(API.class);
                Call<ReportModel> call1 = apiService.api_flag_post(strTokenId,strUid, datum.getUserId(), datum.getId(), editText.getText().toString().trim(), spinner.getSelectedItem().toString());
                call1.enqueue(new Callback<ReportModel>() {
                    @Override
                    public void onResponse(Call<ReportModel> call, retrofit2.Response<ReportModel> response) {
                        loading.dismiss();
                        try {
                            if (response.body().getSuccess()){
                                dialog.cancel();
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportModel> call, Throwable t) {
                        loading.dismiss();
                        Log.e("loginData", t.getMessage() + "");
                    }
                });
            }
        });
        dialog.show();


    }

    private void apiFav(String id, final ImageView imgFav) {

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
                    if(response.body().getData().getTitle().equals("favourite")){

                    }
                    imgFav.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(context,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }


            }


            @Override
            public void onFailure(Call<ModelAddFav> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });

    }



    public class ViewHolder extends RecyclerView.ViewHolder  {

        CircleImageView circleUserProfile;
        TextView txtUserProfile,txtContent,txtTitle,txtCategory,tvslash1,tvslash2,txttags;
        Editor renderer;
        FlowLayout flowLayout;
        AreTextView areTextView;
        CardView cardPost,cardAdvertisement;
        ImageView imgAdvertisement,imgFav,imgComment,imgMain,imgShare,imgReport,ivLink,ivBoost;

        Button btnCat;
        public ViewHolder(View itemView, UserFeedFavouriteAdapter adapter) {
            super(itemView);
            circleUserProfile = itemView.findViewById(R.id.feed_data_item_img_user_profile);
            txtUserProfile = itemView.findViewById(R.id.feed_data_item_txt_username);
            txtTitle = itemView.findViewById(R.id.feed_data_txt_title);
            txtCategory = itemView.findViewById(R.id.feed_data_txt_category);
            renderer = (Editor)itemView.findViewById(R.id.feed_data_item_txt_renderer);
            txtContent = (TextView)itemView.findViewById(R.id.feed_data_item_txt_content);
            imgFav = itemView.findViewById(R.id.feed_data_item_img_fav);
            imgReport = itemView.findViewById(R.id.feed_data_item_img_flag);
            imgComment = itemView.findViewById(R.id.feed_data_item_img_comment);
            imgMain = itemView.findViewById(R.id.feed_data_item_img_main);
            imgShare = itemView.findViewById(R.id.feed_data_item_img_share);
            imgAdvertisement = (ImageView)itemView.findViewById(R.id.feed_data_img_advertisement);
            cardPost = (CardView) itemView.findViewById(R.id.feed_data_card_post);
            cardAdvertisement = (CardView)itemView.findViewById(R.id.feed_data_card_advertisement);
            areTextView = itemView.findViewById(R.id.areTextView);
            txttags = itemView.findViewById(R.id.textview_tags);
            ivLink = itemView.findViewById(R.id.ivLink);
            ivBoost = itemView.findViewById(R.id.ivBoost);
            tvslash1 = itemView.findViewById(R.id.tvslash1);
            tvslash2 = itemView.findViewById(R.id.tvslash2);
            flowLayout = itemView.findViewById(R.id.flowlayout);

        }




    }


}