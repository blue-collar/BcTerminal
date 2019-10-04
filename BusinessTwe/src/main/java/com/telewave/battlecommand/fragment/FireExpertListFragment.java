package com.telewave.battlecommand.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.activity.ExpertInfoDetailActivity;
import com.telewave.battlecommand.adapter.PersonListAdapter;
import com.telewave.battlecommand.bean.ExpertInfo;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ToastUtils;
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
 * 获取消防专家
 *
 * @author zhangjun
 * @date 2019-08-06
 */
public class FireExpertListFragment extends Fragment {

    private View mView;
    //    private LinearLayout ll_zdListRoot;
    private ListView mLvExpert;
    private PersonListAdapter mAdapter;
    private List<ExpertInfo> expertInfoList = new ArrayList<>();

    private static final int GET_EXPERT_LIST_SUCCESS = 0X2001;
    private static final int GET_EXPERT_LIST_FAIL = 0X2003;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_EXPERT_LIST_SUCCESS:
                    if (msg.obj != null) {
                        List<ExpertInfo> list = (List<ExpertInfo>) msg.obj;
                        expertInfoList.clear();
                        expertInfoList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
                case GET_EXPERT_LIST_FAIL:
                    ToastUtils.toastShortMessage("获取专家信息失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_expert_list, container, false);

        initView();
        initData();
        return mView;
    }

    private void initView() {

        mLvExpert = (ListView) mView.findViewById(R.id.lv_expert_info);
        mAdapter = new PersonListAdapter(getActivity(), expertInfoList);
        mLvExpert.setAdapter(mAdapter);

        mLvExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.toastLongMessage("点击position :" + position);
                Intent intent = new Intent(getActivity(), ExpertInfoDetailActivity.class);
                intent.putExtra("ExpertInfo", (Serializable) expertInfoList.get(position));
                startActivity(intent);
            }
        });
    }

    public void initData() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getFireExpertList, RequestMethod.GET);
        request.add("name", "");
        NoHttpManager.getRequestInstance().add(getActivity(), 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                List<ExpertInfo> list = null;
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            list = gson.fromJson(responseBean.getData(), new TypeToken<List<ExpertInfo>>() {
                            }.getType());

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_EXPERT_LIST_SUCCESS;
                            msg.obj = list;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_EXPERT_LIST_FAIL;
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
                msg.what = GET_EXPERT_LIST_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, false);

    }


}
