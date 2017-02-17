package io.nearby.android.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.nearby.android.R;
import io.nearby.android.ui.help.HelpActivity;
import io.nearby.android.ui.map.MapFragment;
import io.nearby.android.ui.myspotted.MySpottedFragment;

/**
 * Created by Marc on 2017-01-26.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final int DEFAULT_NAV_DRAWER_ITEM = R.id.map;
    private static final String NAV_DRAWER_INDEX = "nav_drawer_index";
    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentNavDrawerItem = DEFAULT_NAV_DRAWER_ITEM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

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
        }else{
            if(mCurrentNavDrawerItem != R.id.map) {
                mCurrentNavDrawerItem = R.id.map;
                mNavigationView.setCheckedItem(R.id.map);
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(mCurrentNavDrawerItem != item.getItemId()){
            navigate(item.getItemId());
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
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
        mCurrentNavDrawerItem = itemId;

        switch(itemId){
            case R.id.map:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, MapFragment.newInstance(), MAP_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.my_spotted:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, MySpottedFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.settings:
                break;
            case R.id.help:
                Intent intent = new Intent(this,HelpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
