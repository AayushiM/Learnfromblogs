package app.com.learnfromblogs.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Adapter.DialogtagAdapter;
import app.com.learnfromblogs.Adapter.TagAdapter;
import app.com.learnfromblogs.Adapter.UploadListImage;
import app.com.learnfromblogs.Model.Category.ModelCategory;
import app.com.learnfromblogs.Model.CreatePost.ModelCreatePost;
import app.com.learnfromblogs.Model.Tag.Datum;
import app.com.learnfromblogs.Model.Tag.ModelTag;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import top.defaults.colorpicker.ColorPickerPopup;

public class CreatePostFragment extends AppCompatActivity {

    private static DialogtagAdapter dialogtagAdapter;
    private static TagAdapter tagAdapter;
    private static String strFlag = "";
    Spinner spCategory;
    List<String> catnameList;
    List<app.com.learnfromblogs.Model.Category.Datum> catnameList1;
    private static RecyclerView rcTag;
    RecyclerView imgrv;
    private static List<Datum> tagList;
    private static List<String> autotagList;
    private static List<String> selectTagList;
    ImageView imgAddTag;
    Button btnSubmit;
    String strTag = "", strUid, strToken, strCategory, strDescription, strDescFinal, strWordBoolean = "";
    EditText edTitle, edWebsite;
    Editor editor;
    int count;
    public static TextView txtWordLimit;
    String[] words;
    List<String> imgList;
    List<String> imgsendList;
    int intCountWord = 0;
    ArrayList<File> thumbFileList;
    File compressedImageFile = null;
    String strSizeChecker = "";
    String strSizeChecker1 = "";
    private boolean editing;
    AutoCompleteTextView autoCompleteTextView;
    ImageView img1, img2, close1, close2;
    RelativeLayout rlImg2;
    String strImagePath1 = "", strImagePath2 = "";
    Uri imageUri1, imageUri2;
    String strImg = "";
    private static final int MY_CAPTURE_REQUEST_CODE = 4;
    EditText etOtherCat;
    TextView tvCatOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_fragment);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        imgList = new ArrayList<>();


        thumbFileList = new ArrayList<>();

        Constants.hideKeyboard(CreatePostFragment.this);
        strToken = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");
        Log.e("StrToke", strToken);
        Log.e("strUid", strUid);

        selectTagList = new ArrayList<>();

        findId();
        setUpEditor();

        getTagData();
        getCategory();
        btnClick();

    }

    int REQUEST_CAMERA_BTHUMB = 2, REQUEST_GALLERY_BTHUMB = 3;

    private void selectImage(final boolean isMain) {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                    imageUri = CreatePostFragment.this.getContentResolver().insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    CreatePostFragment.this.startActivityForResult(intent, REQUEST_CAMERA_BTHUMB);

                } else {
                    Intent galleryIntentSingle = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        galleryIntentSingle.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }
                    CreatePostFragment.this.startActivityForResult(galleryIntentSingle, REQUEST_GALLERY_BTHUMB);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

//    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = null;
//        String[] strings = {MediaStore.Images.Media.DATA};
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            cursor = Objects.requireNonNull(this).getContentResolver().query(uri, strings, null, null, null);
//        }
//        assert cursor != null;
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(strings[0]);
//        String path = cursor.getString(idx);
//        cursor.close();
//        return path;
//    }

    public String getRealPathFromURI_1(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this
                .managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void getCategory() {

        final ProgressDialog loading = new ProgressDialog(CreatePostFragment.this);
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
                    if (CreatePostFragment.this != null) {

                        catnameList = new ArrayList<>();

//                            catnameList1.add(catnameList1.size()+1)

                        for (int i = 0; i < catnameList1.size(); i++) {
                            catnameList.add(catnameList1.get(i).getName());
                        }


                        ArrayAdapter aa = new ArrayAdapter(CreatePostFragment.this, R.layout.simple_spinner_item, catnameList);
                        aa.setDropDownViewResource(R.layout.simple_spinner_item);
                        catnameList.add("Other");
//                            catnameList1.add("0");
                        spCategory.setAdapter(aa);
                    }


                    spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            if (spCategory.getSelectedItem().equals("Other")) {
                                strCategory = "other";
                                tvCatOther.setVisibility(View.VISIBLE);
                                etOtherCat.setVisibility(View.VISIBLE);
                            } else {
                                strCategory = catnameList1.get(i).getId();
                                tvCatOther.setVisibility(View.GONE);
                                etOtherCat.setVisibility(View.GONE);
                            }
                            Log.e("strCategory", strCategory);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finishAffinity();
                }

            }


            @Override
            public void onFailure(Call<ModelCategory> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void btnClick() {


//        imgAddTag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tagDialog(CreatePostFragment.this);
//            }
//        });


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreatePostFragment.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreatePostFragment.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreatePostFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    strImg = "1";
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 200);
                } else {
                    ActivityCompat.requestPermissions(CreatePostFragment.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAPTURE_REQUEST_CODE);
                }
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreatePostFragment.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreatePostFragment.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreatePostFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    strImg = "2";
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 200);
                }
                //Old Code
