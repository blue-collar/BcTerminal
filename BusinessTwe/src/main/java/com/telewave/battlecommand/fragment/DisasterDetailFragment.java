package com.telewave.battlecommand.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.telewave.battlecommand.activity.EditDetailActivity;
import com.telewave.battlecommand.activity.ImagePagerActivity;
import com.telewave.battlecommand.adapter.DetailPhotoAdapter;
import com.telewave.battlecommand.adapter.PhotoVideoAdapter;
import com.telewave.battlecommand.bean.DisasterDetail;
import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.battlecommand.bean.FightPicInfo;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.imui.activity.IMSingleActivity;
import com.telewave.battlecommand.imui.afinal.IMKeys;
import com.telewave.battlecommand.presenter.DisasterDetailPresenter;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_INDEX;
import static com.telewave.lib.base.ConstData.EXTRA_IMAGE_URLS;

/**
 * <p>类说明: 实现警情详情加载</p>
 *
 * @author 张军
 * @version 1.0
 * @since 2019-07-26
 */

public class DisasterDetailFragment extends BaseFragment implements IDirectionContract.DisasterDetailView, View.OnClickListener {

    private static final String TAG = "DisasterDetailFragment";
    private View mView;

    private TextView mTvAddress, mTvAlarmTime, mTvZDName, mTvUpperName, mTvAlarmPerson, mTvPhoneNumber;

    private TextView mTvPresentTime, mTvAlarmSort, mTvAlarmCharge, mTvAlarmRange, mTvEventNumbering;
    private TextView mTvDescription, mTvFiredMass, mTvAlarmState, mTvFiredArea;
    private TextView mTvInjured, mTvEvacuation, mTvDeathPeople;
    private TextView mTvSmog, mTvImportantAlarm;

    private ImageView mImgChat;
    private TextView mTvEditDetail;
    private Context context;
    private String tabTitle;
    private String disasterId;
    private DisasterDetail disasterDetail = new DisasterDetail();
    private List<FightPicInfo> files = new ArrayList<>();
    private List<FightPicInfo> picInfos = new ArrayList<>();
    private List<FightPicInfo> videoInfos = new ArrayList<>();
    private List<String> mPathDatas = new ArrayList<>();
    private ArrayList<String> mPathVideos = new ArrayList<>();
    private DisasterInfo disasterInfo;
    private DisasterDetailPresenter mDisasterDetailPresenter;
    private boolean isShowProgress = true;

    private RecyclerView mPicReclView, mVideoReclView;
    private PhotoVideoAdapter mPhotoVideoAdapter;
    private DetailPhotoAdapter mPicAdapter;
    private int mMaxNum = 6;

    private static final int EDIT_DISASTER_DETAIL = 0x1080;

    public DisasterDetailFragment() {

    }

    @SuppressLint("ValidFragment")
    public DisasterDetailFragment(Context context, String tabTitle, String id, DisasterInfo info) {
        this.context = context;
        this.tabTitle = tabTitle;
        this.disasterId = id;
        this.disasterInfo = info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_disaster_detail_total, container, false);
        initView();

