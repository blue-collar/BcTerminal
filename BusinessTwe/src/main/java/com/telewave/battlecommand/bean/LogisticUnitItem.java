package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class LogisticUnitItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceLogisticUnit resourceLogisticUnit;

    public LogisticUnitItem(LatLng latLng, ResourceLogisticUnit resourceLogisticUnit) {
        this.mPosition = latLng;
        this.resourceLogisticUnit = resourceLogisticUnit;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_logistic_unit);
    }

    public ResourceLogisticUnit getResourceLogisticUnit() {
        return resourceLogisticUnit;
    }

    public void setResourceLogisticUnit(ResourceLogisticUnit resourceLogisticUnit) {
        this.resourceLogisticUnit = resourceLogisticUnit;
    }
}
