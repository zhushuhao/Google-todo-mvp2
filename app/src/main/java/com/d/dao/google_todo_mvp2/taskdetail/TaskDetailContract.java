package com.d.dao.google_todo_mvp2.taskdetail;

import com.d.dao.google_todo_mvp2.BasePresenter;
import com.d.dao.google_todo_mvp2.BaseView;

/**
 * Created by dao on 12/11/2016.
 */

public interface TaskDetailContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean completed);

        void showEditTask(String taskId);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();

        void showTaskDeleted();

    }

    interface Presenter extends BasePresenter{

        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();

    }

}
