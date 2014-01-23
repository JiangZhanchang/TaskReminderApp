package com.app.eric.TaskReminderApp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Eric on 14-1-21.
 */
public class ReminderListFragment extends ListFragment {
    private ListAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] itemsFake = new String[]{"Foo", "Bar", "Fizz", "Bin", "Zin", "Ditch", "Bug", "Jiang zhanchang", "Gui liangjing", "Sun Chuyi", "Mo tianquan",
                "Liu jian", "Hu run", "Ma yun", "Zhao zilong"};


        mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.reminder_row, R.id.reminder_Text, itemsFake);
        setListAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEmptyText(getResources().getString(R.string.no_reminders_note));
        registerForContextMenu(getListView());
        setHasOptionsMenu(true);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ReminderEditActivity.class);
        intent.putExtra(ReminderProvider.COLUMN_ROWID, id);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.list_menu_item_longpress, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                //Delete the task
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_insert:
                editReminder(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editReminder(long id) {
        Intent intent = new Intent(getActivity(), ReminderEditActivity.class);
        intent.putExtra(ReminderProvider.COLUMN_ROWID, id);
        startActivity(intent);
    }
}
