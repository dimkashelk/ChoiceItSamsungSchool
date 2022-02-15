package com.example.choiceitsamsungschool.welcome_page;

import android.os.AsyncTask;
import android.util.Log;

import com.example.choiceitsamsungschool.APIServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterUser extends AsyncTask<String, Boolean, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private APIServer apiServer;
    private String firstName;
    private String secondName;
    private String shortName;
    private boolean shortNameOk = true;
    private String email;
    private boolean emailOk = true;
    private String password;
    private String token;

    public RegisterUser(APIServer apiServer, String firstName, String secondName, String shortName, String email, String password) {
        this.apiServer = apiServer;
        this.firstName = firstName;
        this.secondName = secondName;
        this.shortName = shortName;
        this.email = email;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            String json = "{'login': '" + shortName + "', " +
                    "'password': '" + password + "', " +
                    "'first_name': '" + firstName + "', " +
                    "'second_name': '" + secondName + "', " +
                    "'email': '" + email + "' " + "}";
            RequestBody body = RequestBody.create(json, APIServer.JSON);
            Request request = new Request.Builder()
                    .url(APIServer.URL + APIServer.REGISTRATION)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            if (jsonObject.get("status").getAsBoolean()) {
                token = jsonObject.get("token").getAsString();
            } else {
                if (!jsonObject.get("email_free").getAsBoolean()) {
                    emailOk = false;
                }
                if (!jsonObject.get("login_free").getAsBoolean()) {
                    shortNameOk = false;
                }
            }
            return true;
        } catch (Exception e) {
            Log.e("reg user", e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        if (s) {
            if (emailOk && shortNameOk) {
                apiServer.resultCheckingEmailLogin(emailOk, shortNameOk, token);
            } else {
                apiServer.stopLoading(emailOk, shortNameOk);
            }
        }
        apiServer.stopLoading(false, false);
    }
}
