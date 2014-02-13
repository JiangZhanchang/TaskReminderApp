package com.app.eric.TaskReminderApp;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by Eric on 14-1-23.
 */
public class ReminderProvider extends ContentProvider {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dataForReminderTask";
    private static final String DATABASE_TABLE = "reminders";

    public static final String COLUMN_ROWID = "_id";
    public static final String COLUMN_DATE_TIME = "reminder_date_time";
    public static final String COLUMN_BODY = "reminder_body";
    public static final String COLUMN_TITLE = "reminder_title";

    private static final String DATABASE_CREATE = "create table " +
            DATABASE_TABLE + " (" + COLUMN_ROWID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_BODY + " text not null, "
            + COLUMN_DATE_TIME + " integer not null);";

    private SQLiteDatabase mDb;

    public static String AUTHORITY = "com.app.eric.TaskReminderApp.ReminderProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/reminder");

    public static final String REMINDERS_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.com.app.eric.TaskReminder.reminder";
    public static final String REMINDER_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.com.app.eric.TaskReminder.reminder";

    private static final int LIST_REMINDER = 0;
    private static final int ITEM_REMINDER = 1;
    private static final UriMatcher sURIMatcher = builderUriMatcher();

    private static UriMatcher builderUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "reminder", LIST_REMINDER);
        matcher.addURI(AUTHORITY, "reminder/#", ITEM_REMINDER);
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mDb = new DatabaseHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] ignored1, String ignored2, String[] ignored3, String ignored4) {
        String[] projection = new String[]{ReminderProvider.COLUMN_ROWID, ReminderProvider.COLUMN_TITLE, ReminderProvider.COLUMN_BODY, ReminderProvider.COLUMN_DATE_TIME};

        //Use the UriMatcher to see the query type and format the db query accordingly.
        Cursor c;
        switch (sURIMatcher.match(uri)) {
            case LIST_REMINDER:
                c = mDb.query(DATABASE_TABLE, projection, null, null, null, null, null);
                break;
            case ITEM_REMINDER:
                c = mDb.query(DATABASE_TABLE, projection, COLUMN_ROWID + "=?", new String[]{Long.toString(ContentUris.parseId(uri))}, null, null, null, null);
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri + ". --REMINDERPROVIDER");
        }
        ContentResolver cr = null;
        try {
            cr = getContext().getContentResolver();
            c.setNotificationUri(cr, uri);
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case LIST_REMINDER:
                return REMINDERS_MIME_TYPE;
            case ITEM_REMINDER:
                return REMINDER_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        values.remove(ReminderProvider.COLUMN_ROWID);
        long id = mDb.insertOrThrow(ReminderProvider.DATABASE_TABLE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = mDb.delete(ReminderProvider.DATABASE_TABLE, ReminderProvider.COLUMN_ROWID + "=?", new String[]{Long.toString(ContentUris.parseId(uri))});
        if (count > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = mDb.update(ReminderProvider.DATABASE_TABLE, values, COLUMN_ROWID + "=?", new String[]{Long.toString(ContentUris.parseId(uri))});
        if (count > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Created by Eric on 14-1-27.
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            throw new UnsupportedOperationException();
        }
    }

}
