package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class EmergencyUnitItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceEmergencyUnit resourceEmergencyUnit;

    public EmergencyUnitItem(LatLng latLng, ResourceEmergencyUnit resourceEmergencyUnit) {
        this.mPosition = latLng;
        this.resourceEmergencyUnit = resourceEmergencyUnit;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_emergency_unit);
    }

    public ResourceEmergencyUnit getResourceEmergencyUnit() {
        return resourceEmergencyUnit;
    }

    public void setResourceEmergencyUnit(ResourceEmergencyUnit resourceEmergencyUnit) {
        this.resourceEmergencyUnit = resourceEmergencyUnit;
    }
}
