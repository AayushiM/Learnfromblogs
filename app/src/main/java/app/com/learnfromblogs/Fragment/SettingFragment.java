package app.com.learnfromblogs.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Model.Profile.ModelProfile;
import app.com.learnfromblogs.Model.TopicCategory.ModelTopicCategory;
import app.com.learnfromblogs.Model.repoer.ReportModel;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private CheckBox mCheckBoxFavoriteEmail, mCheckBoxFavoriteNotify, mCheckBoxCommentEmail, mCheckBoxCommentNotify, mCheckBoxPostEmail, mCheckBoxPostNotify;
    private EditText mEditTextFB, mEditTextInsta, mEditTextTwitter, mEditTextPinterest, mEditTextLinkdIn, mEditTextGroup, mEditTextOldPass, mEditTextNewPass, mEditTextConfirmPass, mEditTextOther;
    private Button mButtonUpdateNotification, mButtonUpdateLinks, mButtonUpdatePassword, mButtonDeactive;
    private RadioGroup mRadioGrp;
    private RadioButton mRadioBtnOther;
    private View view;
    private String strTokenId, strUid;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");
        mCheckBoxFavoriteEmail = view.findViewById(R.id.setting_favotite_email);
        mCheckBoxFavoriteNotify = view.findViewById(R.id.setting_favotite_notify);
        mCheckBoxCommentEmail = view.findViewById(R.id.setting_comment_email);
        mCheckBoxCommentNotify = view.findViewById(R.id.setting_comment_notify);
        mCheckBoxPostEmail = view.findViewById(R.id.setting_post_email);
        mCheckBoxPostNotify = view.findViewById(R.id.setting_post_notify);

        mEditTextFB = view.findViewById(R.id.setting_social_fb);
        mEditTextInsta = view.findViewById(R.id.setting_social_insta);
        mEditTextTwitter = view.findViewById(R.id.setting_social_twitter);
        mEditTextPinterest = view.findViewById(R.id.setting_social_pinterest);
        mEditTextLinkdIn = view.findViewById(R.id.setting_social_linkdin);
        mEditTextGroup = view.findViewById(R.id.setting_social_grp);
        mEditTextOldPass = view.findViewById(R.id.setting_password_old);
        mEditTextNewPass = view.findViewById(R.id.setting_password_new_password);
        mEditTextConfirmPass = view.findViewById(R.id.setting_password_new_confirm_password);
        mEditTextOther = view.findViewById(R.id.deactive_edittext_message);

        mButtonUpdateNotification = view.findViewById(R.id.update_notification_setting);
        mButtonUpdateLinks = view.findViewById(R.id.update_social_link);
        mButtonUpdatePassword = view.findViewById(R.id.update_Password);
        mButtonDeactive = view.findViewById(R.id.update_deactivate);

        mRadioGrp = view.findViewById(R.id.deactive_radio_grp);
        mRadioBtnOther = view.findViewById(R.id.deactive_radio_other);

        mButtonUpdateNotification.setOnClickListener(this);
        mButtonUpdateLinks.setOnClickListener(this);
        mButtonUpdatePassword.setOnClickListener(this);
        mButtonDeactive.setOnClickListener(this);
        getProfileData();
        mRadioBtnOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditTextOther.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                mEditTextOther.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_notification_setting:
                onNotificationUpdateClick();
                break;
            case R.id.update_social_link:
                onSocialUpdateClick();
                break;
            case R.id.update_Password:
                onChangePasswordClick();
                break;
            case R.id.update_deactivate:
                onDeactiveClick();
                break;
        }
    }


    private void getProfileData() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelProfile> call1 = apiService.api_profile(strTokenId,strUid);
        call1.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, retrofit2.Response<ModelProfile> response) {
                loading.dismiss();

                if(response.body().getSuccess()== true) {
                    if(response.body().getData().getYouHaveFavouriteEmail().equals("1")){
                        mCheckBoxFavoriteEmail.setChecked(true);
                    }
                    else mCheckBoxFavoriteEmail.setChecked(false);
                    if(response.body().getData().getYouHaveFavouriteNotify().equals("1")){
                        mCheckBoxFavoriteNotify.setChecked(true);
                    }
                    else mCheckBoxFavoriteNotify.setChecked(false);

                    if(response.body().getData().getYouHaveCommentEmail().equals("1")){
                        mCheckBoxCommentEmail.setChecked(true);
                    }
                    else mCheckBoxCommentEmail.setChecked(false);
                    if(response.body().getData().getYouHaveCommentNotify().equals("1")){
                        mCheckBoxCommentNotify.setChecked(true);
                    }
                    else mCheckBoxCommentNotify.setChecked(false);

                    if(response.body().getData().getYouHaveSelectedCategoriesEmail().equals("1")){
                        mCheckBoxPostEmail.setChecked(true);
                    }
                    else mCheckBoxPostEmail.setChecked(false);
                    if(response.body().getData().getYouHaveSelectedCategoriesNotify().equals("1")){
                        mCheckBoxPostNotify.setChecked(true);
                    }
                    else mCheckBoxPostNotify.setChecked(false);

                    if(response.body().getData().getFacebook()==null){
                        mEditTextFB.setText("");
                    }
                   else mEditTextFB.setText(response.body().getData().getFacebook());

                    if(response.body().getData().getLinkedin()==null){
                        mEditTextLinkdIn.setText("");
                    }
                    else mEditTextLinkdIn.setText(response.body().getData().getLinkedin());

                    if(response.body().getData().getInstagram()==null){
                        mEditTextInsta.setText("");
                    }
                    else mEditTextInsta.setText(response.body().getData().getInstagram());

                    if(response.body().getData().getTwitter()==null){
                        mEditTextTwitter.setText("");
                    }
                    else mEditTextTwitter.setText(response.body().getData().getTwitter());

                    if(response.body().getData().getPinterest()==null){
                        mEditTextPinterest.setText("");
                    }
                    else mEditTextPinterest.setText(response.body().getData().getPinterest());

                    if(response.body().getData().getMygroup()==null){
                        mEditTextGroup.setText("");
                    }
                    else mEditTextGroup.setText(response.body().getData().getMygroup());


                }else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finishAffinity();
                }
            }


            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }


    private void onDeactiveClick() {
        RadioButton radioButton = view.findViewById(mRadioGrp.getCheckedRadioButtonId());
        String reason = radioButton.getText().toString().trim();
        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        API apiService = APIClient.getClient().create(API.class);
        Call<ModelTopicCategory> call1 = apiService.DeactiveUser(strTokenId, strUid, reason, mEditTextOther.getText().toString().trim());
        call1.enqueue(new Callback<ModelTopicCategory>() {
            @Override
            public void onResponse(Call<ModelTopicCategory> call, retrofit2.Response<ModelTopicCategory> response) {
                loading.dismiss();
                if (response.body().getSuccess() == true) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else  Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

    private void onChangePasswordClick() {
        if (mEditTextOldPass.getText().toString().trim().length() > 0) {
            if (mEditTextNewPass.getText().toString().trim().length() > 0) {
                if (mEditTextConfirmPass.getText().toString().trim().length() > 0) {
                    final ProgressDialog loading = new ProgressDialog(getActivity());
                    loading.setMessage("Please Wait..");
                    loading.setCancelable(false);
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                    API apiService = APIClient.getClient().create(API.class);
                    Call<ModelTopicCategory> call1 = apiService.UpdatePassword(strTokenId, strUid, mEditTextOldPass.getText().toString().trim(), mEditTextNewPass.getText().toString().trim(), mEditTextConfirmPass.getText().toString().trim());
                    call1.enqueue(new Callback<ModelTopicCategory>() {
                        @Override
                        public void onResponse(Call<ModelTopicCategory> call, retrofit2.Response<ModelTopicCategory> response) {
                            loading.dismiss();
                            if (response.body().getSuccess() == true) {
                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else  Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                            loading.dismiss();
                            Toast.makeText(getActivity(), "Password and confirm password are different.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(getActivity(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getActivity(), "Please enter new password", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getActivity(), "Please enter old password", Toast.LENGTH_SHORT).show();
    }

    private void onSocialUpdateClick() {
        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        API apiService = APIClient.getClient().create(API.class);
        Call<ModelTopicCategory> call1 = apiService.UpdateSocial(strTokenId, strUid, mEditTextFB.getText().toString().trim(), mEditTextInsta.getText().toString().trim(), mEditTextTwitter.getText().toString().trim(), mEditTextPinterest.getText().toString().trim(), mEditTextLinkdIn.getText().toString().trim(), mEditTextGroup.getText().toString().trim());
        call1.enqueue(new Callback<ModelTopicCategory>() {
            @Override
            public void onResponse(Call<ModelTopicCategory> call, retrofit2.Response<ModelTopicCategory> response) {
                loading.dismiss();
                if (response.body().getSuccess() == true) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getActivity(), "url format is invalid.", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "Response", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(getActivity(), "url format is invalid.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onNotificationUpdateClick() {
        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        API apiService = APIClient.getClient().create(API.class);
        Call<ModelTopicCategory> call1 = apiService.UpdateNotification(strTokenId, strUid, mCheckBoxFavoriteEmail.isChecked() ? 1 : 0, mCheckBoxFavoriteNotify.isChecked() ? 1 : 0, mCheckBoxCommentEmail.isChecked() ? 1 : 0,
                mCheckBoxCommentNotify.isChecked() ? 1 : 0, mCheckBoxPostEmail.isChecked() ? 1 : 0, mCheckBoxPostNotify.isChecked() ? 1 : 0);
        call1.enqueue(new Callback<ModelTopicCategory>() {
            @Override
            public void onResponse(Call<ModelTopicCategory> call, retrofit2.Response<ModelTopicCategory> response) {
                loading.dismiss();
                if (response.body().getSuccess() == true) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelTopicCategory> call, Throwable t) {
                loading.dismiss();
            }
        });
    }
}
