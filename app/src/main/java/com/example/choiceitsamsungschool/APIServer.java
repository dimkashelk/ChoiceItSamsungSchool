package com.example.choiceitsamsungschool;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.main_page.LoadData;
import com.example.choiceitsamsungschool.main_page.LoadImage;
import com.example.choiceitsamsungschool.main_page.UserPage;
import com.example.choiceitsamsungschool.welcome_page.CheckUserLoginEmail;
import com.example.choiceitsamsungschool.welcome_page.CheckUserLoginPassword;
import com.example.choiceitsamsungschool.welcome_page.RegisterUser;
import com.example.choiceitsamsungschool.welcome_page.WelcomePage;

import java.util.Vector;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class APIServer {
    private static APIServer apiServer = null;
    public static final String EMAIL = "email";
    public static final String LOGIN = "login";
    public static final String EMAIL_LOGIN = "email_login";

    public static final String URL = "https://dimkashelk.asuscomm.com:150/api/";
    public static final String CHECK_LOGIN = "check_login";
    public static final String CHECK_EMAIL = "check_email";
    public static final String CHECK_EMAIL_LOGIN = "check_email_login";
    public static final String AUTHORIZATION = "auth";
    public static final String AUTH = "authorization";
    public static final String REGISTRATION = "reg";
    public static final String FIND_EMAIL = "find_email";
    public static final String CHECK_VERIFY_CODE = "check_verify_code";
    public static final String LOAD_FRIENDS = "friends";
    public static final String LOAD_IMAGE = "images";
    public static final String LOAD_USER_DATA = "user";
    public static final String UPDATE_USER_DATA = "update_user_data";
    private static final String LOAD_USER_SURVEYS = "user_surveys";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private InternalStorage internalStorage;
    public static MainActivity mainActivity;
    public static WelcomePage welcomePage;
    public static UserPage userPage;

    private String token;
    private int count_friends = 0;
    private Vector<Friend> user_friends_list;

    public APIServer() {
        client = new OkHttpClient();
    }

    public APIServer(WelcomePage welcomePage) {
        this.welcomePage = welcomePage;
        client = new OkHttpClient();
    }

    public static APIServer getSingletonAPIServer() {
        if (apiServer == null) {
            apiServer = new APIServer();
        }
        return apiServer;
    }

    public void setWelcomePage(WelcomePage welcomePage) {
        this.welcomePage = welcomePage;
    }

    public void setUserPage(UserPage userPage) {
        this.userPage = userPage;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        APIServer.mainActivity = mainActivity;
    }

    private boolean checkInternetPermission() {
        return ContextCompat.checkSelfPermission(welcomePage, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void getInternetPermission() {
        ActivityCompat.requestPermissions(welcomePage, new String[]{Manifest.permission.INTERNET}, 1);
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
        welcomePage.setOkCreateAccountLogin();
    }

    public void notFreeLogin() {
        welcomePage.setErrorCreateAccountLogin();
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
        welcomePage.setOkCreateAccountEmail();
    }

    public void notFreeEmail() {
        welcomePage.setErrorCreateAccountEmail();
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
        welcomePage.setOkLoginUserLoginPassword(login, token);
    }

    public void errorUserLoginPassword() {
        welcomePage.setErrorLoginUserLoginPassword();
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
        welcomePage.endCheckRegisterData(isFreeEmail, isFreeLogin, token);
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
        welcomePage.foundEmailForForgotPassword();
    }

    public void notFoundEmail() {
        welcomePage.notFoundEmailForForgotPassword();
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
        welcomePage.okVerifyCode(token);
    }

    public void wrongVerifyCode() {
        welcomePage.wrongVerifyCode();
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

    public void loadUserData() {
        loadFriends();
        loadUserSurveys();
    }

    private void loadUserSurveys() {
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        LoadData loader = new LoadData(this);
        loader.execute(APIServer.LOAD_USER_SURVEYS, login, token);
    }

    private void loadFriends() {
        String token = mainActivity.getToken();
        String login = mainActivity.getLogin();
        LoadData loader = new LoadData(this);
        loader.execute(APIServer.LOAD_FRIENDS, login, token);
    }

    public void setFriendsList(Vector<Friend> friends) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        String token = mainActivity.getToken();
        String login = mainActivity.getLogin();
        count_friends = friends.size();
        for (int i = 0; i < friends.size(); i++) {
            LoadImage loader = new LoadImage(this);
            loader.execute(APIServer.LOAD_IMAGE, friends.get(i).friend_id, login, token);
            appDatabase.friendDao().addFriend(friends.get(i));
        }
        user_friends_list = friends;
    }

    public synchronized void setFriendProfileImage(Bitmap bitmap, String id) {
        internalStorage.saveUserProfileImage(bitmap, id);
        count_friends -= 1;
        if (count_friends == 0) {
            userPage.updateFriendsList(user_friends_list);
        }
    }

    public String getToken() {
        return mainActivity.getToken();
    }

    public String getLogin() {
        return mainActivity.getLogin();
    }

    public void setLoginToken(String login, String token) {
        mainActivity.setLogin(login);
        mainActivity.setToken(token);
    }

    public void authorize(String login, String token) {
        Authorize authorize = new Authorize(login, token);
        authorize.setMainActivity(mainActivity);
        authorize.execute();
    }

    public void logout() {
        mainActivity.logout();
    }
}
