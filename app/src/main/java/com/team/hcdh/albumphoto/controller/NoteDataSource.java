package com.team.hcdh.albumphoto.controller;

import android.database.Cursor;

import com.team.hcdh.albumphoto.model.Image;
import com.team.hcdh.albumphoto.model.Note;
import com.team.hcdh.albumphoto.view.TabNote;

import java.util.ArrayList;

public class NoteDataSource {

    public ArrayList<Note> GetAllNotes() {
        ArrayList<Note> arr = new ArrayList<>();
        Cursor cursor = TabNote.database.GetData("SELECT * FROM NhatKy");
        Cursor cursorHinh = TabNote.database.GetData("SELECT * FROM MangHinhAnh");

        if (cursor.getCount() <= 0 || cursorHinh.getCount() <= 0) {
            return null;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note model = cursorToModel(cursor);
            arr.add(model);
            cursor.moveToNext();
        }

        cursorHinh.moveToFirst();
        String a = cursorHinh.getString(0);
        while (!cursorHinh.isAfterLast()) {
            Image img = cursorHinhToModel(cursorHinh);
            //arr.add(model);
            addHinhToNote(arr, img);
            cursorHinh.moveToNext();
        }

        return arr;
    }

    private Note cursorToModel(Cursor cursor) {
        Note model = new Note();
        model.Id = cursor.getString(0);
        model.TieuDe = cursor.getString(1);
        model.NoiDung = cursor.getString(2);
        model.ThoiGian = cursor.getString(3);
        return model;
    }

    private Image cursorHinhToModel(Cursor cursor) {
        Image img = new Image();
        img.IdRef = cursor.getString(1);
        img.HinhAnh = cursor.getBlob(2);
        return img;
    }

    private void addHinhToNote(ArrayList<Note> arrNote, Image img) {
        for (int i = 0; i < arrNote.size(); i++) {
            if (arrNote.get(i).Id == img.IdRef) {
                arrNote.get(i).arrImage.add(img.HinhAnh);
            }
        }
    }

    public ArrayList<byte[]> GetViewNote(String Id) {
        ArrayList<byte[]> arr = new ArrayList<>();
        Cursor cursor = TabNote.database.GetData("SELECT * FROM MangHinhAnh WHERE IdRef = " + Id);

        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            byte[] model = cursor.getBlob(2);
            arr.add(model);
            cursor.moveToNext();
        }
        return arr;
    }
}
