package com.example.choiceitsamsungschool;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.choiceitsamsungschool.welcome_page.WelcomePage;

public class MainActivity extends AppCompatActivity {

    private final static String PREFERENCES_AUTHORIZE_DATA = "authorize_data";

    private SharedPreferences authorize_data;
    private SharedPreferences.Editor editor_authorize_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkAllPermission();

        authorize_data = getSharedPreferences(PREFERENCES_AUTHORIZE_DATA, Context.MODE_PRIVATE);
        editor_authorize_data = authorize_data.edit();

        Intent welcome_page = new Intent(this, WelcomePage.class);
        welcome_page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(welcome_page);
    }

    private void checkAllPermission() {
        // INTERNET
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
    }
}