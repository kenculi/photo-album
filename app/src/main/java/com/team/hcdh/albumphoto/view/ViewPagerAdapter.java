package com.team.hcdh.albumphoto.view;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.team.hcdh.albumphoto.R;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPagerAdapter extends PagerAdapter{
    Activity activity;
    ArrayList<HashMap<String, String>> images = new ArrayList<HashMap<String, String>>();
    ActionBar actionBar;
    LayoutInflater inflater;
    private ImageView image;

    public ViewPagerAdapter(Activity activity, ArrayList<HashMap<String, String>> images, ActionBar actionBar) {
        this.activity = activity;
        this.images = images;
        this.actionBar = actionBar;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item,container,false);

        image = (ImageView)itemView.findViewById(R.id.imageView);

        image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
                if(actionBar.isShowing()){
                    actionBar.hide();
                } else
                actionBar.show();
            }
        });

        DisplayMetrics dis = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dis);
        int height = dis.heightPixels;
        int width = dis.widthPixels;
        image.setMinimumHeight(height);
        image.setMinimumWidth(width);

        try{
            Glide.with(activity.getApplicationContext())
                    .load(images.get(position).get("path"))
                    .into(image);
        }
        catch (Exception ex){
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View)object);
    }
}