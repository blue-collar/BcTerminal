package com.telewave.battlecommand.directboard.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.directboard.adapter.LiveBoardAdapter;
import com.telewave.battlecommand.directboard.bean.LiveBoardBean;
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

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 正在直播列表
 *
 * @author liwh
 * @date 2019/8/8
 */
public class LiveBoardListActivity extends BaseActivity {
    private static final String TAG = "LiveBoardListActivity";

    private static final int GET_LIVE_BOARD_SUCCESS = 0x1003;
    private static final int GET_LIVE_BOARD_FAILURE = 0x1004;

    private View emptyView;
    private ListView liveBoardListView;

    private List<LiveBoardBean> liveBoardList = new ArrayList<LiveBoardBean>();

    private LiveBoardAdapter liveBoardAdapter;

    @Override
    public void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_all_live_board);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllOpenDirectBoard();
    }

    private void initView() {
        liveBoardListView = (ListView) findViewById(R.id.all_live_board_listview);
        emptyView = findViewById(R.id.listview_empty_view);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 查询所有正在直播列表
     */
    private void getAllOpenDirectBoard() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.findLiveingList, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(LiveBoardListActivity.this, 0, request, new HttpListener<String>() {
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
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                Log.e(TAG, "onFailed: " + url);
                msg.what = GET_LIVE_BOARD_FAILURE;
                mHandler.sendMessage(msg);
            }
        }, false, true);
    }

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_LIVE_BOARD_SUCCESS:
                    List<LiveBoardBean> liveBoardListTemp = (List<LiveBoardBean>) msg.obj;
                    if (liveBoardListTemp == null || liveBoardListTemp.isEmpty()) {
                        liveBoardListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        liveBoardList.clear();
                        liveBoardList.addAll((List<LiveBoardBean>) msg.obj);
                        liveBoardAdapter = new LiveBoardAdapter(LiveBoardListActivity.this, liveBoardList);
                        liveBoardListView.setAdapter(liveBoardAdapter);
                    }
                    break;
                case GET_LIVE_BOARD_FAILURE:
                    ToastUtils.toastShortMessage("获取失败");
                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

}
