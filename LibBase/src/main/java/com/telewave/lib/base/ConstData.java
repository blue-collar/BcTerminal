package com.telewave.lib.base;

import android.content.Context;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

import com.telewave.lib.base.util.SharePreferenceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 存放一些信息
 */

public class ConstData {
    /**
     * 新警情声音提示
     * 默认开启
     */
    public static boolean isNewDisasterVoiceOpen = true;
    /**
     * 通知声音提示
     * 默认开启
     */
    public static boolean isNotifyVoiceOpen = true;
    /**
     * 点名声音提示
     * 默认开启
     */
    public static boolean isRollCallVoiceOpen = true;
    /**
     * 新警情打印
     */
    public static boolean isPrintNewDisaster = false;

    public static List<Object> httpSignList = new ArrayList<>();

    /**
     * 终端验证是否通过\扫描二维码认证通过后设定为true
     */
    public static boolean isTerminalChecked = false;

    public static int screenWidth;
    public static int screenHeight;
    //机构名
    public static String ORGAN_NAME;
    //坐标
    public static String mapx;
    public static String mapy;
    /**
     * 微站所属机构Id
     */
    public static String ORGANID;
    /**
     * 真实名（登录完成后获取到的）
     */
    public static String username;
    /**
     * 用户ID（登录完成后获取到的）
     */
    public static String userid;
    /**
     * 是否是站长（登录完成后获取到的）
     */
    public static String isLeader;
    /**
     * 登录密码
     */
    public static String loginPassword;
    /**
     * 用户名 登录框中输入的
     */
    public static String loginName = "";
    /**
     * 微站ID
     */
    public static String WZID = "";
    /**
     * 微站地址
     */
    public static String address = "";
    /**
     * IM签名
     */
    public static String userSig = "";
    /**
     * 微站名称
     */
    public static String WZMC = "";

    //URL管理
    public static URLManager urlManager = new URLManager();

    /**
     * 设备机器码
     */
    public static String deviceId;
    /**
     * 设备号码
     */
    public static String deviceNumber;
    /**
     * 当前设备手机号
     */
    public static String phoneNumber;

    /**
     * 项目存储基路径
     */
    public static final String BASE_DIR = Environment.getExternalStorageDirectory() +
            File.separator + "Android" + File.separator + "data" + File.separator + AppProxy.getInstance().getAPPLICATION_ID();

    /**
     * 保存数据文件存储路径
     */
    public static final String DATA_DIR = BASE_DIR + File.separator + "telewave";
    /**
     * 异常捕获文件存储路径
     */
    public static final String CRASH_PATH = BASE_DIR + File.separator + "Crash";
    /**
     * 聊天基础路径
     */
    public static final String BASE_CHAT_FILE_DIR = DATA_DIR + File.separator + "chat";
    /**
     * 录音文件的存储路径
     */
    public static final String CHAT_VOICE_DIR = BASE_CHAT_FILE_DIR + File.separator + "voice";
    /**
     * 拍照后图片存储位置
     */
    public static final String CHAT_PIC_DIR = BASE_CHAT_FILE_DIR + File.separator + "picture";
    /**
     * 摄像后图片存储位置
     */
    public static final String CHAT_VIDEO_DIR = BASE_CHAT_FILE_DIR + File.separator + "video";
    /**
     * 聊天文件前缀
     */
    public static final String CHAT_FILE_PRX = "chat";

    /**
     * 点名应答录音文件的存储路径
     */
    public static final String ROLL_CALL_VOICE_DIR = DATA_DIR + File.separator + "rollcall";
    /**
     * 重点单位预案存放的路径
     */
    public static final String IMPOTANT_UNIT_DIR = DATA_DIR + File.separator + "importantUnit" + File.separator;
    /**
     * 政策规范存放路径
     */
    public static final String POLICY_GUI_FAN_DIR = DATA_DIR + File.separator + "policyGuiFan" + File.separator;
    /**
     * 案例库存放路径
     */
    public static final String EXAMPLE_LIB_DIR = DATA_DIR + File.separator + "exampleLib" + File.separator;
    /**
     * 升级包文件存放的路径
     */
    public static final String UPDATE_SYSTEM_DIR = DATA_DIR + File.separator + "apk" + File.separator;


