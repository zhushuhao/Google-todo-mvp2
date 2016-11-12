package com.d.dao.google_todo_mvp2.taskdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.d.dao.google_todo_mvp2.R;
import com.d.dao.google_todo_mvp2.addtask.AddTaskActivity;
import com.d.dao.google_todo_mvp2.addtask.AddTaskFragment;

/**
 * Created by dao on 12/11/2016.
 */

public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {


    private static final String ARGUMENT_TASK_ID = "task_id";

    private static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailContract.Presenter mPresenter;

    private TextView tv_title;

    private TextView tv_description;

    private CheckBox cb_complete_status;

    private FloatingActionButton mFab;


    public static TaskDetailFragment newInstance(String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_task_detail, container, false);
        setHasOptionsMenu(true);

        tv_title = (TextView) root.findViewById(R.id.tv_title);

        tv_description = (TextView) root.findViewById(R.id.tv_description);

        cb_complete_status = (CheckBox) root.findViewById(R.id.cb);

        cb_complete_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.completeTask();
                } else {
                    mPresenter.activateTask();
                }
            }
        });

        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editTask();
            }
        });

        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task_detail_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_delete:
                mPresenter.deleteTask();
                break;
        }
        return true;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

        if (active) {
            tv_title.setText("");
            tv_description.setText("加载中...");
        }
    }

    @Override
    public void showMissingTask() {

        tv_title.setText("");
        tv_description.setText("没有数据");

    }

    @Override
    public void hideTitle() {

        tv_title.setVisibility(View.GONE);

    }

    @Override
    public void showTitle(String text) {

        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(text);
    }

    @Override
    public void hideDescription() {

        tv_description.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {

        tv_description.setVisibility(View.VISIBLE);
        tv_description.setText(description);
    }


    @Override
    public void showCompletionStatus(boolean competed) {

        cb_complete_status.setChecked(competed);

    }

    @Override
    public void showEditTask(String taskId) {

        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra(AddTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(), "Task marked complete", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(), "Task marked active", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

}
