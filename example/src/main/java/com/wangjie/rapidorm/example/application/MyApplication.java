package com.wangjie.rapidorm.example.application;

import com.wangjie.androidbucket.application.ABApplication;
import com.wangjie.rapidorm.constants.RapidORMConfig;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class MyApplication extends ABApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        RapidORMConfig.DEBUG = true;
    }
}