    /**
     * 标记物的type
     */
    public static final String DISASTER_LIST_TYPE = "disaster_list_type";
    public static final String DISASTER_LIST = "disaster_list";
    public static final String DISASTER_DETAIL_TYPE = "disaster_detail_type";
    public static final String CLOSED_DISASTER_DETAIL_TYPE = "colosed_disaster_detail_type";

    public static final String DISASTER_DETAIL_FIRE_DOCUMENT_CACHEKEY = "DISASTER_DETAIL_FIRE_DOCUMENT_CACHEKEY";
    public static final String DISASTER_DETAIL_FIGHT_PIC_CACHEKDY = "DISASTER_DETAIL_FIGHT_PIC_CACHEKDY";
    public static final String DISASTER_DETAIL_FIRE_SUMMARY_CACHEKDY = "DISASTER_DETAIL_FIRE_SUMMARY_CACHEKDY";
    public static final String DISASTER_DETAIL_VEHICLE_CACHEKDY = "DISASTER_DETAIL_VEHICLE_CACHEKDY";
    public static final String KNOWLEDGE_CHEMICAL_CACHEKDY = "KNOWLEDGE_CHEMICAL_CACHEKDY";
    public static final String KNOWLEDGE_IMPORTANT_CACHEKDY = "KNOWLEDGE_IMPORTANT_CACHEKDY";
    public static final String KNOWLEDGE_UNIT_PLAN_CACHEKDY = "KNOWLEDGE_UNIT_PLAN_CACHEKDY";
    public static final String KNOWLEDGE_UNIT_DETAIL_CACHEKDY = "KNOWLEDGE_UNIT_DETAIL_CACHEKDY";

    public static final String DISASTERLIST_CACHEKEY = "DISASTERLIST_CACHEKEY";
    public static final String DISASTERDETAIL_CACHEKEY = "DISASTERDETAIL_CACHEKEY";

    public static final String HYDRANT_CACHEKEY = "HYDRANT_CACHEKEY";
    public static final String IMPORTANT_UNIT_CACHEKEY = "IMPORTANT_UNIT_CACHEKEY";
    public static final String FIRE_ORGAN_CACHEKEY = "FIRE_ORGAN_CACHEKEY";
    public static final String MICRO_STATION_CACHEKEY = "MICRO_STATION_CACHEKEY";
    public static final String EMERGENCY_UNIT_CACHEKEY = "EMERGENCY_UNIT_CACHEKEY";
    public static final String LOGISTIC_UNIT_CACHEKEY = "LOGISTIC_UNIT_CACHEKEY";
    public static final String HIGH_BUILD_CACHEKEY = "HIGH_BUILD_CACHEKEY";
    public static final String NINE_SMALL_PLACE_CACHEKEY = "NINE_SMALL_PLACE_CACHEKEY";

    public static final String RESOURCE_HYDRANT_TYPE = "hydrant_type";
    public static final String RESOURCE_IMPORTANT_UNIT_TYPE = "important_unit_type";
    public static final String RESOURCE_FIRE_ORGAN_TYPE = "fire_organ_type";
    public static final String RESOURCE_MICRO_STATION_TYPE = "micro_station_type";
    public static final String RESOURCE_EMERGENCY_UNIT_TYPE = "emergency_unit_type";
    public static final String RESOURCE_LOGISTIC_UNIT_TYPE = "logistic_unit_type";
    public static final String RESOURCE_HIGH_BUILD_TYPE = "high_build_type";
    public static final String RESOURCE_NINE_SMALL_PLACE_TYPE = "nine_small_place_type";
    public static final String RECEIVER_INTENT = "receiver_intent";
    /**
     * Bundle传递key
     */
    public static final String BUNDLE_KEY = "bundle_key";

    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    /**
     * String传递key
     */
    public static final String STRING_KEY = "string_key";
    /**
     * 和风天气 控制台 key
     */
    public static String HeWeather6Key = "505233c6d095436ab8b38c25a08483d3";
    /**
     * 首页定位地址
     */
    public static String locationAddress;
    /**
     * 首页定位经度
     */
    public static double locationLongitude;
    /**
     * 首页定位纬度
     */
    public static double locationLatitude;

