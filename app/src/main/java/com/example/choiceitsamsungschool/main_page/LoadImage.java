package com.example.choiceitsamsungschool.main_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;

import java.io.InputStream;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadImage extends AsyncTask<String, Void, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private Bitmap bitmap;
    private String user_id;
    private String survey_id;
    private String person_id;
    private APIServer apiServer;
    private String mode;

    public LoadImage(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * Load user profile image:
         * @param METHOD
         * @param id_user
         * @param login
         * @param token
         * Load survey title image:
         * @param METHOD
         * @param id_survey
         * @param login
         * @param token
         * Load person image
         * @param METHOD
         * @param person_id
         * @param login
         * @param token
         * */
        RequestBody body;
        Request request;
        String json;
        switch (strings[0]) {
            case APIServer.LOAD_FRIENDS:
                mode = strings[0];
                json = "{'is_profile': " + true + "," +
                        "'login': '" + strings[2] + "'," +
                        "'token': '" + strings[3] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                user_id = strings[1];
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_IMAGE + "/" + strings[1])
                        .post(body)
                        .build();
            case APIServer.LOAD_USER_SURVEYS:
                mode = strings[0];
                json = "{'is_title': " + true + "," +
                        "'login': '" + strings[2] + "'," +
                        "'token': '" + strings[3] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                survey_id = strings[1];
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_USER_SURVEYS + "/" + strings[1])
                        .post(body)
                        .build();
            case APIServer.LOAD_SEARCH_PERSON:
            case APIServer.LOAD_PERSON:
                mode = strings[0];
                json = "{'profile': " + true + "," +
                        "'login': '" + strings[2] + "'," +
                        "'token': '" + strings[3] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                person_id = strings[1];
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_PERSON + "/" + strings[1])
                        .post(body)
                        .build();
            case APIServer.LOAD_SEARCH_SURVEY:
            case APIServer.LOAD_SURVEY:
                mode = strings[0];
                json = "{'survey': " + true + "," +
                        "'login': '" + strings[2] + "'," +
                        "'token': '" + strings[3] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                person_id = strings[1];
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_SURVEY + "/" + strings[1])
                        .post(body)
                        .build();
            default:
                request = new Request.Builder()
                        .url(APIServer.URL)
                        .build();
        }
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            switch (mode) {
                case APIServer.LOAD_SEARCH_SURVEY:
                case APIServer.LOAD_SEARCH_PERSON:
                case APIServer.LOAD_SURVEY:
                case APIServer.LOAD_PERSON:
                case APIServer.LOAD_USER_SURVEYS:
                case APIServer.LOAD_FRIENDS:
                    InputStream inputStream = Objects.requireNonNull(response.body()).byteStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    break;
                default:
                    bitmap = null;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            switch (mode) {
                case APIServer.LOAD_IMAGE:
                    apiServer.setFriendProfileImage(bitmap, user_id);
                    break;
                case APIServer.LOAD_SURVEY:
                case APIServer.LOAD_USER_SURVEYS:
                    apiServer.setSurveyTitleImage(bitmap, survey_id);
                    break;
                case APIServer.LOAD_PERSON:
                    apiServer.setPersonImage(bitmap, person_id);
                    break;
                case APIServer.LOAD_SEARCH_PERSON:
                    apiServer.setSearchProfileImage(bitmap, person_id);
                case APIServer.LOAD_SEARCH_SURVEY:
                    apiServer.setSearchTitleImage(bitmap, survey_id);
            }
        }
    }
}
