package com.robot.simplenews.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robot.simplenews.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class AllNewsFragment extends Fragment {
    public static final int NEWS_TYPE_TOP = 0;
    public static final int NEWS_TYPE_NBA = 1;
    public static final int NEWS_TYPE_CARS = 2;
    public static final int NEWS_TYPE_JOKES = 3;

    @BindView(R.id.tab_layout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private Map<Integer, String> sMap = new HashMap<>();

    public static AllNewsFragment newInstance() {
        Bundle args = new Bundle();
        AllNewsFragment fragment = new AllNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sMap.put(NEWS_TYPE_TOP, getString(R.string.top));
        sMap.put(NEWS_TYPE_NBA, getString(R.string.nba));
        sMap.put(NEWS_TYPE_CARS, getString(R.string.cars));
        sMap.put(NEWS_TYPE_JOKES, getString(R.string.jokes));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        ButterKnife.bind(this, view);

        mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);

        for (int i = 0; i < sMap.size(); i++) {
            mTablayout.addTab(mTablayout.newTab().setText(sMap.get(i)));
        }
        mTablayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void setupViewPager(ViewPager mViewPager) {
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < sMap.size(); i++) {
            adapter.addFragment(NewsListFragment.newInstance(i), sMap.get(i));
        }
        mViewPager.setAdapter(adapter);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
