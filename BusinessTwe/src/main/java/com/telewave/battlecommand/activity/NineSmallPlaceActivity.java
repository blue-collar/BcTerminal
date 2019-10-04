package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.NinePlaceAdapter;
import com.telewave.battlecommand.bean.NineSmallPlace;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 九小场所
 *
 * @author zhangjun
 * @date 2019-08-07
 */
public class NineSmallPlaceActivity extends BaseActivity {

    private ClearEditText mEtName;
    private Button mBtnSearch;
    private ListView mLvNinePlace;
    private NinePlaceAdapter mAdapter;
    private List<NineSmallPlace> mNinePlaces = new ArrayList<>();
    private String organId = "";
    private String name = "";

    private static final int GET_NINE_SMALL_PLACE_LIST_SUCCESS = 0X2010;
    private static final int GET_NINE_SMALL_PLACE_LIST_FAIL = 0X2020;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_NINE_SMALL_PLACE_LIST_SUCCESS:
//                    ToastUtils.toastShortMessage("获取九小场所成功");
                    if (msg.obj != null) {
                        List<NineSmallPlace> list = (List<NineSmallPlace>) msg.obj;
                        mNinePlaces.clear();
                        mNinePlaces.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
                case GET_NINE_SMALL_PLACE_LIST_FAIL:
                    ToastUtils.toastShortMessage("获取九小场所失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_nine_small_place);

        initView();
        initData();
    }

    private void initView() {

        mEtName = (ClearEditText) findViewById(R.id.edit_nine_place_name_search);
        mBtnSearch = (Button) findViewById(R.id.btn_nine_place_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mEtName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    submitNineSmallPlaceParams(organId, name);
                }
            }
        });
        mEtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    name = mEtName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        submitNineSmallPlaceParams(organId, name);
                    }
                }
                return false;
            }
        });
        mLvNinePlace = (ListView) findViewById(R.id.lv_nine_place);
        mAdapter = new NinePlaceAdapter(this, mNinePlaces);
        mLvNinePlace.setAdapter(mAdapter);

        mLvNinePlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NineSmallPlaceActivity.this, NinePlaceDetailActivity.class);
                intent.putExtra("NineSmallPlace", (Serializable) mNinePlaces.get(position));
                startActivity(intent);

            }
        });

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submitNineSmallPlaceParams(String organId, String address) {

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getNineSmallPlaceList, RequestMethod.GET);
        request.add("officeId", organId);
        request.add("placeName", address);

        NoHttpManager.getRequestInstance().add(this, 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                List<NineSmallPlace> list = null;
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<NineSmallPlace>>() {
                            }.getType());

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_NINE_SMALL_PLACE_LIST_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_NINE_SMALL_PLACE_LIST_FAIL;
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
                msg.what = GET_NINE_SMALL_PLACE_LIST_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, false);
    }

    private void initData() {
        organId = ConstData.ORGANID;
        submitNineSmallPlaceParams(organId, name);

    }


}
