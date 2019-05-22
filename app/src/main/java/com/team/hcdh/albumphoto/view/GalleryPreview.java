package com.team.hcdh.albumphoto.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.support.v4.view.ViewPager;
import com.team.hcdh.albumphoto.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryPreview extends AppCompatActivity {

    int position;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    private ArrayList<HashMap<String, String>> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.gallery_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(0, 0);
            }
        });

        Intent intent = getIntent();
        position = Integer.parseInt(intent.getStringExtra("position"));
        images = (ArrayList<HashMap<String, String>>)intent.getSerializableExtra("images");
        viewPager = (ViewPager)findViewById(R.id.viewPager);

//        page.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                //this will log the page number that was click
//                onBackPressed();
//                getSupportActionBar().hide();
//            }
//        });
        //adapter = new ViewPagerAdapter(GalleryPreview.this,images, 2);
        ActionBar actionBar =getSupportActionBar();
        adapter = new ViewPagerAdapter(GalleryPreview.this,images,actionBar);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        int index = images.get(position).get("path").lastIndexOf("/");
        String fileName = images.get(position).get("path").substring(index + 1);
        getSupportActionBar().setTitle(fileName);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                int index = images.get(position).get("path").lastIndexOf("/");
                String fileName = images.get(position).get("path").substring(index + 1);
                getSupportActionBar().setTitle(fileName);
            }
        });
    }
}
