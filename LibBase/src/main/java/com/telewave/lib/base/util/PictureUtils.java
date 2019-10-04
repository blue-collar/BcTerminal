package com.telewave.lib.base.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Note: 图片工具
 * Created by Yuan on 2016/11/9,22:16.
 */

public class PictureUtils {

    private static String tempImagePath = null;

    /**
     * 得到图片的缩放比例
     *
     * @param options       图片选项
     * @param requireWidth  图片需求的宽度
     * @param requireHeight 图片需求的高度
     * @return 图片的缩放比例
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int requireWidth, int requireHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int sampleSize = 1;
        if (height > requireHeight || width > requireWidth) {
            final int heightRadio = Math.round(height / (float) requireHeight);
            final int widthRadio = Math.round(width / ((float) requireWidth));
            sampleSize = heightRadio > widthRadio ? heightRadio : widthRadio;
        }
        return sampleSize;
    }

    /**
     * 得到需要的图片
     *
     * @param filePath          图片的路径
     * @param requireWidth      要求的宽度
     * @param requireHeight     要求的高度
     * @param isRequireCompress 是否是需要压缩的
     * @return 图片文件
     */
    public static Bitmap getSmallBitmap(String filePath, int requireWidth, int requireHeight, boolean isRequireCompress) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, requireWidth, requireHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        try {
            if (isRequireCompress) {
                return correctPicOrientation(compressImage(bitmap), filePath);
            } else {
                return correctPicOrientation(bitmap, filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param filePath          图片的路径
     * @param inSampleSize      图片的缩放倍数
     * @param isRequireCompress 是否是需要压缩的
     * @return 图片文件
     */
    public static Bitmap getSmallBitmap(String filePath, int inSampleSize, boolean isRequireCompress) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        try {
            if (isRequireCompress) {
                return correctPicOrientation(compressImage(bitmap), filePath);
            } else {
                return correctPicOrientation(bitmap, filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getSmallBitmapJust(String filePath, int requireWidth, int requireHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeFile(filePath, options);
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inSampleSize = calculateInSampleSize(options, requireWidth, requireHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if ((requireHeight <= requireWidth && height <= width) || (requireHeight > requireWidth && height > width)) {
            return bitmap;
        } else {
            return rotatePicOrientation(bitmap, 270);
        }
    }


    /**
     * 质量压缩方法
     *
     * @param image 传入的bitmap对象
     * @return 压缩后的对象
     * @throws IOException IO异常
     */
    public static Bitmap compressImage(Bitmap image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 80, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
            if (options < 0) {
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        image = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        isBm.close();
        baos.close();
        return image;
    }

    /**
     * 删除临时图片文件
     *
     * @param path 图片的路径
     * @return 是否删除了图片
     */
    public static boolean deleteTempFile(String path) {
        File file = new File(path);
        boolean isDeleted = false;
        if (file.exists()) {
            isDeleted = file.delete();
        }
        return isDeleted;
    }

    /**
     * 删除临时图片文件
     *
     * @return 是否删除了图片
     */
    public static boolean deleteTempFile() {
        File file = new File(tempImagePath);
        boolean isDeleted = false;
        if (file.exists()) {
            isDeleted = file.delete();
        }
        tempImagePath = null;
        return isDeleted;
    }

    /**
     * 将图片添加到相册中
     *
     * @param context  上下文
     * @param filePath 图片的路径
     */
    public static void galleryAddPic(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录 (可以用来解决自定义目录没存在的情况)
     *
     * @param fileName 图片名字
     * @return
     */
    public static File getAlbumDir(String fileName) {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 创建一个临时的图片文件占个坑
     *
     * @param parentDir 文件要存储的父目录
     * @return
     */
    public static File createImageFile(String parentDir) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File file = new File(parentDir);
        boolean isDirCreated = true;
        //如果是自定义的目录不存在,就要自己手动创建了 file.mkdirs();
        if (!file.exists()) {
            isDirCreated = file.mkdirs();
        }
        if (isDirCreated) {
            try {

                File targetFile = File.createTempFile(imageFileName, ".jpg", file);
                tempImagePath = targetFile.getPath();
                return targetFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 对拍照后旋转的照片进行处理
     *
     * @param filePath 图片的路径
     * @return 图片的旋转角度
     */
    private static int readPictureDegree(String filePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 对旋转的图片进行矫正
     *
     * @param bitmap   图片对象
     * @param filePath 图片的路径
     * @return 矫正后的图片对象
     */
    private static Bitmap correctPicOrientation(Bitmap bitmap, String filePath) {
        Matrix matrix = new Matrix();
        int angle = readPictureDegree(filePath);
        if (angle != 0) {
            matrix.postRotate(angle);
            //当进行的不只是平移变换时，filter参数为true可以进行滤波处理，有助于改善新图像质量;flase时，计算机不做过滤处理
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     * 对旋转的图片进行旋转
     *
     * @param bitmap 图片对象
     *               //     * @param filePath 图片的路径
     * @return 矫正后的图片对象
     */
    private static Bitmap rotatePicOrientation(Bitmap bitmap, float rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        //当进行的不只是平移变换时，filter参数为true可以进行滤波处理，有助于改善新图像质量;flase时，计算机不做过滤处理
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }

    /**
     * 对旋转的图片进行矫正
     *
     * @param filePath 图片的路径
     * @return 矫正后的图片对象
     */
    private static Bitmap correctPicOrientation(String filePath) {
        Matrix matrix = new Matrix();
        int angle = readPictureDegree(filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (angle != 0) {
            matrix.postRotate(angle);
            //当进行的不只是平移变换时，filter参数为true可以进行滤波处理，有助于改善新图像质量;flase时，计算机不做过滤处理
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     * 得到真实的图片路径,因为在android4.4及以上的系统,读取相册返回activity之后得到的是一个id而不是真实的图片路径信息
     * 因此需要contentResolver读通过id来得到图片的真实路径
     *
     * @param context 上下文信息,用来获取contentResolver
     * @param uri     读取相册返回的uri对象
     * @return 真实的图片路径
     */
    public static String getRealPathFormUri(Context context, Uri uri) {
        String result;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    /**
     * 根据图片的ID得到缩略图
     *
     * @param cr
     * @param imageId
     * @return
     */
    public static Bitmap getThumbnailsFromImageId(ContentResolver cr, String imageId) {
        if (imageId == null || imageId.equals("")) {
            return null;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        long imageIdLong = Long.parseLong(imageId);
        //via imageid get the bimap type thumbnail in thumbnail table.
        bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, imageIdLong, MediaStore.Images.Thumbnails.MINI_KIND, options);

        return bitmap;
    }

    /**
     * 根据图片的路径得到该图片在表中的ID
     *
     * @param cr
     * @param filePath
     * @return
     */
    public static String getImageIdFromPath(ContentResolver cr, String filePath) {

        //select condition.
        String whereClause = MediaStore.Images.Media.DATA + " = '" + filePath + "'";

        //colection of results.
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID}, whereClause, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
        cursor.moveToFirst();
        //image id in image table.
        String imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        cursor.close();
        if (imageId == null) {
            return null;
        }
        return imageId;
    }

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


}
