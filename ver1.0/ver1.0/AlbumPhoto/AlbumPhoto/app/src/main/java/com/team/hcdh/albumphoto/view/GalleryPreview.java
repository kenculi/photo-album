package com.team.hcdh.albumphoto.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.team.hcdh.albumphoto.R;

import java.util.ArrayList;
import java.util.HashMap;

public class GalleryPreview extends Activity {

    int position;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    private ArrayList<HashMap<String, String>> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.gallery_preview);
        Intent intent = getIntent();
        position = Integer.parseInt(intent.getStringExtra("position"));
        images = (ArrayList<HashMap<String, String>>)intent.getSerializableExtra("images");

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        //adapter = new ViewPagerAdapter(GalleryPreview.this,images, 2);
        adapter = new ViewPagerAdapter(GalleryPreview.this,images);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
