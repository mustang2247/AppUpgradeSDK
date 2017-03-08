package com.konka.appupgradedemo;

import android.app.Application;

import com.konka.appupgrade.AppUpgradeSDK;


/**
 * Created by suqishuo on 2016/12/27.
 */

public class App extends Application {
    private static final String DEFAULT_LOG_TAG = "AppUpgradeDemo";

    @Override
    public void onCreate() {
        super.onCreate();
        /*
        if (BuildConfig.DEBUG) {
            Logger.init(DEFAULT_LOG_TAG).logLevel(LogLevel.FULL).methodCount(3);
        } else {
            Logger.init(DEFAULT_LOG_TAG).logLevel(LogLevel.NONE);
        }

        Log.d("sqs_test", "APP CONTEXT " + getApplicationContext().hashCode());*/

        AppUpgradeSDK.init(getApplicationContext());

    }

}
