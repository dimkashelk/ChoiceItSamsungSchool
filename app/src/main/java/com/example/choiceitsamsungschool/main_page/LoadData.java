package com.example.choiceitsamsungschool.main_page;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.util.Objects;
import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadData extends AsyncTask<String, Boolean, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private int count_friends = 0;
    private int count_surveys = 0;
    private int count_news = 0;
    private Vector<Friend> friends = new Vector<>();
    private Vector<Survey> surveys = new Vector<>();
    private Vector<Survey> news = new Vector<>();
    private APIServer apiServer;
    private String method;
    private Boolean logout = false;
    private Person person;

    public LoadData(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * LOAD FRIENDS
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * LOAD USER DATA
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * LOAD USER NEWS FEED
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * @param friends_id_list
         * @param min_count
         * @param max_count
         * @param is_increasing_most_popular
         * @param is_increasing_active
         * @param is_increasing_date
         * LOAD PERSON
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * @param person_id
         * */
        RequestBody body;
        Request request;
        String json = "{'login': '" + strings[1] + "', " +
                "'token': '" + strings[2] + "'}";
        method = strings[0];
        body = RequestBody.create(json, APIServer.JSON);
        switch (strings[0]) {
            case APIServer.LOAD_FRIENDS:
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_FRIENDS)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_USER_SURVEYS:
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_USER_DATA)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_USER_NEWS_FEED:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'," +
                        "'friends': '" + strings[3] + "'," +
                        "'min_count': '" + strings[4] + "'," +
                        "'max_count': '" + strings[5] + "'," +
                        "'is_increasing_most_popular': '" + strings[6] + "'," +
                        "'is_increasing_active': '" + strings[7] + "'," +
                        "'is_increasing_date': '" + strings[8] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_USER_NEWS_FEED)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_PERSON:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'," +
                        "'person': '" + strings[3] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_USER_NEWS_FEED)
                        .post(body)
                        .build();
                break;
            default:
                request = new Request.Builder()
                        .url(APIServer.URL)
                        .build();
                break;
        }
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            if (!jsonObject.get("status").getAsBoolean()) {
                logout = true;
                return false;
            }
            switch (strings[0]) {
                case APIServer.LOAD_FRIENDS:
                    count_friends = jsonObject.get("count").getAsInt();
                    JsonArray friends_json = jsonObject.getAsJsonArray("friends");
                    for (int i = 0; i < count_friends; i++) {
                        Friend friend = new Friend();
                        friend.friend_id = friends_json.get(i).getAsJsonObject().get("id").getAsString();
                        friend.first_name = friends_json.get(i).getAsJsonObject().get("first_name").getAsString();
                        friend.second_name = friends_json.get(i).getAsJsonObject().get("second_name").getAsString();
                        friend.image_url = friends_json.get(i).getAsJsonObject().get("image").getAsString();
                        friends.add(friend);
                    }
                    break;
                case APIServer.LOAD_USER_SURVEYS:
                    count_surveys = jsonObject.get("count").getAsInt();
                    JsonArray surveys_json = jsonObject.getAsJsonArray("surveys");
                    for (int i = 0; i < count_surveys; i++) {
                        Survey survey = new Survey();
                        survey.survey_id = surveys_json.get(i).getAsJsonObject().get("id").getAsString();
                        survey.title = surveys_json.get(i).getAsJsonObject().get("title").getAsString();
                        survey.description = surveys_json.get(i).getAsJsonObject().get("description").getAsString();
                        survey.title_image_url = surveys_json.get(i).getAsJsonObject().get("title_image_url").getAsString();
                        survey.person_url = surveys_json.get(i).getAsJsonObject().get("person_url").getAsString();
                        survey.is_archive = surveys_json.get(i).getAsJsonObject().get("is_archive").getAsBoolean();
                        survey.is_favorites = surveys_json.get(i).getAsJsonObject().get("is_favorites").getAsBoolean();
                        surveys.add(survey);
                    }
                    break;
                case APIServer.LOAD_USER_NEWS_FEED:
                    count_news = jsonObject.get("count").getAsInt();
                    JsonArray news_json = jsonObject.getAsJsonArray("news");
                    for (int i = 0; i < count_news; i++) {
                        Survey survey = new Survey();
                        survey.survey_id = news_json.get(i).getAsJsonObject().get("id").getAsString();
                        survey.title = news_json.get(i).getAsJsonObject().get("title").getAsString();
                        survey.description = news_json.get(i).getAsJsonObject().get("description").getAsString();
                        survey.title_image_url = news_json.get(i).getAsJsonObject().get("title_image_url").getAsString();
                        survey.person_url = news_json.get(i).getAsJsonObject().get("person_url").getAsString();
                        survey.is_news = true;
                        news.add(survey);
                    }
                    break;
                case APIServer.LOAD_PERSON:
                    person = new Person();
                    person.person_id = jsonObject.get("person_id").getAsString();
                    person.first_name = jsonObject.get("first_name").getAsString();
                    person.second_name = jsonObject.get("second_name").getAsString();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            switch (method) {
                case APIServer.LOAD_FRIENDS:
                    apiServer.setFriendsList(friends);
                case APIServer.LOAD_USER_SURVEYS:
                    apiServer.setUserSurveys(surveys);
                case APIServer.LOAD_USER_NEWS_FEED:
                    apiServer.setNews(news);
                case APIServer.LOAD_PERSON:
                    apiServer.addPerson(person);
            }
        } else {
            if (logout) {
                apiServer.logout();
            }
        }
    }
}
