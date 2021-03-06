package com.bashalir.go4lunch.Controllers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bashalir.go4lunch.Controllers.Fragments.ListViewFragment;
import com.bashalir.go4lunch.Controllers.Fragments.MapFragment;
import com.bashalir.go4lunch.Controllers.Fragments.WorkmatesFragment;
import com.bashalir.go4lunch.MainActivity;
import com.bashalir.go4lunch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PageActivity extends AppCompatActivity implements MapFragment.MapFragmentListener, NavigationView.OnNavigationItemSelectedListener {

    public ArrayList<CharSequence> mListIdPlace = new ArrayList<>();
    @BindView(R.id.activity_page_bottom_navigation)
    BottomNavigationView mBottomNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    public ArrayList<CharSequence> getListIdPlace() {
        return mListIdPlace;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureBottomView();
        this.configureNavigationView();

    }

    @Override

    public void onBackPressed() {

        // 5 - Handle back click to close menu

        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            this.mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // 4 - Handle Navigation Item Click

        int id = item.getItemId();


        switch (id) {

            case R.id.search:

                break;

            case R.id.settings:

                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:

                break;

        }


        this.mDrawerLayout.closeDrawer(GravityCompat.START);


        return true;

    }



    private void configureNavigationView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_page, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        //3 - Handle actions on menu items

        switch (item.getItemId()) {

            case R.id.menu_activity_main_search:

                Toast.makeText(this, "Recherche indisponible, demandez plutôt l'avis de Google, c'est mieux et plus rapide.", Toast.LENGTH_LONG).show();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }

    private void configureToolbar() {
        setSupportActionBar(mToolbar);
    }

    private void configureBottomView() {
        final FragmentManager fm = getSupportFragmentManager();


        // define your fragments here
        Fragment mapFragment = new MapFragment();
        Fragment listViewFragment = new ListViewFragment();
        Fragment workmatesFragment = new WorkmatesFragment();

        // handle navigation selection
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction ft;
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, mapFragment).commit();
                                return true;
                            case R.id.action_restaurant:

                                Bundle data = new Bundle();

                                data.putCharSequenceArrayList("KEY2", mListIdPlace);
                                listViewFragment.setArguments(data);

                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, listViewFragment, "KEY2").commit();
                                return true;
                            case R.id.action_workmates:
                                ft = fm.beginTransaction();
                                ft.replace(R.id.content_frame, workmatesFragment).commit();
                                return true;
                        }
                        return true;
                    }
                });
    }


    @Override
    public void mapListRestaurant(ArrayList<CharSequence> listRestaurant) {

        mListIdPlace = listRestaurant;
        Log.d("TAGdetach", "nbr :" + mListIdPlace.get(1));
    }
}



