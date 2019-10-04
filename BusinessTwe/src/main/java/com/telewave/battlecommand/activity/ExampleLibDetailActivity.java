package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.adapter.ExampleLibFuJianAdapter;
import com.telewave.battlecommand.bean.ExampleBean;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.HttpResponseUtil;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
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
import java.util.List;


/**
 * 政策规范详情
 *
 * @author liwh
 * @date 2019/3/8
 */
public class ExampleLibDetailActivity extends BaseActivity {

    private static final String TAG = "ExampleLibDetail";

    private TextView exampleLibDetailTitle;
    private TextView exampleLibDetailTime;
    private TextView exampleLibDetailContent;
    private TextView exampleLibDetailFuJian;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;

    private ExampleBean exampleBean;


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_example_lib_detail);
        initView();
        String id = getIntent().getStringExtra("id");
        geExampleLibDetail(id);
    }

    private void initView() {
        exampleLibDetailTitle = (TextView) findViewById(R.id.example_lib_detail_title);
        exampleLibDetailTime = (TextView) findViewById(R.id.example_lib_detail_time);
        exampleLibDetailContent = (TextView) findViewById(R.id.example_lib_detail_content);
        exampleLibDetailFuJian = (TextView) findViewById(R.id.example_lib_detail_fujian_tv);

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        exampleLibDetailFuJian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExampleLibDetailActivity.this).inflate(R.layout.fujian_dialog, null);
                ListView fujian_listView = (ListView) view.findViewById(R.id.fujian_list);
                List<ExampleBean.FileListBean> filesBeanList = exampleBean.getFileList();
                if (filesBeanList != null && !filesBeanList.isEmpty()) {
                    ExampleLibFuJianAdapter exampleLibFuJianAdapter = new ExampleLibFuJianAdapter(ExampleLibDetailActivity.this, filesBeanList);
                    fujian_listView.setAdapter(exampleLibFuJianAdapter);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExampleLibDetailActivity.this);
                    final AlertDialog dialog = builder.create();
                    dialog.setView(view, 0, 0, 0, 0);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                } else {
                    ToastUtils.toastShortMessage("暂无附件信息");
                }
            }
        });
    }


    //获取案例库详情
    private void geExampleLibDetail(String id) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getExampleListDetail + id, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(ExampleLibDetailActivity.this, 0, request, onResponseListener, true, true);
    }

    private HttpListener onResponseListener = new HttpListener() {
        @Override
        public void onSucceed(int what, Response response) {
            // 响应结果
            String result = (String) response.get();
            Log.e(TAG, "result: " + result);
            if (result != null) {
                try {
                    ResponseBean responseBean = new ResponseBean(result);
                    if (responseBean.isSuccess()) {
                        Gson gson = new Gson();
                        ExampleBean exampleBean = gson.fromJson(responseBean.getData(), ExampleBean.class);
                        Message msg = mHandler.obtainMessage();
                        msg.what = GET_SUCCESS;
                        msg.obj = exampleBean;
                        mHandler.sendMessage(msg);
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
            HttpResponseUtil.showResponse(ExampleLibDetailActivity.this, exception, "失败");
            Message msg = mHandler.obtainMessage();
            msg.what = GET_FAIL;
            mHandler.sendMessage(msg);
        }
    };

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_SUCCESS:
                    exampleBean = (ExampleBean) msg.obj;
                    if (exampleBean != null) {
                        exampleLibDetailTitle.setText(exampleBean.getTitle());
                        exampleLibDetailTime.setText(exampleBean.getUpdateDate());
                        exampleLibDetailContent.setText(exampleBean.getContent());
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
