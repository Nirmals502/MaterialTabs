package info.androidhive.materialtabs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.fragments.FiveFragment;
import info.androidhive.materialtabs.fragments.Listing_product;
import info.androidhive.materialtabs.fragments.OneFragment;
import info.androidhive.materialtabs.fragments.HOME_FRAGMENT;
import info.androidhive.materialtabs.fragments.TwoFragment;
import info.androidhive.materialtabs.fragments.download_fragment;
import info.androidhive.materialtabs.fragments.our_services;

public class CustomViewIconTextTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView Txt_Vw_login;
    String Access_tocken = "";
    SharedPreferences shared;
    ImageView Cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_icon_text_tabs);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Txt_Vw_login = (TextView) findViewById(R.id.textView2);
        Cart = (ImageView) findViewById(R.id.imageView3);
        shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
        Access_tocken = (shared.getString("login_status", "nologin"));
        if (Access_tocken.contentEquals("loged_in")) {
            Txt_Vw_login.setText("Logout");
        }
        Txt_Vw_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Access_tocken = (shared.getString("login_status", "nologin"));
                if (Access_tocken.contentEquals("loged_in")) {
                    // Txt_Vw_login.setText("Logout");
                    SharedPreferences shared = getSharedPreferences("Sky_mobile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("login_status", "Logout");
                    //editor.putString("device_id", Device_id);
                    editor.commit();
                    Txt_Vw_login.setText("Login/Signup");
                } else if (Access_tocken.contentEquals("nologin")) {
                    Intent i1 = new Intent(CustomViewIconTextTabsActivity.this, Login_activity.class);

                    startActivity(i1);
                    finish();

                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                } else {
                    Intent i1 = new Intent(CustomViewIconTextTabsActivity.this, Login_activity.class);

                    startActivity(i1);
                    finish();

                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_left);
                }
            }
        });
        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(CustomViewIconTextTabsActivity.this, view_cart.class);

                startActivity(i1);
                finish();

                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_left);
            }
        });
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(2);
        setupTabIcons();
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Our Services");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_applications_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Download");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_file_download_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Home");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabfour = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabfour.setText("Product List");
        tabfour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_assignment_black_24dp, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabfour);

        TextView tabFive = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFive.setText("About Us");
        tabFive.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person_black_24dp, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);
    }

    /**
     * Adding fragments to ViewPager
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new our_services(), "ONE");
        adapter.addFrag(new download_fragment(), "TWO");
        adapter.addFrag(new HOME_FRAGMENT(), "THREE");
        adapter.addFrag(new Listing_product(), "Four");
        adapter.addFrag(new FiveFragment(), "Five");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
