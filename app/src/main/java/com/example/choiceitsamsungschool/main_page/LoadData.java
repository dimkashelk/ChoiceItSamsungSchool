package com.example.choiceitsamsungschool.main_page;

import android.os.AsyncTask;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.db.Friend;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Vector;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadData extends AsyncTask<String, Boolean, Boolean> {
    private OkHttpClient client = new OkHttpClient();
    private int count_friends = 0;
    private Vector<Friend> friends;
    private APIServer apiServer;
    private String method;
    private Boolean logout = false;

    public LoadData(APIServer apiServer) {
        this.apiServer = apiServer;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        /**
         * LOAD FRIENDS
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * LOAD USER DATA
         * @param METHOD
         * @param LOGIN
         * @param TOKEN
         * */
        RequestBody body;
        Request request;
        String json;
        switch (strings[0]) {
            case APIServer.LOAD_FRIENDS:
                method = strings[0];
                friends = new Vector<>();
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_FRIENDS)
                        .post(body)
                        .build();
                break;
            case APIServer.LOAD_USER_DATA:
                json = "{'login': '" + strings[1] + "', " +
                        "'token': '" + strings[2] + "'}";
                body = RequestBody.create(json, APIServer.JSON);
                request = new Request.Builder()
                        .url(APIServer.URL + APIServer.LOAD_USER_DATA)
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
            if (!jsonObject.get("status").getAsBoolean()) {
                logout = true;
                return false;
            }
            switch (strings[0]) {
                case APIServer.LOAD_FRIENDS:
                    count_friends = jsonObject.get("count").getAsInt();
                    JsonArray friends_json = jsonObject.getAsJsonArray("friends");
                    for (int i = 0; i < friends.size(); i++) {
                        Friend friend = new Friend();
                        friend.friend_id = friends_json.get(i).getAsJsonObject().get("id").getAsString();
                        friend.first_name = friends_json.get(i).getAsJsonObject().get("first_name").getAsString();
                        friend.second_name = friends_json.get(i).getAsJsonObject().get("second_name").getAsString();
                        friend.image_url = friends_json.get(i).getAsJsonObject().get("image").getAsString();
                        friends.add(friend);
                    }
                    break;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            switch (method) {
                case APIServer.LOAD_FRIENDS:
                    apiServer.setFriendsList(friends);
            }
        } else {
            if (logout) {
                apiServer.logout();
            }
        }
    }
}
