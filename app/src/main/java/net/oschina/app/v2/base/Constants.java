package net.oschina.app.v2.base;

import android.os.Environment;

public class Constants {
	public static final String INTENT_ACTION_LOGOUT = "com.tonlin.osc.happy.LOGOUT";
	public static final String INTENT_ACTION_COMMENT_CHANGED = "com.tonlin.osc.happy.COMMENT_CHANGED";
	public static final String INTENT_ACTION_NOTICE = "com.tonlin.osc.happy.action.APPWIDGET_UPDATE";

	public final static String BASE_DIR = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/HappyOSC/";
	
	public final static String IMAGE_SAVE_PAHT = BASE_DIR +"download_images";
	
//	public static final String WEICHAT_APPID = "wx7aac2075450f71a2";
//	public static final String WEICHAT_SECRET = "0101b0595ffe2042c214420fac358abc";
	
	/*public static final String WEICHAT_APPID = "wx2c828a06ea14af71";
	public static final String WEICHAT_SECRET = "06e10550a54e0500e89ac7460b933926";*/
	
	public static final String WEICHAT_APPID = "wxaea6f7ec5263cd4e";
	public static final String WEICHAT_SECRET = "3f9f860892bac13cb326640fa06355a2";
	
	//public static final String QQ_APPID = "100424468";
	//public static final String QQ_APPKEY = "c7394704798a158208a74ab60104f0ba";
	
	public static final String QQ_APPID = "1105023611";
	public static final String QQ_APPKEY = "oelQiDJsvcGnlvfw";
	
}
