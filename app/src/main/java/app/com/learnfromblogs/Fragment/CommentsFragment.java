package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Adapter.MessageAdapter;
import app.com.learnfromblogs.Adapter.PostCommentAdapter;
import app.com.learnfromblogs.Model.DeleteMyCommnent.ModelDeleteComment;
import app.com.learnfromblogs.Model.PostCommentList.Datum;
import app.com.learnfromblogs.Model.PostCommentList.ModelPostComment;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class CommentsFragment extends Fragment {


    Button btnPost;
    EditText edMessage;
    String strTokenId,strUid,strPostId;
    List<Datum> commentList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rcComment;
    PostCommentAdapter commentAdapter;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_comments_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");
        rcComment = root.findViewById(R.id.comments_recyclerview);

        strPostId = getArguments().getString("id");
        Log.e("strPostId",strPostId);

        findId(root);
        
        
        getComments();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edMessage.getText().toString().equals("")){
                    apiAddComment();
                }
            }
        });


        return root;
    }

    private void getComments() {


        final ProgressDialog loading = new ProgressDialog(getActivity());
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
                            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            rcComment.setLayoutManager(linearLayoutManager);
                            commentAdapter = new PostCommentAdapter(getActivity(), commentList);
                            rcComment.setAdapter(commentAdapter);
                        }else {
                            Toast.makeText(getActivity(),"No Data Available",Toast.LENGTH_LONG).show();
                        }

                    }else {
                        Toast.makeText(getActivity(),"No Data Available",Toast.LENGTH_LONG).show();
                    }
                }
                Toast.makeText(getActivity(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();


            }


            @Override
            public void onFailure(Call<ModelPostComment> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void apiAddComment() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
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
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    fm.popBackStack ("Result", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    fragment = new FeedFragment();
//                    fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.content_frame, fragment);
//                    fragmentTransaction.commit();
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

    private void findId(View root) {

        btnPost = root.findViewById(R.id.comments_btn_post);
        edMessage = root.findViewById(R.id.comments_ed_add_comment);
    }


}
