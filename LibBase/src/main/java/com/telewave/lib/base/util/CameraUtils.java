package com.telewave.lib.base.util;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Author: rick_tan
 * @Date: 19-7-23
 * @Version: v1.0
 * @Des 相机工具类
 */
public class CameraUtils {
    private static CameraSizeComparator sizeComparator = new CameraSizeComparator();
    public final static int CAMERA_ORIENTATION_0 = 0;
    public final static int CAMERA_ORIENTATION_90 = 90;
    public final static int CAMERA_ORIENTATION_180 = 180;
    public final static int CAMERA_ORIENTATION_270 = 270;

    /**
     * 设置 摄像头的角度
     */
    public static void setCameraDisplayOrientation(Activity context, Camera camera, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        // 获取摄像头信息
        Camera.getCameraInfo(cameraId, info);
        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
        // 获取摄像头当前的角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) { // 前置摄像头
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else { // 后置摄像头
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public static int getCameraDisplayOrientation(Context mContext, int cameraId) {
        if (mContext == null) {
            return 0;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = ((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = CAMERA_ORIENTATION_0;
                break;
            case Surface.ROTATION_90:
                degrees = CAMERA_ORIENTATION_90;
                break;
            case Surface.ROTATION_180:
                degrees = CAMERA_ORIENTATION_180;
                break;
            case Surface.ROTATION_270:
                degrees = CAMERA_ORIENTATION_270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 开启前置摄像头
     *
     * @return
     */
    public static Camera openFrontCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return Camera.open(i);
            }
        }
        return null;
    }

    /**
     * 获取最接近的尺寸
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * 获得合适的预览尺寸，优先查找指定宽高，当寻找不到指定宽高后，则寻找和指定宽高最接近的一组数据
     *
     * @param list
     * @param propHeight
     * @param propWidth
     * @return
     */
    public static Camera.Size getPropPreviewSize(List<Camera.Size> list, int propHeight, int propWidth) {
        Collections.sort(list, sizeComparator);

        int i = 0;
        for (Camera.Size s : list) {
            if ((s.width == propWidth && s.height == propHeight)) {
                return list.get(i);
            }
            i++;
        }


        return list.get(0);
    }


    private static class CameraSizeComparator implements Comparator<Camera.Size> {
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if (lhs.width == rhs.width) {
                return 0;
            } else if (lhs.width > rhs.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        //Timber.v("isCameraCanuse="+canUse);
        return canUse;
    }
}
