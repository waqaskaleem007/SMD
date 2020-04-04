package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MyAdapter.onStudentListener {

    //String students[];
    DataBaseHelper myDb;
    RecyclerView recyclerView;
    final String lexicon = "abcdefghijklmnopqrstuvwxyz";
    final Set<String> identifiers = new HashSet<String>();
    ArrayList<Student> students = new ArrayList<>();
    private static final String TAG = "MainActivity";
    MyAdapter myAdapter;

    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DataBaseHelper(this);

        if(savedInstanceState == null) {
            AddStudent();
            students = getAllStudents();

        }
        else{
            students = (ArrayList<Student>) savedInstanceState.getSerializable("studentList");
        }

        recyclerView = findViewById(R.id.recyclerview);
        myAdapter = new MyAdapter(this,students,this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkBox = findViewById(R.id.checkBox);

        //following handel the state of select all button
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String receivedHexColor = intent.getStringExtra("selectAll");

                assert receivedHexColor != null;
                if(receivedHexColor.equals("start")){

                    boolean flag = false;
                    for(int i = 0; i < students.size(); i++)
                    {
                        if(!students.get(i).isPresent())
                        {
                            flag = true;
                        }
                    }
                    if(flag)
                    {
                        checkBox.setChecked(false);
                    }else
                        checkBox.setChecked(true);
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));

        //select All button
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()) {
                    for (int i = 0; i < myAdapter.mDisplayedValues.size(); i++) {
                        myAdapter.mDisplayedValues.get(i).setPresent(true);
                        checkBox.setChecked(true);
                        myDb.updateData(myAdapter.mDisplayedValues.get(i).getName(),myAdapter.mDisplayedValues.get(i).getRollno(),true);
                        myAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    for (int i = 0; i < myAdapter.mDisplayedValues.size(); i++) {
                        myAdapter.mDisplayedValues.get(i).setPresent(false);
                        checkBox.setChecked(false);
                        myDb.updateData(myAdapter.mDisplayedValues.get(i).getName(),myAdapter.mDisplayedValues.get(i).getRollno(),false);
                        myAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }





    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_search_menue,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Student");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    private void filter(String text) {
        ArrayList<Student> filteredList = new ArrayList<>();

        for (Student item : students) {
            if (item.getName().toLowerCase().contains(text.toLowerCase()) || item.getRollno().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        myAdapter.filterList(filteredList);
    }


    public void AddStudent(){
        String name;
        for(int i=0; i<25; i++){
            name = randomIdentifier();
            boolean isInserted =  myDb.insertData(name,"17L-4000");
            if(isInserted){
                Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(MainActivity.this,"not entered",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            Random rand = new Random();
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }



    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> st = new ArrayList<>();
        Cursor res = myDb.getAllStudents();
        if(res.getCount() == 0){
            Toast.makeText(this,"No data found",Toast.LENGTH_SHORT).show();
            return null;
        }
        Student s;
        while (res.moveToNext()){
            boolean value = res.getInt(3) > 0;
            s = new Student(res.getString(1),res.getString(2),value);
            System.out.println(s);
            st.add(s);
        }

        return st;
    }



    @Override
    public void onStudentClick(int position) {
        students.get(position).setPresent(true);
        //Intent intent = new Intent(this,MainActivity.class);
        Log.d(TAG, "onStudentClick: clicked" + position);
        Toast.makeText(this,"onStudentClicked", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
            savedInstanceState.putSerializable("studentList",students);

    }


    @Override
    protected void onDestroy() {
        myDb.deleteData();
        super.onDestroy();

    }
}