    /**
     * MainActivty是否在栈顶显示
     */
    public static boolean isMainActivtyFrontShow = false;

    /**
     * 应用在前台运行是否发送上报了位置（每次应用回到前台运行上报一次）
     */
    public static boolean isAppOnFrontSendLocation = true;

    /**
     * 后台推动通用消息主题  app收消息
     */
    public static final String WebToAppTp = "WebToWZTp";
    /**
     * 即时通讯主题 同时也为终端推动到后台主题
     */
    public static final String AppToWebTp = "WZToWebTp";
    /**
     * 当前选择警情的id
     */
    public static String currentSelectDisaEventSign = "";
    /**
     * 当前选择警情编码 去掉前6位
     */
    public static String currentSelectDisaterZqbm = "";
    /**
     * 当前选择警情的上级机构OrganId
     */
    public static String currentSelectDisaterOrganId = "";

    public static String[] headers = {"全部", "未结案", "更多"};
    public static String[] disasterDate1 = {"全部", "今天", "最近三天", "最近一周", "最近一月", "最近三月", "最近六月"};
    public static String[] disasterFinish = {"未结案", "已结案"};
    public static String[] disasterDate2 = {"不限", "当天", "三天", "七天", "一个月", "三个月", "六个月", "自定义"};
    public static String[] disasterType = {"不限", "火灾扑救", "抢险救援", "反恐排爆", "公务执勤", "社会救助", "其他出动", "演练测试"};
    public static String[] disasterGrade = {"不限", "一级", "二级", "三级", "四级", "五级"};
    public static String[] disasterCasualty = {"不限", "无伤亡", "3人以内", "4-10人", "10人以上"};

    /**
     * 2019/07/25
     * 语音、视频通话中保存进入房间的userId
     * 保证不重复
     */
    public static HashSet<String> enterRoomUserIdSet = new HashSet<>();

    /**
     * 2019/07/25
     * 语音、视频通话消息中接收人员列表
     * 保证不重复
     */
    public static HashSet<SampleUser> receiveUserSet = new HashSet<>();

    /**
     * 当前用户是否在进行语音、视频通话
     */
    public static boolean isEnterTRTCCALL = false;

    /**
     * 当两个正在通话，第三方呼叫时
     * 保存这时候正在通话的trtcCallType
     * 以便操作
     */
    public static String currentTrtcCallType;

    /**
     * 当前正在通话的房间号
     */
    public static String currentRoomId;

    /**
     * 当前进入的群组ID
     */
    public static String mCurrentGroupId = "";
    /**
     * 当前进入的群组名称
     */
    public static String mCurrentGroupName = "";


    public static String TTSAppID = "16802524";
//    public static String TTSAppID = "16813680";   //微站

    public static TextToSpeech textToSpeech;

    public static boolean isTextToSpeechEnable = false;

