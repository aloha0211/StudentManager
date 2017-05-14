package com.example.administrator.studentmanager.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.studentmanager.R;
import com.example.administrator.studentmanager.database.DbHelper;
import com.example.administrator.studentmanager.model.Student;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentEditActivity extends AppCompatActivity {

    private TextView tvTitle;
    private EditText etID, etName, etBirthday;
    private RadioButton radMale, radFemale;
    private Date mBirthday = new Date();
    private String mId;
    private DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            etBirthday.setText(day + "/" + (month + 1) + "/" + year);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            mBirthday = cal.getTime();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etBirthday = (EditText) findViewById(R.id.etBirthday);
        radMale = (RadioButton) findViewById(R.id.radMale);
        radFemale = (RadioButton) findViewById(R.id.radFemale);

        mId = getIntent().getStringExtra(DbHelper.COLUMN_ID);
        if (mId == null) {
            // ADD MODE
            setDefaultInfo();
        } else {
            // EDIT MODE
            DbHelper dbHelper = new DbHelper(this, null);
            Student student = dbHelper.getStudent(mId);
            if (student != null) {
                tvTitle.setText(R.string.update_student);
                etID.setText(student.getId());
                etID.setEnabled(false);
                etName.setText(student.getName());
                etName.requestFocus();
                etBirthday.setText(student.getBirthday());
                if (student.getGender() == 0)
                    radFemale.setChecked(true);
                else
                    radMale.setChecked(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mId == null)
            return super.onCreateOptionsMenu(menu);
        else {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            DbHelper dbHelper = new DbHelper(this, null);
            if (dbHelper.deleteStudent(mId) > 0) {
                Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, ListStudentsActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        String strDate = etBirthday.getText() + "";
        String strArrtmp[] = strDate.split("/");
        int day = Integer.parseInt(strArrtmp[0]);
        int month = Integer.parseInt(strArrtmp[1]) - 1;
        int year = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog datePicker = new DatePickerDialog(this, callback, year, month, day);
        datePicker.setTitle(R.string.ngaysinh);
        datePicker.show();
    }

    public void clear(View v) {
        if (mId == null) {
            // ADD MODE
            etID.setText("");
            etName.setText("");
            setDefaultInfo();
        } else {
            // EDIT MODE
            etName.setText("");
            Calendar cal = Calendar.getInstance();
            etBirthday.setText(getDateFormat(cal.getTime()));
            radMale.setChecked(true);
            etName.requestFocus();
        }

    }

    public void save(View v) {
        if (mId == null) {
            // ADD MODE
            addStudent();
        } else {
            // EDIT MODE
            updateStudent();
        }
    }

    private void addStudent() {
        String mId = etID.getText().toString().trim();
        if (mId.length() == 0) {
            etID.setError("?");
            etID.requestFocus();
            return;
        }
        String name = etName.getText().toString().trim();
        if (name.length() == 0) {
            etName.setError("?");
            etName.requestFocus();
            return;
        }
        Student student = new Student();
        student.setId(mId);
        student.setName(name);
        student.setBirthday(getDateFormat(mBirthday));
        if (radMale.isChecked())
            student.setGender(1);
        else
            student.setGender(0);

        DbHelper dbHelper = new DbHelper(this, null);
        if (dbHelper.addStudent(student) > 0) {
            showToastMessage(getString(R.string.saved));
            Intent i = new Intent(this, ListStudentsActivity.class);
            startActivity(i);
            this.finish();
        } else {
            if (dbHelper.getStudent(mId) != null)
                showToastMessage(getString(R.string.student_exists));
            else
                showToastMessage(getString(R.string.error));
        }
    }

    private void updateStudent() {
        String name = etName.getText().toString();
        if (name.trim().length() == 0) {
            etName.setError("?");
            etName.requestFocus();
            return;
        }
        Student student = new Student();
        student.setId(mId);
        student.setName(name);
        student.setBirthday(getDateFormat(mBirthday));
        if (radMale.isChecked())
            student.setGender(1);
        else
            student.setGender(0);

        DbHelper dbHelper = new DbHelper(this, null);
        if (dbHelper.updateStudent(student) > 0) {
            showToastMessage(getString(R.string.updated));
            Intent i = new Intent(this, ListStudentsActivity.class);
            startActivity(i);
            this.finish();
        } else {
            showToastMessage(getString(R.string.error));
        }
    }


    private void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setDefaultInfo() {
        Calendar cal = Calendar.getInstance();
        etBirthday.setText(getDateFormat(cal.getTime()));
        radMale.setChecked(true);
        etID.requestFocus();
    }

    private String getDateFormat(Date date) {
        SimpleDateFormat dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dft.format(date);
    }

}
