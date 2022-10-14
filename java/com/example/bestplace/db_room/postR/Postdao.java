package com.example.bestplace.db_room.postR;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface Postdao {

    //insert
    @Insert(onConflict = REPLACE)
    void insert (Post post);

    //delete
    @Delete
    void delete(Post post);

    @Query("UPDATE postable Set title= :new_title WHERE ID= :id")
    void update(int id,String new_title);

    @Query("SELECT * FROM postable")
    List<Post> getall();

}
