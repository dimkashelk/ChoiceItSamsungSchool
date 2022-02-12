package com.example.choiceitsamsungschool;


import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.choiceitsamsungschool.welcome_page.CheckUserLoginEmail;
import com.example.choiceitsamsungschool.welcome_page.CheckUserLoginPassword;
import com.example.choiceitsamsungschool.welcome_page.RegisterUser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class APIServer {
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";
    public static final String EMAIL_LOGIN = "email_login";

    public static final String URL = "https://example.com/api/";
    public static final String CHECK_LOGIN = "check_login";
    public static final String CHECK_EMAIL = "check_email";
    public static final String CHECK_EMAIL_LOGIN = "check_email_login";
    public static final String AUTHORIZATION = "auth";
    public static final String REGISTRATION = "reg";
    public static final String FIND_EMAIL = "find_email";
    public static final String CHECK_VERIFY_CODE = "check_verify_code";
    public static final String LOAD_FRIENDS = "load_friends";
    public static final String LOAD_IMAGE = "images";

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
            checker.execute(email, login, APIServer.EMAIL_LOGIN);
        } else {
            getInternetPermission();
        }
    }

    public void resultCheckingEmailLogin(boolean isFreeEmail, boolean isFreeLogin, String token) {
        mainActivity.endCheckRegisterData(isFreeEmail, isFreeLogin, token);
    }

    public void findEmail(String email) {
        if (checkInternetPermission()) {
            CheckUserLoginEmail checker = new CheckUserLoginEmail();
            checker.setApiServer(this);
            checker.execute(email, APIServer.FIND_EMAIL);
        } else {
            getInternetPermission();
        }
    }

    public void foundEmail() {
        mainActivity.foundEmailForForgotPassword();
    }

    public void notFoundEmail() {
        mainActivity.notFoundEmailForForgotPassword();
    }

    public void checkVerifyCode(String email, String code, String new_password) {
        if (checkInternetPermission()) {
            CheckUserLoginEmail checker = new CheckUserLoginEmail();
            checker.setApiServer(this);
            checker.execute(email, code, new_password, APIServer.CHECK_VERIFY_CODE);
        } else {
            getInternetPermission();
        }
    }

    public void okVerifyCode(String token) {
        mainActivity.okVerifyCode(token);
    }

    public void wrongVerifyCode() {
        mainActivity.wrongVerifyCode();
    }

    public void registration(String firstName, String secondName, String shortName, String email, String password) {
        if (checkInternetPermission()) {
            RegisterUser registerUser = new RegisterUser(
                    this,
                    firstName,
                    secondName,
                    shortName,
                    email,
                    password
            );
            registerUser.execute();
        } else {
            getInternetPermission();
        }
    }
}
