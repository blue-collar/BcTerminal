package com.telewave.battlecommand.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.ImportUnit;
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


/**
 * 重点单位详情
 *
 * @author liwh
 * @date 2019/1/9
 */
public class ImportUnitDetailActivity extends BaseActivity {

    private static final String TAG = "ImpUnitDetailActivity";
//    private TextView reservePlanTv;

    private TextView importUnitName;
    private TextView importUnitAddress;
    private TextView importUnitDutyphone;
    private TextView importUnitContactman;
    private TextView importUnitArtificialperson;
    private TextView importUnitHoldarea;
    private TextView importUnitWorkers;
    private TextView importUnitFixedassets;
    private TextView importUnitAqglrdh;
    private TextView importUnitXfaqglr;
    private TextView importUnitAqglrsfz;
    private TextView importUnitAqzrrdh;
    private TextView importUnitAqzrrsfz;
    private TextView importUnitCjsj;
    private TextView importUnitDlwz;
    private TextView importUnitDwclsj;
    private TextView importUnitFrdb;
    private TextView importUnitFrdbdh;
    private TextView importUnitJzmj;
    private TextView importUnitYzbm;
    private TextView importUnitZjzxfglr;
    private TextView importUnitZjzxfglrdh;
    private TextView importUnitZjzxfglrsfz;

    private String importUnitId;

    private static final int GET_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;

    private static final int GET_IMPORTUNIT_INFO_DETAIL = 0x1003;


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_import_unit_detail);
        initView();
        importUnitId = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(importUnitId)) {
            getImportUnitInfoDetail(importUnitId);
        }

    }

    private void initView() {
//        reservePlanTv = (TextView) findViewById(R.id.reserve_plan_tv);
        importUnitName = (TextView) findViewById(R.id.import_unit_name);
        importUnitAddress = (TextView) findViewById(R.id.import_unit_address);
        importUnitDutyphone = (TextView) findViewById(R.id.import_unit_dutyphone);
        importUnitContactman = (TextView) findViewById(R.id.import_unit_contactman);
        importUnitArtificialperson = (TextView) findViewById(R.id.import_unit_aartificialperson);
        importUnitHoldarea = (TextView) findViewById(R.id.import_unit_holdarea);
        importUnitWorkers = (TextView) findViewById(R.id.import_unit_workers);
        importUnitFixedassets = (TextView) findViewById(R.id.import_unit_fixedassets);
        importUnitAqglrdh = (TextView) findViewById(R.id.import_unit_aqglrdh);
        importUnitXfaqglr = (TextView) findViewById(R.id.import_unit_xfaqglr);
        importUnitAqglrsfz = (TextView) findViewById(R.id.import_unit_aqglrsfz);
        importUnitAqzrrdh = (TextView) findViewById(R.id.import_unit_aqzrrdh);
        importUnitAqzrrsfz = (TextView) findViewById(R.id.import_unit_aqzrrsfz);
        importUnitCjsj = (TextView) findViewById(R.id.import_unit_cjsj);
        importUnitDlwz = (TextView) findViewById(R.id.import_unit_dlwz);
        importUnitDwclsj = (TextView) findViewById(R.id.import_unit_dwclsj);
        importUnitFrdb = (TextView) findViewById(R.id.import_unit_frdb);
        importUnitFrdbdh = (TextView) findViewById(R.id.import_unit_frdbdh);
        importUnitJzmj = (TextView) findViewById(R.id.import_unit_jzmj);
        importUnitYzbm = (TextView) findViewById(R.id.import_unit_yzbm);
        importUnitZjzxfglr = (TextView) findViewById(R.id.import_unit_zjzxfglr);
        importUnitZjzxfglrdh = (TextView) findViewById(R.id.import_unit_zjzxfglrdh);
        importUnitZjzxfglrsfz = (TextView) findViewById(R.id.import_unit_zjzxfglrsfz);

//        reservePlanTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ImportUnitDetailActivity.this, ReservePlanActivity.class);
//                intent.putExtra("id", importUnitId);
//                startActivity(intent);
//            }
//        });
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    //获取装备详细信息
    private void getImportUnitInfoDetail(String id) {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getImportunitInfoDetail + id, RequestMethod.GET);
        NoHttpManager.getRequestInstance().add(ImportUnitDetailActivity.this, GET_IMPORTUNIT_INFO_DETAIL, request, onResponseListener, true, true);
    }

    private HttpListener onResponseListener = new HttpListener() {
        @Override
        public void onSucceed(int what, Response response) {
            if (what == GET_IMPORTUNIT_INFO_DETAIL) {
                // 响应结果
                String result = (String) response.get();
                Log.e(TAG, "result: " + result);
                if (result != null) {
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            Gson gson = new Gson();
                            ImportUnit importUnit = gson.fromJson(responseBean.getData(), ImportUnit.class);
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_SUCCESS;
                            msg.obj = importUnit;
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
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
            HttpResponseUtil.showResponse(ImportUnitDetailActivity.this, exception, "失败");
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
                    ImportUnit importUnit = (ImportUnit) msg.obj;
                    if (importUnit != null) {
                        importUnitName.setText(importUnit.getObjectname());
                        importUnitAddress.setText(importUnit.getObjectaddr());
                        importUnitDutyphone.setText(importUnit.getDutyphone());
                        importUnitContactman.setText(importUnit.getContactman());
                        importUnitArtificialperson.setText(importUnit.getAartificialperson());
                        importUnitHoldarea.setText(importUnit.getHoldarea());
                        importUnitWorkers.setText(importUnit.getWorkers());
                        importUnitFixedassets.setText(importUnit.getFixedassets());
                        importUnitAqglrdh.setText(importUnit.getAqglrdh());
                        importUnitXfaqglr.setText(importUnit.getXfaqglr());
                        importUnitAqglrsfz.setText(importUnit.getAqglrsfz());
                        importUnitAqzrrdh.setText(importUnit.getAqzrrdh());
                        importUnitAqzrrsfz.setText(importUnit.getAqzrrsfz());
                        importUnitCjsj.setText(importUnit.getCjsj());
                        importUnitDlwz.setText(importUnit.getDlwz());
                        importUnitDwclsj.setText(importUnit.getDwclsj());
                        importUnitFrdb.setText(importUnit.getFrdb());
                        importUnitFrdbdh.setText(importUnit.getFrdbdh());
                        importUnitJzmj.setText(importUnit.getJzmj());
                        importUnitYzbm.setText(importUnit.getYzbm());
                        importUnitZjzxfglr.setText(importUnit.getZjzxfglr());
                        importUnitZjzxfglrdh.setText(importUnit.getZjzxfglrdh());
                        importUnitZjzxfglrsfz.setText(importUnit.getZjzxfglrsfz());
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
