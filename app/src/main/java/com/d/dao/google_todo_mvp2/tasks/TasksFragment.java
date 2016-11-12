package com.d.dao.google_todo_mvp2.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.d.dao.google_todo_mvp2.R;
import com.d.dao.google_todo_mvp2.addtask.AddTaskActivity;
import com.d.dao.google_todo_mvp2.data.Task;
import com.d.dao.google_todo_mvp2.taskdetail.TaskDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dao on 11/10/16.
 */

public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter;


    private LinearLayout ll_no_task;
    private ImageView iv_no_task;
    private TextView tv_no_task;
    private TextView tv_no_task_add;


    private LinearLayout ll_task;
    private TextView tv_label;
    private ListView mListView;

    private FloatingActionButton mFab;

    ScrollChildSwipeRefreshLayout mRefreshLayout;

    private TasksAdapter mAdapter;

    public TasksFragment() {
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {

        Log.e("prensenter", "set");
        this.mPresenter = presenter;
    }


    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        mListView = (ListView) root.findViewById(R.id.lv);

        mAdapter = new TasksAdapter(getContext(), new ArrayList<Task>(), mItemListener);

        mListView.setAdapter(mAdapter);
        if (mPresenter == null) {
            Log.e("mpresenter", "null");
        }
        ll_no_task = (LinearLayout) root.findViewById(R.id.ll_no_task);
        tv_no_task = (TextView) root.findViewById(R.id.tv_no_task);
        tv_no_task_add = (TextView) root.findViewById(R.id.tv_no_task_add);
        iv_no_task = (ImageView) root.findViewById(R.id.iv_no_task);

        ll_task = (LinearLayout) root.findViewById(R.id.ll_task);
        tv_label = (TextView) root.findViewById(R.id.tv_label);


        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mFab.setImageResource(R.drawable.ic_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask();
            }
        });

        mRefreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mRefreshLayout.setScrollUpChild(mListView);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks(false);
            }
        });

        setHasOptionsMenu(true);
        return root;
    }


    private TasksAdapter.TaskItemListener mItemListener = new TasksAdapter.TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {

            mPresenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completeTask) {

            mPresenter.completeTask(completeTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {

            mPresenter.activateTask(activatedTask);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_clear:
                mPresenter.clearCompletedTasks();
                break;
            case R.id.item_filter:
                showFilterPopUpMenu();
                break;
            case R.id.item_refresh:
                mPresenter.loadTasks(true);
                break;
            default:
                break;
        }
        return true;
    }

    private void showFilterPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), getActivity().findViewById(R.id.item_filter));

        popupMenu.getMenuInflater().inflate(R.menu.filter_tasks, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.item_active:
                        mPresenter.setFilterType(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.item_completed:
                        mPresenter.setFilterType(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mPresenter.setFilterType(TasksFilterType.ALL_TASKS);
                        break;

                }
                mPresenter.loadTasks(false);
                return true;
            }
        });

        popupMenu.show();


    }


    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showLoadTasksError() {
        showMessage(getString(R.string.load_task_error));
    }

    @Override
    public void showTasks(List<Task> tasks) {

        mAdapter.replaceData(tasks);
        ll_task.setVisibility(View.VISIBLE);
        ll_no_task.setVisibility(View.GONE);
    }

    @Override
    public void showActiveFilterLabel() {
        tv_label.setText("active to-dos");

    }

    @Override
    public void showCompleteFilterLabel() {
        tv_label.setText("已完成的to-dos");

    }

    @Override
    public void showAllFilterLabel() {
        tv_label.setText("所有的to-dos");
    }

    @Override
    public void showNoActiveTasks() {

        showNoTasksViews("没有 active to-dos", R.drawable.ic_check_circle_24dp, false);
    }

    private void showNoTasksViews(String text, int icon, boolean showAddView) {
        ll_task.setVisibility(View.GONE);
        ll_no_task.setVisibility(View.VISIBLE);

        tv_no_task.setText(text);
        iv_no_task.setImageDrawable(getResources().getDrawable(icon));
        tv_no_task_add.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoCompletedTasks() {

        showNoTasksViews("没有已完成的to-dos", R.drawable.ic_verified_user_24dp, false);
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews("没有to-dos", R.drawable.ic_assignment_turned_in_24dp, false);

    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage("已完成的任务已经清除");
    }

    @Override
    public void showTaskDetailsUI(String id) {
        gotoDetailActivity(id);

    }

    private void gotoDetailActivity(String taskId) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void showTaskMarkedComplete() {

        showMessage("task marked complete");
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage("task marked active");

    }

    @Override
    public void showAddTask() {
        gotoEditTaskActivity();
    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage("todo 保存成功");
    }

    private void gotoEditTaskActivity() {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        startActivityForResult(intent, AddTaskActivity.REQUEST_ADD_TASK);

    }


    private void showMessage(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }


}
