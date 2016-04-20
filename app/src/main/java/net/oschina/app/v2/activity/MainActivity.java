package net.oschina.app.v2.activity;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.active.fragment.ActiveFragment;
import net.oschina.app.v2.activity.find.fragment.FindFragment;
import net.oschina.app.v2.activity.home.fragment.HomeFragment;
import net.oschina.app.v2.activity.tweet.fragment.TweetViewPagerFragment;
import net.oschina.app.v2.activity.user.fragment.LoginFragment;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.Constants;
import net.oschina.app.v2.model.MessageNum;
import net.oschina.app.v2.model.event.MessageRefreshSingle;
import net.oschina.app.v2.model.event.MessageRefreshTotal;
import net.oschina.app.v2.service.NoticeUtils;
import net.oschina.app.v2.utils.TLog;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 应用主界面
 * 
 * @author JohnnyMeng
 * @since 2015/04
 */
public class MainActivity extends BaseActivity implements OnClickListener {
	private static final String MAIN_SCREEN = "MainScreen";

	private boolean isRunning = true;

	private Fragment homeFragment, questionFragment,askFragment, findFragment, meFragment;

	/**
	 * actionBar控件
	 */
	LinearLayout bar_home, bar_question, bar_find, actionbar_login;
	RelativeLayout bar_me;
	private EditText et_content;

	private FrameLayout contentView;

	/**
	 * bottomView控件
	 */
	private View fl_home, fl_question, fl_ask;
	private View fl_find, fl_me;
	private ImageView iv_home, iv_question, iv_ask, iv_find, iv_me;
	private TextView tv_home, tv_question, tv_ask, tv_find, tv_me,iv_find_notice,iv_active_notice;

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
				// mBvTweet.setText(activeCount + "");
				// mBvTweet.show();
			} else {
				// mBvTweet.hide();
			}
		}
	};

	@Override
	protected int getLayoutId() {
		return R.layout.v2_activity_main;
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
	protected void onDestroy() {
		isRunning = false;
		NoticeUtils.unbindFromService(this);
		unregisterReceiver(mNoticeReceiver);
		NoticeUtils.tryToShutDown(this);
		refreshMessageUtil.stop();
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle bundle = intent.getExtras();
		dispatchMessage(bundle);
	}
	
	@Override
	protected void onBeforeSetContentLayout() {
		 getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
	}
	
	private void dispatchMessage(Bundle bundle) {
		if (null != bundle) {
			String id = bundle.getString("id");
			String type = bundle.getString("type");
			if ("login".equals(type)) {
				if(meFragment!=null){
					FragmentTransaction fragmentTransaction = this
							.getSupportFragmentManager().beginTransaction();
					fragmentTransaction.remove(meFragment);
					fragmentTransaction.commit();
					meFragment = null;
				}
				changeContent(R.id.layout_me);

			}else if ("reigster".equals(type)){
				changeContent(R.id.layout_me);
			}else if ("answer".equals(type)){
				changeContent(R.id.layout_question);
			}
		}
	}
	
	RefreshMessageUtil refreshMessageUtil;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);

		initView();
		changeContent(R.id.layout_question);
		
//		AppContext.instance().initLoginInfo();
		PushManager.getInstance().initialize(this.getApplicationContext());
