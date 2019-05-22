package com.team.hcdh.albumphoto.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.controller.NoteDataSource;
import com.team.hcdh.albumphoto.utilily.Config;
import com.team.hcdh.albumphoto.utilily.Util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class NewNoteActivity extends AppCompatActivity {
    public final int PICK_PHOTO_FOR_NOTE = 0;
    private Toolbar toolBar;
    private ImageView imgAttach;
    private Bitmap bmpAttach;
    private EditText edtTitle;
    private EditText edtContent;
    private NoteDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // find views
        edtContent = (EditText) findViewById(R.id.edt_content);
        edtTitle = (EditText) findViewById(R.id.edt_title);
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        // get action bar
        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle("Thêm nhật ký ảnh");
        setSupportActionBar(toolBar);

        // get image attach
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        // create data source
        dataSource = new NoteDataSource(this);
        dataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                if (edtTitle.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Nhập tiêu đề nhật ký", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (edtContent.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Nhập nội dung nhật ký", Toast.LENGTH_SHORT).show();
                    return true;
                }
                String dateTime = Util.getCurrentDateTime();
                String imageName = Util.convertStringDatetimeToFileName(dateTime) + ".png";

                // save note to SQLite
                // truyền vào title, content, image, datetime
                dataSource.addNewNote(edtTitle.getText().toString(), edtContent.getText().toString(),
                        imageName, dateTime);

                // save image to SDcard
                if (bmpAttach != null) {
                    Util.saveImageToSDCard(bmpAttach, Config.FOLDER_IMAGES, imageName);
                }
                this.finish();
                Toast.makeText(this, "Thêm nhật ký ảnh thành công!", Toast.LENGTH_LONG).show();
                break;
            case R.id.attach_image:
                // Select image from gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                // ACTION_GET_CONTENT: chọn ra 1 nội dung gì đó
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // PICK_PHOTO_FOR_NOTE: backup
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_PHOTO_FOR_NOTE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    // xử lý trả về
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_FOR_NOTE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Không có ảnh nào được chọn!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                // get image from result
                // data là 1 ảnh
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bmpAttach = BitmapFactory.decodeStream(bufferedInputStream);

                // show image in screen
                imgAttach.setImageBitmap(bmpAttach);
                imgAttach.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSource.close();
    }
}
