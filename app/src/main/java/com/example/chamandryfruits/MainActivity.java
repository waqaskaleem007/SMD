package com.example.chamandryfruits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sleep(3000);
        Intent loginIntent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(loginIntent);
        finish();

    }
}
