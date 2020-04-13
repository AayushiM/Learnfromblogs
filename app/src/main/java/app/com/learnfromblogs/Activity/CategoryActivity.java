package app.com.learnfromblogs.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Adapter.HomeCatAdapter;
import app.com.learnfromblogs.Model.TopicCategory.Datum;
import app.com.learnfromblogs.Model.TopicCategory.ModelTopicCategory;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView rcCategory;
    String strUid,strToken;
    HomeCatAdapter adapter;
    List<Datum>catList;
    String strCat="0";
    Button btnCat;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        strToken = Constants.loginSharedPreferences.getString(Constants.logintoken,"");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");
//        Log.e("TOKEN","as"+strToken+" "+strUid);
//        Toast.makeText(getApplicationContext(),""+strToken+" "+strUid,Toast.LENGTH_SHORT).show();


        rcCategory = findViewById(R.id.category_recyclerview);
        btnCat = findViewById(R.id.category_btn_next);

        flag =getIntent().getStringExtra("cat");
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
                    Intent i = new Intent(CategoryActivity.this,BottomActivity.class);
                    i.putExtra("flag","login");
                    startActivity(i);
                    finishAffinity();
                }else {
                    Toast.makeText(getApplicationContext(),"Please, Select the category",Toast.LENGTH_LONG).show();
                }
            }
        });
        
        
    }

    private void getData() {

        final ProgressDialog loading = new ProgressDialog(CategoryActivity.this);
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
                    rcCategory.setLayoutManager(new GridLayoutManager(CategoryActivity.this, 2));
                    adapter = new HomeCatAdapter(CategoryActivity.this, catList);
                    rcCategory.setAdapter(adapter);
                    rcCategory.setNestedScrollingEnabled(false);
                }


            }


            @Override
            public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }
}
