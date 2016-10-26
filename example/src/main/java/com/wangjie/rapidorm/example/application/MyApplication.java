package com.wangjie.rapidorm.example.application;

import android.app.Application;

import com.wangjie.rapidorm.constants.RapidORMConfig;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RapidORMConfig.DEBUG = true;
    }
}
