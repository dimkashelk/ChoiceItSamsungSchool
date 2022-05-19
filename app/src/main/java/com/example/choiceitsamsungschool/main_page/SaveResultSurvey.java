package com.example.choiceitsamsungschool.main_page;

import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaveResultSurvey extends AsyncTask<String, Boolean, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private APIServer apiServer = APIServer.getSingletonAPIServer();
    private HashMap<String, Integer> map;

    public SaveResultSurvey(HashMap<String, Integer> map) {
        this.map = map;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * SAVE RESULT OF SURVEY
         * @param login
         * @param token
         */
        try {
            RequestBody body;
            Request request;
            JSONObject jsonObject = new JSONObject(map);
            String json = jsonObject.toString();
            body = RequestBody.create(json, APIServer.JSON);
            request = new Request.Builder()
                    .url(APIServer.URL + APIServer.UPDATE_USER_DATA)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean) {
            apiServer.dontSaveSurveyRes();
        }
    }
}
