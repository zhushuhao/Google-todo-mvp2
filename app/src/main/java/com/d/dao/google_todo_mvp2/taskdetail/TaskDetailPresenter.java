package com.d.dao.google_todo_mvp2.taskdetail;

import android.text.TextUtils;

import com.d.dao.google_todo_mvp2.data.Task;
import com.d.dao.google_todo_mvp2.data.source.TasksDataSource;
import com.d.dao.google_todo_mvp2.data.source.TasksRepository;

import org.w3c.dom.Text;

/**
 * Created by dao on 12/11/2016.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private TasksRepository mTasksRepository;

    private TaskDetailContract.View mView;

    private String mTaskId;


    public TaskDetailPresenter(String taskId, TasksRepository repository,
                               TaskDetailContract.View view) {
        this.mTaskId = taskId;
        this.mTasksRepository = repository;
        this.mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void editTask() {
        checkIsEmptyTaskId();
        mView.showEditTask(mTaskId);
    }

    @Override
    public void deleteTask() {
        checkIsEmptyTaskId();

        mTasksRepository.deleteTask(mTaskId);
        mView.showTaskDeleted();

    }

    @Override
    public void completeTask() {
        checkIsEmptyTaskId();
        mTasksRepository.completeTask(mTaskId);
        mView.showTaskMarkedComplete();
    }

    private void checkIsEmptyTaskId() {
        if (TextUtils.isEmpty(mTaskId)) {
            mView.showMissingTask();
            return;
        }
    }

    @Override
    public void activateTask() {

        checkIsEmptyTaskId();

        mTasksRepository.activateTask(mTaskId);
        mView.showTaskMarkedActive();
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {

        checkIsEmptyTaskId();

        mView.setLoadingIndicator(true);

        mTasksRepository.getTask(mTaskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!mView.isActive()) {
                    return;
                }

                mView.setLoadingIndicator(false);
                if (task == null) {
                    mView.showMissingTask();
                } else {
                    showTask(task);
                }

            }

            @Override
            public void onDataNotAvailable() {

                if (!mView.isActive()) {
                    return;
                }
                mView.showMissingTask();
            }
        });
    }

    private void showTask(Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (TextUtils.isEmpty(title)) {
            mView.hideTitle();
        } else {
            mView.showTitle(title);
        }

        if (TextUtils.isEmpty(description)) {
            mView.hideDescription();
        } else {
            mView.showDescription(description);
        }

        mView.showCompletionStatus(task.isCompleted());
    }

}
