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

    public SpotDB() {
    }

    public SpotDB(String spot_id, String title, int survey_id) {
        this.spot_id = spot_id;
        this.title = title;
        this.survey_id = survey_id;
    }
}
