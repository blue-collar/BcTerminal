package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class HighBuildItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceHighBuild resourceHighBuild;

    public HighBuildItem(LatLng latLng, ResourceHighBuild resourceHighBuild) {
        this.mPosition = latLng;
        this.resourceHighBuild = resourceHighBuild;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_high_build);
    }

    public ResourceHighBuild getResourceHighBuild() {
        return resourceHighBuild;
    }

    public void setResourceHighBuild(ResourceHighBuild resourceHighBuild) {
        this.resourceHighBuild = resourceHighBuild;
    }
}
