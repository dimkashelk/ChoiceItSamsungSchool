package com.example.choiceitsamsungschool.welcome_page;

import static com.example.choiceitsamsungschool.APIServer.JSON;

import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;
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
    private boolean okVerifyCode = false;
    private String mode;
    private String token;

    public void setApiServer(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        mode = params[params.length - 1];
        RequestBody body;
        Request request;
        JsonObject jsonObject = new JsonObject();
        switch (mode) {
            case APIServer.LOGIN: {
                jsonObject.addProperty("login", params[0]);
                body = RequestBody.create(jsonObject.toString(), JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.CHECK_LOGIN)
                        .post(body)
                        .build();
                break;
            }
            case APIServer.EMAIL: {
                jsonObject.addProperty("email", params[0]);
                body = RequestBody.create(jsonObject.toString(), JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.CHECK_EMAIL)
                        .post(body)
                        .build();
                break;
            }
            case APIServer.FIND_EMAIL: {
                jsonObject.addProperty("email", params[0]);
                body = RequestBody.create(jsonObject.toString(), JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.FIND_EMAIL)
                        .post(body)
                        .build();
                break;
            }
            default: {
                // CHECK VERIFY CODE
                jsonObject.addProperty("email", params[0]);
                jsonObject.addProperty("code", params[1]);
                jsonObject.addProperty("password", params[2]);
                body = RequestBody.create(jsonObject.toString(), JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.CHECK_VERIFY_CODE)
                        .post(body)
                        .build();
                break;
            }

        }
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObjectRes = gson.fromJson(response.body().string(), JsonObject.class);
            switch (mode) {
                case APIServer.FIND_EMAIL:
                    findEmail = jsonObjectRes.get("find").getAsBoolean();
                    return findEmail;
                case APIServer.LOGIN:
                    isFreeLogin = jsonObjectRes.get("is_free_login").getAsBoolean();
                    return isFreeLogin;
                case APIServer.EMAIL:
                    isFreeEmail = jsonObjectRes.get("is_free_email").getAsBoolean();
                    return isFreeEmail;
                case APIServer.CHECK_VERIFY_CODE:
                    okVerifyCode = jsonObjectRes.get("ok").getAsBoolean();
                    if (okVerifyCode) {
                        token = jsonObjectRes.get("token").getAsString();
                    }
                    return okVerifyCode;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean ans) {
        super.onPostExecute(ans);
        switch (mode) {
            case APIServer.LOGIN:
                if (isFreeLogin) {
                    apiServer.freeLogin();
                } else {
                    apiServer.notFreeLogin();
                }
                break;
            case APIServer.EMAIL:
                if (isFreeEmail) {
                    apiServer.freeEmail();
                } else {
                    apiServer.notFreeEmail();
                }
                break;
            case APIServer.FIND_EMAIL:
                if (findEmail) {
                    apiServer.foundEmail();
                } else {
                    apiServer.notFoundEmail();
                }
                break;
            case APIServer.CHECK_VERIFY_CODE:
                if (okVerifyCode) {
                    apiServer.okVerifyCode(token);
                } else {
                    apiServer.wrongVerifyCode();
                }
                break;
        }
    }
}
