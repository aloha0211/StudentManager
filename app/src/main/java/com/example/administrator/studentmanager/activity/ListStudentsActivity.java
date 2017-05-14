package com.example.administrator.studentmanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.studentmanager.R;
import com.example.administrator.studentmanager.adapter.StudentAdapter;
import com.example.administrator.studentmanager.database.DbHelper;
import com.example.administrator.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.List;

public class ListStudentsActivity extends AppCompatActivity {

    private ListView lvListStudents;
    private List<Student> mListStudents;
    private StudentAdapter mAdapter;
    public static String mSearchText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvListStudents = (ListView) findViewById(R.id.lvListStudents);
        mListStudents = new ArrayList<>();
        mAdapter = new StudentAdapter(this, R.layout.student_row, mListStudents);
        lvListStudents.setAdapter(mAdapter);
        refreshListStudentsData();
        addEventListener();
    }

    public void refreshListStudentsData() {
        DbHelper dbHelper = new DbHelper(this, null);
        mListStudents.clear();
        mListStudents.addAll(dbHelper.getListStudents());
        mAdapter.notifyDataSetChanged();
    }

    public void addStudent(View v) {
        Intent i = new Intent(this, StudentEditActivity.class);
        startActivity(i);
    }

    private void addEventListener() {
        lvListStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Student student = mListStudents.get(position);
                Intent i = new Intent(ListStudentsActivity.this, StudentEditActivity.class);
                i.putExtra(DbHelper.COLUMN_ID,student.getId());
                startActivity(i);

            }
        });
        lvListStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >= 0) {
                    deleteStudent(mListStudents.get(position).getId());
                }
                return true;
            }
        });
    }


    private void deleteStudent(final String id) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.delete).setMessage(getString(R.string.delete_message) + " - " + id + " ?")
                .setIcon(android.R.drawable.ic_delete)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbHelper dbHelper = new DbHelper(ListStudentsActivity.this, null);
                        if (dbHelper.deleteStudent(id) > 0) {
                            Toast.makeText(ListStudentsActivity.this, getString(R.string.deleted), Toast.LENGTH_LONG).show();
                            refreshListStudentsData();
                        } else {
                            Toast.makeText(ListStudentsActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        b.create().show();
    }

}
