package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.TIMLocationElem;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

import java.util.Locale;

/**
 * 位置类型消息处理
 *
 * @author PF-NAN
 * @date 2019-07-26
 */
public class MessageLocationHolder extends MessageContentHolder {

    private TextView tv_address;
    private ImageView iv_addressImage;

    public MessageLocationHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void layoutVariableViews(final MessageInfo msg, final int position) {
        msgContentFrame.setBackground(null);
        TIMLocationElem element = (TIMLocationElem) msg.getTIMMessage().getElement(0);
        tv_address.setText(String.format(Locale.CHINA, "[%s]", element.getDesc()));
        iv_addressImage.setImageResource(R.drawable.icon_location);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_location;
    }

    @Override
    public void initVariableViews() {
        tv_address = rootView.findViewById(R.id.tv_address);
        iv_addressImage = rootView.findViewById(R.id.iv_addressImage);
    }
}
