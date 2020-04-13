package app.com.learnfromblogs.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Model.Login.ModelLogin;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    EditText edPassword,edEmail;
    Button btnSignIn;
    String android_id;
    Constants constants;
    TextView txtRegister;
    CheckBox chRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        constants = new Constants();
        Constants.loginSharedPreferences = getSharedPreferences(Constants.LoginPREFERENCES, MODE_PRIVATE);

        findId();
        btnClick();
        Constants.hideKeyboard(LoginActivity.this);
    }



    private void findId() {

        edPassword =  findViewById(R.id.login_ed_password);
        edEmail = findViewById(R.id.login_ed_email);
        btnSignIn =  findViewById(R.id.login_btn_login);
        txtRegister = findViewById(R.id.login_txt_register_here);
        chRemember = findViewById(R.id.login_ch_remember);
    }

    private void btnClick() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty()) {
                    apiLogin();
                }
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void apiLogin() {

        final ProgressDialog loading = new ProgressDialog(LoginActivity.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelLogin> call1 = apiService.api_signIn(edEmail.getText().toString().trim(),
                edPassword.getText().toString().trim(),android_id);
        call1.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, retrofit2.Response<ModelLogin> response) {
                loading.dismiss();


                if(response.body().getSuccess()== true){
                    Log.e("UserId",""+response.body().getData().getUserId());
                    Log.e("logintoken",""+response.body().getData().getToken());
                    SharedPreferences.Editor editor = Constants.loginSharedPreferences.edit();
                    editor.putString(Constants.logintoken, response.body().getData().getToken());
                    editor.putString(Constants.uid, ""+response.body().getData().getUserId());
                    editor.putString(Constants.firstname, response.body().getData().getFirstName());
                    editor.putString(Constants.lastname, response.body().getData().getLastName());
                    editor.putString(Constants.fullname, response.body().getData().getName());
                    editor.putString(Constants.email, response.body().getData().getEmail());
                    editor.putString(Constants.deviceid, response.body().getData().getDeviceId());
                    editor.putString(Constants.profileurl, response.body().getData().getProfileUrl());
                    if(chRemember.isChecked()) {
                        editor.putBoolean(Constants.LoginStatus, true);
                    }
                    editor.commit();

//                    if(Constants.loginSharedPreferences.getBoolean(Constants.cateactive, false)) {
                        Intent i = new Intent(LoginActivity.this, BottomActivity.class);
                         i.putExtra("flag","login");
                        startActivity(i);
                        finish();
//                    }else {
//                        Intent i = new Intent(LoginActivity.this, CategoryActivity.class);
//                        startActivity(i);
//                        finish();
//                    }

                    Toast.makeText(getApplicationContext(), "" +response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private boolean isEmpty() {



        if (edEmail.getText().toString().trim().equals("")) {
            Toast.makeText(LoginActivity.this, "Please,Enter Email", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (edPassword.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please,Enter Password", Toast.LENGTH_LONG).show();
            return false;
        }






        return true;
    }


}
