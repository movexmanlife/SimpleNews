package com.robot.simplenews.util;

import android.content.Context;
import android.widget.Toast;

import com.robot.simplenews.App;

/**
 */
public class ToastUtil {

    private ToastUtil() {
    }

    public static void show(int resId) {
        show(getContext().getResources().getString(resId));
    }

    public static void show(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resId) {
        showLong(getContext().getResources().getString(resId));
    }

    public static void showLong(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private static Context getContext() {
        return App.getInstance().getApplicationContext();
    }
}
