package com.telewave.battlecommand.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.activity.DisasterDetailActivity;
import com.telewave.battlecommand.activity.MainActivity;
import com.telewave.battlecommand.activity.NotifyListActivity;
import com.telewave.battlecommand.activity.RouteAddressActivity;
import com.telewave.battlecommand.adapter.DisasterInfoRecyclerAdapter;
import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.battlecommand.bean.DisasterType;
import com.telewave.battlecommand.contract.FragmentContract;
import com.telewave.battlecommand.contract.IDirectionContract;
import com.telewave.battlecommand.contract.ListenerManager;
import com.telewave.battlecommand.imui.service.GroupBindCheckOperate;
import com.telewave.battlecommand.presenter.DisasterInfoPresenter;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.DivderItemDecoration;
import com.telewave.lib.base.util.DoubleClickUtils;
import com.telewave.lib.base.util.KeyBoardHiddenUtils;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.util.DateTimePickDialogUtil;
import com.yuan.refreshlayout.NormalRefreshViewHolder;
import com.yuan.refreshlayout.RefreshLayout;
import com.yuan.refreshlayout.RefreshViewHolder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 首页
 *
 * @author liwh
 * @date 2018/12/12
 */
public class IndexPageFragment extends Fragment implements IDirectionContract.IDisasterView, View.OnClickListener {
    private static final String TAG = "IndexPageFragment";

    private View mView;

    private LinearLayout mLayoutUnClosed, mLayoutClosed, mLayoutFired, mLayoutRescued, mLayoutSocialRescued;
    private TextView mTvUnClosed, mTvClosed, mTvFired, mTvRescued, mTvSocialRescued;
    private ClearEditText mEtKeyWordTop, mEtKeyWordMore;
    private Button btnMore;
    private LinearLayout mLayoutTopView, mLayoutSearchMoreView;

    private EditText mEtStartTime, mEtEndTime;
    private Spinner mAlarmTypeSpinner;
    private MySpinnerAdapter mySpinnerAdapter;
    private RadioGroup alarmGroup;

    private RadioButton rbAll, rbUnClosed, rbClosed;
    private LinearLayout mLayoutSearch, mLayoutColseUp;
    private FrameLayout mNoteLayout;

    private boolean isPullLoadData = false;
    private boolean isLoadMoreData = true;
    private RefreshLayout disasterRefresh;
    private RecyclerView disasterReclView;
    private DisasterInfoRecyclerAdapter disasterInfoAdapter;
    private List<DisasterInfo> mDisasterList = new ArrayList<>();

    private List<String> mAlarmTypes = new ArrayList<>();
    //    private String[] alarmTypes = {"全部", "火灾扑救", "抢险救援", "社会救助", "反恐排爆", "公务执勤", "其他出动"};
    private String alarmType;//警情状态
    private DisasterInfoPresenter mDisasterInfoPresenter;
    private ScheduledExecutorService executorService;
    private long timeInterval = 3 * 60;//时间间隔3分钟

    private String IndexType;
    private String officeId;
    private String startTime;
    private String endTime;
    private String address;
    private String state = "2";//默认为未结案（警情状态：全部为空，1表示已结案，2表示未结案）
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int currentPage = 1;
    private int pageSize = 10;
    private int pageTotalNum = 0;//获取从服务器返回的总分页数
    private boolean isShowProgress = true;
    private int count = 0;//判断是否首次进入

