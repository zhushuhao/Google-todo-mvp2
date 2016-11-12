package com.d.dao.google_todo_mvp2.data;


import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by dao on 11/10/16.
 */

public final class Task {
    private final String mId;
    private final String mTitle;

    private final String mDescription;

    private final boolean mCompleted;

    public Task(String title, String description) {
        this(title, description, UUID.randomUUID().toString(), false);
    }

    public Task(String title, String description, String id) {
        this(title, description, id, false);
    }

    public Task(String title, String description, boolean completed) {
        this(title, description, UUID.randomUUID().toString(), completed);
    }


    public Task(String title, String description,
                String id, boolean completed) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
    }


    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTitleForList() {
        if (!Strings.isNullOrEmpty(mTitle)) {
            return mTitle;
        } else {
            return mDescription;
        }
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mTitle) &&
                Strings.isNullOrEmpty(mDescription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equal(mId, task.mId) &&
                Objects.equal(mTitle, task.mTitle) &&
                Objects.equal(mDescription, task.mDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mTitle, mDescription);
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}