        return mView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
////            initData();
////            ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
//        }else {
////            ProgressDialogUtils.dismissDialog();
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        mDisasterDetailPresenter = new DisasterDetailPresenter(this);
        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView() {
        mTvAddress = mView.findViewById(R.id.disaster_detail_address);
        mTvAlarmTime = mView.findViewById(R.id.disaster_detail_alarm_time);
        mTvZDName = mView.findViewById(R.id.disaster_detail_zd_name);
        mTvUpperName = mView.findViewById(R.id.disaster_detail_upper_organ_name);
        mTvAlarmPerson = mView.findViewById(R.id.disaster_detail_edit_alarm_person);
        mTvPhoneNumber = mView.findViewById(R.id.disaster_detail__phone_number);
        mTvPresentTime = mView.findViewById(R.id.disaster_detail_present_time);
        mTvAlarmSort = mView.findViewById(R.id.disaster_detail_alarm_sort);

        mTvAlarmCharge = mView.findViewById(R.id.disaster_detail_charge);
        mTvAlarmRange = mView.findViewById(R.id.disaster_detail_range);
        mTvEventNumbering = mView.findViewById(R.id.disaster_detail_event_numbering);
        mTvDescription = mView.findViewById(R.id.disaster_detail_description);
        mTvFiredMass = mView.findViewById(R.id.disaster_detail_bust_mass);
//        mTvTrappedPeople = mView.findViewById(R.id.disaster_detail_trapped_people);
        mTvAlarmState = mView.findViewById(R.id.disaster_detail_alarm_state);
        mTvFiredArea = mView.findViewById(R.id.disaster_detail_fired_area);

        mTvInjured = mView.findViewById(R.id.disaster_detail_injured_people);
        mTvEvacuation = mView.findViewById(R.id.disaster_detail_evacuated_people);
        mTvDeathPeople = mView.findViewById(R.id.disaster_detail_dead_people);
        mTvSmog = mView.findViewById(R.id.disaster_detail_smog);
        mTvImportantAlarm = mView.findViewById(R.id.disaster_detail_important_alarm);

        mImgChat = mView.findViewById(R.id.iv_disaster_detail_chat);
        mTvEditDetail = mView.findViewById(R.id.tv_disaster_detail_edit);
        mImgChat.setOnClickListener(this);
        mTvEditDetail.setOnClickListener(this);

        mPicReclView = mView.findViewById(R.id.disaster_detail_pic);
        mVideoReclView = mView.findViewById(R.id.disaster_detail_video);

        //设定十六宫格
        GridLayoutManager picLayoutManager = new GridLayoutManager(getActivity(), 4);
        mPicReclView.setLayoutManager(picLayoutManager);
        mPicAdapter = new DetailPhotoAdapter(getActivity(), picInfos, mMaxNum);
        mPicReclView.setAdapter(mPicAdapter);
        mPicAdapter.setOnItemClickListener(onItemClickListener);

        //设定十六宫格
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mVideoReclView.setLayoutManager(gridLayoutManager);
        mPhotoVideoAdapter = new PhotoVideoAdapter(getActivity(), videoInfos, mMaxNum);
        mVideoReclView.setAdapter(mPhotoVideoAdapter);
        mPhotoVideoAdapter.setOnItemPlayListener(onItemPlayListener);


    }

    private void initData() {
        if (disasterId != null && !disasterId.equals("")) {
            mDisasterDetailPresenter.getDisasterDetailInfo(disasterId, isShowProgress);
        }
    }

