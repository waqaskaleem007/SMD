package com.example.assignment2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Student implements Serializable{
    private String name;
    private String rollno;
    private boolean present;

    public Student(String name, String rollno, boolean present) {
        this.name = name;
        this.rollno = rollno;
        this.present = present;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }



}