    public static void initFromSp(Context context) {
        // IP
        ConstData.urlManager.serverIp = SharePreferenceUtils.getDataSharedPreferences(context, "server_ip");
        ConstData.urlManager.serverPort = SharePreferenceUtils.getDataSharedPreferences(context, "server_port");
        ConstData.urlManager.activemqIp = SharePreferenceUtils.getDataSharedPreferences(context, "activemq_ip");
        ConstData.urlManager.activemqPort = SharePreferenceUtils.getDataSharedPreferences(context, "activemq_port");
        ConstData.urlManager.appWebIp = SharePreferenceUtils.getDataSharedPreferences(context, "appweb_ip");
        ConstData.urlManager.appWebPort = SharePreferenceUtils.getDataSharedPreferences(context, "appweb_port");

        ConstData.urlManager.setBaseUrl(ConstData.urlManager.serverIp,
                ConstData.urlManager.serverPort,
                ConstData.urlManager.activemqIp,
                ConstData.urlManager.activemqPort,
                ConstData.urlManager.appWebIp,
                ConstData.urlManager.appWebPort);

        //从sp中取出之前保存的用户信息
        ConstData.loginName = SharePreferenceUtils.getDataSharedPreferences(context, "loginname");
        ConstData.username = SharePreferenceUtils.getDataSharedPreferences(context, "username");
        ConstData.userid = SharePreferenceUtils.getDataSharedPreferences(context, "userid");
        ConstData.phoneNumber = SharePreferenceUtils.getDataSharedPreferences(context, "lxfs");
        ConstData.isLeader = SharePreferenceUtils.getDataSharedPreferences(context, "isLeader");
        ConstData.isNewDisasterVoiceOpen = SharePreferenceUtils.getOtherBooleanDataSharedPreferences(context, "isNewDisasterVoiceOpen");
        ConstData.isNotifyVoiceOpen = SharePreferenceUtils.getOtherBooleanDataSharedPreferences(context, "isNotifyVoiceOpen");
        ConstData.isRollCallVoiceOpen = SharePreferenceUtils.getOtherBooleanDataSharedPreferences(context, "isRollCallVoiceOpen");
        ConstData.isPrintNewDisaster = SharePreferenceUtils.getBooleanDataSharedPreferences(context, "isPrintNewDisaster");
        ConstData.mapx = SharePreferenceUtils.getDataSharedPreferences(context, "mapx");
        ConstData.mapy = SharePreferenceUtils.getDataSharedPreferences(context, "mapy");
        ConstData.ORGANID = SharePreferenceUtils.getDataSharedPreferences(context, "organid");
        ConstData.ORGAN_NAME = SharePreferenceUtils.getDataSharedPreferences(context, "organName");
        ConstData.address = SharePreferenceUtils.getDataSharedPreferences(context, "address");
        ConstData.userSig = SharePreferenceUtils.getDataSharedPreferences(context, "userSig");
    }

    public static class URLManager {
        public String serverIp;
        public String serverPort;
        public String activemqIp;
        public String activemqPort;
        public String appWebIp;
        public String appWebPort;
        public static final int MqPort = 1883;
        /**
         * 管理后台基础地址
         */
        public String baseManageURL;
        /**
         * APP中web页面基础地址
         */
        public String baseAppWebURL;
        /**
         * 文件访问基础地址（包括图片、语音、视频）
         */
        public String baseFilesURL;
        /**
         * 资源访问基础地址
         */
        public String baseResourceURL;
        /**
         * 上传文件基础路径
         */
        public String upLoadFileBaseURL;
        /**
         * 检测设备绑定情况请求地址
         */
        public String httpCheckMobileInfo;
        /**
         * 登录请求地址
         */
        public String httpCheckLoginInfo;
        /**
         * 退出登录
         */
        public String logoutUrl;
        /**
         * 获取微站基础信息
         */
        public String getBasicInfo;
        /**
         * 修改微站基础信息
         */
        public String upateBasicInfo;
        /**
         * 注册用户
         */
        public String registerAccountUrl;
        /**
         * 获取微站机构列表信息
         */
        public String getOrganList;
        /**
         * 搜索微站机构列表信息
         */
        public String searchOrganList;
        /**
         * 获取责任范围列表信息
         */
        public String getZerenScopeList;
        /**
         * 获取责任范围详细信息
         */
        public String getZerenScopeInfoDetail;
        /**
         * 删除责任范围
         */
        public String zeRenScopeDelete;
        /**
         * 新增责任范围
         */
        public String zeRenScopeAdd;
        /**
         * 修改责任范围
         */
        public String zeRenScopeUpdate;
        /**
         * 获取成员信息
         */
        public String getMemberInfo;
        /**
         * 获取成员详细信息
         */
        public String getMemberInfoDetail;
        /**
         * 修改微站成员信息
         */
        public String upateMemberInfo;
        /**
         * 获取装备信息
         */
        public String getEquipmentInfo;
        /**
         * 获取装备详细信息
         */
        public String getEquipmentInfoDetail;
        /**
         * 获取处置规程
         */
        public String getDisposalProcedures;
        /**
         * 获取危化品
         */
        public String getDangerChemical;
        /**
         * 获取案例列表
         */
        public String getExampleList;
        /**
         * 获取案例详情
         */
        public String getExampleListDetail;

