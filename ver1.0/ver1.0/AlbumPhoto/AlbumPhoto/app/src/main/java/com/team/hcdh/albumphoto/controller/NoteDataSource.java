package com.team.hcdh.albumphoto.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.team.hcdh.albumphoto.model.NoteModel;

import java.util.ArrayList;

public class NoteDataSource {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] allColumns = {NoteSQLiteHelper.COLUMN_ID, NoteSQLiteHelper.COLUMN_TITLE, NoteSQLiteHelper.COLUMN_NOTE,
            NoteSQLiteHelper.COLUMN_DATETIME, NoteSQLiteHelper.COLUMN_IMAGE};

    private Context context;

    public NoteDataSource(Context context) {
        this.context = context;
        sqLiteOpenHelper = new NoteSQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() throws SQLiteException {
        sqLiteOpenHelper.close();
    }

    public void addNewNote(String title, String note, String image, String datetime) {
        ContentValues values = new ContentValues();
        values.put(NoteSQLiteHelper.COLUMN_TITLE, title);
        values.put(NoteSQLiteHelper.COLUMN_NOTE, note);
        values.put(NoteSQLiteHelper.COLUMN_DATETIME, datetime);
        values.put(NoteSQLiteHelper.COLUMN_IMAGE, image);
        sqLiteDatabase.insert(NoteSQLiteHelper.TABLE_NAME, null, values);
    }

    public void deleteNote(int id) {
        sqLiteDatabase.delete(NoteSQLiteHelper.TABLE_NAME,
                NoteSQLiteHelper.COLUMN_ID + "=" + id, null);
        Toast.makeText(this.context, "Xóa nhật ký ảnh thành công!", Toast.LENGTH_LONG).show();
    }

    public ArrayList<NoteModel> getAllNotes() {
        ArrayList<NoteModel> arr = new ArrayList<NoteModel>();
        Cursor cursor = sqLiteDatabase.query(NoteSQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
        if (cursor == null) {
            return null;
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            NoteModel model = cursorToModel(cursor);
            arr.add(model);
            cursor.moveToNext();
        }
        return arr;
    }

    // get NoteModel from cursor
    private NoteModel cursorToModel(Cursor cursor) {
        NoteModel model = new NoteModel();
        model.id = cursor.getInt(0);
        model.title = cursor.getString(1);
        model.content = cursor.getString(2);
        model.datetime = cursor.getString(3);
        model.image = cursor.getString(4);
        return model;
    }
}
