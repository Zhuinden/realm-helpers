package com.zhuinden.realmmanagerexample;

import com.zhuinden.realmmanager.RealmManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Zhuinden on 2017.09.24..
 */

@Module
public class RealmModule {
    @Provides
    @Singleton
    RealmManager realmManager() {
        return new RealmManager(); // @Inject constructors are nicer q.q
    }
}
