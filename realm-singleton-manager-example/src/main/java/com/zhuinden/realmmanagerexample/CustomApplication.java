package com.zhuinden.realmmanagerexample;

import android.app.Application;
import android.support.annotation.NonNull;

import com.zhuinden.realmautomigration.RealmAutoMigration;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by Zhuinden on 2017.09.24..
 */

public class CustomApplication
        extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder() //
                .schemaVersion(11) //
                .migration((realm, oldVersion, newVersion) -> RealmAutoMigration.migrate(realm)) //
                .initialData(realm -> {
                    Cat cat = new Cat();
                    for(CatNames catName : CatNames.values()) {
                        cat.setName(catName.getName());
                        realm.insert(cat);
                    }
                }) //
                .build());
        SingletonComponent singletonComponent = DaggerSingletonComponent.create();
        Injector.setComponent(singletonComponent);
    }
}
