package com.example.choiceitsamsungschool;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.choiceitsamsungschool.main_page.AppActivity;
import com.example.choiceitsamsungschool.welcome_page.TextInputLayoutTextWatcher;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Vector;

import com.example.choiceitsamsungschool.welcome_page.WelcomePage;

public class MainActivity extends AppCompatActivity {

    private final static String PREFERENCES_AUTHORIZE_DATA = "authorize_data";

    private SharedPreferences authorize_data;
    private SharedPreferences.Editor editor_authorize_data;


    private APIServer apiServer;

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
}