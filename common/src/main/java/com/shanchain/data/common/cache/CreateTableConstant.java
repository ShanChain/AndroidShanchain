package com.shanchain.data.common.cache;


public class CreateTableConstant {
    public final static String[] INIT_TABLE = {
            CreateTableConstant.CREATE_CACHE_SQL};

    public final static String CREATE_CACHE_SQL = "CREATE TABLE IF NOT EXISTS DataCache ("
            + "cacheKey text , "
            + "cacheValue text , "
            + "cacheTime text "
            + ")";
}
