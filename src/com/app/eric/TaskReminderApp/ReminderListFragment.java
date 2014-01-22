package com.app.eric.TaskReminderApp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.View;

/**
 * Created by Eric on 14-1-21.
 */
public class ReminderListFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getResources().getString(R.string.no_reminders_note));
        setListShown(true);

    }
}