    private DetailPhotoAdapter.OnItemClickListener onItemClickListener = new DetailPhotoAdapter.OnItemClickListener() {
        @Override
        public void onPreViewItemClick(View v, int position) {

            if (mPathDatas == null || mPathDatas.isEmpty()) {
                ToastUtils.toastShortMessage("图片暂未下载成功");
                return;
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

    private PhotoVideoAdapter.OnItemPlayListener onItemPlayListener = new PhotoVideoAdapter.OnItemPlayListener() {
        @Override
        public void onItemPlay(View view, int position) {

            File tempVideoFile;
            Uri uri;
            if (mPathVideos == null || mPathVideos.isEmpty()) {
                ToastUtils.toastShortMessage("视频暂未下载成功");
                return;
            }

            if (mPathVideos.get(position) != null && !mPathVideos.get(position).equals("")) {
                tempVideoFile = new File(mPathVideos.get(position));
                if (tempVideoFile.exists()) {
                    tempVideoFile.mkdir();
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(TUIKit.getAppContext(), TUIKit.getAppContext().getApplicationInfo().packageName + ".uikit.fileprovider", tempVideoFile);
//                        Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".FileProvider", tempVideoFile);
                        intent.setDataAndType(contentUri, "video/mp4");
                    } else {
                        uri = Uri.fromFile(tempVideoFile);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.fromFile(tempVideoFile), "video/mp4");
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setData() {

        if (disasterDetail != null) {
            mTvAddress.setText(disasterDetail.getZhdd());
            mTvAlarmTime.setText(disasterDetail.getBjsj());
            mTvAlarmPerson.setText(disasterDetail.getBjr());
            mTvPhoneNumber.setText(disasterDetail.getBjdh());
            mTvPresentTime.setText(disasterDetail.getDcsj());
            mTvEventNumbering.setText(disasterDetail.getZqbm());
            mTvDescription.setText(disasterDetail.getZqms());
//            mTvTrappedPeople.setText(disasterDetail.getBkrs());
            mTvFiredArea.setText(disasterDetail.getRsmj());
            mTvInjured.setText(disasterDetail.getBkrs());
            mTvDeathPeople.setText(disasterDetail.getSwrs());
            mTvEvacuation.setText(disasterDetail.getBkrs());
        }

        if (disasterDetail.getXqzdjgdm() != null) {
            mTvZDName.setText(disasterDetail.getXqzdjgdm().getName());
        }
        if (disasterDetail.getOragn() != null) {
            mTvUpperName.setText(disasterDetail.getOragn().getName());
        }

        if (disasterDetail.getZqlx() != null) {
            mTvAlarmSort.setText(disasterDetail.getZqlx().getCodeName());
        }
        if (disasterDetail.getZqpd() != null) {
            mTvAlarmCharge.setText(disasterDetail.getZqpd().getCodeName());
        }
        if (disasterDetail.getZhdj() != null) {
            mTvAlarmRange.setText(disasterDetail.getZhdj().getCodeName());
        }

        if (disasterDetail.getExtend() != null && disasterDetail.getExtend().getFirematterclass() != null) {
            mTvFiredMass.setText(disasterDetail.getExtend().getFirematterclass().getCodeName());
        }
        if (disasterDetail.getZqzt() != null) {
            mTvAlarmState.setText(disasterDetail.getZqzt().getCodeName());
        }
        if (disasterDetail.getExtend() != null && disasterDetail.getExtend().getSmogstatus() != null) {
            mTvSmog.setText(disasterDetail.getExtend().getSmogstatus().getCodeName() != null ?
                    disasterDetail.getExtend().getSmogstatus().getCodeName() : "");
        }
        if (disasterDetail.getExtend() != null) {
            if (disasterDetail.getExtend().getIsGravenessCase().equals("1")) {
                mTvImportantAlarm.setText("是");
            } else {
                mTvImportantAlarm.setText("否");
            }
        }

        if (disasterDetail.getFiles() != null) {
            files = disasterDetail.getFiles();
            combineUrl(files);
        }

        divideFiles(files);

        for (int i = 0; i < picInfos.size(); i++) {
            FightPicInfo temp = picInfos.get(i);
            String url = temp.getUrl();
            onGetPicFromUrl(temp, url);
        }

        for (int i = 0; i < videoInfos.size(); i++) {
            FightPicInfo temp = videoInfos.get(i);
            String url = temp.getUrl();
            onGetVideoFromUrl(temp, url);
        }

        mPicAdapter.notifyDataSetChanged();
        mPhotoVideoAdapter.notifyDataSetChanged();
    }

    private void onGetPicFromUrl(final FightPicInfo temp, String url) {

        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, ConstData.CHAT_PIC_DIR, "", true, false);

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

                temp.setFilePhonePath(filePath);
                mPathDatas.add(filePath);

                mPicAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancel(int what) {
                Log.e(TAG, "onCancel: ");

            }
        });
    }

    private void onGetVideoFromUrl(final FightPicInfo temp, String url) {

        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, ConstData.CHAT_PIC_DIR, "", true, false);

        NoHttpManager.getDownloadInstance().add(0, downloadRequest, new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                Log.e(TAG, "onDownloadError: " + exception);
                ToastUtils.toastShortMessage("下载视频失败");
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
//                ToastUtils.toastShortMessage("下载视频完成！");

                temp.setFilePhonePath(filePath);
                mPathVideos.add(filePath);

                mPhotoVideoAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancel(int what) {
                Log.e(TAG, "onCancel: ");

            }
        });
    }

    /**
     *
     */
    private void divideFiles(List<FightPicInfo> files) {
        if (files != null || !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                FightPicInfo temp = files.get(i);
//                String url = temp.getUrl();
                if (temp.getType().equals("01")) {//01表示视频
//                   mPathVideos.add(url);
                    videoInfos.add(temp);
                } else if (temp.getType().equals("02")) {//02表示图片
//                    mPathDatas.add(url);
                    picInfos.add(temp);
                }
            }
        }
    }

