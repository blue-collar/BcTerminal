package com.telewave.lib.base.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.view.View;

import com.telewave.lib.base.util.PictureUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wc on 2016/11/2.
 */

public class BitmapUtils {

    /**
     * 缩略图 路径 to 缩略图
     *
     * @param filePath
     * @return
     */
    public static Bitmap getThumbBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 120, 200);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }

        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        return bm;
    }

    public static Bitmap getSentVideoThumbBitmap(Bitmap bm) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) 270) / width;
        float scaleHeight = ((float) 200) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return bm;
    }

    public static Bitmap getSentVideoThumbBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 200, 170);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if (bm == null) {
            return null;
        }

        int degree = readPictureDegree(filePath);
        bm = rotateBitmap(bm, degree);
        return bm;
    }

    /**
     * 通过路径获取bitmap
     *
     * @param picPath
     * @return
     */
    public static Bitmap getBitmapFromPath(String picPath) {
        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inTempStorage = new byte[1024 * 1024];
        File file = new File(picPath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
        if (fs != null) {
            try {
                bitmap = PictureUtils.getSmallBitmap(picPath, 1, false);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                while (bitmap == null) {
                    try {
                        System.gc();
                        System.runFinalization();
                        bfOptions.inSampleSize = 4;
                        bitmap = PictureUtils.getSmallBitmap(picPath, 4, false);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fs != null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        return inSampleSize;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
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
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 获取视频的缩略图
     *
     * @param videoPath 视频的路径
     */
    public static Bitmap createVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            // retriever.setMode(MediaMetadataRetriever.);
            retriever.setDataSource(videoPath);
            bitmap = retriever.getFrameAtTime(1000);

        } catch (Exception ex) {

        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return bitmap;
    }

    /**
     * 保存bitmap对象为图片文件 jpg
     *
     * @param picName
     * @param bm
     * @return
     */
    public static File saveBitmapToFile(String path, String picName, Bitmap bm) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File f = new File(path, picName);
        try {
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            } else {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 保存bitmap对象为图片文件 jpg
     *
     * @param picName
     * @param bm
     * @return
     */
    public static File saveBitmapToPngFile(String path, String picName, Bitmap bm) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File f = new File(path, picName);
        try {
            if (f.exists()) {
                f.delete();
                f.createNewFile();
            } else {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException | NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 将View转换成Bitmap
     *
     * @param addViewContent
     * @return
     */
    public static Bitmap getViewBitmap(View addViewContent) {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }

}
