package com.example.choiceitsamsungschool.main_page;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {
    public static InternalStorage internalStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        internalStorage = InternalStorage.getInternalStorage(getFilesDir());

        setContentView(R.layout.app_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new NavigationItemListener(this, bottomNavigationView));
        bottomNavigationView.setSelectedItemId(R.id.page_main);
    }
}