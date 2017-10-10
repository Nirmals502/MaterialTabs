package info.androidhive.materialtabs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import info.androidhive.materialtabs.R;

public class MainActivity extends Activity {
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                SharedPreferences shared = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//                userID = (shared.getString("userid", "nodata"));
//                if(!userID.contentEquals("nodata")) {
//                    // do some thing
//                    Intent i1 = new Intent(Screen_01.this, Navigationbar.class);
//                    i1.putExtra("userid", userID);
//                    startActivity(i1);
//
//                    finish();
//
//                }else{
//                    Intent i = new Intent(Screen_01.this, Main.class);
//                    startActivity(i);
//
//                    finish();
//                }
                Intent i1 = new Intent(MainActivity.this, CustomViewIconTextTabsActivity.class);

                startActivity(i1);

                finish();
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);

            }
        }, SPLASH_TIME_OUT);
    }
}

