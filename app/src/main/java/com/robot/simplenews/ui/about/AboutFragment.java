package com.robot.simplenews.ui.about;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.robot.simplenews.ConstDef;
import com.robot.simplenews.R;
import com.robot.simplenews.ui.base.BaseFragment;
import com.robot.simplenews.util.IntentUtil;
import com.robot.simplenews.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
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
                        makePhoneCall(mCallTv);
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
     * @param view
     */
    public void makePhoneCall(View view) {
        PermissionGen.needPermission(AboutFragment.this, ConstDef.PERMISSION_REQUEST_CODE_CALL,
                new String[]{
                        Manifest.permission.CALL_PHONE,
                }
        );
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = ConstDef.PERMISSION_REQUEST_CODE_CALL)
    public void doCall(){
        IntentUtil.gotoCall(getActivity(), mCallTv.getText().toString());
    }

    /**
     * （1）当拒绝授权之后，会调用此方法；
     * （2）当弹出的权限提示的对话框中，选择不再提示，然后拒绝之后，还是会调用此方法。如果再次打电话，则不会弹出权限对话框，直接调用此方法；
     *
     * 结论：此方法要注意，最好要给一个提示。
     */
    @PermissionFail(requestCode = ConstDef.PERMISSION_REQUEST_CODE_CALL)
    public void doCallFail(){
        ToastUtil.show(R.string.permission_deny_call_msg);
    }
}
