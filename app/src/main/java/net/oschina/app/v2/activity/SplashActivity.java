package net.oschina.app.v2.activity;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.Version;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.testin.agent.TestinAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class SplashActivity extends ActionBarActivity {


	private static final String KEY_SPLASH_IS_HAVE_SHOWED = "splash_is_have_showed";
	private static final String SPLASH_SCREEN = "SplashScreen";
	public static final int MAX_WATTING_TIME = 3000;// 停留时间3秒
	protected Version mVersion;
	protected boolean mShouldGoTo = true;

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(SPLASH_SCREEN); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
		
		if (!mShouldGoTo) {
			redirectTo();
		}
	}
	

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(SPLASH_SCREEN); // 保证 onPageEnd 在onPause
		MobclickAgent.onPause(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (TDevice.isServiceRunning(this,
				"net.oschina.app.v2.service.NoticeService")) {
			redirectTo();
			return;
		}


		AppContext.requestDailyEnglish();
		checkUpdate();
		final View view = View.inflate(this, R.layout.a_guide_main_pageone, null);
		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2500);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				// if (mShouldGoTo) {
				// redirectTo();
				// }
				if (AppContext.instance().isLogin()) {
					login();
				} else {

					Intent intent = null;

					if (AppContext.getPersistPreferences().getBoolean(
							KEY_SPLASH_IS_HAVE_SHOWED, false)) {
						intent = new Intent(SplashActivity.this,
								MainActivity.class);

					} else {
						intent = new Intent(SplashActivity.this,
								GuideMainActivity.class);

						Editor editor = AppContext.getPersistPreferences()
								.edit();
						editor.putBoolean(KEY_SPLASH_IS_HAVE_SHOWED, true);
						editor.commit();
					}
					startActivity(intent);
					finish();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		// redirectTo();
		// 兼容低版本cookie（1.5版本以下，包括1.5.0,1.5.1）
		AppContext appContext = (AppContext) getApplication();
		String cookie = appContext.getProperty("cookie");
		if (StringUtils.isEmpty(cookie)) {
			String cookie_name = appContext.getProperty("cookie_name");
			String cookie_value = appContext.getProperty("cookie_value");
			if (!StringUtils.isEmpty(cookie_name)
					&& !StringUtils.isEmpty(cookie_value)) {
				cookie = cookie_name + "=" + cookie_value;
				appContext.setProperty("cookie", cookie);
				appContext.removeProperty("cookie_domain", "cookie_name",
						"cookie_value", "cookie_version", "cookie_path");
			}
		}
		
	}

	private void checkUpdate() {
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					// mVersion = new Version(updateInfo);
					mShouldGoTo = false;
					UmengUpdateAgent.showUpdateDialog(getApplicationContext(),
							updateInfo);
					break;
				case UpdateStatus.No: // has no update
					break;
				case UpdateStatus.NoneWifi: // none wifi
					break;
				case UpdateStatus.Timeout: // time out
					break;
				}
			}
		});
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.update(getApplicationContext());
	}

	private void login(){
		User user=AppContext.instance().getLoginInfo();
		NewsApi.loginUser(AppContext.mClientId, user.getPhone(),user.getPassword(),handler);
	}
	
	JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			redirectTo();
		}
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
			if(response.getInt("code")==88)
			{
				JSONObject userinfo = new JSONObject(response.getString("data"));
			    User user;
			    try {
				user = User.parse(userinfo);
				
				// 保存登录信息
				AppContext.instance().saveLoginInfo(user);
				//hideWaitDialog();
				redirectTo();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AppException e) {
				e.printStackTrace();
			}
		   }else
		   {
			   AppContext.instance().cleanLoginInfo();
			   redirectTo();
		   }
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};
	
	private void redirectTo() {

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}