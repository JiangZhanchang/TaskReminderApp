package com.app.eric.TaskReminderApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Eric on 14-1-23.
 */
public class ReminderEditFragment extends Fragment {
    public static final String DEFAULT_EDIT_FRAGMENT_TAG = "editFragmentTag";

    private EditText mTitleText;
    private EditText mBodyText;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mConfirmButton;
    private long mRowId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mRowId = args.getLong(ReminderProvider.COLUMN_ROWID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_edit, container, false);

        mTitleText = (EditText) view.findViewById(R.id.reminder_Title);
        mBodyText = (EditText) view.findViewById(R.id.reminder_Body);
        mDateButton = (Button) view.findViewById(R.id.reminder_Date);
        mTimeButton = (Button) view.findViewById(R.id.reminder_Time);
        mConfirmButton = (Button) view.findViewById(R.id.reminder_Confirm);
        return view;
    }
}
