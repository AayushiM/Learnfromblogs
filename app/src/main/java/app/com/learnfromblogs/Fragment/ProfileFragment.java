package app.com.learnfromblogs.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import app.com.learnfromblogs.API.API;
import app.com.learnfromblogs.Activity.LoginActivity;
import app.com.learnfromblogs.Adapter.ViewPagerAdapter1;
import app.com.learnfromblogs.Model.Profile.ModelProfile;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import app.com.learnfromblogs.Utils.CustomPager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class ProfileFragment extends Fragment {


    TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.ic_news_feed,
            R.drawable.ic_comments,
            R.drawable.ic_heart
    };
    String strUid,strTokenId;

    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    CircleImageView imgProfile;
    TextView txtName,txtPoints,txtDesc;
    CustomPager viewPager;
    LinearLayout lvProfile;
    ImageView imgThreeDots;



//    CustomPager userviewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        setHasOptionsMenu(true);
        strTokenId = Constants.loginSharedPreferences.getString(Constants.logintoken,"");

        strUid = Constants.loginSharedPreferences.getString(Constants.uid,"");


        findId(root);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        getProfileData();
        btnClick();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabLayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabLayout.getTabAt(position).getIcon().setColorFilter(getResources().getColor(R.color.tab_unselect), PorterDuff.Mode.SRC_IN);
                }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }






    private void btnClick() {

//        imgAddPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                fragment = new CreatePostFragment();
////                fragmentManager = getActivity().getSupportFragmentManager();
////                fragmentTransaction = fragmentManager.beginTransaction();
////                fragmentTransaction.replace(R.id.content_frame, fragment);
////                fragmentTransaction.addToBackStack("Result");
////                fragmentTransaction.commit();
//
//                Intent i = new Intent(getActivity(), CreatePostFragment.class);
//                startActivity(i);
//            }
//        });

        imgThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogOtherOptions();
            }
        });


        lvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new MyEarningFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                Bundle bundle = new Bundle();
                bundle.putString("userid",Constants.loginSharedPreferences.getString(Constants.uid,""));
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("Result");
                fragmentTransaction.commit();
            }
        });

    }

    private void DialogOtherOptions() {

        final Dialog alertDialog = new Dialog(getActivity());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        alertDialog.getWindow().setAttributes(lp);
        final View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialoge_other_menu, null);

        LinearLayout lvAdList = rootView.findViewById(R.id.dialog_other_menu_lv_Ads_list);
        LinearLayout lvMyAccount = rootView.findViewById(R.id.dialog_other_menu_lv_my_account);
        LinearLayout lvUpdateProfile = rootView.findViewById(R.id.dialog_other_menu_lv_update_profile);
        LinearLayout lvLogout= rootView.findViewById(R.id.dialog_other_menu_lv_logout);



        lvMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SettingFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("Result");
                fragmentTransaction.commit();
                alertDialog.dismiss();
            }
        });
        lvAdList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AdListFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("Result");
                fragmentTransaction.commit();
                alertDialog.dismiss();
            }
        });



        lvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new UpdateProfileFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("Result");
                fragmentTransaction.commit();
                alertDialog.dismiss();
            }
        });


        lvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = Constants.loginSharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent i = new Intent(getActivity(),LoginActivity.class);
                startActivity(i);
                getActivity().finishAffinity();
            }
        });






        alertDialog.setContentView(rootView);

        alertDialog.show();

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

                        Picasso.with(getActivity()).load(response.body().getData().getImageThumb()).into(imgProfile);

                    txtName.setText(response.body().getData().getFirstName()+" "+response.body().getData().getLastName());
                    txtPoints.setText(response.body().getData().getPoints());
                    txtDesc.setText(""+response.body().getData().getAboutMe());
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

    private void findId(View root) {

        tabLayout = root.findViewById(R.id.profile_tabs);
        viewPager = root.findViewById(R.id.profile_viewpager);

        imgProfile = root.findViewById(R.id.profile_img_profile_pic);
        txtName = root.findViewById(R.id.profile_txt_name);
        txtPoints = root.findViewById(R.id.profile_txt_user_points);
        txtDesc  = root.findViewById(R.id.profile_txt_user_description);
        imgThreeDots = root.findViewById(R.id.profile_img_three_dots);
        lvProfile = root.findViewById(R.id.profile_lv_point);


    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter1 adapter = new ViewPagerAdapter1(getChildFragmentManager());
        adapter.addFragment(new MyFeedFragment(), "");
        adapter.addFragment(new ProfileTabMessageFragment(), "");
        adapter.addFragment(new MyFavPostFragment(), "");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);


    }
}
