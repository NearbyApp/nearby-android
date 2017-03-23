package io.nearby.android.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import io.nearby.android.NearbyApplication;
import io.nearby.android.R;
import io.nearby.android.data.User;
import io.nearby.android.ui.BaseActivity;
import io.nearby.android.ui.glide.CircleTransform;
import io.nearby.android.ui.help.HelpActivity;
import io.nearby.android.ui.map.MapFragment;
import io.nearby.android.ui.myspotted.MySpottedFragment;
import io.nearby.android.ui.settings.SettingsActivity;

public class MainActivity extends BaseActivity<MainContract.Presenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainContract.View{

    private static final int DEFAULT_NAV_DRAWER_ITEM = R.id.map;
    private static final String NAV_DRAWER_INDEX = "nav_drawer_index";

    @Inject MainPresenter mPresenter;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private ImageView mProfilePictureImageView;
    private TextView mProfileFullName;

    private ActionBarDrawerToggle mDrawerToggle;

    private int mCurrentNavDrawerItem = DEFAULT_NAV_DRAWER_ITEM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if(savedInstanceState != null){
            mCurrentNavDrawerItem = savedInstanceState.getInt(NAV_DRAWER_INDEX);
        }

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProfileFullName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_full_name);
        mProfilePictureImageView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_picture);

        this.setupActionBarAndNavigationDrawer();

        DaggerMainComponent.builder()
                .mainPresenterModule(new MainPresenterModule(this))
                .dataManagerComponent(((NearbyApplication) getApplication())
                        .getDataManagerComponent()).build()
                .inject(this);

        mPresenter.getUserInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(NAV_DRAWER_INDEX, mCurrentNavDrawerItem);
        super.onSaveInstanceState(outState);
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

    @Override
    public void onUserInfoReceived(User user) {
        mProfileFullName.setText(user.getFullName());

        if(user.getProfilePictureUrl() != null){
            Glide.with(this)
                    .load(user.getProfilePictureUrl())
                    .transform(new CircleTransform(this))
                    .into(mProfilePictureImageView);
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = (MainPresenter) presenter;
    }

    private void setupActionBarAndNavigationDrawer(){
        //Setting toolbar
        setSupportActionBar(mToolbar);

        mNavigationView.setNavigationItemSelectedListener(this);

        //Initializing ActionBArDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.nav_drawer_open, R.string.nav_drawer_close);

        // Adding a drawer listener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView.setCheckedItem(mCurrentNavDrawerItem);
        navigate(mCurrentNavDrawerItem);

        //TODO fetch user info to show in the drawer
    }

    private void navigate(int itemId){
        Intent intent;

        switch(itemId){
            case R.id.map:
                mCurrentNavDrawerItem = itemId;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, MapFragment.newInstance())
                        .commit();
                break;
            case R.id.my_spotted:
                mCurrentNavDrawerItem = itemId;
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, MySpottedFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                intent = new Intent(this,HelpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
