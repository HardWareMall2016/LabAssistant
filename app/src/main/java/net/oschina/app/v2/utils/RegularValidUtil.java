package net.oschina.app.v2.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularValidUtil {
	// 判断手机格式是否正确
	public static boolean isMobileNO(String mobiles) {
		try {
			Pattern p = Pattern.compile("^([1][3456789][0-9]+)$");
			Matcher m = p.matcher(mobiles);
			return m.matches()
					&& (mobiles.length() == 11 );
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 判断email格式是否正确
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断email格式是否正确
	 */
	public static String getIp(String content) {
		String ret = "";
		String str = "[0-9]+.[0-9]+.[0-9]+.[0-9]+";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(content);
		if (m.find()) {
			ret = m.group();
		}
		return ret;
	}
}