//                strImg = "2";
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("*/*");
//                startActivityForResult(galleryIntent, 200);

            }
        });


        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setImageResource(R.drawable.ic_default_ad_img);
                close1.setVisibility(View.GONE);
                strImagePath1 = "";

            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img2.setImageResource(R.drawable.ic_default_ad_img);
                close2.setVisibility(View.GONE);
                strImagePath2 = "";
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String text = editor.getContentAsSerialized();

                strDescription = editor.getContentAsHTML(text);


                String[] separated = strDescription.split("@123#456&789");


                if (separated.length > 1) {
                    if (separated.length == 2) {
                        strDescFinal = separated[0] + "img1" + separated[1];
                    } else if (separated.length == 3) {
                        strDescFinal = separated[0] + "img1" + separated[1] + "img2" + separated[2];
                    } else if (separated.length == 4) {
                        strDescFinal = separated[0] + "img1" + separated[1] + "img2" + separated[2] + "img3" + separated[3];
                    } else if (separated.length == 5) {
                        strDescFinal = separated[0] + "img1" + separated[1] + "img2" + separated[2] + "img3" + separated[3] + "img4" + separated[4];
                    } else if (separated.length == 6) {
                        strDescFinal = separated[0] + "img1" + separated[1] + "img2" + separated[2] + "img3" + separated[3] + "img4" + separated[4] + "img5" + separated[5];
                    }
                } else {
                    strDescFinal = strDescription;
                }
//                Log.e("strDesc", ""+strDescFinal);


//                Log.e("ListSize", String.valueOf(imgList.size()));
                for (int i = 0; i < selectTagList.size(); i++) {
                    if (i == 0) {
                        strTag = selectTagList.get(i);
                    } else {
                        strTag = strTag + "," + selectTagList.get(i);
                    }
                }
                if (isEmpty()) {

                    imgsendList = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        if (imgList.size() > i) {
                            imgsendList.add(imgList.get(i));
                        } else {
                            imgsendList.add("");
                        }
//                        Log.e("imgList",""+imgList.size());
                    }

                    Log.e("imgList", "" + imgList.size());
                    Log.e("imgsendList", "" + imgsendList.size());
                    Log.e("strDescFinal", strDescFinal);
//                    apiCreatePost();
                    apiCreatePostMultipart();

                }

            }
        });
    }

    private void apiCreatePostMultipart() {


        final ProgressDialog loading = new ProgressDialog(CreatePostFragment.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        MultipartBody.Part part = null;
//        RequestBody file = null;
        if (imageUri1 != null) {
            File file = new File(getRealPathFromURI(imageUri1));
            if (file.exists()) {
                Log.e("strImagePath1", strImagePath1);
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                part = MultipartBody.Part.createFormData("img_large", file.getName(), fileReqBody);
            }
//            file = RequestBody.create(MediaType.parse("text/plain"), strImagePath1 );
        } else {
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), "");
            part = MultipartBody.Part.createFormData("img_large", "", fileReqBody);
        }
        MultipartBody.Part part1 = null;
