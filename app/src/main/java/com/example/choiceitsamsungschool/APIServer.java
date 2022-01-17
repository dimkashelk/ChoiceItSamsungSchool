package com.example.choiceitsamsungschool;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class APIServer {
    public static final String URL = "https://example.com/api/";
    public static final String CHECK_LOGIN = "check_login";

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
        checker.execute(login);
    }

    public void freeLogin() {
        mainActivity.setOkCreateAccountLogin();
    }

    public void notFreeLogin() {
        mainActivity.setErrorCreateAccountLogin();
    }
}
