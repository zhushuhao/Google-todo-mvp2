package com.d.dao.google_todo_mvp2.addtask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.d.dao.google_todo_mvp2.R;

/**
 * Created by dao on 12/11/2016.
 */

public class AddTaskFragment extends Fragment implements AddTaskContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddTaskContract.Presenter mPresenter;

    private EditText et_title;

    private EditText et_description;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton)
                getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveTask(et_title.getText().toString(),
                        et_description.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_task,container,false);


        et_title= (EditText) root.findViewById(R.id.et_title);

        et_description = (EditText) root.findViewById(R.id.et_description);

        setHasOptionsMenu(true);



        return root;
    }

    public static AddTaskFragment newInstance() {
        return new AddTaskFragment();
    }

    public AddTaskFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(et_title,"todo can not be empty", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showTasksList() {

        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();

    }

    @Override
    public void setTitle(String title) {

        et_title.setText(title);
    }

    @Override
    public void setDescription(String description) {
        et_description.setText(description);

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(AddTaskContract.Presenter presenter) {

        this.mPresenter = presenter;
    }
}
