package com.robot.simplenews.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.kyleduo.switchbutton.SwitchButton;
import com.robot.simplenews.ConstDef;
import com.robot.simplenews.R;
import com.robot.simplenews.event.EventTag;
import com.robot.simplenews.event.RxBus;
import com.robot.simplenews.ui.base.BaseFragment;
import com.robot.simplenews.util.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置页面
 */
public class SettingFragment extends BaseFragment implements SettingContract.View {
    private static final String TAG = SettingFragment.class.getSimpleName();

    @BindView(R.id.switchBtn)
    SwitchButton mSwitchBtn;
    private SettingPresenter mSettingPresenter;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingPresenter = new SettingPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        ButterKnife.bind(this, view);
        final boolean isNight = SharedPreferencesUtil.getBoolean(getContext(), ConstDef.IS_NIGHT, false);
        mSwitchBtn.setChecked(isNight);

        mSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showNight(isChecked);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSettingPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSettingPresenter.detachView();
    }

    @Override
    public void showNight(boolean isNight) {
        RxBus.get().post(EventTag.NIGHT_MODE, isNight);
        if (isNight) {
            SharedPreferencesUtil.setBoolean(getContext(), ConstDef.IS_NIGHT, true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getActivity().recreate();
        } else {
            SharedPreferencesUtil.setBoolean(getContext(), ConstDef.IS_NIGHT, false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getActivity().recreate();
        }
    }
}
