package app.com.learnfromblogs.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import app.com.learnfromblogs.Model.OtherProfile.ModelOtherProfile;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.APIClient;
import app.com.learnfromblogs.Utils.Constants;
import app.com.learnfromblogs.Utils.CustomPager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class OtherUserProfile extends Fragment {


    TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.ic_news_feed,
            R.drawable.ic_comments,
            R.drawable.ic_heart
    };
    public static String strUid;

    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    CircleImageView imgProfile;
    TextView txtName,txtPoints,txtDesc;
    CustomPager viewPager;
    ImageView imgThreeDots;
    LinearLayout lvPoint;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_profile_fragment, container, false);
        Constants.hideKeyboard(getActivity());
        setHasOptionsMenu(true);
        strUid = getArguments().getString("id");



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

        lvPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fragment = new MyEarningFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                Bundle bundle = new Bundle();
                bundle.putString("userid",strUid);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack("Result");
                fragmentTransaction.commit();
            }
        });


    }



    private void getProfileData() {

        final ProgressDialog loading = new ProgressDialog(getActivity());
        loading.setMessage("Please Wait..");
        loading.setCancelable(false);
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        API apiService = APIClient.getClient().create(API.class);
        Call<ModelOtherProfile> call1 = apiService.api_other_profile(strUid);
        call1.enqueue(new Callback<ModelOtherProfile>() {
            @Override
            public void onResponse(Call<ModelOtherProfile> call, retrofit2.Response<ModelOtherProfile> response) {
                loading.dismiss();



                if(response.body().getSuccess()== true) {

                    Picasso.with(getActivity()).load(response.body().getData().getImageLarge()).into(imgProfile);

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
            public void onFailure(Call<ModelOtherProfile> call, Throwable t) {
                loading.dismiss();

                Log.e("loginData", t.getMessage() + "");
            }
        });
    }

    private void findId(View root) {

        tabLayout = root.findViewById(R.id.profile_tabs);
        viewPager = root.findViewById(R.id.profile_viewpager);
        lvPoint = root.findViewById(R.id.profile_lv_point);
        imgProfile = root.findViewById(R.id.profile_img_profile_pic);
        txtName = root.findViewById(R.id.profile_txt_name);
        txtPoints = root.findViewById(R.id.profile_txt_user_points);
        txtDesc  = root.findViewById(R.id.profile_txt_user_description);
        imgThreeDots = root.findViewById(R.id.profile_img_three_dots);
        imgThreeDots.setVisibility(View.GONE);


    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter1 adapter = new ViewPagerAdapter1(getChildFragmentManager());
        adapter.addFragment(new OtherUserFeedFragment(), "");
        adapter.addFragment(new OtherUserMessageFragment(), "");
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);



    }
}
