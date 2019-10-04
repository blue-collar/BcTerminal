package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.WeiZhanSearchAdapter;
import com.telewave.battlecommand.bean.WeiZhanSearch;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.telewave.battlecommand.http.NoHttpManager.getRequestInstance;

/**
 * 注册 微站关键字搜索
 *
 * @author liwh
 * @date 2019/1/10
 */
public class RegisterWeiZhanSearchActivity extends BaseActivity {

    private static final String TAG = "RegWZSearchActivity";

    private Button weiZhanSearchBtn;
    private ClearEditText weiZhanSearchEditText;
    private ListView searchListView;
    private WeiZhanSearchAdapter weiZhanSearchAdapter;

    private List<WeiZhanSearch> weiZhanSearchList = new ArrayList<WeiZhanSearch>();
    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;
    private static final int GET_NO_DATA = 0x1003;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    List<WeiZhanSearch> weiZhanSearchListTemp = (List<WeiZhanSearch>) msg.obj;
                    if (weiZhanSearchListTemp == null || weiZhanSearchListTemp.isEmpty()) {
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        weiZhanSearchList.clear();
                        weiZhanSearchList.addAll(weiZhanSearchListTemp);
                        weiZhanSearchAdapter = new WeiZhanSearchAdapter(RegisterWeiZhanSearchActivity.this, weiZhanSearchList);
                        searchListView.setAdapter(weiZhanSearchAdapter);
                        ToastUtils.toastShortMessage("获取成功");
                    }
                    break;
                case GET_NO_DATA:
                    ToastUtils.toastShortMessage("暂无数据");
                    break;
                case GET_FAIL:
                    ToastUtils.toastShortMessage("获取失败");
                default:
                    break;
            }
            return false;
        }
    }).get());


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_weizhan_search);
        initView();
    }

    private void initView() {
        weiZhanSearchBtn = (Button) findViewById(R.id.weizhan_search_btn);
        weiZhanSearchEditText = (ClearEditText) findViewById(R.id.weizhan_search_edittext);
        searchListView = (ListView) findViewById(R.id.search_listview);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        weiZhanSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr = weiZhanSearchEditText.getText().toString();
                if (TextUtils.isEmpty(searchStr)) {
                    ToastUtils.toastShortMessage("你还没输入搜索关键字");
                } else {
                    searchWeiZhanList(searchStr);
                }
            }
        });
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RegisterWeiZhanSearchActivity.this, RegisterActivity.class);
                intent.putExtra("weiZhanName", weiZhanSearchList.get(position).getXfzmc());
                intent.putExtra("weiZhanId", weiZhanSearchList.get(position).getId());
                startActivity(intent);
                closeKeyboard();
            }
        });
    }

    //搜索微站列表信息
    private void searchWeiZhanList(String searchStr) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.searchOrganList, RequestMethod.GET);
        // 添加请求参数
        request.add("name", searchStr);
        getRequestInstance().add(RegisterWeiZhanSearchActivity.this, 0, request, new HttpListener<String>() {
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
                            List<WeiZhanSearch> mWeiZhanSearchList = gson.fromJson(responseBean.getData(), new TypeToken<List<WeiZhanSearch>>() {
                            }.getType());
                            if (null == mWeiZhanSearchList || mWeiZhanSearchList.size() == 0) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_NO_DATA;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_SUCCESS;
                                msg.obj = mWeiZhanSearchList;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {

            }
        }, true, true);
    }

}
