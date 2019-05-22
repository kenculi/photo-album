package com.team.hcdh.albumphoto.utilily;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {
    public static boolean saveImageToInternalStorage(Context context, Bitmap image, String name) {
        try {
            FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            Log.e("saveImageToInternal", e.getMessage());
            return false;
        }
    }

    public static boolean saveImageToSDCard(Bitmap image, String folder, String name) {
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/";
        try {
            File dir = new File(fullPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            OutputStream fOut = null;
            File file = new File(fullPath, name);
            if (!file.exists()) {
                file.createNewFile();
            }
            fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return true;
        } catch (Exception e) {
            Log.e("saveImageToSDCard", e.getMessage());
            return false;
        }

    }

    public static Bitmap getImageFromMemory(Context context, String folder, String filename) {

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folder + "/";
        Bitmap image = null;

        // Look for the file on the external storage
        try {
            if (Util.isSDReadable() == true) {
                image = BitmapFactory.decodeFile(fullPath + "/" + filename);
            }
        } catch (Exception e) {
            Log.e("getImageFromMemory", e.getMessage());
        }

        // If no file on external storage, look in internal storage
        if (image == null) {
            try {
                File filePath = context.getFileStreamPath(filename);
                FileInputStream fi = new FileInputStream(filePath);
                image = BitmapFactory.decodeStream(fi);
            } catch (Exception ex) {
                Log.e("getImageFromMemory", ex.getMessage());
            }
        }
        return image;
    }

    // check readable ability of SD card
    private static boolean isSDReadable() {
        boolean mExternalStorageAvailable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = true;
            Log.i("isSdReadable", "External storage card is readable.");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            Log.i("isSdReadable", "External storage card is readable.");
            mExternalStorageAvailable = true;
        } else {
            mExternalStorageAvailable = false;
        }
        return mExternalStorageAvailable;
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = dateFormat.format(new Date());
        return datetime;
    }

    public static Date convertStringToDate(String datetime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(datetime);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static void setBitmapToImage(final Context context, final String folder, final String name, final ImageView imageView) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bitmap bitmap = (Bitmap) msg.obj;
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }
        };
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = Util.readImage(folder, name, context);
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                }
            });
            thread.start();
        } catch (Exception ex) {
        }
    }

    // read image from SD or Internal memory
    public static Bitmap readImage(String foler, String filename, Context context) {
        Bitmap img = null;
        // read image from SD card
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + foler + "/" + filename;
        try {
            img = BitmapFactory.decodeFile(fullPath);
        } catch (Exception e) {
            Log.i("DemoReadWriteImage", "Cannot read image from SD card");
        }
        // read image from internal memory
        try {
            File myFile = context.getFileStreamPath(filename);
            FileInputStream fIn = new FileInputStream(myFile);
            img = BitmapFactory.decodeStream(fIn);
        } catch (Exception e) {
            Log.i("DemoReadWriteImage", "Cannot read image from internal memory");
        }
        return img;
    }

    public static String convertStringDatetimeToFileName(String date) {
        return date.toString().replace(":", "").replace(" ", "").replace("-", "");
    }

}
