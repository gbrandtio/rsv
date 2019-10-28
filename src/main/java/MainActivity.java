package com.example.root.rsv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.root.rsv.fragments.FragmentCars;
import com.example.root.rsv.fragments.FragmentClients;
import com.example.root.rsv.fragments.FragmentNotifs;
import com.example.root.rsv.fragments.FragmentRSVs;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  This is the Main Activity after the user is successfully logged in.
 *  In this Activity we show user all the reservations. To do a request we need to
 *  retrieve the user Token from our @Session.
 *
 * @author: Nikolaos Karampinas
 * @email: nkarampi@gmail.com
 * @date: 09/12/18
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.navigation) BottomNavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Fragment fragment;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_rsvs:
                if (!(fragment instanceof FragmentRSVs)) {
                    Menu BVMenu = nv.getMenu();
                    nv.setItemIconTintList(null);
                    fragment = FragmentRSVs.newInstance();
                    setFragment();
                }
                return true;
            case R.id.navigation_cars:
                if (!(fragment instanceof FragmentCars)) {
                    fragment = FragmentCars.newInstance();
                    setFragment();
                }
                return true;
            case R.id.navigation_notifications:
                if (!(fragment instanceof FragmentNotifs)) {
                    fragment = FragmentNotifs.newInstance();
                    setFragment();
                }
                return true;
            case R.id.navigation_clients:
                if (!(fragment instanceof FragmentClients)) {

                    fragment = FragmentClients.newInstance();
                    setFragment();
                }
                return true;
        }
        return true;

    };

    private void setFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.logo_alt);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);


        //Navigation Drawer
        dl = (DrawerLayout)findViewById(R.id.main_activity);
        t = new ActionBarDrawerToggle(MainActivity.this, dl,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        t.setDrawerIndicatorEnabled(false);
        t.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dl.openDrawer(GravityCompat.START);
            }
        });
        t.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragment = FragmentRSVs.newInstance();
        setFragment();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}

