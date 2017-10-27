package com.zhuinden.realmautomigrationexample;

/**
 * Created by Zhuinden on 2017.09.24..
 */

public enum Injector {
    INSTANCE;

    private SingletonComponent singletonComponent;

    static void setComponent(SingletonComponent singletonComponent) {
        INSTANCE.singletonComponent = singletonComponent;
    }

    public static SingletonComponent get() {
        return INSTANCE.singletonComponent;
    }
}
