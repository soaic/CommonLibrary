package com.xiaos.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {
	private static PreferencesUtil preferencesUtils = null;

	synchronized public static PreferencesUtil getInstance() {
		return preferencesUtils = preferencesUtils == null ? new PreferencesUtil()
				: preferencesUtils;
	}

	/**
	 * 保存值到SharedPreferences
	 * 
	 * @param shareName
	 * @param key
	 * @param value
	 * @param context
	 */
	public boolean savePreferences(String shareName, String key, String value,
			Context context) {
		SharedPreferences share = context.getSharedPreferences(shareName,
				Context.MODE_PRIVATE);
		return share.edit().putString(key, value).commit();
	}

	/**
	 * 从SharedPreferences获取值
	 * 
	 * @param shareName
	 * @param key
	 * @param defaultStr
	 * @param context
	 * @return
	 */
	public String getPreferences(String shareName, String key,
			String defaultStr, Context context) {
		SharedPreferences share = context.getSharedPreferences(shareName,
				Context.MODE_PRIVATE);
		return share.getString(key, defaultStr);
	}

	/**
	 * 移除SharedPreferences值
	 * 
	 * @param shareName
	 * @param key
	 * @param defaultStr
	 * @param context
	 * @return
	 */
	public boolean removePreferences(String shareName, String key,
			Context context) {
		SharedPreferences share = context.getSharedPreferences(shareName,
				Context.MODE_PRIVATE);
		return share.edit().remove(key).commit();
	}
}