        /**
         * 获取搜索案例列表
         */
        public String getSearchExampleList;
        /**
         * 微站装备 web页面url
         */
        public String getWzEquipmentInfo;
        /**
         * 添加装备 web页面url
         */
        public String getWzEquipmentInfoAdd;
        /**
         * 微站装备详细信息 web页面url
         */
        public String getWzEquipmentInfoDetail;
        /**
         * 成员信息 web页面url
         */
        public String getWzMemberInfo;
        /**
         * 添加成员 web页面url
         */
        public String getWzMemberInfoAdd;
        /**
         * 成员详细信息 web页面url
         */
        public String getWzMemberInfoDetail;
        /**
         * 灾情信息统计
         */
        public String getDisasterTypeStats;
        /**
         * 获取灾情信息
         */
        public String getDisasterInfo;
        /**
         * 获取灾情信息详情
         */
        public String getDisasterInfoDetail;

        public String getAssistDisasterInfo;

        /**
         * 上传警情详情
         */
        public String uploadDisasterDetail;

        /**
         * 获取火场总结详情
         */
        public String getFireSummaryDetail;
        /**
         * 获取火场文书列表
         */
        public String getFireDocumentDetail;

        /**
         * 上传火场文书信息
         */
        public String upLoadFireDocumentInfo;
        /**
         * 获取出动车辆列表
         */
        public String getVehicleList;

        /**
         * 获取作战图
         */
        public String getFightPicList;

        /**
         * 上传作战图
         */
        public String upLoadFightPicList;

        /**
         * 删除作战图
         */
        public String deleteFightPic;

        /**
         * 获取处置规程列表
         */
        public String getDisposalProcedureList;

        /**
         * 获取处置规程详情
         */
        public String getDisposalProcedureDetail;

        /**
         * 获取危化品列表
         */
        public String getChemicalList;

        /**
         * 获取危化品详情
         */
        public String getChemicalDetail;
        /**
         * 获取政策规范列表
         */
        public String getPolicyguifanList;
        /**
         * 获取政策规范详情
         */
        public String getPolicyguifanDetail;

        /**
         * 获取搜索政策规范列表
         */
        public String getSearchPolicyList;
        /**
         * 获取重点单位列表
         */
        public String getImportunitList;
        /**
         * 获取重点单位详情
         */
        public String getImportunitInfoDetail;
        /**
         * 获取重点单位预案列表
         */
        public String getReservePlanList;

        /**
         * 获取消防专家列表
         */
        public String getFireExpertList;

        /**
         * 获取九小场所列表
         */
        public String getNineSmallPlaceList;

        /**
         * 获取警情统计列表
         */
        public String getDisasterStatistic;

        /**
         * 获取预案库下重点单位列表
         */
        public String getPlanUnitList;

        public String getPlanOrganList;

