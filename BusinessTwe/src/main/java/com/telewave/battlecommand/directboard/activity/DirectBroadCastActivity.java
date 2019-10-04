package com.telewave.battlecommand.directboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.directboard.adapter.AllLiveBoardAdapter;
import com.telewave.battlecommand.directboard.adapter.LiveBoardOrganAdapter;
import com.telewave.battlecommand.directboard.bean.AllLiveBean;
import com.telewave.battlecommand.directboard.bean.LiveBoardBean;
import com.telewave.battlecommand.directboard.bean.LiveBoardOutObj;
import com.telewave.battlecommand.directboard.bean.LiveStream;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;
import com.yuan.refreshlayout.NormalRefreshViewHolder;
import com.yuan.refreshlayout.RefreshLayout;
import com.yuan.refreshlayout.RefreshViewHolder;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 直播
 *
 * @author liwh
 * @date 2019/1/8
 */
public class DirectBroadCastActivity extends BaseActivity {
    private static final String TAG = "DirectBroadCastActivity";

    private static final int OPEN_DIRECT_BOARD_SUCCESS = 0x1001;
    private static final int OPEN_DIRECT_BOARD_FAILURE = 0x1002;
    private static final int GET_LIVE_BOARD_SUCCESS = 0x1003;
    private static final int GET_LIVE_BOARD_FAILURE = 0x1004;
    private static final int GET_FAIL = 0x1005;
    private static final int GET_NO_DATA = 0x1006;

    private RefreshLayout rl_refreshLiveLayout;
    private RecyclerView rc_organ;
    private RecyclerView rc_all;
    private RelativeLayout allLiveBoardLayout;
    private TextView allLiveBoardNumberTv;
    private int roomId;
    private List<LiveBoardOutObj> mLiveBoardOutObjList = new ArrayList<>();
    private LiveBoardOrganAdapter directBoardMemberAdapter;

