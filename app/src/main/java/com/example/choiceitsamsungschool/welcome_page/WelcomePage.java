package com.example.choiceitsamsungschool.welcome_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Vector;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class WelcomePage extends AppCompatActivity {
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
    public final static String BOTTOM_SHEET_FORGOT_PASSWORD_EMAIL_LAYOUT = "bottomSheetForgotPasswordLayout";
    public final static String BOTTOM_SHEET_VERIFY_CODE_VERIFY_CODE_LAYOUT = "bottomSheetVerifyCodeLayout";
    public final static String BOTTOM_SHEET_VERIFY_CODE_PASSWORD_LAYOUT = "bottomSheetForgotPasswordPasswordLayout";
    public final static String BOTTOM_SHEET_VERIFY_CODE_RE_PASSWORD_LAYOUT = "bottomSheetForgotPasswordRePasswordLayout";

    private SharedPreferences authorize_data;
    private SharedPreferences.Editor editor_authorize_data;

    private APIServer apiServer;

    private View bottomSheetViewCreateAccount;
    private View bottomSheetViewLogin;
    private View bottomSheetViewForgotPassword;
    private View bottomSheetViewVerifyCode;

    private BottomSheetDialog bottomSheetDialogCreateAccount;
    private BottomSheetDialog bottomSheetDialogLogin;
    private BottomSheetDialog bottomSheetDialogForgotPassword;
    private BottomSheetDialog bottomSheetDialogVerifyCode;

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
    private LinearLayout bottomSheetLoginForgotPasswordLayout;
    private TextView bottomSheetLoginForgotPassword;

    private TextInputLayout bottomSheetForgotPasswordEmailLayout;
    private TextInputEditText bottomSheetForgotPasswordEmail;
    private boolean bottomSheetForgotPasswordEmailOk = false;

    private TextInputLayout bottomSheetVerifyCodeLayout;
    private TextInputEditText bottomSheetVerifyCodeCode;
    private boolean bottomSheetVerifyCodeCodeOk = false;
    private TextInputLayout bottomSheetVerifyCodePasswordLayout;
    private TextInputEditText bottomSheetVerifyCodePassword;
    private boolean bottomSheetVerifyCodePasswordOk = false;
    private TextInputLayout bottomSheetVerifyCodeRePasswordLayout;
    private TextInputEditText bottomSheetVerifyCodeRePassword;
    private boolean bottomSheetVerifyCodeRePasswordOk = false;

    private CircularProgressButton bottomSheetLoginButton;
    private CircularProgressButton bottomSheetCreateAccountButton;
    private CircularProgressButton bottomSheetForgotPasswordButton;
    private CircularProgressButton bottomSheetVerifyCodeButton;

    private Animation shake;
    private Vibrator vibrator;

    private String emailToChangePassword = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        authorize_data = getSharedPreferences(PREFERENCES_AUTHORIZE_DATA, Context.MODE_PRIVATE);
        editor_authorize_data = authorize_data.edit();

        apiServer = new APIServer(this);

        initializeUI();
    }

    private void initializeUI() {
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        bottomSheetDialogCreateAccount = new BottomSheetDialog(
                WelcomePage.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetViewCreateAccount = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_create_account_activity,
                (LinearLayout) findViewById(R.id.bottomSheetCreateAccount)
        );

        bottomSheetDialogCreateAccount.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                freeCreateAccountData();
            }
        });

        bottomSheetCreateAccountButton = bottomSheetViewCreateAccount.findViewById(R.id.bottomSheetCreateAccountButton);
        bottomSheetCreateAccountButton.setOnClickListener(
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
                WelcomePage.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetViewLogin = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_login_activity,
                (LinearLayout) findViewById(R.id.bottomSheetLogin)
        );

        bottomSheetLoginButton = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginButton);
        bottomSheetLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLoginData(bottomSheetViewLogin)) {
                    bottomSheetDialogLogin.dismiss();
                }
            }
        });

        bottomSheetDialogLogin.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                freeLoginData();
            }
        });

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

        bottomSheetLoginForgotPasswordLayout = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginForgotPasswordLayout);
        bottomSheetLoginForgotPassword = bottomSheetViewLogin.findViewById(R.id.bottomSheetLoginForgotPassword);
        bottomSheetLoginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogLogin.dismiss();
                bottomSheetDialogForgotPassword.show();
            }
        });


        bottomSheetDialogForgotPassword = new BottomSheetDialog(
                WelcomePage.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetViewForgotPassword = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_forgot_password_activity,
                (LinearLayout) findViewById(R.id.bottomSheetForgotPassword)
        );
        bottomSheetDialogForgotPassword.setContentView(bottomSheetViewForgotPassword);

        bottomSheetDialogForgotPassword.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                freeForgotPasswordData();
            }
        });

        bottomSheetDialogVerifyCode = new BottomSheetDialog(
                WelcomePage.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetViewVerifyCode = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.bottom_sheet_verify_code,
                (LinearLayout) findViewById(R.id.bottomSheetVerifyCode)
        );
        bottomSheetDialogVerifyCode.setContentView(bottomSheetViewVerifyCode);

        bottomSheetDialogVerifyCode.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                freeVerifyCodeData();
            }
        });

        bottomSheetVerifyCodeButton = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodeButton);
        bottomSheetVerifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVerifyCode();
            }
        });

        bottomSheetForgotPasswordEmailLayout = bottomSheetViewForgotPassword.findViewById(R.id.bottomSheetForgotPasswordLayout);
        bottomSheetForgotPasswordEmail = bottomSheetViewForgotPassword.findViewById(R.id.bottomSheetForgotPasswordEmail);

        bottomSheetVerifyCodeLayout = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodeLayout);
        bottomSheetVerifyCodeCode = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodeCode);
        bottomSheetVerifyCodePasswordLayout = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodePasswordLayout);
        bottomSheetVerifyCodePassword = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodePassword);
        bottomSheetVerifyCodeRePasswordLayout = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodeRePasswordLayout);
        bottomSheetVerifyCodeRePassword = bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodeRePassword);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetForgotPasswordEmailLayout,
                BOTTOM_SHEET_FORGOT_PASSWORD_EMAIL_LAYOUT
        );
        bottomSheetForgotPasswordEmail.addTextChangedListener(textInputLayoutTextWatcher);

        bottomSheetForgotPasswordButton = bottomSheetViewForgotPassword.findViewById(R.id.bottomSheetForgotPasswordButton);
        bottomSheetForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForgotEmail(true);
            }
        });

        bottomSheetViewForgotPassword.findViewById(R.id.bottomSheetForgotPasswordCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogForgotPassword.dismiss();
                bottomSheetDialogCreateAccount.show();
            }
        });

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetVerifyCodeLayout,
                BOTTOM_SHEET_VERIFY_CODE_VERIFY_CODE_LAYOUT
        );
        bottomSheetVerifyCodeCode.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetVerifyCodePasswordLayout,
                BOTTOM_SHEET_VERIFY_CODE_PASSWORD_LAYOUT
        );
        bottomSheetVerifyCodePassword.addTextChangedListener(textInputLayoutTextWatcher);

        textInputLayoutTextWatcher = new TextInputLayoutTextWatcher(
                this,
                bottomSheetVerifyCodeRePasswordLayout,
                BOTTOM_SHEET_VERIFY_CODE_RE_PASSWORD_LAYOUT
        );
        bottomSheetVerifyCodeRePassword.addTextChangedListener(textInputLayoutTextWatcher);

        bottomSheetViewVerifyCode.findViewById(R.id.bottomSheetVerifyCodeLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogVerifyCode.dismiss();
                bottomSheetDialogLogin.show();
            }
        });
    }

    private void freeVerifyCodeData() {
        bottomSheetVerifyCodeCode.setText("");
        bottomSheetVerifyCodePassword.setText("");
        bottomSheetVerifyCodeRePassword.setText("");

        setEnable(bottomSheetVerifyCodeCode, true);
    }

    private void freeForgotPasswordData() {
        bottomSheetForgotPasswordEmail.setText("");
        setEnable(bottomSheetForgotPasswordEmail, true);
    }

    private void freeLoginData() {
        bottomSheetLoginLogin.setText("");
        bottomSheetLoginPassword.setText("");
    }

    private void freeCreateAccountData() {
        bottomSheetCreateAccountFirstName.setText("");
        bottomSheetCreateAccountSecondName.setText("");
        bottomSheetCreateAccountShortName.setText("");
        bottomSheetCreateAccountEmail.setText("");
        bottomSheetCreateAccountPassword.setText("");
        bottomSheetCreateAccountRePassword.setText("");

        setEnable(bottomSheetCreateAccountFirstName, true);
        setEnable(bottomSheetCreateAccountSecondName, true);
        setEnable(bottomSheetCreateAccountShortName, true);
        setEnable(bottomSheetCreateAccountEmail, true);
        setEnable(bottomSheetCreateAccountPassword, true);
        setEnable(bottomSheetCreateAccountRePassword, true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private boolean checkRegisterData(View v) {
        bottomSheetCreateAccountButton.startAnimation();
        checkUserFirstName();
        checkUserSecondName();
        checkUserPassword();
        checkUserRePassword();
        checkUserLoginEmail();
        return false;
    }

    private void checkUserLoginEmail() {
        if (!bottomSheetCreateAccountEmail.getText().toString().equals("") &&
                !bottomSheetCreateAccountShortName.getText().toString().equals("")) {
            apiServer.registration(
                    bottomSheetCreateAccountFirstName.getText().toString(),
                    bottomSheetCreateAccountSecondName.getText().toString(),
                    bottomSheetCreateAccountShortName.getText().toString(),
                    bottomSheetCreateAccountEmail.getText().toString(),
                    bottomSheetCreateAccountPassword.getText().toString()
            );
        } else {
            if (bottomSheetCreateAccountEmail.getText().toString().equals("")) {
                setErrorCreateAccountEmail();
            }
            if (bottomSheetCreateAccountShortName.getText().toString().equals("")) {
                setErrorCreateAccountLogin();
            }
        }
    }

    private boolean checkLoginData(View v) {
        checkUserLoginLogin();
        checkUserLoginPassword();
        if (bottomSheetLoginLoginOk && bottomSheetLoginPasswordOk) {
            bottomSheetLoginButton.startAnimation();
            if (Patterns.EMAIL_ADDRESS.matcher(
                    bottomSheetLoginLogin.getText().toString()
            ).matches()) {
                apiServer.checkUserEmailPassword(
                        bottomSheetLoginLogin.getText().toString(),
                        bottomSheetLoginPassword.getText().toString()
                );
            } else {
                apiServer.checkUserLoginPassword(
                        bottomSheetLoginLogin.getText().toString(),
                        bottomSheetLoginPassword.getText().toString()
                );
            }
        }
        return false;
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
        bottomSheetCreateAccountShortNameLayout.setError(getResources().getString(
                R.string.not_free_login
        ));
        bottomSheetCreateAccountShortNameLayout.setErrorEnabled(true);
        bottomSheetCreateAccountShortNameLayout.startAnimation(shake);
        vibrate(250);

        bottomSheetCreateAccountShortNameOk = false;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkCreateAccountLogin() {
        bottomSheetCreateAccountShortNameLayout.setHelperText(getResources().getString(
                R.string.free_login
        ));
        bottomSheetCreateAccountShortNameLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.teal_200, null
        )));
        bottomSheetCreateAccountShortNameLayout.setHelperTextEnabled(true);

        bottomSheetCreateAccountShortNameOk = true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setErrorCreateAccountEmail() {
        bottomSheetCreateAccountEmailLayout.setError(getResources().getString(
                R.string.not_free_email
        ));
        bottomSheetCreateAccountEmailLayout.setErrorEnabled(true);
        bottomSheetCreateAccountEmailLayout.startAnimation(shake);
        vibrate(250);

        bottomSheetCreateAccountEmailOk = false;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    public void setOkCreateAccountEmail() {
        bottomSheetCreateAccountEmailLayout.setHelperText(getResources().getString(
                R.string.free_email
        ));
        bottomSheetCreateAccountEmailLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.teal_200, null
        )));
        bottomSheetCreateAccountEmailLayout.setHelperTextEnabled(true);

        bottomSheetCreateAccountEmailOk = true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setOkLoginUserLoginPassword(String login_string, String token) {
        saveToken(login_string, token);

        bottomSheetLoginLoginOk = true;
        bottomSheetLoginPasswordOk = true;

        bottomSheetLoginButton.revertAnimation();

        bottomSheetDialogLogin.dismiss();

        MainActivity.get().login();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setErrorLoginUserLoginPassword() {
        bottomSheetLoginLoginLayout.setError(getResources().getString(
                R.string.wrong_login_data
        ));
        bottomSheetLoginLoginLayout.setErrorEnabled(true);
        bottomSheetLoginLoginLayout.startAnimation(shake);
        vibrate(250);

        bottomSheetLoginPasswordLayout.setError(getResources().getString(
                R.string.wrong_login_data
        ));
        bottomSheetLoginPasswordLayout.setErrorEnabled(true);
        bottomSheetLoginPasswordLayout.startAnimation(shake);
        vibrate(250);

        bottomSheetLoginButton.revertAnimation();
    }

    private void saveToken(String login_string, String token) {
        editor_authorize_data.putString(login_string, token);
        editor_authorize_data.apply();
    }

    public boolean checkUserFirstName() {
        if (bottomSheetCreateAccountFirstName.getText().toString().equals("")) {
            bottomSheetCreateAccountFirstNameOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetCreateAccountFirstName.getText().toString(),
                WelcomePage.ALPHABET_RU
        )) {
            bottomSheetCreateAccountFirstNameLayout.setError(null);
            bottomSheetCreateAccountFirstNameOk = true;
            return true;
        } else {
            bottomSheetCreateAccountFirstNameLayout.setError(getResources().getString(
                    R.string.error_first_name
            ));
            bottomSheetCreateAccountFirstNameOk = false;
            bottomSheetCreateAccountFirstNameLayout.startAnimation(shake);
            vibrate(250);
            return false;
        }
    }

    public boolean checkUserSecondName() {
        if (bottomSheetCreateAccountSecondName.getText().toString().equals("")) {
            bottomSheetCreateAccountSecondNameOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetCreateAccountSecondName.getText().toString(),
                WelcomePage.ALPHABET_RU
        )) {
            bottomSheetCreateAccountSecondNameLayout.setError(null);
            bottomSheetCreateAccountSecondNameOk = true;
            return true;
        } else {
            bottomSheetCreateAccountSecondNameLayout.setError(getResources().getString(
                    R.string.error_second_name
            ));
            bottomSheetCreateAccountSecondNameOk = false;
            bottomSheetCreateAccountSecondNameLayout.startAnimation(shake);
            vibrate(250);
            return false;
        }
    }

    public boolean checkUserShortName() {
        if (bottomSheetCreateAccountShortName.getText().toString().equals("")) {
            bottomSheetCreateAccountShortNameOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetCreateAccountShortName.getText().toString(),
                WelcomePage.ALPHABET_EN_SMALL + WelcomePage.DIGITS
        )) {
            apiServer.checkLoginToCreate(bottomSheetCreateAccountShortName.getText().toString());
            bottomSheetCreateAccountShortNameLayout.setError(null);
            bottomSheetCreateAccountShortNameOk = true;
            return true;
        } else {
            bottomSheetCreateAccountShortNameLayout.setError(getResources().getString(
                    R.string.error_login
            ));
            bottomSheetCreateAccountShortNameOk = false;
            bottomSheetCreateAccountShortNameLayout.startAnimation(shake);
            vibrate(250);
            return false;
        }
    }

    public boolean checkUserShortName(boolean checkIsFree) {
        if (bottomSheetCreateAccountShortName.getText().toString().equals("")) {
            bottomSheetCreateAccountShortNameOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetCreateAccountShortName.getText().toString(),
                WelcomePage.ALPHABET_EN_SMALL + WelcomePage.DIGITS
        )) {
            if (checkIsFree) {
                apiServer.checkLoginToCreate(bottomSheetCreateAccountShortName.getText().toString());
            }
            bottomSheetCreateAccountShortNameLayout.setError(null);
            bottomSheetCreateAccountShortNameOk = true;
            return true;
        } else {
            bottomSheetCreateAccountShortNameLayout.setError(getResources().getString(
                    R.string.error_login
            ));
            bottomSheetCreateAccountShortNameOk = false;
            bottomSheetCreateAccountShortNameLayout.startAnimation(shake);
            vibrate(250);
            return false;
        }
    }

    public boolean checkUserEmail() {
        if (bottomSheetCreateAccountEmail.getText().toString().equals("")) {
            bottomSheetCreateAccountEmailOk = false;
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(bottomSheetCreateAccountEmail.getText().toString()).matches()) {
            bottomSheetCreateAccountEmailLayout.setError(getResources().getString(
                    R.string.error_email
            ));
            bottomSheetCreateAccountEmailLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetCreateAccountEmailOk = false;
            return false;
        } else {
            apiServer.checkEmailToCreate(bottomSheetCreateAccountEmail.getText().toString());
            bottomSheetCreateAccountEmailLayout.setError(null);
            bottomSheetCreateAccountEmailOk = true;
            return true;
        }
    }

    public boolean checkUserEmail(boolean checkIsFree) {
        if (bottomSheetCreateAccountEmail.getText().toString().equals("")) {
            bottomSheetCreateAccountEmailOk = false;
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(bottomSheetCreateAccountEmail.getText().toString()).matches()) {
            bottomSheetCreateAccountEmailLayout.setError(getResources().getString(
                    R.string.error_email
            ));
            bottomSheetCreateAccountEmailLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetCreateAccountEmailOk = false;
            return false;
        } else {
            if (checkIsFree) {
                apiServer.checkEmailToCreate(bottomSheetCreateAccountEmail.getText().toString());
            }
            bottomSheetCreateAccountEmailLayout.setError(null);
            bottomSheetCreateAccountEmailOk = true;
            return true;
        }
    }

    public boolean checkUserPassword() {
        if (bottomSheetCreateAccountPassword.getText().toString().equals("") ||
                bottomSheetCreateAccountPassword.getText().toString().length() < 8) {
            bottomSheetCreateAccountPasswordLayout.setError(getResources().getString(
                    R.string.error_password
            ));
            bottomSheetCreateAccountPasswordLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetCreateAccountPasswordOk = false;
            return false;
        }
        if (!checkStringToAnotherChars(
                bottomSheetCreateAccountPassword.getText().toString(),
                WelcomePage.ALPHABET_EN + WelcomePage.DIGITS + WelcomePage.SYMBOLS
        )) {
            bottomSheetCreateAccountPasswordLayout.setError(getResources().getString(
                    R.string.error_password
            ));
            bottomSheetCreateAccountPasswordLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetCreateAccountPasswordOk = false;
            return false;
        } else {
            bottomSheetCreateAccountEmailLayout.setError(null);
            bottomSheetCreateAccountPasswordOk = true;
            return true;
        }
    }

    public boolean checkUserRePassword() {
        if (bottomSheetCreateAccountRePassword.getText().toString().equals("")) {
            bottomSheetCreateAccountRePasswordOk = false;
            return false;
        }
        if (!bottomSheetCreateAccountPassword.getText().toString().equals(
                bottomSheetCreateAccountRePassword.getText().toString()
        )) {
            bottomSheetCreateAccountRePasswordLayout.setError(getResources().getString(
                    R.string.error_re_password
            ));
            bottomSheetCreateAccountRePasswordLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetCreateAccountRePasswordOk = false;
            return false;
        } else {
            bottomSheetCreateAccountRePasswordLayout.setError(null);
            bottomSheetCreateAccountRePasswordOk = true;
            return true;
        }
    }

    public boolean checkUserRePassword(boolean passwordInput) {
        if (bottomSheetCreateAccountRePassword.getText().toString().equals("")) {
            bottomSheetCreateAccountRePasswordOk = false;
            return false;
        }
        if (!bottomSheetCreateAccountPassword.getText().toString().equals(
                bottomSheetCreateAccountRePassword.getText().toString()
        )) {
            bottomSheetCreateAccountRePasswordLayout.setError(getResources().getString(
                    R.string.error_re_password
            ));
            if (!passwordInput) {
                bottomSheetCreateAccountRePasswordLayout.startAnimation(shake);
                vibrate(250);
            }
            bottomSheetCreateAccountRePasswordOk = false;
            return false;
        } else {
            bottomSheetCreateAccountRePasswordLayout.setError(null);
            bottomSheetCreateAccountRePasswordOk = true;
            return true;
        }
    }

    public boolean checkUserLoginLogin() {
        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(
                bottomSheetLoginLogin.getText().toString()
        ).matches();
        if (isEmail) {
            bottomSheetLoginLoginOk = true;
            return true;
        }
        if (bottomSheetLoginLogin.getText().toString().equals("")) {
            bottomSheetLoginLoginOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetLoginLogin.getText().toString(),
                WelcomePage.ALPHABET_EN + WelcomePage.DIGITS + "@"
        )) {
            bottomSheetLoginLoginLayout.setError(null);
            bottomSheetLoginLoginOk = true;
            return true;
        } else {
            bottomSheetLoginLoginLayout.setError(
                    getResources().getString(R.string.error_login) +
                            '\n' +
                            getResources().getString(R.string.error_email)
            );
            bottomSheetLoginLoginLayout.startAnimation(shake);
            vibrate(250);
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
                WelcomePage.ALPHABET_EN + WelcomePage.DIGITS + WelcomePage.SYMBOLS
        )) {
            bottomSheetLoginPasswordLayout.setError(null);
            bottomSheetLoginPasswordOk = true;
            return true;
        } else {
            bottomSheetLoginPasswordLayout.setError(getResources().getString(
                    R.string.error_password
            ));
            bottomSheetLoginPasswordLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetLoginPasswordOk = false;
            return false;
        }
    }

    public void setErrorCreateAccountRePassword() {
        bottomSheetCreateAccountRePasswordLayout.setError(getResources().getString(
                R.string.error_re_password
        ));
        bottomSheetCreateAccountRePasswordLayout.setErrorEnabled(true);
        bottomSheetCreateAccountRePasswordLayout.startAnimation(shake);
        vibrate(250);
    }

    public void setErrorCreateAccountRePassword(boolean passwordInput) {
        bottomSheetCreateAccountRePasswordLayout.setError(getResources().getString(
                R.string.error_re_password
        ));
        bottomSheetCreateAccountRePasswordLayout.setErrorEnabled(true);
        if (!passwordInput) {
            bottomSheetCreateAccountRePasswordLayout.startAnimation(shake);
            vibrate(250);
        }
    }

    public void setOkCreateAccountRePassword() {
        bottomSheetCreateAccountRePasswordLayout.setError(null);
        bottomSheetCreateAccountRePasswordLayout.setErrorEnabled(false);
    }

    private void vibrate(int milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(milliseconds);
        }
    }

    public void endCheckRegisterData(boolean isFreeEmail, boolean isFreeLogin, String token) {
        if (isFreeEmail) {
            setOkCreateAccountEmail();
        } else {
            setErrorCreateAccountEmail();
        }
        if (isFreeLogin) {
            setOkCreateAccountLogin();
        } else {
            setErrorCreateAccountLogin();
        }
        bottomSheetCreateAccountButton.revertAnimation();
        if (bottomSheetCreateAccountFirstNameOk &&
                bottomSheetCreateAccountSecondNameOk &&
                bottomSheetCreateAccountShortNameOk &&
                bottomSheetCreateAccountEmailOk &&
                bottomSheetCreateAccountPasswordOk &&
                bottomSheetCreateAccountRePasswordOk) {
            bottomSheetDialogCreateAccount.dismiss();
            Toast.makeText(this, token, Toast.LENGTH_LONG).show();
            // TODO: saveToken(bottomSheetCreateAccountShortName.getText().toString(), token);
        }
    }

    public boolean checkForgotEmail(boolean findEmail) {
        if (bottomSheetForgotPasswordEmail.getText().toString().equals("")) {
            bottomSheetForgotPasswordEmailOk = false;
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(bottomSheetForgotPasswordEmail.getText().toString()).matches()) {
            bottomSheetForgotPasswordEmailLayout.setError(getResources().getString(
                    R.string.error_email
            ));
            bottomSheetForgotPasswordEmailLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetForgotPasswordEmailOk = false;
            return false;
        } else {
            if (findEmail) {
                bottomSheetForgotPasswordButton.startAnimation();
                apiServer.findEmail(
                        bottomSheetForgotPasswordEmail.getText().toString()
                );
            }
            bottomSheetForgotPasswordEmailLayout.setError(null);
            bottomSheetForgotPasswordEmailOk = true;
            return true;
        }
    }

    public void foundEmailForForgotPassword() {
        bottomSheetForgotPasswordButton.revertAnimation();

        bottomSheetForgotPasswordEmailOk = true;

        emailToChangePassword = bottomSheetForgotPasswordEmail.getText().toString();

        setEnable(bottomSheetForgotPasswordEmail, false);
        setEnable(bottomSheetVerifyCodeCode, true);

        bottomSheetDialogForgotPassword.dismiss();
        bottomSheetDialogVerifyCode.show();
    }

    public void notFoundEmailForForgotPassword() {
        bottomSheetForgotPasswordButton.revertAnimation();

        bottomSheetForgotPasswordEmailLayout.setError(getResources().getString(
                R.string.forgot_password_error
        ));
        bottomSheetForgotPasswordEmailLayout.setErrorEnabled(true);
        bottomSheetForgotPasswordEmailOk = false;
    }

    private void checkVerifyCode() {
        if (bottomSheetVerifyCodePasswordOk && bottomSheetVerifyCodeRePasswordOk
                && !bottomSheetVerifyCodeCode.getText().toString().equals("")) {
            bottomSheetVerifyCodeButton.startAnimation();

            setEnable(bottomSheetVerifyCodeCode, false);

            apiServer.checkVerifyCode(
                    emailToChangePassword,
                    bottomSheetVerifyCodeCode.getText().toString(),
                    bottomSheetVerifyCodePassword.getText().toString()
            );
        } else {
            bottomSheetVerifyCodePasswordLayout.startAnimation(shake);
            bottomSheetVerifyCodeRePasswordLayout.startAnimation(shake);
        }
    }

    public void okVerifyCode(String token) {
        bottomSheetForgotPasswordButton.revertAnimation();

        bottomSheetVerifyCodeCodeOk = true;

        bottomSheetVerifyCodeLayout.setHelperText(getResources().getString(
                R.string.ok_verify_code
        ));
        bottomSheetVerifyCodeLayout.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(
                R.color.teal_200, null
        )));
        bottomSheetVerifyCodeLayout.setHelperTextEnabled(true);

        bottomSheetDialogVerifyCode.dismiss();

        // TODO: saveToken(bottomSheetCreateAccountShortName.getText().toString(), token);
    }

    private void setEnable(EditText editText, boolean enable) {
        editText.setClickable(enable);
        editText.setCursorVisible(enable);
        editText.setFocusable(enable);
        editText.setFocusableInTouchMode(enable);
    }

    public void wrongVerifyCode() {
        bottomSheetForgotPasswordButton.revertAnimation();

        setEnable(bottomSheetVerifyCodeCode, true);
        setEnable(bottomSheetForgotPasswordEmail, true);

        bottomSheetVerifyCodeCodeOk = false;

        bottomSheetVerifyCodeLayout.setError(getResources().getString(
                R.string.wrong_verify_code
        ));
        bottomSheetVerifyCodeLayout.setErrorEnabled(true);
        bottomSheetVerifyCodeLayout.startAnimation(shake);

        bottomSheetForgotPasswordButton.setText(getResources().getString(
                R.string.get_verify_code
        ));
    }

    public boolean checkVerifyPassword() {
        if (bottomSheetVerifyCodePassword.getText().toString().equals("")) {
            bottomSheetVerifyCodePasswordOk = false;
            return false;
        }
        if (checkStringToAnotherChars(
                bottomSheetVerifyCodePassword.getText().toString(),
                WelcomePage.ALPHABET_EN + WelcomePage.DIGITS + WelcomePage.SYMBOLS
        )) {
            bottomSheetVerifyCodePasswordLayout.setError(null);
            bottomSheetVerifyCodePasswordOk = true;
            return true;
        } else {
            bottomSheetVerifyCodePasswordLayout.setError(getResources().getString(
                    R.string.error_password
            ));
            bottomSheetVerifyCodePasswordLayout.startAnimation(shake);
            vibrate(250);
            bottomSheetLoginPasswordOk = false;
            return false;
        }
    }

    public boolean checkVerifyRePassword(boolean passwordInput) {
        if (bottomSheetVerifyCodeRePassword.getText().toString().equals("")) {
            bottomSheetVerifyCodeRePasswordOk = false;
            return false;
        }
        if (!bottomSheetVerifyCodePassword.getText().toString().equals(
                bottomSheetVerifyCodeRePassword.getText().toString()
        )) {
            bottomSheetVerifyCodeRePasswordLayout.setError(getResources().getString(
                    R.string.error_re_password
            ));
            if (!passwordInput) {
                bottomSheetVerifyCodeRePasswordLayout.startAnimation(shake);
                vibrate(250);
            }
            bottomSheetVerifyCodeRePasswordOk = false;
            return false;
        } else {
            bottomSheetVerifyCodeRePasswordLayout.setError(null);
            bottomSheetVerifyCodeRePasswordOk = true;
            return true;
        }
    }

    public void setErrorVerifyCodeRePassword(boolean passwordInput) {
        bottomSheetVerifyCodeRePasswordLayout.setError(getResources().getString(
                R.string.error_re_password
        ));
        bottomSheetVerifyCodeRePasswordLayout.setErrorEnabled(true);
        if (!passwordInput) {
            bottomSheetVerifyCodeRePasswordLayout.startAnimation(shake);
            vibrate(250);
        }
    }

    public void setOkVerifyCodeRePassword() {
        bottomSheetVerifyCodeRePasswordLayout.setError(null);
        bottomSheetVerifyCodeRePasswordLayout.setErrorEnabled(false);
    }

    public void stopLoading() {
        bottomSheetCreateAccountButton.revertAnimation();
    }
}
