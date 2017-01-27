package io.nearby.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import io.nearby.android.map.MapFragment;

/**
 * Created by Marc on 2017-01-26.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int DEFALT_NAV_DRAWER_INDEX = 0;
    private static final String NAV_DRAWER_INDEX = "nav_drawer_index";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;

    private int mCurrentNavDrawerItemIndex = DEFALT_NAV_DRAWER_INDEX;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(this);

        this.setupActionBarAndNavigationDrawer();

        if(savedInstanceState != null){
            int navDrawer_index = savedInstanceState.getInt(NAV_DRAWER_INDEX);
            onNavigationItemSelected(mNavigationView.getMenu().getItem(navDrawer_index));
        }
        else {
            onNavigationItemSelected(mNavigationView.getMenu().getItem(DEFALT_NAV_DRAWER_INDEX));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(NAV_DRAWER_INDEX, mCurrentNavDrawerItemIndex);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean shouldItemBeSelected = false;


        switch(item.getItemId()){
            case R.id.map:
                mCurrentNavDrawerItemIndex = 0;
                shouldItemBeSelected = true;
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container,new MapFragment())
                        .addToBackStack(null)
                        .commit();

                break;
            case R.id.my_spotted:
                mCurrentNavDrawerItemIndex = 1;
                shouldItemBeSelected = true;
                break;
            case R.id.settings:
                break;
            case R.id.help:
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawers();

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
        this.setSupportActionBar(mToolbar);

        //Initializing ActionBArDrawerToggle
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);

        // Adding a drawer listener
        this.mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }
}
