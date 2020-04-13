package app.com.learnfromblogs.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Adapter.TagAdapter;
import app.com.learnfromblogs.Model.Category.ModelCategory;
import app.com.learnfromblogs.Model.CreatePost.ModelCreatePost;
import app.com.learnfromblogs.Model.SinglePost.ModelSinglePost;
import app.com.learnfromblogs.Model.Tag.Datum;
import app.com.learnfromblogs.Model.Tag.ModelTag;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import top.defaults.colorpicker.ColorPickerPopup;

public class EditPostFragment extends AppCompatActivity {

    private Spinner spCategory;
    private EditText edTitle,edWebsite;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView imgAddTag;
    private Button btnSubmit;
    private RecyclerView rcTag;
    public static TextView txtWordLimit;
    private Editor editor;
    private List<String> catnameList;
    private List<app.com.learnfromblogs.Model.Category.Datum> catnameList1;
    private List<String> selectTagList;
    private List<String> selectTagList1;
    private  String strTokenId, strUid,strId,strCategory,strWordBoolean="",strDescription,strDescFinal,strTag;
    private static List<Datum> tagList;
    private static String strFlag="";
    private static List<String> autotagList;
    private static TagAdapter tagAdapter;
    private  String[] words;
    private List <String> imgList;
    List <String> imgsendList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_fragment);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Constants.hideKeyboard(EditPostFragment.this);
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken, "");
        strUid = Constants.loginSharedPreferences.getString(Constants.uid, "");

        imgList = new ArrayList<>();
        findId();

//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
            strId = getIntent().getStringExtra("id");
