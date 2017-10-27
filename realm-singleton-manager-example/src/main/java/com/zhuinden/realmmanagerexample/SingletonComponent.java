package com.zhuinden.realmmanagerexample;

import com.zhuinden.realmmanager.RealmManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Zhuinden on 2017.09.24..
 */

@Singleton
@Component(modules = RealmModule.class)
public interface SingletonComponent {
    RealmManager realmManager();
}
