package com.example.choiceitsamsungschool.welcome_page;

import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckUserLoginPassword extends AsyncTask<String, Boolean, String> {
    private OkHttpClient client = new OkHttpClient();
    private APIServer apiServer;
    private String login;
    private String mode;
    private String email;

    public void setApiServer(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            mode = strings[2];
            if (mode.equals(APIServer.LOGIN)) {
                login = strings[0];
                String password = strings[1];
                String json = "{'login': '" + login + "', " +
                        "'password': '" + password + "'}";
                RequestBody body = RequestBody.create(json, APIServer.JSON);
                Request request = new Request.Builder()
                        .url(APIServer.URL + APIServer.AUTHORIZATION)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    return null;
                }
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                if (jsonObject.get("ok").getAsBoolean()) {
                    return jsonObject.get("token").getAsString();
                } else {
                    return null;
                }
            } else {
                email = strings[0];
                String password = strings[1];
                String json = "{'email': '" + login + "', " +
                        "'password': '" + password + "'}";
                RequestBody body = RequestBody.create(json, APIServer.JSON);
                Request request = new Request.Builder()
                        .url(APIServer.URL + APIServer.AUTHORIZATION)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    return null;
                }
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                if (jsonObject.get("ok").getAsBoolean()) {
                    login = jsonObject.get("login").getAsString();
                    return jsonObject.get("token").getAsString();
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null && login != null) {
            apiServer.okUserLoginPassword(login, s);
        } else {
            apiServer.errorUserLoginPassword();
        }
    }


}
