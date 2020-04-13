package app.com.learnfromblogs.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Adapter.FeedAdapter;
import app.com.learnfromblogs.Adapter.FeedCatAdapter;
import app.com.learnfromblogs.Adapter.FeedTypeAdapter;
import app.com.learnfromblogs.Model.AllFeed.Datum;
import app.com.learnfromblogs.Model.AllFeed.ModelAllFeed;
import app.com.learnfromblogs.Model.TopicCategory.ModelTopicCategory;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.AppUtil;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class FeedFragment extends Fragment {

    private RecyclerView rcFeedType, rcFeedData;
    EditText editTextSearch;
    List<String> feedType;
    FeedTypeAdapter feedTypeAdapter;
    FeedAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    String strTokenId="", strUid="";
    List<Datum> feedList = new ArrayList<>();
    List<Datum> feedList1 = new ArrayList<>();
    List<app.com.learnfromblogs.Model.TopicCategory.Datum> categoryList = new ArrayList<>();
    String strType="",strSubId="", searchKey="",type="", slug="";
    ProgressBar progressBar;
    public static int check = 0;
    int page=1;
    boolean isScrolling=false, searched;
    int currentItems,totalItems,scrollOutItems;
    RecyclerView rcDialogTag;
    FeedCatAdapter feedCatAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_feed_fragment, container, false);
        Constants.hideKeyboard(getActivity());

        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");
        editTextSearch = root.findViewById(R.id.feedfragment_edittext_search);
//        editTextSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                getData(editTextSearch.getText().toString().trim());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

//        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    searchKey = ;
//                    AppUtil.hideKeyboard(getActivity());
////                    Toast.makeText(getContext(), searchKey, Toast.LENGTH_SHORT).show();
//
//                    return true;
//                }
//                return false;
//            }
//        });
        editTextSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return false;
                if(keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH){
                    AppUtil.hideKeyboard(getActivity());
                    searchKey = editTextSearch.getText().toString().trim();
                    editTextSearch.setText("");
                    getData2();
                }
                return false;
            }
        });

        feedType = new ArrayList<>();
        feedType.add("All");
        feedType.add("Popular");
        feedType.add("Recent");
        feedType.add("Category");



        findId(root);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rcFeedType.setLayoutManager(linearLayoutManager);
        feedTypeAdapter = new FeedTypeAdapter(getActivity(), feedType);
        rcFeedType.setAdapter(feedTypeAdapter);
        rcFeedType.setNestedScrollingEnabled(false);
        feedTypeClick();



