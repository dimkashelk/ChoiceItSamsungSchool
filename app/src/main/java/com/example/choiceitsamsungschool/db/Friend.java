package com.example.choiceitsamsungschool.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @ColumnInfo
    public Integer age;

    @ColumnInfo
    public Integer count_surveys;

    @ColumnInfo
    public Integer count_friends;

    @Ignore
    public String image_url;

    public Friend() {
    }

    public Friend(String friend_id, String first_name, String second_name) {
        this.friend_id = friend_id;
        this.first_name = first_name;
        this.second_name = second_name;
    }
}
