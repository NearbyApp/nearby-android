package io.nearby.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Marc on 2017-01-26.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        this.setupActionBarAndNavigationDrawer();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.map:
                return true;
            case R.id.my_spotted:
                return true;
            case R.id.settings:
                break;
            case R.id.help:
                break;
        }

        return false;
    }

    private void setupActionBarAndNavigationDrawer(){
        //Setting toolbar
        this.setSupportActionBar(mToolbar);

        //Initializing ActionBArDrawerToggle
        this.mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.nav_drawer_open,R.string.nav_drawer_close);

        // Adding a drawer listener
        this.mDrawerLayout.addDrawerListener(mDrawerToggle);
        this.mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);
    }
}
