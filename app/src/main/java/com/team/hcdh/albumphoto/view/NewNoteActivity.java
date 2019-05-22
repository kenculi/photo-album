package com.team.hcdh.albumphoto.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.team.hcdh.albumphoto.R;
import com.team.hcdh.albumphoto.controller.HinhAdapter;
import com.team.hcdh.albumphoto.model.HinhNew;
import com.team.hcdh.albumphoto.utilily.Util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity {
    public final int REQUEST_CODE_FOLDER = 1;
    public final int REQUEST_CODE_CAMERA = 2;
    private Toolbar toolBar;
    private ImageView img_attach;
    private Bitmap bitmap;
    private EditText edt_title;
    private EditText edt_content;
    private GridView lv_img;
    byte[] hA;
    ArrayList<HinhNew> arr;
    HinhAdapter adapter;
    public static final int MESSAGE_INSERT_DONE = 1995;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        AnhXa();
        AnhXaHandler();
        lv_img.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // khi click vào hình thì đưa lên img_attach
                byte[] get_hinh = arr.get(i).getHinh();
                Bitmap bitmap = BitmapFactory.decodeByteArray(get_hinh, 0, get_hinh.length);
                img_attach.setImageBitmap(bitmap);


            }
        });
        lv_img.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                DialogXoa(i);
                return false;
            }
        });
    }

    // lang nghe va cap nhat view
    private void AnhXaHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_INSERT_DONE:
                        Toast.makeText(NewNoteActivity.this, "Thêm nhật ký ảnh thành công!", Toast.LENGTH_LONG).show();
                        NewNoteActivity.this.finish();
                        break;
                    default:
                        Toast.makeText(NewNoteActivity.this, "Không có handler phù hợp!", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    public void AnhXa() {
        // find views
        edt_content = (EditText) findViewById(R.id.edt_content);
        edt_title = (EditText) findViewById(R.id.edt_title);
        img_attach = (ImageView) findViewById(R.id.img_attach);
        lv_img = (GridView) findViewById(R.id.gv_img);

        arr = new ArrayList<>();
        adapter = new HinhAdapter(this, R.layout.dong_view_image, arr);
        lv_img.setAdapter(adapter);

        toolBar = (Toolbar) findViewById(R.id.my_toolbar);
        toolBar.setTitle("Thêm nhật ký ảnh");
        setSupportActionBar(toolBar);
    }

    public void DialogXoa(final int vitri_nhan) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn chắc chắn xóa chứ?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                arr.remove(vitri_nhan);
                adapter.notifyDataSetChanged();
                int tong = arr.size();
                if (tong > 0) {
                    for (int i = 1; i <= arr.size(); i++) {
                        byte[] thaythe_xoa = arr.get(0).getHinh();
                        Bitmap bitmap_thaythe_xoa = BitmapFactory.decodeByteArray(thaythe_xoa, 0, thaythe_xoa.length);
                        img_attach.setImageBitmap(bitmap_thaythe_xoa);
                    }
                } else {
                    img_attach.setVisibility(View.GONE);
                }
            }
        });

        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogXoa.show();
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
                if (edt_title.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Nhập tiêu đề nhật ký", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (edt_content.getText().toString().trim().length() <= 0) {
                    Toast.makeText(this, "Nhập nội dung nhật ký", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (arr.size() == 0) {
                    Toast.makeText(this, "Bạn chưa chọn ảnh nhật ký", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Insert();
                break;

            case R.id.attach_image:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOLDER);
                break;

            case R.id.camera_image:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intentCamra = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamra, REQUEST_CODE_CAMERA);
                } else {
                    Toast.makeText(this, "Không cho phép mở camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_FOLDER:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), REQUEST_CODE_FOLDER);
                } else {
                    Toast.makeText(this, "Không cho phép truy cập ảnh", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Không có ảnh nào được chọn!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    bitmap = BitmapFactory.decodeStream(bufferedInputStream);
                    img_attach.setImageBitmap(bitmap);
                    img_attach.setVisibility(View.VISIBLE);

                    BitmapDrawable bitmapDrawable = (BitmapDrawable) img_attach.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    new ChuyenBitSangByte_Folder().execute(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Không có ảnh nào được chọn!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
                img_attach.setImageBitmap(bitmap1);
                img_attach.setVisibility(View.VISIBLE);

                // chuyển bitmap sang byte
                BitmapDrawable bitmap_chup = (BitmapDrawable) img_attach.getDrawable();
                Bitmap bitmap_chuphinh1 = bitmap_chup.getBitmap();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap_chuphinh1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] hA_chup = byteArrayOutputStream.toByteArray();
                arr.add(new HinhNew(hA_chup));
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void Insert() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String TieuDe = edt_title.getText().toString().trim();
                String NoiDung = edt_content.getText().toString().trim();
                String ThoiGian = Util.getCurrentDateTime();
                String Id = Util.convertStringDatetimeToFileName(ThoiGian);

                for (int i = 0; i < arr.size(); i++) {
                    hA = arr.get(i).getHinh();
                    TabNote.database.Insert_Image(Id, hA);
                }
                TabNote.database.QueryData("INSERT INTO NhatKy VALUES('" + Id + "','" + TieuDe + "','" + NoiDung + "','" + ThoiGian + "')");

                // xét view ve main thông qua handler
                Message message = new Message();
                // gửi thông báo
                handler.sendEmptyMessage(MESSAGE_INSERT_DONE);

                // --------------gửi nội dung về
                // tiêu đề nơi đến
                //message.what = MESSAGE_INSERT;
                // arg1: luu tru, arg2: gui đi
                // chuyen kq ve
                // message.arg1 = insert_result;
                // gui buc thu di
                // handler.sendMessage(message);
            }
        });
        thread.start();
    }

    private class ChuyenBitSangByte_Folder extends AsyncTask<Bitmap, Void, byte[]> {
        byte[] kq;

        @Override
        protected byte[] doInBackground(Bitmap... bitmaps) {
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            kq = byteArray.toByteArray();
            return kq;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            arr.add(new HinhNew(kq));
            adapter.notifyDataSetChanged();
            super.onPostExecute(bytes);
        }
    }
}
