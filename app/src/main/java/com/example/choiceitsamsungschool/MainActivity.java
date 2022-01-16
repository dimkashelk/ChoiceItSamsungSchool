package com.example.choiceitsamsungschool;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private final static String salt = "[B@65b3120a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton create_account_view = findViewById(R.id.create_account);
        create_account_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MainActivity.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.bottom_sheet_create_account_activity,
                        (LinearLayout) findViewById(R.id.bottomSheetCreateAccount)
                );
                bottomSheetView.findViewById(R.id.bottomSheetCreateAccountButton).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkRegisterData(bottomSheetView)) {
                                    bottomSheetDialog.dismiss();
                                }
                            }
                        }
                );
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private boolean checkRegisterData(View v) {
        EditText firstName = (EditText) v.findViewById(R.id.bottomSheetCreateAccountFirstName);
        EditText secondName = (EditText) v.findViewById(R.id.bottomSheetCreateAccountSecondName);
        EditText login = (EditText) v.findViewById(R.id.bottomSheetCreateAccountShortName);
        EditText email = (EditText) v.findViewById(R.id.bottomSheetCreateAccountEmail);
        EditText password = (EditText) v.findViewById(R.id.bottomSheetCreateAccountPassword);
        EditText re_password = (EditText) v.findViewById(R.id.bottomSheetCreateAccountRePassword);

        boolean fl = true;

        if (firstName.getText().toString().equals("") || firstName.getText().toString().contains(" ")) {
            firstName.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            firstName.setBackground(getResources().getDrawable(
                    R.drawable.input_custom
            ));
        }

        if (secondName.getText().toString().equals("") ||
                secondName.getText().toString().contains(" ")) {
            secondName.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            secondName.setBackground(getResources().getDrawable(
                    R.drawable.input_custom
            ));
        }

        if (login.getText().toString().equals("") ||
                login.getText().toString().contains(" ")) {
            login.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            Vector<Character> login_chars = new Vector<>();

            String login_string = login.getText().toString();
            for (int i = 0; i < login_string.length(); i++) {
                login_chars.add(login_string.charAt(i));
            }
            String digits_and_alphabet = "qwertyuiopasdfghjklzxcvbnm0123456789";
            for (Character i: login_chars) {
                if (!digits_and_alphabet.contains(i.toString())) {
                    fl = false;
                    break;
                }
            }
            if (fl) {
                login.setBackground(getResources().getDrawable(
                        R.drawable.input_custom
                ));
            } else {
                login.setBackground(getResources().getDrawable(
                        R.drawable.input_custom_error
                ));
            }
        }

        if (email.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            email.setBackground(getResources().getDrawable(
                    R.drawable.input_custom
            ));
        }

        if (password.getText().toString().equals("") ||
                password.getText().toString().contains(" ")) {
            password.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            password.setBackground(getResources().getDrawable(
                    R.drawable.input_custom
            ));
        }

        if (!password.getText().toString().equals(re_password.getText().toString())) {
            re_password.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            if (re_password.getText().toString().equals("") ||
                    re_password.getText().toString().contains(" ")) {
                re_password.setBackground(getResources().getDrawable(
                        R.drawable.input_custom_error
                ));
                fl = false;
            } else {
                re_password.setBackground(getResources().getDrawable(
                        R.drawable.input_custom
                ));
            }
        }
        return fl;
    }
}