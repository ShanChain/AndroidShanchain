package com.shanchain.data.common.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;

import com.shanchain.data.common.base.AppManager;
import com.shanchain.data.common.utils.encryption.DesUtils;
import com.shanchain.data.common.utils.encryption.MD5Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CommonCacheHelper {
    private static final String DES_KEY = "des_key";
    private static final String ARKSPOT_CACHE = "arkspot_cache";
    private static CommonCacheHelper mInstance;
    private LruCache<String, String> mMemoryCache;
    private Context mContext;
    private String mDesKey;

    private static final String TABLE = "DataCache";
    private static final String USER_TABLES = "user_cache_tables";

    private BaseSqlDao mBaseDao;

    public synchronized static CommonCacheHelper getInstance() {
        if (mInstance == null){
            mInstance = new CommonCacheHelper(AppManager.getInstance().getContext());
        }
        return mInstance;
    }

    public String getCache(String userId , String key){

        if (TextUtils.isEmpty(key)){
            return null;
        }
        String value =  mMemoryCache.get(userId + key);
        if ( !TextUtils.isEmpty(value)){
            try{
                JSONObject object = new JSONObject(value);
                value = object.getString("value");
            }catch (Exception e){

            }
            return value;
        }
        Map<String, String> wheres = new HashMap<String, String>();
        wheres.put("cacheKey", MD5Utils.md5(userId + key));
        CacheModel cachemodel = (CacheModel) mBaseDao.selectSingleData(getTableName(userId), wheres, CacheModel.class);
        try {
            if( cachemodel != null && mContext != null){
                String cacheValue = cachemodel.getCacheValue();
                if (!TextUtils.isEmpty(cacheValue)) {
                    value = DesUtils.decrypt(cacheValue, mDesKey);
                    JSONObject object = new JSONObject();
                        object.put("time",cachemodel.getCacheTime());
                        object.put("value",value);
                    mMemoryCache.put(userId + key,object.toString());
                    return value;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    public String getCacheAndTime(String userId , String key){

        if (TextUtils.isEmpty(key)){
            return null;
        }
        String value =  mMemoryCache.get(userId + key);
        if ( !TextUtils.isEmpty(value)){
            return value;
        }
        Map<String, String> wheres = new HashMap<String, String>();
        wheres.put("cacheKey", MD5Utils.md5(userId + key));
        CacheModel cachemodel = (CacheModel) mBaseDao.selectSingleData(getTableName(userId), wheres, CacheModel.class);
        try {
            if( cachemodel != null && mContext != null){
                String cacheValue = cachemodel.getCacheValue();
                if (!TextUtils.isEmpty(cacheValue)) {
                    value = DesUtils.decrypt(cacheValue, mDesKey);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("time",cachemodel.getCacheTime());
                        object.put("value",value);
                        value = object.toString();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mMemoryCache.put(userId + key,value);
                    return value;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

    public void setCache(String userId ,String key ,String value){
        if (TextUtils.isEmpty(key)){
                return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("time",System.currentTimeMillis());
            object.put("value",value);
            mMemoryCache.put(userId + key,object.toString());
            if (mContext != null){
                Map<String, String> items = new HashMap<String, String>();
                items.put("cacheKey", MD5Utils.md5(userId + key));
                items.put("cacheTime",String.valueOf(System.currentTimeMillis()));
                items.put("cacheValue", DesUtils.encrypt(value,mDesKey));

                boolean isOk = mBaseDao.insertOrUpdate(getTable(userId), items);
                if(!isOk){
                    //重试
                    mBaseDao.excuteSql(getCreateUserTableSql(getTableName(userId)));
                    mBaseDao.insertOrUpdate(getTable(userId), items);
                }
            }
        }catch (Exception e){
        }

    }

    public void deleteCache(String userId){
        if (!TextUtils.isEmpty(userId)){
            mBaseDao.dropTable(getTableName(userId));
        }
    }

    public String getTableName(String userId){
        if(userId.equalsIgnoreCase("0")){
            return TABLE;
        }
        return "SC_" + MD5Utils.md5(userId);
    }

    private CommonCacheHelper(Context context){
        mContext = context;
        if (mMemoryCache == null){
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, String>(cacheSize) {
                @Override
                protected int sizeOf(String key, String string) {
                    return string.length() / 1024;
                }
            };
        }
        if (mBaseDao == null){
            mBaseDao = BaseSqlDao.getInstance();        }
        mDesKey = SharedPreferencesUtils.getString(mContext,ARKSPOT_CACHE,DES_KEY,"");
        if (TextUtils.isEmpty(mDesKey)){
            mDesKey = DesUtils.generateDesKey();
            SharedPreferencesUtils.putString(mContext,ARKSPOT_CACHE,DES_KEY,mDesKey);
        }
    }

    private String getTable(String userId){
        String mTableName = TABLE;
        if (userId != null && !userId.equals("0")){
            mTableName = getTableName(userId);
            String tables = SharedPreferencesUtils.getString(mContext,ARKSPOT_CACHE,USER_TABLES,"");
            String [] table = tables.split(",");
            StringBuffer buffer = new StringBuffer();
            buffer.append(mTableName+",");
            boolean exit = false;
            for (int i = 0;i < table.length;i++ ){
                String tableName = table[i];
                if (tableName.equals(mTableName)){
                    exit = true;
                }else {
                    buffer.append(tableName+",");
                }
            }
            if (!exit){
                mBaseDao.excuteSql(getCreateUserTableSql(mTableName));
            }
            SharedPreferencesUtils.putString(mContext,ARKSPOT_CACHE,USER_TABLES,buffer.toString());
        }
        return mTableName;
    }

    private String getCreateUserTableSql(String tableName){
        return  "CREATE TABLE IF NOT EXISTS "+ tableName + "("
                + "cacheKey text , "
                + "cacheValue text , "
                + "cacheTime text "
                + ")";
    }
}
