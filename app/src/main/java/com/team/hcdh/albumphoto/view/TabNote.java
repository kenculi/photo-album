package com.team.hcdh.albumphoto.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.controller.Database;
import com.team.hcdh.albumphoto.controller.NoteAdapter;
import com.team.hcdh.albumphoto.controller.NoteDataSource;
import com.team.hcdh.albumphoto.model.Note;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class TabNote extends Fragment implements AdapterView.OnItemClickListener {
    private Toolbar toolBar;
    private TextView lblNoContent;
    private ProgressBar proLoading;
    private ListView lstNote;
    private ArrayList<Note> arr;
    private NoteAdapter adapter;
    public static Database database;
    private NoteDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.activity_main_note, container, false);

        FloatingActionButton newNote = (FloatingActionButton) rootView.findViewById(R.id.new_note);
        newNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewNoteActivity.class);
                getActivity().startActivity(intent);
            }
        });
        dataSource = new NoteDataSource();
        proLoading = (ProgressBar) rootView.findViewById(R.id.prd_load);
        lblNoContent = (TextView) rootView.findViewById(R.id.lbl_no_content);

        lstNote = (ListView) rootView.findViewById(R.id.lst_note);
        arr = new ArrayList<>();
        adapter = new NoteAdapter(getActivity(), arr);
        lstNote.setAdapter(adapter);

        database = new Database(getActivity(), "QuanLy.sqlite", null, 1);
        database.QueryData("CREATE TABLE IF NOT EXISTS NhatKy(Id VARCHAR(50) PRIMARY KEY, TieuDe VARCHAR(100), NoiDung VARCHAR(250), ThoiGian VARCHAR(100) )");
        database.QueryData("CREATE TABLE IF NOT EXISTS MangHinhAnh(Id INTEGER PRIMARY KEY AUTOINCREMENT, IdRef VARCHAR(50), HinhAnh BLOB )");

        // read data from DB
        viewAllNotes();

        // bắt sự kiện khi người dùng click
        lstNote.setOnItemClickListener(this);
        return rootView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (arr != null && arr.size() > 0) {
                proLoading.setVisibility(View.INVISIBLE);
                lblNoContent.setVisibility(View.INVISIBLE);
                lstNote.setVisibility(View.VISIBLE);
                // view tất cả các note của list
                NoteAdapter adapter = new NoteAdapter(getActivity(), arr);
                lstNote.setAdapter(adapter);
            } else {
                proLoading.setVisibility(View.INVISIBLE);
                lstNote.setVisibility(View.INVISIBLE);
                lblNoContent.setVisibility(View.VISIBLE);
            }
        }
    };

    private void viewAllNotes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                arr = dataSource.GetAllNotes();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // đọc tất cả các note từ db
        viewAllNotes();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ViewNoteActivity.class);
        intent.putExtra("note_id", arr.get(position).getId());
        intent.putExtra("title", arr.get(position).getTieuDe());
        intent.putExtra("content", arr.get(position).getNoiDung());
        intent.putExtra("date", arr.get(position).getThoiGian());
        startActivity(intent);
    }
}