    //和Activity,fragment通信
    public FragmentContract.F2A fToA;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_index_page, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDisasterInfoPresenter = new DisasterInfoPresenter(this);
        fToA = (MainActivity) getActivity();
        initView();
        initData();

    }

    private void initData() {
        ++count;
        officeId = ConstData.ORGANID;
        address = "";
        startTime = sdf.format(new Date()) + " 00:00:00";
        endTime = sdf.format(new Date()) + " 23:59:59";
        mDisasterInfoPresenter.getDisasterTypeCount(officeId, startTime, endTime, false);
        mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(runnable, timeInterval, timeInterval, TimeUnit.SECONDS);
    }

    /**
     * 定时刷新
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String start = sdf.format(new Date()) + " 00:00:00";
            String end = sdf.format(new Date()) + " 23:59:59";
            mDisasterInfoPresenter.getDisasterTypeCount(officeId, start, end, false);
        }
    };

    private void initView() {

        disasterReclView = mView.findViewById(R.id.rv_disaster_list);
        disasterRefresh = mView.findViewById(R.id.disaster_refresh);

        disasterRefresh.shouldHandleRecyclerViewLoadingMore(disasterReclView);
        disasterRefresh.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                isPullLoadData = true;
                currentPage = 1;
                mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, false);

            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                currentPage++;
                if (isLoadMoreData) {
                    mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, true);
                    return true;
                } else {
                    disasterRefresh.endRefreshing();
                    disasterRefresh.endLoadingMore();
                    return false;
                }
            }
        });
        RefreshViewHolder holder = new NormalRefreshViewHolder(getActivity(), true);
        disasterRefresh.setRefreshViewHolder(holder);

        mLayoutUnClosed = mView.findViewById(R.id.ll_unclosed);
        mLayoutClosed = mView.findViewById(R.id.ll_closed);
        mLayoutFired = mView.findViewById(R.id.ll_fired);
        mLayoutRescued = mView.findViewById(R.id.ll_rescued);
        mLayoutSocialRescued = mView.findViewById(R.id.ll_social);

        mAlarmTypeSpinner = mView.findViewById(R.id.sp_alarm_type);
        mTvUnClosed = mView.findViewById(R.id.tv_unclosed_num);
        mTvClosed = mView.findViewById(R.id.tv_closed_num);
        mTvFired = mView.findViewById(R.id.tv_fired_num);
        mTvRescued = mView.findViewById(R.id.tv_fire_rescued_num);
        mTvSocialRescued = mView.findViewById(R.id.tv_social_rescued_num);
        mLayoutTopView = mView.findViewById(R.id.ll_index_page_top_view);
        mLayoutSearchMoreView = mView.findViewById(R.id.ll_index_page_search_more_view);
        mEtKeyWordTop = mView.findViewById(R.id.edit_top_key_word);
        btnMore = mView.findViewById(R.id.btn_more);
        mEtStartTime = mView.findViewById(R.id.et_start_time);
        mEtEndTime = mView.findViewById(R.id.et_end_time);
        alarmGroup = mView.findViewById(R.id.rg_all);
        rbAll = mView.findViewById(R.id.rb_all);
        rbClosed = mView.findViewById(R.id.rb_closed);
        rbUnClosed = mView.findViewById(R.id.rb_unclosed);
        mEtStartTime.setText(sdf.format(new Date()));
        mEtEndTime.setText(sdf.format(new Date()));
        mEtStartTime.setOnClickListener(this);
        mEtEndTime.setOnClickListener(this);

        mEtKeyWordMore = mView.findViewById(R.id.et_key_word_more);
        mLayoutSearch = mView.findViewById(R.id.ll_search);
        mLayoutColseUp = mView.findViewById(R.id.ll_close_up);
        mLayoutSearch.setOnClickListener(this);
        mLayoutColseUp.setOnClickListener(this);
        mNoteLayout = mView.findViewById(R.id.toolbar_disaster_list);

        alarmGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                state = "";
                if (R.id.rb_all == checkId) {
                    state = "";
                } else if (R.id.rb_closed == checkId) {
                    state = "1";
                } else if (R.id.rb_unclosed == checkId) {
                    state = "2";
                } else {
                }
            }
        });

        mLayoutUnClosed.setOnClickListener(this);
        mLayoutClosed.setOnClickListener(this);
        mLayoutFired.setOnClickListener(this);
        mLayoutRescued.setOnClickListener(this);
        mLayoutSocialRescued.setOnClickListener(this);
        btnMore.setOnClickListener(this);
        mLayoutSearch.setOnClickListener(this);
//        mNoteLayout.setOnClickListener(this);
        mNoteLayout.setOnClickListener(null);//暂时屏蔽点击功能

        //简单的string数组适配器：样式res，数组
        //        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
        //                android.R.layout.simple_spinner_item, alarmTypes);
        //        //下拉的样式res
        //        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAlarmTypes = Arrays.asList("全部", "火灾扑救", "抢险救援", "社会救助", "反恐排爆", "公务执勤", "其他出动");
        mySpinnerAdapter = new MySpinnerAdapter(mAlarmTypes);
        //绑定 Adapter到控件
        mAlarmTypeSpinner.setAdapter(mySpinnerAdapter);
        mAlarmTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (mAlarmTypes.get(position).equals("全部")) {
                    alarmType = "";
                } else if (mAlarmTypes.get(position).equals("火灾扑救")) {
                    alarmType = "10000";
                } else if (mAlarmTypes.get(position).equals("抢险救援")) {
                    alarmType = "20000";
                } else if (mAlarmTypes.get(position).equals("社会救助")) {
                    alarmType = "50000";
                } else if (mAlarmTypes.get(position).equals("反恐排爆")) {
                    alarmType = "30000";
                } else if (mAlarmTypes.get(position).equals("公务执勤")) {
                    alarmType = "40000";
                } else {//其他出动
                    alarmType = "60000";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mEtKeyWordTop.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                KeyBoardHiddenUtils.closeInput(mEtKeyWordTop);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    address = mEtKeyWordTop.getText().toString().trim();
                    if (!TextUtils.isEmpty(address)) {
//                      startTime = "";
//                      endTime = "";
                        alarmType = "";
                        state = null;
                        mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);
                    }
                }
                address = "";
                return false;
            }
        });

        mEtKeyWordMore.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                KeyBoardHiddenUtils.closeInput(mEtKeyWordMore);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    address = mEtKeyWordMore.getText().toString().trim();
                    if (!TextUtils.isEmpty(address)) {
                        mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);
                        address = "";
                    }
                }
                return false;
            }
        });

        disasterInfoAdapter = new DisasterInfoRecyclerAdapter(getActivity(), mDisasterList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        disasterReclView.setLayoutManager(linearLayoutManager);
        //设置为垂直布局，这也是默认的
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        disasterReclView.addItemDecoration(new DivderItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        disasterInfoAdapter.setOnItemClickListener(onDisasterItemClickListener);
        disasterReclView.setAdapter(disasterInfoAdapter);

    }

    class MySpinnerAdapter extends BaseAdapter {
        private List<String> list;

        public MySpinnerAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_alarm_type, null);
            TextView textView = convertView.findViewById(R.id.text1);
            textView.setText(list.get(position));
            return convertView;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    private DisasterInfoRecyclerAdapter.OnItemClickListener onDisasterItemClickListener = new DisasterInfoRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            DisasterInfo tempInfo = mDisasterList.get(position);
            Intent intent = new Intent(getActivity(), DisasterDetailActivity.class);
            intent.putExtra("disasterId", mDisasterList.get(position).getId());
            intent.putExtra("DisasterInfo", (Serializable) tempInfo);
            startActivity(intent);

        }

        @Override
        public void onLocated(View view, int position) {
            DisasterInfo disasterInfo = mDisasterList.get(position);
            String disasterId = disasterInfo.getId();
            String mapx = "";
            String mapy = "";
            String address = "";
            String disasterType = "";
            Field[] fields = DisasterInfo.class.getDeclaredFields();

            for (Field field : fields) {
                if (field.getName().equals("gisX")) {
                    mapx = disasterInfo.getGisX();
                    continue;
                } else if (field.getName().equals("gisY")) {
                    mapy = disasterInfo.getGisY();
                    continue;
                } else if (field.getName().equals("zhdd")) {
                    address = disasterInfo.getZhdd();
                    continue;
                } else if (field.getName().equals("zqlx")) {
                    disasterType = disasterInfo.getZqlx().getCodeValue();
                    continue;
                }
            }
            if (!TextUtils.isEmpty(mapx) && !TextUtils.isEmpty(mapy) &&
                    !TextUtils.isEmpty(address) && !TextUtils.isEmpty(disasterType)) {
                fToA.selectViewPagerIndex(2);
                ListenerManager.getInstance().sendRescueDisposalReLoadPositionData(disasterId, disasterType, mapx, mapy, address);
            } else {
                ToastUtils.toastShortMessage("暂无经纬度和地址");
            }
        }

        @Override
        public void onChatted(View view, int position) {
            if (!DoubleClickUtils.isFastDoubleClick()) {
                /*点击通讯录处理*/
                GroupBindCheckOperate.getInstance().checkGroup(getActivity(), mDisasterList.get(position));
            }
        }

        @Override
        public void onNavigated(View view, int position) {
            DisasterInfo disasterInfo = mDisasterList.get(position);
            String mapx = "";
            String mapy = "";
            String address = "";
            Field[] fields = DisasterInfo.class.getDeclaredFields();

            for (Field field : fields) {
                if (field.getName().equals("gisX")) {
                    mapx = disasterInfo.getGisX();
                    continue;
                } else if (field.getName().equals("gisY")) {
                    mapy = disasterInfo.getGisY();
                    continue;
                } else if (field.getName().equals("zhdd")) {
                    address = disasterInfo.getZhdd();
                    continue;
                }
            }
            if (!TextUtils.isEmpty(mapx) && !TextUtils.isEmpty(mapy) &&
                    !TextUtils.isEmpty(address)) {
                LatLng naviLatLng = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));
                Intent intent = new Intent(getActivity(), RouteAddressActivity.class);
                intent.putExtra("enableLatLng", naviLatLng);
                intent.putExtra("enableAddress", address);
                startActivity(intent);
            } else {
                ToastUtils.toastShortMessage("暂无经纬度和地址");
            }
        }
    };

    /**
     * 根据顶部类别来刷新数据
     */
    private void selectTypeDatas(String type) {
//        startTime = "";
//        endTime = "";
//        address = "";
        if (type.equals("1")) {//已结案
            alarmType = null;
            state = type;
            mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);

        } else if (type.equals("2")) {//未结案
            alarmType = null;
            state = type;
            mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);

        } else if (type.equals("10000")) {
            alarmType = type;
            state = null;
            mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);

        } else if (type.equals("20000")) {
            alarmType = type;
            state = null;
            mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);
        } else if (type.equals("50000")) {
            alarmType = type;
            state = null;
            mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);
        }
    }

    @Override
    public void onClick(View view) {
        final int vId = view.getId();
        if (R.id.btn_more == vId) {//更多筛选
            mLayoutTopView.setVisibility(View.GONE);
            mLayoutSearchMoreView.setVisibility(View.VISIBLE);
            address = mEtKeyWordTop.getText().toString().trim();
            if (address != null && !address.equals("")) {
                mEtKeyWordMore.setText(address);
            }
        } else if (R.id.ll_close_up == vId) {//收起
            mLayoutSearchMoreView.setVisibility(View.GONE);
            mLayoutTopView.setVisibility(View.VISIBLE);
            address = mEtKeyWordMore.getText().toString().trim();
            if (address != null && !address.equals("")) {
                mEtKeyWordTop.setText(address);
            }
        } else if (R.id.ll_search == vId) {//查询
            if (!mEtStartTime.getText().toString().trim().equals("")) {
                startTime = mEtStartTime.getText().toString().trim() + " 00:00:00";
            } else {
                startTime = "";
            }
            if (!mEtEndTime.getText().toString().trim().equals("")) {
                endTime = mEtEndTime.getText().toString().trim() + " 23:59:59";
            } else {
                endTime = "";
            }

            address = mEtKeyWordMore.getText().toString().trim();
            if (address != null && !address.equals("")) {
                mEtKeyWordTop.setText(address);
            }
//                mDisasterInfoPresenter.getDisasterTypeCount(officeId, startTime, endTime, false);
            mDisasterInfoPresenter.getDisasterList(officeId, startTime, endTime, address, alarmType, state, currentPage, pageSize, isShowProgress);
        } else if (R.id.ll_unclosed == vId) {//未结案
            IndexType = state = "2";
            alarmType = "";
//                rbUnClosed.setChecked(true);
            selectTypeDatas(IndexType);
        } else if (R.id.ll_closed == vId) {//已结案
            IndexType = state = "1";
            alarmType = "";
//                rbClosed.setChecked(true);
            selectTypeDatas(IndexType);
        } else if (R.id.ll_fired == vId) {//火灾扑救
//                IndexType = alarmType = "10000";
            IndexType = "10000";
//                state = "";
//                mAlarmTypeSpinner.setSelection(0);
            selectTypeDatas(IndexType);
        } else if (R.id.ll_rescued == vId) {//抢险救援
//                IndexType = alarmType = "20000";
            IndexType = "20000";
//                state = "";
//                mAlarmTypeSpinner.setSelection(1);
            selectTypeDatas(IndexType);
        } else if (R.id.ll_social == vId) {//社会救助
//                IndexType = alarmType = "50000";
            IndexType = "50000";
//                state = "";
//                mAlarmTypeSpinner.setSelection(2);
            selectTypeDatas(IndexType);
        } else if (R.id.toolbar_disaster_list == vId) {
            startActivity(new Intent(getActivity(), NotifyListActivity.class));
        } else if (R.id.et_start_time == vId) {//开始时间
            KeyBoardHiddenUtils.closeInput(getActivity());
            DateTimePickDialogUtil startDialog = new DateTimePickDialogUtil(getActivity());
            startDialog.dateTimePicKDialog(mEtStartTime, mEtEndTime.getEditableText().toString().trim(), DateTimePickDialogUtil.SELECT_START_TIME);
        } else if (R.id.et_end_time == vId) {//结束时间
            KeyBoardHiddenUtils.closeInput(getActivity());
            DateTimePickDialogUtil endDialog = new DateTimePickDialogUtil(getActivity());
            endDialog.dateTimePicKDialog(mEtEndTime, mEtStartTime.getEditableText().toString().trim(), DateTimePickDialogUtil.SELECT_END_TIME);
        } else {
        }
    }

    /**
     * 显示等待对画框
     */
    @Override
    public void showWaitDialog() {
        if (!ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.showDialog(getActivity(), "正在加载,请稍后...");
        }
    }

    /**
     * 隐藏掉等待对话框
     */
    @Override
    public void dismissWaitDialog() {
        if (ProgressDialogUtils.isDialogShowing()) {
            ProgressDialogUtils.dismissDialog();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissWaitDialog();
        ToastUtils.toastShortMessage(msg);
        mDisasterList.clear();
        disasterInfoAdapter.setList(mDisasterList);
        disasterInfoAdapter.notifyDataSetChanged();
        if (isPullLoadData) {
            disasterRefresh.endRefreshing();
        }
    }

    @Override
    public void onDisasterTypeLoadingCompleted(DisasterType disasterType) {

        mTvUnClosed.setText("" + disasterType.getCountWja());
        mTvClosed.setText("" + disasterType.getCountYja());
        mTvFired.setText("" + disasterType.getCountHzpj());
        mTvRescued.setText("" + disasterType.getCountQxjy());
        mTvSocialRescued.setText("" + disasterType.getCountShjz());
    }

    @Override
    public void onDisasterListCompleted(List<DisasterInfo> infoList) {

        Log.e(TAG, "onDisasterListCompleted: " + currentPage);
        if (infoList == null || infoList.isEmpty()) {
            if (isLoadMoreData) {
                ToastUtils.toastShortMessage("没有更多的数据");
            } else {
                ToastUtils.toastShortMessage("暂无数据");
            }
            //不是加载更多的话,就不会将原来的清除掉
            if (!isLoadMoreData) {
                mDisasterList.clear();
                disasterInfoAdapter.notifyDataSetChanged();
            }
            disasterRefresh.endRefreshing();
            disasterRefresh.endLoadingMore();
            return;
        }
        if (isPullLoadData) {
            mDisasterList.clear();
        }
        if (!isLoadMoreData) {
            mDisasterList.clear();
        }
        mDisasterList.addAll(infoList);
        //去除重复数据
        Set set = new HashSet(mDisasterList);
        mDisasterList = new ArrayList(set);

        disasterInfoAdapter.notifyDataSetChanged();
        disasterRefresh.endRefreshing();
        disasterRefresh.endLoadingMore();
        pageTotalNum = mDisasterInfoPresenter.getPageTotalNum();
        if (count == 1) {
            ++count;
            ToastUtils.toastShortMessage("加载完成");
        } else {
            if (currentPage >= pageTotalNum) {
                ToastUtils.toastShortMessage("已加载全部");
                isLoadMoreData = false;
            } else {
                ToastUtils.toastShortMessage("加载完成");
                isLoadMoreData = true;
                isPullLoadData = false;
            }
        }
    }
}