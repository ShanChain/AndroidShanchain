package com.shanchain.data.common.rn.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by flyye on 2017/9/20.
 */

public class ReactArguments {

    public static String readFromAsset(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        ByteArrayOutputStream os = null;
        InputStream is = null;
        try {
            is = assetManager.open(path);
            os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return new String(os.toByteArray());
        } catch (IOException e) {
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }

    public static WritableMap toWritableMap(String json) {
        JSONObject object = toJSONObject(json);
        if (object == null)
            return null;
        return toWritableMap(object);
    }

    public static WritableMap toWritableMap(JSONObject object) {
        if (object == null)
            return null;
        WritableMap map = Arguments.createMap();
        Iterator<String> keys = object.keys();
        try {
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = object.get(key);
                if (value instanceof Boolean) {
                    map.putBoolean(key, ((Boolean) value).booleanValue());
                } else if (value instanceof Number) {
                    if (value instanceof Integer) {
                        map.putInt(key, ((Integer) value).intValue());
                    } else {
                        map.putDouble(key, ((Number) value).doubleValue());
                    }
                } else if (value instanceof String) {
                    map.putString(key, (String) value);
                } else if (value instanceof JSONObject) {
                    map.putMap(key, toWritableMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    parseArray(map, key, (JSONArray) value);
                }
            }
            return map;
        } catch (JSONException e) {
        }
        return null;
    }

    private static JSONObject toJSONObject(String json) {
        if (TextUtils.isEmpty(json))
            return null;
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
        }
        return null;
    }

