package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class MicroStationItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceMicroStation resourceMicroStation;

    public MicroStationItem(LatLng latLng, ResourceMicroStation resourceMicroStation) {
        this.mPosition = latLng;
        this.resourceMicroStation = resourceMicroStation;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_weizhan);
    }

    public ResourceMicroStation getResourceMicroStation() {
        return resourceMicroStation;
    }

    public void setResourceMicroStation(ResourceMicroStation resourceMicroStation) {
        this.resourceMicroStation = resourceMicroStation;
    }
}
