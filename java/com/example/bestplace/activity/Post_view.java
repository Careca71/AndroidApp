package com.example.bestplace.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bestplace.R;
import com.example.bestplace.db_room.postR.Post;
import com.example.bestplace.utils.My_filemanager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
//this activity show the post and the images of the associated gallery
public class Post_view extends AppCompatActivity {
    //variable for the ui
    TextView textView_title,textView_location;
    //post
    Post post;
    //list of uri images
    List<String> img_uri=new ArrayList<>();
    //myfilemanager to read the uri from the file.txt and from the directory
    My_filemanager my_filemanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the view of the layour
        setContentView(R.layout.activity_post_view);
        textView_title=findViewById(R.id.view_post_title);
        textView_location=findViewById(R.id.view_post_location);
        //get the intent and the post from MAinactivity
        Intent data=getIntent();
        post= new Gson().fromJson(data.getStringExtra("POST"), Post.class);
        if(post!=null) {
            //set text of the views
            textView_title.setText(post.getTitle().toString());
            textView_location.setText(post.getLocation().toString());

            //init filemanager
            my_filemanager = new My_filemanager(this, post);

            //Path read the photo from file
            String path="POST"+post.getID()+".txt";
            my_filemanager.read_from_file("POST"+post.getID()+".txt",img_uri,null);
            //update photo from the directory of the post
            my_filemanager.update_all_photos(img_uri);

            //init the galleryfragment
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.view_images,new Gallery_fragment(this,post,img_uri)).commit();
            }
        }
    }
}