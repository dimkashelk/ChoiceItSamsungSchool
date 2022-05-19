package com.example.choiceitsamsungschool.main_page;

import android.text.Editable;
import android.text.TextWatcher;

public class TextInputWatcher implements TextWatcher {
    private HomePage homePage;
    private boolean is_homePage = false;
    private FriendsPage friendsPage;
    private boolean is_friendsPage = false;
    private SearchPage searchPage;
    private boolean is_searchPage = false;
    private AddLine addLine;
    private boolean is_addLine = false;

    public TextInputWatcher(HomePage homePage) {
        this.homePage = homePage;
        is_homePage = true;
    }

    public TextInputWatcher(FriendsPage friendsPage) {
        this.friendsPage = friendsPage;
        is_friendsPage = true;
    }

    public TextInputWatcher(SearchPage searchPage) {
        this.searchPage = searchPage;
        is_searchPage = true;
    }

    public TextInputWatcher(AddLine addLine) {
        this.addLine = addLine;
        is_addLine = true;
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
        if (is_friendsPage) {
            friendsPage.filterFriends();
        }
        if (is_searchPage) {
            searchPage.search();
        }
        if (is_addLine) {
            addLine.checkTitle();
        }
    }
}
