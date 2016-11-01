package com.robot.simplenews;

import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 */
public class App extends Application {

    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        Logger.init(getString(R.string.app_name))
                .methodCount(3)
                .hideThreadInfo()
                .logLevel(LogLevel.NONE)
                .methodOffset(2);
    }

    public static App getInstance() {
        return mApp;
    }
}
