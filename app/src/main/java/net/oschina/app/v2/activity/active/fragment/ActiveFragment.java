package net.oschina.app.v2.activity.active.fragment;

import java.io.IOException;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.FindPasswordActivity;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.MallActivity;
import net.oschina.app.v2.activity.RegistActivity;
import net.oschina.app.v2.activity.tweet.view.CircleImageView;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.model.MessageNum;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.event.MessageRefreshSingle;
import net.oschina.app.v2.model.event.MessageRefreshTotal;
import net.oschina.app.v2.ui.dialog.SignInDialog;
import net.oschina.app.v2.ui.dialog.WaitDialog;
import net.oschina.app.v2.utils.ActiveNumType;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.FastBlurUtil;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.UIHelper;
import net.oschina.app.v2.utils.ViewFinder;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class ActiveFragment extends BaseFragment {
	private static final String FIND_SCREEN = "find_screen";
	private TextView tv_nickname;
	private TextView tv_jifen;
	private TextView tv_add_integral;
	private TextView tv_verify;
	private TextView tv_verified;
	private TextView tv_address;
	private ImageView iv_sex;
	private TextView tv_jifen2;
	//private TextView active_tiwen_num;
	//private TextView active_huida_num;
	//private TextView active_beicaina_num;
	private TextView active_fensishu_num;
	private TextView active_wodetiwen_num;
	private TextView active_wodehuida_num;
	private TextView active_zhuiwenwode_num;
	private TextView active_fensiqiuzhu_num;
	private TextView active_wodezhushouhao_disc;

	private TextView active_woganxingqu_discription;

	private TextView tv_rank;
	private View mHeaderLayoutContent;
	private ImageView mViewHeaderBg;
	private TextView btnSignIn;
	private TextView btnZhichi;
	private TextView mViewAttention;

	private boolean mIsWatingLogin;
	private View mIvClearUserName, mIvClearPassword;
	private EditText mEtUserName, mEtPassword;
	private TextView tv_forget_regist;
	private Button mBtnLogin;
	private TextView tv_forget_password;
	private CircleImageView iv_img_head;
	/**
	 * 新消息提示
	 */
	private TextView mMyQuestionNotice;
	private TextView mMyAnswerNotice;
	private TextView mFansForHelpNotice;
	private TextView mAskAfterNotice;
	private TextView mSysMessageNotice;
	private TextView mAttentionNotice;

	private DisplayImageOptions options;
	
	// private BroadcastReceiver mReceiver = new BroadcastReceiver() {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// if (mErrorLayout != null) {
	// mIsWatingLogin = true;
	// mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
	// mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
	// }
	// }
	// @Override
	// public void onReceive(Context arg0, Intent arg1) {
	// }
	// };
	private TextWatcher mUserNameWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// 用户名
			mIvClearUserName.setVisibility(TextUtils.isEmpty(s) ? View.GONE
					: View.VISIBLE);
		}
	};
	private TextWatcher mPassswordWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mIvClearPassword.setVisibility(TextUtils.isEmpty(s) ? View.GONE
					: View.VISIBLE);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = null;
		options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.ic_default_avatar);
		if (AppContext.instance().isLogin()) {
			view = inflater.inflate(R.layout.v2_fragment_active_private, container, false);
			mViewHeaderBg =(ImageView)view.findViewById(R.id.header_bg);
			mHeaderLayoutContent=view.findViewById(R.id.header_layout_content);
			ViewTreeObserver vto = mHeaderLayoutContent.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						mHeaderLayoutContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}else{
						mHeaderLayoutContent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
					mViewHeaderBg.setMinimumHeight(mHeaderLayoutContent.getHeight());
				}
			});
			initViews(view);
			loadData();
			// 重新请求用户信息
			sendGetUserInfomation();
			ViewFinder viewFinder=new ViewFinder(view);
			
			mMyQuestionNotice    =viewFinder.find(R.id.myquestion_notice);
			mMyAnswerNotice      =viewFinder.find(R.id.myanswer_notice);
			mFansForHelpNotice   =viewFinder.find(R.id.fansforhelp_notice);
			mAskAfterNotice      =viewFinder.find(R.id.askafter_notice);
			mSysMessageNotice    =viewFinder.find(R.id.sysMessage_notice);
			mAttentionNotice     =viewFinder.find(R.id.attention_notice);
			
			
			
			// 更新消息数目
			MessageNum message = AppContext.instance().getMessageNum();
			if (message == null) {
				message=new MessageNum();
			}
			ActiveNumType.updateMessageLabelWithEmpty(message.getQnum(), mMyQuestionNotice);
			ActiveNumType.updateMessageLabelWithEmpty(message.getAnum(), mMyAnswerNotice);
			ActiveNumType.updateMessageLabel(message.getFnum(), mFansForHelpNotice);
			ActiveNumType.updateMessageLabel(message.getAfternum(), mAskAfterNotice);
			ActiveNumType.updateMessageLabel(message.getMnum(), mSysMessageNotice);
			ActiveNumType.updateMessageLabel(message.getGnum(), mAttentionNotice);
			
			mIsWatingLogin = true;
		} else {
			view = inflater.inflate(R.layout.v2_fragment_login, container,
					false);
			initLoginView(view);
			setClick();
		}
		EventBus.getDefault().register(this);
		return view;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		loadData();
	}
	
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}




	private void setClick() {
		tv_forget_regist.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RegistActivity.class);
				startActivity(intent);
				// UIHelper.showInterest(LoginActivity.this);
			}
		});
		// 登陆按钮的监听，监听之后拿到数据，进行判断，根据返回的字段错误，显示错误信息
		mBtnLogin.setOnClickListener(new OnClickListener() {
			private String nickname;
			private String password;
			private StringEntity entity;

			@Override
			public void onClick(View v) {
				// 获取nickname和password
				obtainValues();

				NewsApi.loginUser(AppContext.mClientId, nickname, password,
						handler);

				Editor editor = AppContext.getPersistPreferences().edit();
				editor.putString(AppContext.LAST_INPUT_USER_NAME, nickname);
				editor.putString(AppContext.LAST_INPUT_PASSWORD, password);
				editor.commit();

			}

			private void obtainValues() {
				nickname = mEtUserName.getText().toString();
				password = mEtPassword.getText().toString();
			}
		});

		tv_forget_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						FindPasswordActivity.class);
				startActivity(intent);
			}
		});
	}

	// json响应的handle
	JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
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
						// 保存登录信息
						AppContext.instance().saveLoginInfo(user);
						// hideWaitDialog();
						handleLoginSuccess();
						AppContext.showToast("登录成功");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (AppException e) {
						e.printStackTrace();
					}
				} else {
					AppContext.instance().cleanLoginInfo();
					hideWaitDialog();
					AppContext.showToast(response.optString("desc"));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	// 初始化控件
	private void initLoginView(View view) {
		// 用户名
		mEtUserName = (EditText) view.findViewById(R.id.et_username);
		// 输入密码
		mEtPassword = (EditText) view.findViewById(R.id.et_password);
		// 登陆按钮
		mBtnLogin = (Button) view.findViewById(R.id.btn_login);
		tv_forget_regist = (TextView) view.findViewById(R.id.tv_forget_regist);
		tv_forget_password = (TextView) view
				.findViewById(R.id.tv_forget_password);

		mEtUserName.addTextChangedListener(mUserNameWatcher);
		mEtPassword.addTextChangedListener(mPassswordWatcher);
		mIvClearUserName = view.findViewById(R.id.iv_clear_username);
		mIvClearUserName.setOnClickListener(this);
		mIvClearPassword = view.findViewById(R.id.iv_clear_password);
		mIvClearPassword.setOnClickListener(this);

		String userName = AppContext.getPersistPreferences().getString(
				AppContext.LAST_INPUT_USER_NAME, "");
		String pwd = AppContext.getPersistPreferences().getString(
				AppContext.LAST_INPUT_PASSWORD, "");
		mEtUserName.setText(userName);
		mEtPassword.setText(pwd);
	}

	@Override
	public void onDestroyView() {
		// UmengUpdateAgent.setUpdateListener(null);
		super.onDestroyView();
	}

	private void initViews(View view) {
		view.findViewById(R.id.active_wodetiwen).setOnClickListener(this);
		view.findViewById(R.id.active_wodehuida).setOnClickListener(this);
		view.findViewById(R.id.active_zhuiwenwode).setOnClickListener(this);
		view.findViewById(R.id.active_fensiqiuzhu).setOnClickListener(this);
		view.findViewById(R.id.active_xitongxiaoxi).setOnClickListener(this);
		view.findViewById(R.id.active_guanzhuxinxi).setOnClickListener(this);
		view.findViewById(R.id.active_wodesoucang).setOnClickListener(this);
		view.findViewById(R.id.active_woganxingqu).setOnClickListener(this);
		view.findViewById(R.id.active_renwuchengjiu).setOnClickListener(this);
		view.findViewById(R.id.active_wodewupin).setOnClickListener(this);
		view.findViewById(R.id.active_wodezhushouhao).setOnClickListener(this);
		view.findViewById(R.id.go_mall).setOnClickListener(this);


		btnSignIn=(TextView)view.findViewById(R.id.btn_sign);
		btnSignIn.setOnClickListener(this);

		mViewAttention=(TextView)view.findViewById(R.id.attention);

		btnZhichi=(TextView)view.findViewById(R.id.zhichi);
		btnZhichi.setOnClickListener(this);

		active_woganxingqu_discription = (TextView) view
				.findViewById(R.id.active_woganxingqu_discription);
		// 昵称
		tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
		iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
		// 积分
		tv_jifen = (TextView) view.findViewById(R.id.tv_jifen);
		tv_jifen.setOnClickListener(this);

		tv_add_integral = (TextView) view.findViewById(R.id.tv_add_integral);

		// 等级
		tv_rank = (TextView) view.findViewById(R.id.rank);
		// 认证情况
		tv_verify = (TextView) view.findViewById(R.id.tv_verify);
		tv_verified= (TextView) view.findViewById(R.id.tv_verified);
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		// 提问
		//active_tiwen_num = (TextView) view.findViewById(R.id.active_tiwen_num);
		// 回答
		//active_huida_num = (TextView) view.findViewById(R.id.active_huida_num);
		// 被采纳数量
		//active_beicaina_num = (TextView) view.findViewById(R.id.active_beicaina_num);
		// 粉丝数
		active_fensishu_num = (TextView) view
				.findViewById(R.id.active_fensishu_num);
		// 我的提问
		active_wodetiwen_num = (TextView) view
				.findViewById(R.id.active_wodetiwen_num);
		// 回答
		active_wodehuida_num = (TextView) view
				.findViewById(R.id.active_wodehuida_num);
		// 被采纳数量
		active_zhuiwenwode_num = (TextView) view
				.findViewById(R.id.active_zhuiwenwode_num);
		// 粉丝数
		active_fensiqiuzhu_num = (TextView) view
				.findViewById(R.id.active_fensiqiuzhu_num);

		iv_img_head = (CircleImageView) view.findViewById(R.id.iv_img_head);
		iv_img_head.setBorderColor(0xFF9EE3F2);
		iv_img_head.setBorderWidth(4);

		iv_img_head.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				User u = AppContext.instance().getLoginInfo();
				if (null != u) {
					UIHelper.showUserCenter(getActivity(), u.getId(),
							u.getNickname());
				}

			}
		});

		active_wodezhushouhao_disc = (TextView) view
				.findViewById(R.id.active_wodezhushouhao_discription);

	}

	private void loadData() {
		final User user = AppContext.instance().getLoginInfo();
		if(user==null){
			return;
		}
//		tv_nickname.setText(Html.fromHtml("昵称: " + user.getNickname()
//				+ "  <font color='#9A9A9A'>LV" + user.getRank() + "</font>"));
//		tv_jifen.setText(Html.fromHtml("积分： <font color='#FB7C62'>"
//				+ user.getIntegral() + "分</font>"));

		if(user.issigned()){
			btnSignIn.setText("已签到");
			btnSignIn.setEnabled(false);
			btnSignIn.setBackgroundResource(R.drawable.bg_gray_half_circle_selector);
		}else {
			btnSignIn.setText("签到");
			btnSignIn.setEnabled(true);
			btnSignIn.setBackgroundResource(R.drawable.bg_red_half_circle_selector);
		}

		
		tv_nickname.setText(user.getNickname());
		tv_rank.setText(" LV." + user.getRank());

		mViewAttention.setText("关注"+user.getGnum());

		btnZhichi.setText(String.format("收到%d个支持",user.getSupportednum()));

		tv_jifen.setText(user.getIntegral() + "分");
		if (user.getRealname_status() == 1) {
			tv_verify.setText("已认证");
			tv_verify.setBackgroundResource(R.drawable.bg_pick_rounded_selector);
			//tv_verify.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_pink_normal));
			tv_verify.setVisibility(View.GONE);
			tv_verified.setVisibility(View.VISIBLE);
		} else {
			tv_verify.setText("未认证");
			tv_verify.setBackgroundResource(R.drawable.bg_gray_rounded_selector);
			tv_verified.setVisibility(View.GONE);
			tv_verify.setVisibility(View.VISIBLE);
			//tv_verify.setBackgroundDrawable(getResources().getDrawable(R.drawable.common_btn_pressed));
		}

		tv_verify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (user.getRealname_status() != 1) {
					UIHelper.showShiMingRenZheng(getActivity());
//				}
			}
		});
		tv_address.setText(user.getCompany());

		if (user.getSex() == 1) {
			iv_sex.setImageResource(R.drawable.userinfo_icon_male);
		} else {
			iv_sex.setImageResource(R.drawable.userinfo_icon_female);
		}

		//active_tiwen_num.setText(user.getQnum() + "");
		//active_huida_num.setText(user.getAnum() + "");
		//active_beicaina_num.setText(user.getCnum() + "");
		active_fensishu_num.setText("粉丝 "+user.getFnum() + "");
		active_wodetiwen_num.setText(+user.getQnum()+"");
		active_wodehuida_num.setText(user.getAnum()+"");
		active_zhuiwenwode_num.setText(getString(R.string.active_zhuiwenwode,
				"0"));
		active_fensiqiuzhu_num.setText(getString(R.string.active_fensiqiuzhu,
				"0")+"("+user.getFansnum()+")");

		String interest = user.getInterest();
		if (TextUtils.isEmpty(interest) || "null".equals(interest)) {
			interest = "";
		}
		active_woganxingqu_discription.setText(interest);

		int invitation = user.getInvitation();
		active_wodezhushouhao_disc.setText(invitation > 0 ? String
				.valueOf(invitation) : "");

		if(TextUtils.isEmpty(user.getHead())){
			iv_img_head.setImageResource(R.drawable.ic_default_avatar);
			mViewHeaderBg.setImageResource(R.drawable.header_bg);
		}else{
			ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(user.getHead()), iv_img_head,options,new ImageLoadingListener(){
						@Override
						public void onLoadingStarted(String s, View view) {
						}

						@Override
						public void onLoadingFailed(String s, View view, FailReason failReason) {

						}

						@Override
						public void onLoadingComplete(String s, View view, Bitmap bitmap) {
							if(bitmap==null){
								return;
							}

							Bitmap blurBitmap = FastBlurUtil.doBlur(bitmap, 8, false);
							mViewHeaderBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
							mViewHeaderBg.setImageBitmap(blurBitmap);
						}

						@Override
						public void onLoadingCancelled(String s, View view) {

						}
					});
		}
		// ImageLoader.getInstance().displayImage(user.getFace(), avatar);
		NewsApi.refreshScore(AppContext.instance().getLoginUid(), mRefreshScoreHandler);
	}
	
	private JsonHttpResponseHandler mRefreshScoreHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					
					String data=response.getString("data");
					int  integral=AppContext.instance().getLoginInfo().getIntegral();
					JSONObject o=new JSONObject(data);
					if(data!=null){
						integral=o.getInt("integral");
					}
					
					/*int integral=AppContext.instance().getLoginInfo().getIntegral();
					try {
						integral=Integer.parseInt(s);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					User user=AppContext.instance().getLoginInfo();
					user.setIntegral(integral);
					AppContext.instance().saveLoginInfo(user);
					tv_jifen.setText(user.getIntegral() + "分");
					
				} else {
					AppContext.showToast(response.getString("desc"));
				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};

	/**
	 * 更新消息状态为已读
	 * @param tv
	 * @param type
	 */
	public void updateTime(final TextView tv,final int type){
		
		NewsApi.updateTime(AppContext.instance().getLoginUid(), type, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						ActiveNumType.updateMessageLabel(0, tv);
						ActiveNumType.updateMessageNum(0, type, AppContext.instance().getMessageNum());
						EventBus.getDefault().post(new MessageRefreshSingle());
					} else {
						AppContext.showToast(response.optString("desc"));
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
	// 点击事件
	@Override
	public void onClick(View v) {
		
		final int id = v.getId();
		if (id == R.id.active_wodetiwen) {
			// 我的提问
			UIHelper.showMyQuestion(getActivity());
			updateTime(mMyQuestionNotice,ActiveNumType.ask_type);
		} else if (id == R.id.active_wodehuida) {
			// 我的回答
			UIHelper.showAnswer(getActivity());
			updateTime(mMyAnswerNotice,ActiveNumType.answer_type);
		} else if (id == R.id.find_faguiwenxian) {
			// 追问我的
			UIHelper.askmeagain(getActivity());
			updateTime(mAskAfterNotice,ActiveNumType.askafter_type);
			updateTime(mAskAfterNotice,ActiveNumType.askmeafter_type);
			/* mTvCacheSize.setText("0KB"); */
		} else if (id == R.id.active_zhuiwenwode) {
//			UIHelper.askmeagain(getActivity());
			int znum=0;
			int anum=0;
			if(AppContext.instance().getMessageNum()!=null){
				znum=AppContext.instance().getMessageNum().getZnum();
				anum=AppContext.instance().getMessageNum().getAfternum();
			}
			UIHelper.answerAfterByExtras(getActivity(),znum,
					anum);
			updateTime(mAskAfterNotice,ActiveNumType.askafter_type);
			/*updateTime(mAskAfterNotice,ActiveNumType.askafter_type);
			updateTime(mAskAfterNotice,ActiveNumType.askmeafter_type);*/
		} else if (id == R.id.active_fensiqiuzhu) {
			// 粉丝求助
			/* TDevice.openFensiqiuzhu(getActivity()); */
			UIHelper.fensiqiuzhu(getActivity());
			updateTime(mFansForHelpNotice,ActiveNumType.fansforhelp_type);
		} else if (id == R.id.active_xitongxiaoxi) {
			// 系统消息的模块
			UIHelper.xitongxiaoxi(getActivity());
			updateTime(mSysMessageNotice,ActiveNumType.sysMessage_type);
		} else if (id == R.id.active_guanzhuxinxi) {
			int anum=0;
			if(AppContext.instance().getMessageNum()!=null){
				anum=AppContext.instance().getMessageNum().getGnum();
			}
			UIHelper.guanzhuxiaoxiExtra(getActivity(),anum);
		}else if (id == R.id.active_wodesoucang) {
			int anum=0;
			if(AppContext.instance().getMessageNum()!=null){
				anum=AppContext.instance().getMessageNum().getGnum();
			}
			UIHelper.wodesoucangExtra(getActivity(), anum);
		} else if (id == R.id.active_woganxingqu) {
			UIHelper.woganxingqude(getActivity());
		} else if (id == R.id.active_renwuchengjiu) {
			UIHelper.renwuchengjiu(getActivity());
		} else if (id == R.id.active_wodewupin) {
			UIHelper.wodewupin(getActivity());
		} else if (id == R.id.active_wodezhushouhao) {
			UIHelper.myZhuShouId(getActivity());
		} else if (id == R.id.iv_clear_username) {
			mEtUserName.getText().clear();
			mEtUserName.requestFocus();
		} else if (id == R.id.iv_clear_password) {
			mEtPassword.getText().clear();
			mEtPassword.requestFocus();
		} else if (id == R.id.go_mall) {
			Intent intent = new Intent(getActivity(), MallActivity.class);
			startActivity(intent);
		}else if(id == R.id.btn_sign){
			signIndRequest();
		}else if(id==R.id.tv_jifen){
			//UIHelper.jifenxiangqing(getActivity());
		}else if(id==R.id.zhichi){
			UIHelper.zhichiwode(getActivity());
		}
	}

	private WaitDialog mSignProgressDialog;

	private void showSignDialog(){
		mSignProgressDialog=showWaitDialog(R.string.signing);
	}

	private void closeSignDialog(){
		if(mSignProgressDialog!=null&&mSignProgressDialog.isShowing()){
			mSignProgressDialog.dismiss();
			mSignProgressDialog=null;
		}
	}


	private void signIndRequest(){
		showSignDialog();
		NewsApi.signIn(AppContext.instance().getLoginUid(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				closeSignDialog();
				try {
					switch (response.getInt("code")){
						case 88:
							SignInDialog dialog=new SignInDialog(getActivity(),R.style.Dialog);
							JSONObject json = new JSONObject(response.optString("data"));
							String signremind=json.optString("signremind");
							String integral=json.optString("integral");
							String curmonth=json.optString("curmonth");
							int continuecount=json.optInt("continuecount");
							dialog.setContinuecount(continuecount);
							dialog.setSignremind(signremind);
							dialog.setIntegral(integral);
							dialog.setCurmonth(curmonth);
							dialog.prepareUI();
							dialog.show();

							//刷新个人信息积分
							tv_jifen.setText(integral + "分");
							try{
								User user=AppContext.instance().getLoginInfo();
								int preIntegral=user.getIntegral();
								int addIntegra=Integer.parseInt(integral)-preIntegral;
								if(addIntegra>0){
									tv_add_integral.setText("+"+addIntegra);
									user.setIntegral(Integer.parseInt(integral));
									AppContext.instance().saveLoginInfo(user);
									tv_add_integral.setVisibility(View.VISIBLE);
								}else{
									tv_add_integral.setVisibility(View.GONE);
								}

							}catch (Exception ex){
								tv_add_integral.setText("+2");
								tv_add_integral.setVisibility(View.VISIBLE);
							}

							btnSignIn.setText("已签到");
							btnSignIn.setEnabled(false);
							btnSignIn.setBackgroundResource(R.drawable.bg_gray_half_circle_selector);
							break;
						case 1290:
							AppContext.showToast("uid为空");
							break;
						case 1291:
							AppContext.showToast("用户不存在");
							break;
						case 1295:
							AppContext.showToast("今日已签到");
							break;
						case 1294:
							AppContext.showToast("签到失败");
							break;
					}


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				closeSignDialog();
			}
		});
	}

	private void updateMessage(MessageNum message) {
		ActiveNumType.updateMessageLabelWithEmpty(message.getQnum(), mMyQuestionNotice);
		ActiveNumType.updateMessageLabelWithEmpty(message.getAnum(), mMyAnswerNotice);
		ActiveNumType.updateMessageLabel(message.getFnum(), mFansForHelpNotice);
		ActiveNumType.updateMessageLabel(message.getAfternum(), mAskAfterNotice);
		ActiveNumType.updateMessageLabel(message.getMnum(), mSysMessageNotice);
		ActiveNumType.updateMessageLabel(message.getGnum(), mAttentionNotice);
	}

	private void setNewMessage(TextView textView, int number) {
		if (number > 0) {
			textView.setText(number + "");
			textView.setBackgroundResource(R.drawable.g_unread_messages_bg);
		} else {
			textView.setText("");
			textView.setBackgroundResource(R.drawable.ic_item_goto_right_tip);
		}
	}

	private void handleLoginSuccess() {
		// getActivity().finish();
		// this.notifyDataSetChanged();

		// Johnny Commit
		// MainActivity.mTabHost.setCurrentTab(3);
		// MainActivity.mTabHost.setCurrentTab(4);

		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.putExtra("type", "login");
		getActivity().startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FIND_SCREEN);
		MobclickAgent.onResume(getActivity());

		if (mIsWatingLogin && AppContext.instance().isLogin()) {
			sendGetUserInfomation();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FIND_SCREEN);
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onUserLogin() {
		handleLoginSuccess();
	}

	private void sendGetUserInfomation() {
		int uid = AppContext.instance().getLoginUid();
		NewsApi.getUserInfo(uid, uid,1,new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					org.json.JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						JSONObject userinfo = new JSONObject(response
								.getString("data"));
						User user = User.parse(userinfo);
						User oldUser = AppContext.instance().getLoginInfo();
						user.setOnce(oldUser.isOnce());
						user.setPassword(oldUser.getPassword());
						AppContext.instance().saveLoginInfo(user);
						// 重新刷新数据
						loadData();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		});
	}
	
	/**
	 * 刷新
	 * @param totalMessage
	 */
	public void onEventMainThread(MessageRefreshTotal totalMessage){
		updateMessage(totalMessage.messageNum);
	}
	
	
}
