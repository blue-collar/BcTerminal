package com.telewave.battlecommand.directboard.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.telewave.lib.widget.BaseActivity;
import com.telewave.battlecommand.directboard.ConSpData;
import com.telewave.battlecommand.trtc.TRTCSettingDialog;
import com.telewave.business.twe.R;
import com.tencent.trtc.TRTCCloudDef;

import java.util.ArrayList;

public class LiveBoardSetActivity extends BaseActivity {
    private final static String TAG = TRTCSettingDialog.class.getSimpleName();
    private Spinner spSolution, spFps;
    private SeekBar sbVideoBitrate;
    private CheckBox cbPriorSmall;
    private RadioButton rbSmooth, rbClear;
    private TextView tvVideoBitrate, tvSubmit;
    private int curRes = 2;

    static class TRTCSettingBitrateTable {
        public int resolution;
        public int defaultBitrate;
        public int minBitrate;
        public int maxBitrate;
        public int step;

        public TRTCSettingBitrateTable(int resolution, int defaultBitrate, int minBitrate, int maxBitrate, int step) {
            this.resolution = resolution;
            this.defaultBitrate = defaultBitrate;
            this.minBitrate = minBitrate;
            this.maxBitrate = maxBitrate;
            this.step = step;
        }
    }

    private ArrayList<TRTCSettingBitrateTable> paramArray;

    final static int DEFAULT_BITRATE = 600;
    final static int DEFAULT_FPS = 15;

    private int videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
    private int videoFps = DEFAULT_FPS;
    private int videoBitrate = DEFAULT_BITRATE;
    public boolean priorSmall = false;
    private int qosPreference = TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_CLEAR;


    public int getResolution() {
        return videoResolution;
    }

    public int getQosPreference() {
        return qosPreference;
    }

