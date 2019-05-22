package com.team.hcdh.albumphoto.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.controller.HinhViewAdapter;
import com.team.hcdh.albumphoto.model.HinhNew;
import com.team.hcdh.albumphoto.model.Note;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ViewNoteActivity extends AppCompatActivity {
    private Note model;
    private Toolbar toolBar;
    private TextView lblContent;
    private ImageView imgAttach;
    private GridView gv_img;
    private CheckBox chkTuDong;
    private Button btnTruoc, btnSau;
    String noteID;
    String title;
    String content;
    String datetime;
    int sogiay = 1;
    int vitri = -1;
    Integer position = 0;
    private ArrayList<HinhNew> arr;
    private HinhViewAdapter adapter;
    byte[] img_view;
    TimerTask timerTask;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        lblContent = (TextView) findViewById(R.id.lbl_content);
        imgAttach = (ImageView) findViewById(R.id.img_attach);
        gv_img = (GridView) findViewById(R.id.gv_img_view);
        chkTuDong = (CheckBox) findViewById(R.id.chkTuDong);
        btnTruoc = (Button) findViewById(R.id.btnTruoc);
        btnSau = (Button) findViewById(R.id.btnSau);

        gv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                vitri = i;
                byte[] h = arr.get(vitri).getHinh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(h, 0, h.length);
                imgAttach.setImageBitmap(bitmap);
            }
        });

        chkTuDong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnSau.setEnabled(false);
                    btnTruoc.setEnabled(false);
                    XuLyChayAnh();
                } else {
                    btnSau.setEnabled(true);
                    btnTruoc.setEnabled(true);
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        });

        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if (position < 0) {
                    position = arr.size() - 1;
                }
                new ChayHinhTask().execute(position);
            }
        });

        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position >= arr.size()) {
                    position = 0;
                }
                new ChayHinhTask().execute(position);
            }
        });

        // lấy dl truyền từ tabNote qua
        noteID = getIntent().getExtras().getString("note_id");
        title = getIntent().getExtras().getString("title");
        content = getIntent().getExtras().getString("content");
        datetime = getIntent().getExtras().getString("date");

        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle(title);
        toolBar.setSubtitle(datetime);
        setSupportActionBar(toolBar);

        arr = new ArrayList<>();
        adapter = new HinhViewAdapter(this, R.layout.dong_view_image, arr);
        gv_img.setAdapter(adapter);

        lblContent.setText(content);

        Cursor cursor = TabNote.database.GetData("SELECT * FROM MangHinhAnh WHERE IdRef = '" + noteID + "'");
        while (cursor.moveToNext()) {
            arr.add(new HinhNew(cursor.getBlob(2)));
        }
        for (int i = 0; i < arr.size(); i++) {
            img_view = arr.get(0).getHinh();
        }
        //Chuyen byte thanh bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(img_view, 0, img_view.length);
        imgAttach.setImageBitmap(bitmap);

        adapter.notifyDataSetChanged();
    }
    private void XuLyChayAnh() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            timerTask = null;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        position++;
                        if (position >= arr.size())
                            position = 0;
                        new ChayHinhTask().execute(position);
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, sogiay * 1000);
    }

    private class ChayHinhTask extends AsyncTask<Integer, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Integer... integers) {
            byte[] dsAnh = arr.get(integers[0]).getHinh();
            Bitmap bitmap_ds = BitmapFactory.decodeByteArray(dsAnh, 0, dsAnh.length);
            return bitmap_ds;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgAttach.setImageBitmap(bitmap);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
                dialogXoa.setMessage("Bạn chắc chắn xóa chứ?");
                dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TabNote.database.QueryData("DELETE FROM NhatKy WHERE Id = '" + noteID + "'");
                        TabNote.database.QueryData("DELETE FROM MangHinhAnh WHERE IdRef = '" + noteID + "'");
                        Toast.makeText(ViewNoteActivity.this, "Đã xóa nhật ký thành công", Toast.LENGTH_SHORT).show();
                        ViewNoteActivity.this.finish();
                    }
                });

                dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialogXoa.show();
                break;
            case R.id.edit:
                Intent intent = new Intent(ViewNoteActivity.this, EditNoteActivity.class);
                intent.putExtra("note_id_intent", noteID);
                intent.putExtra("title_intent", title);
                intent.putExtra("content_intent", content);
                startActivity(intent);
                ViewNoteActivity.this.finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
