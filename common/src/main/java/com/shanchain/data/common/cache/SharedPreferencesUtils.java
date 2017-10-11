package com.shanchain.data.common.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreferencesUtils {

	public static final String SP_ARKSPOT = "SP_ARKSPOT";


	public static void setPreferences(Context context,String key,boolean value){
		if(context == null) return;
		SharedPreferences sharedPreferences = context.getSharedPreferences(SP_ARKSPOT, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getPreferencesBoolean(Context context,String key){
		return getPreferencesBoolean(context, key, true);
	}

	public static boolean getPreferencesBoolean(Context context,String key, boolean defaultValue) {
		if(context == null) return defaultValue;
		SharedPreferences sharedPreferences = context.getSharedPreferences(SP_ARKSPOT, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, defaultValue);
	}
	public static void setPreferences(Context context,String key,String value){
		if(context == null) return;
		SharedPreferences sharedPreferences = context.getSharedPreferences(SP_ARKSPOT, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public static String getPreferences(Context context,String key){
		if(context == null) return "";
		SharedPreferences sharedPreferences = context.getSharedPreferences(SP_ARKSPOT, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}
	public static SharedPreferences getSharedPreferences(Context context, String fileName) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer;
	}

	public static Editor getEditor(Context context, String fileName) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.edit();
	}

	public static String getString(Context context, String fileName, String name, String defaultValue) {
		if(context == null) return "";
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getString(name, defaultValue);
	}

	public static void putString(Context context, String fileName, String name, String value) {
		if(context == null) return;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putString(name, value).commit();
	}

	public static boolean getBoolean(Context context, String fileName, String name, boolean defaultValue) {
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getBoolean(name, defaultValue);
	}

	public static void putBoolean(Context context, String fileName, String name, boolean value) {
		if(context == null) return;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putBoolean(name, value).commit();
	}

	public static long getLong(Context context, String fileName, String name, long defaultValue) {
		if(context == null) return -1;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getLong(name, defaultValue);
	}

	public static void putLong(Context context, String fileName, String name, long value) {
		if(context == null) return;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putLong(name, value).commit();
	}

	public static int getInt(Context context, String fileName, String name, int defaultValue) {
		if(context == null) return -1;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		return prefer.getInt(name, defaultValue);
	}

	public static void putInt(Context context, String fileName, String name, int value) {
		if(context == null) return;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().putInt(name, value).commit();
	}

	public static void remove(Context context, String fileName, String name) {
		if(context == null) return;
		SharedPreferences prefer = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		prefer.edit().remove(name).commit();
	}

	public static String getString(SharedPreferences prefer, String name, String defaultString) {
		return prefer.getString(name, defaultString);
	}


}
