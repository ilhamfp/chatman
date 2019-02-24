package com.chatman.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chatman.R;
import com.chatman.fragment.BotFragment;
import com.chatman.fragment.HomeFragment;
import com.chatman.fragment.ProfileFragment;
import com.chatman.helper.PreferencesHelper;
import com.chatman.helper.SensorHelper;

public class MainActivity extends AppCompatActivity implements
        ProfileFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        BotFragment.OnFragmentInteractionListener {

    private Context context;
    private BottomNavigationView bottomNavbar;
    private SensorHelper mSensorHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    loadFragment(new ProfileFragment()); break;
                case R.id.navigation_home:
                    loadFragment(new HomeFragment()); break;
                case R.id.navigation_bot:
                    loadFragment(new BotFragment()); break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        ImageView toolbarImage = findViewById(R.id.toolbar_image);
        Glide.with(this).load(getImage("logo")).fitCenter().into(toolbarImage);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(ContextCompat
                        .getColor(this, android.R.color.black));
            }
            else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(ContextCompat
                        .getColor(this, R.color.white));
            }

        }
        context = this;

        //Set sensor
        mSensorHelper = new SensorHelper(context);

        // Bottom Navigation Bar
        bottomNavbar = findViewById(R.id.navigation);
        bottomNavbar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Set laman pertama
        loadFragment(new ProfileFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }



    public boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorHelper.listenSensor(Sensor.TYPE_PROXIMITY);
        mSensorHelper.listenSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorHelper.unlistenSensor();
    }
}
