package com.d.dao.google_todo_mvp2.addtask;

import com.d.dao.google_todo_mvp2.data.Task;
import com.d.dao.google_todo_mvp2.data.source.TasksDataSource;
import com.d.dao.google_todo_mvp2.data.source.TasksRepository;

/**
 * Created by dao on 12/11/2016.
 */

public class AddTaskPresenter implements AddTaskContract.Presenter, TasksDataSource.GetTaskCallback {


    private AddTaskContract.View mView;

    private TasksRepository mTasksRepository;

    private String mTaskId;

    private boolean mIsDataMissing;


    public AddTaskPresenter(String taskId, TasksRepository repository,
                            AddTaskContract.View view,
                            boolean showLoadDataFromRepo) {

        this.mTaskId = taskId;
        this.mTasksRepository = repository;

        this.mView = view;

        this.mIsDataMissing = showLoadDataFromRepo;

    }


    @Override
    public void saveTask(String title, String description) {

        if(isNewTask()){
            createTask(title,description);
        }else {
            updateTask(title,description);
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");

        }

        mTasksRepository.getTask(mTaskId, this);
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void start() {

        if (!isNewTask() && mIsDataMissing) {
            populateTask();
        }
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (mView.isActive()) {
            mView.setTitle(task.getTitle());
            mView.setDescription(task.getDescription());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        if (mView.isActive()) {
            mView.showEmptyTaskError();
        }
    }

    private void createTask(String title,String description){
        Task task = new Task(title,description);
        if(task.isEmpty()){
            mView.showEmptyTaskError();
        }else {
            mTasksRepository.saveTask(task);
            mView.showTasksList();
        }
    }

    private void updateTask(String title,String description){
        if(isNewTask()){
            throw new RuntimeException("updateTask() was called but task is new.");
        }

        mTasksRepository.saveTask(new Task(title,description));
        mView.showTasksList();
    }
}
