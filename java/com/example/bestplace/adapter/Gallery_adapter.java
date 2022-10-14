package com.example.bestplace.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bestplace.R;
import com.example.bestplace.utils.My_dialog;
import com.example.bestplace.utils.My_filemanager;

import java.util.List;
//this is the adapter for the gallery fragment
public class Gallery_adapter extends RecyclerView.Adapter<Gallery_adapter.Gallery_VH> {

    int post_id;
    List<String> strings_img;
    Activity context;
    //contructor
    public Gallery_adapter(Activity context,List<String> strings_img,int post_id) {
        this.context=context;
        this.strings_img = strings_img;
        this.post_id=post_id;
    }

    //oncreateviewholder
    @NonNull
    @Override
    public Gallery_adapter.Gallery_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_images,parent,false);
        return new Gallery_adapter.Gallery_VH(v);
    }


    //Onbindviewholder i set the imageuri with glide.
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull Gallery_adapter.Gallery_VH holder, int position) {
       if(strings_img.get(position)!=null) {
           Uri uri = Uri.parse(strings_img.get(position));
           Glide.with(context).load(uri).apply(RequestOptions.centerCropTransform()).into(holder.item_img);
       }
    }

    @Override
    public int getItemCount() {
        return strings_img.size();
    }

    //class gallery view holder . in this class I set the holder for my adapter an other the long click on the images
    public class Gallery_VH extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener{
        ImageView item_img;
        Button bt_dialog_delete,bt_dialog_dismiss;
        Dialog dialog;

        //constructor
        public Gallery_VH(@NonNull View itemView) {
            super(itemView);
            item_img=itemView.findViewById(R.id.gallery_item);
            item_img.setOnLongClickListener(this);
        }

        //Onlongclick on the images i lunch dismiss_or_delete
        @Override
        public boolean onLongClick(View v) {
            if(v.getId()==item_img.getId()){
               dismiss_or_delete();
            }
            return true;
        }
        //In dismiss or delete a create a new dialog to confirm or dismiss the delete request
        public void dismiss_or_delete() {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_delete_or_share);
            int width = WindowManager.LayoutParams.MATCH_PARENT;
            int height= WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.show();
            bt_dialog_dismiss=dialog.findViewById(R.id.dialog_button_dismiss);
            bt_dialog_delete=dialog.findViewById(R.id.dialog_button_delete);
            bt_dialog_delete.setOnClickListener(this);
            bt_dialog_dismiss.setOnClickListener(this);
        }

        //on click on dialog buttons
        @Override
        public void onClick(View v) {
                if(v.getId()==bt_dialog_delete.getId()){
                    My_filemanager my_filemanager=new My_filemanager(context,null);
                    my_filemanager.delete_image(strings_img.get(getAbsoluteAdapterPosition()),post_id);
                    strings_img.remove(getAbsoluteAdapterPosition());
                    notifyItemRemoved(getAbsoluteAdapterPosition());
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
                if(v.getId()==bt_dialog_dismiss.getId()){
                    dialog.dismiss();
                }
        }
    }
}
