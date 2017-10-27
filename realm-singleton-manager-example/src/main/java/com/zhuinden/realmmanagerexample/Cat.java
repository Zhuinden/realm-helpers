package com.zhuinden.realmmanagerexample;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Zhuinden on 2017.09.24..
 */

public class Cat extends RealmObject {
    @Index
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
