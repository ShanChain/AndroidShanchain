package com.shanchain.data.common.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by flyye on 2017/11/23.
 */

public class PushFilterBuilder {

    JSONObject mFilterJson = new JSONObject();
    JSONArray  filterArray = new JSONArray();


    public PushFilterBuilder(){

    }

    public PushFilterBuilder addAndFiter(JSONArray array){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("and",array);
        filterArray.add(jsonObject);
        return  this;
    }

    public PushFilterBuilder addOrFilter(JSONArray array){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("or",array);
        filterArray.add(jsonObject);
        return  this;
    }

    public String getFilter(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("and",filterArray);
        mFilterJson.put("where",jsonObject);
        if(filterArray.size() > 0){
            return mFilterJson.toJSONString();
        }
        return "";
    }

}
