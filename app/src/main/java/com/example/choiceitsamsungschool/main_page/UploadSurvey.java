package com.example.choiceitsamsungschool.main_page;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import androidx.core.util.Pair;

import com.example.choiceitsamsungschool.APIServer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadSurvey extends AsyncTask<String, String, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private APIServer apiServer;
    private String method;
    private String title;
    private String description;
    private List<Pair<Bitmap, String>> images;
    private boolean add_to_favorites;
    private boolean only_for_friends;
    private boolean anonymous_statistic;
    private List<String> friends;
    private int day;
    private int month;
    private int year;

    public UploadSurvey(
            APIServer apiServer,
            String title,
            String description,
            List<Pair<Bitmap, String>> images,
            int day,
            int month,
            int year,
            boolean add_to_favorites,
            boolean only_for_friends,
            boolean anonymous_statistic,
            List<String> friends
    ) {
        this.apiServer = apiServer;
        this.title = title;
        this.description = description;
        this.images = images;
        this.day = day;
        this.month = month;
        this.year = year;
        this.add_to_favorites = add_to_favorites;
        this.only_for_friends = only_for_friends;
        this.anonymous_statistic = anonymous_statistic;
        this.friends = friends;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * Upload survey
         * @param login
         * @param token
         */
        RequestBody body;
        Request request;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("login", strings[0]);
        jsonObject.addProperty("token", strings[1]);
        JsonArray array = new JsonArray();
        for (Pair<Bitmap, String> pair : images) {
            JsonObject images_json = new JsonObject();
            images_json.addProperty("image", bitmapToString(pair.first));
            images_json.addProperty("title", pair.second);
            array.add(images_json);
        }
        jsonObject.add("images", array);
        Timestamp timestamp = new Timestamp(new Date(day + "/" + month + "/" + year).getTime());
        jsonObject.addProperty("to_date", timestamp.getTime());
        jsonObject.addProperty("add_to_favorites", add_to_favorites);
        jsonObject.addProperty("only_for_friends", only_for_friends);
        jsonObject.addProperty("anonymous_statistic", anonymous_statistic);
        array = new JsonArray();
        for (String str : friends) {
            array.add(str);
        }
        jsonObject.add("send_to_friends", array);
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("description", description);
        body = RequestBody.create(jsonObject.toString(), APIServer.JSON);
        request = new Request.Builder()
                .url(APIServer.URL + APIServer.UPLOAD_SURVEY)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObjectRes = gson.fromJson(response.body().string(), JsonObject.class);
            if (!jsonObjectRes.get("status").getAsBoolean()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            apiServer.successfulUpload();
        } else {
            apiServer.unsuccessfulUpload();
        }
    }
}
