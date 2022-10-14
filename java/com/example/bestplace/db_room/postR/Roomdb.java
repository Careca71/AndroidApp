package com.example.bestplace.db_room.postR;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Post.class}, version = 1,exportSchema = false)
public abstract class Roomdb extends RoomDatabase {
    //istanza del db
    private static Roomdb database;

    //db name
    private static String DATABASE_NAME = "database";

    public synchronized static Roomdb getInstance(Context context){

        if(database==null){
            //inizialize db
            database= Room.databaseBuilder(context.getApplicationContext()
                    , Roomdb.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    //create dao
    public abstract Postdao postdao();

}
