package com.example.bestplace.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bestplace.R;
import com.example.bestplace.adapter.Gallery_adapter;
import com.example.bestplace.db_room.postR.Post;
import com.example.bestplace.utils.My_filemanager;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Result;


//this fragment will be used to show the gallery of the posts or to create new gallery for new posts
public class Gallery_fragment extends Fragment implements View.OnClickListener {
    //variable for the ui
    RecyclerView recyclerView;
    ImageButton bt_add_photo,bt_add_gallery;
    //variable to init RV
    LinearLayoutManager llm;
    Gallery_adapter gallery_adapter;
    List<String> img_list = new ArrayList<>();
    //context
    Activity context;
    //post
    Post post;
    //myfilemanager is a utility class for manage , insert and delete new posts
    My_filemanager my_filemanager;
    //two tipe of file :image from camera(jpg) and from gallery(txt)
    File image,gallerytxt=null;

    int count=0;

    //Onactivity result is deprecated , i found this solution on:
    //I got the result of ACTION_IMAGE_CAPTURE and add to the adapter
    //or for the result of ACTION_PICK and add to the adapter the uri of images

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result ) {
                    Uri uri;
                    if (result.getResultCode() == Activity.RESULT_OK ) {
                       //set the result of action_capture
                        if(image!=null){
                            uri= FileProvider.getUriForFile(context,"com.example.android.fileprovider",image);
                            if(post.getImg_uri()==null) {
                                post.setImg_uri(uri.toString());
                            }
                            count++;
                            img_list.add(uri.toString());
                            gallery_adapter.notifyDataSetChanged();
                        }else{
                            //result of action pick
                            Intent intent=result.getData();
                            uri=intent.getData();
                            if(uri!=null){
                                if(post.getImg_uri()==null) {
                                    post.setImg_uri(uri.toString());
                                }
                                my_filemanager.write_on_file(gallerytxt,uri,0);
                                img_list.add(uri.toString());
                                gallery_adapter.notifyDataSetChanged();
                            }

                        }
                    }

                }
            });


    //Constructor of Gallery Fragment. the context and post passed
    public Gallery_fragment(Activity context, Post post,List<String> img_list) {
        this.context = context;
        this.post = post;
        this.img_list=img_list;
    }

    //Oncreate method
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //Initializing the view of fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the view for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_gallery);
        bt_add_photo = (ImageButton) view.findViewById(R.id.bt_add_photo);
        bt_add_gallery=(ImageButton) view.findViewById(R.id.bt_add_fromgallery);
        bt_add_gallery.setOnClickListener(this);
        bt_add_photo.setOnClickListener(this);
        //init the recyclerview
        initialize_recyclerview();
        //new file manager
        my_filemanager=new My_filemanager(context,post);
        return view;
    }

    //init the RV
    private void initialize_recyclerview() {
        llm=new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);
        gallery_adapter= new Gallery_adapter(context,img_list,post.getID());
        recyclerView.setAdapter(gallery_adapter);
    }


    //Set onClick for add_photo Button and the add_from_gallery button
    @Override
    public void onClick(View v) {
        if (v.getId() == bt_add_photo.getId()) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                // Create the File where the photo should go
                   image =my_filemanager.createDir_andPhoto_File(count) ;
                // Continue only if the File was successfully created
                if (image != null) {
                    //get the path and send the intent
                    String s= image.getPath();
                    Uri photoURI = FileProvider.getUriForFile(context,"com.example.android.fileprovider",image);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //set the result on the photouri
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                    someActivityResultLauncher.launch(takePictureIntent);
                }
            }
        }if(v.getId()==bt_add_gallery.getId()){
            //set image= null to set the result of the action pick in the activity result
            image=null;
            Intent pickfromgallery= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if(pickfromgallery.resolveActivity(context.getPackageManager())!=null){
                //if i never created a gallerytxt of this post i create
                if(gallerytxt==null){
                    gallerytxt=my_filemanager.create_fileuri_txt(0);
                }
                //lunch the action_pick
                if (gallerytxt!=null){
                    someActivityResultLauncher.launch(pickfromgallery);
                }
            }
        }
    }

}



