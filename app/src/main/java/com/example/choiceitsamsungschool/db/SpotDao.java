package com.example.choiceitsamsungschool.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SpotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSpot(SpotDB spotDB);

    @Query("select * from SpotDB")
    public List<SpotDB> getAllSpot();

    @Query("select * from SpotDB where survey_id = :surveyId")
    public List<SpotDB> getSpot(String surveyId);

    @Query("delete from SpotDB")
    void removeAllSpot();
}
