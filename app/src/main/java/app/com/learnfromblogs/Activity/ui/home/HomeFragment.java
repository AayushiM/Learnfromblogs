package app.com.learnfromblogs.Activity.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.Adapter.HomeCatAdapter;
import app.com.learnfromblogs.R;

public class HomeFragment extends Fragment {

//    private HomeViewModel homeViewModel;

    RecyclerView rcCatgory;
    HomeCatAdapter adapter;
    List<String> catList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        catData();
        rcCatgory = root.findViewById(R.id.home_rc_category);
//        rcCatgory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        adapter = new HomeCatAdapter(getActivity(),catList);
//        rcCatgory.setAdapter(adapter);
//        rcCatgory.setNestedScrollingEnabled(false);

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });
        return root;
    }

    private void catData() {
        catList = new ArrayList<>();
        catList.add("Business Strategy");
        catList.add("Exam Preparation");
        catList.add("General");
        catList.add("Goal Setting");
        catList.add("Learning Managment");
        catList.add("Online Learning");
    }
}