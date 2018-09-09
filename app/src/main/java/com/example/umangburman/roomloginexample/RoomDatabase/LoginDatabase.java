package com.example.umangburman.roomloginexample.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {LoginTable.class}, version = 1, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    public abstract LoginDao loginDoa();

    private static LoginDatabase INSTANCE;

    public static LoginDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (LoginDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            context, LoginDatabase.class, "LOGIN_DATABASE")
                            .fallbackToDestructiveMigration()
                            .build();

                }

            }

        }

        return INSTANCE;

    }

}
