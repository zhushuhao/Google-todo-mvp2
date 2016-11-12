package com.d.dao.google_todo_mvp2.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.d.dao.google_todo_mvp2.R;
import com.d.dao.google_todo_mvp2.data.source.TasksRepository;
import com.d.dao.google_todo_mvp2.data.source.local.TasksLocalDataSource;
import com.d.dao.google_todo_mvp2.data.source.remote.TasksRemoteDataSource;
import com.d.dao.google_todo_mvp2.statistics.StatisticsActivity;
import com.d.dao.google_todo_mvp2.utils.ActivityUtil;

/**
 * Created by dao on 11/10/16.
 */

public class TasksActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private TasksPresenter mTaskPresenter;

    private TasksFragment mTasksFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initToolbar();
        initDrawer();
        initFragment();
        initPresenter();
        initFilterType(savedInstanceState);

    }

    private void initFilterType(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            TasksFilterType currentFilterType =
                    (TasksFilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_KEY);
            mTaskPresenter.setFilterType(currentFilterType);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mTaskPresenter.getFilterType());
        super.onSaveInstanceState(outState);
    }





    private void initPresenter() {
        mTaskPresenter = new TasksPresenter(
                TasksRepository.getInstance(TasksRemoteDataSource.getInstance(),
                        TasksLocalDataSource.getInstance(this)),
                mTasksFragment
        );
    }

    private void initFragment() {

        mTasksFragment = (TasksFragment) getSupportFragmentManager().
                findFragmentById(R.id.contentFrame);


        if (mTasksFragment == null) {
            mTasksFragment = TasksFragment.newInstance();
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    mTasksFragment, R.id.contentFrame);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        setUpDrawerContent();

    }

    private void setUpDrawerContent() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_item:
                        break;
                    case R.id.statistics_item:
                        gotoStatisticsActivity();
                        break;
                    default:
                        break;
                }

                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void gotoStatisticsActivity() {
//        Intent intent = new Intent(TasksActivity.this, StatisticsActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }


}
