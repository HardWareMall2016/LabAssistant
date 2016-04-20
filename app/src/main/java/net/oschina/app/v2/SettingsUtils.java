package net.oschina.app.v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingsUtils {

	public static final String PREFERENCE_HUODONG = "huodong";
	public static final String PREFERENCE_ANSWER = "answer";
	public static final String PREFERENCE_CANAED = "canaed";
	public static final String PREFERENCE_ZHUIWEN = "zhuiwen";
	public static final String PREFERENCE_SYSTEM = "system";
	public static final String PREFERENCE_FANS = "fans";

	public static void writeBoolean(Context context, String key, boolean value) {
		SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = p.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean readBoolean(Context context, String key) {
		SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(context);
		return p.getBoolean(key, true);
	}

}
