package com.zhuinden.realmpaginationexample.features.tasks;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuinden.realmpaginationexample.R;
import com.zhuinden.realmpaginationexample.data.entity.Task;

/**
 * Created by Owner on 2017. 10. 09..
 */

public class TaskAdapter
        extends PagedListAdapter<Task, TaskAdapter.ViewHolder> {
    public TaskAdapter() {
        super(Task.DIFF_CALLBACK);
    }

//    protected TaskAdapter(@NonNull ListAdapterConfig<Task> config) {
//        super(config);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task task = getItem(position);
        if(task != null) {
            holder.bind(task);
        }
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.task_text);
        }

        public void bind(@NonNull Task item) {
            text.setText(item.getText());
        }
    }
}
