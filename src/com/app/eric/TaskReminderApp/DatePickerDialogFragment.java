package com.app.eric.TaskReminderApp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * Created by Eric on 14-1-23.
 */
public class DatePickerDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args=getArguments();
        Fragment editFragment=getFragmentManager().findFragmentByTag(ReminderEditFragment.DEFAULT_EDIT_FRAGMENT_TAG);
        DatePickerDialog.OnDateSetListener listener=(DatePickerDialog.OnDateSetListener)editFragment;
        return  new DatePickerDialog(getActivity(),listener,
                args.getInt(ReminderEditFragment.YEAR),
                args.getInt(ReminderEditFragment.MONTH),
                args.getInt(ReminderEditFragment.DAY));
    }
}
