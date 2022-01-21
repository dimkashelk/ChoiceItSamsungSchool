package com.example.choiceitsamsungschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private final static String SYMBOLS = "!@#$%,^:&?()*/-+[]";

    private final static String PREFERENCES_AUTHORIZE_DATA = "authorize_data";
    public final static String BOTTOM_SHEET_CREATE_ACCOUNT_FIRST_NAME_LAYOUT = "bottomSheetCreateAccountFirstNameLayout";
    public final static String BOTTOM_SHEET_CREATE_ACCOUNT_SECOND_NAME_LAYOUT = "bottomSheetCreateAccountSecondNameLayout";
    public final static String BOTTOM_SHEET_CREATE_ACCOUNT_SHORT_NAME_LAYOUT = "bottomSheetCreateAccountShortNameLayout";
    public final static String BOTTOM_SHEET_CREATE_ACCOUNT_EMAIL_LAYOUT = "bottomSheetCreateAccountEmailLayout";
    public final static String BOTTOM_SHEET_CREATE_ACCOUNT_PASSWORD_LAYOUT = "bottomSheetCreateAccountPasswordLayout";
    public final static String BOTTOM_SHEET_CREATE_ACCOUNT_RE_PASSWORD_LAYOUT = "bottomSheetCreateAccountRePasswordLayout";
    public final static String BOTTOM_SHEET_LOGIN_LOGIN_LAYOUT = "bottomSheetLoginLoginLayout";
    public final static String BOTTOM_SHEET_LOGIN_PASSWORD_LAYOUT = "bottomSheetLoginPasswordLayout";

    private SharedPreferences authorize_data;
    private SharedPreferences.Editor editor_authorize_data;

    private APIServer apiServer;

    private View bottomSheetViewCreateAccount;
    private View bottomSheetViewLogin;
    private BottomSheetDialog bottomSheetDialogCreateAccount;
    private BottomSheetDialog bottomSheetDialogLogin;

    private TextInputEditText bottomSheetCreateAccountFirstName;
    private TextInputLayout bottomSheetCreateAccountFirstNameLayout;
    private boolean bottomSheetCreateAccountFirstNameOk = false;
    private TextInputEditText bottomSheetCreateAccountSecondName;
    private TextInputLayout bottomSheetCreateAccountSecondNameLayout;
    private boolean bottomSheetCreateAccountSecondNameOk = false;
    private TextInputEditText bottomSheetCreateAccountShortName;
    private TextInputLayout bottomSheetCreateAccountShortNameLayout;
    private boolean bottomSheetCreateAccountShortNameOk = false;
    private TextInputEditText bottomSheetCreateAccountEmail;
    private TextInputLayout bottomSheetCreateAccountEmailLayout;
    private boolean bottomSheetCreateAccountEmailOk = false;
    private TextInputEditText bottomSheetCreateAccountPassword;
    private TextInputLayout bottomSheetCreateAccountPasswordLayout;
    private boolean bottomSheetCreateAccountPasswordOk = false;
    private TextInputEditText bottomSheetCreateAccountRePassword;
    private TextInputLayout bottomSheetCreateAccountRePasswordLayout;
    private boolean bottomSheetCreateAccountRePasswordOk = false;

    private TextInputEditText bottomSheetLoginLogin;
    private TextInputLayout bottomSheetLoginLoginLayout;
    private boolean bottomSheetLoginLoginOk = false;
    private TextInputEditText bottomSheetLoginPassword;
    private TextInputLayout bottomSheetLoginPasswordLayout;
    private boolean bottomSheetLoginPasswordOk = false;

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

        bottomSheetCreateAccountFirstName = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountFirstName);
        bottomSheetCreateAccountFirstNameLayout = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountFirstNameLayout);
        bottomSheetCreateAccountSecondName = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountSecondName);
        bottomSheetCreateAccountSecondNameLayout = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountSecondNameLayout);
        bottomSheetCreateAccountShortName = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortName);
        bottomSheetCreateAccountShortNameLayout = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortNameLayout);
        bottomSheetCreateAccountEmail = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountEmail);
        bottomSheetCreateAccountEmailLayout = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountEmailLayout);
        bottomSheetCreateAccountPassword = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountPassword);
        bottomSheetCreateAccountPasswordLayout = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountPasswordLayout);
        bottomSheetCreateAccountRePassword = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountRePassword);
        bottomSheetCreateAccountRePasswordLayout = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountRePasswordLayout);

        TextInputLayoutTextWatcher textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountFirstNameLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_FIRST_NAME_LAYOUT
        );
        bottomSheetCreateAccountFirstName.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountSecondNameLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_SECOND_NAME_LAYOUT
        );
        bottomSheetCreateAccountSecondName.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountSecondNameLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_SECOND_NAME_LAYOUT
        );
        bottomSheetCreateAccountSecondName.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountShortNameLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_SHORT_NAME_LAYOUT
        );
        bottomSheetCreateAccountShortName.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountEmailLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_EMAIL_LAYOUT
        );
        bottomSheetCreateAccountEmail.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountPasswordLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_PASSWORD_LAYOUT
        );
        bottomSheetCreateAccountPassword.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetCreateAccountRePasswordLayout,
                BOTTOM_SHEET_CREATE_ACCOUNT_RE_PASSWORD_LAYOUT
        );
        bottomSheetCreateAccountRePassword.addTextChangedListener(textInputLayoutTextWatcher);

        bottomSheetLoginLogin = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginLogin);
        bottomSheetLoginLoginLayout = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginLoginLayout);
        bottomSheetLoginPassword = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginPassword);
        bottomSheetLoginPasswordLayout = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginPasswordLayout);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetLoginLoginLayout,
                BOTTOM_SHEET_LOGIN_LOGIN_LAYOUT
        );
        bottomSheetLoginLogin.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetLoginPasswordLayout,
                BOTTOM_SHEET_LOGIN_PASSWORD_LAYOUT
        );
        bottomSheetLoginPassword.addTextChangedListener(textInputLayoutTextWatcher);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private boolean checkRegisterData(View v) {
        checkUserFirstName();
        checkUserSecondName();
        checkUserShortName();
        checkUserEmail();
        checkUserPassword();
        checkUserRePassword();
        return bottomSheetCreateAccountFirstNameOk &&
                bottomSheetCreateAccountSecondNameOk &&
                bottomSheetCreateAccountShortNameOk &&
                bottomSheetCreateAccountEmailOk &&
                bottomSheetCreateAccountPasswordOk &&
                bottomSheetCreateAccountRePasswordOk;
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
        TextInputLayout login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortNameLayout);
        login.setError(getResources().getString(
                R.string.not_free_login
        ));
        login.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.pink_700, null
        )));
        login.setErrorEnabled(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkCreateAccountLogin() {
        TextInputLayout login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountShortNameLayout);
        login.setHelperText(getResources().getString(
                R.string.free_login
        ));
        login.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.teal_200, null
        )));
        login.setHelperTextEnabled(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setErrorCreateAccountEmail() {
        TextInputLayout login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountEmailLayout);
        login.setHelperText(getResources().getString(
                R.string.not_free_email
        ));
        login.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.pink_700, null
        )));
        login.setHelperTextEnabled(true);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    public void setOkCreateAccountEmail() {
        TextInputLayout login = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountEmailLayout);
        login.setBoxStrokeColor(R.color.teal_200);
        login.setHelperText(getResources().getString(
                R.string.free_email
        ));
        login.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.teal_200, null
        )));
        login.setHelperTextEnabled(true);
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

    public boolean checkUserFirstName() {
        if (bottomSheetCreateAccountFirstName.getText().toString().equals("") || bottomSheetCreateAccountFirstName.getText().toString().contains(" ")) {
            bottomSheetCreateAccountFirstNameLayout.setError(getResources().getString(
                    R.string.error_first_name
            ));
            bottomSheetCreateAccountFirstNameOk = false;
            return false;
        } else {
            if (checkStringToAnotherChars(
                    bottomSheetCreateAccountFirstName.getText().toString(),
                    MainActivity.ALPHABET_RU
            )) {
                bottomSheetCreateAccountFirstNameLayout.setError(null);
                bottomSheetCreateAccountFirstNameOk = true;
                return true;
            } else {
                bottomSheetCreateAccountFirstNameLayout.setError(getResources().getString(
                        R.string.error_first_name
                ));
                bottomSheetCreateAccountFirstNameOk = false;
                return false;
            }
        }
    }

    public boolean checkUserSecondName() {
        if (bottomSheetCreateAccountSecondName.getText().toString().equals("") ||
                bottomSheetCreateAccountSecondName.getText().toString().contains(" ")) {
            bottomSheetCreateAccountSecondNameLayout.setError(getResources().getString(
                    R.string.error_second_name
            ));
            bottomSheetCreateAccountSecondNameOk = false;
            return false;
        } else {
            if (checkStringToAnotherChars(
                    bottomSheetCreateAccountSecondName.getText().toString(),
                    MainActivity.ALPHABET_RU
            )) {
                bottomSheetCreateAccountSecondNameLayout.setError(null);
                bottomSheetCreateAccountSecondNameOk = true;
                return true;
            } else {
                bottomSheetCreateAccountSecondNameLayout.setError(getResources().getString(
                        R.string.error_second_name
                ));
                bottomSheetCreateAccountSecondNameOk = false;
                return false;
            }
        }
    }

    public boolean checkUserShortName() {
        if (bottomSheetCreateAccountShortName.getText().toString().equals("") ||
                bottomSheetCreateAccountShortName.getText().toString().contains(" ")) {
            bottomSheetCreateAccountShortNameOk = false;
            return false;
        } else {
            if (checkStringToAnotherChars(
                    bottomSheetCreateAccountShortName.getText().toString(),
                    MainActivity.ALPHABET_EN_SMALL + MainActivity.DIGITS
            )) {
                apiServer.checkLoginToCreate(bottomSheetCreateAccountShortName.getText().toString());
                bottomSheetCreateAccountShortNameLayout.setError(null);
                bottomSheetCreateAccountShortNameOk = true;
                return true;
            } else {
                bottomSheetCreateAccountShortNameOk = false;
                return false;
            }
        }
    }

    public boolean checkUserEmail() {
        if (bottomSheetCreateAccountEmail.getText().toString().equals("") || !Patterns.EMAIL_ADDRESS.matcher(bottomSheetCreateAccountEmail.getText().toString()).matches()) {
            bottomSheetCreateAccountEmailLayout.setError(getResources().getString(
                    R.string.error_email
            ));
            bottomSheetCreateAccountEmailOk = false;
            return false;
        } else {
            apiServer.checkEmailToCreate(bottomSheetCreateAccountEmail.getText().toString());
            bottomSheetCreateAccountEmailLayout.setError(null);
            bottomSheetCreateAccountEmailOk = true;
            return true;
        }
    }

    public boolean checkUserPassword() {
        if (bottomSheetCreateAccountPassword.getText().toString().equals("") ||
                bottomSheetCreateAccountPassword.getText().toString().contains(" ")) {
            bottomSheetCreateAccountPasswordLayout.setError(getResources().getString(
                    R.string.error_password
            ));
            bottomSheetCreateAccountPasswordOk = false;
            return false;
        } else {
            bottomSheetCreateAccountEmailLayout.setError(null);
            bottomSheetCreateAccountPasswordOk = true;
            return true;
        }
    }

    public boolean checkUserRePassword() {
        if (!bottomSheetCreateAccountPassword.getText().toString().equals(bottomSheetCreateAccountRePassword.getText().toString())) {
            bottomSheetCreateAccountRePasswordLayout.setError(getResources().getString(
                    R.string.error_re_password
            ));
            bottomSheetCreateAccountRePasswordOk = false;
            return false;
        } else {
            if (bottomSheetCreateAccountRePassword.getText().toString().equals("") ||
                    bottomSheetCreateAccountRePassword.getText().toString().contains(" ")) {
                bottomSheetCreateAccountRePasswordLayout.setError(getResources().getString(
                        R.string.error_re_password
                ));
                bottomSheetCreateAccountRePasswordOk = false;
                return false;
            } else {
                bottomSheetCreateAccountRePasswordLayout.setError(null);
                bottomSheetCreateAccountRePasswordOk = true;
                return true;
            }
        }
    }

    public boolean checkUserLoginLogin() {
        if (bottomSheetLoginLogin.getText().toString().equals("")) {
            bottomSheetLoginLoginOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetLoginLogin.getText().toString(),
                MainActivity.ALPHABET_EN + MainActivity.DIGITS + "@"
        )) {
            bottomSheetLoginLoginLayout.setError(null);
            bottomSheetLoginLoginOk = true;
            return true;
        } else {
            bottomSheetLoginLoginLayout.setError(getResources().getString(
                    R.string.error_login
            ));
            bottomSheetLoginLoginOk = false;
            return false;
        }
    }

    public boolean checkUserLoginPassword() {
        if (bottomSheetLoginPassword.getText().toString().equals("")) {
            bottomSheetLoginPasswordOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetLoginPassword.getText().toString(),
                MainActivity.ALPHABET_EN + MainActivity.DIGITS + MainActivity.SYMBOLS
        )) {
            bottomSheetLoginPasswordLayout.setError(null);
            bottomSheetLoginPasswordOk = true;
            return true;
        } else {
            bottomSheetLoginPasswordLayout.setError(getResources().getString(
                    R.string.error_password
            ));
            bottomSheetLoginPasswordOk = false;
            return false;
        }
    }
}