package com.example.choiceitsamsungschool.main_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.InternalStorage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadImage extends AsyncTask<String, Void, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private InternalStorage internalStorage;
    private Bitmap bitmap;
    private String user_id;

    public LoadImage(InternalStorage internalStorage) {
        this.internalStorage = internalStorage;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        RequestBody body;
        Request request;
        String json = "{'is_profile': " + true + "}";
        body = RequestBody.create(json, APIServer.JSON);
        user_id = strings[0];
        request = new Request.Builder()
                .url(APIServer.URL + APIServer.LOAD_IMAGE + "/" + strings[0])
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
            jsonObject.get("image").getAsByte();
            InputStream inputStream = response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            internalStorage.save_user_profile_image(bitmap, user_id);
        }
    }
}
