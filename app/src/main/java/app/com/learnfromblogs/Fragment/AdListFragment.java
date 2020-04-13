package app.com.learnfromblogs.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.Constants;

public class AdListFragment extends Fragment {

    private RecyclerView rcAd;
    private ProgressBar progressBar;
    ImageView imgPlus;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_ad_list_fragment, container, false);
        Constants.hideKeyboard(getActivity());


        findId(root);


        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CreateAdFragment();
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("creatad");
                fragmentTransaction.commit();
            }
        });


        return root;
    }

    private void findId(View root) {

        rcAd = root.findViewById(R.id.ad_list_recyclerview);
        progressBar = root.findViewById(R.id.ad_list_progressbar);
        imgPlus = root.findViewById(R.id.ad_list_img_plus);
    }

}
