package com.example.choiceitsamsungschool.main_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
import com.example.choiceitsamsungschool.db.SpotDB;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private int count_surveys_search = 0;
    private int count_persons_search = 0;
    private int count_spots = 0;
    private int survey_id = 0;
    private Vector<Friend> friends = new Vector<>();
    private Vector<Survey> surveys = new Vector<>();
    private Vector<Survey> news = new Vector<>();
    private Vector<Person> persons = new Vector<>();
    private Vector<SpotDB> spotDBs = new Vector<>();
    private Vector<Bitmap> spotDBs_images = new Vector<>();
    private Vector<Bitmap> survey_images = new Vector<>();
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
         * @param is_selected_date
         * @param is_selected_most_popular
         * @param is_increasing
         * LOAD PERSON
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * @param person_id
         * SEARCH
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * @param value
         * @param search_persons
         * @param search_surveys
         * @param search_friends_surveys
         * @param age_from
         * @param age_to
         * @param count_question_from
         * @param count_question_to
         * LOAD SURVEY
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * @param survey_id
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
                        "'is_selected_most_popular': '" + strings[7] + "'," +
                        "'is_increasing': '" + strings[8] + "'," +
                        "'is_selected_date': '" + strings[6] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_USER_NEWS_FEED)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_SEARCH_PERSON:
            case APIServer.LOAD_PERSON:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'," +
                        "'person': '" + strings[3] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_PERSON)
                        .post(body)
                        .build();
                break;
            case APIServer.SEARCH:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'," +
                        "'value': '" + strings[3] + "'," +
                        "'search_persons': '" + strings[4] + "'," +
                        "'search_surveys': '" + strings[5] + "'," +
                        "'search_friends_surveys': '" + strings[6] + "'," +
                        "'age_from': '" + strings[7] + "'," +
                        "'age_to': '" + strings[8] + "'," +
                        "'count_question_from': '" + strings[9] + "'," +
                        "'count_question_to': '" + strings[10] + "'," + "}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.SEARCH)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_SURVEY:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'," +
                        "'survey_id': '" + strings[3] + "'";
                survey_id = Integer.getInteger(strings[3]);
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_SURVEY)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_PERSON_SURVEYS:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'," +
                        "'person_id': '" + strings[3] + "'";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_PERSON_SURVEYS)
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
                        friend.age = friends_json.get(i).getAsJsonObject().get("age").getAsInt();
                        friend.count_surveys = friends_json.get(i).getAsJsonObject().get("count_surveys").getAsInt();
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
                case APIServer.LOAD_SEARCH_PERSON:
                case APIServer.LOAD_PERSON:
                    person = new Person();
                    person.person_id = jsonObject.get("person_id").getAsString();
                    person.first_name = jsonObject.get("first_name").getAsString();
                    person.second_name = jsonObject.get("second_name").getAsString();
                    break;
                case APIServer.SEARCH:
                    count_surveys_search = jsonObject.get("count_surveys").getAsInt();
                    count_persons_search = jsonObject.get("count_persons").getAsInt();
                    for (int i = 0; i < count_persons_search; i++) {
                        Person person = new Person();
                        person.person_id = jsonObject.get("persons").getAsJsonArray().get(i).getAsJsonObject().get("person_id").getAsString();
                        person.first_name = jsonObject.get("persons").getAsJsonArray().get(i).getAsJsonObject().get("first_name").getAsString();
                        person.second_name = jsonObject.get("persons").getAsJsonArray().get(i).getAsJsonObject().get("second_name").getAsString();
                        person.is_search = true;
                        persons.add(person);
                    }
                    for (int i = 0; i < count_surveys_search; i++) {
                        Survey survey = new Survey();
                        survey.survey_id = jsonObject.get("surveys").getAsJsonArray().get(i).getAsJsonObject().get("survey_id").getAsString();
                        survey.title = jsonObject.get("surveys").getAsJsonArray().get(i).getAsJsonObject().get("title").getAsString();
                        survey.description = jsonObject.get("surveys").getAsJsonArray().get(i).getAsJsonObject().get("description").getAsString();
                        survey.title_image_url = jsonObject.get("surveys").getAsJsonArray().get(i).getAsJsonObject().get("title_image_url").getAsString();
                        survey.person_url = jsonObject.get("surveys").getAsJsonArray().get(i).getAsJsonObject().get("person_url").getAsString();
                        survey.is_search = true;
                        surveys.add(survey);
                    }
                    break;
                case APIServer.LOAD_SURVEY:
                    count_spots = jsonObject.get("count_spots").getAsInt();
                    JsonArray array = jsonObject.get("spots").getAsJsonArray();
                    for (int i = 0; i < count_spots; i++) {
                        SpotDB spotDB = new SpotDB(
                                array.get(i).getAsJsonObject().get("spot_id").getAsString(),
                                array.get(i).getAsJsonObject().get("title").getAsString(),
                                survey_id,
                                array.get(i).getAsJsonObject().get("count_voice").getAsInt()
                        );
                        spotDBs.add(spotDB);
                        spotDBs_images.add(decode(array.get(i).getAsJsonObject().get("image").getAsString()));
                    }
                    break;
                case APIServer.LOAD_PERSON_SURVEYS:
                    count_surveys = jsonObject.get("count_surveys").getAsInt();
                    JsonArray data = jsonObject.get("spots").getAsJsonArray();
                    for (int i = 0; i < count_surveys; i++) {
                        Survey survey = new Survey();
                        survey.survey_id = data.get(i).getAsJsonObject().get("survey_id").getAsString();
                        survey.title = data.get(i).getAsJsonObject().get("title").getAsString();
                        survey.description = data.get(i).getAsJsonObject().get("description").getAsString();
                        survey.person_url = data.get(i).getAsJsonObject().get("person_url").getAsString();
                        surveys.add(survey);
                        survey_images.add(decode(data.get(i).getAsJsonObject().get("image").getAsString()));
                    }
                    break;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Bitmap decode(String str) {
        byte[] imageAsBytes = Base64.decode(str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            switch (method) {
                case APIServer.LOAD_FRIENDS:
                    apiServer.setFriendsList(friends);
                    break;
                case APIServer.LOAD_USER_SURVEYS:
                    apiServer.setUserSurveys(surveys);
                    break;
                case APIServer.LOAD_USER_NEWS_FEED:
                    apiServer.setNews(news);
                    break;
                case APIServer.LOAD_SEARCH_PERSON:
                case APIServer.LOAD_PERSON:
                    apiServer.addPerson(person);
                    break;
                case APIServer.SEARCH:
                    apiServer.setResultSearch(persons, surveys);
                    break;
                case APIServer.LOAD_SURVEY:
                    apiServer.setSpots(survey_id, spotDBs, spotDBs_images);
                    break;
                case APIServer.LOAD_PERSON_SURVEYS:
                    apiServer.setSurveys(surveys, survey_images);
            }
        } else {
            if (logout) {
                apiServer.logout();
            }
        }
    }
}