//		PushManager.getInstance().initialize(this);

		IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_NOTICE);
		registerReceiver(mNoticeReceiver, filter);

		NoticeUtils.bindToService(this);
		UIHelper.sendBroadcastForNotice(this);

		//updateSystemMessage();
		
		EventBus.getDefault().register(this);
		refreshMessageUtil=new RefreshMessageUtil();
	//	refreshMessageUtil.start(new Handler());
	}
	
	@Override
	public void onStop(){
		super.onStop();
		refreshMessageUtil.stop();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		if(refreshMessageUtil!=null){
			refreshMessageUtil.start(new Handler());
		}
	}

	
	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = null;
		view = inflateView(R.layout.v2_actionbar_main_home);
		bar_home = (LinearLayout) view.findViewById(R.id.actionbar_home);
		bar_question = (LinearLayout) view.findViewById(R.id.actionbar_question);
		bar_find = (LinearLayout) view.findViewById(R.id.actionbar_find);
		bar_me = (RelativeLayout) view.findViewById(R.id.actionbar_me);
		actionbar_login = (LinearLayout) view
				.findViewById(R.id.actionbar_login);
		et_content = (EditText) view.findViewById(R.id.et_content);

		ImageButton searchBtn = (ImageButton) view
				.findViewById(R.id.btn_search);
		Button shaixuanBtn = (Button) view.findViewById(R.id.btn_shaixuan);
		Button fankuiBtn = (Button) view.findViewById(R.id.btn_fankui);
		Button settingBtn = (Button) view.findViewById(R.id.btn_setting);
		searchBtn.setOnClickListener(this);
		shaixuanBtn.setOnClickListener(this);
		fankuiBtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
		et_content.setOnClickListener(this);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}

	private void initView() {
		contentView = (FrameLayout) findViewById(R.id.frame_content);

		fl_home = findViewById(R.id.layout_home);
		fl_question =  findViewById(R.id.layout_question);
		fl_ask = findViewById(R.id.layout_ask);
		fl_find = findViewById(R.id.layout_find);
		fl_me =  findViewById(R.id.layout_me);

		iv_home = (ImageView) findViewById(R.id.image_home);
		iv_question = (ImageView) findViewById(R.id.image_question);
		iv_ask = (ImageView) findViewById(R.id.image_ask);
		iv_find = (ImageView) findViewById(R.id.image_find);
		iv_me = (ImageView) findViewById(R.id.image_me);

		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_question = (TextView) findViewById(R.id.tv_question);
		tv_ask = (TextView) findViewById(R.id.tv_ask);
		tv_find = (TextView) findViewById(R.id.tv_find);
		tv_me = (TextView) findViewById(R.id.tv_me);
		
		iv_find_notice = (TextView) findViewById(R.id.iv_find_notice);
		iv_active_notice = (TextView) findViewById(R.id.iv_active_notice);

		//fl_home=null;
		fl_home.setOnClickListener(this);
		fl_question.setOnClickListener(this);
		fl_ask.setOnClickListener(this);
		fl_find.setOnClickListener(this);
		fl_me.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_home:
		case R.id.layout_question:
		case R.id.layout_find:
		case R.id.layout_me:
			changeContent(v.getId());
			break;
		case R.id.layout_ask:
			UIHelper.showAskView(MainActivity.this);
			break;
		case R.id.et_content:
		case R.id.btn_search:
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et_content.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			if (et_content == null) {
				UIHelper.showSearch(MainActivity.this, "");
			} else {
				UIHelper.showSearch(MainActivity.this, et_content.getText()
						.toString());
			}
			break;
		case R.id.btn_shaixuan:
			TweetViewPagerFragment fragment = (TweetViewPagerFragment)questionFragment;
			fragment.filterListData(v);
			break;
		case R.id.btn_fankui:
			UIHelper.showFeedBack(MainActivity.this);
			break;
		case R.id.btn_setting:
			UIHelper.showSetting(MainActivity.this);
			break;
		default:
			break;
		}
	}
	
	Fragment mCurrentFragment;

	public void changeContent(int clickViewId) {
		FragmentTransaction fragmentTransaction = this
				.getSupportFragmentManager().beginTransaction();
		hideFragments(fragmentTransaction);
		
		switch (clickViewId) {
		case R.id.layout_home:
			bar_home.setVisibility(View.VISIBLE);
			bar_question.setVisibility(View.GONE);
			bar_find.setVisibility(View.GONE);
			bar_me.setVisibility(View.GONE);
			actionbar_login.setVisibility(View.GONE);
			
			if(homeFragment!=null){
				fragmentTransaction.show(homeFragment);
				
//				fragmentTransaction.remove(homeFragment);
//				homeFragment = new HomeFragment();
//				fragmentTransaction.add(R.id.frame_content, homeFragment);
			}else{
				homeFragment = new HomeFragment();
				fragmentTransaction.add(R.id.frame_content, homeFragment);
			}
			mCurrentFragment=homeFragment;
			iv_home.setSelected(true);
			tv_home.setSelected(true);	
			iv_question.setSelected(false);
			tv_question.setSelected(false);
			iv_find.setSelected(false);
			tv_find.setSelected(false);
			iv_me.setSelected(false);
			tv_me.setSelected(false);
			
			break;
		case R.id.layout_question:
			bar_home.setVisibility(View.GONE);
			bar_question.setVisibility(View.VISIBLE);
			bar_find.setVisibility(View.GONE);
			bar_me.setVisibility(View.GONE);
			actionbar_login.setVisibility(View.GONE);
			
			if(questionFragment!=null){
				fragmentTransaction.show(questionFragment);
//				fragmentTransaction.remove(questionFragment);
//				questionFragment = new TweetViewPagerFragment();
//				fragmentTransaction.add(R.id.frame_content, questionFragment);
			}else{
				questionFragment = new TweetViewPagerFragment();
				fragmentTransaction.add(R.id.frame_content, questionFragment);
			}
			mCurrentFragment=questionFragment;
			iv_home.setSelected(false);
			tv_home.setSelected(false);	
			iv_question.setSelected(true);
			tv_question.setSelected(true);
			iv_find.setSelected(false);
			tv_find.setSelected(false);
			iv_me.setSelected(false);
			tv_me.setSelected(false);
			
			break;
		case R.id.layout_find:
			bar_home.setVisibility(View.GONE);
			bar_question.setVisibility(View.GONE);
			bar_find.setVisibility(View.VISIBLE);
			bar_me.setVisibility(View.GONE);
			actionbar_login.setVisibility(View.GONE);
			
			if(findFragment!=null){
				fragmentTransaction.show(findFragment);
			}else{
				findFragment = new FindFragment();
				fragmentTransaction.add(R.id.frame_content, findFragment);
			}
			mCurrentFragment=findFragment;
			iv_home.setSelected(false);
			tv_home.setSelected(false);	
			iv_question.setSelected(false);
			tv_question.setSelected(false);
			iv_find.setSelected(true);
			tv_find.setSelected(true);
			iv_me.setSelected(false);
			tv_me.setSelected(false);
			
			break;
		case R.id.layout_me:
			bar_home.setVisibility(View.GONE);
			bar_question.setVisibility(View.GONE);
			bar_find.setVisibility(View.GONE);
			
			if (AppContext.instance().isLogin()) {
				bar_me.setVisibility(View.VISIBLE);
				actionbar_login.setVisibility(View.GONE);
			} else {
				bar_me.setVisibility(View.GONE);
				actionbar_login.setVisibility(View.VISIBLE);
			}
			
			if(meFragment!=null){
				fragmentTransaction.show(meFragment);
			}else{
				if (AppContext.instance().isLogin()) {
					meFragment = new ActiveFragment();
				}else{
					meFragment = new LoginFragment();
				}
				fragmentTransaction.add(R.id.frame_content, meFragment);
			}
			mCurrentFragment=meFragment;
			iv_home.setSelected(false);
			tv_home.setSelected(false);	
			iv_question.setSelected(false);
			tv_question.setSelected(false);
			iv_find.setSelected(false);
			tv_find.setSelected(false);
			iv_me.setSelected(true);
			tv_me.setSelected(true);
			
			break;
		}
		
		fragmentTransaction.commit();
	}
	
	public void hideFragments(FragmentTransaction ft) {
		if(mCurrentFragment!=null){
			ft.hide(mCurrentFragment);
		}
	    /*if (homeFragment != null)
	      ft.hide(homeFragment);
	    if (questionFragment != null)
	      ft.hide(questionFragment);
	    if (findFragment != null)
	      ft.hide(findFragment);
	    if (meFragment != null)
	      ft.hide(meFragment);*/
	  }

	

