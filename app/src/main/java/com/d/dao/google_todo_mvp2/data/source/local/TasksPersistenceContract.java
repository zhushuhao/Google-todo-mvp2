package com.d.dao.google_todo_mvp2.data.source.local;

import android.provider.BaseColumns;

/**
 * Created by dao on 11/10/16.
 */

public final class TasksPersistenceContract {

    private TasksPersistenceContract(){}


    public static abstract  class TaskEntry implements BaseColumns{
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }
}
