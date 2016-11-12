package com.d.dao.google_todo_mvp2.addtask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.d.dao.google_todo_mvp2.R;
import com.d.dao.google_todo_mvp2.data.source.TasksRepository;
import com.d.dao.google_todo_mvp2.data.source.local.TasksLocalDataSource;
import com.d.dao.google_todo_mvp2.data.source.remote.TasksRemoteDataSource;
import com.d.dao.google_todo_mvp2.utils.ActivityUtil;

/**
 * Created by dao on 12/11/2016.
 */

public class AddTaskActivity extends AppCompatActivity {


    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";


    private Toolbar toolbar;
    private AddTaskPresenter mPresenter;


    private String mTaskId;
    ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initToolbar();
        initOutData();
        initFragment(savedInstanceState);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
    }

    private void initOutData() {
        mTaskId = getIntent().getStringExtra(
                AddTaskFragment.ARGUMENT_EDIT_TASK_ID);

    }

    private void initFragment(Bundle savedInstanceState) {
        AddTaskFragment fragment = (AddTaskFragment) getSupportFragmentManager().findFragmentById(
                R.id.contentFrame);


        if (fragment == null) {
            fragment = AddTaskFragment.newInstance();

            if (getIntent().hasExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID)) {

                mActionBar.setTitle("Edit to-do");
                Bundle bundle = new Bundle();
                bundle.putString(AddTaskFragment.ARGUMENT_EDIT_TASK_ID
                        , mTaskId);
                fragment.setArguments(bundle);
            } else {
                mActionBar.setTitle("new to-do");
            }

            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.contentFrame);


        }


        boolean shouldLoadDataFromRepo = true;

        if (savedInstanceState != null) {
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(
                    SHOULD_LOAD_DATA_FROM_REPO_KEY
            );
        }

        mPresenter = new AddTaskPresenter(mTaskId,
                TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                        TasksLocalDataSource.getInstance(getApplicationContext())),
                fragment,
                shouldLoadDataFromRepo);


        fragment.setPresenter(mPresenter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