        /**
         * 获取预案库中重点单位详情
         */
        public String getPlanUnitDetail;
        /**
         * 通知列表
         */
        public String getNotifyList;
        /**
         * 获取通知详细信息
         */
        public String getNotifyInfoDetail;
        /**
         * 签到
         */
        public String qiandaoUrl;
        /**
         * 获取所有团队签到列表
         */
        public String getAllQiandaoList;
        /**
         * 获取未签到成员
         */
        public String getUnqiandaoMember;
        /**
         * 获取个人签到列表
         */
        public String getPersonQiandaoList;
        /**
         * 获取个人签到详情
         */
        public String getPersonQiandaoDetail;
        /**
         * 获取首页救援效率
         */
        public String getRescueEfficiency;
        /**
         * 获取APP更新版本信息
         */
        public String getAppUpdateInfo;
        /**
         * 获取APP更新版本记录
         */
        public String getAppUpdateRecord;
        /**
         * 根据灾情id获取即时通讯群机构微站成员列表
         */
        public String getChatMemberList;
        /**
         * 获取灾情历史聊天数据
         */
        public String getHistoryChatData;
        /**
         * 更新即时通讯消息已读未读状态
         */
        public String updateChatMessageStatus;
        /**
         * 根据灾情id获取火场文书列表
         */
        public String getFireDocumentData;
        /**
         * 报警请求接口
         */
        public String callPoliceDataUrl;
        /**
         * 修改灾情状态请求接口
         */
        public String updateDisasterStatus;
        /**
         * 获取预设信息
         */
        public String getPresetInformation;
        /**
         * 获取预设信息
         */
        public String deleteStationOrPerson;
        /*
        获取人员列表
         * */
        public String getPersonList;

        /*
         *保存微站预设信息
         * */
        public String saveLinkWz;

        /*
         *获取微站列表
         * */
        public String findWzList;
        /**
         * 获取协议
         */
        public String ProtocolURL;

        /**
         * 加载页面地址
         */
        public String webURL;

        /*
         * 更新密码
         * */
        public String updatePassward;

        /*通讯录列表*/
        public String addressAddressList;
        /*通讯录列表*/
        public String imGetJoinedGroupList;
        /*用户基本信息*/
        public String imGetUserInfo;

        /*判断是否已经绑定聊天室*/
        public String imGetGroupInfo;
        /*绑定聊天室到警情*/
        public String imSaveGroupInfo;

        /*判断群组是否已经绑定警情*/
        public String imIsDissGroup;

        /*群组加人*/
        public String imAddGroupMember;
        /*查询微站以及人员*/
        public String findAllWzAndMember;
        /*查询水源*/
        public String getWaterList;
        /*新增水源*/
        public String addWater;
        /*值班表*/
        public String getSchedule;
        /*获取中队列表tree*/
        public String getTeam;
        /*搜索装备*/
        public String searchDevice;
        /**
         * 更新用户直播信息
         */
        public String updateLiveInfo;
        /**
         * 获取正直播的列表信息
         */
        public String findLiveingList;

        /**
         * 获取本单位直播的列表信息
         */
        public String findLiveByOfficeId;

        /**
         * 查询总队的直播列表信息
         */
        public String findCorpsLiveList;

