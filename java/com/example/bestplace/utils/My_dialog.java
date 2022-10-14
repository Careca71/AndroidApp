package com.example.bestplace.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bestplace.R;
import com.example.bestplace.adapter.Gallery_adapter;
import com.example.bestplace.db_room.postR.Post;

//class mydialog helps me to lunch a dialog in a edit new_post activity in case of missed location

public class My_dialog  implements View.OnClickListener {
    //activity and post
    Activity context;
    Post post;
   //ui of dialog
    EditText dialog_text;
    Button dialog_button;
    Dialog dialog;

//constructor
    public My_dialog(Activity context, Post post) {
       this.context=context;
       this.post=post;
     }
    //set manual location set from the dialog text
     public void set_manual_location(){
         dialog= new Dialog(context);
         dialog.setContentView(R.layout.dialog_update_location);
         int width = WindowManager.LayoutParams.MATCH_PARENT;
         int height= WindowManager.LayoutParams.WRAP_CONTENT;
         dialog.show();
         dialog_text=dialog.findViewById(R.id.edit_location);
         dialog_button=dialog.findViewById(R.id.dialog_button_updateloc);
         dialog_text.setOnClickListener(this);
         dialog_button.setOnClickListener(this);
     }
     //onclick
    @Override
    public void onClick(View v) {
        if(post!=null){
            if(v.getId()==dialog_button.getId()){
                post.setLocation(dialog_text.getText().toString().trim());
                dialog.dismiss();
            }
        }
    }


}
