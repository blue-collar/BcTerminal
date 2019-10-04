package com.telewave.lib.base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: android 6.0 权限申请工具
 * Created by Yuan on 2016/10/31,12:23.
 */

public class PermissionHelper {

    private static final String TAG = "PermissionHelper";
    private static final int REQUEST_PERMISSION_CODE = 1000;

    private Context mContext;
    private Fragment mV4Fragment;
    private android.app.Fragment mFragment;

    private PermissionListener mListener;

    //已授权权限集合
    private List<String> mDeniedpermissionlist = new ArrayList<>();
    //被拒绝的权限集合
    private List<String> mGrantPermissonList = new ArrayList<>();

    public PermissionHelper(@NonNull Context context) {
        checkCallingObjectSuitability(context);
        this.mContext = context;
    }

    public PermissionHelper(@NonNull Fragment fragment) {
        checkCallingObjectSuitability(fragment);
        this.mV4Fragment = fragment;
        this.mContext = fragment.getActivity();
    }

    public PermissionHelper(@NonNull android.app.Fragment fragment) {
        checkCallingObjectSuitability(fragment);
        this.mFragment = fragment;
        this.mContext = fragment.getActivity();
    }

    /**
     * 权限授权申请
     *
     * @param hintMessage 要申请的权限的提示
     * @param permissions 要申请的权限
     * @param listener    申请成功之后的callback
     */
    public void requestPermissions(@NonNull CharSequence hintMessage, @Nullable PermissionListener listener, @NonNull final String... permissions) {

        if (listener != null) {
            mListener = listener;
        }
        //没全部权限
        if (!hasPermissions(mContext, permissions)) {
            //没有获取到权限集合
            final String[] deinedPermissions = mDeniedpermissionlist.toArray(new String[mDeniedpermissionlist.size()]);

            //需要向用户解释为什么申请这个权限
            boolean shouldShowRationale = false;
            for (String perm : permissions) {
                shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(mContext, perm);
                Log.e(TAG, "requestPermissions: " + shouldShowRationale);
            }

            if (shouldShowRationale) {
                showMessageOKCancel(hintMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (mV4Fragment != null) {
                            executePermissionsRequest(mV4Fragment, deinedPermissions, REQUEST_PERMISSION_CODE);
                        } else if (mFragment != null) {
                            executePermissionsRequest(mFragment, deinedPermissions, REQUEST_PERMISSION_CODE);
                        } else {
                            executePermissionsRequest(mContext, deinedPermissions, REQUEST_PERMISSION_CODE);
                        }

                    }
                });
            } else {
                if (mV4Fragment != null) {
                    executePermissionsRequest(mV4Fragment, deinedPermissions, REQUEST_PERMISSION_CODE);
                } else if (mFragment != null) {
                    executePermissionsRequest(mFragment, deinedPermissions, REQUEST_PERMISSION_CODE);
                } else {
                    executePermissionsRequest(mContext, deinedPermissions, REQUEST_PERMISSION_CODE);
                }
            }
        } else if (mListener != null) { //有全部权限
            mListener.doAfterGrand(permissions);
        }
    }

    /**
     * 处理onRequestPermissionsResult
     *
     * @param requestCode  授权返回码
     * @param permissions  权限集合
     * @param grantResults 授权结果集合
     */
    public void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mGrantPermissonList.add(permissions[i]);
                        mDeniedpermissionlist.remove(permissions[i]);
                    }
                }

                if (mListener != null) {
                    if (mGrantPermissonList.size() > 0) {
                        mListener.doAfterGrand((String[]) mGrantPermissonList.toArray(new String[mGrantPermissonList.size()]));
                        mGrantPermissonList.clear();
                    }
                    if (mDeniedpermissionlist.size() > 0) {
                        mListener.doAfterDenied((String[]) mDeniedpermissionlist.toArray(new String[mDeniedpermissionlist.size()]));
                        mDeniedpermissionlist.clear();
                    }
                }
                break;
        }
    }

    /**
     * 判断是否具有某权限
     *
     * @param context
     * @param perms
     * @return
     */
    public boolean hasPermissions(@NonNull Context context, @NonNull String... perms) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                mDeniedpermissionlist.add(perm);
            } else {
                mGrantPermissonList.add(perm);
            }
        }

        return mDeniedpermissionlist.size() == 0;
    }

    /**
     * 兼容fragment
     *
     * @param object
     * @param perm
     * @return
     */
    @TargetApi(23)
    private boolean shouldShowRequestPermissionRationale(@NonNull Object object, @NonNull String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return object instanceof android.app.Fragment && ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        }
    }

    /**
     * 执行申请,兼容fragment
     *
     * @param object      activity或者fragment对象
     * @param perms       需要申请的权限
     * @param requestCode 请求码
     */
    @TargetApi(23)
    private void executePermissionsRequest(@NonNull Object object, @NonNull String[] perms, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    /**
     * 检查传递Context是否合法
     *
     * @param object
     */
    private void checkCallingObjectSuitability(@Nullable Object object) {
        if (object == null) {
            throw new NullPointerException("Activity或者Fragment不能为null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        if (!(isSupportFragment || isActivity || (isAppFragment && isNeedRequest()))) {
            if (isAppFragment) {
                throw new IllegalArgumentException("目标sdk必须是23以上并且调用者是android.app.Fragment");
            } else {
                throw new IllegalArgumentException("调用者必须是Activity或者Fragment");
            }
        }
    }

    /**
     * 提供给外界的方法用来显示设置对话框,手动打开权限
     *
     * @param message 对画框的消息内容
     */
    public void openPermissionsSetting(String message) {
        showMessageOKCancel(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    /**
     * 是否需要动态申请权限 只有android6.0以上的系统需要动态申请
     *
     * @return 是否是6.0以上的系统
     */
    private boolean isNeedRequest() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private void showMessageOKCancel(CharSequence message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(mContext).setMessage(message).setPositiveButton("确定", okListener).setNegativeButton("取消", null).show();
    }

    /**
     * 权限回调接口
     */
    public interface PermissionListener {

        /**
         * 权限申请成功回调接口
         *
         * @param permissions 申请成功的权限
         */
        void doAfterGrand(String... permissions);

        /**
         * 权限申请失败回调接口
         *
         * @param permissions 申请失败的权限
         */
        void doAfterDenied(String... permissions);
    }
}