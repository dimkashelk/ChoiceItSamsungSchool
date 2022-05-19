package com.example.choiceitsamsungschool.main_page;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.example.choiceitsamsungschool.APIServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaveUserChanges extends AsyncTask<String, Boolean, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private APIServer apiServer = APIServer.getSingletonAPIServer();
    private String first_name;
    private String second_name;
    private String login;
    private boolean change_password = false;
    private String old_password;
    private String new_password;
    private boolean change_profile_image = false;
    private Bitmap profile_image;
    private String s = "Нет подключения к серверу";

    public SaveUserChanges() {
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
        change_password = true;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
        change_password = true;
    }

    public void setProfile_image(Bitmap profile_image) {
        this.profile_image = profile_image;
        change_profile_image = true;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * SAVE USER CHANGES
         * @param login
         * @param token
         */
        try {
            RequestBody body;
            Request request;
            String json = "{'new_login': '" + login + "'," +
                    "'first_name': '" + first_name + "'," +
                    "'second_name': '" + second_name + "'," +
                    "'login': '" + strings[0] + "'," +
                    "'token': '" + strings[1] + "',";
            if (change_password) {
                json += "'old_password': '" + old_password + "'," +
                        "'new_password': '" + new_password + "',";
            }
            if (change_profile_image) {
                json += "'new_profile_image': '" + BitMapToString(profile_image) + "',";
            }
            json += "}";
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.UPDATE_USER_DATA)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            if (change_password) {
                if (jsonObject.get("changed_password").getAsBoolean()) {
                    apiServer.setLoginToken(jsonObject.get("login").getAsString(), jsonObject.get("token").getAsString());
                    UserPageSettings.freeLogin();
                } else {
                    if (!jsonObject.get("login_free").getAsBoolean()) {
                        UserPageSettings.notFreeLogin();
                    }
                    return false;
                }
            } else {
                if (!jsonObject.get("ok").getAsBoolean()) {
                    if (!jsonObject.get("login_free").getAsBoolean()) {
                        UserPageSettings.freeLogin();
                    } else {
                        UserPageSettings.notFreeLogin();
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        s = "Успешно";
        return true;
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        UserPageSettings.stopLoadingButton(s);
    }
}
