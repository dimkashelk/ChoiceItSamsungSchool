package com.example.choiceitsamsungschool;

import android.text.Editable;
import android.text.TextWatcher;

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
        layout.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (layout_type.equals(MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_FIRST_NAME_LAYOUT)) {
            if (!mainActivity.checkUserFirstName()) {
                layout.setErrorEnabled(true);
            }
        } else if (layout_type.equals(MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_SECOND_NAME_LAYOUT)) {
            if (!mainActivity.checkUserSecondName()) {
                layout.setErrorEnabled(true);
            }
        } else if (layout_type.equals(MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_SHORT_NAME_LAYOUT)) {
            if (!mainActivity.checkUserShortName()) {
                layout.setErrorEnabled(true);
            }
        } else if (layout_type.equals(MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_PASSWORD_LAYOUT)) {
            if (!mainActivity.checkUserPassword()) {
                layout.setErrorEnabled(true);
            }
        } else if (layout_type.equals(MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_RE_PASSWORD_LAYOUT)) {
            if (!mainActivity.checkUserRePassword()) {
                layout.setErrorEnabled(true);
            }
        } else if (layout_type.equals(MainActivity.BOTTOM_SHEET_CREATE_ACCOUNT_EMAIL_LAYOUT)) {
            if (!mainActivity.checkUserEmail()) {
                layout.setErrorEnabled(true);
            }
        }
        if (s.toString().equals("")) {
            layout.setErrorEnabled(false);
        }
    }
}
