package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNotes(v);
            }
        });

    }


    public void getNotes(View view)

    {

        String[] colNames = {"ID","NAME","ROLLNO","ISPRESENT"};

        Cursor cursor = getContentResolver().query(

                Uri.parse("content://com.example.assignment2.StudentProvider/students"), colNames, null, null, null);

        if (cursor.getCount() > 0){

            String result = "";

            while (cursor.moveToNext()){
                boolean value = cursor.getInt(3) > 0;
                result += cursor.getString(cursor.getColumnIndex("NAME")) + " " + cursor.getString(cursor.getColumnIndex("ROLLNO")) + " "  + value + "\n";
            }

            Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

        }

    }
}
