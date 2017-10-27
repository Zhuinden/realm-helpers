package com.zhuinden.realmpaginationexample.application.injection;

import android.app.Application;

import com.zhuinden.realmpaginationexample.application.CustomApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Module
public class AppModule {
    @Provides
    Application application(CustomApplication customApplication) {
        return customApplication;
    }
}
