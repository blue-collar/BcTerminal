package com.telewave.battlecommand.fragment;

import android.Manifest;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.telewave.battlecommand.adapter.GridImageAdapter;
import com.telewave.battlecommand.adapter.NewFireDocumentAdapter;
import com.telewave.battlecommand.bean.NewFireDocument;
import com.telewave.battlecommand.bean.ZhddHcws;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.presenter.FireDocumentInfoPresenter;
import com.telewave.battlecommand.utils.FullyGridLayoutManager;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.GeneralUtil;
import com.telewave.lib.base.util.PermissionHelper;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.SharePreferenceUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * <p>类说明: 实现火场文书的获取、新增火场文书上传</p>
 *
 * @author 张军
 * @version 1.0
 * @since 2019-07-26
 */

public class FireDocumentFragment extends BaseFragment implements View.OnClickListener, IDirectionContract.FireDocumentView {

    private View mView;

    private Context context;
    private String tabTitle;
    private String disasterId;
    private String uuid;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private RelativeLayout mLayoutFireDocument;
    //    private LinearLayout  mLayoutFireDocumentEdit;
    private ScrollView mViewFireDocumentEdit;
    private EditText mEtPerson, mEtTime, mEtContent, mEtDescription;
    private Button mBtnFireDocumentAdd, mBtnFireDocumentUpload, mBtnBack;
    private RecyclerView mFireDocumentReclView;
    private List<NewFireDocument> infoList = new ArrayList<>();
    private NewFireDocumentAdapter adapter;
    private ZhddHcws fireInfo;

    private int maxSelectNum = 8;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> selectPictureList = new ArrayList<>();
    private RecyclerView mPictureRecyclerView;
    private GridImageAdapter pictureAdapter;
    private PopupWindow pop;
    private PermissionHelper permissionHelper;
    private FireDocumentInfoPresenter mInfoPresenter;
    private boolean isShowProgress = true;

    private final static int GET_FIRE_DOCUMENT_SUCCESS = 1015;
    private final static int GET_FIRE_DOCUMENT_FAIL = 1017;
    private final static int UPLOAD_FIRE_DOCUMENT_SUCCESS = 1050;
    private final static int UPLOAD_FIRE_DOCUMENT_FAIL = 1055;

    private static final int CHOOSE_PICTURE_REQUEST = 0x1060;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_FIRE_DOCUMENT_SUCCESS:
//                    sortData(infoList);
                    List<NewFireDocument> tempInfo = (List<NewFireDocument>) msg.obj;
                    if (tempInfo != null) {
                        infoList = tempInfo;
//                        setData(tempInfo.get(0));
                        infoList.addAll(tempInfo);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case GET_FIRE_DOCUMENT_FAIL:
                    ToastUtils.toastShortMessage("获取火场文书失败");

                    break;
                case UPLOAD_FIRE_DOCUMENT_SUCCESS:
                    ToastUtils.toastShortMessage("上传火场文书成功");
                    mViewFireDocumentEdit.setVisibility(View.GONE);
                    mLayoutFireDocument.setVisibility(View.VISIBLE);
                    addData(fireInfo);
                    adapter.notifyDataSetChanged();

                    break;
                case UPLOAD_FIRE_DOCUMENT_FAIL:
                    ToastUtils.toastShortMessage("上传火场文书失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    public FireDocumentFragment() {

    }

    @SuppressLint("ValidFragment")
    public FireDocumentFragment(Context context, String tabTitle, String id) {
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

        mView = inflater.inflate(R.layout.fragment_fire_document_detail, container, false);
        permissionHelper = new PermissionHelper(this);

        initView();

        initCameraPermission();
        return mView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

        if (isVisible) {


        } else {

        }
    }

    @Override
    protected void onFragmentFirstVisible() {

        mInfoPresenter = new FireDocumentInfoPresenter(this);
        initData();
    }

    private void initView() {
        mLayoutFireDocument = mView.findViewById(R.id.rl_fire_document_view);
//        mLayoutFireDocumentEdit = mView.findViewById(R.id.ll_fire_document_edit);
        mViewFireDocumentEdit = mView.findViewById(R.id.scroll_view_fire_document_edit);

        mFireDocumentReclView = mView.findViewById(R.id.rvTrace);
        mEtPerson = mView.findViewById(R.id.ed_response_person);
        mEtTime = mView.findViewById(R.id.ed_response_time);
        mEtContent = mView.findViewById(R.id.ed_response_content);
        mEtDescription = mView.findViewById(R.id.ed_response_description);
        mBtnFireDocumentAdd = mView.findViewById(R.id.btn_fire_document_add);

        mPictureRecyclerView = (RecyclerView) mView.findViewById(R.id.pic_recycler);
        mFireDocumentReclView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewFireDocumentAdapter(getActivity(), infoList);
        mFireDocumentReclView.setAdapter(adapter);

        mBtnFireDocumentUpload = mView.findViewById(R.id.btn_fire_document_upload);
        mBtnBack = mView.findViewById(R.id.btn_fire_document_back);
        mBtnFireDocumentAdd.setOnClickListener(this);
        mBtnFireDocumentUpload.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);

        initPictureWidget();
    }

