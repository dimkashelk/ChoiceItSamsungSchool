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
    private boolean isFreeEmail = false;
    private boolean isFreeLogin = false;
    private boolean findEmail = false;
    private String mode;

    public void setApiServer(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        mode = params[params.length - 1];
        RequestBody body;
        Request request;
        if (mode.equals(APIServer.LOGIN)) {
            String json = "'login': '" + params[0] + "'";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.CHECK_LOGIN)
                    .post(body)
                    .build();
        } else if (mode.equals(APIServer.EMAIL_LOGIN)) {
            String json = "'email': '" + params[0] + "'";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.CHECK_EMAIL)
                    .post(body)
                    .build();
        } else if (mode.equals(APIServer.FIND_EMAIL)) {
            String json = "'email': '" + params[0] + "'";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.CHECK_EMAIL)
                    .post(body)
                    .build();
        } else {
            String json = "'email': '" + params[0] + "', 'login': '" + params[1] + "'";
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
            if (mode.equals(APIServer.FIND_EMAIL)) {
                findEmail = jsonObject.get("find").getAsBoolean();
                return findEmail;
            } else if (mode.equals(APIServer.LOGIN)) {
                isFreeLogin = jsonObject.get("is_free_login").getAsBoolean();
                return isFreeLogin;
            }
             else if (mode.equals(APIServer.EMAIL)) {
                isFreeEmail = jsonObject.get("is_free_email").getAsBoolean();
                return isFreeEmail;
            } else if (mode.equals(APIServer.EMAIL_LOGIN)) {
                isFreeLogin = jsonObject.get("is_free_login").getAsBoolean();
                isFreeEmail = jsonObject.get("is_free_email").getAsBoolean();
                return isFreeEmail && isFreeLogin;
            }
             return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean ans) {
        super.onPostExecute(ans);
        if (mode.equals(APIServer.LOGIN)) {
            if (isFreeLogin) {
                apiServer.freeLogin();
            } else {
                apiServer.notFreeLogin();
            }
        } else if (mode.equals(APIServer.EMAIL)) {
            if (isFreeEmail) {
                apiServer.freeEmail();
            } else {
                apiServer.notFreeEmail();
            }
        } else if (mode.equals(APIServer.FIND_EMAIL)) {
            if (findEmail) {
                apiServer.foundEmail();
            } else {
                apiServer.notFoundEmail();
            }
        } else {
            apiServer.resultCheckingEmailLogin(isFreeEmail, isFreeLogin);
        }
    }
}
