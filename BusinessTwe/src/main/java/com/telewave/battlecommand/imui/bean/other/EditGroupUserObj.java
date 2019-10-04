package com.telewave.battlecommand.imui.bean.other;

import android.support.annotation.IntRange;

import com.tencent.imsdk.TIMUserProfile;


/**
 * 添加、删除
 *
 * @author PF-NAN
 * @date 2019-07-25
 */
public class EditGroupUserObj extends TIMUserProfile {
    /*0：添加，1：删除*/
    public int type = 0;

    public EditGroupUserObj(@IntRange(from = 0, to = 1) int type) {
        this.type = type;
    }
}
