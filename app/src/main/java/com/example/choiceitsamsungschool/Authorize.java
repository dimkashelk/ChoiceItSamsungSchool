package com.example.choiceitsamsungschool;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Authorize extends AsyncTask<String, Void, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private String login;
    private String token;
    private MainActivity mainActivity;

    public Authorize(String login, String token) {
        this.login = login;
        this.token = token;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * CHECK USER'S LOGIN + TOKEN
         */
        RequestBody body;
        Request request;
        String json;
        try {
            json = "{'login': '" + login + "', " +
                    "'token': '" + token + "'}";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.AUTH)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            return jsonObject.get("ok").getAsBoolean();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            mainActivity.login();
        } else {
            mainActivity.logout();
        }
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
