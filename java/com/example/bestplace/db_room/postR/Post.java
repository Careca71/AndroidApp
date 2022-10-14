package com.example.bestplace.db_room.postR;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "postable")
public class Post {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "img_uri")
    private String img_uri;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "location")
    private String location;

    //getter and setter
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
