package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.ChemicalDetail;
import com.telewave.battlecommand.http.HttpListener;
import com.telewave.battlecommand.http.NoHttpManager;
import com.telewave.battlecommand.http.ResponseBean;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.ProgressDialogUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class ChemicalDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvBasic, mTvFeature, mTvProtect, mTvDepreciation, mTvPrecautions, mTvEmergency;
    private List<TextView> mLabels = new ArrayList<>();
    private List<View> detailViews = new ArrayList<>();
    private LinearLayout mLayoutBasic, mLayoutFeature, mLayoutProtect, mLayoutPrecautions, mLayoutDepreciation, mLayoutEmergency;
    //基本信息
    private TextView mTvChineseName, mTvEnglishName, mTvImdg, mTvCas, mTvRtecs, mTvUn, mTvDangerCode, mTvMolecularFormula, mTvMolecularWeight;
    private TextView mTvMeltPoint, mTvBoilPoint, mTvFlashPoint, mTvDangerType, mTvDangerPackageFlag, mTvPackageType, mTvFireRange;
    //特性
    private TextView mTvAppearance, mTvRelDensityWater, mTvRelDensityAir, mTvStability, mTvSolubility, mTvFlammability, mTvToxicity;
    private TextView mTvAutoIgnitionTemperature, mTvCombustionHeat, mTvSaturatedVaporPressure, mTvCombustionProduct, mTvPolymerizationHazard, mTvDangerFeature;
    //防护
    private TextView mTvBreatheProtect, mTvEyeProtect, mTvHandsProtect, mTvProtectClothes, mTvAvoidContactCondition;
    //阖值
    private TextView mTvCriticalTemperature, mTvCriticalPressure, mTvContactExtremeValue, mTvSmallLeakageRadius, mTvSmallLeakageDaytime, mTvSmallLeakageNight;
    private TextView mTvLargeLeakageRadius, mTvLargeLeakageDaytime, mTvLargeLeakageNight, mTvLowerExplosionLimit, mTvUpperExplosionLimit;
    //注意事项
    private TextView mTvMainUsage, mTvFirePutMethod, mTvLeakageDisposal, mTvStorageTransportationConsiderations;
    //应急措施
    private TextView mTvForbidden, mTvBreatheIn, mTvIngestion, mTvInvasionRoute, mTvHealtHazard, mTvEngineerControl, mTvSkinContact, mTvEyeContact, mTvOther;

    private ChemicalDetail detailInfo;
    private String chemicalId;

    private static final int GET_CHEMICAL_DETAIL_SUCCESS = 0x1110;
    private static final int GET_CHEMICAL_DETAIL_FAIL = 0x1120;

    private Handler mHandler = new Handler(new WeakReference<Handler.Callback>(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            ProgressDialogUtils.dismissDialog();
            switch (msg.what) {
                case GET_CHEMICAL_DETAIL_SUCCESS:
                    if (msg.obj != null) {

                        detailInfo = (ChemicalDetail) msg.obj;
                        if (detailInfo != null) {
                            setData(detailInfo);
                        }
                    }

                    break;
                case GET_CHEMICAL_DETAIL_FAIL:
                    ToastUtils.toastShortMessage("获取危化品信息失败");

                    break;
                default:
                    break;
            }
            return false;
        }
    }).get());

    @Override
    protected void setUpViewAndData(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_chemical_detail);

        initView();
        initData();
    }

    private void initView() {

        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvBasic = findViewById(R.id.tv_chemical_basic_info);
        mTvFeature = findViewById(R.id.tv_chemical_feature);
        mTvProtect = findViewById(R.id.tv_chemical_protect);
        mTvDepreciation = findViewById(R.id.tv_chemical_depreciation);
        mTvPrecautions = findViewById(R.id.tv_chemical_precautions);
        mTvEmergency = findViewById(R.id.tv_chemical_emergency);
        mLabels.add(mTvBasic);
        mLabels.add(mTvFeature);
        mLabels.add(mTvProtect);
        mLabels.add(mTvDepreciation);
        mLabels.add(mTvPrecautions);
        mLabels.add(mTvEmergency);

        mTvBasic.setOnClickListener(this);
        mTvFeature.setOnClickListener(this);
        mTvProtect.setOnClickListener(this);
        mTvDepreciation.setOnClickListener(this);
        mTvPrecautions.setOnClickListener(this);
        mTvEmergency.setOnClickListener(this);

//        mContentView = findViewById(R.id.view_chemical_detail);
        mLayoutBasic = findViewById(R.id.ll_basic_info);
        mLayoutFeature = findViewById(R.id.ll_feature_info);
        mLayoutProtect = findViewById(R.id.ll_protect_info);
        mLayoutPrecautions = findViewById(R.id.ll_precautions_info);
        mLayoutDepreciation = findViewById(R.id.ll_depreciation_info);
        mLayoutEmergency = findViewById(R.id.ll_emergency_info);
        detailViews.add(mLayoutBasic);
        detailViews.add(mLayoutFeature);
        detailViews.add(mLayoutProtect);
        detailViews.add(mLayoutPrecautions);
        detailViews.add(mLayoutDepreciation);
        detailViews.add(mLayoutEmergency);

        mTvChineseName = findViewById(R.id.tv_chemical_chinese_name);
        mTvEnglishName = findViewById(R.id.tv_chemical_english_name);
        mTvImdg = findViewById(R.id.tv_chemical_imdg);
        mTvCas = findViewById(R.id.tv_chemical_cas);
        mTvRtecs = findViewById(R.id.tv_chemical_rtesc);
        mTvUn = findViewById(R.id.tv_chemical_un);
        mTvDangerCode = findViewById(R.id.tv_chemical_danger_code);
        mTvMolecularFormula = findViewById(R.id.tv_chemical_molecular_formula);
        mTvMolecularWeight = findViewById(R.id.tv_chemical_molecular_weight);
        mTvMeltPoint = findViewById(R.id.tv_chemical_melting_point);
        mTvBoilPoint = findViewById(R.id.tv_chemical_boiling_point);
        mTvFlashPoint = findViewById(R.id.tv_chemical_flash_point);
        mTvDangerType = findViewById(R.id.tv_chemical_danger_type);
        mTvDangerPackageFlag = findViewById(R.id.tv_chemical_danger_package_flag);
        mTvPackageType = findViewById(R.id.tv_chemical_package_type);
        mTvFireRange = findViewById(R.id.tv_chemical_fire_range);

        mTvAppearance = findViewById(R.id.tv_chemical_appearance);
        mTvRelDensityWater = findViewById(R.id.tv_chemical_relative_density_water);
        mTvRelDensityAir = findViewById(R.id.tv_chemical_relative_density_air);
        mTvStability = findViewById(R.id.tv_chemical_stability);
        mTvSolubility = findViewById(R.id.tv_chemical_solubility);
        mTvFlammability = findViewById(R.id.tv_chemical_flammability);
        mTvToxicity = findViewById(R.id.tv_chemical_toxicity);
        mTvAutoIgnitionTemperature = findViewById(R.id.tv_chemical_auto_ignition_temperature);
        mTvCombustionHeat = findViewById(R.id.tv_chemical_combustion_heat);
        mTvSaturatedVaporPressure = findViewById(R.id.tv_chemical_saturated_vapor_pressure);
        mTvCombustionProduct = findViewById(R.id.tv_chemical_combustion_product);
        mTvPolymerizationHazard = findViewById(R.id.tv_chemical_polymerization_hazard);
        mTvDangerFeature = findViewById(R.id.tv_chemical_danger_feature);

        mTvBreatheProtect = findViewById(R.id.tv_chemical_breathe_system_protect);
        mTvEyeProtect = findViewById(R.id.tv_chemical_eye_protect);
        mTvHandsProtect = findViewById(R.id.tv_chemical_hands_protect);
        mTvProtectClothes = findViewById(R.id.tv_chemical_protect_clothes);
        mTvAvoidContactCondition = findViewById(R.id.tv_chemical_condition_to_avoid_contact);

        mTvCriticalTemperature = findViewById(R.id.tv_chemical_critical_temperature);
        mTvCriticalPressure = findViewById(R.id.tv_chemical_critical_pressure);
        mTvContactExtremeValue = findViewById(R.id.tv_chemical_contact_extreme_value);
        mTvSmallLeakageRadius = findViewById(R.id.tv_chemical_small_leakage_barrier_radius);
        mTvSmallLeakageDaytime = findViewById(R.id.tv_chemical_small_leakage_daytime);
        mTvSmallLeakageNight = findViewById(R.id.tv_chemical_small_leakage_night);
        mTvLargeLeakageRadius = findViewById(R.id.tv_chemical_large_amount_leakage_isolation_radius);
        mTvLargeLeakageDaytime = findViewById(R.id.tv_chemical_large_leakage_daytime);
        mTvLargeLeakageNight = findViewById(R.id.tv_chemical_large_leakage_night);
        mTvLowerExplosionLimit = findViewById(R.id.tv_chemical_lower_explosion_limit);
        mTvUpperExplosionLimit = findViewById(R.id.tv_chemical_upper_explosion_limit);

        mTvMainUsage = findViewById(R.id.tv_chemical_main_usage);
        mTvFirePutMethod = findViewById(R.id.tv_chemical_put_fire_method);
        mTvLeakageDisposal = findViewById(R.id.tv_chemical_leakage_disposal);
        mTvStorageTransportationConsiderations = findViewById(R.id.tv_chemical_storage_transportation_considerations);

        mTvForbidden = findViewById(R.id.tv_chemical_forbidden);
        mTvBreatheIn = findViewById(R.id.tv_chemical_breathe_in);
        mTvIngestion = findViewById(R.id.tv_chemical_ingestion);
        mTvInvasionRoute = findViewById(R.id.tv_chemical_invasion_route);
        mTvHealtHazard = findViewById(R.id.tv_chemical_health_hazard);
        mTvEngineerControl = findViewById(R.id.tv_chemical_engineering_control);
        mTvSkinContact = findViewById(R.id.tv_chemical_skin_contact);
        mTvEyeContact = findViewById(R.id.tv_chemical_eye_contect);
        mTvOther = findViewById(R.id.tv_chemical_other);

    }

    private void initData() {
        chemicalId = getIntent().getStringExtra("ChemicalId");

        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.getChemicalDetail + chemicalId, RequestMethod.GET);

        NoHttpManager.getRequestInstance().add(ChemicalDetailActivity.this, 1001, request, new HttpListener<String>() {
            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                ChemicalDetail tempInfo = null;
                if (result != null) {
                    Gson gson = new Gson();
                    try {
                        ResponseBean responseBean = new ResponseBean(result);
                        if (responseBean.isSuccess()) {
                            tempInfo = gson.fromJson(responseBean.getData(), ChemicalDetail.class);

                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_CHEMICAL_DETAIL_SUCCESS;
                            msg.obj = tempInfo;
                            mHandler.sendMessage(msg);
                        } else {
                            Message msg = mHandler.obtainMessage();
                            msg.what = GET_CHEMICAL_DETAIL_FAIL;
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, long networkMillis) {
                Message msg = mHandler.obtainMessage();
                msg.what = GET_CHEMICAL_DETAIL_FAIL;
                mHandler.sendMessage(msg);
            }
        }, false, false);

    }

    private void setData(ChemicalDetail info) {

        mTvChineseName.setText(info.getCname() != null ? info.getCname() : "");
        mTvEnglishName.setText(info.getEname() != null ? info.getEname() : "");
        mTvImdg.setText(info.getImdg() != null ? info.getImdg() : "");
        mTvCas.setText(info.getCas() != null ? info.getCas() : "");
        mTvRtecs.setText(info.getRtecs() != null ? info.getRtecs() : "");
        mTvUn.setText(info.getUn() != null ? info.getUn() : "");
        mTvDangerCode.setText(info.getDcno() != null ? info.getDcno() : "");
        mTvMolecularFormula.setText(info.getChemname() != null ? info.getChemname() : "");
        mTvMolecularWeight.setText(info.getChemheight() != null ? info.getChemheight() : "");
        mTvMeltPoint.setText(info.getMeltingpoint() != null ? info.getMeltingpoint() : "");
        mTvBoilPoint.setText(info.getBoilingpoint() != null ? info.getBoilingpoint() : "");
        mTvFlashPoint.setText(info.getFlashpoint() != null ? info.getFlashpoint() : "");
        mTvDangerType.setText(info.getDangertype() != null ? info.getDangertype() : "");
        mTvDangerPackageFlag.setText(info.getDangertag() != null ? info.getDangertag() : "");
        mTvPackageType.setText(info.getPackagetype() != null ? info.getPackagetype() : "");
        mTvFireRange.setText(info.getFirelevel() != null ? info.getFirelevel() : "");

        mTvAppearance.setText(info.getShape() != null ? info.getShape() : "");
        mTvRelDensityWater.setText(info.getDensitywater() != null ? info.getDensitywater() : "");
        mTvRelDensityAir.setText(info.getDensityair() != null ? info.getDensityair() : "");
        mTvStability.setText(info.getStability() != null ? info.getStability() : "");
        mTvSolubility.setText(info.getResolvable() != null ? info.getResolvable() : "");
        mTvFlammability.setText(info.getCombustible() != null ? info.getCombustible() : "");
        mTvToxicity.setText(info.getToxicity() != null ? info.getToxicity() : "");
        mTvAutoIgnitionTemperature.setText(info.getFiringpoint() != null ? info.getFiringpoint() : "");
        mTvCombustionHeat.setText(info.getFireheat() != null ? info.getFireheat() : "");
        mTvSaturatedVaporPressure.setText(info.getStreampressure() != null ? info.getStreampressure() : "");
        mTvCombustionProduct.setText(info.getFireresult() != null ? info.getFireresult() : "");
        mTvPolymerizationHazard.setText(info.getAggregationdanger() != null ? info.getAggregationdanger() : "");
        mTvDangerFeature.setText(info.getDangerspeciality() != null ? info.getDangerspeciality() : "");

        mTvBreatheProtect.setText(info.getBreathsystemprotect() != null ? info.getBreathsystemprotect() : "");
        mTvEyeProtect.setText(info.getEyeprotect() != null ? info.getEyeprotect() : "");
        mTvHandsProtect.setText(info.getHandprotect() != null ? info.getHandprotect() : "");
        mTvProtectClothes.setText(info.getExposuresuit() != null ? info.getExposuresuit() : "");
        mTvAvoidContactCondition.setText(info.getVoidtouch() != null ? info.getVoidtouch() : "");

        mTvCriticalTemperature.setText(info.getCriticaltemperature() != null ? info.getCriticaltemperature() : "");
        mTvCriticalPressure.setText(info.getCriticalpressure() != null ? info.getCriticalpressure() : "");
        mTvContactExtremeValue.setText(info.getTouchlimitvalue() != null ? info.getTouchlimitvalue() : "");
        mTvSmallLeakageRadius.setText(info.getFewleakinsulateradius() != null ? info.getFewleakinsulateradius() : "");
        mTvSmallLeakageDaytime.setText(info.getFewleakevacuateradius1() != null ? info.getFewleakevacuateradius1() : "");
        mTvSmallLeakageNight.setText(info.getFewleakevacuateradius2() != null ? info.getFewleakevacuateradius2() : "");
        mTvLargeLeakageRadius.setText(info.getMassleakinsulateradius() != null ? info.getMassleakinsulateradius() : "");
        mTvLargeLeakageDaytime.setText(info.getMassleakevacuateradius1() != null ? info.getMassleakevacuateradius1() : "");
        mTvLargeLeakageNight.setText(info.getMassleakevacuateradius2() != null ? info.getMassleakevacuateradius2() : "");
        mTvLowerExplosionLimit.setText(info.getMinexplode() != null ? info.getMinexplode() : "");
        mTvUpperExplosionLimit.setText(info.getMaxexplode() != null ? info.getMaxexplode() : "");

        mTvMainUsage.setText(info.getMainfunc() != null ? info.getMainfunc() : "");
        mTvFirePutMethod.setText(info.getExtinguishfire() != null ? info.getExtinguishfire() : "");
        mTvLeakageDisposal.setText(info.getLeaktreatment() != null ? info.getLeaktreatment() : "");
        mTvStorageTransportationConsiderations.setText(info.getDepositinfo() != null ? info.getDepositinfo() : "");

        mTvForbidden.setText(info.getTaboo() != null ? info.getTaboo() : "");
        mTvBreatheIn.setText(info.getInhale() != null ? info.getInhale() : "");
        mTvIngestion.setText(info.getEat() != null ? info.getEat() : "");
        mTvInvasionRoute.setText(info.getCorradepath() != null ? info.getCorradepath() : "");
        mTvHealtHazard.setText(info.getHarmhealth() != null ? info.getHarmhealth() : "");
        mTvEngineerControl.setText(info.getEngineeringcontrol() != null ? info.getEngineeringcontrol() : "");
        mTvSkinContact.setText(info.getSkintouch() != null ? info.getSkintouch() : "");
        mTvEyeContact.setText(info.getEyetouch() != null ? info.getEyetouch() : "");
        mTvOther.setText(info.getOtherinfo() != null ? info.getOtherinfo() : "");

    }

    /**
     * 设置标签背景颜色
     *
     * @param textView
     */
    private void setLabelsBackgroundColor(TextView textView) {
        for (TextView view : mLabels) {
            if (view == textView) {
                view.setTextColor(getResources().getColor(R.color.red));
                view.setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                view.setTextColor(getResources().getColor(R.color.black));
                view.setBackgroundColor(getResources().getColor(R.color.dark_gray));
            }
        }
    }

    private void setDetailViewVisible(View tempView) {
        for (View view : detailViews) {
            if (view == tempView) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (R.id.tv_chemical_basic_info == vId) {           //基本信息
            setLabelsBackgroundColor(mTvBasic);
            setDetailViewVisible(mLayoutBasic);
//                mContentView.removeAllViews();
//                mLayoutBasic.setVisibility(View.VISIBLE);
//                mContentView.addView(mLayoutBasic);
        } else if (R.id.tv_chemical_feature == vId) {       //特性
            setLabelsBackgroundColor(mTvFeature);
//                mLayoutFeature.setVisibility(View.VISIBLE);
            setDetailViewVisible(mLayoutFeature);
        } else if (R.id.tv_chemical_protect == vId) {       //防护
            setLabelsBackgroundColor(mTvProtect);
//                mLayoutProtect.setVisibility(View.VISIBLE);
            setDetailViewVisible(mLayoutProtect);
        } else if (R.id.tv_chemical_depreciation == vId) {  //阖值
            setLabelsBackgroundColor(mTvDepreciation);
//                mLayoutDepreciation.setVisibility(View.VISIBLE);
            setDetailViewVisible(mLayoutDepreciation);
        } else if (R.id.tv_chemical_precautions == vId) {   //注意事项
            setLabelsBackgroundColor(mTvPrecautions);
//                mLayoutPrecautions.setVisibility(View.VISIBLE);
            setDetailViewVisible(mLayoutPrecautions);
        } else if (R.id.tv_chemical_emergency == vId) {     //应急措施
            setLabelsBackgroundColor(mTvEmergency);
//                mLayoutEmergency.setVisibility(View.VISIBLE);
            setDetailViewVisible(mLayoutEmergency);
        } else {
        }
    }
}
