package com.example.administrator.studentmanager.model;

/**
 * Created by Administrator on 14/05/2017.
 */

public class Student {

    private String id;
    private String name;
    private String birthday;
    private int gender;

    public Student() {

    }

    public Student(String id, String name, String birthday, int gender) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
