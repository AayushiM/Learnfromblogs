package app.com.learnfromblogs.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import app.com.learnfromblogs.R;
import app.com.learnfromblogs.Utils.Constants;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Constants constants;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        constants = new Constants();
        Constants.loginSharedPreferences = getSharedPreferences(Constants.LoginPREFERENCES, MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


                if (Constants.loginSharedPreferences.getBoolean(Constants.LoginStatus, false)) {
                    Intent i1 = new Intent(SplashActivity.this, BottomActivity.class);
                    i1.putExtra("flag","login");
                    startActivity(i1);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                } else {

                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
