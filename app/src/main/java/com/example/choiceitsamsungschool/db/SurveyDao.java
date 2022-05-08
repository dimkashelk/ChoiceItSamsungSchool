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

    @Query("select * from Survey where survey_id = :surveyId")
    public List<Survey> getSurvey(String surveyId);

    @Query("select * from Survey where is_favorites")
    public List<Survey> getFavorites();

    @Query("select * from Survey where is_archive")
    public List<Survey> getArchives();

    @Query("select * from Survey where not is_archive and not is_favorites and not is_news")
    public List<Survey> getSurveys();

    @Query("select * from Survey where is_news")
    public List<Survey> getNews();

    @Query("select * from Survey where is_search")
    public List<Survey> getResult();

    @Query("select * from Survey where title like '%' || :search || '%'")
    public List<Survey> getAllSurvey(String search);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSurvey(Survey survey);

    @Query("delete from Survey")
    void removeAllSurvey();

    @Query("delete from Survey where is_news")
    void removeAllNews();

    @Query("select * from Survey where person_url = :person_id")
    List<Survey> getSurveysPerson(String person_id);
}