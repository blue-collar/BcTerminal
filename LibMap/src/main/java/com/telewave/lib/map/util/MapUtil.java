package com.telewave.lib.map.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;

import java.io.File;

public class MapUtil {

    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名

    /**
     * 检查地图应用是否安装
     *
     * @return
     */
    public static boolean isGdMapInstalled() {
        return isInstallPackage(PN_GAODE_MAP);
    }

    public static boolean isBaiduMapInstalled() {
        return isInstallPackage(PN_BAIDU_MAP);
    }

    public static boolean isTencentMapInstalled() {
        return isInstallPackage(PN_TENCENT_MAP);
    }

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     *
     * @param latLng
     * @returns 使用此方法需要下载导入百度地图的BaiduLBS_Android.jar包
     */
    public static LatLng BD09ToGCJ02(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = latLng.longitude - 0.0065;
        double y = latLng.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lat = z * Math.sin(theta);
        double gg_lng = z * Math.cos(theta);
        return new LatLng(gg_lat, gg_lng);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     *
     * @param latLng
     * @returns 需要百度地图的BaiduLBS_Android.jar包
     */
    public static LatLng GCJ02ToBD09(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double z = Math.sqrt(latLng.longitude * latLng.longitude + latLng.latitude * latLng.latitude) + 0.00002 * Math.sin(latLng.latitude * x_pi);
        double theta = Math.atan2(latLng.latitude, latLng.longitude) + 0.000003 * Math.cos(latLng.longitude * x_pi);
        double bd_lat = z * Math.sin(theta) + 0.006;
        double bd_lng = z * Math.cos(theta) + 0.0065;
        return new LatLng(bd_lat, bd_lng);
    }

    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openGaoDeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
        if (slat != 0) {
            builder.append("&sname=").append(sname)
                    .append("&slat=").append(slat)
                    .append("&slon=").append(slon);
        }
        builder.append("&dlat=").append(dlat)
                .append("&dlon=").append(dlon)
                .append("&dname=").append(dname)
                .append("&dev=0")
                .append("&t=0");
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_GAODE_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    public static void openGaoDeDriveNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;

//        double destination[] = bdToGaoDe(dlat, dlon);
//        dlat = destination[0];
//        dlon = destination[1];
        Log.e("openGaoDeDriveNavi", "openGaoDeDriveNavi: " + dlat);
        Log.e("openGaoDeDriveNavi", "openGaoDeDriveNavi: " + dlon);

        StringBuilder builder = new StringBuilder("androidamap://navi?sourceApplication=battle_command&dev=0&style=0");

        builder.append("&lat=").append(dlat)
                .append("&lon=").append(dlon);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_GAODE_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    public static void openGaoDeBikeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        double destination[] = bdToGaoDe(dlat, dlon);
        dlat = destination[0];
        dlon = destination[1];
        StringBuilder builder = new StringBuilder("amapuri://openFeature?featureName=OnRideNavi&rideType=bike&sourceApplication=battle_command");

        builder.append("&lat=").append(dlat)
                .append("&lon=").append(dlon)
                .append("&dev=0");
        uriString = builder.toString();
        Log.e("maputil", "openBaiDuWikeNavi: " + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_GAODE_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    /**
     * 打开百度地图导航功能(默认坐标点是百度地图，不需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuRoutePlanNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving&");
        if (slat != 0) {
            //起点坐标转换
//            LatLng origin = new LatLng(slat,slon);
//            LatLng originLatLng = GCJ02ToBD09(origin);
//            slat = originLatLng.latitude;
//            slon = originLatLng.longitude;
            builder.append("origin=latlng:")
                    .append(slat)
                    .append(",")
                    .append(slon)
                    .append("|name:")
                    .append(sname);
        }
        builder.append("&destination=latlng:")
                .append(dlat)
                .append(",")
                .append(dlon)
                .append("|name:")
                .append(dname);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    /**
     * 打开百度地图导航功能(默认坐标点是百度地图，不需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuDrinveNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;

        StringBuilder builder = new StringBuilder("baidumap://map/navi?src=andr.baidu.openAPIdemo");
        builder.append("&location=")
                .append(dlat)
                .append(",")
                .append(dlon)
                .append("&coord_type=")
                .append("bd09ll");
        uriString = builder.toString();
        Log.e("maputil", "openBaiDuWikeNavi: " + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    /**
     * 打开百度地图导航功能(默认坐标点是百度地图，不需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 必填
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuBikeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("baidumap://map/bikenavi?src=andr.baidu.openAPIdemo");
        builder
                .append("&origin=")
                .append(slat)
                .append(",")
                .append(slon)
                .append("&destination=")
                .append(dlat)
                .append(",")
                .append(dlon)
                .append("&coord_type=")
                .append("bd09ll");
        uriString = builder.toString();
        Log.e("maputil", "openBaiDuWikeNavi: " + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

    /**
     * 打开百度地图导航功能(默认坐标点是百度地图，不需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 必填
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuWikeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("baidumap://map/walknavi?src=andr.baidu.openAPIdemo");
        builder
                .append("&origin=")
                .append(slat)
                .append(",")
                .append(slon)
                .append("&destination=")
                .append(dlat)
                .append(",")
                .append(dlon)
                .append("&coord_type=")
                .append("bd09ll");
        uriString = builder.toString();
        Log.e("maputil", "openBaiDuWikeNavi: " + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }
}
