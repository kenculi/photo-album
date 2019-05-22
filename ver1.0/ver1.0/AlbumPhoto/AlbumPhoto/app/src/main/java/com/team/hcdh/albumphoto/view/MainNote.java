package com.team.hcdh.albumphoto.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.controller.NoteAdapter;
import com.team.hcdh.albumphoto.controller.NoteDataSource;
import com.team.hcdh.albumphoto.model.NoteModel;

import java.util.ArrayList;

public class MainNote extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Toolbar toolBar;
    private ListView lstNote;
    private TextView lblNoContent;
    private ProgressBar proLoading;
    private NoteDataSource dataSource;
    private ArrayList<NoteModel> arrNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_note);

        lstNote = (ListView) findViewById(R.id.lst_note);
        proLoading = (ProgressBar) findViewById(R.id.prd_load);
        lblNoContent = (TextView) findViewById(R.id.lbl_no_content);

        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle("NHẬT KÝ ẢNH");
        setSupportActionBar(toolBar);

        // create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();

        // read data from DB
        viewAllNotes();

        // create adapter
        NoteAdapter adapter = new NoteAdapter(this, arrNotes);
        lstNote.setAdapter(adapter);
        lstNote.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_note:
                //nhảy sang gdien thêm note
                Intent intent = new Intent(this, NewNoteActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // update UI - display notes on listview
            if (arrNotes != null && arrNotes.size() > 0) {
                proLoading.setVisibility(View.INVISIBLE);
                lblNoContent.setVisibility(View.INVISIBLE);
                lstNote.setVisibility(View.VISIBLE);
                // view all note to listview
                NoteAdapter adapter = new NoteAdapter(MainNote.this, arrNotes);
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
                // read all notes form DB
                arrNotes = dataSource.getAllNotes();
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewAllNotes();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ViewNoteActivity.class);
        intent.putExtra("note_id", arrNotes.get(position).id);
        intent.putExtra("title", arrNotes.get(position).title);
        intent.putExtra("content", arrNotes.get(position).content);
        intent.putExtra("date", arrNotes.get(position).datetime);
        intent.putExtra("image", arrNotes.get(position).image);
        startActivity(intent);
    }
}
