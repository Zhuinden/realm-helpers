# Realm Helpers

A collection of helpers that are still all in an early stage, but some people could consider them helpful.

As they are alpha, they are not exposed yet on Jitpack.

## Realm Auto-Migration

Automatic migration from the currently existing schema to the currently existing model classes.

By calling `RealmAutoMigration.migrate(dynamicRealm)`, the schema is matched between the file and the models. 

The library relies on reflection.

It is recommended to apply `@MigratedList` on every `RealmList<T>` field if using auto-migration.

### Proguard

```
-keepnames public class * extends io.realm.RealmModel
-keep public class * extends io.realm.RealmModel { *; }
-keepattributes *Annotation*
```

### Behavior

It attempts to migrate the Realm schema from one version to the current models provided in the configuration.

In case of mismatch, fields defined only in schema but not in model are removed, and fields defined only in model but not in schema are added.

### RealmList fields

To add `RealmList` field, you must specify {@link MigratedList} on that field with the list type.

This properly supports both links and primitive lists. 

`@AutoMigration.MigratedList` must be applied to detect changes in `RealmList<Primitive>`'s `@Required` annotation.

### Example

``` java
public class Dog
        extends RealmObject {
    @PrimaryKey
    private long id;

    @Index
    private String name;

    @Required
    private String ownerName;

    private Cat cat;

    @AutoMigration.MigratedList(listType = Cat.class)
    private RealmList<Cat> manyCats;
    
    @AutoMigration.MigratedList(listType = String.class)
    private RealmList<String> phoneNumbers;
}
```

## Realm Pagination

The `RealmPaginationManager` is expected to be a singleton, and `open()` must be called at least once (reference counted).

Afterwards, `realmPaginationManager.createLivePagedListProvider()` allows creating a `LivePagedListProvider<T>`, which can provide `LiveData<PagedList<T>>` from the RealmResults.

The queries are executed on a background looper thread. The returned elements in the adapter are loaded to memory via `realm.copyFromRealm()`.

The created `RealmResults<T>` should be obtained synchronously, and NOT with `find*Async()` method.

## Realm Manager

The `RealmManager` class allows creating a singleton "Realm manager" class, which can provide the thread-local instance without incrementing the reference counter in Realm's cache.

- `openLocalInstance()` opens a new instance, incrementing the reference count.

- `getLocalInstance()` returns the currently open instance, without incrementing the reference count.

- `closeLocalInstance()` closes the instance, decrementing the reference count.


## License

    Copyright 2017 Gabor Varadi

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
