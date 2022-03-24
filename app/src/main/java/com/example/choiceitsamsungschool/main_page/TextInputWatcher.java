package com.example.choiceitsamsungschool.main_page;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

public class TextInputWatcher implements TextWatcher {
    private HomePage homePage;
    private boolean is_homePage = false;

    public TextInputWatcher(HomePage homePage) {
        this.homePage = homePage;
        is_homePage = true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (is_homePage) {
            homePage.filterFriends();
        }
    }
}
