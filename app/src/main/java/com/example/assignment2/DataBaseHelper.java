package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.service.autofill.CustomDescription;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Student.db";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Student (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "NAME TEXT, "+
                "ROLLNO TEXT, "+
                "ISPRESENT BOOLEAN) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Student");
        onCreate(db);
    }

    public void deleteData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM Student");

    }


    public boolean insertData(String name, String rollno){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("ROLLNO",rollno);
        contentValues.put("ISPRESENT",false);
        long result = db.insert("Student",null,contentValues);
        return result != -1;
    }

    public boolean updateData(String name, String rollno, boolean present){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("ROLLNO",rollno);
        contentValues.put("ISPRESENT",present);
        long result = db.update("Student",contentValues, "NAME = ?",new String[]{name});
        return result != -1;
    }


    public Cursor getAllStudents(){
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM Student",null);
    }





}
