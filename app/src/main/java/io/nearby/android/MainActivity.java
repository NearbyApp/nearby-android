package io.nearby.android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import io.nearby.android.map.MapFragment;

/**
 * Created by Marc on 2017-01-26.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int DEFAULT_NAV_DRAWER_ITEM = R.id.map;
    private static final String NAV_DRAWER_INDEX = "nav_drawer_index";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentNavDrawerItem = DEFAULT_NAV_DRAWER_ITEM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(this);

        if(savedInstanceState != null){
            mCurrentNavDrawerItem = savedInstanceState.getInt(NAV_DRAWER_INDEX);
        }

        this.setupActionBarAndNavigationDrawer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_DRAWER_INDEX, mCurrentNavDrawerItem);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.support.v7.appcompat.R.id.home){
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean shouldItemBeSelected = false;

        switch(item.getItemId()){
            case R.id.map:
                mCurrentNavDrawerItem = item.getItemId();
                shouldItemBeSelected = true;
                break;
            case R.id.my_spotted:
                mCurrentNavDrawerItem = item.getItemId();
                shouldItemBeSelected = true;
                break;
            case R.id.settings:
                break;
            case R.id.help:
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return shouldItemBeSelected;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab:
                break;
        }
    }

    private void setupActionBarAndNavigationDrawer(){
        //Setting toolbar
        setSupportActionBar(mToolbar);

        mNavigationView.setNavigationItemSelectedListener(this);

        //Initializing ActionBArDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);

        // Adding a drawer listener
        this.mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView.setCheckedItem(mCurrentNavDrawerItem);
        navigate(mCurrentNavDrawerItem);
    }

    private void navigate(int itemId){
        switch(itemId){
            case R.id.map:
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container,new MapFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.my_spotted:
                break;
            case R.id.settings:
                break;
            case R.id.help:
                break;
            default:
                break;
        }
    }
}
