package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class FireOrganItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceFireOrgan resourceFireOrgan;

    public FireOrganItem(LatLng latLng, ResourceFireOrgan resourceFireOrgan) {
        this.mPosition = latLng;
        this.resourceFireOrgan = resourceFireOrgan;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_fire_organ);
    }

    public ResourceFireOrgan getResourceFireOrgan() {
        return resourceFireOrgan;
    }

    public void setResourceFireOrgan(ResourceFireOrgan resourceFireOrgan) {
        this.resourceFireOrgan = resourceFireOrgan;
    }
}
