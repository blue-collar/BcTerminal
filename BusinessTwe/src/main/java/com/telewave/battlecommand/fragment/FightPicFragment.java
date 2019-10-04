package com.telewave.battlecommand.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.telewave.battlecommand.activity.ImagePagerActivity;
import com.telewave.battlecommand.adapter.PhotoWallAdapter;
import com.telewave.battlecommand.bean.FightPicInfo;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.GeneralUtil;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_INDEX;
import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_URLS;

/**
 * <p>类说明: 实现作战图的获取、分页查看和上传</p>
 *
 * @author 张军
 * @version 1.0
 * @since 2019-07-26
 */

public class FightPicFragment extends BaseFragment implements IDirectionContract.FightPicView {

    private static final String TAG = "FightPicFragment";
    private View mView;

    private Button btnUpload;
    private Context context;
    private String tabTitle;
    private String disasterId;
    private String picId;

    private int maxSelectNum = 8;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectPictureList = new ArrayList<>();
    private List<FightPicInfo> picInfos = new ArrayList<>();
    private List<FightPicInfo> selectPics = new ArrayList<>();
    //    private List<FightPicInfo> unpicInfos = new ArrayList<>();
    private FightPicInfo tempInfo;
    private List<String> mPathDatas = new ArrayList<>();
    private List<String> unUploadedPics = new ArrayList<>();
    private RecyclerView mPictureRecyclerView;
    //    private GridImageAdapter pictureAdapter;
    private PhotoWallAdapter mPhotoWallAdapter;
    private PopupWindow pop;
    private PermissionHelper permissionHelper;

    private static final int CHOOSE_PICTURE_REQUEST = 0x1002;

    private static final int UPLOAD_PICS_SUCCESS = 0X1003;
    private static final int UPLOAD_PICS_FAIL = 0X1005;
    private static final int GET_PICS_SUCCESS = 0X1007;
    private static final int GET_PICS_FAIL = 0X1009;
    private static final int DELETE_PIC_SUCCESS = 0X1011;
    private static final int DELETE_PIC_FAIL = 0X1013;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case UPLOAD_PICS_SUCCESS:
                    List<FightPicInfo> uploadedPics = (List<FightPicInfo>) msg.obj;
                    for (FightPicInfo info : uploadedPics) {
                        info.setUploaded(true);
                    }
                    ToastUtils.toastShortMessage("上传成功");
                    mPhotoWallAdapter.notifyDataSetChanged();
//                    unUploadedPics.clear();

                    break;
                case UPLOAD_PICS_FAIL:

                    ToastUtils.toastShortMessage("上传失败");
                    break;
                case GET_PICS_SUCCESS:
//                    ToastUtil.showMessage(getActivity(), "获取作战图成功");
                    List<FightPicInfo> list = (List<FightPicInfo>) msg.obj;
                    if (list != null) {
                        selectPictureList.clear();
                        picInfos.addAll(list);
                    }

//                    List<String> urls = combineUrlAndDownloadPics(picInfos);
                    combineUrl();
                    //标识为已上传
                    for (int i = 0; i < picInfos.size(); i++) {
                        FightPicInfo tempInfo = picInfos.get(i);
                        String url = tempInfo.getUrl();
                        onGetPicsFromUrl(url, tempInfo);
                        picInfos.get(i).setUploaded(true);
                    }
                    mPhotoWallAdapter.notifyDataSetChanged();

                    break;
                case GET_PICS_FAIL:
                    ToastUtils.toastShortMessage("获取作战图失败");

                    break;
                case DELETE_PIC_SUCCESS:
                    ToastUtils.toastShortMessage("删除作战图成功");
                    picInfos.remove(tempInfo);
                    mPhotoWallAdapter.notifyDataSetChanged();

                    break;
                case DELETE_PIC_FAIL:
                    ToastUtils.toastShortMessage("删除作战图失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    public FightPicFragment() {

    }

