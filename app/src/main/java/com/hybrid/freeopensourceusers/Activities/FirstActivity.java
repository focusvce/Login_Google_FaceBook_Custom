package com.hybrid.freeopensourceusers.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.hybrid.freeopensourceusers.R;
import com.hybrid.freeopensourceusers.Fragments.SessionFragment;
import com.hybrid.freeopensourceusers.Fragments.TrendingFragment;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {



    private ViewPager mViewPager ;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPagerAdapter  mAdapter   =    new ViewPagerAdapter(getSupportFragmentManager());
    GoogleApiClient mGoogleApiClient;
    SharedPreferences user_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        bindViews();

        user_details = getSharedPreferences("user_details", MODE_PRIVATE);
        if(getSharedPreferences("com.hybrid.freeopensourceusers", MODE_PRIVATE).getBoolean("firstrun",true)) {
            user_details.edit().putBoolean("logged_in", false).apply();
            getSharedPreferences("com.hybrid.freeopensourceusers", MODE_PRIVATE).edit().putBoolean("firstrun",false).apply();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, FirstActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    /*
    Initialize the views
     */
    private void bindViews() {

        mToolbar   =    (Toolbar) findViewById(R.id.m_toolbar);
        mViewPager =    (ViewPager) findViewById(R.id.m_viewpager);
        mTabLayout =    (TabLayout) findViewById(R.id.tab_layout);


        if( mToolbar != null)
            setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("FOCUS");

        setupViewPager(mViewPager);

        if(mTabLayout != null)
            mTabLayout.setupWithViewPager(mViewPager);

    }


    public void setupViewPager(ViewPager viewPager){
        mAdapter.addFrag(new TrendingFragment(), "Trending");
        mAdapter.addFrag(new SessionFragment(), "Sessions");
        viewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


     /*
    Class for the View pager
     */

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {


        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings_logged_in:
                Log.e("MAIN", "starting user activity");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.action_settings_logged_out:
                LoginManager.getInstance().logOut();
                signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("logged_in",false).apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...

                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        if(user_details.getBoolean("logged_in",false))
        inflater.inflate(R.menu.menu_main_logged_out, menu);
        else
        inflater.inflate(R.menu.menu_main_logged_in,menu);
        return true;
    }



}
