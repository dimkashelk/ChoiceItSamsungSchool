package com.example.choiceitsamsungschool;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.choiceitsamsungschool.db.User;
import com.example.choiceitsamsungschool.main_page.AppActivity;
import com.example.choiceitsamsungschool.welcome_page.WelcomePage;

public class MainActivity extends AppCompatActivity {

    private final static String PREFERENCES_AUTHORIZE_DATA = "authorize_data";

    private SharedPreferences authorize_data;
    private SharedPreferences.Editor editor_authorize_data;

    private APIServer apiServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading_page);

        checkAllPermission();

        authorize_data = getSharedPreferences(PREFERENCES_AUTHORIZE_DATA, Context.MODE_PRIVATE);
        editor_authorize_data = authorize_data.edit();

        APIServer.setMainActivity(this);

        User default_user = new User("Дмитрий", "Шелковников", "", "dimkashelk");
        AppDatabase appDatabase = AppDatabase.getDatabase(getBaseContext());
        appDatabase.userDao().removeAllUsers();
        appDatabase.userDao().addUser(default_user);

//        APIServer.getSingletonAPIServer().authorize(getLogin(), getToken());
        login();
    }

    public void checkAllPermission() {
        // INTERNET
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
        // WRITE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        // READ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public void getInternalStoragePermission() {
        // WRITE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        // READ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public SharedPreferences getAuthorize_data() {
        return authorize_data;
    }

    public String getToken() {
        return authorize_data.getString("token", "");
    }

    public String getLogin() {
        return authorize_data.getString("login", "");
    }

    public void setLogin(String login) {
        editor_authorize_data.putString("login", login);
    }

    public void setToken(String token) {
        editor_authorize_data.putString("token", token);
    }

    public void login() {
        Intent app_page = new Intent(this, AppActivity.class);
        app_page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(app_page);
    }

    public void logout() {
        Intent welcome_page = new Intent(this, WelcomePage.class);
        welcome_page.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(welcome_page);
    }
}