package com.example.choiceitsamsungschool;


import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class APIServer {
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";

    public static final String URL = "https://example.com/api/";
    public static final String CHECK_LOGIN = "check_login";
    public static final String CHECK_EMAIL = "check_email";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private MainActivity mainActivity;

    APIServer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        client = new OkHttpClient();
    }

    public void checkLoginToCreate(String login) {
        CheckUserLoginEmail checker = new CheckUserLoginEmail();
        checker.setApiServer(this);
        checker.execute(login, APIServer.LOGIN);
    }

    public void freeLogin() {
        mainActivity.setOkCreateAccountLogin();
    }

    public void notFreeLogin() {
        mainActivity.setErrorCreateAccountLogin();
    }

    public void checkEmailToCreate(String email) {
        CheckUserLoginEmail checker = new CheckUserLoginEmail();
        checker.setApiServer(this);
        checker.execute(email, APIServer.EMAIL);
    }

    public void freeEmail() {
        mainActivity.setOkCreateAccountEmail();
    }

    public void notFreeEmail() {
        mainActivity.setErrorCreateAccountEmail();
    }
}
