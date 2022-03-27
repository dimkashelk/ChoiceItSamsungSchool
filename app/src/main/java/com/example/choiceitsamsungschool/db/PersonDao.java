package com.example.choiceitsamsungschool.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPerson(Person person);

    @Query("select * from Person")
    public List<Person> getAllPersons();

    @Query("select * from Person where id = :personId")
    public List<Person> getPerson(long personId);

    @Query("select * from Person where person_id = :personId")
    public List<Person> getPerson(String personId);

    @Query("select * from Person where is_search")
    public List<Person> getResults();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePerson(Person person);

    @Query("delete from Person")
    void removeAllPersons();
}