        public void setBaseUrl(String manageIP, String managePort, String mqIP, String mqPort, String appWebIP, String appWebPort) {
            baseManageURL = "http://" + manageIP + ":" + managePort + "/twmfs/TelewaveMFS/";
            webURL = "http://" + manageIP + ":" + managePort + "/twmfs/static/protocolConf/";
            baseAppWebURL = "http://" + appWebIP + ":" + appWebPort + "/#/";
            baseFilesURL = "http://" + manageIP + ":" + managePort + "/files";
            baseResourceURL = "http://" + manageIP + ":" + managePort + "/geoserver/wfs";
            upLoadFileBaseURL = "http://" + manageIP + ":" + managePort + "/upload";

            httpCheckMobileInfo = baseManageURL + "sys/regist";
            httpCheckLoginInfo = baseManageURL + "sys/login";
            getBasicInfo = baseManageURL + "mfs/bWxxfz/getDetail/";
            upateBasicInfo = baseManageURL + "mfs/bWxxfz/save4Terminal";
            getMemberInfo = baseManageURL + "mfs/mfsMemberinfo/findByWzid";
            getMemberInfoDetail = baseManageURL + "mfs/mfsMemberinfo/getDetail/";
            upateMemberInfo = baseManageURL + "mfs/mfsMemberinfo/saveMember";

            registerAccountUrl = baseManageURL + "mfs/mfsMemberinfo/addMember";
            getOrganList = baseManageURL + "sys/findChildByOfficeId";
            searchOrganList = baseManageURL + "mfs/bWxxfz/findListByParam";

            getEquipmentInfo = baseManageURL + "mfs/mfsEquipmentinfo/findByWzid";
            getEquipmentInfoDetail = baseManageURL + "mfs/mfsEquipmentinfo/getDetail/";

            getZerenScopeList = baseManageURL + "mfs/mfsResponsibility/findByWzid";
            getZerenScopeInfoDetail = baseManageURL + "mfs/mfsResponsibility/getDetail/";
            zeRenScopeAdd = baseManageURL + "mfs/mfsResponsibility/addResponsibility";
            zeRenScopeDelete = baseManageURL + "mfs/mfsResponsibility/delResponsibility";
            zeRenScopeUpdate = baseManageURL + "mfs/mfsResponsibility/saveResponsibility";

            getDisposalProcedures = baseAppWebURL + "czgc/czgc";
            getDangerChemical = baseAppWebURL + "whp/whp";

            getWzEquipmentInfo = baseAppWebURL + "wzzb/wzzb/";
            getWzEquipmentInfoAdd = baseAppWebURL + "wzzb/add/";
            getWzEquipmentInfoDetail = baseAppWebURL + "wzzb/detail/";
            getWzMemberInfo = baseAppWebURL + "wzcy/wzcy/";
            getWzMemberInfoAdd = baseAppWebURL + "wzcy/add/";
            getWzMemberInfoDetail = baseAppWebURL + "wzcy/detail/";
            getDisasterTypeStats = baseManageURL + "/alarm/zqxx/countPage";

            getDisasterTypeStats = baseManageURL + "alarm/zqxx/countPage";
            getDisasterInfo = baseManageURL + "alarm/zqxx/page";
            getDisasterInfoDetail = baseManageURL + "alarm/zqxx/getDetail/";
            getAssistDisasterInfo = baseManageURL + "alarm/zqxx/assistPage";

            uploadDisasterDetail = baseManageURL + "alarm/zqxx/saveZqxxSummary";
            getFireSummaryDetail = baseManageURL + "alarm/findHczjByAlarmId";
            getFireDocumentDetail = baseManageURL + "alarm/findHcwsListByAlarmId";
            upLoadFireDocumentInfo = baseManageURL + "alarm/saveHcws";
            getVehicleList = baseManageURL + "alarm/findDpclListByAlarmId";
            getFightPicList = baseManageURL + "alarm/findFightGraph";
            upLoadFightPicList = baseManageURL + "alarm/uploadFightGraph";
            deleteFightPic = baseManageURL + "alarm/deleteFightGraph";

            getChemicalList = baseManageURL + "assist/smDcmaterial/page";
            getChemicalDetail = baseManageURL + "assist/smDcmaterial/getDetail/";
            getDisposalProcedureList = baseManageURL + "sys/getDictDetailListByDCode";
            getDisposalProcedureDetail = baseManageURL + "assist/mcCzgc/getDetail/";

            getFireExpertList = baseManageURL + "assist/findExpertByName";
            getNineSmallPlaceList = baseManageURL + "assist/findNinePlaceByName";
            getDisasterStatistic = baseManageURL + "assist/statisticsJqNum";
            getPlanOrganList = baseManageURL + "assist/yajbxx/countOrganYaNum";
            getPlanUnitList = baseManageURL + "assist/objObjectinfo/findObjectByName";
            getPlanUnitDetail = baseManageURL + "assist/objObjectinfo/getDetail/";
            logoutUrl = baseManageURL + "sys/logout";

            getPolicyguifanList = baseManageURL + "assist/mfsRegulations/alllist";
            getPolicyguifanDetail = baseManageURL + "assist/mfsRegulations/getDetail/";
            getSearchPolicyList = baseManageURL + "assist/mfsRegulations/alllist";

            getImportunitList = baseManageURL + "assist/objObjectinfo/findObjectByName";
//            getImportunitList = baseManageURL + "assist/objObjectinfo/findObjectByWzid";      //微站
            getImportunitInfoDetail = baseManageURL + "assist/objObjectinfo/getDetail/";

            getReservePlanList = baseManageURL + "assist/yajbxx/findYajbxxByObjid";

            qiandaoUrl = baseManageURL + "duty/mfsSign/sign";
            getNotifyList = baseManageURL + "duty/mfsNotice/getNoticeList";
            getNotifyInfoDetail = baseManageURL + "duty/mfsNotice/getDetail/";

            getAllQiandaoList = baseManageURL + "duty/mfsSign/getSignList";
            getUnqiandaoMember = baseManageURL + "duty/mfsSign/getUnSignList";

            getPersonQiandaoList = baseManageURL + "duty/mfsSign/getSignHistoryByMember";
            getPersonQiandaoDetail = baseManageURL + "duty/mfsSign/getDetail/";

            getRescueEfficiency = baseManageURL + "alarm/arrivalEfficiency";

            getAppUpdateInfo = baseManageURL + "resource/upgradeDetection";
            getAppUpdateRecord = baseAppWebURL + "bbxx/bbxx";

            getChatMemberList = baseManageURL + "chat/treeData";

            getHistoryChatData = baseManageURL + "chat/historyData";
            updateChatMessageStatus = "http://" + manageIP + ":" + managePort + "/twmfs/chat/chatmsg/updateStatusByYd";

            getFireDocumentData = baseManageURL + "alarm/findHcwsListByAlarmId";

            getExampleList = baseManageURL + "assist/assCasesinfo/alllist";
            getExampleListDetail = baseManageURL + "assist/assCasesinfo/getDetail/";
            getSearchExampleList = baseManageURL + "assist/assCasesinfo/alllist";
            callPoliceDataUrl = baseManageURL + "alarm/reportAlarm";

            updateDisasterStatus = baseManageURL + "alarm/zdcdd/changeAlarmState";
            getPresetInformation = baseManageURL + "mfs/bWxxfz/getPreSet/";
            deleteStationOrPerson = baseManageURL + "mfs/wxxfzObj/delLinkWz";
            getPersonList = baseManageURL + "mfs/bWxxfz/findZjListByWzid";
            saveLinkWz = baseManageURL + "mfs/wxxfzObj/saveLinkWz";
            findWzList = baseManageURL + "mfs/bWxxfz/findListByJwd";
            ProtocolURL = webURL + "userAgreementConf.html";
            addressAddressList = baseManageURL + "address/addressList";
            updatePassward = baseManageURL + "sys/modifyPwd";

            imGetJoinedGroupList = baseManageURL + "IM/getJoinedGroupList";

            imGetUserInfo = baseManageURL + "sys/getUserInfo";
            imGetGroupInfo = baseManageURL + "IM/getGroupInfo";
            imSaveGroupInfo = baseManageURL + "IM/saveGroupInfo";
            imIsDissGroup = baseManageURL + "IM/IsDissGroup";
            imAddGroupMember = baseManageURL + "IM/addGroupMember";
            findAllWzAndMember = baseManageURL + "mfs/bWxxfz/findAllWzAndMember";
            getWaterList = baseManageURL + "mhjy/findWaterList";
            addWater = baseManageURL + "mhjy/saveWater";
            getSchedule = baseManageURL + "duty/swgl/findDuty";
            getTeam = baseManageURL + "sys/findAllOffice";
            searchDevice = baseManageURL + "assist/viewClxxOffice/findClxxList";
            updateLiveInfo = baseManageURL + "IM/updateLiveInfo";
            findLiveingList = baseManageURL + "IM/findLiveingList";
            findLiveByOfficeId = baseManageURL + "IM/findLiveByOfficeId";
            findCorpsLiveList = baseManageURL + "IM/findCorpsLiveList";
        }
    }
}
