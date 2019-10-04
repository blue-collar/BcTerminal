package com.tencent.qcloud.tim.uikit.component.gatherimage;

import com.telewave.lib.base.util.ScreenUtils;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

public abstract class DynamicChatUserIconView extends DynamicLayoutView<MessageInfo> {

    private int iconRadius = -1;

    public int getIconRadius() {
        return iconRadius;
    }

    /**
     * 设置聊天头像圆角
     *
     * @param iconRadius
     */
    public void setIconRadius(int iconRadius) {
        this.iconRadius = ScreenUtils.getPxByDp(iconRadius);
    }
}
