package app.com.learnfromblogs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Adapter.PostCommentAdapter;
import app.com.learnfromblogs.Model.DeleteMyCommnent.ModelDeleteComment;
import app.com.learnfromblogs.Model.PostCommentList.Datum;
import app.com.learnfromblogs.Model.PostCommentList.ModelPostComment;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class CommentActivity extends AppCompatActivity {
    private Context mContext = this;
    Button btnPost;
    EditText edMessage;
    String strTokenId,strUid,strPostId;
    List<Datum> commentList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rcComment;
    PostCommentAdapter commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Post comment");

        Constants.hideKeyboard(CommentActivity.this);
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");
        rcComment = findViewById(R.id.comments_recyclerview);

        strPostId = getIntent().getStringExtra("id");
        btnPost = findViewById(R.id.comments_btn_post);
        edMessage = findViewById(R.id.comments_ed_add_comment);
        Log.e("strPostId",strPostId);
        getComments();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edMessage.getText().toString().equals("")){
                    apiAddComment();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void apiAddComment() {

        final ProgressDialog loading = new ProgressDialog(mContext);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelDeleteComment> call1 = apiService.api_comment_submit(strTokenId,strUid,strPostId,edMessage.getText().toString());
        call1.enqueue(new Callback<ModelDeleteComment>() {
            @Override
            public void onResponse(Call<ModelDeleteComment> call, retrofit2.Response<ModelDeleteComment> response) {
                loading.dismiss();
//                {"success":true,"data":[],"message":"You have commented successfully, It will show after approved by admin!","status":200}
                if(response.body().getSuccess()== true){
                    edMessage.setText("");
                    finish();
                }
//                Toast.makeText(getActivity(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<ModelDeleteComment> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }



    private void getComments() {


        final ProgressDialog loading = new ProgressDialog(mContext);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelPostComment> call1 = apiService.api_post_comment(strPostId);
        call1.enqueue(new Callback<ModelPostComment>() {
            @Override
            public void onResponse(Call<ModelPostComment> call, retrofit2.Response<ModelPostComment> response) {
                loading.dismiss();
//                {"success":true,"data":[],"message":"You have commented successfully, It will show after approved by admin!","status":200}
//                try {
//                    Log.e("ResponseBody",response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                if(response.body().getSuccess()== true){
                    if(response.body().getData() != null){
                        commentList = new ArrayList<>();
                        commentList = response.body().getData().getData();
                        if(commentList.size() > 0){
                            linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            rcComment.setLayoutManager(linearLayoutManager);
                            commentAdapter = new PostCommentAdapter(mContext, commentList);
                            rcComment.setAdapter(commentAdapter);
                        }else {
                            Toast.makeText(mContext,"No Data Available",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(mContext,"No Data Available",Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(mContext,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();


            }


            @Override
            public void onFailure(Call<ModelPostComment> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }
}
