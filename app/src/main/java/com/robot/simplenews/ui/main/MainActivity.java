package com.robot.simplenews.ui.main;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.robot.simplenews.R;
import com.robot.simplenews.ui.about.AboutFragment;
import com.robot.simplenews.ui.base.BaseActivity;
import com.robot.simplenews.ui.images.ImageFragment;
import com.robot.simplenews.ui.news.AllNewsFragment;
import com.robot.simplenews.ui.setting.SettingFragment;
import com.robot.simplenews.ui.weather.WeatherFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class MainActivity extends BaseActivity implements MainContract.View {
    private static final String KEY_PARAM_POSITION_SELECTED = "positionSelected";
    private static final int DEFAULT_POSITION_SELECTED = R.id.navigation_item_news;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurPositionSelected = DEFAULT_POSITION_SELECTED;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent(mNavigationView);

        mMainPresenter = new MainPresenter(this);
        mMainPresenter.attachView(this);
        restoreSavedData(savedInstanceState);
        mMainPresenter.switchNavigation(mCurPositionSelected);
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurPositionSelected = savedInstanceState.getInt(KEY_PARAM_POSITION_SELECTED, DEFAULT_POSITION_SELECTED);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_PARAM_POSITION_SELECTED, mCurPositionSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mCurPositionSelected = menuItem.getItemId();
                        mMainPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void switch2News() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, AllNewsFragment.newInstance()).commit();
        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switch2Images() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, ImageFragment.newInstance()).commit();
        mToolbar.setTitle(R.string.navigation_images);
    }

    @Override
    public void switch2Weather() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, WeatherFragment.newInstance()).commit();
        mToolbar.setTitle(R.string.navigation_weather);
    }

    @Override
    public void switch2Setting() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, SettingFragment.newInstance()).commit();
        mToolbar.setTitle(R.string.navigation_setting);
    }

    @Override
    public void switch2About() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, AboutFragment.newInstance()).commit();
        mToolbar.setTitle(R.string.navigation_about);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }
}
