package com.telewave.battlecommand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.telewave.battlecommand.adapter.RouteAddressSearchAdapter;
import com.telewave.battlecommand.view.ClearEditText;
import com.telewave.business.twe.R;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到地址搜索页面
 *
 * @author liwh
 * @date 2019/1/10
 */
public class RoutePlanSearchAddressActivity extends BaseActivity {
    private PoiNearbySearchOption mOption;
    private ListView searchResultListView;
    private RouteAddressSearchAdapter qianDaoAddressSearchAdapter;
    private ClearEditText qiandaoAddressSearchInput;
    private PoiInfo poiInfo = null;
    private PoiSearch mPoiSearch = null;
    private LatLng currentLatLng = null;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_route_plan_search_address);
        initView();
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(onGetPoiSearchResultListener);
        currentLatLng = getIntent().getParcelableExtra("currentLatLng");

    }

    private void initView() {
        qiandaoAddressSearchInput = (ClearEditText) findViewById(R.id.qiandao_address_search_input);
        searchResultListView = (ListView) findViewById(R.id.lv_view);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        qiandaoAddressSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = qiandaoAddressSearchInput.getText().toString().trim();
                    if (TextUtils.isEmpty(key)) {
                        ToastUtils.toastShortMessage("请输入您想要搜索的信息");
                        return true;
                    }
                    PoiNearbySearchOption option = getDefaultPoiNearbySearchOption();
                    option.keyword(key);
                    mPoiSearch.searchNearby(option);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });
    }

    public PoiNearbySearchOption getDefaultPoiNearbySearchOption() {
        mOption = new PoiNearbySearchOption();
        mOption.sortType(PoiSortType.distance_from_near_to_far);
        mOption.location(currentLatLng);
        mOption.radius(500);
        //设置每页容量，默认为每页100条
        mOption.pageCapacity(100);
        return mOption;
    }


    // POI搜索监听器
    private OnGetPoiSearchResultListener onGetPoiSearchResultListener = new OnGetPoiSearchResultListener() {
        List<PoiInfo> mInfoList = new ArrayList();

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
                ToastUtils.toastShortMessage("未找到结果");
                return;
            }
            if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
                List<PoiInfo> infos = poiResult.getAllPoi();
                Log.e("dsfdasfds", "onGetReverseGeoCodeResult: " + poiResult.getAllPoi().size());
                // 将周边信息加入表
                if (infos != null) {
                    mInfoList.clear();
                    mInfoList.addAll(infos);
                }
                // 通知适配数据已改变
                qianDaoAddressSearchAdapter = new RouteAddressSearchAdapter(RoutePlanSearchAddressActivity.this, mInfoList);
                qianDaoAddressSearchAdapter.setOnSelectedListener(new RouteAddressSearchAdapter.OnSelectedListener() {
                    @Override
                    public void onItemSelected(int position) {
                        poiInfo = (PoiInfo) qianDaoAddressSearchAdapter.getItem(position);
                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("poiInfo", poiInfo);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();//传值结束
                    }
                });
                searchResultListView.setAdapter(qianDaoAddressSearchAdapter);
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }
    };

    @Override
    protected void onDestroy() {
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
            mPoiSearch = null;
        }
        super.onDestroy();
    }
}

