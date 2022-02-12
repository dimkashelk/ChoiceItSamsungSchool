package com.example.choiceitsamsungschool;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.FriendDao;
import com.example.choiceitsamsungschool.db.User;
import com.example.choiceitsamsungschool.db.UserDao;

@Database(entities = {
        User.class,
        Friend.class
}, version = 16, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract FriendDao friendDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
