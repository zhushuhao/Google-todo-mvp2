package com.d.dao.google_todo_mvp2.taskdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.d.dao.google_todo_mvp2.R;
import com.d.dao.google_todo_mvp2.data.source.TasksRepository;
import com.d.dao.google_todo_mvp2.data.source.local.TasksLocalDataSource;
import com.d.dao.google_todo_mvp2.data.source.remote.TasksRemoteDataSource;
import com.d.dao.google_todo_mvp2.utils.ActivityUtil;

/**
 * Created by dao on 12/11/2016.
 */

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    private String mStringId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        initToolbar();
        initOutData();
        initFragment();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
//
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initOutData() {
        mStringId = getIntent().getStringExtra(EXTRA_TASK_ID);
    }

    private void initFragment() {
        TaskDetailFragment fragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = TaskDetailFragment.newInstance(mStringId);

            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.contentFrame);
        }
        new TaskDetailPresenter(mStringId, TasksRepository.getInstance(
                TasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(getApplicationContext())), fragment);
    }

    // TODO: 12/11/2016 莫名其妙的不行
//    @Override
//    public boolean onSupportNavigateUp() {
//        Log.e("clicked", "back");
//        onBackPressed();
//        return true;
//    }


}
