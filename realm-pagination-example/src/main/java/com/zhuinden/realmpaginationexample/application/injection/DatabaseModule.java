package com.zhuinden.realmpaginationexample.application.injection;

import com.zhuinden.realmpagination.RealmPaginationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Owner on 2017. 10. 09..
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    RealmPaginationManager realmPaginationManager() {
        return new RealmPaginationManager();
    }
}
