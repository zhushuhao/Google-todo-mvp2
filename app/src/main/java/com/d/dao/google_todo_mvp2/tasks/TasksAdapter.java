package com.d.dao.google_todo_mvp2.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.d.dao.google_todo_mvp2.R;
import com.d.dao.google_todo_mvp2.data.Task;

import java.util.List;

/**
 * Created by dao on 11/11/2016.
 */

public class TasksAdapter extends BaseAdapter {

    private List<Task> mList;

    private TaskItemListener mItemListener;

    private Context mContext;

    private ViewHolder mHolder;

    public TasksAdapter(Context context, List<Task> tasks, TaskItemListener listener) {
        this.mList = tasks;

        this.mItemListener = listener;

        this.mContext = context;
    }

    public void replaceData(List<Task> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<Task> tasks) {
        mList = tasks;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public Task getItem(int position) {
        return mList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_task, parent, false
            );
            mHolder = new ViewHolder();

            mHolder.cb = (CheckBox) convertView.findViewById(R.id.cb_complete);
            mHolder.title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        final Task task = getItem(position);
        mHolder.title.setText(task.getTitleForList());

        mHolder.cb.setChecked(task.isCompleted());

        if (task.isCompleted()) {
            convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_completed_touch_feedback));
        } else {
            convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.touch_feedback));
        }

        mHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task.isCompleted()) {
                    mItemListener.onCompleteTaskClick(task);
                } else {
                    mItemListener.onActivateTaskClick(task);
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onTaskClick(task);
            }
        });

        return convertView;
    }

    class ViewHolder {
        CheckBox cb;
        TextView title;
    }

    public interface TaskItemListener {
        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completeTask);


        void onActivateTaskClick(Task activatedTask);

    }
}
