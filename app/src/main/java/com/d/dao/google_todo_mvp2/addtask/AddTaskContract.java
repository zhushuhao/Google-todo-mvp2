package com.d.dao.google_todo_mvp2.addtask;

import com.d.dao.google_todo_mvp2.BasePresenter;
import com.d.dao.google_todo_mvp2.BaseView;

/**
 * Created by dao on 12/11/2016.
 */

public interface AddTaskContract {

    interface View extends BaseView<Presenter>{

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();

    }

    interface Presenter extends BasePresenter{

        void saveTask(String title,String description);

        void populateTask();

        boolean isDataMissing();

    }
}
