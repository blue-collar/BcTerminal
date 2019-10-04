package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telewave.battlecommand.adapter.ArrowExpandHolder;
import com.telewave.battlecommand.bean.OrganInfo;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册 微站选择
 *
 * @author liwh
 * @date 2019/1/8
 */
public class RegisterWeiZhanChoiceActivity extends BaseActivity implements TreeNode.TreeNodeClickListener {
    private static final String TAG = "ChoiceWeiZhanActivity";
    private AndroidTreeView tView;
    private ImageView searchWeizhanImageView;
    private ViewGroup containerView;
    private TreeNode root;

    private String choiceId = "";

    private List<OrganInfo> organInfoList = new ArrayList<OrganInfo>();
    private static final int GET_PARENT_SUCCESS = 0x1001;
    private static final int GET_FAIL = 0x1002;
    private static final int GET_NO_DATA = 0x1003;


    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_PARENT_SUCCESS:
                    List<OrganInfo> organInfoListTemp = (List<OrganInfo>) msg.obj;
                    if (organInfoListTemp == null || organInfoListTemp.isEmpty()) {
                        ToastUtils.toastShortMessage("暂无数据");
                    } else {
                        organInfoList.clear();
                        organInfoList.addAll(organInfoListTemp);
                        for (int i = 0; i < organInfoList.size(); i++) {
                            TreeNode treeNode = new TreeNode(new ArrowExpandHolder.IconTreeItem(organInfoList.get(i).getId(),
                                    organInfoList.get(i).getSname(), organInfoList.get(i).getType()));
                            root.addChild(treeNode);
                        }
                        containerView.addView(tView.getView());
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
    public void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_weizhan_choice);
        searchWeizhanImageView = (ImageView) findViewById(R.id.search_weizhan_iv);
        containerView = (ViewGroup) findViewById(R.id.container);
        root = TreeNode.root();
        tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        tView.setUse2dScroll(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultNodeClickListener(RegisterWeiZhanChoiceActivity.this);
        tView.setDefaultViewHolder(ArrowExpandHolder.class);
        tView.setUseAutoToggle(false);
//        tView.expandAll();

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchWeizhanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterWeiZhanChoiceActivity.this, RegisterWeiZhanSearchActivity.class);
                startActivity(intent);
            }
        });
        getOrganList();
    }

    private void fillFolder(TreeNode folder, List<OrganInfo> mOrganInfoList) {
        for (int i = 0; i < mOrganInfoList.size(); i++) {
            TreeNode treeNode = new TreeNode(new ArrowExpandHolder.IconTreeItem(mOrganInfoList.get(i).getId(),
                    mOrganInfoList.get(i).getSname(), mOrganInfoList.get(i).getType()));
            folder.addChild(treeNode);
        }
    }

    //获取机构列表信息
    private void getOrganList() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getOrganList, RequestMethod.GET);
        // 添加请求参数
        request.add("id", choiceId);
        NoHttpManager.getRequestInstance().add(RegisterWeiZhanChoiceActivity.this, 0, request, new HttpListener<String>() {
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
                            List<OrganInfo> mOrganInfoList = gson.fromJson(responseBean.getData(), new TypeToken<List<OrganInfo>>() {
                            }.getType());
                            if (null == mOrganInfoList || mOrganInfoList.size() == 0) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_NO_DATA;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = mHandler.obtainMessage();
                                msg.what = GET_PARENT_SUCCESS;
                                msg.obj = mOrganInfoList;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    @Override
    public void onClick(final TreeNode node, Object value) {
        if (((ArrowExpandHolder.IconTreeItem) value).type.equals("1")) {
            if (node.isExpanded()) {
                tView.toggleNode(node);
            } else {
                choiceId = ((ArrowExpandHolder.IconTreeItem) value).id;
                Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getOrganList, RequestMethod.GET);
                // 添加请求参数
                request.add("id", choiceId);
                NoHttpManager.getRequestInstance().add(RegisterWeiZhanChoiceActivity.this, 0, request, new HttpListener<String>() {
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
                                    List<OrganInfo> mOrganInfoList = gson.fromJson(responseBean.getData(), new TypeToken<List<OrganInfo>>() {
                                    }.getType());
                                    if (null == mOrganInfoList || mOrganInfoList.size() == 0) {
                                        Message msg = mHandler.obtainMessage();
                                        msg.what = GET_NO_DATA;
                                        mHandler.sendMessage(msg);
                                    } else {
                                        List<TreeNode> childrenTreeNodeList = node.getChildren();
                                        if (childrenTreeNodeList != null && childrenTreeNodeList.size() > 0) {
                                            for (int i = 0; i < mOrganInfoList.size(); i++) {
                                                for (int j = 0; j < childrenTreeNodeList.size(); j++) {
                                                    ArrowExpandHolder.IconTreeItem iconTreeItem = (ArrowExpandHolder.IconTreeItem) childrenTreeNodeList.get(j).getValue();
                                                    if (mOrganInfoList.get(i).getId().equals(iconTreeItem.id)) {
                                                        node.deleteChild(childrenTreeNodeList.get(j));
                                                    }
                                                }
                                            }
                                        }
                                        fillFolder(node, mOrganInfoList);
                                        tView.toggleNode(node);
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
        } else {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putString("name", ((ArrowExpandHolder.IconTreeItem) value).text);
            bundle.putString("id", ((ArrowExpandHolder.IconTreeItem) value).id);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();//传值结束
        }
    }
}
