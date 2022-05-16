package com.example.choiceitsamsungschool;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
import com.example.choiceitsamsungschool.db.SpotDB;
import com.example.choiceitsamsungschool.db.Survey;
import com.example.choiceitsamsungschool.main_page.CreatePage;
import com.example.choiceitsamsungschool.main_page.FriendsPage;
import com.example.choiceitsamsungschool.main_page.HomePage;
import com.example.choiceitsamsungschool.main_page.LoadData;
import com.example.choiceitsamsungschool.main_page.LoadImage;
import com.example.choiceitsamsungschool.main_page.PersonPage;
import com.example.choiceitsamsungschool.main_page.SaveResultSurvey;
import com.example.choiceitsamsungschool.main_page.SearchPage;
import com.example.choiceitsamsungschool.main_page.Spot;
import com.example.choiceitsamsungschool.main_page.SurveyPage;
import com.example.choiceitsamsungschool.main_page.UploadSurvey;
import com.example.choiceitsamsungschool.main_page.UserPage;
import com.example.choiceitsamsungschool.main_page.UserPageArchive;
import com.example.choiceitsamsungschool.main_page.UserPageFavorites;
import com.example.choiceitsamsungschool.welcome_page.CheckUserLoginEmail;
import com.example.choiceitsamsungschool.welcome_page.CheckUserLoginPassword;
import com.example.choiceitsamsungschool.welcome_page.RegisterUser;
import com.example.choiceitsamsungschool.welcome_page.WelcomePage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import okhttp3.MediaType;

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
    public static final String LOAD_USER_SURVEYS = "user_surveys";
    public static final String LOAD_USER_NEWS_FEED = "user_news_feed";
    public static final String LOAD_PERSON = "load_person";
    public static final String LOAD_PERSON_SURVEYS = "load_person/surveys";
    public static final String SEARCH = "search";
    public static final String LOAD_SURVEY = "load_survey";
    public static final String LOAD_SEARCH_PERSON = "load_search_person";
    public static final String LOAD_SEARCH_SURVEY = "load_search_survey";
    public static final String UPLOAD_SURVEY = "upload_survey";

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private InternalStorage internalStorage;
    public static MainActivity mainActivity;
    public static WelcomePage welcomePage;
    public static UserPage userPage;
    public static HomePage homePage;
    public static FriendsPage friendsPage;
    public static SearchPage searchPage;
    public static CreatePage createPage;
    public static SurveyPage surveyPage;
    public static PersonPage personPage;

    private String token;
    private int count_friends = 0;
    private int count_surveys = 0;
    private int count_surveys_favorites = 0;
    private int count_surveys_archive = 0;
    private int count_search_surveys = 0;
    private int count_search_persons = 0;
    private Vector<Friend> user_friends_list;
    private int count_news = 0;
    private int count_news_persons = 0;

    public APIServer() {

    }

    public APIServer(WelcomePage welcomePage) {
        this.welcomePage = welcomePage;
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
        loadUserNewsFeed();
    }

    public void loadUserNewsFeed() {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        appDatabase.surveyDao().removeAllNews();
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        LoadData loader = new LoadData(this);
        List<String> strings = HomePage.get_selected_friends();
        StringBuilder ids = new StringBuilder();
        for (String s : strings) {
            ids.append(s).append(",");
        }
        if (ids.length() > 0) {
            ids.deleteCharAt(ids.length() - 1);
        }
        loader.execute(
                APIServer.LOAD_USER_NEWS_FEED,
                login,
                token,
                ids.toString(),
                String.valueOf(HomePage.get_min_count()),
                String.valueOf(HomePage.get_max_count()),
                String.valueOf(HomePage.get().get_selected_date()),
                String.valueOf(HomePage.get().get_selected_most_popular()),
                String.valueOf(HomePage.get_increasing())
        );
    }

    private void loadUserSurveys() {
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        LoadData loader = new LoadData(this);
        loader.execute(APIServer.LOAD_USER_SURVEYS, login, token);
    }

    public void loadFriends() {
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
        HomePage.updateFriendsChips();
    }

    public synchronized void setFriendProfileImage(Bitmap bitmap, String id) {
        internalStorage.saveUserProfileImage(bitmap, id);
        count_friends -= 1;
        if (count_friends == 0) {
            userPage.updateFriendsList(user_friends_list);
            friendsPage.filterFriends();
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

    public void setUserSurveys(Vector<Survey> surveys) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        String token = mainActivity.getToken();
        String login = mainActivity.getLogin();
        for (int i = 0; i < surveys.size(); i++) {
            if (surveys.get(i).is_archive) {
                count_surveys_archive++;
            } else if (surveys.get(i).is_favorites) {
                count_surveys_favorites++;
            } else {
                count_surveys++;
            }
            LoadImage loader = new LoadImage(this);
            loader.execute(APIServer.LOAD_IMAGE, surveys.get(i).survey_id, login, token);
            appDatabase.surveyDao().addSurvey(surveys.get(i));

            List<Person> persons = appDatabase.personDao().getPerson(surveys.get(i).person_url);
            if (persons.size() == 0 && surveys.get(i).person_url.equals(login)) {
                LoadData loadData = new LoadData(this);
                loadData.execute(APIServer.LOAD_PERSON, login, token, surveys.get(i).person_url);
            }
        }
    }

    public void setSurveyTitleImage(Bitmap bitmap, String survey_id) {
        internalStorage.saveSurveyTitleImage(bitmap, survey_id);
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        List<Survey> surveys = appDatabase.surveyDao().getSurvey(survey_id);
        if (surveys.size() != 0) {
            Survey survey = surveys.get(0);
            if (survey.is_favorites) {
                count_surveys_favorites--;
                if (count_surveys_favorites == 0) {
                    UserPageFavorites.updateFavorites();
                }
            } else if (survey.is_archive) {
                count_surveys_archive--;
                if (count_surveys_archive == 0) {
                    UserPageArchive.updateArchive();
                }
            } else {
                count_surveys--;
                if (count_surveys == 0) {
                    UserPage.get().updateSurvey();
                }
            }
        }
    }

    public void setHomePage(HomePage homePage) {
        APIServer.homePage = homePage;
    }

    public void setNews(Vector<Survey> news) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        String token = mainActivity.getToken();
        String login = mainActivity.getLogin();
        count_news = news.size();
        HashSet<String> persons = new HashSet<>();
        for (int i = 0; i < news.size(); i++) {
            LoadImage loader_image = new LoadImage(this);
            loader_image.execute(APIServer.LOAD_SURVEY, news.get(i).survey_id, login, token);
            appDatabase.surveyDao().addSurvey(news.get(i));
            persons.add(news.get(i).person_url);
        }
        for (String person : persons) {
            LoadData loader_person = new LoadData(this);
            loader_person.execute(APIServer.LOAD_PERSON, login, token, person);
        }
    }

    public void addPerson(Person person) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        List<Person> persons = appDatabase.personDao().getPerson(person.person_id);
        if (persons.size() == 0) {
            LoadImage loader = new LoadImage(this);
            loader.execute(APIServer.LOAD_PERSON, person.person_id, login, token);
            appDatabase.personDao().addPerson(person);
        }
    }

    public void setPersonImage(Bitmap bitmap, String person_id) {
        internalStorage.savePersonProfileImage(bitmap, person_id);
        count_news_persons--;
        if (count_news_persons == 0) {
            HomePage.updateNewsFeed();
        }
    }

    public void setFriendsPage(FriendsPage friendsPage) {
        APIServer.friendsPage = friendsPage;
    }

    public void setSearchPage(SearchPage searchPage) {
        APIServer.searchPage = searchPage;
    }

    public void search() {
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        String value = searchPage.getSearchText();
        String search_persons = String.valueOf(searchPage.searchPersons());
        String search_surveys = String.valueOf(searchPage.searchSurveys());
        String search_friends_surveys = String.valueOf(searchPage.searchFriendsSurveys());
        String age_from = String.valueOf(searchPage.getAgeFrom());
        String age_to = String.valueOf(searchPage.getAgeTo());
        String count_question_from = String.valueOf(searchPage.getCountQuestionsFrom());
        String count_question_to = String.valueOf(searchPage.getCountQuestionsTo());
        LoadData searcher = new LoadData(this);
        searcher.execute(
                APIServer.SEARCH,
                login,
                token,
                value,
                search_persons,
                search_surveys,
                search_friends_surveys,
                age_from,
                age_to,
                count_question_from,
                count_question_to
        );
    }

    public void setResultSearch(Vector<Person> persons, Vector<Survey> surveys) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        for (Person person : persons) {
            List<Person> personList = appDatabase.personDao().getPerson(person.person_id);
            if (personList.size() == 0) {
                LoadImage loader = new LoadImage(this);
                loader.execute(
                        APIServer.LOAD_SEARCH_PERSON,
                        login,
                        token,
                        person.person_id
                );
                appDatabase.personDao().addPerson(person);
                count_search_persons++;
            }
        }
        for (Survey survey : surveys) {
            List<Survey> surveyList = appDatabase.surveyDao().getSurvey(survey.survey_id);
            if (surveyList.size() == 0) {
                LoadImage loader = new LoadImage(this);
                loader.execute(
                        APIServer.LOAD_SEARCH_SURVEY,
                        survey.survey_id,
                        login,
                        token
                );
                appDatabase.surveyDao().addSurvey(survey);
                count_search_surveys++;
            }
        }
    }

    public void setSearchProfileImage(Bitmap bitmap, String person_id) {
        count_search_persons--;
        internalStorage.savePersonProfileImage(bitmap, person_id);
        if (count_search_persons == 0 && count_search_surveys == 0) {
            searchPage.updateResults();
        }
    }

    public void setSearchTitleImage(Bitmap bitmap, String survey_id) {
        count_search_surveys--;
        internalStorage.savePersonProfileImage(bitmap, survey_id);
        if (count_search_persons == 0 && count_search_surveys == 0) {
            searchPage.updateResults();
        }
    }

    public void setCreatePage(CreatePage createPage) {
        APIServer.createPage = createPage;
    }

    public void successfulUpload() {
        createPage.successfulUpload();
    }

    public void unsuccessfulUpload() {
        createPage.unsuccessfulUpload();
    }

    public void createSurvey(
            String title,
            String description,
            List<Bitmap> images,
            int day,
            int month,
            int year,
            boolean add_to_favorites,
            boolean only_for_friends,
            boolean anonymous_statistic,
            List<String> friends
    ) {
        UploadSurvey upload = new UploadSurvey(
                this,
                title,
                description,
                images,
                day,
                month,
                year,
                add_to_favorites,
                only_for_friends,
                anonymous_statistic,
                friends
        );
        upload.execute(
                MainActivity.get().getLogin(),
                MainActivity.get().getToken()
        );
    }

    public void endSurvey(HashMap<Integer, List<Spot>> map) {
        String login = mainActivity.getLogin();
        String token = mainActivity.getToken();
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());

        HashMap<String, Integer> dop = new HashMap<>();

        for (Integer position : map.keySet()) {
            for (Spot spot : map.get(position)) {
                SpotDB spotDB = appDatabase.spotDao().getSpot(spot.id);
                if (spotDB != null) {
                    dop.put(spotDB.spot_id, position);
                }
            }
        }

        SaveResultSurvey saver = new SaveResultSurvey(dop);
        saver.execute(login, token);
    }

    public void dontSaveSurveyRes() {
        surveyPage.errorSave();
    }

    public static void setSurveyPage(SurveyPage surveyPage) {
        APIServer.surveyPage = surveyPage;
    }

    public void loadSurvey(Survey survey) {
        LoadData loader = new LoadData(this);
        loader.execute(APIServer.LOAD_SURVEY, getLogin(), getToken(), survey.survey_id);
    }

    public void setSpots(int survey_id, List<SpotDB> spotDBs, List<Bitmap> images) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        for (int i = 0; i < spotDBs.size(); i++) {
            appDatabase.spotDao().addSpot(spotDBs.get(i));
            InternalStorage.getInternalStorage().saveSpotImage(images.get(i), spotDBs.get(i).spot_id);
        }

        surveyPage.loadedSurvey(String.valueOf(survey_id));
    }

    public void loadSurveys(String person_id) {
        LoadData loader = new LoadData(this);
        loader.execute(APIServer.LOAD_PERSON_SURVEYS, getLogin(), getToken(), person_id);
    }

    public void setSurveys(Vector<Survey> surveys, Vector<Bitmap> survey_images) {
        AppDatabase appDatabase = AppDatabase.getDatabase(mainActivity.getBaseContext());
        for (int i = 0; i < surveys.size(); i++) {
            appDatabase.surveyDao().addSurvey(surveys.get(i));
            InternalStorage.getInternalStorage().saveSurveyTitleImage(survey_images.get(i), surveys.get(i).survey_id);
        }

        personPage.updateSurveys();
    }
}
