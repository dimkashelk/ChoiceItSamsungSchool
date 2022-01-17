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
    private final static String ALPHABET_RU_LARGE = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
    private final static String ALPHABET_RU_SMALL = "йцукенгшщзхъфывапролджэячсмитьбю";
    private final static String ALPHABET_RU = ALPHABET_RU_LARGE + ALPHABET_RU_SMALL;
    private final static String ALPHABET_EN_LARGE = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private final static String ALPHABET_EN_SMALL = "qwertyuiopasdfghjklzxcvbnm";
    private final static String ALPHABET_EN = ALPHABET_EN_LARGE + ALPHABET_EN_SMALL;
    private final static String DIGITS = "0123456789";

    private APIServer apiServer;

    private View bottomSheetViewCreateAccount;
    private BottomSheetDialog bottomSheetDialogCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiServer = new APIServer(this);

        initializeUI();
    }

    private void initializeUI() {
        bottomSheetDialogCreateAccount = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetViewCreateAccount = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_create_account_activity,
                (LinearLayout) findViewById(R.id.bottomSheetCreateAccount)
        );
        bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkRegisterData(bottomSheetViewCreateAccount)) {
                            bottomSheetDialogCreateAccount.dismiss();
                        }
                    }
                }
        );
        bottomSheetDialogCreateAccount.setContentView(bottomSheetViewCreateAccount);
        MaterialButton create_account_view = findViewById(R.id.create_account);
        create_account_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogCreateAccount.show();
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

//        TODO: доделать проверку свободности логина и почты

        if (firstName.getText().toString().equals("") || firstName.getText().toString().contains(" ")) {
            firstName.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            if (checkStringToAnotherChars(
                    firstName.getText().toString(),
                    MainActivity.ALPHABET_RU
            )) {
                firstName.setBackground(getResources().getDrawable(
                        R.drawable.input_custom
                ));
            } else {
                firstName.setBackground(getResources().getDrawable(
                        R.drawable.input_custom_error
                ));
                fl = false;
            }
        }

        if (secondName.getText().toString().equals("") ||
                secondName.getText().toString().contains(" ")) {
            secondName.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            if (checkStringToAnotherChars(
                    secondName.getText().toString(),
                    MainActivity.ALPHABET_RU
            )) {
                secondName.setBackground(getResources().getDrawable(
                        R.drawable.input_custom
                ));
            } else {
                secondName.setBackground(getResources().getDrawable(
                        R.drawable.input_custom_error
                ));
                fl = false;
            }
        }

        if (login.getText().toString().equals("") ||
                login.getText().toString().contains(" ")) {
            login.setBackground(getResources().getDrawable(
                    R.drawable.input_custom_error
            ));
            fl = false;
        } else {
            if (checkStringToAnotherChars(
                    login.getText().toString(),
                    MainActivity.ALPHABET_EN_SMALL + MainActivity.DIGITS
            )) {
                apiServer.checkLoginToCreate(login.getText().toString());
                login.setBackground(getResources().getDrawable(
                        R.drawable.input_custom
                ));
            } else {
                login.setBackground(getResources().getDrawable(
                        R.drawable.input_custom_error
                ));
                fl = false;
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

    private boolean checkStringToAnotherChars(String stringToCheck, String alphabet) {
        Vector<Character> stringToCheck_chars = new Vector<>();
        for (int i = 0; i < stringToCheck.length(); i++) {
            stringToCheck_chars.add(stringToCheck.charAt(i));
        }
        for (Character i : stringToCheck_chars) {
            if (!alphabet.contains(i.toString())) {
                return false;
            }
        }
        return true;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void setErrorCreateAccountLogin() {
        EditText login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortName);
        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_error
        ));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkCreateAccountLogin() {
        EditText login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortName);
        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_ok
        ));
    }
}