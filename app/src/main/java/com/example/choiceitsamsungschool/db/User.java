package com.example.choiceitsamsungschool.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "first_name")
    public String first_name;

    @ColumnInfo(name = "second_name")
    public String second_name;

    @ColumnInfo(name = "profile_image")
    public String profile_image;

    public User(String first_name, String second_name, String profile_image) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.profile_image = profile_image;
    }
}
