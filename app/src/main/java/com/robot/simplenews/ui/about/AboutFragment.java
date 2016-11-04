package com.robot.simplenews.ui.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robot.simplenews.R;
import com.robot.simplenews.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于页面
 */
public class AboutFragment extends BaseFragment implements AboutContract.View {
    private static final String TAG = AboutFragment.class.getSimpleName();

    @BindView(R.id.tv_version)
    TextView mVersionTv;
    private AboutPresenter mAboutPresenter;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAboutPresenter = new AboutPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAboutPresenter.attachView(this);
        mAboutPresenter.getVersion();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAboutPresenter.detachView();
    }

    @Override
    public void showVersion(String version) {
        mVersionTv.setText(version);
    }
}
