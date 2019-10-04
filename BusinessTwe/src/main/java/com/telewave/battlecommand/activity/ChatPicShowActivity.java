package com.telewave.battlecommand.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.BitmapUtils;
import com.telewave.lib.base.util.FileUtils;
import com.telewave.lib.widget.BaseActivity;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoView;

/**
 * 即时通讯显示图片页面
 */
public class ChatPicShowActivity extends BaseActivity {

    private static final String TAG = "ChatPicShowActivity";
    private PhotoView photoView;
    private Bitmap bitmap;
    private Toolbar mToolBar;
    private TextView mToolbarTitle;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat_image_show);
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolBar = (Toolbar) findViewById(R.id.mToolbar);
        photoView = (PhotoView) findViewById(R.id.photo_view);
        mToolbarTitle.setText("图片预览");
        mToolBar.setNavigationIcon(R.mipmap.back_pic);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handlerPic();
    }

    private void handlerPic() {
        final String PicPath = getIntent().getStringExtra("PicPath");
        if (TextUtils.isEmpty(PicPath)) {
            finish();
            return;
        }
        Log.e(TAG, "ChatPicShowActivity--PicPath==" + PicPath);
        Bitmap bitmap = BitmapUtils.getBitmapFromPath(PicPath);
        if (bitmap != null) {
            photoView.setImageBitmap(bitmap);
        } else {
            Glide.with(this).load(PicPath)
                    .apply(new RequestOptions().placeholder(R.mipmap.load_default).error(R.mipmap.load_default))
                    .into(photoView);
        }
        /**
         * 2019/04/04
         * 增加图片长按 弹出保存到本地相册
         */
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatPicShowActivity.this);
                builder.setItems(new String[]{"保存到本地相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * 拷贝到指定路径
                                 */
                                FileUtils.copyFile(ChatPicShowActivity.this, PicPath);

                            }
                        }).start();
                    }
                });
                builder.show();
                return false;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
