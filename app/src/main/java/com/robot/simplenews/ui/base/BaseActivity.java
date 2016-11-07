package com.robot.simplenews.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.robot.simplenews.ConstDef;
import com.robot.simplenews.util.SharedPreferencesUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 *
 */
public class BaseActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isNight = SharedPreferencesUtil.getBoolean(this, ConstDef.IS_NIGHT, false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        super.onCreate(savedInstanceState);
    }
}
