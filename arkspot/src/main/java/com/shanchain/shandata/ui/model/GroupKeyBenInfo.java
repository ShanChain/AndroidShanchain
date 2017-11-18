package com.shanchain.shandata.ui.model;

import java.io.Serializable;

/**
 * Created by zhoujian on 2017/9/26.
 */

public class GroupKeyBenInfo implements Serializable{
    /**
     * groupId : 27697479745538
     * hua : {"characterId":1222,"headImg":"default img","password":"hx1505802159842","title":"default title","userName":"sc-1425704407"}
     */

    private ComUserInfo hua;

    public ComUserInfo getHua() {
        return hua;
    }

    public void setHua(ComUserInfo hua) {
        this.hua = hua;
    }
}
