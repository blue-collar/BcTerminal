package com.telewave.battlecommand.bean;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.clusterutil.clustering.ClusterItem;
import com.telewave.business.twe.R;

public class NineSmallPlaceItem implements ClusterItem {
    private LatLng mPosition;
    private ResourceNineSmallPlace resourceNineSmallPlace;

    public NineSmallPlaceItem(LatLng latLng, ResourceNineSmallPlace resourceNineSmallPlace) {
        this.mPosition = latLng;
        this.resourceNineSmallPlace = resourceNineSmallPlace;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.img_mark_nine_small_place);
    }

    public ResourceNineSmallPlace getResourceNineSmallPlace() {
        return resourceNineSmallPlace;
    }

    public void setResourceNineSmallPlace(ResourceNineSmallPlace resourceNineSmallPlace) {
        this.resourceNineSmallPlace = resourceNineSmallPlace;
    }
}
