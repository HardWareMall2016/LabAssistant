package net.oschina.app.v2.activity.user.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.ShareHelper;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class MyZhuShouIDFragment extends BaseFragment implements
SoftKeyboardStateListener, OnClickEmojiListener {

	private static final String SETTINGS_SCREEN = "settings_screen";
    private TextView myid,yiyaoqing_num,shengyuyaoqing_num,zongyaoqing_num,ball_num,shengyu_time;
	private SoftKeyboardStateHelper mKeyboardHelper;
	private Button bt_sms,bt_weixin;

	String zhuShouId = "";
	ShareHelper sp = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_myzhushouid, container,
				false);
		initViews(view);
		
		handleSubmit();
		
		sp = new ShareHelper(getActivity());
		
		return view;
	}

	
	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.bt_sms) {
		
			User u = AppContext.instance().getLoginInfo();
			if(null != u)
			{
				//sp.shareToSms("我的助手ID号：" + u.getInvitation());
				sp.shareToSms("实验助手，实验室从业人员的专属社区。邀请码"+ u.getInvitation()+"，注册时别忘填写邀请人哦~各大应用市场搜索“实验助手”即可下载。");
				
			}
			
		} else if (id == R.id.bt_weixin) {
			User u = AppContext.instance().getLoginInfo();
			if(null != u)
			{
				sp.handleShare("实验助手，实验室从业人员的专属社区，邀请码"+ u.getInvitation()+"，赶快来加入我们吧！","实验助手，实验室从业人员的专属社区，邀请码"+ u.getInvitation()+"，赶快来加入我们吧！",
						"http://a.app.qq.com/o/simple.jsp?pkgname=com.shiyanzhushou.app&g_f=991653",getActivity());
				//sp.shareToWeiChat("","实验助手，实验室从业人员的专属社区，邀请码"+ u.getInvitation()+"，赶快来加入我们吧！","http://a.app.qq.com/o/simple.jsp?pkgname=com.shiyanzhushou.app&g_f=991653","");
			}
		}
	}
	@Override
	public void onDestroyView() {
		//UmengUpdateAgent.setUpdateListener(null);
		super.onDestroyView();
	}

	private void initViews(View view) {
		myid = (TextView)view.findViewById(R.id.myid);
		yiyaoqing_num = (TextView)view.findViewById(R.id.yiyaoqing_num);
		shengyuyaoqing_num = (TextView)view.findViewById(R.id.shengyuyaoqing_num);
		zongyaoqing_num = (TextView)view.findViewById(R.id.zongyaoqing_num);
		ball_num = (TextView)view.findViewById(R.id.ball_num);
		shengyu_time = (TextView)view.findViewById(R.id.shengyu_time);
		bt_sms = (Button)view.findViewById(R.id.bt_sms);
		bt_weixin = (Button)view.findViewById(R.id.bt_weixin);
	
		bt_sms.setOnClickListener(this);
		bt_weixin.setOnClickListener(this);
		
		
		
		
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	

	private void handleSubmit() {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_network_error);
			return;
		}
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLogin(getActivity());
			return;
		}

		getAssistantInfo(AppContext.instance().getLoginUid());

	}
	protected void getAssistantInfo(int uid) {
		NewsApi.getAssistantInfo(uid,mJsonHander);
	}
	
	private void onLoadSuccess(JSONObject data)
	{  
		User user = AppContext.instance().getLoginInfo();
		myid.setText(user.getInvitation()+"");
		
		yiyaoqing_num.setText(Html.fromHtml("本周已邀请人数：<font color='#25829C'>" +data.optInt("wnum",0)+"</font>"));
		shengyuyaoqing_num.setText(Html.fromHtml("本周可邀请人数：<font color='#25829C'>" + data.optInt("snum",0)+"</font>"));
		zongyaoqing_num.setText(Html.fromHtml("总邀请人数：<font color='#25829C'>"+ data.optInt("total",0)+"</font>"));
		ball_num.setText(Html.fromHtml("你的魅力已经打败了 <font color='#25829C'>"+data.optInt("value",0)+"%</font> 的网友"));
		shengyu_time.setText(Html.fromHtml("距下次更新邀请限制数还有：<font color='#25829C'>"+data.optInt("days",0) + "</font>天"));
	}
	
	
	private JsonHttpResponseHandler mJsonHander = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
				 if(response.getString("desc").equals("success"))
				 {
					onLoadSuccess(new JSONObject(response.getString("data")));
					
			     }else
			     {
				   int code=response.getInt("code");
				   AppContext.showToast("获取失败");
                 }
				
				 } catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					
					
				}
		 }

		@Override
		 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			//mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(SETTINGS_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(SETTINGS_SCREEN);
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onEmojiClick(Emoji emoji) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardOpened(int keyboardHeightInPx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardClosed() {
		// TODO Auto-generated method stub
		
	}
}