    private void initData() {
        if (disasterId != null && !disasterId.equals("")) {
//            onGetFireDocumentInfo(disasterId);
            mInfoPresenter.getDisasterFireDocumentInfo(disasterId, isShowProgress);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        initData();
    }

    private void onGetFireDocumentInfo(String id) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFireDocumentDetail, RequestMethod.GET);

        request.add("alarmId", id);
        String url = request.url();
        Log.e("NoHttpDebugTag", "url: " + url);
        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e("NoHttpDebugTag", "result: " + result);
                List<NewFireDocument> list = null;
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();

                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<NewFireDocument>>() {
                            }.getType());

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_FIRE_DOCUMENT_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_FIRE_DOCUMENT_FAIL;
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
                msg.what = GET_FIRE_DOCUMENT_FAIL;
                mHandler.sendMessage(msg);

            }
        }, true, true);
    }

    private void initCameraPermission() {
        permissionHelper.requestPermissions("需要相机权限", new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permissions) {

            }

            @Override
            public void doAfterDenied(String... permissions) {

            }
        }, Manifest.permission.CAMERA);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            //第一种方式，弹出选择和拍照的dialog
            showPicturePop();
        }
    };

    private void initPictureWidget() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false);
        mPictureRecyclerView.setLayoutManager(manager);
        pictureAdapter = new GridImageAdapter(getActivity(), onAddPicClickListener);
        pictureAdapter.setList(selectPictureList);
        pictureAdapter.setSelectMax(maxSelectNum);
        mPictureRecyclerView.setAdapter(pictureAdapter);
        pictureAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectPictureList.size() > 0) {
                    LocalMedia media = selectPictureList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(FireDocumentFragment.this).externalPicturePreview(position, selectPictureList);
                            break;
                        default:
                            break;
                    }
                }
            }
        });
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
                    PictureSelector.create(FireDocumentFragment.this)
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
                    PictureSelector.create(FireDocumentFragment.this)
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

    private void addData(ZhddHcws info) {
        NewFireDocument documentInfo = new NewFireDocument();
        documentInfo.setFkr(info.getFkr());
        documentInfo.setFksj(info.getFksj());
        documentInfo.setFknr(info.getFknr());
        documentInfo.setRecordtype(info.getRecordtype());
        infoList.add(0, documentInfo);
    }

    private ZhddHcws onGetInputContent() {
        ZhddHcws info = new ZhddHcws();
        if (mEtPerson.getText().toString().trim() == null || mEtPerson.getText().toString().trim().equals("")) {
            ToastUtils.toastShortMessage("反馈人不能为空");
            return null;
        }
        if (mEtTime.getText().toString().trim() == null || mEtTime.getText().toString().trim().equals("")) {
            ToastUtils.toastShortMessage("反馈时间不能为空");
            return null;
        }
        if (mEtContent.getText().toString().trim() == null || mEtContent.getText().toString().trim().equals("")) {
            ToastUtils.toastShortMessage("反馈内容不能为空");
            return null;
        }
        if (mEtDescription.getText().toString().trim() == null || mEtDescription.getText().toString().trim().equals("")) {
            ToastUtils.toastShortMessage("阶段描述不能为空");
            return null;
        }
        info.setFkr(mEtPerson.getText().toString().trim());
//        info.setFkr(ConstData.userid);
        info.setFksj(mEtTime.getText().toString().trim());
        info.setFknr(mEtContent.getText().toString().trim());

        info.setZquuid(disasterId);
        if (ConstData.ORGANID != null) {
            info.setOfficeId(ConstData.ORGANID);
        } else {
            uuid = GeneralUtil.getUUID();
            info.setOfficeId(uuid);
        }

        info.setRecordtype(mEtDescription.getText().toString().trim());
        return info;
    }

    private void onUploadFireDocumentInfo() {

        fireInfo = onGetInputContent();

        if (fireInfo == null) {
//            ToastUtil.showMessage(getActivity(), "请重新填写");
            return;
        }
//        selectList.addAll(selectPictureList);

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.upLoadFireDocumentInfo, RequestMethod.POST);
//        Request<String> request = NoHttp.createStringRequest("http://192.168.9.91:8083/twmfs/TelewaveMFS/alarm/saveHcws", RequestMethod.POST);

        if (selectPictureList != null && !selectPictureList.isEmpty()) {
            request.add("isFile", "true");
        } else {
            request.add("isFile", "false");
        }

        if (selectPictureList != null && !selectPictureList.isEmpty()) {
            for (int i = 0; i < selectPictureList.size(); i++) {
                FileBinary fileBinary = new FileBinary(new File(selectPictureList.get(i).getPath()));
                request.add("file", fileBinary);
            }
        }

        request.add("zqid", disasterId);
        request.add("fkr", fireInfo.getFkr());
        request.add("officeId", fireInfo.getOfficeId());
        request.add("fknr", fireInfo.getFknr());
        request.add("fksj", fireInfo.getFksj());
        request.add("recordtype", fireInfo.getRecordtype());

        String url = request.url();

        NoHttpManager.getRequestInstance().add(getActivity(), 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            String str = responseBean.getData();
                            if (str.equals("true")) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPLOAD_FIRE_DOCUMENT_SUCCESS;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = UPLOAD_FIRE_DOCUMENT_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Message msg = mHandler.obtainMessage();
                        msg.what = UPLOAD_FIRE_DOCUMENT_FAIL;
                        mHandler.sendMessage(msg);
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                msg.what = UPLOAD_FIRE_DOCUMENT_FAIL;
                mHandler.sendMessage(msg);

            }
        }, true, true);

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
                    selectPictureList.clear();
                    selectPictureList.addAll(images);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    pictureAdapter.setList(images);
                    pictureAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 权限处理
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onClick(View view) {
        final int vId = view.getId();
        if (R.id.btn_fire_document_add == vId) {
            mLayoutFireDocument.setVisibility(View.GONE);
//                mLayoutFireDocumentEdit.setVisibility(View.VISIBLE);
            mViewFireDocumentEdit.setVisibility(View.VISIBLE);
            Date date = new Date();
            mEtTime.setText(sdf.format(date));
            String userName = SharePreferenceUtils.getDataSharedPreferences(getActivity(), "username");
            mEtPerson.setText(userName);
        } else if (R.id.btn_fire_document_upload == vId) {
            onUploadFireDocumentInfo();
        } else if (R.id.btn_fire_document_back == vId) {
            mViewFireDocumentEdit.setVisibility(View.GONE);
            mLayoutFireDocument.setVisibility(View.VISIBLE);
        } else {
        }
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
        if (infoList != null) {
            infoList.clear();
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onFireDocumentCompleted(List<NewFireDocument> info) {

        if (info != null) {
            infoList.addAll(info);
            adapter.notifyDataSetChanged();
        }
    }
}
