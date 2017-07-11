package net.oschina.app.v2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import net.oschina.app.v2.AppContext;

public class ShareUtil {
    private static final String PREFERENCE_NAME = "setting_sp";

    //悬赏
    public static final String IS_REWARD = "isreward";
    //未解决
    public static final String IS_SOLVEED = "issolveed";
    //分类id
    public static final String MAIN_FILTER_ID = "MainFilterId";
    //分类str
    public static final String MAIN_FILTER_STR = "MainFilterStr";
    //子类IDs
    public static final String SELECTED_CAT_IDS = "SelectedCatIds";
    //子类Strs
    public static final String SELECTED_CAT_STRS = "SelectedCatStrs";

    //boolean 开关
    public static final String VALUE_TURN_OFF ="0";
    public static final String VALUE_TURN_ON ="1";

    //原积分
    //public static final String ORIGINAL_INTEGRAL = "originalIntegral";


    private static SharedPreferences getInstance() {
        return AppContext.instance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void setValue(String key, String value) {
        SharedPreferences.Editor edit = getInstance().edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getStringValue(String key) {
        return getInstance().getString(key, null);
    }

    public static String getStringValue(String key,String def) {
        return getInstance().getString(key, def);
    }

    public static int getIntValue(String key,int def) {
        return getInstance().getInt(key, def);
    }

    public static void setIntValue(String key,int value) {
        SharedPreferences.Editor edit = getInstance().edit();
        edit.putInt(key, value);
        edit.commit();
    }
}