//        RequestBody file2 = null;
        if (imageUri1 != null) {
//            file2 = RequestBody.create(MediaType.parse("text/plain"), strImagePath1 );
            //File file1 = new File(imageUri2 != null ? getRealPathFromURI(imageUri2) : getRealPathFromURI(imageUri1));

            File file1 = null;

            if (imageUri1 !=null){
                file1 = new File(getRealPathFromURI(imageUri1));
            }

            if (imageUri2 != null){
                file1 = new File(getRealPathFromURI(imageUri2));
            }

           // File file1 = new File(imageUri2 != null ? getRealPathFromURI(imageUri2) : getRealPathFromURI(imageUri1));
            if (file1.exists()) {

                Log.e("strImagePath2", strImagePath2);
                RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                part1 = MultipartBody.Part.createFormData("img_thumb", "", fileReqBody1);
                Log.e("fileReqBody1", fileReqBody1.toString());
            }
        } else {
            RequestBody fileReqBody1 = RequestBody.create(MediaType.parse("image/*"), "");
            part1 = MultipartBody.Part.createFormData("img_thumb", "", fileReqBody1);
        }

        RequestBody bodyUid = RequestBody.create(MediaType.parse("text/plain"), strUid);
        RequestBody bodyToken = RequestBody.create(MediaType.parse("text/plain"), strToken);

        RequestBody bodyTitle = RequestBody.create(MediaType.parse("text/plain"), edTitle.getText().toString());

        RequestBody bodyCategory = RequestBody.create(MediaType.parse("text/plain"), strCategory);
        RequestBody bodylink = RequestBody.create(MediaType.parse("text/plain"), edWebsite.getText().toString());
        RequestBody bodyOtherCategory = RequestBody.create(MediaType.parse("text/plain"), "" + 0);


        RequestBody bodyTag = RequestBody.create(MediaType.parse("text/plain"), strTag);
        RequestBody bodyContent = RequestBody.create(MediaType.parse("text/plain"), strDescFinal);
        RequestBody bodyImg1 = RequestBody.create(MediaType.parse("image/*"), imgsendList.get(0));
        RequestBody bodyImg2 = RequestBody.create(MediaType.parse("image/*"), imgsendList.get(1));
        RequestBody bodyImg3 = RequestBody.create(MediaType.parse("image/*"), imgsendList.get(2));
        RequestBody bodyImg4 = RequestBody.create(MediaType.parse("image/*"), imgsendList.get(3));
        RequestBody bodyImg5 = RequestBody.create(MediaType.parse("image/*"), imgsendList.get(4));
        RequestBody bodyType;
        if (strImagePath1.equals("") && strImagePath2.equals("")) {
            bodyType = RequestBody.create(MediaType.parse("text/plain"), "" + 5);
        } else {
            bodyType = RequestBody.create(MediaType.parse("text/plain"), "" + 1);
        }


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCreatePost> call1 = apiService.api_create_post_multipart(part, part1, bodyToken, bodyUid, bodyTitle,
                bodyCategory, bodyOtherCategory, bodyTag, bodylink, bodyContent,
                bodyImg1, bodyImg2, bodyImg3, bodyImg4, bodyImg5, bodyType);
        call1.enqueue(new Callback<ModelCreatePost>() {
            @Override
            public void onResponse(Call<ModelCreatePost> call, retrofit2.Response<ModelCreatePost> response) {
                loading.dismiss();


                if (response.body().getSuccess() == true) {
                    Intent i = new Intent(CreatePostFragment.this, BottomActivity.class);
                    i.putExtra("flag", "Edit");
                    startActivity(i);

                }
                Toast.makeText(CreatePostFragment.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<ModelCreatePost> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
                Toast.makeText(CreatePostFragment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
//        yourSelectedImage = BitmapFactory.decodeFile(filePath);
        return cursor.getString(column_index);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(CreatePostFragment.this);
    }

    private boolean isEmpty() {

        if (edTitle.getText().toString().trim().equals("")) {
            Toast.makeText(CreatePostFragment.this, "Please,Enter Title", Toast.LENGTH_LONG).show();
            return false;
        } else if (edTitle.getText().toString().trim().length() <= 10) {
            Toast.makeText(CreatePostFragment.this, "Minimum 10 characters", Toast.LENGTH_LONG).show();
            return false;
        } else if (spCategory.getSelectedItem().equals("Other") && etOtherCat.getText().toString().trim().equals("")) {
            Toast.makeText(CreatePostFragment.this, "Please,Enter Other Category.", Toast.LENGTH_LONG).show();
            return false;
        } else if (words == null) {
            Toast.makeText(CreatePostFragment.this, "Please,Write the Description.", Toast.LENGTH_LONG).show();
            return false;
        } else if (words.length > 300) {
            Toast.makeText(CreatePostFragment.this, "Please,Write the Description less than 300 words", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void apiCreatePost() {


        final ProgressDialog loading = new ProgressDialog(CreatePostFragment.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCreatePost> call1 = apiService.api_create_post(strToken, strUid,
                edTitle.getText().toString(),
                Integer.parseInt(strCategory), 0,
                strTag, edWebsite.getText().toString(),
                strDescFinal, imgsendList.get(0), imgsendList.get(1), imgsendList.get(2), imgsendList.get(3), imgsendList.get(4), 5);
        call1.enqueue(new Callback<ModelCreatePost>() {
            @Override
            public void onResponse(Call<ModelCreatePost> call, retrofit2.Response<ModelCreatePost> response) {
                loading.dismiss();

                if (response.body().getSuccess() == true) {
                    Intent i = new Intent(CreatePostFragment.this, BottomActivity.class);
                    i.putExtra("flag", "Edit");
                    startActivity(i);
                    Toast.makeText(CreatePostFragment.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }


            @Override
            public void onFailure(Call<ModelCreatePost> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
                Toast.makeText(CreatePostFragment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTagData() {

        final ProgressDialog loading = new ProgressDialog(CreatePostFragment.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelTag> call1 = apiService.api_tag();
        call1.enqueue(new Callback<ModelTag>() {
            @Override
            public void onResponse(Call<ModelTag> call, retrofit2.Response<ModelTag> response) {
                loading.dismiss();

                if (response.body().getSuccess() == true) {
                    tagList = new ArrayList<>();
                    tagList = response.body().getData();
                }

                autotagList = new ArrayList<>();
                for (int i = 0; i < tagList.size(); i++) {
                    autotagList.add(tagList.get(i).getTitle());
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreatePostFragment.this, R.layout.simple_spinner_item, autotagList);
                autoCompleteTextView.setThreshold(1);//will start working from first character
                autoCompleteTextView.setAdapter(adapter);


                autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        for (int i = 0; i < tagList.size(); i++) {
                            if (autoCompleteTextView.getText().toString().equals(tagList.get(i).getTitle())) {

                                strFlag = "n";
                                String strSelectTag = tagList.get(i).getTitle();
                                if (selectTagList.size() > 0) {
                                    for (int K = 0; K < selectTagList.size(); K++) {
                                        if (selectTagList.get(K).equals(strSelectTag)) {
                                            strFlag = "y";
                                        }
                                    }
                                }

                                if (strFlag.equals("n")) {
                                    selectTagList.add(tagList.get(i).getTitle());
                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreatePostFragment.this, LinearLayoutManager.VERTICAL, false);
                                rcTag.setLayoutManager(linearLayoutManager);
                                tagAdapter = new TagAdapter(CreatePostFragment.this, selectTagList);
                                rcTag.setAdapter(tagAdapter);
//                                rcTag.computeVerticalScrollRange();

                                autoCompleteTextView.setText("");

                                break;
                            }
                        }

                    }
                });


            }


            @Override
            public void onFailure(Call<ModelTag> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }


    private void findId() {
        spCategory = findViewById(R.id.add_post_sp_category);
        rcTag = findViewById(R.id.add_post_tag_recyclerview);
        imgrv = findViewById(R.id.imgrv);
        imgAddTag = findViewById(R.id.add_post_img_ad_tag);
        btnSubmit = findViewById(R.id.add_post_btn_submit);
        edTitle = findViewById(R.id.add_post_ed_title);
        edWebsite = findViewById(R.id.add_post_ed_website);
        editor = findViewById(R.id.add_post_ed_editor);
        txtWordLimit = findViewById(R.id.add_post_txt_wordlimit);
        autoCompleteTextView = findViewById(R.id.add_post_tag_autocomplete);
        img1 = findViewById(R.id.add_post_img1);
        img2 = findViewById(R.id.add_post_img2);
        close1 = findViewById(R.id.add_post_img1_close);
        close2 = findViewById(R.id.add_post_img2_close);
        etOtherCat = findViewById(R.id.etOtherCat);
        tvCatOther = findViewById(R.id.tvCatOther);

        rlImg2 = findViewById(R.id.add_rl_img1);


//        findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
//            }
//        });


//        edDescription = root.findViewById(R.id.add_post_ed_description);


    }


//    private static void tagDialog(final Context context) {
//
//
//        final Dialog alertDialog = new Dialog(context);
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(alertDialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//
//        alertDialog.getWindow().setAttributes(lp);
//        final View rootView = LayoutInflater.from(context).inflate(R.layout.dialoge_tag, null);
//
//
//        final RecyclerView rcDialogTag = (RecyclerView)rootView.findViewById(R.id.dialog_tag_recycelrview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
//        rcDialogTag.setLayoutManager(linearLayoutManager);
//        dialogtagAdapter = new DialogtagAdapter(context,tagList);
//        rcDialogTag.setAdapter(dialogtagAdapter);
//
//
//        dialogtagAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                strFlag="n";
//                if(selectTagList.size() > 0){
//                    for ( int i=0 ; i< selectTagList.size() ; i++){
//                        if(selectTagList.get(i).getTitle().equals(tagList.get(position).getTitle())){
//                            strFlag="y";
//                        }
//                    }
//                }
//
//                if (strFlag.equals("n")) {
//                    selectTagList.add(tagList.get(position));
//                }
//
//                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
//                rcTag.setLayoutManager(linearLayoutManager);
//                tagAdapter = new TagAdapter(CreatePostFragment.this,selectTagList);
//                rcTag.setAdapter(tagAdapter);
//                alertDialog.dismiss();
//
//
//            }
//        });
//
//        alertDialog.setContentView(rootView);
//
//        alertDialog.show();
//    }


    private void setUpEditor() {


        findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });

        findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });


        findViewById(R.id.action_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerPopup.Builder(CreatePostFragment.this)
                        .initialColor(Color.RED) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(findViewById(android.R.id.content), new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
//                                Toast.makeText(CreatePostFragment.this, "picked" + colorHex(color), Toast.LENGTH_LONG).show();
                                editor.updateTextColor(colorHex(color));
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {

                            }
                        });


            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.openImagePicker();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertLink();
            }
        });


        findViewById(R.id.action_erase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clearAllContents();
            }
        });
        //editor.dividerBackground=R.drawable.divider_background_dark;
        //editor.setFontFace(R.string.fontFamily__serif);
        Map<Integer, String> headingTypeface = getHeadingTypeface();
        Map<Integer, String> contentTypeface = getContentface();
        editor.setHeadingTypeface(headingTypeface);
        editor.setContentTypeface(contentTypeface);
        editor.setDividerLayout(R.layout.tmpl_divider_layout);
        editor.setEditorImageLayout(R.layout.tmpl_image_view);
        editor.setListItemLayout(R.layout.tmpl_list_item);

        //editor.setNormalTextSize(10);
        // editor.setEditorTextColor("#FF3333");
        //editor.StartEditor();

//        editor.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event)
//            {
//
////                if (event.getAction() == KeyEvent.ACTION_DOWN)
////                {
//                    switch (keyCode)
//                    {
//                        case KeyEvent.KEYCODE_DPAD_CENTER:
//                        case KeyEvent.KEYCODE_ENTER:
//                            Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
//                            strSizeChecker1 = strSizeChecker1+strSizeChecker;
//
//                            return true;
//                        default:
//                            break;
//                    }
//
//
//
////                }
//                return false;
//            }
//        });


        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();


                String text1 = editor.getContentAsSerialized();

                String strDescription1 = editor.getContentAsHTML(text1);

                String plain = Html.fromHtml(strDescription1).toString();
                Log.e("text1", plain);
                words = plain.split(" ");


//                words   = text.toString().split(" ");


//                String yourText= text.toString().replace(String.valueOf((char) 160), " ");
//                if (yourText.split("\\s+").length > 0 ) {
//
//
//
//                    int space = 0;
//                    int length = 0;
//                    for (int i = 0; i < yourText.length(); i++) {
//                        if (yourText.charAt(i) == ' ') {
//                            space++;
//                            if (space >= 0) {
//                                intCountWord  = intCountWord + 1;
//                                break;
//                            }
//
//                        }
//                    }
//                    if (length > 1) {
//////                        editText.getText().delete(length, yourText.length()); // deleting last space
//                        setCharLimit(editText, length - 1); //or limit edit text
//                    }
//                } else {
//                    removeFilter(editText);
//                }


                Log.e("strSizeChecker", strSizeChecker);


                if (words.length > 300) {
                    // Trim words to length MAX_WORDS
                    // Join words into a String
                    strWordBoolean = "y";
                    txtWordLimit.setText("Your Description must not be greater than 300 words.");
                    txtWordLimit.setTextColor(ContextCompat.getColor(CreatePostFragment.this, R.color.red));
                } else {

                    txtWordLimit.setText(words.length + "/300");
                    txtWordLimit.setTextColor(ContextCompat.getColor(CreatePostFragment.this, R.color.green));
                }

            }

            @Override
            public void onUpload(Bitmap image, String uuid) {
//                Toast.makeText(CreatePostFragment.this, uuid, Toast.LENGTH_LONG).show();
                /**
                 * TODO do your upload here from the bitmap received and all onImageUploadComplete(String url); to insert the result url to
                 * let the editor know the upload has completed
                 */


                editor.onImageUploadComplete("@123#456&789", uuid);
                // editor.onImageUploadFailed(uuid);
            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
                return view;
            }

        });


        /**
         * rendering serialized content
         // */
        //  String serialized = "{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003etextline 1 a great time and I will branch office is closed on Sundays\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"H1\"],\"textSettings\":{\"textColor\":\"#c00000\"},\"type\":\"INPUT\"},{\"content\":[],\"type\":\"hr\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003ethe only one that you have received the stream free and open minded person to discuss a business opportunity to discuss my background.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"childs\":[{\"content\":[\"it is a great weekend and we will have the same to me that the same a great time\"],\"contentStyles\":[\"BOLD\"],\"textSettings\":{\"textColor\":\"#FF0000\"},\"type\":\"IMG_SUB\"}],\"content\":[\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\"],\"type\":\"img\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eI have a place where I have a great time and I will branch manager state to boast a new job in a few weeks and we can host or domain to get to know.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"childs\":[{\"content\":[\"the stream of water in a few weeks and we can host in the stream free and no ippo\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#5E5E5E\"},\"type\":\"IMG_SUB\"}],\"content\":[\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\"],\"type\":\"img\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is that I can get it done today will online at location and I am not a big difference to me so that we are headed \\u003ca href\\u003d\\\"www.google.com\\\"\\u003ewww.google.com\\u003c/a\\u003e it was the only way I.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is not a good day to get the latest version to blame it to the product the.\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"BOLDITALIC\"],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is that I can send me your email to you and I am not able a great time and consideration I have to do the needful.\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"INDENT\"],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eI will be a while ago to a great weekend a great time with the same.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}";
//        String serialized = "{\"nodes\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003e\\u003cspan style\\u003d\\\"color:#000000;\\\"\\u003e\\u003cspan style\\u003d\\\"color:#000000;\\\"\\u003eit is not available beyond that statue in a few days and then we could\\u003c/span\\u003e\\u003c/span\\u003e\\u003c/p\\u003e\\n\"],\"contentStyles\":[\"H1\"],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[],\"type\":\"hr\"},{\"content\":[\"author-tag\"],\"macroSettings\":{\"data-author-name\":\"Alex Wong\",\"data-tag\":\"macro\",\"data-date\":\"12 July 2018\"},\"type\":\"macro\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is a free trial to get a great weekend a good day to you u can do that for.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is that I have to do the needful as early in life is not available beyond my imagination to be a good.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"childs\":[{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003e\\u003cb\\u003eit is not available in the next week or two and I have a place where I\\u003c/b\\u003e\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#006AFF\"},\"type\":\"IMG_SUB\"}],\"content\":[\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\"],\"type\":\"img\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is not available in the next week to see you tomorrow morning to see you then.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"},{\"content\":[],\"type\":\"hr\"},{\"content\":[\"\\u003cp dir\\u003d\\\"ltr\\\"\\u003eit is not available in the next day delivery to you soon with it and.\\u003c/p\\u003e\\n\"],\"contentStyles\":[],\"textSettings\":{\"textColor\":\"#000000\"},\"type\":\"INPUT\"}]}";
        // EditorContent des = editor.getContentDeserialized(serialized);
        // editor.render(des);

//        Intent intent = new Intent(getApplicationContext(), RenderTestActivity.class);
//        intent.putExtra("content", serialized);
//        startActivity(intent);


        /**
         * Rendering html
         */
        //render();
        //editor.render();  // this method must be called to start the editor
//        String text = "<h1 data-tag=\"input\" style=\"color:#c00000;\"><span style=\"color:#C00000;\">textline 1 a great time and I will branch office is closed on Sundays</span></h1><hr data-tag=\"hr\"/><p data-tag=\"input\" style=\"color:#000000;\">the only one that you have received the stream free and open minded person to discuss a business opportunity to discuss my background.</p><div data-tag=\"img\"><img src=\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\" /><p data-tag=\"img-sub\" style=\"color:#FF0000;\" class=\"editor-image-subtitle\"><b>it is a great weekend and we will have the same to me that the same a great time</b></p></div><p data-tag=\"input\" style=\"color:#000000;\">I have a place where I have a great time and I will branch manager state to boast a new job in a few weeks and we can host or domain to get to know.</p><div data-tag=\"img\"><img src=\"http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg\" /><p data-tag=\"img-sub\" style=\"color:#5E5E5E;\" class=\"editor-image-subtitle\">the stream of water in a few weeks and we can host in the stream free and no ippo</p></div><p data-tag=\"input\" style=\"color:#000000;\">it is that I can get it done today will online at location and I am not a big difference to me so that we are headed <a href=\"www.google.com\">www.google.com</a> it was the only way I.</p><blockquote data-tag=\"input\" style=\"color:#000000;\">I have to do the negotiation and a half years old story and I am looking forward in a few days.</blockquote><p data-tag=\"input\" style=\"color:#000000;\">it is not a good day to get the latest version to blame it to the product the.</p><ol data-tag=\"ol\"><li data-tag=\"list-item-ol\"><span style=\"color:#000000;\">it is that I can send me your email to you and I am not able a great time and consideration I have to do the needful.</span></li><li data-tag=\"list-item-ol\"><span style=\"color:#000000;\">I have to do the needful and send to me and</span></li><li data-tag=\"list-item-ol\"><span style=\"color:#000000;\">I will be a while ago to a great weekend a great time with the same.</span></li></ol><p data-tag=\"input\" style=\"color:#000000;\">it was u can do to make an offer for a good day I u u have been working with a new job to the stream free and no.</p><p data-tag=\"input\" style=\"color:#000000;\">it was u disgraced our new home in time to get the chance I could not find a good idea for you have a great.</p><p data-tag=\"input\" style=\"color:#000000;\">I have to do a lot to do the same a great time and I have a great.</p><p data-tag=\"input\" style=\"color:#000000;\"></p>";
        //editor.render("<p>Hello man, whats up!</p>");
        //String text = "<p data-tag=\"input\" style=\"color:#000000;\">I have to do the needful and send to me and my husband is in a Apple has to offer a variety is not a.</p><p data-tag=\"input\" style=\"color:#000000;\">I have to go with you will be highly grateful if we can get the latest</p><blockquote data-tag=\"input\" style=\"color:#000000;\">I have to do the negotiation and a half years old story and I am looking forward in a few days.</blockquote><p data-tag=\"input\" style=\"color:#000000;\">I have to do the needful at your to the product and the other to a new job is going well and that the same old stuff and a half day city is the stream and a good idea to get onboard the stream.</p>";
//        editor.render(text);
//        root.findViewById(R.id.btnRender).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*
//                Retrieve the content as serialized, you could also say getContentAsHTML();
//                */
//                String text = editor.getContentAsSerialized();
//                editor.getContentAsHTML();
//                String mSerializedHtml= editor.getContentAsHTML(text);
//                Log.e("mSerializedHtml",mSerializedHtml);


//                Intent intent = new Intent(getApplicationContext(), RenderTestActivity.class);
//                intent.putExtra("content", text);
//                startActivity(intent);
//            }
//        });


        /**
         * Since the endusers are typing the content, it's always considered good idea to backup the content every specific interval
         * to be safe.
         *
         private final long backupInterval = 10 * 1000;
         Timer timer = new Timer();
         timer.scheduleAtFixedRate(new TimerTask() {
        @Override public void run() {
        String text = editor.getContentAsSerialized();
        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        preferences.putString(String.format("backup-{0}",  new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new Date())), text);
        preferences.apply();
        }
        }, 0, backupInterval);

         */
    }


    private InputFilter filter;

    private void setCharLimit(EditText et, int max) {
        filter = new InputFilter.LengthFilter(max);
        et.setFilters(new InputFilter[]{filter});
    }

    private void removeFilter(EditText et) {
        if (filter != null) {
            et.setFilters(new InputFilter[0]);
            filter = null;
        }
    }


    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK){
            if (strImg.equals("1")) {
                try {
                    imageUri1 = data.getData();
                    img1.setImageURI(imageUri1);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri1);
                    strImagePath1 = imageUri1.getPath();
                    close1.setVisibility(View.VISIBLE);
                    rlImg2.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            }
            if (strImg.equals("2")) {
                try {
                    imageUri2 = data.getData();
                    img2.setImageURI(imageUri1);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri2);
                    strImagePath2 = imageUri2.getPath();
                    close2.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            }

            //Old Code

           /* else {
                imageUri2 = data.getData();
                strImagePath2 = imageUri2.getPath();
                img2.setImageURI(imageUri2);
                close2.setVisibility(View.VISIBLE);
            }*/
        }
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(CreatePostFragment.this.getContentResolver(), uri);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded", encoded);
                imgList.add(encoded);
                Log.d("this", String.valueOf(bitmap));
                editor.insertImage(bitmap);
            } catch (IOException e) {

                Toast.makeText(CreatePostFragment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(CreatePostFragment.this, "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (strImg.equals("1")) {
                try {
                    imageUri1 = CropImage.getPickImageResultUri(CreatePostFragment.this, data);
                    img1.setImageURI(imageUri1);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri1);
                    strImagePath1 = imageUri1.getPath();
                    close1.setVisibility(View.VISIBLE);
                    rlImg2.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                }
            } else {
                imageUri2 = CropImage.getPickImageResultUri(CreatePostFragment.this, data);
                strImagePath2 = imageUri2.getPath();
                img2.setImageURI(imageUri2);
                close2.setVisibility(View.VISIBLE);
            }
//            startCropImageActivity(imageUri);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //((ImageButton) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Log.e("Uri", "" + result.getUri().getPath());
//                strImagePath = result.getUri().getPath();
                if (strImg.equals("1")) {
                    img1.setImageURI(result.getUri());
                    strImagePath1 = result.getUri().getPath();
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(CreatePostFragment.this.getContentResolver(), result.getUri());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    close1.setVisibility(View.VISIBLE);
                    rlImg2.setVisibility(View.VISIBLE);
                } else {
                    strImagePath2 = result.getUri().getPath();
                    img2.setImageURI(result.getUri());
                    close2.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(2, 2).start(CreatePostFragment.this);

    }


    public Map<Integer, String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/RobotoCondensed-Regular.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/RobotoCondensed-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/RobotoCondensed-Italic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/RobotoCondensed-BoldItalic.ttf");
        return typefaceMap;
    }

    public Map<Integer, String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/RobotoCondensed-Regular.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/RobotoCondensed-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/RobotoCondensed-Italic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/RobotoCondensed-BoldItalic.ttf");
        return typefaceMap;
    }

}
