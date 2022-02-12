package com.example.choiceitsamsungschool.main_page;

import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadData extends AsyncTask<String, Boolean, Boolean> {
    private OkHttpClient client = new OkHttpClient();

    // InputStream inputStream = response.body().byteStream();
    // Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * LOAD FRIENDS: PARAM, LOGIN, TOKEN
         * */
        RequestBody body;
        Request request;
        switch (strings[0]) {
            case APIServer.LOAD_FRIENDS:
                String json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_FRIENDS)
                        .post(body)
                        .build();
                break;
            default:
                request = new Request.Builder()
                        .url(APIServer.URL)
                        .build();
                break;
        }
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            switch (strings[0]) {
                case APIServer.LOAD_FRIENDS:
                    break;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    private void save_image() {
    }
}