/*	private void updateSystemMessage() {
		Thread thread = new Thread(mRunnable);
		thread.start();
	}*/

/*	private Runnable mRunnable = new Runnable() {
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
	};*/

/*	private void sendActiveNum() {
		// 如果未登录过,不发获取数据协议。
		if (0 == AppContext.instance().getLoginUid()) {
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
								
								if (message.getStatus() ==0){
									AppContext.instance().Logout();
									AppContext.showToast("操作失败，您的账号已经被停封");
									Intent intent = new Intent(MainActivity.this,MainActivity.class);
									intent.putExtra("type", "login");
									startActivity(intent);
									
								}else{
									// 更新内存中消息数目
									updateBadge(message);
									AppContext.instance().setMessageNum(message);
								}
							} else {
								AppContext.showToast(response.optString("desc"));
							}
						} catch (Exception e) {
						}
					}
				});
	}
	*/
	/**
	 * 刷新
	 * @param totalMessage
	 */
	public void onEventMainThread(MessageRefreshTotal totalMessage){
		updateBadge(totalMessage.messageNum);
	}
	
	/**
	 * 刷新
	 * @param totalMessage
	 */
	public void onEventMainThread(MessageRefreshSingle totalMessage){
		updateBadge(AppContext.instance().getMessageNum());
	}
	
	private void updateBadge(MessageNum message) {
		int findNum=message.getFindNum();
		int activeNum=message.getActiveNum();
		
		
		if (findNum > 0) {
			iv_find_notice.setVisibility(View.VISIBLE);
			//大于99显示99+
			//String findStr=findNum>99?findNum+"+":findNum+"";
			//iv_find_notice.setText(findStr);
			
		} else {
			iv_find_notice.setVisibility(View.GONE);
		}
		if (activeNum > 0) {
			iv_active_notice.setVisibility(View.VISIBLE);
			//大于99显示99+
			String activeStr=activeNum>99?activeNum+"+":activeNum+"";
			iv_active_notice.setText(activeStr);
		} else {
			iv_active_notice.setVisibility(View.GONE);
		}
	}

}
