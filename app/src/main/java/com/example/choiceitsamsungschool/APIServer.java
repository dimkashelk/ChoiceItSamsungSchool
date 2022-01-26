package com.example.choiceitsamsungschool;


import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class APIServer {
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";

    public static final String URL = "https://example.com/api/";
    public static final String CHECK_LOGIN = "check_login";
    public static final String CHECK_EMAIL = "check_email";
    public static final String AUTHORIZATION = "auth";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private MainActivity mainActivity;

    APIServer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        client = new OkHttpClient();
    }

    private boolean checkInternetPermission() {
        return ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void getInternetPermission() {
        ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.INTERNET}, 1);
    }

    public void checkLoginToCreate(String login) {
        if (checkInternetPermission()) {
            CheckUserLoginEmail checker = new CheckUserLoginEmail();
            checker.setApiServer(this);
            checker.execute(login, APIServer.LOGIN);
        } else {
            getInternetPermission();
        }
    }

    public void freeLogin() {
        mainActivity.setOkCreateAccountLogin();
    }

    public void notFreeLogin() {
        mainActivity.setErrorCreateAccountLogin();
    }

    public void checkEmailToCreate(String email) {
        if (checkInternetPermission()) {
            CheckUserLoginEmail checker = new CheckUserLoginEmail();
            checker.setApiServer(this);
            checker.execute(email, APIServer.EMAIL);
        } else {
            getInternetPermission();
        }
    }

    public void freeEmail() {
        mainActivity.setOkCreateAccountEmail();
    }

    public void notFreeEmail() {
        mainActivity.setErrorCreateAccountEmail();
    }

    public void checkUserLoginPassword(String login, String password) {
        if (checkInternetPermission()) {
            CheckUserLoginPassword checker = new CheckUserLoginPassword();
            checker.setApiServer(this);
            checker.execute(login, password, APIServer.LOGIN);
        } else {
            getInternetPermission();
        }
    }


    public void okUserLoginPassword(String login, String token) {
        mainActivity.setOkLoginUserLoginPassword(login, token);
    }

    public void errorUserLoginPassword() {
        mainActivity.setErrorLoginUserLoginPassword();
    }

    public void checkUserEmailPassword(String email, String password) {
        if (checkInternetPermission()) {
            CheckUserLoginPassword checker = new CheckUserLoginPassword();
            checker.setApiServer(this);
            checker.execute(email, password, APIServer.EMAIL);
        } else {
            getInternetPermission();
        }
    }

    public void checkEmailLoginToCreate(String email, String login) {
        if (checkInternetPermission()) {
            CheckUserLoginEmail checker = new CheckUserLoginEmail();
            checker.setApiServer(this);
            checker.execute(email, APIServer.EMAIL);
            try {
                while (!checker.isCancelled()) {
                }
            } catch (Exception e) {
                notFreeEmail();
            }
        } else {
            getInternetPermission();
        }
    }

    public void checkLoginToCreateWithWait(String login) {
        if (checkInternetPermission()) {
            CheckUserLoginEmail checker = new CheckUserLoginEmail();
            checker.setApiServer(this);
            checker.execute(login, APIServer.LOGIN);
            try {
                while (!checker.isCancelled()) {
                }
            } catch (Exception e) {
                notFreeEmail();
            }
        } else {
            getInternetPermission();
        }
    }
}
