package app.com.learnfromblogs.Activity.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.chinalwb.are.render.AreTextView;

import org.json.JSONObject;

import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Model.AllFeed.ModelAllFeed;
import app.com.learnfromblogs.Model.aboutus.AboutUs;
import app.com.learnfromblogs.Model.declaimerpolicy.DeclaimerPolicy;
import app.com.learnfromblogs.Model.privacypolicy.PrivacyPolicy;
import app.com.learnfromblogs.Model.termofuse.TermOfUse;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HTMLDataActivity extends AppCompatActivity {
    private Context mContext = this;
    private int type;
    private AreTextView areTextView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_t_m_l_data);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        type = getIntent().getIntExtra("Type", 0);
        areTextView = findViewById(R.id.areTextView);
        areTextView.invalidate();
        API apiService = APIClient.getClient().create(API.class);
        switch (type){
            case 0:
                Call<AboutUs> callAboutUs = apiService.api_aboutUs();
                callAboutUs.enqueue(new Callback<AboutUs>() {
                    @Override
                    public void onResponse(Call<AboutUs> call, Response<AboutUs> response) {
                        if (response.isSuccessful()){
                            getSupportActionBar().setTitle( response.body().getData().getTitle());
                            areTextView.fromHtml(response.body().getData().getContent());
                        }
                    }

                    @Override
                    public void onFailure(Call<AboutUs> call, Throwable t) {

                    }
                });
                break;
            case 1:
                break;
            case 2:
                Call<TermOfUse> callTermOfUse = apiService.api_termOfUse();
                callTermOfUse.enqueue(new Callback<TermOfUse>() {
                    @Override
                    public void onResponse(Call<TermOfUse> call, Response<TermOfUse> response) {
                        if (response.isSuccessful()){
                            getSupportActionBar().setTitle( response.body().getData().getTitle());
                            areTextView.fromHtml(response.body().getData().getContent());
                        }
                    }

                    @Override
                    public void onFailure(Call<TermOfUse> call, Throwable t) {

                    }
                });
                break;
            case 3:
                Call<PrivacyPolicy> callPrivacyPolicy = apiService.api_PrivacyPolicy();
                callPrivacyPolicy.enqueue(new Callback<PrivacyPolicy>() {
                    @Override
                    public void onResponse(Call<PrivacyPolicy> call, Response<PrivacyPolicy> response) {
                        if (response.isSuccessful()){
                            getSupportActionBar().setTitle( response.body().getData().getTitle());
                            areTextView.fromHtml(response.body().getData().getContent());
                        }
                    }

                    @Override
                    public void onFailure(Call<PrivacyPolicy> call, Throwable t) {

                    }
                });
                break;
            case 4:
                Call<DeclaimerPolicy> callDeclaimer = apiService.api_declaimerPolicy();
                callDeclaimer.enqueue(new Callback<DeclaimerPolicy>() {
                    @Override
                    public void onResponse(Call<DeclaimerPolicy> call, Response<DeclaimerPolicy> response) {
                        if (response.isSuccessful()){
                            getSupportActionBar().setTitle( response.body().getData().getTitle());
                            areTextView.fromHtml(response.body().getData().getContent());
                        }
                    }

                    @Override
                    public void onFailure(Call<DeclaimerPolicy> call, Throwable t) {

                    }
                });
                break;
            case 5:
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
