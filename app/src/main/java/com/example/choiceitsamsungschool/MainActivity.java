package com.example.choiceitsamsungschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

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

    private final static String PREFERENCES_AUTHORIZE_DATA = "authorize_data";

    private SharedPreferences authorize_data;
    private SharedPreferences.Editor editor_authorize_data;

    private APIServer apiServer;

    private View bottomSheetViewCreateAccount;
    private View bottomSheetViewLogin;
    private BottomSheetDialog bottomSheetDialogCreateAccount;
    private BottomSheetDialog bottomSheetDialogLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authorize_data = getSharedPreferences(PREFERENCES_AUTHORIZE_DATA, Context.MODE_PRIVATE);
        editor_authorize_data = authorize_data.edit();

        checkAllPermission();

        apiServer = new APIServer(this);

        initializeUI();
    }

    private void checkAllPermission() {
        // INTERNET
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }
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

        bottomSheetDialogLogin = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetViewLogin = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_login_activity,
                (LinearLayout) findViewById(R.id.bottomSheetLogin)
        );
        bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkLoginData(bottomSheetViewLogin)) {
                            bottomSheetDialogLogin.dismiss();
                        }
                    }
                }
        );
        bottomSheetDialogLogin.setContentView(bottomSheetViewLogin);
        MaterialButton login_view = findViewById(R.id.login_account);
        login_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogLogin.show();
            }
        });

        TextView bottomSheetCreateAccountLogin = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountLogin);
        bottomSheetCreateAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogCreateAccount.dismiss();
                bottomSheetDialogLogin.show();
            }
        });

        TextView bottomSheetLoginCreateAccount = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginCreateAccount);
        bottomSheetLoginCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogLogin.dismiss();
                bottomSheetDialogCreateAccount.show();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private boolean checkRegisterData(View v) {
        TextInputEditText firstName = v.findViewById(R.id.bottomSheetCreateAccountFirstName);
        TextInputEditText secondName = v.findViewById(R.id.bottomSheetCreateAccountSecondName);
        TextInputEditText login = v.findViewById(R.id.bottomSheetCreateAccountShortName);
        TextInputEditText email = v.findViewById(R.id.bottomSheetCreateAccountEmail);
        TextInputEditText password = v.findViewById(R.id.bottomSheetCreateAccountPassword);
        TextInputEditText re_password = v.findViewById(R.id.bottomSheetCreateAccountRePassword);

        boolean fl = true;

        if (firstName.getText().toString().equals("") || firstName.getText().toString().contains(" ")) {
            firstName.setError(getResources().getString(
                    R.string.error_first_name
            ));
            fl = false;
        } else {
            if (checkStringToAnotherChars(
                    firstName.getText().toString(),
                    MainActivity.ALPHABET_RU
            )) {
                firstName.setError(null);
            } else {
                firstName.setError(getResources().getString(
                        R.string.error_first_name
                ));
                fl = false;
            }
        }

        if (secondName.getText().toString().equals("") ||
                secondName.getText().toString().contains(" ")) {
            secondName.setError(getResources().getString(
                    R.string.error_second_name
            ));
            fl = false;
        } else {
            if (checkStringToAnotherChars(
                    secondName.getText().toString(),
                    MainActivity.ALPHABET_RU
            )) {
                secondName.setError(null);
            } else {
                secondName.setError(getResources().getString(
                        R.string.error_second_name
                ));
                fl = false;
            }
        }

        if (login.getText().toString().equals("") ||
                login.getText().toString().contains(" ")) {
            login.setError(getResources().getString(
                    R.string.error_login
            ));
            fl = false;
        } else {
            if (checkStringToAnotherChars(
                    login.getText().toString(),
                    MainActivity.ALPHABET_EN_SMALL + MainActivity.DIGITS
            )) {
                apiServer.checkLoginToCreate(login.getText().toString());
                login.setError(null);
            } else {
                login.setError(getResources().getString(
                        R.string.error_login
                ));
                fl = false;
            }
        }

        if (email.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError(getResources().getString(
                    R.string.error_email
            ));
            fl = false;
        } else {
            apiServer.checkEmailToCreate(email.getText().toString());
            email.setError(null);
        }

        if (password.getText().toString().equals("") ||
                password.getText().toString().contains(" ")) {
            password.setError(getResources().getString(
                    R.string.error_password
            ));
            fl = false;
        } else {
            email.setError(null);
        }

        if (!password.getText().toString().equals(re_password.getText().toString())) {
            re_password.setError(getResources().getString(
                    R.string.error_re_password
            ));
            fl = false;
        } else {
            if (re_password.getText().toString().equals("") ||
                    re_password.getText().toString().contains(" ")) {
                re_password.setError(getResources().getString(
                        R.string.error_re_password
                ));
                fl = false;
            } else {
                re_password.setError(null);
            }
        }
        return fl;
    }

    private boolean checkLoginData(View v) {
        EditText login_email = v.findViewById(R.id.bottomSheetLoginLogin);
        EditText password = v.findViewById(R.id.bottomSheetLoginPassword);

        String login_string = login_email.getText().toString();
        String password_string = password.getText().toString();
        if (login_string.isEmpty() || password_string.isEmpty()) {
            setErrorLoginUserLoginPassword();
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(login_string).matches()) {
                apiServer.checkUserEmailPassword(login_string, password_string);
            } else {
                apiServer.checkUserLoginPassword(login_string, password_string);
            }
        }
        return true;
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
                R.drawable.input_custom_error, null
        ));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkCreateAccountLogin() {
        EditText login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortName);
        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_ok, null
        ));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setErrorCreateAccountEmail() {
        EditText login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountEmail);
        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_error, null
        ));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkCreateAccountEmail() {
        EditText login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountEmail);
        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_ok, null
        ));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkLoginUserLoginPassword(String login_string, String token) {
        saveToken(login_string, token);

        EditText login = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginLogin);
        EditText password = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginPassword);

        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_ok, null
        ));
        password.setBackground(getResources().getDrawable(
                R.drawable.input_custom_ok, null
        ));

        // TODO: открываем следующую страницу! Он молодец, он ввел пароль!
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setErrorLoginUserLoginPassword() {
        EditText login = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginLogin);
        EditText password = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginPassword);

        login.setBackground(getResources().getDrawable(
                R.drawable.input_custom_error, null
        ));
        password.setBackground(getResources().getDrawable(
                R.drawable.input_custom_error, null
        ));
        // TODO: восстановление пароля
    }

    private void saveToken(String login_string, String token) {
        editor_authorize_data.putString(login_string, token);
        editor_authorize_data.apply();
    }
}