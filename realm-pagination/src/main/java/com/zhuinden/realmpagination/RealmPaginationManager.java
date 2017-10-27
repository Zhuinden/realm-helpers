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

import android.arch.paging.LivePagedListProvider;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.Realm;
import io.realm.RealmModel;

/**
 * Created by Zhuinden on 2017.10.27..
 */

public class RealmPaginationManager {
    private final Executor mainThreadExecutor = new RealmMainThreadExecutor();
    private AtomicReference<HandlerThread> handlerThread = new AtomicReference<>();
    private AtomicReference<Handler> handler = new AtomicReference<>();
    private AtomicReference<RealmQueryExecutor> queryExecutor = new AtomicReference<>();
    private AtomicInteger openCount = new AtomicInteger();
    private AtomicReference<Realm> workerRealm = new AtomicReference<>();

    public void open() {
        if(openCount.getAndIncrement() == 0) {
            HandlerThread handlerThread = new HandlerThread("REALM_PAGINATION_THREAD[" + hashCode() + "]");
            this.handlerThread.set(handlerThread);
            //noinspection SynchronizationOnLocalVariableOrMethodParameter
            synchronized(handlerThread) {
                handlerThread.start();
                try {
                    handlerThread.wait();
                } catch(InterruptedException e) {
                    // Ignored
                }
            }
            Handler handler = new Handler(handlerThread.getLooper());
            this.handler.set(handler);
            this.queryExecutor.set(new RealmQueryExecutor(this, handler));
            handler.post(() -> {
                workerRealm.set(Realm.getDefaultInstance()); // TODO: Support other configurations.
            });
        }
    }

    public void close() {
        if(openCount.decrementAndGet() == 0) {
            handler.get().post(() -> {
                workerRealm.get().close();
                workerRealm.set(null);
                handlerThread.get().quit();
                handlerThread.set(null);
                handler.set(null);
            });
        }
    }

    Realm getWorkerRealm() {
        return workerRealm.get();
    }

    Executor getMainThreadExecutor() {
        return mainThreadExecutor;
    }

    public <T extends RealmModel> LivePagedListProvider<Integer, T> createLivePagedListProvider(RealmQueryDefinition<T> queryDefinition) {
        if(!isHandlerThreadOpen()) {
            throw new IllegalStateException("The pagination manager must be opened before a live list is created from it.");
        }
        return new RealmLivePagedListProvider<>(this, queryDefinition);
    }

    RealmQueryExecutor getRealmQueryExecutor() {
        return queryExecutor.get();
    }

    boolean isHandlerThreadOpen() {
        return handlerThread.get() != null;
    }
}
