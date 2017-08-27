package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 专门存放和获取SharePreference数据的工具类, 保存和配置一些设置信息
 * 
 */
public class PrefUtils {

	private static final String SHARE_PREFS_NAME = "config";

	/**
	 *  2017/5/16
	 *  描述：存放布尔类型的数据到sp中
	 *  
	 */
	public static void putBoolean(Context ctx, String key, boolean value) {
		SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
				Context.MODE_PRIVATE);

		pref.edit().putBoolean(key, value).commit();
	}

	/**
	 *  2017/5/16
	 *  描述：取出sp中布尔类型的数据
	 *  
	 */
	public static boolean getBoolean(Context ctx, String key,
			boolean defaultValue) {
		SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
				Context.MODE_PRIVATE);

		return pref.getBoolean(key, defaultValue);
	}

	/**
	 *  2017/5/16
	 *  描述：存放string类型的数据到sp中
	 *  
	 */
	public static void putString(Context ctx, String key, String value) {
		SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
				Context.MODE_PRIVATE);

		pref.edit().putString(key, value).commit();
	}

	/**
	 *  2017/5/16
	 *  描述：取出sp中string类型的数据
	 *  
	 */
	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
				Context.MODE_PRIVATE);

		return pref.getString(key, defaultValue);
	}

	/**
	 *  2017/5/16
	 *  描述：存放int类型的数据到sp中
	 *  
	 */
	public static void putInt(Context ctx, String key, int value) {
		SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
				Context.MODE_PRIVATE);

		pref.edit().putInt(key, value).commit();
	}

	/**
	 *  2017/5/16
	 *  描述：取出sp中int类型的数据
	 *  
	 */
	public static int getInt(Context ctx, String key, int defaultValue) {
		SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
				Context.MODE_PRIVATE);

		return pref.getInt(key, defaultValue);
	}

}
