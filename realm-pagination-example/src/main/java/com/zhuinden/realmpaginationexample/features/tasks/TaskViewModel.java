package com.zhuinden.realmpaginationexample.features.tasks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import com.zhuinden.realmpaginationexample.application.Injector;
import com.zhuinden.realmpaginationexample.data.dao.TaskDao;
import com.zhuinden.realmpaginationexample.data.entity.Task;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class TaskViewModel
        extends ViewModel {
    private final TaskDao taskDao;

    private LiveData<PagedList<Task>> liveResults;

    public TaskViewModel() {
        taskDao = Injector.get().taskDao(); // should be provided by ViewModelProviders.Factory
        liveResults = taskDao.tasksSortedByDate().create(0, new PagedList.Config.Builder() //
                .setPageSize(20) //
                .setPrefetchDistance(20) //
                .setEnablePlaceholders(true) //
                .build());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public LiveData<PagedList<Task>> getTasks() {
        return liveResults;
    }
}
