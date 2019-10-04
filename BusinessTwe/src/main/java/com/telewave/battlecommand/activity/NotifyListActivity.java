package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.telewave.battlecommand.adapter.NotifyListAdapter;
import com.telewave.battlecommand.bean.NotifyInfo;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 通知列表
 *
 * @author liwh
 * @date 2019/1/14
 */
public class NotifyListActivity extends BaseActivity {
    private static final String TAG = "NotifyListActivity";
    private ListView notifyListView;
    private View emptyView;
    private List<NotifyInfo> notifyInfoList = new ArrayList<NotifyInfo>();
    private NotifyListAdapter notifyAdapter;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    List<NotifyInfo> notifyInfoListTemp = (List<NotifyInfo>) msg.obj;
                    if (notifyInfoListTemp == null || notifyInfoListTemp.isEmpty()) {
                        notifyListView.setEmptyView(emptyView);
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        notifyInfoList.clear();
                        notifyInfoList.addAll(notifyInfoListTemp);
                        notifyAdapter = new NotifyListAdapter(NotifyListActivity.this, notifyInfoList);
                        notifyListView.setAdapter(notifyAdapter);
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


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notify_list);
        initView();
        initData();
//        getNotifyList(ConstData.WZID);
    }

    public void initView() {
        notifyListView = (ListView) findViewById(R.id.notify_listview);
        emptyView = findViewById(R.id.listview_empty_view);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 4; i++) {
            NotifyInfo tempInfo = new NotifyInfo();
            tempInfo.setContent("为切实做好冬春火灾防控工作，维护全区火灾形势持续稳定，坚决预防火灾事故，强化监管职能，全力整治火灾隐患。近日，新余渝水消防联合区文化局深入歌舞娱乐场所开展消防安全检查。\n" +
                    "\n" +
                    "检查中，联合检查人员针对歌舞娱乐场所火灾危险特性，重点检查疏散通道、安全出口是否被堵塞、上锁；电气设备的安装是否规范，是否存在私拉乱接临时电气线路现象；值班人员是否在岗在位，用火用电是否符合消防安全规定；是否制定消防安全制度、灭火和应急疏散预案；是否开展每日巡查、检查等。对发现隐患部位责令立即整改，对不能马上整改的下发《限期改正通知书》，并要求单位不能有侥幸心理，加强管理，确保不发生任何火灾事故。\n" +
                    "\n" +
                    "检查结束后，联合检查组一一嘱咐各单位负责人，消防安全形势严峻，各单位一定要坚守住“安全”这根弦，严格落实好消防安全主体责任和防火巡查等管理制度，从细微处抓手，切实加强员工消防安全意识和应急处置能力培训，为辖区的消防安全形势持续稳定打下了良好的基础。\n" +
                    "\n" +
                    "为切实做好冬春火灾防控工作，维护全区火灾形势持续稳定，坚决预防火灾事故，强化监管职能，全力整治火灾隐患。近日，新余渝水消防联合区文化局深入歌舞娱乐场所开展消防安全检查。\n" +
                    "\n" +
                    "检查中，联合检查人员针对歌舞娱乐场所火灾危险特性，重点检查疏散通道、安全出口是否被堵塞、上锁；电气设备的安装是否规范，是否存在私拉乱接临时电气线路现象；值班人员是否在岗在位，用火用电是否符合消防安全规定；是否制定消防安全制度、灭火和应急疏散预案；是否开展每日巡查、检查等。对发现隐患部位责令立即整改，对不能马上整改的下发《限期改正通知书》，并要求单位不能有侥幸心理，加强管理，确保不发生任何火灾事故。\n" +
                    "\n" +
                    "检查结束后，联合检查组一一嘱咐各单位负责人，消防安全形势严峻，各单位一定要坚守住“安全”这根弦，严格落实好消防安全主体责任和防火巡查等管理制度，从细微处抓手，切实加强员工消防安全意识和应急处置能力培训，为辖区的消防安全形势持续稳定打下了良好的基础。");

            tempInfo.setCreateDate("2019-06-28 16:51:41");
            tempInfo.setId("fd48a8bad74243b6a6701209f78f7c83");
            tempInfo.setIsNewRecord(false);
            tempInfo.setOrganname("新余市消防支队");
            tempInfo.setRemarks("");
            tempInfo.setSendTime("2019-06-28 16:51:41");
            tempInfo.setTitle("做好冬春火灾防控工作");
            tempInfo.setUpdateDate("2019-06-28 16:51:41");

            NotifyInfo.UserBean user = new NotifyInfo.UserBean();
            user.setAdmin(true);
            user.setId("1");
            user.setIsNewRecord(false);
            user.setLoginFlag("1");
            user.setName("系统管理员");

            tempInfo.setUser(user);
            notifyInfoList.add(tempInfo);
        }
        notifyAdapter = new NotifyListAdapter(NotifyListActivity.this, notifyInfoList);
        notifyListView.setAdapter(notifyAdapter);
    }

//    //获取通知列表信息
//    private void getNotifyList(String wzid) {
//        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getNotifyList, RequestMethod.GET);
//        // 添加请求参数
//        request.add("wzid", wzid);
//        NoHttpManager.getRequestInstance().add(NotifyListActivity.this, 0, request, new HttpListener<String>() {
//            @Override
//            public void onSucceed(int what, Response<String> response) {
//                // 响应结果
//                String result = (String) response.get();
//                Log.e(TAG, "result: " + result);
//                if (result != null) {
//                    try {
//                        ResponseBean responseBean = new ResponseBean(result);
//                        if (responseBean.isSuccess()) {
//                            Gson gson = new Gson();
//                            List<NotifyInfo> notifyInfos = gson.fromJson(responseBean.getData(), new TypeToken<List<NotifyInfo>>() {
//                            }.getType());
//                            Message msg = mHandler.obtainMessage();
//                            msg.what = GET_SUCCESS;
//                            msg.obj = notifyInfos;
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
//
//            @Override
//            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
//                Message msg = mHandler.obtainMessage();
//                msg.what = GET_FAIL;
//                mHandler.sendMessage(msg);
//            }
//        }, true, true);
//    }
}