package com.app.demotraveler;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class PostsActivity extends AppCompatActivity {

    private Toolbar mToolbat;
    private ImageButton NormalPosts,NormalPosts2,NormalPosts3,NormalPosts4;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);


        mToolbat= (Toolbar) findViewById(R.id.posts_page_toolbar);
        setSupportActionBar(mToolbat);
        getSupportActionBar().setTitle("New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NormalPosts = (ImageButton) findViewById(R.id.normal_post_btn1);
        NormalPosts2= (ImageButton) findViewById(R.id.normal_post_btn2);
        NormalPosts3= (ImageButton) findViewById(R.id.normal_post_btn3);
        NormalPosts4= (ImageButton) findViewById(R.id.normal_post_btn4);



        NormalPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToNormalPostsActivity();
            }
        });



        NormalPosts2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToSchActivity();
            }
        });


        NormalPosts3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToQAActivity();
            }
        });

        NormalPosts4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToGoingActivity();
            }
        });

    }

    private void SendUserToGoingActivity() {
        Intent mainIntent2 = new Intent(PostsActivity.this, GoingActivity.class);
        startActivity(mainIntent2);
    }

    private void SendUserToSchActivity() {

        Intent mainIntent2 = new Intent(PostsActivity.this, SchPostsActivity.class);
        startActivity(mainIntent2);
    }

    private void SendUserToQAActivity() {
        Intent mainIntent = new Intent(PostsActivity.this, QAActivity.class);
        startActivity(mainIntent);

    }

    private void SendUserToNormalPostsActivity() {


        Intent mainIntent3 = new Intent(PostsActivity.this, NormalPostsActivity.class);
        startActivity(mainIntent3);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            SendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }



    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(PostsActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }





}
