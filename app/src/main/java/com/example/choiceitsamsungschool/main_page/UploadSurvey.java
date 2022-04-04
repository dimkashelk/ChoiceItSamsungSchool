package com.example.choiceitsamsungschool.main_page;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.example.choiceitsamsungschool.APIServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
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
    private List<Bitmap> images;
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
            List<Bitmap> images,
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
        StringBuilder json = new StringBuilder("{'login': '" + strings[0] + "', " +
                "'token': '" + strings[1] + "', " +
                "'images': [");
        for (Bitmap image : images) {
            json.append("'").append(bitmapToString(image)).append("',");
        }
        json.append("],");
        json.append("'to_date': '").append(day).append("/").append(month).append("/").append(year).append("',");
        json.append("'add_to_favorites': ").append(add_to_favorites).append(",");
        json.append("'only_for_friends': ").append(only_for_friends).append(",");
        json.append("'anonymous_statistic': ").append(anonymous_statistic).append(",");
        json.append("'send_to_friends': [");
        for (String str : friends) {
            json.append(str).append(",");
        }
        json.append("],");
        json.append("'title': '").append(title).append("',");
        json.append("'description': '").append(description).append("'}");
        body = RequestBody.create(json.toString(), APIServer.JSON);
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
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            if (!jsonObject.get("status").getAsBoolean()) {
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