//        }

        getPostData();
        setUpEditor();
        btnClick();




    }




    private void btnClick() {



//        imgAddTag.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tagDialog(CreatePostFragment.this);
//            }
//        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String text = editor.getContentAsSerialized();

                strDescription = editor.getContentAsHTML(text);




                String[] separated = strDescription.split("@123#456&789");


                if(separated.length > 1) {
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
                }else {
                    strDescFinal = strDescription;
                }
//                Log.e("strDesc", ""+strDescFinal);



//                Log.e("ListSize", String.valueOf(imgList.size()));
                for(int i=0 ; i<selectTagList.size() ; i++){
                    if(i==0) {
                        strTag =selectTagList.get(i);
                    }else {
                        strTag =strTag+","+selectTagList.get(i);
                    }
                }
                if(isEmpty()) {

                    imgsendList = new ArrayList<>();
                    for(int i=0;i<5;i++)
                    {
                        if(imgList.size() > i)
                        {
                            imgsendList.add(imgList.get(i));
                        }else {
                            imgsendList.add("");
                        }
//                        Log.e("imgList",""+imgList.size());
                    }

                    Log.e("imgList",""+imgList.size());
                    Log.e("strDescFinal",strDescFinal);
                    apiUpdateData();

                }

            }
        });
    }

    private void apiUpdateData() {

        final ProgressDialog loading = new ProgressDialog(EditPostFragment.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCreatePost> call1 = apiService.api_update_post(strTokenId,strUid,strId,
                edTitle.getText().toString(),
                Integer.parseInt(strCategory),0,
                strTag,edWebsite.getText().toString(),
                strDescFinal,imgsendList.get(0),imgsendList.get(1),imgsendList.get(2),imgsendList.get(3),imgsendList.get(4), 5);
        call1.enqueue(new Callback<ModelCreatePost>() {
            @Override
            public void onResponse(Call<ModelCreatePost> call, retrofit2.Response<ModelCreatePost> response) {
                loading.dismiss();


//                try {
//                    Log.e("ResponseBody",response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                if(response.body().getSuccess()== true){
                   Intent i = new Intent(EditPostFragment.this, BottomActivity.class);
                    i.putExtra("flag","Edit");
                    startActivity(i);
                    Toast.makeText(EditPostFragment.this, "" +response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }


            }


            @Override
            public void onFailure(Call<ModelCreatePost> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });

    }

    private boolean isEmpty() {

        if (edTitle.getText().toString().trim().equals("")) {
            Toast.makeText(EditPostFragment.this, "Please,Enter Title", Toast.LENGTH_LONG).show();
            return false;
        } else if (edTitle.getText().toString().trim().length() <= 10) {
            Toast.makeText(EditPostFragment.this, "Minimum 10 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (words == null ) {
            Toast.makeText(EditPostFragment.this, "Please,Write the Description.", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (words.length  > 200 ) {
            Toast.makeText(EditPostFragment.this, "Please,Write the Description less than 200 words", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

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
                new ColorPickerPopup.Builder(EditPostFragment.this)
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
        Map<Integer, String> headingTypeface =  getHeadingTypeface();
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
                Log.e("text1",plain);
                words   = plain.split(" ");


                if (words.length > 200) {
                    // Trim words to length MAX_WORDS
                    // Join words into a String
                    strWordBoolean = "y";
                    txtWordLimit.setText("Your Description must not be greater than 200 words.");
                    txtWordLimit.setTextColor(ContextCompat.getColor(EditPostFragment.this, R.color.red));
                }else {

                    txtWordLimit.setText(words.length+"/200");
                    txtWordLimit.setTextColor(ContextCompat.getColor(EditPostFragment.this, R.color.green));
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


    private String colorHex(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return String.format(Locale.getDefault(), "#%02X%02X%02X", r, g, b);
    }

    private void getPostData() {

        final ProgressDialog loading = new ProgressDialog(EditPostFragment.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelSinglePost> call1 = apiService.api_my_post_single(strTokenId,strUid,strId);
        call1.enqueue(new Callback<ModelSinglePost>() {
            @Override
            public void onResponse(Call<ModelSinglePost> call, retrofit2.Response<ModelSinglePost> response) {
                loading.dismiss();


                if(response.body().getSuccess()== true) {
                    edTitle.setText(response.body().getData().getTitle());
                    edWebsite.setText(response.body().getData().getPostLink());

                    selectTagList1 = new ArrayList<>();
                    selectTagList1 = Arrays.asList(response.body().getData().getTagIds().split(","));
                    editor.render(response.body().getData().getContent());

                    String strDescription1 = editor.getContentAsHTML(editor.getContentAsSerialized());

                    String plain = Html.fromHtml(strDescription1).toString();
                    Log.e("text1",plain);
                    words   = plain.split(" ");
                    if (words.length > 200) {
                        // Trim words to length MAX_WORDS
                        // Join words into a String
                        strWordBoolean = "y";
                        txtWordLimit.setText("Your Description must not be greater than 200 words.");
                        txtWordLimit.setTextColor(ContextCompat.getColor(EditPostFragment.this, R.color.red));
                    }else {

                        txtWordLimit.setText(words.length+"/200");
                        txtWordLimit.setTextColor(ContextCompat.getColor(EditPostFragment.this, R.color.green));
                    }

                    getCategory(response.body().getData().getSubjectId());
                    getTagData();


                }

            }


            @Override
            public void onFailure(Call<ModelSinglePost> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.e("encoded",encoded);
                imgList.add(encoded);
                // Log.d(TAG, String.valueOf(bitmap));
                editor.insertImage(bitmap);
            } catch (IOException e) {

                Toast.makeText(EditPostFragment.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(EditPostFragment.this, "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }
    }

    private void getTagData() {

        final ProgressDialog loading = new ProgressDialog(EditPostFragment.this);
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

                if(response.body().getSuccess()== true) {
                    tagList = new ArrayList<>();
                    tagList = response.body().getData();

                    selectTagList = new ArrayList<>();

                    for (int i = 0; i < selectTagList1.size(); i++) {
                        for (int j=0; j<tagList.size();j++) {
                            if (selectTagList1.get(i).equals(tagList.get(j).getId())){
                                selectTagList.add(tagList.get(j).getTitle());
                            }
                        }
                    }
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditPostFragment.this, LinearLayoutManager.VERTICAL, false);
                    rcTag.setLayoutManager(linearLayoutManager);
                    tagAdapter = new TagAdapter(EditPostFragment.this, selectTagList);
                    rcTag.setAdapter(tagAdapter);

                    autotagList = new ArrayList<>();
                    for (int i = 0; i < tagList.size(); i++) {
                        autotagList.add(tagList.get(i).getTitle());
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditPostFragment.this, R.layout.simple_spinner_item, autotagList);
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

                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EditPostFragment.this, LinearLayoutManager.VERTICAL, false);
                                    rcTag.setLayoutManager(linearLayoutManager);
                                    tagAdapter = new TagAdapter(EditPostFragment.this, selectTagList);
                                    rcTag.setAdapter(tagAdapter);
//                                rcTag.computeVerticalScrollRange();

                                    autoCompleteTextView.setText("");

                                    break;
                                }
                            }

                        }
                    });

                }



            }


            @Override
            public void onFailure(Call<ModelTag> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void getCategory(final String subjectId) {

        final ProgressDialog loading = new ProgressDialog(EditPostFragment.this);
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelCategory> call1 = apiService.api_categories(strTokenId,strUid);
        call1.enqueue(new Callback<ModelCategory>() {
            @Override
            public void onResponse(Call<ModelCategory> call, retrofit2.Response<ModelCategory> response) {
                loading.dismiss();


                if (response.body().getSuccess() == true){
                    catnameList1 = new ArrayList<>();
                    catnameList1 = response.body().getData();
                    if (EditPostFragment.this != null) {

                        catnameList = new ArrayList<>();
                        for (int i = 0; i < catnameList1.size(); i++) {
                            catnameList.add(catnameList1.get(i).getName());
                        }


                        ArrayAdapter aa = new ArrayAdapter(EditPostFragment.this, R.layout.simple_spinner_item, catnameList);
                        aa.setDropDownViewResource(R.layout.simple_spinner_item);
                        spCategory.setAdapter(aa);
                    }

                    for (int j=0;j< catnameList1.size() ; j++){

                        if(catnameList1.get(j).getId().equals(subjectId)){
                            spCategory.setSelection(j);
                            break;
                        }
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
                }else {
                    Intent i = new Intent(EditPostFragment.this, LoginActivity.class);
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

    private void findId() {
        spCategory = findViewById(R.id.add_post_sp_category);
        rcTag = findViewById(R.id.add_post_tag_recyclerview);
        imgAddTag = findViewById(R.id.add_post_img_ad_tag);
        btnSubmit = findViewById(R.id.add_post_btn_submit);
        edTitle = findViewById(R.id.add_post_ed_title);
        edWebsite = findViewById(R.id.add_post_ed_website);
        editor =  findViewById(R.id.add_post_ed_editor);
        txtWordLimit = findViewById(R.id.add_post_txt_wordlimit);
        autoCompleteTextView = findViewById(R.id.add_post_tag_autocomplete);
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
