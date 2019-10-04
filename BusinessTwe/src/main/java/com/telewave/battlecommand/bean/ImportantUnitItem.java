package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class ImportantUnitItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceImportantUnit resourceImportantUnit;

    public ImportantUnitItem(LatLng latLng, ResourceImportantUnit resourceImportantUnit) {
        this.mPosition = latLng;
        this.resourceImportantUnit = resourceImportantUnit;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_zhongdiandanwei);
    }

    public ResourceImportantUnit getResourceImportantUnit() {
        return resourceImportantUnit;
    }

    public void setResourceImportantUnit(ResourceImportantUnit resourceImportantUnit) {
        this.resourceImportantUnit = resourceImportantUnit;
    }
}
