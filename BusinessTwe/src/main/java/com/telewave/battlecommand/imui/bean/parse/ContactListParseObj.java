package com.telewave.battlecommand.imui.bean.parse;

import com.telewave.battlecommand.imui.bean.ContactInfoObj;
import com.telewave.battlecommand.imui.bean.IMParseBaseObj;

import java.util.List;

/**
 * 联系人解析积累
 *
 * @author PF-NAN
 * @date 2019-07-22
 */
public class ContactListParseObj extends IMParseBaseObj<List<ContactInfoObj>> {

    public List<ContactInfoObj> data1;

    @Override
    public String toString() {
        return "-------" + data.toString() + "=========" + data1.toString();
    }
}
