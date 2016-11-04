package com.robot.simplenews.ui.main;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.robot.simplenews.R;
import com.robot.simplenews.event.EventTag;
import com.robot.simplenews.event.RxBus;
import com.robot.simplenews.ui.about.AboutFragment;
import com.robot.simplenews.ui.base.BaseActivity;
import com.robot.simplenews.ui.base.BaseFragment;
import com.robot.simplenews.ui.images.ImageFragment;
import com.robot.simplenews.ui.news.AllNewsFragment;
import com.robot.simplenews.ui.setting.SettingFragment;
import com.robot.simplenews.ui.weather.WeatherFragment;
import com.robot.simplenews.util.LogUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 */
public class MainActivity extends BaseActivity implements MainContract.View {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String KEY_FRAGMENT_TAG = "fragment_tag";
    private static final String FRAGMENT_TAG_NEWS = "fragment_news";
    private static final String FRAGMENT_TAG_IMAGE = "fragment_image";
    private static final String FRAGMENT_TAG_WEATHER = "fragment_weather";
    private static final String FRAGMENT_TAG_SETTING = "fragment_setting";
    private static final String FRAGMENT_TAG_ABOUT = "fragment_about";

    private String[] mFragmentTags = new String[]{FRAGMENT_TAG_NEWS, FRAGMENT_TAG_IMAGE, FRAGMENT_TAG_WEATHER, FRAGMENT_TAG_SETTING, FRAGMENT_TAG_ABOUT};
    private int[] mNavigationIds = new int[]{R.id.navigation_item_news, R.id.navigation_item_images, R.id.navigation_item_weather, R.id.navigation_item_setting, R.id.navigation_item_about};
    private Map<String, Integer> mTagMap = new HashMap<>();
    private String mFragmentCurrentTag = FRAGMENT_TAG_NEWS;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private MainPresenter mMainPresenter;
    private Observable<Boolean> mNightModeObservable;
    private BaseFragment mAllNewsFragment;
    private BaseFragment mImageFragment;
    private BaseFragment mWeatherFragment;
    private BaseFragment mSettingFragment;
    private BaseFragment mAboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();

        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerContent(mNavigationView);

        mMainPresenter = new MainPresenter(this);
        mMainPresenter.attachView(this);
        restoreSavedData(savedInstanceState);

        Integer selectedNavigationId = mTagMap.get(mFragmentCurrentTag);
        if (selectedNavigationId != null) {
            mMainPresenter.switchNavigation(selectedNavigationId);
        } else {
            mMainPresenter.switchNavigation(R.id.navigation_item_news);
        }

        mNightModeObservable = RxBus.get().register(EventTag.NIGHT_MODE, Boolean.class);
        mNightModeObservable.subscribeOn(Schedulers.newThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isNight) {
                LogUtil.e(TAG, "Night mode:" + isNight);
                LogUtil.e(TAG, Thread.currentThread().getName());
            }
        });
    }

    private void initData() {
        for (int i = 0; i < mFragmentTags.length; i++) {
            mTagMap.put(mFragmentTags[i], mNavigationIds[i]);
        }
    }

    private void restoreSavedData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreFragments();
            mFragmentCurrentTag =  savedInstanceState.getString(KEY_FRAGMENT_TAG);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FRAGMENT_TAG, mFragmentCurrentTag);
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
                        mMainPresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void switch2News(int id) {
        switchFragment(id);
        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switch2Images(int id) {
        switchFragment(id);
        mToolbar.setTitle(R.string.navigation_images);
    }

    @Override
    public void switch2Weather(int id) {
        switchFragment(id);
        mToolbar.setTitle(R.string.navigation_weather);
    }

    @Override
    public void switch2Setting(int id) {
        switchFragment(id);
        mToolbar.setTitle(R.string.navigation_setting);
    }

    @Override
    public void switch2About(int id) {
        switchFragment(id);
        mToolbar.setTitle(R.string.navigation_about);
    }

    /**
     * 切换不同的Fragment
     */
    public void switchFragment(int id) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        hideFragments(manager, trans);

        if (id == R.id.navigation_item_news) {
            mAllNewsFragment = selectedFragment(trans, mAllNewsFragment, AllNewsFragment.class, FRAGMENT_TAG_NEWS);
        } else if (id == R.id.navigation_item_images) {
            mImageFragment = selectedFragment(trans, mImageFragment, ImageFragment.class, FRAGMENT_TAG_IMAGE);
        } else if (id == R.id.navigation_item_weather) {
            mWeatherFragment = selectedFragment(trans, mWeatherFragment, WeatherFragment.class, FRAGMENT_TAG_WEATHER);
        } else if (id == R.id.navigation_item_setting) {
            mSettingFragment = selectedFragment(trans, mSettingFragment, SettingFragment.class, FRAGMENT_TAG_SETTING);
        } else if (id == R.id.navigation_item_about) {
            mAboutFragment = selectedFragment(trans, mAboutFragment, AboutFragment.class, FRAGMENT_TAG_ABOUT);
        }
        trans.commit();
    }

    private BaseFragment selectedFragment(FragmentTransaction transaction, BaseFragment fragment, Class<?> clazz, String tag) {
        mFragmentCurrentTag = tag;
        if (fragment == null) {
            try {
                Method newInstanceMethod = clazz.getDeclaredMethod("newInstance");;
                fragment = (BaseFragment)(newInstanceMethod.invoke(null));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            transaction.add(R.id.frame_content, fragment, tag);
        }
        transaction.show(fragment);
        return fragment;
    }

    /**
     * 先全部隐藏
     * @param fragmentManager
     * @param transaction
     */
    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < mFragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(mFragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    /**
     * 恢复fragment
     */
    private void restoreFragments() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for (int i = 0; i < mFragmentTags.length; i++) {
            String tag = mFragmentTags[i];
            BaseFragment fragment = (BaseFragment)manager.findFragmentByTag(tag);
            if (TextUtils.equals(FRAGMENT_TAG_NEWS, tag)) {
                mAllNewsFragment = fragment;
            } else if (TextUtils.equals(FRAGMENT_TAG_IMAGE, tag)) {
                mImageFragment = fragment;
            } else if (TextUtils.equals(FRAGMENT_TAG_WEATHER, tag)) {
                mWeatherFragment = fragment;
            } else if (TextUtils.equals(FRAGMENT_TAG_SETTING, tag)) {
                mSettingFragment = fragment;
            } else if (TextUtils.equals(FRAGMENT_TAG_ABOUT, tag)) {
                mAboutFragment = fragment;
            }
            if (fragment == null) {
                continue;
            }
            transaction.hide(fragment);
        }
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(EventTag.NIGHT_MODE, mNightModeObservable);
        mMainPresenter.detachView();
    }
}
