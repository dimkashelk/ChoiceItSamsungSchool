package com.example.choiceitsamsungschool.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Survey {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "survey_id")
    public String survey_id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo
    public String title_image_url;

    @ColumnInfo
    public Boolean is_archive = false;

    @ColumnInfo
    public Boolean is_favorites = false;

    @ColumnInfo
    public Boolean is_news = false;

    @ColumnInfo
    public Boolean is_search = false;

    @ColumnInfo
    public String person_url;

    public Survey() {
    }

    public Survey(String survey_id, String title, String description) {
        this.survey_id = survey_id;
        this.title = title;
        this.description = description;
    }
}
