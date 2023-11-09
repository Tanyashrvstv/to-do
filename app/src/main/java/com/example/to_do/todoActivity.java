package com.example.to_do;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class todoActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listView;
    private ArrayAdapter<String> tasks;

    private TaskDatabaseHelper mTaskDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_todo);
        super.onCreate(savedInstanceState);
        mTaskDbHelper = new TaskDatabaseHelper(this);
          listView = (ListView) findViewById(R.id.lvItem);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==  R.id.action_add_task) {
            final EditText taskEdit = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add a new task").setMessage("What to do next?")
                    .setView(taskEdit).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String task = String.valueOf(taskEdit.getText());
                            SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                            db.close();
                            updateUI();
                        }
                    }).setNegativeButton("Cancel", null).create();
            dialog.show();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.title_task);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE, TaskContract.TaskEntry.COL_TASK_TITLE + " = ?", new String[]{task});
        db.close();
        updateUI();
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE, new String[] {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));

            if(tasks == null) {
                tasks = new ArrayAdapter<>(this, R.layout.todo_task, R.id.title_task, taskList);
                listView.setAdapter(tasks);
            }
            else {
                tasks.clear();
                tasks.addAll(taskList);
                tasks.notifyDataSetChanged();;
            }

            cursor.close();
            db.close();
        }
    }
}
