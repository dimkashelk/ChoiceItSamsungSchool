package com.example.choiceitsamsungschool.main_page;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {
    public static InternalStorage internalStorage;
    private static AppActivity appActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        internalStorage = InternalStorage.getInternalStorage();

        setContentView(R.layout.app_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new NavigationItemListener(this, bottomNavigationView));
        bottomNavigationView.setSelectedItemId(R.id.page_main);

        APIServer.getSingletonAPIServer().loadUserData();

        appActivity = this;
    }

    public static AppActivity get() {
        return appActivity;
    }

    public View getContentLayout() {
        return findViewById(R.id.content);
    }

    public View getLayout() {
        return findViewById(R.id.app_page);
    }
}