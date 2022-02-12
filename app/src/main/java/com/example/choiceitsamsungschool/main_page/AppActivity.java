package com.example.choiceitsamsungschool.main_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new NavigationItemListener(this, bottomNavigationView));
        bottomNavigationView.setSelectedItemId(R.id.page_main);
    }
}