    @SuppressLint("ValidFragment")
    public FightPicFragment(Context context, String tabTitle, String id) {
        this.context = context;
        this.tabTitle = tabTitle;
        this.disasterId = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_fight_pic_detail, container, false);
        permissionHelper = new PermissionHelper(this);
        initView();
//        initCameraPermission();
        return mView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
//            if (!ProgressDialogUtils.isDialogShowing()) {
//                ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
//            }
        } else {
//            if (ProgressDialogUtils.isDialogShowing()) {
//                ProgressDialogUtils.dismissDialog();
//            }
        }
    }

    @Override
    protected void onFragmentFirstVisible() {

        initData();
    }

    private void initView() {
        mPictureRecyclerView = (RecyclerView) mView.findViewById(R.id.pic_recycler);
        btnUpload = mView.findViewById(R.id.btn_fight_pic_save);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                selectList.addAll(selectPictureList);
                uploadPic(picInfos);
            }
        });
        initPictureWidget();
    }

    private void initData() {
        if (disasterId != null && !disasterId.equals("")) {
            onGetFightPics(disasterId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void onGetFightPics(String id) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFightPicList, RequestMethod.GET);
//        Request<String> request = NoHttp.createStringRequest("http://192.168.9.91:8083/twmfs/TelewaveMFS/alarm/findFightGraph", RequestMethod.GET);

        request.add("zqid", id);
        String url = request.url();

        Log.e("NoHttpDebugTag", "获取作战图详情----------" + url);
        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> stringResponse) {
                String result = stringResponse.get();
                List<FightPicInfo> list = null;
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<FightPicInfo>>() {
                            }.getType());

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_PICS_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_PICS_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                msg.what = GET_PICS_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    /**
     * 删除图片
     *
     * @param picId 图片id
     */
    private void onDeleteFightPic(String picId) {

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.deleteFightPic, RequestMethod.GET);
//        Request<String> request = NoHttp.createStringRequest("http://192.168.9.91:8083/twmfs/TelewaveMFS/alarm/deleteFightGraph", RequestMethod.GET);
        request.add("fileId", picId);

        String url = request.url();

        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> stringResponse) {
                String result = stringResponse.get();
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            String str = responseBean.getData();
                            if (str.equals("true")) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = DELETE_PIC_SUCCESS;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = DELETE_PIC_FAIL;
                                mHandler.sendMessage(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                msg.what = DELETE_PIC_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);

    }

    private PhotoWallAdapter.OnItemClickListener onItemClickListener = new PhotoWallAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View v, int position) {
            if (picInfos.size() < maxSelectNum || picInfos == null) {
                showPicturePop();
            } else {
                Toast.makeText(getActivity(), "数量已够", Toast.LENGTH_LONG).show();
//                ToastUtil.showMessage(MainActivity.this, "数量已够");
            }
        }

        @Override
        public void onItemLongClick(View v, int position) {
            tempInfo = picInfos.get(position);
            //已上传则可删除，未上传则不调接口删除，本地删除即可
            if (tempInfo.isUploaded()) {
                onDeleteFightPic(tempInfo.getId());
            } else {
                picInfos.remove(tempInfo);
                mPhotoWallAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onPreViewItemClick(View v, int position) {
            mPathDatas.clear();
            for (FightPicInfo info : picInfos) {
                String path = info.getFilePhonePath();
                if (!TextUtils.isEmpty(path)) {
                    mPathDatas.add(path);
                }
            }

            imageBrower(position, mPathDatas);
        }
    };

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls
     */
    protected void imageBrower(int position, List<String> urls) {
        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, (Serializable) urls);
        intent.putExtra(EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    private void initPictureWidget() {
//        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
//        mPictureRecyclerView.setLayoutManager(manager);
        mPhotoWallAdapter = new PhotoWallAdapter(getActivity(), picInfos, maxSelectNum);
        //设定十六宫格
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);

        mPictureRecyclerView.setLayoutManager(gridLayoutManager);
        mPictureRecyclerView.setAdapter(mPhotoWallAdapter);
        mPhotoWallAdapter.setOnItemClickListener(onItemClickListener);

    }

    private void showPicturePop() {
        View bottomView = View.inflate(getActivity(), R.layout.layout_bottom_dialog_one, null);
        TextView mAlbum = (TextView) bottomView.findViewById(R.id.tv_album);
        TextView mCamera = (TextView) bottomView.findViewById(R.id.tv_camera);
//        TextView mVideo = (TextView) bottomView.findViewById(R.id.tv_video);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int vId = view.getId();
                if (R.id.tv_album == vId) {
                    //相册
                    PictureSelector.create(FightPicFragment.this)
                            .openGallery(PictureMimeType.ofAll())
                            .maxSelectNum(maxSelectNum)
                            .minSelectNum(1)
                            .imageSpanCount(4)
                            // 是否可预览图片
                            .previewImage(true)
                            // 是否可预览视频
                            .previewVideo(true)
                            // 显示多少秒以内的视频or音频也可适用 int
                            .videoMaxSecond(15)
                            // 传入已选图片 List<LocalMedia> list
                            .selectionMedia(selectPictureList)
                            .selectionMode(PictureConfig.MULTIPLE)
                            .forResult(CHOOSE_PICTURE_REQUEST);
                } else if (R.id.tv_camera == vId) {
                    //拍照
                    PictureSelector.create(FightPicFragment.this)
                            .openCamera(PictureMimeType.ofAll())
                            .imageFormat(PictureMimeType.PNG)
                            .recordVideoSecond(10)//视频秒数录制 默认60s int
                            .forResult(CHOOSE_PICTURE_REQUEST);
                } else if (R.id.tv_cancel == vId) {
                    //取消
                    //closePopupWindow();
                } else {
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void uploadPic(final List<FightPicInfo> selectPicList) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.upLoadFightPicList, RequestMethod.POST);
//        Request<String> request = NoHttp.createStringRequest("http://192.168.9.91:8083/twmfs/TelewaveMFS/alarm/uploadFightGraph", RequestMethod.POST);
        String ids = "";
        if (selectPicList != null && !selectPicList.isEmpty()) {
            for (int i = 0; i < selectPicList.size(); i++) {
                FightPicInfo tempInfo = selectPicList.get(i);
                ids += tempInfo.getId() + ",";
                FileBinary fileBinary = new FileBinary(new File(tempInfo.getFilePhonePath()));
                request.add("file", fileBinary);
            }
        }
        request.add("fileIds", ids);
        request.add("zqid", disasterId);
//        String url = request.url();

        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> stringResponse) {
                String result = stringResponse.get();
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            String str = responseBean.getData();
                            if (str.equals("true")) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPLOAD_PICS_SUCCESS;
                                msg.obj = selectPicList;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPLOAD_PICS_FAIL;
                                mHandler.sendMessage(msg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                msg.what = UPLOAD_PICS_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);

                    for (int i = 0; i < images.size(); i++) {
                        String path = images.get(i).getPath();
//                        mPathDatas.add(images.get(i).getPath());
                        FightPicInfo tempInfo = new FightPicInfo();
                        picId = GeneralUtil.getUUID();
                        tempInfo.setFilePhonePath(path);
                        tempInfo.setId(picId);
//                        unUploadedPics.add(path);
//                        unpicInfos.add(tempInfo);
                        picInfos.add(tempInfo);
                        selectPics.add(tempInfo);
                    }
//                    mPhotoWallAdapter.setList(selectPics);

                    mPhotoWallAdapter.notifyDataSetChanged();

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 组合URL
     */
    private void combineUrl() {

        if (picInfos != null) {
            for (int i = 0; i < picInfos.size(); i++) {
                FightPicInfo tempInfo = picInfos.get(i);

                String url = "http://" + ConstData.urlManager.serverIp + ":" + ConstData.urlManager.serverPort
                        + "/files/" + tempInfo.getSavepath();
                picInfos.get(i).setUrl(url);
            }
        }

    }

    private void onGetPicsFromUrl(String url, final FightPicInfo tempInfo) {

        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, ConstData.CHAT_PIC_DIR, "", true, false);

        //NoHttpManager.getDownloadInstance().add(11, downloadRequest, new SimpleDownloadListener());
        NoHttpManager.getDownloadInstance().add(0, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                Log.e(TAG, "onDownloadError: " + exception);
                ToastUtils.toastShortMessage("下载图片失败");
            }

            @Override
            public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {
                Log.e(TAG, "onStart: " + "isResume :" + isResume + "rangeSize :" + rangeSize + "allCount: " + allCount);

            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                Log.e(TAG, "onProgress: " + progress + "fileCount :" + fileCount + "speed :" + speed);

            }

            @Override
            public void onFinish(int what, String filePath) {
                Log.e(TAG, "onFinish: ");
//                ToastUtils.toastShortMessage("下载图片完成！");
                tempInfo.setFilePhonePath(filePath);
                mPathDatas.add(filePath);
                mPhotoWallAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancel(int what) {
                Log.e(TAG, "onCancel: ");

            }
        });
    }

    /**
     * 权限处理
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
        }
    }

    /**
     * 隐藏掉等待对话框
     */
    @Override
    public void dismissWaitDialog() {
        if (ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.dismissDialog();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissWaitDialog();
        ToastUtils.toastShortMessage(msg);
    }

    @Override
    public void onFightPicCompleted(List<FightPicInfo> info) {

        if (info != null) {
            selectPictureList.clear();
            picInfos.addAll(info);
        }
        combineUrl();
//        List<String> urls = combineUrlAndDownloadPics(info);
//        for (String url : urls) {
//            onGetPicsFromUrl(url);
//        }

//        pictureAdapter.setList(selectPictureList);
        mPhotoWallAdapter.notifyDataSetChanged();
    }
}
