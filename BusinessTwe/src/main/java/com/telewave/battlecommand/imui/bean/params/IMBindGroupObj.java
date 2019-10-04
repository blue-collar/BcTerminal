package com.telewave.battlecommand.imui.bean.params;

/**
 * 绑定聊天室到警情参数类
 *
 * @author PF-NAN
 * @date 2019-07-27
 */
public class IMBindGroupObj {
    /*群组id*/
    public String id;
    /*群组名字*/
    public String groupname;
    /*警情id*/
    public String zquuid;
    /*群组类型 1:微站 用户，2：指挥app用户*/
    public String groupType = "2";

    public IMBindGroupObj(String groupId, String groupName, String zquuid) {
        this.id = groupId;
        this.groupname = groupName;
        this.zquuid = zquuid;
    }
}
