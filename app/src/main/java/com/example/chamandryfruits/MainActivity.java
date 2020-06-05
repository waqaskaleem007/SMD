package com.example.chamandryfruits;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        sleep(3000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            Intent RegisterIntent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(RegisterIntent);
            finish();
        }
        else{
            Intent loginIntent = new Intent(MainActivity.this,Home.class);
            startActivity(loginIntent);
            finish();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        return false;
    }
}
