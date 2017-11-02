package com.shanchain.arkspot.ui.view.activity.chat.view;

import com.shanchain.arkspot.ui.model.BdContactInfo;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/28.
 */

public interface ContactView {

    void initContactSuccess(List<BdContactInfo> focus, List<BdContactInfo> fans, List<BdContactInfo> each);

    void initGroupSuccess(List<BdContactInfo> group);


}
