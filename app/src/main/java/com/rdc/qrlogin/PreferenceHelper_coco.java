package com.rdc.qrlogin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper_coco {

	Context context ;

	public static void SavePreferences(Activity activity, String key,
                                       String value) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	//Save Boolean
	public static void SavePreferencesBoolean(Activity activity, String key,
                                              boolean value) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void deletePreferences(Activity activity, String key) {

		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public static void clearAllPreferences(Activity activity) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	public static String getPreferences(Activity activity, String key) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		String savedPref = sharedPreferences.getString(key, null);
		// Log.i(TAG, savedPref);
		return savedPref;
	}

	public static String getPreferencesWithDefault(Activity activity, String key, String defaultval) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		String savedPref = sharedPreferences.getString(key, defaultval);
		// Log.i(TAG, savedPref);
		return savedPref;
	}
	public static String getPreferenceswithdefault(Activity activity, String key, String strdefault) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		String savedPref = sharedPreferences.getString(key, strdefault);
		// Log.i(TAG, savedPref);
		return savedPref;
	}
	public static boolean getPreferencesBoolean(Activity activity, String key) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		boolean savedPref = sharedPreferences.getBoolean(key, false);
		// Log.i(TAG, savedPref);
		return savedPref;
	}
/*	public static boolean getPreferencesBooleanWithDefault(Activity activity, String key,boolean defaultval) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences(
				"choco", Context.MODE_PRIVATE);
		boolean savedPref = sharedPreferences.getBoolean(key, defaultval);
		// Log.i(TAG, savedPref);
		return savedPref;
	}*/
}
