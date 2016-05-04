package net.oschina.app.v2.activity;

import java.io.IOException;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.tweet.fragment.TweetViewPagerFragment;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.base.Constants;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentList;
import net.oschina.app.v2.model.MessageNum;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.service.NoticeUtils;
import net.oschina.app.v2.ui.BadgeView;
import net.oschina.app.v2.utils.TLog;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.internal.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 应用主界面
 * 
 * @author tonlin
 * @since 2014/08
 */

public class MainActivity_bak extends BaseActivity implements OnTabChangeListener,
		OnItemClickListener, OnClickListener {
	private static final String MAIN_SCREEN = "MainScreen";
	public static FragmentTabHost mTabHost;
	private ListPopupWindow mMenuWindow;
	private BadgeView mBvTweet;
	private int defualtPage = 0;
	private boolean isRunning = true;

	private View mBvNotice1, mBvNotice2;
	LinearLayout bar_ask;
	LinearLayout bar_home, bar_find, bar_me, actionbar_login;
	private EditText et_content;

	private BroadcastReceiver mNoticeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int atmeCount = intent.getIntExtra("atmeCount", 0);// @我
			int msgCount = intent.getIntExtra("msgCount", 0);// 留言
			int reviewCount = intent.getIntExtra("reviewCount", 0);// 评论
			int newFansCount = intent.getIntExtra("newFansCount", 0);// 新粉丝
			int activeCount = atmeCount + reviewCount + msgCount;// +
			TLog.log("@me:" + atmeCount + " msg:" + msgCount + " review:"
					+ reviewCount + " newFans:" + newFansCount + " active:"
					+ activeCount);
			if (activeCount > 0) {
				mBvTweet.setText(activeCount + "");
				mBvTweet.show();
			} else {
				mBvTweet.hide();
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.v2_activity_main_bak;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(MAIN_SCREEN);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(MAIN_SCREEN);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle bundle = intent.getExtras();
		dispatchMessage(bundle);
	}

	private void dispatchMessage(Bundle bundle) {
		if (null != bundle) {
			String id = bundle.getString("id");
			String type = bundle.getString("type");
			if ("login".equals(type)) {
				mTabHost.setCurrentTab(0);
			} else if ("answer".equals(type)) {
				mTabHost.setCurrentTab(0); // 积分商城链接跳转
			} else if ("daily".equals(type)) { // 助手日报-某篇文章
				UIHelper.showDaily(this);
			} else if ("weekly".equals(type)) { // 实验周刊-某篇文章
				UIHelper.showWeekly(this);
			} else if ("documents".equals(type)) { // 法规文献-某篇文章
				UIHelper.showReferences(this);
			} else if ("party".equals(type)) { // 活动中心-某篇文章
				UIHelper.showActivityCenter(this);
			} else if ("train".equals(type)) { // 培训信息-某篇文章
				UIHelper.showTrain(this);
			} else if ("brands".equals(type)) { // 品牌库-某篇文章
				UIHelper.showBrand(this);
			} else if ("product".equals(type)) { // 积分商城
				startActivity(new Intent(this, MallActivity.class));
			} else if ("realname".equals(type)) { // 实名认证
				mTabHost.setCurrentTab(3);
			} else if ("giveintegral".equals(type)) { // 赠送积分、举报
				startActivity(new Intent(this, MallActivity.class));
			} else if ("exchange".equals(type)) { // 礼品兑换
				startActivity(new Intent(this, MallActivity.class));
			} else if ("addanswer".equals(type)) {
				Comment comment = new Comment();
				comment.setId(Integer.valueOf(id));
				comment.setZhuiWen(true);
				handleReplyComment(comment);
			} else if ("addanswerafter".equals(type)) {
				Comment comment = new Comment();
				comment.setId(Integer.valueOf(id));
				comment.setZhuiWen(false);
				handleReplyComment(comment);
			} else if ("adopt".equals(type)) {
				UIHelper.xitongxiaoxi(this);
			} else if ("assistantinfo".equals(type)) {
				UIHelper.myZhuShouId(this);
			} else if ("upgrade".equals(type)) {
				startActivity(new Intent(this, MallActivity.class));
			} else if ("questionadd".equals(type)) {
				mTabHost.setCurrentTab(2);
			} else if ("exchange".equals(type)) {
				startActivity(new Intent(this, MallActivity.class));
			} else if ("attentionlist".equals(type)) {
				startActivity(new Intent(this, RankActivity.class));
			} else if ("registration".equals(type)) { // 培训信息-我要报名
				UIHelper.showTrain(this);
			} else if ("invitation".equals(type)) {
				UIHelper.guanzhuxiaoxi(this);
			}
		}
	}

	private void handleReplyComment(Comment comment) {
		UIHelper.showReplyCommentForResult(this, 1000, false, comment.getId(),
				CommentList.CATALOG_TWEET, comment);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		AppContext.instance().initLoginInfo();
		PushManager.getInstance().initialize(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			mTabHost.getTabWidget().setShowDividers(0);
		}
		initTabs();
		mTabHost.setCurrentTab(0);
		mTabHost.setOnTabChangedListener(this);

		IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
		registerReceiver(mNoticeReceiver, filter);

		NoticeUtils.bindToService(this);
		UIHelper.sendBroadcastForNotice(this);

		updateSystemMessage();
	}

	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = null;
		view = inflateView(R.layout.v2_actionbar_main_home);
		bar_home = (LinearLayout) view.findViewById(R.id.actionbar_home);
		bar_ask = (LinearLayout) view.findViewById(R.id.actionbar_question);
		bar_find = (LinearLayout) view.findViewById(R.id.actionbar_find);
		bar_me = (LinearLayout) view.findViewById(R.id.actionbar_me);
		actionbar_login = (LinearLayout) view
				.findViewById(R.id.actionbar_login);
		et_content = (EditText) view.findViewById(R.id.et_content);

		ImageButton searchBtn = (ImageButton) view
				.findViewById(R.id.btn_search);
		//Button shaixuanBtn = (Button) view.findViewById(R.id.btn_shaixuan);
		Button fankuiBtn = (Button) view.findViewById(R.id.btn_fankui);
		Button settingBtn = (Button) view.findViewById(R.id.btn_setting);
		searchBtn.setOnClickListener(this);
		//shaixuanBtn.setOnClickListener(this);
		fankuiBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
		et_content.setOnClickListener(this);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.et_content:
		case R.id.btn_search:
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_content.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			if (et_content == null) {

				UIHelper.showSearch(MainActivity_bak.this, "");
			} else {
				UIHelper.showSearch(MainActivity_bak.this, et_content.getText()
						.toString());
			}
			break;
		/*case R.id.btn_shaixuan:
			// UIHelper.showSearch(MainActivity.this);
			Fragment f = getFragment();
			if (f != null) {
				TweetViewPagerFragment fragment = (TweetViewPagerFragment) f;
				fragment.filterListData(v);
			}
			break;*/
		case R.id.btn_fankui:
			UIHelper.showFeedBack(MainActivity_bak.this);
			break;
		case R.id.btn_setting:
			UIHelper.showSetting(MainActivity_bak.this);
			break;
		}

	}

	private Fragment getFragment() {
		String tag = mTabHost.getCurrentTabTag();
		FragmentManager fm = getSupportFragmentManager();
		Fragment f = fm.findFragmentByTag(tag);
		return f;
	}

	@Override
	protected void onDestroy() {
		isRunning = false;
		NoticeUtils.unbindFromService(this);
		unregisterReceiver(mNoticeReceiver);
		NoticeUtils.tryToShutDown(this);
		super.onDestroy();
	}

	private void initTabs() {
		MainTab[] tabs = MainTab.values();
		final int size = tabs.length;
		for (int i = 0; i < 4; i++) {
			MainTab mainTab = tabs[i];
			TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
			View indicator = inflateView(R.layout.v2_tab_indicator);
			ImageView icon = (ImageView) indicator.findViewById(R.id.tab_icon);
			icon.setImageResource(mainTab.getResIcon());
			// icon.setBackgroundResource(mainTab.getResIcon());
			TextView title = (TextView) indicator.findViewById(R.id.tab_titile);
			title.setText(getString(mainTab.getResName()));
			tab.setIndicator(indicator);
			tab.setContent(new TabContentFactory() {
				@Override
				public View createTabContent(String tag) {
					return new View(MainActivity_bak.this);
				}
			});
			mTabHost.addTab(tab, mainTab.getClz(), null);

			if (mainTab.equals(MainTab.TWEET)) {
				mBvNotice1 = indicator.findViewById(R.id.tab_notice);
			}
			if (mainTab.equals(MainTab.ME)) {
				mBvNotice2 = indicator.findViewById(R.id.tab_notice);
			}

			/*
			 * if (mainTab.equals(MainTab.ME)) { View con =
			 * indicator.findViewById(R.id.container); mBvTweet = new
			 * BadgeView(this, con);
			 * mBvTweet.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			 * mBvTweet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			 * mBvTweet.setBackgroundResource(R.drawable.tab_notification_bg); }
			 */
		}
	}

	private void updateBadge(MessageNum message) {
		if (Integer.parseInt(message.toString()) > 0) {
			mBvNotice2.setVisibility(View.VISIBLE);
		} else {
			mBvNotice2.setVisibility(View.GONE);
		}
		if (message.getDnum() > 0) {
			mBvNotice1.setVisibility(View.VISIBLE);
		} else {
			mBvNotice1.setVisibility(View.GONE);
		}

		/*
		 * String number=message.toString(); if (mBvTweet != null) { if
		 * (Integer.parseInt(number) > 0) { mBvTweet.setText(number);
		 * mBvTweet.show(); } else { mBvTweet.setText(""); mBvTweet.hide(); } }
		 */
	}

	@Override
	public void onTabChanged(String tabId) {
		// final int size = mTabHost.getTabWidget().getTabCount();
		//
		changeActionBar(mTabHost.getCurrentTab());
		// if (mTabHost.getCurrentTab() == 0) {
		// mTabHost.setCurrentTab(defualtPage);
		// UIHelper.showTweetPub(this);
		// } else {
		// for (int i = 0; i < size; i++) {
		// View v = mTabHost.getTabWidget().getChildAt(i);
		//
		// if (i == mTabHost.getCurrentTab()) {
		// v.findViewById(R.id.tab_icon).setSelected(true);
		// v.findViewById(R.id.tab_titile).setSelected(true);
		// } else {
		// v.findViewById(R.id.tab_icon).setSelected(false);
		// v.findViewById(R.id.tab_titile).setSelected(false);
		// }
		// }
		// supportInvalidateOptionsMenu();
		// }
		// defualtPage = mTabHost.getCurrentTab();
	}

	private void changeActionBar(int tab) {

		System.out.println("表情会:"+tab);
		// View view=null;
		// view=getLayoutInflater().inflate(
		// R.layout.v2_actionbar_main_home, null);

		switch (tab) {
		case 0:
			bar_home.setVisibility(View.VISIBLE);
			bar_ask.setVisibility(View.GONE);
			bar_find.setVisibility(View.GONE);
			bar_me.setVisibility(View.GONE);
			actionbar_login.setVisibility(View.GONE);
			break;
		case 1:
			bar_home.setVisibility(View.GONE);
			bar_ask.setVisibility(View.VISIBLE);
			bar_find.setVisibility(View.GONE);
			bar_me.setVisibility(View.GONE);
			actionbar_login.setVisibility(View.GONE);
			break;
		case 2:
			bar_home.setVisibility(View.GONE);
			bar_ask.setVisibility(View.GONE);
			bar_find.setVisibility(View.VISIBLE);
			bar_me.setVisibility(View.GONE);
			actionbar_login.setVisibility(View.GONE);
			break;
		case 3:
			bar_home.setVisibility(View.GONE);
			bar_ask.setVisibility(View.GONE);
			bar_find.setVisibility(View.GONE);
			
			if (AppContext.instance().isLogin()) {
				bar_me.setVisibility(View.VISIBLE);
				actionbar_login.setVisibility(View.GONE);
			} else {
				bar_me.setVisibility(View.GONE);
				actionbar_login.setVisibility(View.VISIBLE);

			}
			
//			bar_me.setVisibility(View.GONE);
//			actionbar_login.setVisibility(View.GONE);
			break;
		case 4:
			bar_home.setVisibility(View.GONE);
			bar_ask.setVisibility(View.GONE);
			bar_find.setVisibility(View.GONE);
			if (AppContext.instance().isLogin()) {
				bar_me.setVisibility(View.VISIBLE);
				actionbar_login.setVisibility(View.GONE);
			} else {
				bar_me.setVisibility(View.GONE);
				actionbar_login.setVisibility(View.VISIBLE);

			}
			break;
		}
	}

	private long mLastExitTime;

	@Override
	public void onBackPressed() {
		if (System.currentTimeMillis() - mLastExitTime < 2000) {

			super.onBackPressed();
		} else {
			mLastExitTime = System.currentTimeMillis();
			AppContext.showToastShort(R.string.tip_click_back_again_to_exist);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PushManager.getInstance().initialize(this.getApplicationContext());
		// autoLogin();
		dispatchMessage(getIntent().getExtras());
	}

	// ////////////////////////////////////////////////////////////
	// ///// auto login
	// ///////////////////////////////////////////////////////////

	private void autoLogin() {
		if (AppContext.instance().isLogin()) {
			return;
		}

		String userName = AppContext.getPersistPreferences().getString(
				AppContext.LAST_INPUT_USER_NAME, "");
		String pwd = AppContext.getPersistPreferences().getString(
				AppContext.LAST_INPUT_PASSWORD, "");

		if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pwd)) {
			NewsApi.loginUser(AppContext.mClientId, userName, pwd,
					autoLoginHandler);
			AppContext.showToast("正在登录...");
		}
	}

	// json响应的handle
	JsonHttpResponseHandler autoLoginHandler = new JsonHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			// 请求失败则解析errorResponse，返回错误信息给用户
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				if (response.getInt("code") == 88) {
					JSONObject userinfo = new JSONObject(
							response.getString("data"));
					User user;
					try {
						user = User.parse(userinfo);

						if (null != user) {
							// 保存登录信息
							AppContext.instance().saveLoginInfo(user);
							// hideWaitDialog();
							// handleLoginSuccess();
							AppContext.showToast("登录成功");

							Fragment f = getFragment();
							if (f instanceof BaseFragment) {
								((BaseFragment) f).onUserLogin();
							}

						}

					} catch (IOException e) {
						e.printStackTrace();
					} catch (AppException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					AppContext.instance().cleanLoginInfo();
					// hideWaitDialog();
					AppContext.showToast("登录失败：" + response.getInt("code"));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	private void updateSystemMessage() {
		Thread thread = new Thread(mRunnable);
		thread.start();
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			while (isRunning) {
				try {
					// 更新消息
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							sendActiveNum();
						}
					});
					// 休眠
					Thread.sleep(60 * 1000);
					System.out.println("update notice...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private void sendActiveNum() {
		//如果未登录过,不发获取数据协议。
		if(0==AppContext.instance().getLoginUid()){
			return;
		}
		NewsApi.getActiveNum(AppContext.instance().getLoginUid(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (response.optInt("code") == 88) {
								JSONObject json = new JSONObject(response
										.optString("data"));
								MessageNum message = MessageNum.parse(json);
								// 更新内存中消息数目
								updateBadge(message);
								AppContext.instance().setMessageNum(message);
							} else {
								AppContext.showToast(response.optString("desc"));
							}
						} catch (Exception e) {
						}
					}
				});
	}
}
