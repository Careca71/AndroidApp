package com.example.bestplace.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestplace.R;
import com.example.bestplace.activity.Edit_new_post;
import com.example.bestplace.activity.MainActivity;
import com.example.bestplace.activity.Post_view;
import com.example.bestplace.db_room.postR.Post;
import com.example.bestplace.db_room.postR.Roomdb;
import com.example.bestplace.utils.My_filemanager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//My post adapter! shows in the Mainactivity
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Activity context;
    List<Post> postList=new ArrayList<>();
    Roomdb database;
    //constructor take a database to delete or insert the item of my rv
    public PostAdapter(Activity context, List<Post> postList,Roomdb database) {
        this.context=context;
        this.postList=postList;
        this.database=database;
    }

    //oncreateview holder
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_of_post,parent,false);
        return new PostViewHolder(v);
    }
    //onbindviewholder
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if(postList.get(position).getImg_uri()!=null) {
            Uri uri = Uri.parse(postList.get(position).getImg_uri());
            holder.img_post.setImageURI(uri);
        }
        holder.title.setText(postList.get(position).getTitle());
        holder.loc.setText(postList.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    //Post view holder class
    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        ImageView img_post;
        TextView title,loc;
        ImageButton bt_share,bt_delete;
        My_filemanager my_filemanager;
        //in a costructor a set the view for all components of my item
        //and alte set the onclick view for some of them
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv);
            img_post=itemView.findViewById(R.id.item_img);
            title=itemView.findViewById(R.id.item_text);
            loc=itemView.findViewById(R.id.item_loc);
            bt_share=itemView.findViewById(R.id.bt_item_share);
            bt_delete=itemView.findViewById(R.id.bt_item_delete);
            bt_delete.setOnClickListener(this);
            bt_share.setOnClickListener(this);
            cv.setOnClickListener(this);
        }
        //Onclick for the button delete /share or if the user click on the item
        @Override
        public void onClick(View v) {
            //button delete I remove from adapter and DB the item selected
            if(v.getId()==bt_delete.getId()){
                Post post=postList.get(getAbsoluteAdapterPosition());
                postList.remove(post);
                database.postdao().delete(post);
                my_filemanager=new My_filemanager(context,post);
                my_filemanager.delete_all_photos();
                notifyItemRemoved(getAbsoluteAdapterPosition());
                notifyItemRangeChanged(getAbsoluteAdapterPosition(),postList.size());
                notifyDataSetChanged();
            }
            //Button share i share the item selected
            if(v.getId()==bt_share.getId()){
                Post post=postList.get(getAbsoluteAdapterPosition());
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM,Uri.parse(post.getImg_uri()));
                intent.putExtra(Intent.EXTRA_TEXT,"i'M ON "+post.getLocation()+" BESTPLACE_APP");
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(intent,"share"));
            }
            //If the user click on the CV i lunch the View_post.activity
            if(v.getId()==cv.getId()){
                Post post=postList.get(getAbsoluteAdapterPosition());
                Intent intent = new Intent(context, Post_view.class);
                intent = intent.putExtra("POST", new Gson().toJson(post));
                context.startActivity(intent);
            }
        }
    }

}