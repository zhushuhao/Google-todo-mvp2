package com.d.dao.google_todo_mvp2.data.source;

import com.d.dao.google_todo_mvp2.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation to load tasks from the data sources into a cache.
 * Created by dao on 11/10/16.
 */

public class TasksRepository implements TasksDataSource {

    private static TasksRepository mInstance = null;


    private TasksDataSource mTasksRemoteDataSource;
    private TasksDataSource mTaskLoalDataSource;


    private Map<String, Task> mCacheTasks = new LinkedHashMap<>();

    private boolean mCacheIsDirty = false;

    private TasksRepository() {
    }


    private TasksRepository(TasksDataSource remote, TasksDataSource local) {
        mTaskLoalDataSource = local;
        mTasksRemoteDataSource = remote;
    }

    public static TasksRepository getInstance(TasksDataSource remote, TasksDataSource local) {
        if (mInstance == null) {
            mInstance = new TasksRepository(remote, local);
        }
        return mInstance;
    }


    @Override
    public void getTasks(final LoadTasksCallback callback) {

        //如果当前的内存缓存可用而且内存缓存有内容,直接返回内存中缓存的数据就可以了
        if (mCacheTasks.size() > 0 && !mCacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(mCacheTasks.values()));
            return;
        }

        //  走到这时
        //服务器端的当前的信息（内存缓存与本地数据库都不可以用了）发生了变化不再可用了，与最新的信息已经不一样了,需要从服务器请求
        if (mCacheIsDirty) {
            getTasksFromRemoteDataSource(callback);//从服务器获取
        } else {//服务器端的信息与本地数据库信息相同，但是内存没有缓存数据
            // !mCacheIsDirty && (mCacheTasks.size==0)
            //去本地数据库获取信息即可
            getTasksFromLocalDataSource(callback);
        }
    }

    private void getTasksFromLocalDataSource(final LoadTasksCallback callback) {
        mTaskLoalDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {//从本地数据库获取成功
                refreshCache(tasks);//更新内存数据
                callback.onTasksLoaded(new ArrayList<>(mCacheTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                getTasksFromRemoteDataSource(callback);
            }
        });
    }

    private void getTasksFromRemoteDataSource(final LoadTasksCallback callback) {

        mTasksRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);//刷新内存缓存列表
                refreshLocalDataSource(tasks);//刷新本地数据库
                callback.onTasksLoaded(new ArrayList<>(mCacheTasks.values()));
            }
            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });

    }

    //更新缓存列表
    private void refreshCache(List<Task> tasks) {
        mCacheTasks.clear();
        for (Task task : tasks) {
            mCacheTasks.put(task.getId(), task);
        }
        if (mCacheIsDirty) {
            mCacheIsDirty = false;
        }
    }

    //刷新数据库缓存
    private void refreshLocalDataSource(List<Task> tasks) {
        mTaskLoalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTaskLoalDataSource.saveTask(task);
        }
    }


    private void getTaskFromRemoteDataSource(String taskId, final GetTaskCallback callback){
        mTaskLoalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                //服务器请求到了
                //缓存到列表
                mCacheTasks.put(task.getId(), task);
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();

            }
        });
    }
    private void getTaskFromLocalDataSource(final String taskId, final GetTaskCallback callback){
        //从本地数据库查询
        mTaskLoalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                //查询到之后，缓存到列表中
                mCacheTasks.put(task.getId(), task);
                callback.onTaskLoaded(task);
            }
            @Override
            public void onDataNotAvailable() {
                //本地数据库不存在,
                //去服务器请求
                getTaskFromLocalDataSource(taskId,callback);

            }
        });

    }
    @Override
    public void getTask(final String taskId, final GetTaskCallback callback) {

        Task cachedTask = getTaskWithId(taskId);

        //缓存的数组列表有
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }
        getTaskFromLocalDataSource(taskId,callback);



    }


    private Task getTaskWithId(String taskId) {
        if (mCacheTasks == null || mCacheTasks.isEmpty()) {
            return null;
        } else {
            return mCacheTasks.get(taskId);
        }

    }

    @Override
    public void saveTask(Task task) {
        mTasksRemoteDataSource.saveTask(task);
        mTaskLoalDataSource.saveTask(task);


        mCacheTasks.put(task.getId(), task);

    }

    @Override
    public void completeTask(Task task) {
        mTasksRemoteDataSource.completeTask(task);
        mTaskLoalDataSource.completeTask(task);

        Task completeTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);


        mCacheTasks.put(task.getId(), completeTask);


    }

    @Override
    public void completeTask(String taskId) {

        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(Task task) {

        mTaskLoalDataSource.activateTask(task);
        mTasksRemoteDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId(), false);


        mCacheTasks.put(task.getId(), activeTask);

    }

    @Override
    public void activateTask(String taskId) {

        activateTask(getTaskWithId(taskId));

    }

    @Override
    public void clearCompleteTasks() {

        mTasksRemoteDataSource.clearCompleteTasks();
        mTaskLoalDataSource.clearCompleteTasks();


        if(mCacheTasks!=null && mCacheTasks.size()>0){
            Iterator<Map.Entry<String, Task>> it = mCacheTasks.entrySet().iterator();

            Map.Entry<String, Task> entry = it.next();

            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }


    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {

        mTasksRemoteDataSource.deleteAllTasks();
        mTaskLoalDataSource.deleteAllTasks();

        mCacheTasks.clear();

    }

    @Override
    public void deleteTask(String taskId) {

        mTaskLoalDataSource.deleteTask(taskId);
        mTasksRemoteDataSource.deleteTask(taskId);
        mCacheTasks.remove(taskId);
    }
}
