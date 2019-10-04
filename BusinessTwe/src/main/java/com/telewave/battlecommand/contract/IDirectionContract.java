package com.telewave.battlecommand.contract;

import com.baidu.mapapi.model.LatLng;
import com.telewave.battlecommand.bean.ChemicalInfo;
import com.telewave.battlecommand.bean.DisasterDetail;
import com.telewave.battlecommand.bean.DisasterInfo;
import com.telewave.battlecommand.bean.DisasterType;
import com.telewave.battlecommand.bean.FightPicInfo;
import com.telewave.battlecommand.bean.FireSummaryInfo;
import com.telewave.battlecommand.bean.ImportUnit;
import com.telewave.battlecommand.bean.NewFireDocument;
import com.telewave.battlecommand.bean.PlanUnitDetail;
import com.telewave.battlecommand.bean.ResourceEmergencyUnit;
import com.telewave.battlecommand.bean.ResourceFireOrgan;
import com.telewave.battlecommand.bean.ResourceHighBuild;
import com.telewave.battlecommand.bean.ResourceHydrant;
import com.telewave.battlecommand.bean.ResourceImportantUnit;
import com.telewave.battlecommand.bean.ResourceLogisticUnit;
import com.telewave.battlecommand.bean.ResourceMicroStation;
import com.telewave.battlecommand.bean.ResourceNineSmallPlace;
import com.telewave.battlecommand.bean.VehicleDispatch;

import java.util.List;

/**
 * 协议类接口
 *
 * @author liwh
 * @date 2018/12/11
 */
public interface IDirectionContract {
    interface IClearData {
        /**
         * 清除数据
         */
        void clearData();
    }

    interface BaseView {
        /**
         * 显示等待对画框
         */
        void showWaitDialog();

        /**
         * 隐藏掉等待对话框
         */
        void dismissWaitDialog();

        void showErrorMsg(String msg);

    }

    /**
     * 危化品页面使用
     */
    interface IChemicalInfoView extends BaseView {
        void onChemicalListCompleted(List<ChemicalInfo> list);
    }

    interface IChemicalInfoPresenter extends IClearData {

        void getChemicalList(String chineseName, int currentPage, int pageSize, boolean isShowProgress);
    }

    /**
     * 预案库重点单位详情
     */
    interface IPlanUnitDetailView extends BaseView {
        void onPlanUnitDetailCompleted(PlanUnitDetail unitDetail);
    }

    interface IPlanUnitDetailPresenter extends IClearData {
        void getPlanUnitDetailInfo(String organId, boolean isShowProgress);
    }

    /**
     * 知识库预案库里重点单位页面用
     */
    interface IPlanUnitView extends BaseView {

        void onPlanUnitListCompleted(List<ImportUnit> list);
    }

    interface IPlanUnitInfoPresenter extends IClearData {

        void getPlanUnitInfo(String organId, String name, int currentPage, int pageSize, boolean isShowProgress);
    }

    /**
     * 知识库里重点单位页面使用
     */
    interface IUnitView extends BaseView {

        void onUnitListCompleted(List<ImportUnit> info);
    }

    interface ImportUnitInfoPresenter extends IClearData {

        void getImportUnitInfo(String organId, String name, int currentPage, int pageSize, boolean isShowProgress);
    }

    /**
     * 警情页面使用
     */
    interface IDisasterView extends BaseView {

        /**
         * 警情类别统计
         *
         * @param disasterType
         */
        void onDisasterTypeLoadingCompleted(DisasterType disasterType);

        /**
         * 警情列表获取
         *
         * @param infoList
         */
        void onDisasterListCompleted(List<DisasterInfo> infoList);


    }

    /**
     * 出动车辆
     */
    interface VehicleView extends BaseView {

        void onVehicleCompleted(List<VehicleDispatch> vehicleDispatchList);
    }

    interface IDisasterVehicleInfoPresenter extends IClearData {

        void getDisasterVehicleInfo(String organId, boolean isShowProgress);
    }

    /**
     * 火场总结
     */
    interface FireSummaryView extends BaseView {

        void onFireSummaryCompleted(FireSummaryInfo info);
    }

    interface IDisasterSummaryInfoPresenter extends IClearData {

        void getDisasterSummaryInfo(String organId, boolean isShowProgress);
    }

    /**
     * 火场文书
     */
    interface FireDocumentView extends BaseView {

        void onFireDocumentCompleted(List<NewFireDocument> info);
    }

    interface IDisasterFireDocumentInfoPresenter extends IClearData {

        void getDisasterFireDocumentInfo(String organId, boolean isShowProgress);

//        void onUploadFireDocumentInfo(String organId, String zqId, String fkr, String fknr, String fksj, String recordType, boolean isShowProgress);
    }

    /**
     * 作战图
     */
    interface FightPicView extends BaseView {

        void onFightPicCompleted(List<FightPicInfo> info);
    }

    interface IDisasterFightPicPresenter extends IClearData {

        void getDisasterFightPicInfo(String organId, boolean isShowProgress);

    }

    /**
     * 警情详情页使用
     */
    interface DisasterDetailView extends BaseView {

