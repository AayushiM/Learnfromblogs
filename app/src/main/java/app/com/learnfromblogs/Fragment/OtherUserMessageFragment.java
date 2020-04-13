package app.com.learnfromblogs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.Adapter.MessageAdapter;
import app.com.learnfromblogs.Model.MyComment.Datum;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.Constants;

public class OtherUserMessageFragment extends Fragment {

    private RecyclerView rcProfileMessage;
    private MessageAdapter adapter;

    private List<Datum> commentList = new ArrayList<>();
    private List<Datum> commentList1 = new ArrayList<>();
    private boolean isScrolling=false;
    private int currentItems,totalItems,scrollOutItems;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;
    private int page=1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_profile_tab_message_fragment, container, false);
        Constants.hideKeyboard(getActivity());


        commentList.clear();
        commentList1.clear();
        findId(root);
//        getCommentData();


        return root;
    }

//    private void getCommentData() {
//
//
//        final ProgressDialog loading = new ProgressDialog(getActivity());
//        loading.setMessage("Please Wait..");
//        loading.setCancelable(false);
//        loading.setCanceledOnTouchOutside(false);
//        loading.show();
//
//
//        API apiService = APIClient.getClient().create(API.class);
//        Call<ModelMyComment> call1 = apiService.api_my_comments(OtherUserProfile.strUid,""+page);
//        call1.enqueue(new Callback<ModelMyComment>() {
//            @Override
//            public void onResponse(Call<ModelMyComment> call, retrofit2.Response<ModelMyComment> response) {
//                loading.dismiss();
//
//                if(response.body().getSuccess()== true) {
//                    commentList = response.body().getData().getData();
//                    commentList1.addAll(commentList);
//                    if(commentList1.size() > 0) {
//                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//                        rcProfileMessage.setLayoutManager(linearLayoutManager);
//                        adapter = new MessageAdapter(getActivity(), commentList1);
//                        rcProfileMessage.setAdapter(adapter);
//
//                        rcProfileMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                            @Override
//                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                                super.onScrollStateChanged(recyclerView, newState);
//
//                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                                    isScrolling = true;
//
//                                }
//
//                            }
//
//                            @Override
//                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                                super.onScrolled(recyclerView, dx, dy);
//
//                                currentItems = linearLayoutManager.getChildCount();
//                                totalItems = linearLayoutManager.getItemCount();
//                                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
//
//                                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
//                                    isScrolling = false;
//                                    progressBar.setVisibility(View.VISIBLE);
//                                    page += 1;
//                                    getData1();
//                                }
//
//                            }
//                        });
//
//
//                    }
//                }else {
//                    Intent i = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(i);
//                    getActivity().finishAffinity();
//                }
//
//
//            }
//
//
//            @Override
//            public void onFailure(Call<ModelMyComment> call, Throwable t) {
//                loading.dismiss();
//
//                Log.e("loginData", t.getMessage() + "");
//            }
//        });
//    }

    private void findId(View root) {
        rcProfileMessage = (RecyclerView)root.findViewById(R.id.profile_tab_message_recyclerview);
        progressBar = (ProgressBar)root.findViewById(R.id.profile_tab_message_progressbar);

    }


//    private void getData1() {
//
//
//
//        API apiService = APIClient.getClient().create(API.class);
//        Call<ModelMyComment> call1 = apiService.api_my_comments(strTokenId,strUid,""+page);
//        call1.enqueue(new Callback<ModelMyComment>() {
//            @Override
//            public void onResponse(Call<ModelMyComment> call, retrofit2.Response<ModelMyComment> response) {
//                progressBar.setVisibility(View.GONE);
////                Log.e("page", String.valueOf(page));
//
////                Log.e("Response","A"+response.body().getData().get().size());
////                Toast.makeText(getActivity(),"Toast", Toast.LENGTH_SHORT).show();
//                commentList = response.body().getData().getData();
//                commentList1.addAll(commentList);
//                adapter.notifyDataSetChanged();
////                Log.e("feedList1", String.valueOf(feedList1.size()));
//
//
//
//
//            }
//
//
//            @Override
//            public void onFailure(Call<ModelMyComment> call, Throwable t) {
//
//
//
//            }
//        });
//    }

}
