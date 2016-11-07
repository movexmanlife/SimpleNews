package com.robot.simplenews.ui.about;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.robot.simplenews.R;
import com.robot.simplenews.ui.base.BaseFragment;
import com.robot.simplenews.util.IntentUtil;
import com.robot.simplenews.util.ToastUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 关于页面
 */
public class AboutFragment extends BaseFragment implements AboutContract.View {
    private static final String TAG = AboutFragment.class.getSimpleName();

    @BindView(R.id.tv_version)
    TextView mVersionTv;
    @BindView(R.id.tv_call)
    TextView mCallTv;
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

        /**
         * 防止抖动
         */
        RxView.clicks(mCallTv)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        makePhoneCall();
                    }
                });
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

    /**
     * 拨打电话
     */
    public void makePhoneCall() {
        RxPermissions.getInstance(getContext())
                .requestEach(Manifest.permission.CALL_PHONE)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            doCallSuccess();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 权限拒绝，并且会弹出“权限提示对话框”
                            doCallFail();
                        } else {
                            // 权限拒绝，并且不会弹出“权限提示对话框”
                            doCallFail();
                        }

                    }
                });
    }

    public void doCallSuccess() {
        IntentUtil.gotoCall(getActivity(), mCallTv.getText().toString());
    }

    public void doCallFail() {
        ToastUtil.show(R.string.permission_deny_call_msg);
    }
}
