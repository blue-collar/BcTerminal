package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class HydrantItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceHydrant resourceHydrant;

    public HydrantItem(LatLng latLng, ResourceHydrant resourceHydrant) {
        this.mPosition = latLng;
        this.resourceHydrant = resourceHydrant;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_xiaofangshuan);
    }

    public ResourceHydrant getResourceHydrant() {
        return resourceHydrant;
    }

    public void setHydrant(ResourceHydrant resourceHydrant) {
        this.resourceHydrant = resourceHydrant;
    }
}
