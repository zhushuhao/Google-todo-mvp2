package com.d.dao.google_todo_mvp2.data.source;

import com.d.dao.google_todo_mvp2.data.Task;

import java.util.List;

/**
 * Created by dao on 11/10/16.
 */

public interface TasksDataSource {
    interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);//加载完成

        void onDataNotAvailable();
    }

    interface GetTaskCallback {
        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void getTasks(LoadTasksCallback callback);

    void getTask(String taskId, GetTaskCallback callback);

    void saveTask(Task task);

    void completeTask(Task task);

    void completeTask(String taskId);

    void activateTask(Task task);

    void activateTask(String taskId);

    void clearCompleteTasks();

    void refreshTasks();

    void deleteAllTasks();

    void deleteTask(String taskId);
}