    public int getVideoFps() {
        return videoFps;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }


    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_live_board_set);
        paramArray = new ArrayList<>();
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_160_160, 250, 40, 300, 10));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_180, 350, 80, 350, 10));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_320_240, 400, 100, 400, 10));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_480_480, 500, 200, 1000, 10));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360, 600, 200, 1000, 10));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_480, 700, 250, 1000, 50));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540, 900, 400, 1600, 50));
        paramArray.add(new TRTCSettingBitrateTable(TRTCCloudDef.TRTC_VIDEO_RESOLUTION_1280_720, 1250, 500, 2000, 50));

        initView();
        loadCache(this);
        updateDialogValue();
    }

    private void initView() {
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spSolution = (Spinner) findViewById(R.id.sp_solution);
        spFps = (Spinner) findViewById(R.id.sp_video_fps);
        sbVideoBitrate = (SeekBar) findViewById(R.id.sk_video_bitrate);
        cbPriorSmall = (CheckBox) findViewById(R.id.cb_prior_small);
        tvVideoBitrate = (TextView) findViewById(R.id.tv_video_bitrate);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        rbSmooth = (RadioButton) findViewById(R.id.rb_smooth);
        rbClear = (RadioButton) findViewById(R.id.rb_clear);

        ArrayAdapter<String> spinnerAadapter = new ArrayAdapter<String>(LiveBoardSetActivity.this,
                R.layout.textview_spinner, getResources().getStringArray(R.array.solution));
        spSolution.setAdapter(spinnerAadapter);
        spSolution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curRes = position;
                updateSolution(curRes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> spinnerFpsAadapter = new ArrayAdapter<String>(LiveBoardSetActivity.this,
                R.layout.textview_spinner, getResources().getStringArray(R.array.video_fps));
        spFps.setAdapter(spinnerFpsAadapter);
        spFps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvVideoBitrate.setText("" + getBitrate(sbVideoBitrate.getProgress()) + "kbps");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sbVideoBitrate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvVideoBitrate.setText("" + getBitrate(progress) + "kbps");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        tvSubmit.setClickable(true);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoResolution = getResolution(spSolution.getSelectedItemPosition());
                videoFps = getFps(spFps.getSelectedItemPosition());
                videoBitrate = getBitrate(sbVideoBitrate.getProgress());
                qosPreference = rbSmooth.isChecked() ? TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_SMOOTH : TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_CLEAR;
                priorSmall = cbPriorSmall.isChecked();

                saveCache(LiveBoardSetActivity.this);
                finish();
            }
        });

    }

    private void updateDialogValue() {
        curRes = getResolutionPos(videoResolution);
        spSolution.setSelection(curRes);
        cbPriorSmall.setChecked(priorSmall);
        updateSolution(curRes);
        spFps.setSelection(getFpsPos(videoFps));
        sbVideoBitrate.setProgress(getBitrateProgress(videoBitrate));
        tvVideoBitrate.setText("" + getBitrate(sbVideoBitrate.getProgress()) + "kbps");

        if (qosPreference == TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_SMOOTH) {
            rbSmooth.setChecked(true);
        } else {
            rbClear.setChecked(true);
        }
    }

    private int getResolutionPos(int resolution) {
        for (int i = 0; i < paramArray.size(); i++) {
            if (resolution == (paramArray.get(i).resolution)) {
                return i;
            }
        }
        return 4;
    }

    private int getResolution(int pos) {
        if (pos >= 0 && pos < paramArray.size()) {
            return paramArray.get(pos).resolution;
        }
        return TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360;
    }

    private int getFpsPos(int fps) {
        switch (fps) {
            case 15:
                return 0;
            case 20:
                return 1;
            default:
                return 0;
        }
    }

    private int getFps(int pos) {
        switch (pos) {
            case 0:
                return 15;
            case 1:
                return 20;
            default:
                return 15;
        }
    }

    private int getMinBitrate(int pos) {
        if (pos >= 0 && pos < paramArray.size()) {
            return paramArray.get(pos).minBitrate;
        }
        return 300;
    }

    private int getMaxBitrate(int pos) {
        if (pos >= 0 && pos < paramArray.size()) {
            return paramArray.get(pos).maxBitrate;
        }
        return 1000;
    }

    private int getDefBitrate(int pos) {
        if (pos >= 0 && pos < paramArray.size()) {
            return paramArray.get(pos).defaultBitrate;
        }
        return 400;
    }

    /**
     * 获取当前精度
     */
    private int getStepBitrate(int pos) {
        if (pos >= 0 && pos < paramArray.size()) {
            return paramArray.get(pos).step;
        }
        return 10;
    }

    private int getBitrateProgress(int bitrate) {
        int minBitrate = getMinBitrate(curRes);
        int stepBitrate = getStepBitrate(curRes);

        int progress = (bitrate - minBitrate) / stepBitrate;
        Log.e(TAG, "getBitrateProgress->progress: " + progress + ", min: " + minBitrate + ", stepBitrate: " + stepBitrate + "/" + bitrate);
        return progress;
    }

    private int getBitrate(int progress) {
        int minBitrate = getMinBitrate(curRes);
        int maxBitrate = getMaxBitrate(curRes);
        int stepBitrate = getStepBitrate(curRes);
        int bit = (progress * stepBitrate) + minBitrate;
        Log.e(TAG, "getBitrate->bit: " + bit + ", min: " + minBitrate + ", max: " + maxBitrate);
        return bit;
    }

    private void updateSolution(int pos) {
        int minBitrate = getMinBitrate(curRes);
        int maxBitrate = getMaxBitrate(curRes);

        int stepBitrate = getStepBitrate(curRes);
        int max = (maxBitrate - minBitrate) / stepBitrate;
        if (sbVideoBitrate.getMax() != max) {    // 有变更时设置默认值
            sbVideoBitrate.setMax(max);
            int defBitrate = getDefBitrate(curRes);
            sbVideoBitrate.setProgress(getBitrateProgress(defBitrate));
        } else {
            sbVideoBitrate.setMax(max);
        }
    }

    private void saveCache(Context context) {
        try {
            SharedPreferences shareInfo = context.getSharedPreferences(ConSpData.PER_DATA, 0);
            SharedPreferences.Editor editor = shareInfo.edit();
            editor.putInt(ConSpData.PER_RESOLUTION, videoResolution);
            editor.putInt(ConSpData.PER_VIDEOFPS, videoFps);
            editor.putInt(ConSpData.PER_VIDEOBITRATE, videoBitrate);
            editor.putInt(ConSpData.PER_QOSTYPE, qosPreference);
            editor.putBoolean(ConSpData.PER_PRIOR_SMALL, priorSmall);
            editor.commit();
        } catch (Exception e) {

        }

    }

    private void loadCache(Context context) {
        try {
            SharedPreferences shareInfo = context.getSharedPreferences(ConSpData.PER_DATA, 0);
            videoResolution = shareInfo.getInt(ConSpData.PER_RESOLUTION, TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360);
            videoFps = shareInfo.getInt(ConSpData.PER_VIDEOFPS, DEFAULT_FPS);
            videoBitrate = shareInfo.getInt(ConSpData.PER_VIDEOBITRATE, DEFAULT_BITRATE);
            qosPreference = shareInfo.getInt(ConSpData.PER_QOSTYPE, TRTCCloudDef.TRTC_VIDEO_QOS_PREFERENCE_CLEAR);
            priorSmall = shareInfo.getBoolean(ConSpData.PER_PRIOR_SMALL, false);
        } catch (Exception e) {

        }

    }

}
