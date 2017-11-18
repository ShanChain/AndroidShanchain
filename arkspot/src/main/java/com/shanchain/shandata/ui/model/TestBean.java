package com.shanchain.shandata.ui.model;

import java.util.List;

/**
 * Created by zhoujian on 2017/10/17.
 */

public class TestBean {


    /**
     * code : 000000
     * data : [{"chain":{"count":4,"detailIds":["n1"]},"detailId":"n5"},{"chain":"","detailId":"n4"},{"chain":"","detailId":"n3"},{"chain":"","detailId":"n2"},{"chain":{"count":2,"detailIds":["s31"]},"detailId":"s35"},{"chain":{"count":2,"detailIds":["s31"]},"detailId":"s36"},{"chain":{"count":3,"detailIds":["s31","s33"]},"detailId":"s37"},{"chain":"","detailId":"s27"},{"chain":"","detailId":"s28"},{"chain":"","detailId":"s29"},{"chain":{"count":3,"detailIds":[]},"detailId":"s31"},{"chain":"","detailId":"s32"},{"chain":{"count":3,"detailIds":["s31"]},"detailId":"s33"},{"chain":{"count":4,"detailIds":["s1"]},"detailId":"s6"},{"chain":{"count":2,"detailIds":["s1"]},"detailId":"s7"},{"chain":{"count":4,"detailIds":["s1","s5"]},"detailId":"s8"},{"chain":{"count":4,"detailIds":["s1","s5","s8"]},"detailId":"s9"},{"chain":{"count":4,"detailIds":["s1","s6"]},"detailId":"s10"},{"chain":{"count":4,"detailIds":["s1","s6","s10"]},"detailId":"s11"},{"chain":{"count":3,"detailIds":["s1","s6"]},"detailId":"s12"},{"chain":{"count":3,"detailIds":["s1","s6"]},"detailId":"s13"},{"chain":{"count":4,"detailIds":[]},"detailId":"s1"},{"chain":"[]","detailId":"t6"},{"chain":"[]","detailId":"t2"},{"chain":"[]","detailId":"t8"}]
     * message : ok
     */

    public String code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * chain : {"count":4,"detailIds":["n1"]}
         * detailId : n5
         */

        public ChainBean chain;
        public String detailId;

        public static class ChainBean {
            /**
             * count : 4
             * detailIds : ["n1"]
             */

            public int count;
            public List<String> detailIds;
        }
    }
}
