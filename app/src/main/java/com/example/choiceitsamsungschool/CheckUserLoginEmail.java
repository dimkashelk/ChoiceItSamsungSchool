package com.example.choiceitsamsungschool;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckUserLoginEmail extends AsyncTask<String, Void, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private APIServer apiServer;
    private String mode;

    public void setApiServer(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        mode = params[1];
        RequestBody body;
        Request request;
        if (mode.equals(APIServer.LOGIN)) {
            String json = "'login': '" + params[0] + "'";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.CHECK_LOGIN)
                    .post(body)
                    .build();
        } else {
            String json = "'email': '" + params[0] + "'";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.CHECK_EMAIL)
                    .post(body)
                    .build();
        }
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            return jsonObject.get("is_free").getAsBoolean();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean ans) {
        super.onPostExecute(ans);
        if (mode.equals(APIServer.LOGIN)) {
            if (ans) {
                apiServer.freeLogin();
            } else {
                apiServer.freeLogin();
            }
        } else {
            if (ans) {
                apiServer.freeEmail();
            } else {
                apiServer.freeEmail();
            }
        }
    }
}
