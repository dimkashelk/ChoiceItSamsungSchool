package com.example.choiceitsamsungschool.db;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(indices = @Index(value = "person_id", unique = true))
public class Person {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "person_id")
    public String person_id;

    @ColumnInfo(name = "first_name")
    public String first_name;

    @ColumnInfo(name = "second_name")
    public String second_name;

    @ColumnInfo
    public int count_friends;

    @ColumnInfo
    public int count_surveys;

    @ColumnInfo
    public boolean is_search = false;

    @Ignore
    public Bitmap profile_image;

    public Person() {
    }

    public Person(String person_id, String first_name, String second_name) {
        this.person_id = person_id;
        this.first_name = first_name;
        this.second_name = second_name;
    }

    public Person(Friend friend) {
        this.person_id = friend.friend_id;
        this.first_name = friend.first_name;
        this.second_name = friend.second_name;
    }
}