    private List<AllLiveBean> mAllLiveBeanObjList = new ArrayList<>();
    private AllLiveBoardAdapter allLiveBoardAdapter;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case OPEN_DIRECT_BOARD_SUCCESS:
                    Intent intent = new Intent(DirectBroadCastActivity.this, AnchorLiveBoardActivity.class);
                    intent.putExtra("roomId", roomId);
                    startActivity(intent);
                    break;
                case OPEN_DIRECT_BOARD_FAILURE:
                    ToastUtils.toastShortMessage("获取列表失败");
                    break;
                case GET_LIVE_BOARD_SUCCESS:
                    List<LiveBoardBean> liveBoardListTemp = (List<LiveBoardBean>) msg.obj;
                    if (liveBoardListTemp != null && !liveBoardListTemp.isEmpty()) {
                        allLiveBoardNumberTv.setText(liveBoardListTemp.size() + "");
                    }
                    break;
                case GET_LIVE_BOARD_FAILURE:
                    ToastUtils.toastShortMessage("获取失败");
                    break;
                case GET_FAIL:
                    ToastUtils.toastShortMessage("获取失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    public void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_direct_broad);
        rl_refreshLiveLayout = (RefreshLayout) findViewById(R.id.rl_refreshLive);
        rc_organ = (RecyclerView) findViewById(R.id.rc_organ);
        rc_all = (RecyclerView) findViewById(R.id.rc_all);
        allLiveBoardLayout = (RelativeLayout) findViewById(R.id.all_live_board_layout);
        allLiveBoardNumberTv = (TextView) findViewById(R.id.all_live_board_number);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.start_direct_broadcast_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDirectBoardDialog();
            }
        });
        allLiveBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectBroadCastActivity.this, LiveBoardListActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.direct_broadcast_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectBroadCastActivity.this, LiveBoardSetActivity.class);
                startActivity(intent);
            }
        });
        initEvent();
        getOpenLiveBoard();
        getOrganLiveList();
        getAllLiveList();

    }

    /**
     * 开始直播弹框提示
     */
    private void openDirectBoardDialog() {
        View dialogView = LayoutInflater.from(DirectBroadCastActivity.this).inflate(R.layout.custom_dialog, null);
        TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        TextView dialog_msg = (TextView) dialogView
                .findViewById(R.id.dialog_msg);
        tv_title.setText("温馨提示");
        dialog_msg.setText("您确定要开始直播吗?");
        AlertDialog.Builder builder = new AlertDialog.Builder(DirectBroadCastActivity.this);
        final AlertDialog dialog = builder.create();
        dialog.setView(dialogView, 0, 0, 0, 0);
        // 点击屏幕外侧，dialog不消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button btnOK = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveStream liveStream = new LiveStream();
                liveStream.setIsOpenLive("1");
                liveStream.setLiveOfficeid(ConstData.ORGANID);
                liveStream.setLiveOfficename(ConstData.ORGAN_NAME);
                liveStream.setLiveUserid(ConstData.userid);
                liveStream.setLiveUsername(ConstData.username);
                long nowTime = System.currentTimeMillis() / 1000;
                roomId = new Long(nowTime).intValue();
                liveStream.setRoomNumber(roomId + "");
                Gson gson = new Gson();
                String jsonStr = gson.toJson(liveStream);
                openDirectBoard(jsonStr);
                dialog.dismiss();
            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initEvent() {
        rl_refreshLiveLayout.shouldHandleRecyclerViewLoadingMore(rc_organ);
        rl_refreshLiveLayout.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                getOpenLiveBoard();
                getOrganLiveList();
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                rl_refreshLiveLayout.endRefreshing();
                rl_refreshLiveLayout.endLoadingMore();
                return false;
            }
        });
        RefreshViewHolder holder = new NormalRefreshViewHolder(DirectBroadCastActivity.this, true);
        rl_refreshLiveLayout.setRefreshViewHolder(holder);

    }

    /**
     * 开始直播
     * 上传直播状态
     *
     * @param jsonStr
     */
    private void openDirectBoard(String jsonStr) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.updateLiveInfo, RequestMethod.POST);
        request.setDefineRequestBodyForJson(jsonStr);
        NoHttpManager.getRequestInstance().add(DirectBroadCastActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = OPEN_DIRECT_BOARD_SUCCESS;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = OPEN_DIRECT_BOARD_FAILURE;
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
                Log.e(TAG, "onFailed: " + url);
                msg.what = OPEN_DIRECT_BOARD_FAILURE;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    /**
     * 查询所有正在直播列表
     */
    private void getOpenLiveBoard() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.findLiveingList, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(DirectBroadCastActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            List<LiveBoardBean> liveBoardList = gson.fromJson(responseBean.getData(), new TypeToken<List<LiveBoardBean>>() {
                            }.getType());
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_LIVE_BOARD_SUCCESS;
                            msg.obj = liveBoardList;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_LIVE_BOARD_FAILURE;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                rl_refreshLiveLayout.endRefreshing();
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                rl_refreshLiveLayout.endRefreshing();
                Message msg = mHandler.obtainMessage();
                Log.e(TAG, "onFailed: " + url);
                msg.what = GET_LIVE_BOARD_FAILURE;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }


    /**
     * 获取本单位直播的列表信息
     */
    private void getOrganLiveList() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.findLiveByOfficeId, RequestMethod.GET);
        request.add("officeId", ConstData.ORGANID);
        NoHttpManager.getRequestInstance().add(DirectBroadCastActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            List<LiveBoardBean> liveBoardList = gson.fromJson(responseBean.getData(), new TypeToken<List<LiveBoardBean>>() {
                            }.getType());
                            LiveBoardOutObj organLiveBoardOutObj = new LiveBoardOutObj("本单位直播", liveBoardList);
                            mLiveBoardOutObjList.clear();
                            mLiveBoardOutObjList.add(organLiveBoardOutObj);
                            directBoardMemberAdapter = new LiveBoardOrganAdapter(DirectBroadCastActivity.this, mLiveBoardOutObjList);
                            rc_organ.setAdapter(directBoardMemberAdapter);
                            rc_organ.setLayoutManager(new LinearLayoutManager(DirectBroadCastActivity.this));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                rl_refreshLiveLayout.endRefreshing();
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                rl_refreshLiveLayout.endRefreshing();
                Message msg = mHandler.obtainMessage();
                Log.e(TAG, "onFailed: " + url);
                msg.what = OPEN_DIRECT_BOARD_FAILURE;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    /**
     * 获取总队直播的列表信息
     */
    private void getAllLiveList() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.findCorpsLiveList, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(DirectBroadCastActivity.this, 0, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            List<AllLiveBean> allLiveTestList = gson.fromJson(responseBean.getData(), new TypeToken<List<AllLiveBean>>() {
                            }.getType());
                            mAllLiveBeanObjList.clear();
                            mAllLiveBeanObjList.addAll(allLiveTestList);
                            allLiveBoardAdapter = new AllLiveBoardAdapter(DirectBroadCastActivity.this, mAllLiveBeanObjList);
                            rc_all.setAdapter(allLiveBoardAdapter);
                            rc_all.setLayoutManager(new LinearLayoutManager(DirectBroadCastActivity.this));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                rl_refreshLiveLayout.endRefreshing();
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                rl_refreshLiveLayout.endRefreshing();
                Message msg = mHandler.obtainMessage();
                Log.e(TAG, "onFailed: " + url);
                msg.what = OPEN_DIRECT_BOARD_FAILURE;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }


}
