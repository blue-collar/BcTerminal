package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.telewave.battlecommand.bean.NotifyInfo;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;

import java.lang.ref.WeakReference;


/**
 * 通知详情
 *
 * @author liwh
 * @date 2019/1/15
 */
public class NotifyDetailActivity extends BaseActivity {

    private static final String TAG = "NotifyDetailActivity";

    private TextView notifyDetailTitle;
    private TextView notifyDetailTime;
    private TextView notifyDetailFrom;
    private TextView notifyDetailContent;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;

    private static final int GET_NOTIFY_INFO_DETAIL = 0x1003;


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notify_detail);
        initView();
        String id = getIntent().getStringExtra("id");
//        getNotifyInfoDetail(id);
    }

    private void initView() {
        notifyDetailTitle = (TextView) findViewById(R.id.notify_detail_title);
        notifyDetailTime = (TextView) findViewById(R.id.notify_detail_time);
        notifyDetailFrom = (TextView) findViewById(R.id.notify_detail_from);
        notifyDetailContent = (TextView) findViewById(R.id.notify_detail_content);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


//    //获取装备详细信息
//    private void getNotifyInfoDetail(String id) {
//        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getNotifyInfoDetail + id, RequestMethod.GET);
//        NoHttpManager.getRequestInstance().add(NotifyDetailActivity.this, GET_NOTIFY_INFO_DETAIL, request, onResponseListener, true, true);
//    }
//
//    private HttpListener onResponseListener = new HttpListener() {
//        @Override
//        public void onSucceed(int what, Response response) {
//            if (what == GET_NOTIFY_INFO_DETAIL) {
//                // 响应结果
//                String result = (String) response.get();
//                Log.e(TAG, "result: " + result);
//                if (result != null) {
//                    try {
//                        ResponseBean responseBean = new ResponseBean(result);
//                        if (responseBean.isSuccess()) {
//                            Gson gson = new Gson();
//                            NotifyInfo notifyInfo = gson.fromJson(responseBean.getData(), NotifyInfo.class);
//                            Message msg = mHandler.obtainMessage();
//                            msg.what = GET_SUCCESS;
//                            msg.obj = notifyInfo;
//                            mHandler.sendMessage(msg);
//                        } else {
//                            Message msg = mHandler.obtainMessage();
//                            msg.what = GET_FAIL;
//                            mHandler.sendMessage(msg);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
//            HttpResponseUtil.showResponse(NotifyDetailActivity.this, exception, "失败");
//            Message msg = mHandler.obtainMessage();
//            msg.what = GET_FAIL;
//            mHandler.sendMessage(msg);
//        }
//    };

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    NotifyInfo notifyInfo = (NotifyInfo) msg.obj;
                    if (notifyInfo != null) {
                        notifyDetailTitle.setText(notifyInfo.getTitle());
                        notifyDetailTime.setText(notifyInfo.getSendTime());
                        notifyDetailFrom.setText("来源:" + notifyInfo.getOrganname());
                        notifyDetailContent.setText(notifyInfo.getContent());
                        ToastUtils.toastShortMessage("获取成功");
                    } else {
                        ToastUtils.toastShortMessage("暂无数据");
                    }
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
}
