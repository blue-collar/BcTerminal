package com.telewave.battlecommand.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.telewave.battlecommand.bean.UploadFileReturnBean;
import com.telewave.battlecommand.http.RxHttpUtils;
import com.telewave.battlecommand.http.SimpleRxSubscriber;
import com.telewave.battlecommand.mqtt.MqttMessageDto.ReceiveRollCallMessage;
import com.telewave.battlecommand.mqtt.MqttMessageDto.SendRollCallMessage;
import com.telewave.battlecommand.service.MyMqttService;
import com.telewave.battlecommand.view.ChatVoicePlayAnimView;
import com.telewave.battlecommand.view.RollCallVoiceRecordButton;
import com.telewave.business.twe.R;
import com.telewave.lib.base.ConstData;
import com.telewave.lib.base.util.CalendarUtils;
import com.telewave.lib.base.util.FileUtils;
import com.telewave.lib.base.util.ToastUtils;
import com.telewave.lib.widget.BaseActivity;
import com.tencent.qcloud.tim.uikit.utils.MediaPlayTool;
import com.yanzhenjie.nohttp.FileBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import java.io.File;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 点名 应答页面
 *
 * @author liwh
 * @date 2018/12/20
 */
public class RollCallAnswerActivity extends BaseActivity implements RollCallVoiceRecordButton.AudioRecordFinishListener {
    private static final String TAG = "RollCallAnswerActivity";
    private TextView rollCallTime;
    private TextView rollcallOrganName;
    private TextView rollcallJiezhiTime;
    private LinearLayout rollCallVoiceLayout;
    private FrameLayout rollcallVoicePlayContent;
    private LinearLayout sureChoiceRollCallLayout;
    private LinearLayout cancelChoiceRollCallLayout;

    private RollCallVoiceRecordButton rollCallVoiceBtn;
    private TextView rollcallVoiceTime;
    private ChatVoicePlayAnimView rollCallVoiceAnim;

    private LinearLayout checkedBtnLayout;

    //录音文件路径
    private String voiceFilePath;
    //接收点名实体类
    private ReceiveRollCallMessage receiveRollCallMessage;

    @Override
    protected void setUpViewAndData(Bundle savedInstanceState) {
        setContentView(R.layout.activity_rollcall_answer);
        receiveRollCallMessage = (ReceiveRollCallMessage) getIntent().getSerializableExtra("ReceiveRollCallMessage");
        rollCallTime = (TextView) findViewById(R.id.rollcall_time);
        rollcallOrganName = (TextView) findViewById(R.id.rollcall_organname);
        rollcallJiezhiTime = (TextView) findViewById(R.id.rollcall_jiezhi_time);
        rollCallVoiceLayout = (LinearLayout) findViewById(R.id.rollcall_voice_layout);
        rollcallVoicePlayContent = (FrameLayout) findViewById(R.id.rollcall_voice_play_content);
        sureChoiceRollCallLayout = (LinearLayout) findViewById(R.id.sure_choice_rollcall_layout);
        cancelChoiceRollCallLayout = (LinearLayout) findViewById(R.id.cancel_choice_rollcall_layout);
        rollCallVoiceBtn = (RollCallVoiceRecordButton) findViewById(R.id.rollcall_voice_button);
        rollCallVoiceBtn.setAudioRecordFinishListener(this);
        rollcallVoiceTime = (TextView) findViewById(R.id.rollcall_voice_time);
        rollCallVoiceAnim = (ChatVoicePlayAnimView) findViewById(R.id.rollcall_voice_anim);
        checkedBtnLayout = (LinearLayout) findViewById(R.id.btn_checked_layout);
        rollCallVoiceAnim.setIsSend(false);

        sureChoiceRollCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureChoiceRollCallLayout.setVisibility(View.GONE);
                cancelChoiceRollCallLayout.setVisibility(View.GONE);
                checkedBtnLayout.setVisibility(View.VISIBLE);
            }
        });
        cancelChoiceRollCallLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollCallVoiceBtn.setVisibility(View.VISIBLE);
                rollCallVoiceLayout.setVisibility(View.GONE);
                checkedBtnLayout.setVisibility(View.GONE);
                FileUtils.deleteFile(voiceFilePath);
                voiceFilePath = null;
            }
        });
        rollcallVoicePlayContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(voiceFilePath);
                if (!file.exists()) {
                    return;
                }
                MediaPlayTool instance = MediaPlayTool.getInstance();
                //如果正在播放的是当前的,就退出播放了
                if (instance.isPlaying()) {
                    instance.stop();
                    rollCallVoiceAnim.stopVoiceAnimation();
                    if (instance.getVoicePath().equals(file.getPath())) {
                        return;
                    }
                }
                rollCallVoiceAnim.setVisibility(View.VISIBLE);
                rollCallVoiceAnim.startVoiceAnimation();
                instance.playVoice(file.getPath(), false);
                instance.setOnVoicePlayCompletionListener(new MediaPlayTool.OnVoicePlayCompletionListener() {
                    @Override
                    public void OnVoicePlayCompletion() {
                        rollCallVoiceAnim.stopVoiceAnimation();
                    }
                });
            }
        });
        checkedBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SendRollCallMessage sendRollCallMessage = new SendRollCallMessage();