    /**
     * 组合URL
     */
    private void combineUrl(List<FightPicInfo> info) {

        if (info != null) {
            for (int i = 0; i < info.size(); i++) {
                FightPicInfo tempInfo = info.get(i);

                String url = "http://" + ConstData.urlManager.serverIp + ":" + ConstData.urlManager.serverPort
                        + "/files/" + tempInfo.getSavepath();
                info.get(i).setUrl(url);
            }
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
    }

    @Override
    public void onDisasterDetailCompleted(DisasterDetail info) {
        if (info != null) {
            disasterDetail = info;
            setData();
        }
    }

    @Override
    public void onClick(View view) {
        final int vId = view.getId();
        if (R.id.iv_disaster_detail_chat == vId) {//聊天
            /**
             * 判断是人工报警还是APP报警
             * 人工报警直接切换到电话页面，并抽取当前报警电话出来
             * APP报警则切换到聊天页面
             */
            TUIKitLog.e("===点击通讯录处理=========" + disasterDetail.getBjrId());
            /*点击通讯录处理*/
            if (!TextUtils.isEmpty(disasterDetail.getBjrId())) {
                Intent chatIntentm = new Intent(getActivity(), IMSingleActivity.class);
                chatIntentm.putExtra(IMKeys.INTENT_ID, disasterDetail.getBjrId());
                startActivity(chatIntentm);
            } else if (!TextUtils.isEmpty(disasterDetail.getBjdh())) {
                try {
//                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + disasterDetail.getBjdh()));
                    Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + disasterDetail.getBjdh()));
                    dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialIntent);
                } catch (Exception e) {
                    ToastUtils.toastShortMessage("该设备不支持拨打电话");
                }
            } else {
                ToastUtils.toastShortMessage("暂无报警电话");
            }
        } else if (R.id.tv_disaster_detail_edit == vId) {//编辑详情
            Intent intent = new Intent(getActivity(), EditDetailActivity.class);
            intent.putExtra("disasterId", disasterId);
            intent.putExtra("disasterDetail", (Serializable) disasterDetail);
            startActivityForResult(intent, EDIT_DISASTER_DETAIL);

        } else {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_DISASTER_DETAIL) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String fireArea = bundle.getString("fireArea");
                    String trappedPeople = bundle.getString("trappedPeople");
                    int hurtPeople = bundle.getInt("hurtPeople");
                    int deadPeople = bundle.getInt("deadPeople");
                    disasterDetail.setRsmj(fireArea);
                    disasterDetail.setBkrs(trappedPeople);
                    disasterDetail.setSsrs(String.valueOf(hurtPeople));
                    disasterDetail.setSwrs(String.valueOf(deadPeople));
                    mTvFiredArea.setText(fireArea);
                    mTvInjured.setText(trappedPeople);
                    mTvEvacuation.setText("" + hurtPeople);
                    mTvDeathPeople.setText("" + deadPeople);
                }
            }
        }
    }
}
