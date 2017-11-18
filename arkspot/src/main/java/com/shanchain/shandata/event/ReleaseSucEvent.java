package com.shanchain.shandata.event;

/**
 * Created by zhoujian on 2017/11/17.
 */

public class ReleaseSucEvent {
    private boolean suc;

    public ReleaseSucEvent(boolean suc) {
        this.suc = suc;
    }

    public boolean isSuc() {
        return suc;
    }

    public void setSuc(boolean suc) {
        this.suc = suc;
    }
}
