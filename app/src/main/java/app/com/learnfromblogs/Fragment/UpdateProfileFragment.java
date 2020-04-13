package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Model.Country.ModelCountry;
import app.com.learnfromblogs.Model.Profile.ModelProfile;
import app.com.learnfromblogs.Model.UpdateProfile.ModelUpdateProfile;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateProfileFragment extends Fragment {

    public String strTokenId, strUid,strCountry;
    private Button btnSubmit;
    private EditText edFirstName, edLastName, edEmail, edDescription, edWebsite;
    private TextView txtLimit;
    private Spinner spCountry;
    private List<String> countryList;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_update_profile_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");


        findId(root);
        getProfile();
        BtnClick();



        return root;
    }

    private void BtnClick() {
        
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty()) {
                    updateProfile();
                }
            }
        });
    }

    private void updateProfile() {


        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        API apiService = APIClient.getClient().create(API.class);
        Call<ModelUpdateProfile> call1 = apiService.api_update_profile(strTokenId,strUid,edFirstName.getText().toString(),
                                                                edLastName.getText().toString(),edWebsite.getText().toString(),
                                                                strCountry,edDescription.getText().toString());
        call1.enqueue(new Callback<ModelUpdateProfile>() {
            @Override
            public void onResponse(Call<ModelUpdateProfile> call, retrofit2.Response<ModelUpdateProfile> response) {
                loading.dismiss();
                if(response.body().getSuccess()== true){

                    getFragmentManager().popBackStack();

                }
                Toast.makeText(getActivity(),""+response.body().getMessage(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<ModelUpdateProfile> call, Throwable t) {
                loading.dismiss();
                Log.e("loginData", t.getMessage() + "");
            }
        });
    }


    private boolean isEmpty() {



        if (edFirstName.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please,Enter First Name", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (edLastName.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please,Enter Last Name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (edEmail.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please,Enter Email", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (edDescription.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please,Enter Description", Toast.LENGTH_LONG).show();
            return false;
        }






        return true;
    }


    private void getProfile() {


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



                    edFirstName.setText(response.body().getData().getFirstName());
                    edLastName.setText( response.body().getData().getLastName());
                    edEmail.setText( response.body().getData().getEmail());
                    edWebsite.setText( response.body().getData().getWebsiteLink());
                    edDescription.setText(""+response.body().getData().getAboutMe());
                    Toast.makeText(getContext(),response.body().getData().getAboutMe() , Toast.LENGTH_SHORT).show();


                    int wordsLength = countWords(response.body().getData().getAboutMe());// words.length;
                    // count == 0 means a new word is going to start
                    if (wordsLength >= 30) {
                        setCharLimit(edDescription, edDescription.getText().length());
                    } else {
                        removeFilter(edDescription);
                    }

                    txtLimit.setText("Maximum "+String.valueOf(wordsLength) + "/" + 30 +" words");
//                    txtLimit.setText("Maximum "+"20" + "/" + 30 +" words");


                    edDescription.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            int wordsLength = countWords(s.toString());// words.length;
                            // count == 0 means a new word is going to start
                            if (count == 0 && wordsLength >= 30) {
                                setCharLimit(edDescription, edDescription.getText().length());
                            } else {
                                removeFilter(edDescription);
                            }
                            txtLimit.setText("Maximum "+String.valueOf(wordsLength) + "/" + 30 +" words");
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });

                    getCountryData(response.body().getData().getCountry());

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

    private int countWords(String s) {
//        String words = s.trim();
//        if (words.isEmpty())
//            return 0;
//        return words.split("\\s+").length; // separate string around spaces
        if (s == null || s.isEmpty()) { return 0; }
        String[] words = s.split("\\s+"); return words.length;


    }

    private InputFilter filter;

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[] { filter });
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }


    private void getCountryData(final String country) {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCountry> call1 = apiService.api_country_list();
        call1.enqueue(new Callback<ModelCountry>() {
            @Override
            public void onResponse(Call<ModelCountry> call, final retrofit2.Response<ModelCountry> response) {
                loading.dismiss();


                if (response.body().getSuccess() == true) {

                    countryList = new ArrayList<>();



                    for (int i=0;i<response.body().getData().size();i++){
                        countryList.add(response.body().getData().get(i).getName());
                    }


                    ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, countryList);
                    aa.setDropDownViewResource(R.layout.simple_spinner_item);
                    spCountry.setAdapter(aa);

                    for (int i=0;i<response.body().getData().size();i++){
                        if(response.body().getData().get(i).getId().equals(country)){
                            spCountry.setSelection(i);
                        }
                    }




                    spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            strCountry = response.body().getData().get(i).getId();
//                            strCategory = catnameList1.get(i).getId();
//                            Log.e("strCategory", strCategory);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                    getActivity().finishAffinity();
                }

            }


            @Override
            public void onFailure(Call<ModelCountry> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void findId(View root) {

        edFirstName = root.findViewById(R.id.update_profile_ed_firstname);
        edLastName = root.findViewById(R.id.update_profile_ed_lastname);
        edEmail = root.findViewById(R.id.update_profile_ed_email);
        edWebsite = root.findViewById(R.id.update_profile_ed_website);
        edDescription = root.findViewById(R.id.update_profile_ed_description);
        spCountry = root.findViewById(R.id.update_profile_sp_country);
        txtLimit = root.findViewById(R.id.update_profile_txt_description_limit);
        btnSubmit = root.findViewById(R.id.update_profile_btn_submit);
    }
}
