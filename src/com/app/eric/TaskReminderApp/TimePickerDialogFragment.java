package com.app.eric.TaskReminderApp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

/**
 * Created by Eric on 14-1-23.
 */
public class TimePickerDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        Fragment editFragment = getFragmentManager().findFragmentByTag(ReminderEditFragment.DEFAULT_EDIT_FRAGMENT_TAG);
        TimePickerDialog.OnTimeSetListener listener = (TimePickerDialog.OnTimeSetListener) editFragment;
        return new TimePickerDialog(getActivity(), listener,
                args.getInt(ReminderEditFragment.HOUR),
                args.getInt(ReminderEditFragment.MINS),
                true);
    }
}
