package com.bashalir.go4lunch.Controllers.Activities;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import butterknife.BindView;
import butterknife.ButterKnife;

import com.bashalir.go4lunch.Controllers.Fragments.MapFragment;
import com.bashalir.go4lunch.Controllers.Fragments.RestaurantFragment;
import com.bashalir.go4lunch.Controllers.Fragments.WorkmatesFragment;
import com.bashalir.go4lunch.R;

public class PageActivity extends AppCompatActivity {

@BindView(R.id.activity_page_bottom_navigation)
    BottomNavigationView mBottomNavigationView;
@BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureBottomView();
         }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_page, menu);
        return true;
    }

    private void configureToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void configureBottomView() {
        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment mapFragment = new MapFragment();
        final Fragment restaurantFragment = new RestaurantFragment();
        final Fragment workmatesFragment = new WorkmatesFragment();

        // handle navigation selection
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction fragmentTransaction;
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, mapFragment).commit();
                                return true;
                            case R.id.action_restaurant:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, restaurantFragment).commit();
                                return true;
                            case R.id.action_workmates:
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, workmatesFragment).commit();
                                return true;
                        }
                        return true;
                    }
                });
    }


}



