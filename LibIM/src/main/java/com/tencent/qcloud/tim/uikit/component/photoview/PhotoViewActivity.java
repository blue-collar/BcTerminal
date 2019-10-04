package com.tencent.qcloud.tim.uikit.component.photoview;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMImage;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.utils.FileUtil;
import com.tencent.qcloud.tim.uikit.utils.ImageUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.io.File;


public class PhotoViewActivity extends Activity {

    private PhotoView mPhotoView;
    private Matrix mCurrentDisplayMatrix = null;
    public static TIMImage mCurrentOriginalImage;
    private TextView mViewOriginalBtn;
    private String mImagePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_view);
        mImagePath = getIntent().getStringExtra(TUIKitConstants.IMAGE_DATA);
        Uri uri = FileUtil.getUriFromPath(mImagePath);
        boolean isSelf = getIntent().getBooleanExtra(TUIKitConstants.SELF_MESSAGE, false);
        mCurrentDisplayMatrix = new Matrix();
        mPhotoView = findViewById(R.id.photo_view);
        mPhotoView.setDisplayMatrix(mCurrentDisplayMatrix);
        mPhotoView.setOnMatrixChangeListener(new MatrixChangeListener());
        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());
        mPhotoView.setOnSingleFlingListener(new SingleFlingListener());
        mViewOriginalBtn = findViewById(R.id.view_original_btn);
        if (isSelf || mCurrentOriginalImage == null) {
            mPhotoView.setImageURI(uri);
        } else {
            String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + mCurrentOriginalImage.getUuid();
            File file = new File(path);
            if (file.exists()) {
                mImagePath = path;
                mPhotoView.setImageURI(FileUtil.getUriFromPath(file.getPath()));
            } else {
                mPhotoView.setImageURI(uri);
                mViewOriginalBtn.setVisibility(View.VISIBLE);
                mViewOriginalBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentOriginalImage != null) {

                            final String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + mCurrentOriginalImage.getUuid();
                            final File file = new File(path);
                            if (!file.exists()) {
                                mCurrentOriginalImage.getImage(path, new TIMCallBack() {
                                    @Override
                                    public void onError(int code, String desc) {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        mPhotoView.setImageURI(FileUtil.getUriFromPath(file.getPath()));
                                        mViewOriginalBtn.setText("已完成");
                                        mViewOriginalBtn.setOnClickListener(null);
                                    }
                                });
                            } else {
                                mPhotoView.setImageURI(FileUtil.getUriFromPath(file.getPath()));
                            }
                        }
                    }
                });
            }
        }

        findViewById(R.id.photo_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                TUIKitDialog tuiKitDialog = new TUIKitDialog(PhotoViewActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("确定保存该图片到相册？")
                        .setDialogWidth(0.8f);
                tuiKitDialog.getBtn_neg().setTextColor(Color.parseColor("#D41616"));
                tuiKitDialog.getBtn_pos().setTextColor(Color.parseColor("#D41616"));

                tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtil.saveImage(mImagePath, new ImageUtil.CopyInter() {
                            @Override
                            public void onStart() {
                                TUIKitLog.e("=============开始");
                            }

                            @Override
                            public void onSuccess(String filePath) {
                                ToastUtils.toastLongMessage("保存成功");
                            }

                            @Override
                            public void onFailure() {
                                TUIKitLog.e("=============失败");
                            }
                        });
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                return true;
            }
        });

    }

    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;
        }
    }


    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }

    private class SingleFlingListener implements OnSingleFlingListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }
}
