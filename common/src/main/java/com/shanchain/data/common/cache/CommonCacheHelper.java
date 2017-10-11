package com.shanchain.data.common.cache;

import android.content.Context;
import android.text.TextUtils;
import android.util.LruCache;

import com.shanchain.data.common.utils.encryption.DesUtils;
import com.shanchain.data.common.utils.encryption.MD5Utils;

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

    public synchronized static CommonCacheHelper getInstance(Context context) {
        if (mInstance == null){
            mInstance = new CommonCacheHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public String getCache(String userId , String key){

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
        mMemoryCache.put(userId + key,value);
        if (mContext != null){
            Map<String, String> items = new HashMap<String, String>();
            items.put("cacheKey", MD5Utils.md5(userId + key));
            items.put("cacheTime",String.valueOf(System.currentTimeMillis()));
            items.put("cacheValue", DesUtils.encrypt(value,mDesKey));
            mBaseDao.insertOrUpdate(getTableName(userId), items);
        }
    }

    public void deleteCache(String userId){
        if (!TextUtils.isEmpty(userId)){
            String tableName = MD5Utils.md5(userId);
            mBaseDao.dropTable(tableName);
        }
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

    private String getTableName(String userId){
        String mTableName = TABLE;
        if (userId != null && !userId.equals("0")){
            mTableName = MD5Utils.md5(userId);
            String tables = SharedPreferencesUtils.getString(mContext,ARKSPOT_CACHE,USER_TABLES,"");
            String [] table = tables.split(",");
            StringBuffer buffer = new StringBuffer();
            buffer.append(mTableName+",");
            boolean exit = false;
            for (int i = 0;i < table.length && buffer.toString().split(",").length < 5 ;i++ ){
                String tableName = table[i];
                if (!tableName.equals(mTableName)){
                    exit = true;
                    buffer.append(tableName+",");
                }
                table[i] = "";
            }
            String tableLast = table[table.length-1];
            if (!tableLast.equals(mTableName) && !TextUtils.isEmpty(tableLast)){
                mBaseDao.dropTable(tableLast);
            }
            if (!exit && !tableLast.equals(mTableName)){
                mBaseDao.excuteSql(getCreateUserTableSql(mTableName));
            }
            SharedPreferencesUtils.putString(mContext,ARKSPOT_CACHE,USER_TABLES,buffer.substring(0,buffer.length()-1));
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
