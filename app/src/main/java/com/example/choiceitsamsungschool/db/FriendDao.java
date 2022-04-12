package com.example.choiceitsamsungschool.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFriend(Friend friend);

    @Query("select * from Friend")
    public List<Friend> getAllFriend();

    @Query("select * from Friend where id = :friendId")
    public List<Friend> getFriend(long friendId);

    @Query("select age from Friend")
    public List<Integer> getAges();

    @Query("select * from Friend where (first_name || ' ' || second_name) like '%' || :search || '%'")
    public List<Friend> getAllFriend(String search);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFriend(Friend friend);

    @Query("delete from Friend")
    void removeAllFriends();
}