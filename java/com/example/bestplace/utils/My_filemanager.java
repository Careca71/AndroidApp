package com.example.bestplace.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.example.bestplace.db_room.postR.Post;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//myfilemanager is a utility class that implements some methods for
// manage(create/read/write/delete) files
public class My_filemanager {
    private static Activity context;
    Post post;

    //constructor
    public My_filemanager(Activity context, Post post) {
        this.context = context;
        this.post = post;
    }

    //this method white on a file in getexternadir(DITRECTORY_DOCUMENTS)
    //the uri of gallery images
    public static void write_on_file(File gallerytxt, Uri uri,int replace) {
        FileOutputStream fileOutputStream = null;
        String uri_img = uri.toString();
        boolean append=true;
        try {
            if(replace == 0){
                fileOutputStream = new FileOutputStream(gallerytxt, append);
            }else {
                fileOutputStream = new FileOutputStream(gallerytxt, false);
            }
            fileOutputStream.write((uri_img + "\n").getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this method read on a file in getexternadir(DITRECTORY_DOCUMENTS)
    //the uri of gallery images and store in a string list
    public void read_from_file(String path, List<String> uri_list, String del_string) {

        FileInputStream fileInputStream = null;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + path);

        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String t;
                while ((t = bufferedReader.readLine()) != null) {
                    if(del_string==null || !(del_string.equals(t))){
                        uri_list.add(t);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //this method create a new file txt. whew i will save uri of the gallery
    //images
    public File create_fileuri_txt(int id) {
        File gallery_txt = null;
        if (id == 0) {
            id = post.getID();
        }
        String imageFileName = "POST" + id + ".txt";
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
        File storageDir = new File(path);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        gallery_txt = new File(path, imageFileName);
        return gallery_txt;
    }


    //this method create a dir DCIM and new file.jpeg
    public File createDir_andPhoto_File(int count) {
        File image = null;
        // Create an image file name
        String imageFileName = "PHOTO" + post.getID() + "_" + count + ".jpg";
        String path = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/" + "POST" + post.getID();
        File storageDir = new File(path);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        image = new File(path, imageFileName);
        return image;
    }

    //this method deletes all photos from DCIM and also the txt file
    public void delete_all_photos() {
        File storagedir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/" + "POST" + post.getID());
        if (storagedir.exists()) {
            new Thread(() -> {
                List<File> listFiles = Arrays.asList(storagedir.listFiles());
                for (int i = 0; i < listFiles.size(); i++) {
                    listFiles.get(i).delete();
                }
                storagedir.delete();
            }).start();
        }
        File txtfile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + "POST" + post.getID() + ".txt");

        if (txtfile.exists()) txtfile.delete();
    }
    //this method get all photo from a dcim dir
    public void update_all_photos(List<String> urilist) {
        File storagedir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/" + "POST" + post.getID());
        if (storagedir.exists()) {
            new Thread(() -> {
                List<File> listFiles = Arrays.asList(storagedir.listFiles());
                for (int i = 0; i < listFiles.size(); i++) {
                    urilist.add(FileProvider.getUriForFile(context, "com.example.android.fileprovider", listFiles.get(i)).toString());
                }
                storagedir.delete();
            }).start();
        }
    }

//this method control if the images is from gallery or taked by the camera and also delete them
    public void delete_image(String uri, int id) {
        if(uri.contains(".jpg")){
        String[] temp = (uri.split("content://com.example.android.fileprovider/my_images/DCIM/"));
            String dir_path = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + "/";
            String uri_img = Uri.parse(dir_path + temp[1]).toString();
            File file = new File(uri_img);
            if (file.exists()) {
                file.delete();
            }
        }else {
            String path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString() + "/"+"POST"+id+".txt";
            List<String> list = new ArrayList<>();
                read_from_file("POST"+id+".txt", list,uri);
                File file_temp = new File(path);
                file_temp.delete();
                for (int i = 0; i < list.size(); i++) {
                    Log.d("R_CONTROL", "delete_image: "+Uri.parse(list.get(i)));
                        write_on_file(file_temp, Uri.parse(list.get(i)),0);
                }
        }

    }
}


