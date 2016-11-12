package com.d.dao.google_todo_mvp2.tasks;

import com.d.dao.google_todo_mvp2.BasePresenter;
import com.d.dao.google_todo_mvp2.BaseView;
import com.d.dao.google_todo_mvp2.data.Task;

import java.util.List;

/**
 * Created by dao on 11/10/16.
 */

public interface TasksContract {

    interface View extends BaseView<Presenter> {

        //加载指示
        void setLoadingIndicator(boolean active);

        //
        boolean isActive();

        //加载task出错
        void showLoadTasksError();

        // 显示请求的tasks
        void showTasks(List<Task> tasks);

        //标签显示内容
        void showActiveFilterLabel();

        void showCompleteFilterLabel();

        void showAllFilterLabel();

        //请求数据为空
        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showNoTasks();

        void showCompletedTasksCleared();


        void showTaskDetailsUI(String id);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showAddTask();

        void showSuccessfullySavedMessage();

    }

    interface Presenter extends BasePresenter {

        void loadTasks(boolean forceUpdate);


        void setFilterType(TasksFilterType type);

        TasksFilterType getFilterType();

        void clearCompletedTasks();

        void openTaskDetails(Task task);

        void completeTask(Task task);

        void activateTask(Task task);

        void addNewTask();

        void result(int requestCode,int resultCode);
    }
}
