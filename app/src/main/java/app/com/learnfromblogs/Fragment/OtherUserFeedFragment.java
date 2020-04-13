package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Adapter.MyFeedAdapter;
import app.com.learnfromblogs.Model.PersonalUserPost.Datum;
import app.com.learnfromblogs.Model.PersonalUserPost.ModelPersonalUserPost;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class OtherUserFeedFragment extends Fragment {

    RecyclerView rcFeed;
    MyFeedAdapter feedAdapter;
    String strUid,strTokenId;
    List<Datum> feedList = new ArrayList<>();
    List<Datum> feedList1 = new ArrayList<>();
    int page=1;
    LinearLayoutManager linearLayoutManager;
    int currentItems,totalItems,scrollOutItems;
    boolean isScrolling=false;
    ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_my_feed_fregment, container, false);
        Constants.hideKeyboard(getActivity());
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");

        findId(root);

        getUserPost();


        return root;
    }

    private void getUserPost() {


        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelPersonalUserPost> call1 = apiService.api_other_user_feed(OtherUserProfile.strUid,""+page);
        call1.enqueue(new Callback<ModelPersonalUserPost>() {
            @Override
            public void onResponse(Call<ModelPersonalUserPost> call, retrofit2.Response<ModelPersonalUserPost> response) {
                loading.dismiss();

                if(response.body().getSuccess()== true) {

                    feedList = response.body().getData().getData();
                    feedList1.addAll(feedList);
                    if(feedList1.size() > 0) {
                       linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcFeed.setLayoutManager(linearLayoutManager);
                        feedAdapter = new MyFeedAdapter(getActivity(), feedList1,"other");
                        rcFeed.setAdapter(feedAdapter);


                        rcFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);

                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                    isScrolling = true;

                                }

                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                currentItems = linearLayoutManager.getChildCount();
                                totalItems = linearLayoutManager.getItemCount();
                                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                                    isScrolling = false;
                                    progressBar.setVisibility(View.VISIBLE);
                                    page += 1;
                                    getData1();
                                }

                            }
                        });
                    }
//                    rcFeed.setNestedScrollingEnabled(false);
                }else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finishAffinity();
                }





            }


            @Override
            public void onFailure(Call<ModelPersonalUserPost> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void findId(View root) {

        rcFeed = root.findViewById(R.id.feed_recyclerview);
        progressBar = root.findViewById(R.id.feed_progressbar);

    }


    private void getData1() {



        API apiService = APIClient.getClient().create(API.class);
        Call<ModelPersonalUserPost> call1 = apiService.api_user_feed(strTokenId,strUid,""+page);
        call1.enqueue(new Callback<ModelPersonalUserPost>() {
            @Override
            public void onResponse(Call<ModelPersonalUserPost> call, retrofit2.Response<ModelPersonalUserPost> response) {
                progressBar.setVisibility(View.GONE);
//                Log.e("page", String.valueOf(page));

//                Log.e("Response","A"+response.body().getData().get().size());
//                Toast.makeText(getActivity(),"Toast", Toast.LENGTH_SHORT).show();
                feedList = response.body().getData().getData();
                feedList1.addAll(feedList);
                feedAdapter.notifyDataSetChanged();
                Log.e("feedList1", String.valueOf(feedList1.size()));




            }


            @Override
            public void onFailure(Call<ModelPersonalUserPost> call, Throwable t) {



            }
        });
    }
}