//         rcFeedData.setNestedScrollingEnabled(false);


        getData();


        return root;
    }

    private void getCategory(final Dialog alertDialog) {


        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelTopicCategory> call1 = apiService.api_category(strTokenId,strUid);
        call1.enqueue(new Callback<ModelTopicCategory>() {
            @Override
            public void onResponse(Call<ModelTopicCategory> call, retrofit2.Response<ModelTopicCategory> response) {
                loading.dismiss();

                if (response.body().getSuccess() == true) {
                    categoryList = new ArrayList();
                    categoryList = response.body().getData();
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rcDialogTag.setLayoutManager(linearLayoutManager);
                    feedCatAdapter = new FeedCatAdapter(getActivity(), categoryList);
                    rcDialogTag.setAdapter(feedCatAdapter);


                    feedCatAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                          alertDialog.dismiss();

                            strSubId = categoryList.get(i).getId();
//                            Toast.makeText(getContext(), strSubId, Toast.LENGTH_SHORT).show();
                            getData();
                        }
                    });

                }



            }


            @Override
            public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void feedTypeClick() {
        feedTypeAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check = position;
                page = 0;

                feedTypeAdapter.notifyDataSetChanged();
                feedList.clear();
                if (position==0){
                    strSubId="";
                    strType = "";

                    getData();
                }else if(position==1){
                    strSubId="";
                    strType = "1";
                    getData();
                }else if(position == 2){
                    strSubId="";
                    strType = "2";
                    getData();
                }
                else if(position == 3){
//                    strType = "2";
//                    getData();
                    strType = "";
                    categoryDialog();
                }
            }
        });

    }

    private void categoryDialog() {

        final Dialog alertDialog = new Dialog(getActivity());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        alertDialog.getWindow().setAttributes(lp);
        final View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialoge_tag, null);

        rcDialogTag = (RecyclerView)rootView.findViewById(R.id.dialog_tag_recycelrview);
        getCategory(alertDialog);


        alertDialog.setContentView(rootView);

        alertDialog.show();
    }

    private void getData() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelAllFeed> call1 = apiService.api_feed(type, slug,
                strUid,
                searchKey, strSubId,
                strType, ""+page);
        Log.e("data_post",String.valueOf(page));
        Log.e("searchKey",searchKey);
        Log.e("getData", String.valueOf(page)+type+ slug+
                strUid+
                searchKey+ strSubId+
                strType);
        call1.enqueue(new Callback<ModelAllFeed>() {
            @Override
            public void onResponse(Call<ModelAllFeed> call, retrofit2.Response<ModelAllFeed> response) {
                loading.dismiss();
//                searched = false;

                if(response.body().getSuccess()== true) {

                    feedList = response.body().getData().getData();
                    feedList1.addAll(feedList);
                    if(feedList1 != null) {
                        if (feedList1.size() == 0){
                            Toast.makeText(getActivity(), "No post found", Toast.LENGTH_SHORT).show();
                        }
                        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        rcFeedData.setLayoutManager(linearLayoutManager);
                        adapter = new FeedAdapter(getActivity(), feedList1);
                        rcFeedData.setAdapter(adapter);
                        adapter.setListner(pos -> {
                            Toast.makeText(getContext(), pos, Toast.LENGTH_SHORT).show();
                            type = "tagged";
                            slug = pos;
//                            Toast.makeText(getContext(), slug, Toast.LENGTH_SHORT).show();
                            getData2();
                        });
                        searchKey="";
                        type="";
                        slug="";
//                        Log.e("feedList1", String.valueOf(feedList1.size()));


                        rcFeedData.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                                    Toast.makeText(getContext(), page, Toast.LENGTH_SHORT).show();
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


    private void getData2() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelAllFeed> call1 = apiService.api_feed(type, slug,
                strUid,
                searchKey, strSubId,
                strType, ""+page);
        Log.e("data_post",String.valueOf(page));
        Log.e("searchKey",searchKey);
        Log.e("getData2", String.valueOf(page)+type+ slug+
                strUid+
                searchKey+ strSubId+
                strType);
        call1.enqueue(new Callback<ModelAllFeed>() {
            @Override
            public void onResponse(Call<ModelAllFeed> call, retrofit2.Response<ModelAllFeed> response) {
                loading.dismiss();
//                searched = false;

                if(response.body().getSuccess()== true) {

                    feedList = response.body().getData().getData();
//                    feedList1.addAll(feedList);
//                    if(feedList1 != null) {
                    if (feedList.size() == 0){
                        Toast.makeText(getActivity(), "No post found", Toast.LENGTH_SHORT).show();
                    }
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rcFeedData.setLayoutManager(linearLayoutManager);
                    adapter = new FeedAdapter(getActivity(), feedList);
                    rcFeedData.setAdapter(adapter);
                    adapter.setListner(pos -> {
                        Toast.makeText(getContext(), pos, Toast.LENGTH_SHORT).show();
                        type = "tagged";
                        slug = pos;
//                            Toast.makeText(getContext(), slug, Toast.LENGTH_SHORT).show();
                        getData2();
                    });
                    searchKey="";
                    type="";
                    slug="";
//                        Log.e("feedList1", String.valueOf(feedList1.size()));


                    rcFeedData.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                                    Toast.makeText(getContext(), page, Toast.LENGTH_SHORT).show();
                                getData1();
                            }

                        }
                    });
//                    }
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

    private void findId(View root) {
        rcFeedType = root.findViewById(R.id.feed_recyclerview_feed_type);
        rcFeedData = root.findViewById(R.id.feed_recyclerview_feed__data);
        progressBar = root.findViewById(R.id.feed_progressbar);
    }


    private void getData1() {



        API apiService = APIClient.getClient().create(API.class);
        Call<ModelAllFeed> call1 = apiService.api_feed(type, slug,
                strUid,
                searchKey, strSubId,
                strType, ""+page);
        call1.enqueue(new Callback<ModelAllFeed>() {
            @Override
            public void onResponse(Call<ModelAllFeed> call, retrofit2.Response<ModelAllFeed> response) {
                progressBar.setVisibility(View.GONE);
                Log.e("getData1", String.valueOf(page)+type+ slug+
                        strUid+
                        searchKey+ strSubId+
                        strType);

//                Log.e("Response","A"+response.body().getData().get().size());
//                Toast.makeText(getActivity(),"Toast", Toast.LENGTH_SHORT).show();
                feedList = response.body().getData().getData();
                feedList1.addAll(feedList);
                adapter.notifyDataSetChanged();




            }


            @Override
            public void onFailure(Call<ModelAllFeed> call, Throwable t) {



            }
        });
    }
}