    public static Bundle toBundle(ReadableMap map) {
        if (map == null)
            return null;
        Bundle bundle = new Bundle();
        ReadableMapKeySetIterator iterator = map.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = map.getType(key);
            switch (type) {
                case Boolean:
                    bundle.putBoolean(key, map.getBoolean(key));
                    break;
                case Number:
                    bundle.putDouble(key, map.getDouble(key));
                    break;
                case String:
                    bundle.putString(key, map.getString(key));
                    break;
                case Array:
                    parseArray(bundle, key, map.getArray(key));
                    break;
                case Map:
                    bundle.putBundle(key, toBundle(map.getMap(key)));
                    break;
            }
        }
        return bundle;
    }

    public static Bundle toBundle(String json) {
        JSONObject object = toJSONObject(json);
        if (object == null)
            return null;
        return toBundle(object);
    }

    public static Bundle toBundle(JSONObject object) {
        if (object == null)
            return null;
        try {
            Bundle bundle = new Bundle();
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = object.get(key);
                if (value instanceof Boolean) {
                    bundle.putBoolean(key, ((Boolean) value).booleanValue());
                } else if (value instanceof Integer) {
                    bundle.putInt(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    bundle.putLong(key, ((Long) value).longValue());
                } else if (value instanceof Double) {
                    bundle.putDouble(key, ((Double) value).doubleValue());
                } else if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else if (value instanceof JSONObject) {
                    bundle.putBundle(key, toBundle((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    parseArray(bundle, key, (JSONArray) value);
                }
            }
            return bundle;
        } catch (JSONException e) {
        }
        return null;
    }

    private static void parseArray(Bundle bundle, String key, JSONArray array) {
        if (array == null)
            return;
        int length = array.length();
        if (length == 0) {
            bundle.putIntArray(key, new int[0]);
        }
        try {
            int type = getType(array);
            if (type == TYPE_BOOLEAN) {
                boolean[] values = new boolean[length];
                for (int i = 0; i < length; i++) {
                    values[i] = array.optBoolean(i);
                }
                bundle.putBooleanArray(key, values);
            } else if (type == TYPE_INTEGER) {
                int[] values = new int[length];
                for (int i = 0; i < length; i++) {
                    values[i] = array.optInt(i);
                }
                bundle.putIntArray(key, values);
            } else if (type == TYPE_LONG) {
                long[] values = new long[length];
                for (int i = 0; i < length; i++) {
                    values[i] = array.optLong(i);
                }
                bundle.putLongArray(key, values);
            } else if (type == TYPE_DOUBLE) {
                double[] values = new double[length];
                for (int i = 0; i < length; i++) {
                    values[i] = array.optDouble(i);
                }
                bundle.putDoubleArray(key, values);
            } else if (type == TYPE_STRING) {
                String[] values = new String[length];
                for (int i = 0; i < length; i++) {
                    values[i] = array.optString(i);
                }
                bundle.putStringArray(key, values);
            } else if (type == TYPE_OBJECT) {
                Bundle[] values = new Bundle[length];
                for (int i = 0; i < length; i++) {
                    values[i] = toBundle(array.optJSONObject(i));
                }
                bundle.putParcelableArray(key, values);
            }
        } catch (JSONException e) {
        }
    }


    private static void parseArray(WritableMap map, String key, JSONArray array) {
        if (array == null)
            return;
        int length = array.length();
        WritableArray writableArray = new WritableNativeArray();
        try {
            int type = getType(array);
            if (type == TYPE_BOOLEAN) {
                for (int i = 0; i < length; i++) {
                    writableArray.pushBoolean(array.optBoolean(i));
                }
            } else if (type == TYPE_INTEGER) {
                for (int i = 0; i < length; i++) {
                    writableArray.pushInt(array.optInt(i));
                }
            } else if (type == TYPE_LONG) {
                for (int i = 0; i < length; i++) {
                    writableArray.pushDouble(array.optLong(i));
                }
            } else if (type == TYPE_DOUBLE) {
                for (int i = 0; i < length; i++) {
                    writableArray.pushDouble(array.optDouble(i));
                }
            } else if (type == TYPE_STRING) {
                for (int i = 0; i < length; i++) {
                    writableArray.pushString(array.optString(i));
                }
            } else if (type == TYPE_OBJECT) {
                for (int i = 0; i < length; i++) {
                    writableArray.pushMap(toWritableMap(array.optJSONObject(i)));
                }
            }
            map.putArray(key, writableArray);
        } catch (JSONException e) {
        }
    }

    private static void parseArray(Bundle bundle, String key, ReadableArray array) {
        if (array == null)
            return;
        int length = array.size();
        if (length == 0) {
            bundle.putIntArray(key, new int[]{});
            return;
        }
        ReadableType type = array.getType(0);
        if (ReadableType.Boolean == type) {
            boolean[] values = new boolean[length];
            for (int i = 0; i < length; i++) {
                values[i] = array.getBoolean(i);
            }
            bundle.putBooleanArray(key, values);
        } else if (ReadableType.Number == type) {
            double[] values = new double[length];
            for (int i = 0; i < length; i++) {
                values[i] = array.getDouble(i);
            }
            bundle.putDoubleArray(key, values);
        } else if (ReadableType.String == type) {
            String[] values = new String[length];
            for (int i = 0; i < length; i++) {
                values[i] = array.getString(i);
            }
            bundle.putStringArray(key, values);
        } else if (ReadableType.Map == type) {
            Bundle[] values = new Bundle[length];
            for (int i = 0; i < length; i++) {
                values[i] = toBundle(array.getMap(i));
            }
            bundle.putParcelableArray(key, values);
        }
    }

    private final static int TYPE_BOOLEAN = 0;
    private final static int TYPE_INTEGER = 1;
    private final static int TYPE_LONG = 2;
    private final static int TYPE_DOUBLE = 3;
    private final static int TYPE_STRING = 4;
    private final static int TYPE_OBJECT = 5;

    private static int getType(JSONArray array) throws JSONException {
        Object child = array.get(0);
        if (child instanceof Boolean)
            return TYPE_BOOLEAN;
        else if (child instanceof Integer)
            return TYPE_INTEGER;
        else if (child instanceof Long)
            return TYPE_LONG;
        else if (child instanceof Double)
            return TYPE_DOUBLE;
        else if (child instanceof String) {
            return TYPE_STRING;
        } else if (child instanceof JSONObject) {
            return TYPE_OBJECT;
        } else {
            throw new IllegalArgumentException("unknown type");
        }
    }
}
