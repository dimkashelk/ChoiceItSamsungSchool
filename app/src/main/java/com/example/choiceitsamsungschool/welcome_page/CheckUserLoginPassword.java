package com.example.choiceitsamsungschool.welcome_page;

import static com.example.choiceitsamsungschool.APIServer.JSON;

import android.os.AsyncTask;
import android.util.Log;

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
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("login", login);
                jsonObject.addProperty("password", password);
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(APIServer.URL + APIServer.AUTHORIZATION)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    return null;
                }
                Gson gson = new Gson();
                JsonObject jsonObjectRes = gson.fromJson(response.body().string(), JsonObject.class);
                if (jsonObjectRes.get("ok").getAsBoolean()) {
                    return jsonObjectRes.get("token").getAsString();
                } else {
                    return null;
                }
            } else {
                email = strings[0];
                String password = strings[1];
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("email", email);
                jsonObject.addProperty("password", password);
                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url(APIServer.URL + APIServer.AUTHORIZATION)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    return null;
                }
                Gson gson = new Gson();
                JsonObject jsonObjectRes = gson.fromJson(response.body().string(), JsonObject.class);
                if (jsonObjectRes.get("ok").getAsBoolean()) {
                    login = jsonObjectRes.get("login").getAsString();
                    return jsonObjectRes.get("token").getAsString();
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            Log.e("auth error", e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);
        if (token != null && login != null) {
            apiServer.okUserLoginPassword(login, token);
        } else {
            apiServer.errorUserLoginPassword();
        }
    }
}
