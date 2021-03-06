package com.team.hcdh.albumphoto.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.model.HinhNew;
import com.team.hcdh.albumphoto.view.EditNoteActivity;

import java.util.List;

public class EditHinhAdapter extends BaseAdapter {
    private EditNoteActivity context;
    private int layout;
    private List<HinhNew> arr;
    HinhNew hinh;

    int vitri;

    public EditHinhAdapter(EditNoteActivity context, int layout, List<HinhNew> arr) {
        this.context = context;
        this.layout = layout;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView img_view_new;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.img_view_new = (ImageView) view.findViewById(R.id.img_view_new);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        hinh = arr.get(i);
        vitri = i;

        // chuyển byte[] sang bitmap
        byte[] hinhanh = hinh.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);

        holder.img_view_new.setImageBitmap(bitmap);

        return view;
    }
}
