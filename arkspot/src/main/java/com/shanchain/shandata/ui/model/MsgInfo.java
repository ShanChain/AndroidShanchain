package com.shanchain.shandata.ui.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by zhoujian on 2017/9/11.
 */

public class MsgInfo  implements MultiItemEntity{

    public static final int MSG_TEXT_SEND = 1;
    public static final int MSG_TEXT_RECEIVE = 2;


    private int itemType;

//    private EMMessage mEMMessage;

    @Override
    public int getItemType() {
        return 0;
    }

    public void setItemType(int type) {
        this.itemType = type;
    }

//    public EMMessage getEMMessage() {
//        return mEMMessage;
//    }

//    public void setEMMessage(EMMessage EMMessage) {
//        mEMMessage = EMMessage;
//    }
}
