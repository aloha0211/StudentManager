package com.example.administrator.studentmanager.database;

/**
 * Created by Administrator on 14/05/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.administrator.studentmanager.model.Student;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ran on 4/20/2016.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "student";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_GENDER = "gender";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_ID + " text primary key not null, " + COLUMN_NAME + " text not null, "
            + COLUMN_BIRTHDAY + " text not null, " + COLUMN_GENDER + " integer);";

    private static final String TAG = "DbHelper";

    public DbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Student> getListStudents() {
        List<Student> students = new ArrayList<Student>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_BIRTHDAY, COLUMN_GENDER},
                null, null, null, null, COLUMN_NAME);

        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
                int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));

                Student student = new Student(id, name, birthday, gender);
                students.add(student);
            } while (c.moveToNext());
            c.close();
        }

        db.close();
        return students;
    }

    public Student getStudent(String id) {
        Student student;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + "'" + id + "'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(COLUMN_NAME));
            String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
            int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
            student = new Student(id, name, birthday, gender);
        } else
            student = null;
        c.close();
        db.close();
        return student;
    }

    public long addStudent(Student student) {
        long ret = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, student.getId());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_BIRTHDAY, student.getBirthday());
        values.put(COLUMN_GENDER, student.getGender());

        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.insert(TABLE_NAME, null, values);
        db.close();
        return ret;
    }

    public int updateStudent(Student student) {
        int ret = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_BIRTHDAY, student.getBirthday());
        values.put(COLUMN_GENDER, student.getGender());

        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{student.getId()});
        db.close();
        return ret;
    }

    public int deleteStudent(String id) {
        int ret = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        db.close();
        return ret;
    }

    public List<Student> searchStudent(String searchText) {
        List<Student> students = new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_NAME + " LIKE " + "'%" + searchText + "%'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
                int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
                Student student = new Student(id, name, birthday, gender);
                students.add(student);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return students;
    }
}
