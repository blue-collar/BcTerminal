package com.tencent.qcloud.tim.uikit.component;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telewave.lib.base.util.ScreenUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.TIMVideo;
import com.tencent.imsdk.TIMVideoElem;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoView;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.component.video.UIKitVideoView;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.ImageUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 预览(图片、视频)
 *
 * @author PF-NAN
 * @date 2019-08-09
 */
public class IMMessagePreviewActivity extends Activity {
    /*会话类型*/
    public static final String KEY_CONVERSTION_TYPE = "KEY_CONVERSTION_TYPE";
    /*会话id*/
    public static final String KEY_CONVERSTION_ID = "KEY_CONVERSTION_ID";
    /*需要加载的消息数量*/
    public static final String KEY_CONVERSTION_UNREAD_NUMBER = "KEY_CONVERSTION_UNREAD_NUMBER";
    /*消息唯一标示*/
    public static final String KEY_MESSAGE_ID = "KEY_MESSAGE_ID";

    private ViewPager vp_messageView;
    /*需要展示的所有消息*/
    private List<MessageInfo> mMessageInfos = new ArrayList<>();
    private MessagePagerAdapter mAadapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_message_preview);
        vp_messageView = findViewById(R.id.vp_messageView);
        mAadapter = new MessagePagerAdapter();
        vp_messageView.setAdapter(mAadapter);
        findViewById(R.id.photo_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();

    }

    private void initData() {
        final int converstionType = getIntent().getIntExtra(KEY_CONVERSTION_TYPE, 0);
        String converstionId = getIntent().getStringExtra(KEY_CONVERSTION_ID);
        int loadNumber = getIntent().getIntExtra(KEY_CONVERSTION_UNREAD_NUMBER, 50);
        final String messageId = getIntent().getStringExtra(KEY_MESSAGE_ID);
        TIMConversation timConversation = TIMManager.getInstance().getConversation(converstionType == 0 ? TIMConversationType.C2C : TIMConversationType.Group, converstionId);

        timConversation.getMessage(loadNumber, null, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                int currentIndex = 0;
                mMessageInfos.clear();
                List<MessageInfo> msgInfos = MessageInfoUtil.TIMMessages2MessageInfos(timMessages, converstionType != 0);

                if (null != msgInfos && !msgInfos.isEmpty()) {
                    for (int i = msgInfos.size() - 1; i >= 0; i--) {
                        MessageInfo messageInfo = msgInfos.get(i);
                        if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_IMAGE || messageInfo.getMsgType() == MessageInfo.MSG_TYPE_VIDEO) {
                            mMessageInfos.add(messageInfo);
                            if (TextUtils.equals(messageId, messageInfo.getId())) {
                                currentIndex = mMessageInfos.size() - 1;
                            }
                        }
                    }
                    mAadapter.notifyDataSetChanged();
                    vp_messageView.setCurrentItem(currentIndex);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            vp_messageView.setVisibility(View.VISIBLE);

                        }
                    }, 500);
                }

            }
        });
    }


    private class MessagePagerAdapter extends PagerAdapter {
        private static final int DEFAULT_RADIUS = 5;
        String mImagePath = "";

        private int videoWidth;
        private int videoHeight;

        @Override
        public int getCount() {
            return mMessageInfos.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View inflate = View.inflate(IMMessagePreviewActivity.this, R.layout.im_item_image_video_view, null);
            final PhotoView photo_view = inflate.findViewById(R.id.photo_view);
            ImageView video_play_btn = inflate.findViewById(R.id.video_play_btn);
            UIKitVideoView video_play_view = inflate.findViewById(R.id.video_play_view);
            video_play_view.setVisibility(View.GONE);
            video_play_view.pause();
            MessageInfo messageInfo = mMessageInfos.get(position);
            initItemViewData(photo_view, video_play_btn, messageInfo);
            initItemViewEvent(photo_view, video_play_view, video_play_btn, messageInfo);
            container.addView(inflate);
            return inflate;
        }

        /**
         * 初始化控件数据
         *
         * @param photo_view
         * @param video_play_btn
         * @param messageInfo
         */
        private void initItemViewData(final PhotoView photo_view, ImageView video_play_btn, final MessageInfo messageInfo) {
            if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_IMAGE) {
                video_play_btn.setVisibility(View.GONE);
                TIMImageElem timImageElem = (TIMImageElem) messageInfo.getTIMMessage().getElement(0);
                ArrayList<TIMImage> imageList = timImageElem.getImageList();
                for (TIMImage timImage : imageList) {
                    if (timImage.getType() == TIMImageType.Original) {
                        String imagePath = TUIKitConstants.IMAGE_DOWNLOAD_DIR + timImage.getUuid();
                        final File file = new File(imagePath);
                        if (file.exists()) {
                            GlideEngine.loadCornerImage(photo_view, file.getPath(), null, 0);
                        } else {
                            timImage.getImage(imagePath, new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    GlideEngine.loadCornerImage(photo_view, messageInfo.getDataPath(), null, 0);
                                }

                                @Override
                                public void onSuccess() {
                                    GlideEngine.loadCornerImage(photo_view, file.getPath(), null, 0);
                                }
                            });
                        }
                    }
                }
            } else if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_VIDEO) {
                video_play_btn.setVisibility(View.VISIBLE);
                GlideEngine.loadCornerImage(photo_view, messageInfo.getDataPath(), null, DEFAULT_RADIUS);
            }
        }

        /**
         * 初始化控件事件
         *
         * @param photo_view
         * @param video_play_view
         * @param video_play_btn
         * @param messageInfo
         */
        private void initItemViewEvent(final PhotoView photo_view, final UIKitVideoView video_play_view, final ImageView video_play_btn, final MessageInfo messageInfo) {
            photo_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_IMAGE) {
                        initImageOnLongEvent(photo_view, messageInfo);
                    } else if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_VIDEO) {
                        initVideoOnLongEvent(video_play_view, messageInfo);
                    }
                    return true;
                }
            });
            video_play_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_VIDEO) {
                        initVideoOnLongEvent(video_play_view, messageInfo);
                    }
                    return true;
                }
            });
            video_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_VIDEO) {
                        TIMVideoElem videoElem = (TIMVideoElem) messageInfo.getTIMMessage().getElement(0);
                        TIMVideo videoInfo = videoElem.getVideoInfo();
                        String videoPath = TUIKitConstants.VIDEO_DOWNLOAD_DIR + videoInfo.getUuid();
                        final File videoFile = new File(videoPath);
                        if (videoFile.exists()) {
                            photo_view.setVisibility(View.GONE);
                            video_play_btn.setVisibility(View.GONE);
                            video_play_view.setVisibility(View.VISIBLE);

                            Bitmap firstFrame = ImageUtil.getBitmapFormPath(messageInfo.getDataPath());
                            if (firstFrame != null) {
                                videoWidth = firstFrame.getWidth();
                                videoHeight = firstFrame.getHeight();
                                updateVideoView(video_play_view);
                            }
                            video_play_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    video_play_btn.setVisibility(View.VISIBLE);
                                }
                            });
                            video_play_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    video_play_view.start();
                                }
                            });
                            video_play_view.setVideoURI(messageInfo.getDataUri());
                        } else {
                            videoInfo.getVideo(videoPath, new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    ToastUtils.toastLongMessage("视频下载失败");
                                    messageInfo.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                                }

                                @Override
                                public void onSuccess() {
                                    messageInfo.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                                    photo_view.setVisibility(View.GONE);
                                    video_play_btn.setVisibility(View.GONE);
                                    video_play_view.setVisibility(View.VISIBLE);
                                    Bitmap firstFrame = ImageUtil.getBitmapFormPath(messageInfo.getDataPath());
                                    if (firstFrame != null) {
                                        videoWidth = firstFrame.getWidth();
                                        videoHeight = firstFrame.getHeight();
                                        updateVideoView(video_play_view);
                                    }
                                    video_play_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                        @Override
                                        public void onCompletion(MediaPlayer mp) {
                                            video_play_btn.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    video_play_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mediaPlayer) {
                                            video_play_view.start();
                                        }
                                    });
                                    video_play_view.setVideoURI(messageInfo.getDataUri());
                                }
                            });
                        }
                    }
                }
            });
            video_play_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (video_play_view.isPlaying()) {
                        video_play_view.pause();
                        video_play_btn.setVisibility(View.VISIBLE);
                    } else {
                        video_play_btn.setVisibility(View.GONE);
                        video_play_view.start();

                    }
                }
            });

        }

        /**
         * 处理视频长按事件
         *
         * @param video_play_view
         * @param messageInfo
         */
        private void initVideoOnLongEvent(final UIKitVideoView video_play_view, final MessageInfo messageInfo) {
            video_play_view.pause();
            TUIKitDialog tuiKitDialog = new TUIKitDialog(IMMessagePreviewActivity.this)
                    .builder()
                    .setCancelable(true)
                    .setCancelOutside(true)
                    .setTitle("确定保存该视频到相册？")
                    .setDialogWidth(0.8f);
            tuiKitDialog.getBtn_neg().setTextColor(Color.parseColor("#D41616"));
            tuiKitDialog.getBtn_pos().setTextColor(Color.parseColor("#D41616"));

            tuiKitDialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TIMVideoElem videoElem = (TIMVideoElem) messageInfo.getTIMMessage().getElement(0);
                    TIMVideo videoInfo = videoElem.getVideoInfo();
                    final String videoPath = TUIKitConstants.VIDEO_DOWNLOAD_DIR + videoInfo.getUuid();
                    final File videoFile = new File(videoPath);
                    if (videoFile.exists()) {
                        saveVideoFile(videoPath);
                    } else {
                        videoInfo.getVideo(videoPath, new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {
                                ToastUtils.toastLongMessage("视频下载失败");
                                messageInfo.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                            }

                            @Override
                            public void onSuccess() {
                                messageInfo.setStatus(MessageInfo.MSG_STATUS_DOWNLOADED);
                                saveVideoFile(videoPath);
                            }
                        });
                    }


                }
            }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }


        /**
         * 处理图片长按事件
         *
         * @param photo_view
         * @param messageInfo
         */
        private void initImageOnLongEvent(PhotoView photo_view, final MessageInfo messageInfo) {
            TUIKitDialog tuiKitDialog = new TUIKitDialog(IMMessagePreviewActivity.this)
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
                    TIMImageElem timImageElem = (TIMImageElem) messageInfo.getTIMMessage().getElement(0);
                    ArrayList<TIMImage> imageList = timImageElem.getImageList();
                    for (TIMImage timImage : imageList) {
                        if (timImage.getType() == TIMImageType.Original) {
                            String imagePath = TUIKitConstants.IMAGE_DOWNLOAD_DIR + timImage.getUuid();
                            final File file = new File(imagePath);
                            if (file.exists()) {
                                mImagePath = file.getPath();
                                saveImageFile();
                            } else {
                                timImage.getImage(imagePath, new TIMCallBack() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        mImagePath = messageInfo.getDataPath();
                                        saveImageFile();
                                    }

                                    @Override
                                    public void onSuccess() {
                                        mImagePath = file.getPath();
                                        saveImageFile();
                                    }
                                });
                            }
                        }
                    }
                }
            }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        }

        /**
         * 保存图片
         */
        private void saveImageFile() {
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

        /**
         * 保存视频文件
         *
         * @param videoPath
         */
        private void saveVideoFile(String videoPath) {
            ImageUtil.saveVideo(videoPath, new ImageUtil.CopyInter() {
                @Override
                public void onStart() {
                    TUIKitLog.e("=============开始=====");
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

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        private void updateVideoView(UIKitVideoView video_play_view) {
            if (videoWidth <= 0 && videoHeight <= 0) {
                return;
            }
            boolean isLandscape = true;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                isLandscape = false;
            }

            int deviceWidth;
            int deviceHeight;
            if (isLandscape) {
                deviceWidth = Math.max(ScreenUtils.getScreenWidth(IMMessagePreviewActivity.this), ScreenUtils.getScreenHeight(IMMessagePreviewActivity.this));
                deviceHeight = Math.min(ScreenUtils.getScreenWidth(IMMessagePreviewActivity.this), ScreenUtils.getScreenHeight(IMMessagePreviewActivity.this));
            } else {
                deviceWidth = Math.min(ScreenUtils.getScreenWidth(IMMessagePreviewActivity.this), ScreenUtils.getScreenHeight(IMMessagePreviewActivity.this));
                deviceHeight = Math.max(ScreenUtils.getScreenWidth(IMMessagePreviewActivity.this), ScreenUtils.getScreenHeight(IMMessagePreviewActivity.this));
            }
            float deviceRate = (float) deviceWidth / (float) deviceHeight;

            float rate = (float) videoWidth / (float) videoHeight;
            int width = 0;
            int height = 0;
            if (rate < deviceRate) {
                height = deviceHeight;
                width = (int) (deviceHeight * rate);
            } else {
                width = deviceWidth;
                height = (int) (deviceWidth / rate);
            }
            ViewGroup.LayoutParams params = video_play_view.getLayoutParams();
            params.width = width;
            params.height = height;
            video_play_view.setLayoutParams(params);
        }
    }


}
