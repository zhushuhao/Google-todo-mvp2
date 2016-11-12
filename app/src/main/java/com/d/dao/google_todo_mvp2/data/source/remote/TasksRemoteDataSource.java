package com.d.dao.google_todo_mvp2.data.source.remote;

import android.os.Handler;

import com.d.dao.google_todo_mvp2.data.Task;
import com.d.dao.google_todo_mvp2.data.source.TasksDataSource;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dao on 11/10/16.
 * 查询服务器数据
 */

public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource mInstance;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Task> TASKS_SERVICE_DATA;

    static {
//        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
//        addTask("Build tower in Pisa", "Ground looks good,no foundation work required");
//        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost");

        TASKS_SERVICE_DATA = new LinkedHashMap<>(0);
//        addTask("Build tower in Pisa", "Ground looks good,no foundation work required");
//        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost");
    }


    private static void addTask(String title, String description) {
        Task task = new Task(title, description);
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    public static TasksRemoteDataSource getInstance() {
        if (mInstance == null) {
            mInstance = new TasksRemoteDataSource();
        }
        return mInstance;
    }

    private TasksRemoteDataSource() {
    }


    @Override
    public void getTasks(final LoadTasksCallback callback) {

        Handler handler = new Handler();

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));
//            }
//        }, SERVICE_LATENCY_IN_MILLIS);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values()));

            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getTask(String taskId, final GetTaskCallback callback) {

        final Task task = TASKS_SERVICE_DATA.get(taskId);
        //// TODO: 11/10/16 是否需要检测为空

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onTaskLoaded(task);
            }
        }, SERVICE_LATENCY_IN_MILLIS);

    }

    @Override
    public void saveTask(Task task) {
        TASKS_SERVICE_DATA.put(task.getId(), task);

    }

    @Override
    public void completeTask(Task task) {


        Task t = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(task.getId(), t);// key已经存在，替换value
    }

    @Override
    public void completeTask(String taskId) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void activateTask(Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId(), false);
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);

    }

    @Override
    public void activateTask(String taskId) {
        // Not required for the remote data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void clearCompleteTasks() {

        Iterator<Map.Entry<String, Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllTasks() {

        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(String taskId) {

        TASKS_SERVICE_DATA.remove(taskId);

    }
}
