package com.example.choiceitsamsungschool.welcome_page;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class TextInputLayoutTextWatcher implements TextWatcher {
    private WelcomePage welcomeActivity;
    private TextInputLayout layout;
    private String layout_type;

    public TextInputLayoutTextWatcher(WelcomePage welcomeActivity, TextInputLayout layout, String layout_type) {
        this.welcomeActivity = welcomeActivity;
        this.layout = layout;
        this.layout_type = layout_type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        switch (layout_type) {
            case WelcomePage.BOTTOM_SHEET_CREATE_ACCOUNT_FIRST_NAME_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserFirstName());
                break;
            case WelcomePage.BOTTOM_SHEET_CREATE_ACCOUNT_SECOND_NAME_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserSecondName());
                break;
            case WelcomePage.BOTTOM_SHEET_CREATE_ACCOUNT_SHORT_NAME_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserShortName(false));
                break;
            case WelcomePage.BOTTOM_SHEET_CREATE_ACCOUNT_EMAIL_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserEmail(false));
                break;
            case WelcomePage.BOTTOM_SHEET_CREATE_ACCOUNT_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserPassword());
                if (!welcomeActivity.checkUserRePassword(true)) {
                    welcomeActivity.setErrorCreateAccountRePassword(true);
                } else {
                    welcomeActivity.setOkCreateAccountRePassword();
                }
                break;
            case WelcomePage.BOTTOM_SHEET_CREATE_ACCOUNT_RE_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserRePassword(true));
                break;
            case WelcomePage.BOTTOM_SHEET_LOGIN_LOGIN_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserLoginLogin());
                break;
            case WelcomePage.BOTTOM_SHEET_LOGIN_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkUserLoginPassword());
                break;
            case WelcomePage.BOTTOM_SHEET_FORGOT_PASSWORD_EMAIL_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkForgotEmail(false));
                break;
            case WelcomePage.BOTTOM_SHEET_VERIFY_CODE_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkVerifyPassword());
                if (!welcomeActivity.checkVerifyRePassword(true)) {
                    welcomeActivity.setErrorVerifyCodeRePassword(true);
                } else {
                    welcomeActivity.setOkVerifyCodeRePassword();
                }
                break;
            case WelcomePage.BOTTOM_SHEET_VERIFY_CODE_RE_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!welcomeActivity.checkVerifyRePassword(true));
                break;
        }
        if (s.toString().equals("")) {
            layout.setErrorEnabled(false);
        }
    }
}
