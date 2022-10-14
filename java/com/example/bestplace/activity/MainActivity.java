package com.example.bestplace.activity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bestplace.R;
import com.example.bestplace.adapter.PostAdapter;
import com.example.bestplace.db_room.postR.Post;
//import com.example.bestplace.db_room.postR.Roomdb;
import com.example.bestplace.db_room.postR.Roomdb;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//Main activity of this APP
public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    //variables of ui
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;

    //varible of initialize RV
    LinearLayoutManager llm;
    PostAdapter p_adapter;
    List<Post> postList=new ArrayList<>();
    //Post database
    Roomdb database;

    // Onactivityresult-> deprecated
    //the following solution found on
    // :https://stackoverflow.com/questions/62371106/onactivityresult-method-is-deprecated-what-is-the-alternative
    //on result activity i get the gson with new Post data and set them into db and adapter
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK ) {
                Intent data = result.getData();
                Post p = new Gson().fromJson(data.getStringExtra("POST"), Post.class);
                database.postdao().insert(p);
                postList.clear();
                postList.addAll(database.postdao().getall());
                p_adapter.notifyDataSetChanged();
            }
        }
    });

    //On create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(this);

        //init db
        database= Roomdb.getInstance(this);
        //find rv
        recyclerView = findViewById(R.id.recycler_viiew);

        //init RV
        init_recyclerview();

        //Add all posts from db into adapter
        postList.addAll(database.postdao().getall());
        p_adapter.notifyDataSetChanged();

    }

    //init the RV.
    private void init_recyclerview() {
        llm = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(llm);
        p_adapter = new PostAdapter(this,postList,database);
        recyclerView.setAdapter(p_adapter);
    }


    //Onnavigation item selected , change post in a gson and send it on the Edit_new_post.activity
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bt_add) {
            Post post= new Post();
            post.setID(postList.size()+1);
            Log.d("R_CONTROL", "CLICCATO ");

            Intent intent = new Intent(MainActivity.this, Edit_new_post.class);
            intent = intent.putExtra("POST", new Gson().toJson(post));
            someActivityResultLauncher.launch(intent);
        }
        return false;
    }
}