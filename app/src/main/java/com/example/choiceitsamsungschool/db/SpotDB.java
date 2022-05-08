package com.example.choiceitsamsungschool.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SpotDB {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "spot_id")
    public String spot_id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo
    public int survey_id;

    @ColumnInfo
    public int count_voice = 0;

    public SpotDB() {
    }

    public SpotDB(String spot_id, String title, int survey_id) {
        this.spot_id = spot_id;
        this.title = title;
        this.survey_id = survey_id;
    }

    public SpotDB(String spot_id, String title, int survey_id, int count_voice) {
        this.spot_id = spot_id;
        this.title = title;
        this.survey_id = survey_id;
        this.count_voice = count_voice;
    }
}
