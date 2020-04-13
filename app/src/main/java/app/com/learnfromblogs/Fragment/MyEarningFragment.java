package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
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
import app.com.learnfromblogs.Adapter.MyPointAdapter;
import app.com.learnfromblogs.Model.Point.Datum;
import app.com.learnfromblogs.Model.Point.ModelPoints;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class MyEarningFragment extends Fragment {

    private RecyclerView rcEarning;
    String strUid;
    private boolean isScrolling=false;

    private int currentItems,totalItems,scrollOutItems;
    private  List<Datum> pointList = new ArrayList<>();
    private  List<Datum> pointList1 = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private MyPointAdapter adapter;
    private int page=1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_my_earning_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        strUid = getArguments().getString("userid");
        Log.e("strUid",""+strUid);
        apiPointDetails();

        findId(root);







        return root;
    }

    private void findId(View root) {


        rcEarning = root.findViewById(R.id.my_earning_recyclerview);
        progressBar = root.findViewById(R.id.my_earning_progressbar);
    }


    private void apiPointDetails() {


        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelPoints> call1 = apiService.api_point_list(strUid,"2");
        call1.enqueue(new Callback<ModelPoints>() {
            @Override
            public void onResponse(Call<ModelPoints> call, retrofit2.Response<ModelPoints> response) {
                loading.dismiss();
                if(response.body().getSuccess()== true) {
                    pointList = new ArrayList<>();
                    pointList = response.body().getData().getData();
                    pointList1.addAll(pointList);
                    Toast.makeText(getActivity(),""+pointList1.size(),Toast.LENGTH_SHORT).show();
                    if(pointList1.size()>0) {
                         linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcEarning.setLayoutManager(linearLayoutManager);
                        adapter = new MyPointAdapter(getActivity(), pointList1);
                        rcEarning.setAdapter(adapter);
                    }

                    rcEarning.addOnScrollListener(new RecyclerView.OnScrollListener() {
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



            }


            @Override
            public void onFailure(Call<ModelPoints> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }


    private void getData1() {



        API apiService = APIClient.getClient().create(API.class);
        Call<ModelPoints> call1 = apiService.api_point_list(strUid, String.valueOf(page));
        call1.enqueue(new Callback<ModelPoints>() {
            @Override
            public void onResponse(Call<ModelPoints> call, retrofit2.Response<ModelPoints> response) {
                progressBar.setVisibility(View.GONE);
                Log.e("page", String.valueOf(page));

//                Log.e("Response","A"+response.body().getData().get().size());
//                Toast.makeText(getActivity(),"Toast", Toast.LENGTH_SHORT).show();
                Log.e("Success",""+response.body().getSuccess());
                if(response.body().getSuccess()== true) {
                    if(response.body().getData().getData() != null) {
                        pointList = response.body().getData().getData();
                        pointList1.addAll(pointList);
                        adapter.notifyDataSetChanged();
                    }
                }
//                Log.e("feedList1", String.valueOf(feedList1.size()));




            }


            @Override
            public void onFailure(Call<ModelPoints> call, Throwable t) {



            }
        });
    }

}
