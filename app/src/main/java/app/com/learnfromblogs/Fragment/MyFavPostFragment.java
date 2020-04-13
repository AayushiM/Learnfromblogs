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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Adapter.UserFeedFavouriteAdapter;
import app.com.learnfromblogs.Model.AllFeed.Datum;
import app.com.learnfromblogs.Model.AllFeed.ModelAllFeed;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class MyFavPostFragment extends Fragment {

    String strTokenId,strUid;
    RecyclerView rcFav;
    List<Datum> feedList = new ArrayList<>();
    List<Datum> feedList1 = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    int currentItems,totalItems,scrollOutItems;
    UserFeedFavouriteAdapter adapter;
    boolean isScrolling=false;
    private ProgressBar progressBar;
    String strType="",strSubId="", searchKey="",type="", slug="";
    int page=1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_my_fav_post_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");


        findId(root);
        apifav();



        return root;
    }

    private void apifav() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelAllFeed> call1 = apiService.api_get_fav_post(strTokenId,strUid,""+page);
        call1.enqueue(new Callback<ModelAllFeed>() {
            @Override
            public void onResponse(Call<ModelAllFeed> call, retrofit2.Response<ModelAllFeed> response) {
                loading.dismiss();


                if(response.body().getSuccess()== true) {

                    feedList = response.body().getData().getData();

                    feedList1.addAll(feedList);
                    if(feedList1.size()>0) {
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcFav.setLayoutManager(linearLayoutManager);
                        adapter = new UserFeedFavouriteAdapter(getActivity(), feedList1);
                        rcFav.setAdapter(adapter);
                        adapter.setListner(pos -> {
                            Toast.makeText(getContext(), pos, Toast.LENGTH_SHORT).show();
                            type = "tagged";
                            slug = pos;
//                            Toast.makeText(getContext(), slug, Toast.LENGTH_SHORT).show();
//                            getData();
                        });
                        type="";
                        slug="";
                        Log.e("feedList1", String.valueOf(feedList1.size()));


                        rcFav.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                }else {

                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finishAffinity();

                }
            }




            @Override
            public void onFailure(Call<ModelAllFeed> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void getData1() {


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelAllFeed> call1 = apiService.api_get_fav_post(strTokenId,strUid,""+page);
        call1.enqueue(new Callback<ModelAllFeed>() {
            @Override
            public void onResponse(Call<ModelAllFeed> call, retrofit2.Response<ModelAllFeed> response) {
                progressBar.setVisibility(View.GONE);
                feedList = response.body().getData().getData();
                feedList1.addAll(feedList);
                adapter.notifyDataSetChanged();



            }




            @Override
            public void onFailure(Call<ModelAllFeed> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void findId(View root) {

        rcFav = root.findViewById(R.id.my_fav_post_recyclerview);
        progressBar = root.findViewById(R.id.my_fav_post_progressbar);
    }
}
