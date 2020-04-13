package app.com.learnfromblogs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import app.com.learnfromblogs.Adapter.ViewPagerAdapter1;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.CustomPager;

public class ProfileFragment1 extends Fragment {


    CustomPager userviewPager;
    TabLayout tabLayout;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_profile_fragment1, container, false);


        tabLayout = (TabLayout) root.findViewById(R.id.auction_details_tabs);
        userviewPager = (CustomPager) root.findViewById(R.id.auction_details_viewpager2);

        setupViewPager(userviewPager);
        tabLayout.setupWithViewPager(userviewPager);


        return root;
    }


    private void setupViewPager(ViewPager viewPager1) {


        ViewPagerAdapter1 adapter = new ViewPagerAdapter1(getActivity().getSupportFragmentManager());
        adapter.addFragment(new MyFeedFragment(), "Top Ten Bidder");
        adapter.addFragment(new ProfileTabMessageFragment(), "Show History");

        viewPager1.setOffscreenPageLimit(1);
        viewPager1.setAdapter(adapter);
    }

}