        /**
         * 警情详情统计
         *
         * @param disasterDetail
         */
        void onDisasterDetailCompleted(DisasterDetail disasterDetail);

    }


    /**
     * 指挥界面用的方法
     */
    interface IDirectionView extends BaseView {

        /**
         * 清除界面上的标记物,用来加载更过历史灾情的时候进行清除再标记
         */
        void clearMapOverlays();

        /**
         * 标记目标
         *
         * @param latLng 标记物的经纬度
         * @param type   目标物的类型
         * @param order  目标物在数据集合中顺序
         */
        void onMarkTarget(LatLng latLng, String type, int order, String disasterType);


        /**
         * 百度地图水源聚合标记
         *
         * @param latLng
         * @param resourceHydrant
         */
        void onMarkHydrantTarget(LatLng latLng, ResourceHydrant resourceHydrant);

        /**
         * 百度地图消防机构聚合标记
         *
         * @param latLng
         * @param resourceFireOrgan
         */
        void onMarkFireOrganTarget(LatLng latLng, ResourceFireOrgan resourceFireOrgan);

        /**
         * 百度地图重点单位聚合标记
         *
         * @param latLng
         * @param resourceImportantUnit
         */
        void onMarkImportantUnitTarget(LatLng latLng, ResourceImportantUnit resourceImportantUnit);

        /**
         * 百度地图微站聚合标记
         *
         * @param latLng
         * @param resourceMicroStation
         */
        void onMarkMicroStationTarget(LatLng latLng, ResourceMicroStation resourceMicroStation);

        /**
         * 百度地图应急单位聚合标记
         *
         * @param latLng
         * @param resourceEmergencyUnit
         */
        void onMarkEmergencyUnitTarget(LatLng latLng, ResourceEmergencyUnit resourceEmergencyUnit);

        /**
         * 百度地图联勤单位聚合标记
         *
         * @param latLng
         * @param resourceLogisticUnit
         */
        void onMarkLogisticUnitTarget(LatLng latLng, ResourceLogisticUnit resourceLogisticUnit);

        /**
         * 百度地图高层建筑聚合标记
         *
         * @param latLng
         * @param resourceHighBuild
         */
        void onMarkHighBuildTarget(LatLng latLng, ResourceHighBuild resourceHighBuild);

        /**
         * 百度地图九小场所聚合标记
         *
         * @param latLng
         * @param resourceNineSmallPlace
         */
        void onMarkNineSmallPlaceTarget(LatLng latLng, ResourceNineSmallPlace resourceNineSmallPlace);

    }


    interface IDisasterDetailInfoPresenter extends IClearData {
        /**
         * @param organId
         * @param isShowProgress
         */
        void getDisasterDetailInfo(String organId, boolean isShowProgress);
    }

    /**
     * 灾情信息的代理类接口
     */
    interface IDisasterInfoPresenter extends IClearData {

        /**
         * @param organId
         * @param isShowProgress
         */
        void getDisasterDetailInfo(String organId, boolean isShowProgress);

        /**
         * 统计首页警情状态数据
         *
         * @param organId        机构id
         * @param startTime      开始时间
         * @param endTime        结束时间
         * @param isShowProgress 是否显示进度条
         */
        void getDisasterTypeCount(String organId, String startTime, String endTime, boolean isShowProgress);

        /**
         * 查询首页警情列表信息
         *
         * @param organId        机构id
         * @param startTime      开始时间
         * @param endTime        结束时间
         * @param address        地址
         * @param type           报警分类
         * @param state          警情状态
         * @param currentPage    页码
         * @param pageSize       请求条数
         * @param isShowProgress 是否显示滚动条
         */
        void getDisasterList(String organId, String startTime, String endTime, String address, String type, String state, int currentPage, int pageSize, boolean isShowProgress);

        /**
         * @param organId          机构Id
         * @param isShowInfoWindow 是否显示滚动条
         */
        void getDisasterInfoDetail(String organId, boolean isShowInfoWindow);


    }

    /**
     * 周边资源代理类接口
     */
    interface IDisasterResourcesPresenter extends IClearData {

        /**
         * 消防栓数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceHydrantData(double distance, double longitude, double latitude, boolean isShowProgress);

        /**
         * 消防机构/消防队的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceFireOrganData(double distance, double longitude, double latitude, boolean isShowProgress);


        /**
         * 微站的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceMicroStationData(double distance, double longitude, double latitude, boolean isShowProgress);

        /**
         * 重点单位的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceImportUnitData(double distance, double longitude, double latitude, boolean isShowProgress);

        /**
         * 应急单位的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceEmergencyUnitData(double distance, double longitude, double latitude, boolean isShowProgress);

        /**
         * 联动单位的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceLogisticUnitData(double distance, double longitude, double latitude, boolean isShowProgress);

        /**
         * 高层建筑的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceHighBuildData(double distance, double longitude, double latitude, boolean isShowProgress);

        /**
         * 九小场所的数据
         *
         * @param distance       距离目标地点的距离
         * @param longitude      经度
         * @param latitude       纬度
         * @param isShowProgress
         */
        void getResourceNineSmallPlaceData(double distance, double longitude, double latitude, boolean isShowProgress);

    }


}