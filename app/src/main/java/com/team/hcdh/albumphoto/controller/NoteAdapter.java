package com.team.hcdh.albumphoto.controller;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.model.Image;
import com.team.hcdh.albumphoto.model.Note;
import com.team.hcdh.albumphoto.utilily.Util;
import com.team.hcdh.albumphoto.view.TabNote;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private List<Note> arr;
    private List<Image> arr_Image;
    private Random random;
    private String[] arrColor = {"#F0F8FF", "#FAEBD7", "#00FFFF", "#7FFFD4", "#F0FFFF",
                                "#F5F5DC", "#FFE4C4", "#000000", "#FFEBCD", "#0000FF",
                                "#8A2BE2", "#A52A2A", "#DEB887", "#5F9EA0", "#7FFF00",
                                "#D2691E", "#FF7F50", "#6495ED", "#FFF8DC", "#DC143C",
                                "#00FFFF", "#00008B", "#008B8B", "#B8860B", "#A9A9A9",
                                "#006400", "#BDB76B", "#8B008B", "#556B2F", "#FF8C00",
                                "#9932CC", "#8B0000", "#E9967A", "#8FBC8F", "#483D8B",
                                "#2F4F4F", "#00CED1", "#9400D3", "#FF1493", "#00BFFF",
                                "#1E90FF", "#FF0000", "#9ACD32", "#FFFF00", "#EE82EE",
                                "#008080", "#00FF7F", "#87CEEB", "#800080", "#DDA0DD",
                                "#98FB98", "#EEE8AA", "#CD853F", "#6B8E23"};

    public NoteAdapter(Context context, List<Note> arr) {
        this.context = context;
        this.arr = arr;
        random = new Random();
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        LinearLayout layoutMark;
        TextView lblTitle, lblContent;
        ImageView imgAttach;
        TextView lblDay, lblDate, lblTime;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.note_layout, viewGroup, false);

            holder.layoutMark = (LinearLayout) view.findViewById(R.id.layout_mark);
            holder.lblTitle = (TextView) view.findViewById(R.id.lbl_title);
            holder.lblContent = (TextView) view.findViewById(R.id.lbl_content);
            holder.imgAttach = (ImageView) view.findViewById(R.id.imageView);
            holder.lblDay = (TextView) view.findViewById(R.id.lbl_day);
            holder.lblDate = (TextView) view.findViewById(R.id.lbl_date);
            holder.lblTime = (TextView) view.findViewById(R.id.lbl_time);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.layoutMark.setBackgroundColor(Color.parseColor(arrColor[random.nextInt(arrColor.length)]));

        Note model = arr.get(i);

        String id = model.getId();

        holder.lblTitle.setText(model.getTieuDe());
        holder.lblContent.setText(model.getNoiDung());

        // set datetime
        Date date = Util.convertStringToDate(model.getThoiGian());
        String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
        String stringMonth = (String) DateFormat.format("MMM", date);
        String day = (String) DateFormat.format("dd", date);
        String time = (String) DateFormat.format("hh:mm", date);

        holder.lblDay.setText(dayOfTheWeek);
        holder.lblDate.setText(day + " " + stringMonth);
        holder.lblTime.setText(time);

        Cursor cursor = TabNote.database.GetData("SELECT * FROM MangHinhAnh WHERE IdRef = '" + id + "'");
        while (cursor.moveToNext()) {
            byte[] hinh = cursor.getBlob(2);
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0, hinh.length);
            holder.imgAttach.setImageBitmap(bitmap);
        }
        return view;
    }

}
