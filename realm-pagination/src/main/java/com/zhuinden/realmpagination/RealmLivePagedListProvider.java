/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhuinden.realmpagination;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListProvider;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Zhuinden on 2017.10.27..
 */

class RealmLivePagedListProvider<T extends RealmModel>
        extends LivePagedListProvider<Integer, T> {
    private RealmPaginationManager realmPaginationManager;
    private final RealmQueryDefinition<T> queryDefinition;

    public RealmLivePagedListProvider(RealmPaginationManager realmPaginationManager, RealmQueryDefinition<T> queryDefinition) {
        this.realmPaginationManager = realmPaginationManager;
        this.queryDefinition = queryDefinition;
    }

    @Override
    @NonNull
    protected DataSource<Integer, T> createDataSource() {
        return new RealmTiledDataSource<>(realmPaginationManager.getWorkerRealm(), queryDefinition);
    }

    @NonNull
    @Override
    public LiveData<PagedList<T>> create(@Nullable Integer initialLoadKey, PagedList.Config config) {
        return new RealmComputableLiveData<PagedList<T>>(realmPaginationManager) {
            @Nullable
            private PagedList<T> mList;
            @Nullable
            private DataSource<Integer, T> mDataSource;

            private RealmResults<T> realmResults;

            @SuppressWarnings("Convert2MethodRef")
            private final DataSource.InvalidatedCallback mCallback = () -> invalidate();

            private final RealmChangeListener<RealmResults<T>> mInvalidationTracker = (realmResults) -> {
                mCallback.onInvalidated();
            };

            @Override
            protected PagedList<T> compute() {
                realmResults = queryDefinition.createResults(realmPaginationManager.getWorkerRealm());
                @Nullable Integer initializeKey = initialLoadKey;
                if(mList != null) {
                    //noinspection unchecked
                    initializeKey = (Integer) mList.getLastKey();
                }

                do {
                    if(mDataSource != null) {
                        mDataSource.removeInvalidatedCallback(mCallback);
                        realmResults.removeChangeListener(mInvalidationTracker);
                    }

                    mDataSource = createDataSource();
                    realmResults.addChangeListener(mInvalidationTracker);
                    mDataSource.addInvalidatedCallback(mCallback);

                    mList = new PagedList.Builder<Integer, T>() //
                            .setDataSource(mDataSource) //
                            .setMainThreadExecutor(realmPaginationManager.getMainThreadExecutor())
                            .setBackgroundThreadExecutor(realmPaginationManager.getRealmQueryExecutor())
                            .setConfig(config)
                            .setInitialKey(initializeKey)
                            .build();
                } while(mList.isDetached());
                return mList;
            }
        }.getLiveData();
    }
}
