
package com.telewave.battlecommand.imui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.telewave.lib.widget.BaseActivity;
import com.telewave.business.twe.R;

public class IMLocationActivity extends BaseActivity {

    static MapView mMapView = null;
    private BaiduMap mBaiduMap;

    //位置预览的经纬度
    private double toLatitude;
    private double toLongtitude;


    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.im_activity_location);
        mMapView = (MapView) findViewById(R.id.bmapView);

        mBaiduMap = mMapView.getMap();
        Intent intent = getIntent();
        if (intent.hasExtra("latitude") && intent.hasExtra("longitude")) {
            toLatitude = intent.getDoubleExtra("latitude", 0);
            toLongtitude = intent.getDoubleExtra("longitude", 0);
        }
        showMap(toLatitude, toLongtitude);
    }


    private void showMap(double latitude, double longtitude) {
        LatLng llA = new LatLng(latitude, longtitude);
        OverlayOptions ooA = new MarkerOptions().position(llA).icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_marka))
                .zIndex(4).draggable(true);
        mBaiduMap.addOverlay(ooA);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 17.0f);
        mBaiduMap.animateMapStatus(u);
    }


    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        showMap(toLatitude, toLongtitude);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

}
