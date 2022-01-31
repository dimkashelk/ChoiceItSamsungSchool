package com.example.choiceitsamsungschool.welcome_page;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.choiceitsamsungschool.MainActivity;
import com.google.android.material.textfield.TextInputLayout;

public class TextInputLayoutTextWatcher implements TextWatcher {
    private MainActivity mainActivity;
    private TextInputLayout layout;
    private String layout_type;

    TextInputLayoutTextWatcher(MainActivity mainActivity, TextInputLayout layout, String layout_type) {
        this.mainActivity = mainActivity;
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
            case MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_FIRST_NAME_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserFirstName());
                break;
            case MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_SECOND_NAME_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserSecondName());
                break;
            case MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_SHORT_NAME_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserShortName(false));
                break;
            case MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_EMAIL_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserEmail(false));
                break;
            case MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserPassword());
                if (!mainActivity.checkUserRePassword(true)) {
                    mainActivity.setErrorCreateAccountRePassword(true);
                } else {
                    mainActivity.setOkCreateAccountRePassword();
                }
                break;
            case MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_RE_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserRePassword(true));
                break;
            case MainActivity.BOTTOM_SHEET_LOGIN_LOGIN_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserLoginLogin());
                break;
            case MainActivity.BOTTOM_SHEET_LOGIN_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkUserLoginPassword());
                break;
            case MainActivity.BOTTOM_SHEET_FORGOT_PASSWORD_EMAIL_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkForgotEmail(false));
                break;
            case MainActivity.BOTTOM_SHEET_VERIFY_CODE_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkVerifyPassword());
                if (!mainActivity.checkVerifyRePassword(true)) {
                    mainActivity.setErrorVerifyCodeRePassword(true);
                } else {
                    mainActivity.setOkVerifyCodeRePassword();
                }
                break;
            case MainActivity.BOTTOM_SHEET_VERIFY_CODE_RE_PASSWORD_LAYOUT:
                layout.setErrorEnabled(!mainActivity.checkVerifyRePassword(true));
                break;
        }
        if (s.toString().equals("")) {
            layout.setErrorEnabled(false);
        }
    }
}
