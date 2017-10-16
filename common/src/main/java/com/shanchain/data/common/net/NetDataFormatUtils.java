package com.shanchain.data.common.net;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;


public class NetDataFormatUtils {


    public static WritableMap getNetErrWritableMap(String errorStr, String errorCode, String data) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("code", errorCode);
        writableMap.putString("message", errorStr);
        writableMap.putString("data", data);
        return writableMap;
    }

    public static WritableMap parseJSON2WritableMap(String jsonStr) {
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> iterator= jsonObject.keys();
            String key;
            Object value ;
            WritableMap valueMap = Arguments.createMap();
            while (iterator.hasNext()) {
                key = iterator.next();
                value = jsonObject.get(key);
                if (value instanceof JSONArray) {
                    int length = ((JSONArray)value).length();
                    WritableArray array = Arguments.createArray();
                    for(int i = 0; i < length; i++) {
                        JSONObject object = ((JSONArray)value).getJSONObject(i);
                        array.pushMap(parseJSON2WritableMap(object.toString()));
                    }
                    valueMap.putArray(key, array);

                } else if (value instanceof JSONObject) {
                    valueMap.putMap(key, parseJSON2WritableMap(value.toString()));

                } else if (value instanceof String) {
                    valueMap.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    valueMap.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    valueMap.putBoolean(key, (Boolean) value);
                } else if (value instanceof Double) {
                    valueMap.putDouble(key, (Double) value);
                } else {
                    valueMap.putString(key, value.toString());
                }
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WritableArray parseJSONArray2WritableArray(String arry) {
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(arry);
            WritableArray valueList = Arguments.createArray();
            for (int i=0; i<jsonArray.length(); i++) {
                WritableMap content = parseJSON2WritableMap(jsonArray.getJSONObject(i).toString());
                valueList.pushMap(content);
            }
            return valueList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
