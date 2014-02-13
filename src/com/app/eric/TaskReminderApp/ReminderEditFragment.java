package com.app.eric.TaskReminderApp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Eric on 14-1-23.
 */
public class ReminderEditFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
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

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSaveTask();
            }
        });

        updateDateButton();
        updateTimeButton();

        if (mRowId == 0) {
            mTitleText.setText("请输入提醒事件名称。");
            mBodyText.setText("请输入提醒事件内容。");
        } else {
            //Fire off a background loader to retrieve the data from the database.
            getLoaderManager().initLoader(0, null, this);
        }

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

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FROMAT = "kk:mm";

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContentUris.withAppendedId(ReminderProvider.CONTENT_URI, mRowId), null, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        //Close this fragment down if the item we're editing was deleted.
        if (data.getCount() == 0) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ((OnFinishEditor) getActivity()).finishEditor();
                }
            });
        }

        mTitleText.setText(data.getString(data.getColumnIndexOrThrow(ReminderProvider.COLUMN_TITLE)));
        mBodyText.setText(data.getString(data.getColumnIndexOrThrow(ReminderProvider.COLUMN_BODY)));
        //Get the date from the database
        Long dateInMills = data.getLong(data.getColumnIndexOrThrow(ReminderProvider.COLUMN_DATE_TIME));
        Date date = new Date(dateInMills);
        mCalendar.setTime(date);
        updateTimeButton();
        updateDateButton();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDateButton();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(mCalendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(mCalendar.MINUTE, minute);
        updateTimeButton();
    }

    private void confirmSaveTask() {

        //Toast.makeText(getActivity(),"Clicked save button",Toast.LENGTH_LONG).show();//显示文本对话框
        //mTitleText.setError("Empty is not allowed.");//设置文本框错误信息

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReminderProvider.COLUMN_ROWID, mRowId);
        contentValues.put(ReminderProvider.COLUMN_TITLE, mTitleText.getText().toString());
        contentValues.put(ReminderProvider.COLUMN_BODY, mBodyText.getText().toString());
        contentValues.put(ReminderProvider.COLUMN_DATE_TIME, mCalendar.getTimeInMillis());

        if (mRowId == 0) {
            Uri itemUri = getActivity().getContentResolver().insert(ReminderProvider.CONTENT_URI, contentValues);
            mRowId = ContentUris.parseId(itemUri);
        } else {
            int count = getActivity().getContentResolver().update(ContentUris.withAppendedId(ReminderProvider.CONTENT_URI, mRowId), contentValues, null, null);
            if (count != 1)
                throw new IllegalStateException("Unable to update " + mRowId + ".");
        }

        Toast.makeText(getActivity(), getString(R.string.task_saved_message), Toast.LENGTH_LONG).show();
        getActivity().finish();

    }

    private void updateTimeButton() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FROMAT);
        String timeForButton = timeFormat.format(mCalendar.getTime());
        mTimeButton.setText(timeForButton);
    }

    private void updateDateButton() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_FORMAT);
        String timeForButton = timeFormat.format(mCalendar.getTime());
        mDateButton.setText(timeForButton);
    }


}
