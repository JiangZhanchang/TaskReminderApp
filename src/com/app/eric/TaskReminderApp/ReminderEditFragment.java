package com.app.eric.TaskReminderApp;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Eric on 14-1-23.
 */
public class ReminderEditFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, LoaderManager.LoaderCallbacks<Cursor> {
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

        if (savedInstanceState != null && savedInstanceState.containsKey(CALENDAR)) {
            mCalendar = (Calendar) savedInstanceState.getSerializable(CALENDAR);
        } else {
            mCalendar = Calendar.getInstance();
        }

        Bundle args = getArguments();
        if (args != null) {
            mRowId = args.getLong(ReminderProvider.COLUMN_ROWID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the calendar instance in case the user changed it
        outState.putSerializable(CALENDAR, mCalendar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminder_edit, container, false);

        mTitleText = (EditText) view.findViewById(R.id.reminder_Title);
        mBodyText = (EditText) view.findViewById(R.id.reminder_Body);
        mDateButton = (Button) view.findViewById(R.id.reminder_Date);
        mTimeButton = (Button) view.findViewById(R.id.reminder_Time);
        mConfirmButton = (Button) view.findViewById(R.id.reminder_Confirm);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        return view;
    }

    static final String YEAR = "year";
    static final String MONTH = "month";
    static final String DAY = "day";
    static final String HOUR = "hour";
    static final String MINS = "mins";
    static final String CALENDAR = "calendar";
    private Calendar mCalendar;

    private void showDatePicker() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DialogFragment newFragment = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(YEAR, mCalendar.get(Calendar.YEAR));
        args.putInt(MONTH, mCalendar.get(Calendar.MONTH));
        args.putInt(DAY, mCalendar.get(Calendar.DAY_OF_MONTH));
        newFragment.setArguments(args);
        newFragment.show(fragmentTransaction, "datePicker");
    }

    private void showTimePicker() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DialogFragment newFragment = new TimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(HOUR, mCalendar.get(Calendar.HOUR));
        args.putInt(MINS, mCalendar.get(Calendar.MINUTE));
        newFragment.setArguments(args);
        newFragment.show(fragmentTransaction, "timePicker");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mDateButton.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}
