package com.example.bestplace.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestplace.utils.My_dialog;
import com.example.bestplace.utils.Mygeocode;
import com.example.bestplace.R;
import com.example.bestplace.db_room.postR.Post;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//The Edit_new_post Activity give at the user the possibility of create his posts.
public class Edit_new_post extends AppCompatActivity implements View.OnClickListener {

    //variable for the ui
    TextView tv_title, tv_position;
    Button bt_save;
    ImageButton bt_loc;

    //post
    Post post=new Post();
    //mygeocode , utility class that implements the method of fusedlocationprovider
    Mygeocode mygeocode;
    //mydialog, utility class that implements the view of different dialog
    My_dialog my_dialog;
    //list of the images uri
    List<String> img_uri=new ArrayList<>();

    //Oncreate method , i get the new post from the Mainactivity into a gson file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_post);

        Intent intent= getIntent();
        post = new Gson().fromJson(intent.getStringExtra("POST"), Post.class);
        //request the permission to write and store data.
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        //init the Fragment gallery
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new Gallery_fragment(this,post,img_uri)).commit();
        }
        //init views
        tv_title = findViewById(R.id.post_title);
        tv_position = findViewById(R.id.post_location);
        bt_loc = findViewById(R.id.bt_location);
        bt_save = findViewById(R.id.bt_save);
        bt_save.setOnClickListener(this);
        mygeocode=new Mygeocode(this,tv_position,post);
    }

    //On start method, when the ui is loaded , my geocode try to get the current position
    @Override
    protected void onStart() {
        super.onStart();
        //request and check the position permission
        if (ActivityCompat.checkSelfPermission(Edit_new_post.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Edit_new_post.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mygeocode.g_last_loc();
        } else {
            ActivityCompat.requestPermissions(Edit_new_post.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            ActivityCompat.requestPermissions(Edit_new_post.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }

    }

    //ONclick method for the save button
    @Override
    public void onClick(View v) {
        Intent intent;
            //control the post title and set
            if(v.getId()==bt_save.getId()) {
                if (tv_title.getText()==null) {
                    Toast.makeText(this, "Insert title", Toast.LENGTH_SHORT).show();
                }else{
                    post.setTitle(tv_title.getText().toString());
                }
                //control the icon of the post
                if(post.getImg_uri()==null){
                    Toast.makeText(this, "Take a picture to continue!", Toast.LENGTH_SHORT).show();
                }
                //control if the mygeocode have finisced his work on the save click.
                //If the position is still null , i give at the user the possibility to set position manually
                if(post.getLocation()==null|| tv_position.getText()==null){
                    Toast.makeText(this, "Insert location to continue", Toast.LENGTH_SHORT).show();
                    my_dialog= new My_dialog(this,post);
                    my_dialog.set_manual_location();
                    tv_position.setText(post.getLocation());

                }

                //if all of the post data are set i give back the post completed
                if(post.getImg_uri()!=null && post.getLocation()!=null && post.getTitle()!=null) {
                    intent = new Intent();
                    intent = intent.putExtra("POST", new Gson().toJson(post));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
       if(tv_position.getText()!=null){
           if (ActivityCompat.checkSelfPermission(Edit_new_post.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Edit_new_post.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               mygeocode.g_last_loc();
           } else {
               ActivityCompat.requestPermissions(Edit_new_post.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
               ActivityCompat.requestPermissions(Edit_new_post.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
           }
       }
    }

    //Onsavedistancestate a put in a bundle the current information of the post.
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("title",tv_title.getText().toString());
        outState.putString("position",tv_position.getText().toString());
        outState.putInt("id",post.getID());
        super.onSaveInstanceState(outState);
    }
    //ONrestoreInstancestate ,restore the previws value of the post and the UI.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        tv_title.setText(savedInstanceState.get("title").toString());
        tv_position.setText(savedInstanceState.get("position").toString());
        int id= (int) savedInstanceState.get("id");
        post.setID(id);
        if(post.getTitle()==null)post.setTitle(tv_title.getText().toString());
        if(post.getLocation()==null)post.setLocation(tv_position.getText().toString());
        super.onRestoreInstanceState(savedInstanceState);
    }

}