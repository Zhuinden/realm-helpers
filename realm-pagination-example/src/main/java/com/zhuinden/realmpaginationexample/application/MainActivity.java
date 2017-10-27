package com.zhuinden.realmpaginationexample.application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.zhuinden.realmpagination.RealmPaginationManager;
import com.zhuinden.realmpaginationexample.R;
import com.zhuinden.realmpaginationexample.features.tasks.TaskFragment;

public class MainActivity
        extends AppCompatActivity {
    ViewGroup container;
    RealmPaginationManager realmPaginationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        container = findViewById(R.id.container);
        realmPaginationManager = Injector.get().realmPaginationManager();
        if(savedInstanceState == null) { // TODO: navigation.
            getSupportFragmentManager() //
                    .beginTransaction() //
                    .add(R.id.container, new TaskFragment(), "TASK_FRAGMENT") //
                    .commit();
        }
        realmPaginationManager.open();
    }

    @Override
    protected void onDestroy() {
        realmPaginationManager.close();
        super.onDestroy();
    }
}
