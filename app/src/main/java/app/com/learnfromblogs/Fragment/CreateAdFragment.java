package app.com.learnfromblogs.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Model.Category.ModelCategory;
import app.com.learnfromblogs.Model.CreateAd.ModelCreateAd;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CreateAdFragment extends Fragment {

    private EditText edTitle, edWebsite;
    private ImageView imgAd;
    private Spinner spCategory, spNoOfDays, spShowType;
    private String strCategory, strNoOfDays, strShowType;
    private String strToken, strUid;
    private List<String> catnameList;
    private List<String> dayList;
    private List<String> showList;
    private static final int MY_CAPTURE_REQUEST_CODE = 4;
    private String strImagePath;

    Button btnSubmit;
    private List<app.com.learnfromblogs.Model.Category.Datum> catnameList1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_create_ad_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        strToken = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");

        findId(root);
        categoryData();
        dayData();
        showData();
        btnClick();


        return root;
    }

    private void btnClick() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) {
                    createAd();
                }
            }
        });

        imgAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    onSelectImageClick(v);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAPTURE_REQUEST_CODE);
                }
            }
        });
    }

    private void createAd() {


        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        File file = new File(strImagePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("img_large", file.getName(), fileReqBody);
        MultipartBody.Part part1 = MultipartBody.Part.createFormData("img_thumb", file.getName(), fileReqBody);
        RequestBody bodyUid = RequestBody.create(MediaType.parse("text/plain"), strUid);
        RequestBody bodyToken = RequestBody.create(MediaType.parse("text/plain"), strToken);
        RequestBody bodyDays = RequestBody.create(MediaType.parse("text/plain"), strNoOfDays);

        RequestBody bodyCategory = RequestBody.create(MediaType.parse("text/plain"), strCategory);
        RequestBody bodylink = RequestBody.create(MediaType.parse("text/plain"), edWebsite.getText().toString());
        RequestBody bodyShowType = RequestBody.create(MediaType.parse("text/plain"), strShowType);

        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCreateAd> call1 = apiService.api_create_ad(part, part1, bodyUid, bodyToken, bodyDays, bodyCategory, bodylink, bodyShowType);
        call1.enqueue(new Callback<ModelCreateAd>() {
            @Override
            public void onResponse(Call<ModelCreateAd> call, retrofit2.Response<ModelCreateAd> response) {
                loading.dismiss();

//                {"success":true,"data":[],"message":"Advertisement create successfully, It will show after approved by admin!","status":200}
                if (response.body().getSuccess() == true) {
                    getFragmentManager().popBackStack();
                }
                Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<ModelCreateAd> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });


    }


    private void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(getActivity());
    }


    public void onActivityResult(int i, int i2, Intent intent) {


        if (i == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && i2 == getActivity().RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), intent);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.

            startCropImageActivity(imageUri);

        }

        if (i == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(intent);
            if (i2 == getActivity().RESULT_OK) {
                //((ImageButton) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Log.e("Uri", "" + result.getUri().getPath());
                strImagePath = result.getUri().getPath();
                imgAd.setImageURI(result.getUri());

            } else if (i2 == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(2, 2).start(getActivity());

    }


    private boolean isEmpty() {
        if (edTitle.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please,Enter Title", Toast.LENGTH_LONG).show();
            return false;
        } else if (edWebsite.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please,Enter Website", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void showData() {
        showList = new ArrayList<>();
        showList.add("Sign Up");
        showList.add("other");


        ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, showList);
        aa.setDropDownViewResource(R.layout.simple_spinner_item);
        spShowType.setAdapter(aa);

        spShowType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (showList.get(i).equals("Sign Up")) {
                    strShowType = "1";
                } else {
                    strShowType = "2";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void dayData() {

        dayList = new ArrayList<>();
        dayList.add("7 days");
        dayList.add("15 days");
        dayList.add("30 days");


        ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, dayList);
        aa.setDropDownViewResource(R.layout.simple_spinner_item);
        spNoOfDays.setAdapter(aa);


        spNoOfDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (dayList.get(i).equals("7 days")) {
                    strNoOfDays = "7";
                } else if (dayList.get(i).equals("15 days")) {
                    strNoOfDays = "15";
                } else {
                    strNoOfDays = "30";
                }
//                strCategory = catnameList1.get(i).getId();
//                Log.e("strCategory", strCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void categoryData() {
        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCategory> call1 = apiService.api_categories(strToken, strUid);
        call1.enqueue(new Callback<ModelCategory>() {
            @Override
            public void onResponse(Call<ModelCategory> call, retrofit2.Response<ModelCategory> response) {
                loading.dismiss();


                if (response.body().getSuccess() == true) {
                    catnameList1 = new ArrayList<>();
                    catnameList1 = response.body().getData();
                    if (getActivity() != null) {

                        catnameList = new ArrayList<>();
                        for (int i = 0; i < catnameList1.size(); i++) {
                            catnameList.add(catnameList1.get(i).getName());
                        }


                        ArrayAdapter aa = new ArrayAdapter(getActivity(), R.layout.simple_spinner_item, catnameList);
                        aa.setDropDownViewResource(R.layout.simple_spinner_item);
                        spCategory.setAdapter(aa);
                    }


                    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            strCategory = catnameList1.get(i).getId();
                            Log.e("strCategory", strCategory);
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
            public void onFailure(Call<ModelCategory> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void findId(View root) {

        edTitle = root.findViewById(R.id.create_ad_ed_title);
        edWebsite = root.findViewById(R.id.create_ad_ed_website);
        spCategory = root.findViewById(R.id.create_ad_sp_categories);
        spNoOfDays = root.findViewById(R.id.create_ad_sp_no_of_day);
        spShowType = root.findViewById(R.id.create_ad_sp_show_type);
        imgAd = root.findViewById(R.id.create_ad_img_ad);
        btnSubmit = root.findViewById(R.id.create_ad_btn_submit);
    }
}
