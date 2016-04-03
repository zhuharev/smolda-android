package com.smolda.god.sqlitetest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FeedView extends AppCompatActivity {

    final FeedRepo repo = new FeedRepo(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Intent intent = getIntent();
        Integer pos = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);

        TextView textView = (TextView) findViewById(R.id.feedView);
        TextView textTitle = (TextView) findViewById(R.id.feedTitle);
        TextView textViewDate = (TextView) findViewById(R.id.feedViewDate);


        Feed feed = repo.getFeedById(pos);

        textView.setText(feed.body);
        textTitle.setText(feed.title);
        textViewDate.setText(feed.published);
        if (feed.top_image!=""){
            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
            Boolean img_show = settings.getBoolean(setting.SHOW_IMAGES, true);
            if (img_show) {
                ImageView img = (ImageView) findViewById(R.id.imageView);
                Glide.with(this).load(feed.top_image).into(img);
            }
           // Glide.with(this).load(feed.top_image).diskCacheStrategy(Glide.cach);
        }
       // setTitle(feed.title);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
