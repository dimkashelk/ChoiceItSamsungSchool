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

    public void setApiServer(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String ans;
        RequestBody body = RequestBody.create(params[0], APIServer.JSON);
        Request request = new Request.Builder()
                .url(APIServer.URL + APIServer.CHECK_LOGIN)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            boolean isFree = jsonObject.get("is_free").getAsBoolean();
            return isFree;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean ans) {
        super.onPostExecute(ans);
        if (ans) {
            apiServer.freeLogin();
        } else {
            apiServer.notFreeLogin();
        }
    }
}
