package com.d.dao.google_todo_mvp2.tasks;

import android.app.Activity;

import com.d.dao.google_todo_mvp2.addtask.AddTaskActivity;
import com.d.dao.google_todo_mvp2.data.Task;
import com.d.dao.google_todo_mvp2.data.source.TasksDataSource;
import com.d.dao.google_todo_mvp2.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dao on 11/10/16.
 */

public class TasksPresenter implements TasksContract.Presenter {

    private boolean mFirstLoad = true;

    private TasksRepository mTasksRepository;

    private TasksContract.View mView;


    private TasksFilterType mFilterType = TasksFilterType.ALL_TASKS;

    public TasksPresenter(TasksRepository tasksRepository, TasksContract.View view) {
        this.mTasksRepository = tasksRepository;
        this.mView = view;
        mView.setPresenter(this);

    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void loadTasks(boolean forceUpdate) {

        loadTasks(forceUpdate || mFirstLoad, true);
        if (mFirstLoad) {
            mFirstLoad = false;
        }
    }

    @Override
    public void setFilterType(TasksFilterType type) {
        this.mFilterType = type;
    }

    @Override
    public TasksFilterType getFilterType() {
        return mFilterType;
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRepository.clearCompleteTasks();
        mView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public void openTaskDetails(Task task) {

        mView.showTaskDetailsUI(task.getId());
    }

    @Override
    public void completeTask(Task task) {

        mTasksRepository.completeTask(task);
        mView.showTaskMarkedComplete();

        loadTasks(false, false);
    }

    @Override
    public void activateTask(Task task) {

        mTasksRepository.activateTask(task);
        mView.showTaskMarkedActive();

        loadTasks(false, false);
    }

    @Override
    public void addNewTask() {
        mView.showAddTask();
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (AddTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mView.showSuccessfullySavedMessage();
        }
    }


    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mView.setLoadingIndicator(true);
        }

        // 是否强制刷新，是，则再次请求数据时直接从服务期请求数据
        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }

        //获取所有tasks
        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                for (Task task : tasks) {
                    switch (mFilterType) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                    }
                }

                if (!mView.isActive()) {
                    return;
                }

                if (showLoadingUI) {
                    mView.setLoadingIndicator(false);
                }


                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {

                if (!mView.isActive()) {
                    return;
                }
                mView.showLoadTasksError();
            }
        });
    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            processEmptyTasks();
        } else {
            mView.showTasks(tasks);
            showFilterLabel();
        }
    }

    private void showFilterLabel() {
        switch (mFilterType) {
            case ACTIVE_TASKS:
                mView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mView.showCompleteFilterLabel();
                break;
            default:
                mView.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTasks() {
        switch (mFilterType) {
            case ACTIVE_TASKS:
                mView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mView.showNoCompletedTasks();
                break;
            default:
                mView.showNoTasks();
                break;
        }
    }


}
