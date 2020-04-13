package app.com.learnfromblogs.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {

    EditText edName, edEmail, edUrl, edPassword;
    Button btnRegister;
    CheckBox ch1, ch2;
    String strandroidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        strandroidId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Constants.hideKeyboard(RegisterActivity.this);
        findId();
        btnClick();

    }


    private void findId() {

        edName = findViewById(R.id.register_ed_name);
        edEmail = findViewById(R.id.register_ed_email);
        edUrl = findViewById(R.id.register_ed_website);
        edPassword = findViewById(R.id.register_ed_password);
        btnRegister = findViewById(R.id.register_btn_register);
        ch1 = findViewById(R.id.register_ch_1);
        ch2 = findViewById(R.id.register_ch_2);
    }

    private void btnClick() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) {
                    apiRegister();
                }
            }
        });

    }

    private void apiRegister() {

        final ProgressDialog loading = new ProgressDialog(RegisterActivity.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ResponseBody> call1 = apiService.api_signup(edName.getText().toString().trim(),
                edEmail.getText().toString().trim(),
                edUrl.getText().toString().trim(),
                edPassword.getText().toString().trim());
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                loading.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Boolean success = jsonObject.getBoolean("success");
                    if (success == true) {
//                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
//                        startActivity(i);
//                        finish();



                        verifyRegisterDailog();

//
                    } else {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.has("email")) {
                            JSONArray email = jsonObject1.getJSONArray("email");
                            String strEmail = email.getString(0);
                            Toast.makeText(RegisterActivity.this, "" + strEmail, Toast.LENGTH_SHORT).show();
                        }
                        if (jsonObject1.has("website_link")) {
                            JSONArray website_link = jsonObject1.getJSONArray("website_link");
                            String strWeb = website_link.getString(0);
                            Toast.makeText(RegisterActivity.this, "" + strWeb, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                if(response.body().getSuccess()== true){
////
//
//
//
//
//                    Toast.makeText(getApplicationContext(), "" +response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getApplicationContext(), "" +response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().getData());
////                        JSONObject  jsonObject1  = jsonObject.getJSONObject("data");
//                        if(jsonObject1.has("email")){
//                            Toast.makeText(RegisterActivity.this,"yes",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(RegisterActivity.this,"No",Toast.LENGTH_SHORT).show();
//                        }
//
//                    } catch (JSONException e) {
//                        Log.e("Message",e.getMessage());
//                        e.printStackTrace();
//                    }
//                }

            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void verifyRegisterDailog() {


        final Dialog alertDialog = new Dialog(RegisterActivity.this);
//        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().getDecorView().setBackground(ContextCompat.getDrawable(RegisterActivity.this, R.drawable.drawable_back_dialog));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        alertDialog.getWindow().setAttributes(lp);


        View rootView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.dialog_verify_email, null);

        Button btnSubmit = rootView.findViewById(R.id.verify_email_btn_submit);
        Button btnCancel = rootView.findViewById(R.id.verify_email_btn_cancel);
        final EditText edOtp = rootView.findViewById(R.id.verify_email_ed_otp);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edOtp.getText().toString().isEmpty()) {
                    verifyRegister(edOtp.getText().toString(), alertDialog);
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        alertDialog.setContentView(rootView);
        alertDialog.show();
    }

    private void verifyRegister(String strOtp, final Dialog alertDialog) {


        final ProgressDialog loading = new ProgressDialog(RegisterActivity.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ResponseBody> call1 = apiService.api_verify_otp(edEmail.getText().toString(), strOtp, strandroidId);
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                loading.dismiss();

//                if(response.body().getSuccess()== true){
//                        Intent i = new Intent(RegisterActivity.this, CategoryActivity.class);
//                        startActivity(i);
//                        finish();
//                        alertDialog.dismiss();
//                }
//                    Toast.makeText(getApplicationContext(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();


                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Boolean success = jsonObject.getBoolean("success");
                    if (success == true) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String strToken = jsonObject1.getString("token");
                        String strUid = jsonObject1.getString("user_id");
                        String strName = jsonObject1.getString("name");
                        String strFirstName = jsonObject1.getString("first_name");
                        String strLastName = jsonObject1.getString("last_name");
                        String strEmail = jsonObject1.getString("email");
                        String strdevice_id = jsonObject1.getString("device_id");
                        String strUrl = jsonObject1.getString("profile_url");

                        SharedPreferences.Editor editor = Constants.loginSharedPreferences.edit();
                        editor.putString(Constants.logintoken, strToken);
                        editor.putString(Constants.uid, "" + strUid);
                        editor.putString(Constants.firstname, strFirstName);
                        editor.putString(Constants.lastname, strLastName);
                        editor.putString(Constants.fullname, strName);
                        editor.putString(Constants.email, strEmail);
                        editor.putString(Constants.deviceid, strdevice_id);
                        editor.putString(Constants.profileurl, strUrl);
                        editor.putBoolean(Constants.LoginStatus, true);
                        editor.commit();


                        Intent i = new Intent(RegisterActivity.this, CategoryActivity.class);
                        i.putExtra("cat","1");
                        startActivity(i);
                        finish();
                        alertDialog.dismiss();


//
                    }
                    String strMessage = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), "" + strMessage, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("loginData1", e.getMessage() + "");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("loginData2", e.getMessage() + "");
                }


            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });


    }


    private boolean isEmpty() {


        if (edName.getText().toString().trim().equals("")) {
            Toast.makeText(RegisterActivity.this, "Please,Enter Email", Toast.LENGTH_LONG).show();
            return false;
        } else if (edEmail.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please,Enter Email", Toast.LENGTH_LONG).show();
            return false;
        } else if (edUrl.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please,Enter Website", Toast.LENGTH_LONG).show();
            return false;
        } else if (edPassword.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please,Enter Password", Toast.LENGTH_LONG).show();
            return false;
        } else if (!ch1.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please,Agree with condition", Toast.LENGTH_LONG).show();
            return false;
        } else if (!ch2.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please, Agree to this online", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
