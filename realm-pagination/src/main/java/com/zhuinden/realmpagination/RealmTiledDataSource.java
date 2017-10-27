/*
 * Copyright (C) 2017 Gabor Varadi
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

import android.arch.paging.TiledDataSource;
import android.support.annotation.WorkerThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by Zhuinden on 2017.10.27..
 */

class RealmTiledDataSource<T extends RealmModel> extends TiledDataSource<T> {
    private final Realm workerRealm;
    private final RealmResults<T> liveResults;

    private final RealmChangeListener<RealmResults<T>> realmChangeListener = results -> {
        invalidate();
    };

    // WORKER THREAD
    public RealmTiledDataSource(Realm workerRealm, RealmQueryDefinition<T> queryDefinition) {
        this.workerRealm = workerRealm;
        this.liveResults = queryDefinition.createResults(workerRealm);
        if(!liveResults.isLoaded()) {
            liveResults.load(); // unavoidable
        }
        this.liveResults.addChangeListener(realmChangeListener);
    }

    @Override
    @WorkerThread
    public int countItems() {
        if(workerRealm.isClosed() || !liveResults.isValid()) {
            return 0;
        }
        return liveResults.size();
    }

    @Override
    @WorkerThread
    public List<T> loadRange(int startPosition, int count) {
        int countItems = countItems();
        if(countItems == 0) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>(count);
        for(int i = startPosition; i < startPosition + count && i < countItems; i++) {
            // noinspection ConstantConditions
            list.add(workerRealm.copyFromRealm(liveResults.get(i)));
        }
        return Collections.unmodifiableList(list);
    }
}
