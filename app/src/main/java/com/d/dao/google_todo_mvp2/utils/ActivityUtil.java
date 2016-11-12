package com.d.dao.google_todo_mvp2.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by dao on 11/10/16.
 */

public class ActivityUtil {

    public static void addFragmentToActivity(FragmentManager fragmentManager
            , Fragment fragment, int id) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(id, fragment);
        transaction.commit();
    }

}
