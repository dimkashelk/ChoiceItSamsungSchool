package com.example.choiceitsamsungschool.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Friend {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "friend_id")
    public String friend_id;

    @ColumnInfo(name = "first_name")
    public String first_name;

    @ColumnInfo(name = "second_name")
    public String second_name;

    @ColumnInfo(name = "profile_image")
    public String profile_image;

    public Friend() {
    }

    public Friend(String friend_id, String first_name, String second_name, String profile_image) {
        this.friend_id = friend_id;
        this.first_name = first_name;
        this.second_name = second_name;
        this.profile_image = profile_image;
    }
}
