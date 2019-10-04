package com.telewave.lib.base.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: rick_tan
 * @Date: 19-7-23
 * @Version: v1.0
 * @Des 照片工具类
 */
public class PhotoUtils {

    public static final int REQUEST_TAKE = 0;
    public static final int REQUEST_CHOOSE = 1;
    public static final int REQUEST_CROP = 2;
    private static final String TAG = "PhotoUtils";


    /**
     * 调用相机
     *
     * @param context
     * @param fileName 文件路径
     */
    public static void takePhoto(Activity context, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName)));
        context.startActivityForResult(intent, REQUEST_TAKE);
    }

    /**
     * 调用图库
     *
     * @param context
     */
    public static void choosePhoto(Activity context) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(i, REQUEST_CHOOSE);
    }

    /**
     * 裁剪图片
     *
     * @param context
     * @param uri
     * @param outputName
     */
    public static void cropPhoto(Activity context, Uri uri, String outputName) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        Uri outputUri = Uri.fromFile(new File(outputName));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        context.startActivityForResult(intent, REQUEST_CROP);
    }

    /**
     * 压缩图片
     *
     * @param fileSrc
     * @param fileDst
     * @param targetW
     * @param targetH
     */
    public static boolean compressPhoto(String fileSrc, String fileDst, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        // 计算缩放
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileSrc, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(fileSrc, bmOptions);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(fileDst));
            fos.write(stream.toByteArray());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取选中图片的地址
     *
     * @param context
     * @param data
     * @return
     */
    public static String getSelectedPath(Context context, Intent data) {
        Uri uriParam = data.getData();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(uriParam.getScheme());
        builder.authority(uriParam.getAuthority());
        builder.path(uriParam.getPath());
        Uri selectedImage = builder.build();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        //获取选择照片的数据视图
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        //从数据视图中获取已选择图片的路径
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }


    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
