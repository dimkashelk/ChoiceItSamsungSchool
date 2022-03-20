package com.example.choiceitsamsungschool.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @Ignore
    public String image_url;

    public Survey() {
    }

    public Survey(String survey_id, String title, String description) {
        this.survey_id = survey_id;
        this.title = title;
        this.description = description;
    }
}
