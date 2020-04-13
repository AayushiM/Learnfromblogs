package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.BottomActivity;
import app.com.learnfromblogs.Activity.CategoryActivity;
import app.com.learnfromblogs.Adapter.HomeCatAdapter;
import app.com.learnfromblogs.Model.TopicCategory.Datum;
import app.com.learnfromblogs.Model.TopicCategory.ModelTopicCategory;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class CategoryFragment extends Fragment {
    RecyclerView rcCategory;
    EditText editTextSearch;
    String strUid,strToken;
    HomeCatAdapter adapter;
    public static List<Datum> catList;
    List<Datum> filterList;
    String strCat="0";
    Button btnCat;
    String flag;
    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        strToken = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");
//        Log.e("TOKEN","as"+strToken+" "+strUid);
//        Toast.makeText(getApplicationContext(),""+strToken+" "+strUid,Toast.LENGTH_SHORT).show();


        rcCategory = view.findViewById(R.id.category_recyclerview);
        btnCat = view.findViewById(R.id.category_btn_next);
        editTextSearch = view.findViewById(R.id.category_edittext_search);

        flag = "0";
        if(flag.equals("0")){
            btnCat.setVisibility(View.GONE);
        }


        getData();


        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0 ; i<catList.size(); i++){
                    if(catList.get(i).getIsSelected() == 1){
                        strCat = "1";
                        break;
                    }
                }
                if(strCat.equals("1")){
                    Intent i = new Intent(getActivity(), BottomActivity.class);
                    i.putExtra("flag","login");
                    startActivity(i);
                    getActivity().finishAffinity();
                }else {
                    Toast.makeText(getActivity(),"Please, Select the category",Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;

    }

    private void getData() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelTopicCategory> call1 = apiService.api_category(strToken,strUid);
        call1.enqueue(new Callback<ModelTopicCategory>() {
            @Override
            public void onResponse(Call<ModelTopicCategory> call, retrofit2.Response<ModelTopicCategory> response) {
                loading.dismiss();

                if(response.body().getSuccess()== true) {
                    catList = new ArrayList();
                    catList = response.body().getData();
                    rcCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    adapter = new HomeCatAdapter(getActivity(), catList);
                    rcCategory.setAdapter(adapter);
                    rcCategory.setNestedScrollingEnabled(false);
                    setupListener();
                }


            }


            @Override
            public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void setupListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (catList.size() > 0){
                    filterList = new ArrayList<>();
                    for (Datum datum: catList){
                        if (datum.getName().toLowerCase().contains(s.toString().toLowerCase().trim())){
                            filterList.add(datum);
                        }
                    }
                    adapter.updateList(filterList);
                }
            }
        });
    }
}