//                sendRollCallMessage.setBeanData(voiceFilePath, receiveRollCallMessage);
//                MyMqttService.publishMessage(sendRollCallMessage.toJson());
//                ToastUtil.showMessage(RollCallAnswerActivity.this, "应答成功");
//                finish();
                uploadRollCallVoiceAndSendMsg();
            }
        });
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rollCallTime.setText(receiveRollCallMessage.getCalltime());
        rollcallOrganName.setText(receiveRollCallMessage.getOrganname());
        rollcallJiezhiTime.setText(CalendarUtils.getQianDaoTime(receiveRollCallMessage.getCutofftime()));
    }

    @Override
    public void onAudioRecordFinish(final int second, final String filePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (second > 0) {
                    Log.e(TAG, "run: " + filePath);
                    voiceFilePath = filePath;
                    rollCallVoiceBtn.setVisibility(View.GONE);
                    rollCallVoiceLayout.setVisibility(View.VISIBLE);
                    rollcallVoiceTime.setText(second + "s");
                } else {
                    rollCallVoiceBtn.setVisibility(View.VISIBLE);
                    rollCallVoiceLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    public void uploadRollCallVoiceAndSendMsg() {
        Request<String> request = NoHttp.createStringRequest(ConstData.urlManager.upLoadFileBaseURL, RequestMethod.POST);
        FileBinary fileBinary = new FileBinary(new File(voiceFilePath));
        request.add("file", fileBinary);
        request.add("uploadContent", "rollcall");
        RxHttpUtils.request(request).doOnNext(new Action1<Response<String>>() {
            @Override
            public void call(Response<String> stringResponse) {
                Log.e(TAG, "call944: " + stringResponse.get());
                String result = stringResponse.get();
                if (result != null) {
                    Gson gson = new Gson();
                    UploadFileReturnBean uploadFileReturnBean = gson.fromJson(result, UploadFileReturnBean.class);
                    if (uploadFileReturnBean.isSuccess()) {
                        if (uploadFileReturnBean.getUploads() != null && uploadFileReturnBean.getUploads().size() > 0) {
                            String filePath = ConstData.urlManager.baseFilesURL + uploadFileReturnBean.getUploads().get(0).getPath();
                            SendRollCallMessage sendRollCallMessage = new SendRollCallMessage();
                            sendRollCallMessage.setBeanData(filePath, receiveRollCallMessage);
                            MyMqttService.publishMessage(sendRollCallMessage.toJson());
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SimpleRxSubscriber<Response<String>>() {
            @Override
            protected void dismissDialogOnError() {
            }

            @Override
            public void onCompleted() {
                ToastUtils.toastShortMessage("应答成功");
                finish();
            }

            @Override
            public void onNext(Response<String> stringResponse) {
            }
        });
    }

}