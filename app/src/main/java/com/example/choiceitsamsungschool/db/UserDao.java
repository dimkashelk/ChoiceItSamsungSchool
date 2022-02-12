package com.example.choiceitsamsungschool.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User friend);

    @Query("select * from User")
    public List<User> getAllUsers();

    @Query("select * from User where id = :userId")
    public List<User> getFriend(long userId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User friend);

    @Query("delete from User")
    void removeAllUsers();
}