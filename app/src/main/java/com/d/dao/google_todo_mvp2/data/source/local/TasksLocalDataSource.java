package com.d.dao.google_todo_mvp2.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.d.dao.google_todo_mvp2.data.Task;
import com.d.dao.google_todo_mvp2.data.source.TasksDataSource;
import com.d.dao.google_todo_mvp2.data.source.local.TasksPersistenceContract.TaskEntry;


import java.util.ArrayList;
import java.util.List;

/**
 * 从本地数据库查询数据
 * Created by dao on 11/10/16.
 */

public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource mInstance;

    private TasksDbHelper mDbHelper;

    public TasksLocalDataSource(Context context) {
        mDbHelper = new TasksDbHelper(context);
    }


    public static TasksLocalDataSource getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TasksLocalDataSource(context);
        }
        return mInstance;
    }

    @Override
    public void getTasks(LoadTasksCallback callback) {

        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED};

        Task task = null;
        Cursor cursor = db.query(TaskEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String itemId = cursor.getString(
                        cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                task = new Task(title, description, itemId, completed);
                tasks.add(task);
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(tasks);
        }
    }

    @Override
    public void getTask(String taskId, GetTaskCallback callback) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED};

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";

        String[] selectionArgs = {taskId};

        Cursor c = db.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs,
                null, null, null);

        Task task = null;
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
            String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
            String description = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed = c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
            task = new Task(title, description, itemId, completed);
        }

        if (c != null) {
            c.close();
        }
        db.close();

        if (task != null) {
            callback.onTaskLoaded(task);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveTask(Task task) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        cv.put(TaskEntry.COLUMN_NAME_TITLE, task.getDescription());
        cv.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        cv.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        db.insert(TaskEntry.TABLE_NAME, null, cv);

        db.close();
    }

    @Override
    public void completeTask(Task task) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        db.update(TaskEntry.TABLE_NAME, cv, selection, selectionArgs);
        db.close();
    }

    @Override
    public void completeTask(String taskId) {
        // Not required for the local data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }


    //激活任务，设置未完成
    @Override
    public void activateTask(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


        ContentValues cv = new ContentValues();

        cv.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        db.update(TaskEntry.TABLE_NAME, cv, selection, selectionArgs);

        db.close();
    }

    @Override
    public void activateTask(String taskId) {

        // Not required for the local data source because the {@link TasksRepository} handles
        // converting from a {@code taskId} to a {@link task} using its cached data.
    }

    @Override
    public void clearCompleteTasks() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";

        String[] selectionArgs = {"1"};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void refreshTasks() {

        //todo: what does this mean?
        // Not required because the {@link TasksRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllTasks() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();

    }

    @Override
    public void deleteTask(String taskId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};
        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
