package com.shanchain.shandata.ui.view.activity.chat.view;

import com.shanchain.shandata.ui.model.BdContactInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/28.
 */

public interface ContactView {

    void initContactSuccess(List<BdContactInfo> focus, List<BdContactInfo> fans, List<BdContactInfo> each);

    void initGroupSuccess(List<BdContactInfo> group);


}
