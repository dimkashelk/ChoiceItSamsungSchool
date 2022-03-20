package com.example.choiceitsamsungschool.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSurvey(Survey survey);

    @Query("select * from Survey")
    public List<Survey> getAllSurvey();

    @Query("select * from Survey where id = :surveyId")
    public List<Survey> getSurvey(long surveyId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSurvey(Survey survey);

    @Query("delete from Friend")
    void removeAllSurvey();
}