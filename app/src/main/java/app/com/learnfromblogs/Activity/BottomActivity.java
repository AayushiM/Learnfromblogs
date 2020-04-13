package app.com.learnfromblogs.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import app.com.learnfromblogs.Activity.ui.HTMLDataActivity;
import app.com.learnfromblogs.Fragment.CategoryFragment;
import app.com.learnfromblogs.Fragment.FeedFragment;
import app.com.learnfromblogs.Fragment.NotificationFragment;
import app.com.learnfromblogs.Fragment.ProfileFragment;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.Constants;

public class BottomActivity extends AppCompatActivity {

    BottomNavigationView navView;
    FragmentManager fragmentManager;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        dl = (DrawerLayout)findViewById(R.id.container);
        nv = findViewById(R.id.navigationView);

        navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (getIntent().getStringExtra("flag").equals("login")) {
            navView.setSelectedItemId(R.id.navigation_home);
        }else {
            navView.setSelectedItemId(R.id.navigation_profile);
        }

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = new Intent(BottomActivity.this, HTMLDataActivity.class);
                switch (menuItem.getItemId()){
                    case R.id.about_us:
                        intent.putExtra("Type", 0);
                        break;
                    case R.id.All_about_lfb:
                        intent.putExtra("Type", 1);
                        break;
                    case R.id.terms:
                        intent.putExtra("Type", 2);
                        break;
                    case R.id.privacy:
                        intent.putExtra("Type", 3);
                        break;
                    case R.id.disclaimer:
                        intent.putExtra("Type", 4);
                        break;
                    case R.id.contact:
                        intent.putExtra("Type", 5);
                        break;
                }
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        dl.openDrawer(GravityCompat.START);
        return super.onSupportNavigateUp();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragment = new FeedFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                    return true;


                case R.id.navigation_profile:

                    fragment = new ProfileFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", Constants.loginSharedPreferences.getString(Constants.uid,""));
                    fragment.setArguments(bundle);
                    fragmentTransaction.addToBackStack("Result");
                    fragmentTransaction.commit();

//                    Intent i = new Intent(getApplicationContext(), ProfileFragment1.class);
//                    startActivity(i);
                    return true;




                case R.id.navigation_create_post:

                    Intent i = new Intent(getApplicationContext(),CreatePostFragment.class);
                    startActivity(i);

                    return true;


                case R.id.navigation_category:

                    fragment = new CategoryFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("cat", "0");
                    fragment.setArguments(bundle1);
                    fragmentTransaction.addToBackStack("Result");
                    fragmentTransaction.commit();

//                    Intent i1 = new Intent(getApplicationContext(),CategoryActivity.class);
//                    i1.putExtra("cat","0");
//                    startActivity(i1);

                    return true;


                case R.id.navigation_notification:

                    fragment = new NotificationFragment();
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack("Result");
                    fragmentTransaction.commit();

                    return true;

            }

            return false;
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);;

        if (fragment != null) {

            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
