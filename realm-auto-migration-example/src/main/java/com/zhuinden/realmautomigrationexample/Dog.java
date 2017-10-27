package com.zhuinden.realmautomigrationexample;

import com.zhuinden.realmautomigration.RealmAutoMigration;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.Required;

/**
 * Created by Owner on 2017. 09. 28..
 */
public class Dog
        extends RealmObject {
    @Index
    private String name;

    @Required
    @Index
    private String ownerName;

    private Cat cat;

    @RealmAutoMigration.MigratedList(listType = Cat.class)
    private RealmList<Cat> manyCats;

    @RealmAutoMigration.MigratedList(listType = String.class)
    @Required
    private RealmList<String> phoneNumbers;
}
