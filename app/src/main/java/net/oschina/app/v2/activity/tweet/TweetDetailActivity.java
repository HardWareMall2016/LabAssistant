package net.oschina.app.v2.activity.tweet;

import java.util.HashMap;
import java.util.Map;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.ImageShowerActivity;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.tweet.adapter.TweetAnswerAdapter;
import net.oschina.app.v2.activity.tweet.fragment.TweetAnswerListFragment;
import net.oschina.app.v2.activity.tweet.fragment.TweetDetailFragment;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker.OnMediaPickerListener;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.emoji.EmojiEditText;
import net.oschina.app.v2.emoji.SupperListActivity;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.event.AdoptSuccEvent;
import net.oschina.app.v2.model.event.MessageRefreshTotal;
import net.oschina.app.v2.ui.dialog.CommonDialog;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

/**
 * 问题详细界面
 * 
 * @author Johnny
 * 
 */
public class TweetDetailActivity extends BaseActivity {

	private boolean isFirstComming = true;

	public String mediaUri = "";
	public String superlist = "";
	public String url;

	private ShareHelper shareHelper;
	private TweetDetailFragment f;
	private ExtendMediaPicker pickView;
	private TweetAnswerListFragment answerListFragment;

	private View mTextView;
	private View mBottomView;
	private Button mButton;
	private TextView tv_zhuiwen, mTvActionTitle;
	private EmojiEditText mEtInput;
	private ImageButton mBtnEmoji, mBtnMore;
	private Button mBtnSend;
	private TextView mTvAsk, mTvRank, mTvTime, mTvTitle, mTvCommentCount;
	private ImageView mIvPic;
	private RelativeLayout reward_layout;
	private ImageView pic_reward;
	private TextView tv_reward;
	protected View tip_layout;
	protected View tip_close;
	private Ask ask;

	private HashMap<Integer, UserBean> userList;

	@Override
	protected int getLayoutId() {
		return R.layout.tweet_detail_layout;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	protected void initActionBar(ActionBar actionBar) {
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = inflateView(R.layout.v2_actionbar_custom_back_title_more);
		View back = view.findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		View tv_back_title = view.findViewById(R.id.tv_back_title);
		if (null != tv_back_title) {

			tv_back_title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
		}
		mTvActionTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
		View moreBtn = view.findViewById(R.id.tv_actionbar_right_more);
		moreBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMore();
			}
		});
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}

	@Override
	protected void init(Bundle savedInstanceState) {

		shareHelper = new ShareHelper(this);
		pickView = new ExtendMediaPicker(this);

		mTvAsk = (TextView) findViewById(R.id.tv_byask);
		mTvRank = (TextView) findViewById(R.id.tv_byrank);
		mTvTime = (TextView) findViewById(R.id.tv_time);
		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mIvPic = (ImageView) findViewById(R.id.iv_pic);
		mTvCommentCount = (TextView) findViewById(R.id.tv_comment_count);

		tv_zhuiwen = (TextView) findViewById(R.id.tv_zhuiwen);
		mBottomView = findViewById(R.id.bottomview);
		mButton = (Button) findViewById(R.id.button);

		mTextView = findViewById(R.id.ly_bottom);
		mBtnEmoji = (ImageButton) findViewById(R.id.btn_emoji);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnMore = (ImageButton) findViewById(R.id.btn_more);
		mEtInput = (EmojiEditText) findViewById(R.id.et_input);
		mEtInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EmojiEditText editText = ((EmojiEditText) v);
				if (null != editText.getmHeaderUnDelete()) {
					int index = editText.getmHeaderUnDelete().length();
					if (index > editText.getSelectionStart()) {
						editText.setSelection(index);
					}
				}
			}
		});

		reward_layout = (RelativeLayout) findViewById(R.id.reward_layout);
		pic_reward = (ImageView) findViewById(R.id.pic_reward);
		tv_reward = (TextView) findViewById(R.id.tv_reward);

		// 填问题数据
		ask = (Ask) getIntent().getSerializableExtra("ask");
		NewsApi.getAskById(ask.getId(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.getString("desc").equals("success")) {

						try {
							ask = Ask.parse( new JSONObject(response
									.getString("data")));
							fillUI();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
//		fillUI();

		tip_layout=findViewById(R.id.tip_layout);
		tip_close=findViewById(R.id.tip_close);
		tip_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				tip_layout.setVisibility(View.GONE);
			}
		});
		// 如果没有登录的话
		if (!AppContext.instance().isLogin()) {
			showLogin();
		}

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();

		answerListFragment = new TweetAnswerListFragment();
		answerListFragment.setArguments(getIntent().getExtras());
		trans.add(R.id.container, answerListFragment);

		trans.commit();

		mBtnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pickView.showPickerView();
			}
		});

		if (AppContext.instance().isLogin()) {
			User user = AppContext.instance().getLoginInfo();
			if (user.getRealname_status() == 1) {// 已认证
				mBtnEmoji.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TweetDetailActivity.this,
								SupperListActivity.class);
						intent.putExtra("supperList", userList);
						startActivityForResult(intent, 1000);
					}
				});
			} else {
				mBtnEmoji.setImageDrawable(getResources().getDrawable(
						R.drawable.at_forbid));
				mBtnEmoji.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						AppContext.showToast("需要认证");
					}
				});
			}
		} else {
			mBtnEmoji.setImageDrawable(getResources().getDrawable(
					R.drawable.at_forbid));
			mBtnEmoji.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AppContext.showToast("需要认证");
				}
			});
		}

		EventBus.getDefault().register(this);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	/**
	 * 刷新
	 * @param totalMessage
	 */
	public void onEventMainThread(AdoptSuccEvent adoptSuccEvent){
		if(ask.getId()==adoptSuccEvent.askId){
			changeAskState();
		}
	}

	private JsonHttpResponseHandler msubHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					reset();
					answerListFragment.adapter.clear();
					answerListFragment.sendRequestData();
					AppContext.showToast("回答成功");
				} else {
					AppContext.showToast("回答失败");
				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	private JsonHttpResponseHandler mCommentAfterHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					reset();
					answerListFragment.adapter.clear();
					answerListFragment.sendRequestData();
					AppContext.showToast("追问成功");
				} else {
					AppContext.showToast("追问失败");
				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	public void showTiWen() {
		mButton.setTag(2);
		mButton.setText("继续回答");
		mBottomView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
	}

	public void showZhuiwen() {
		mEtInput.setHint("追问");
		mEtInput.setHintTextColor(getResources().getColor(
				R.color.font_zhuiwen_back));
		mBottomView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.VISIBLE);
	}

	public void reset() {
		TDevice.hideSoftKeyboard(mEtInput);

		if (mEtInput != null) {
			mEtInput.getText().clear();
			mEtInput.clearFocus();
			// mEtInput.setHint(R.string.publish_comment);
			mEtInput.setHint(R.string.publish_comment);
			mEtInput.setHintTextColor(getResources().getColor(
					R.color.font_zhuiwen_back));
			// mEtInput.setTextColor();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu_more:
			onMore();
			break;
		}
		return true;
	}

	
	private String toSomeone;
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
			userList = (HashMap<Integer, UserBean>) intent
					.getSerializableExtra("supperList");
			if (userList != null) {
				StringBuilder builder = new StringBuilder();
				StringBuilder sb = new StringBuilder();
				for (Map.Entry<Integer, UserBean> entry : userList.entrySet()) {
					UserBean user = entry.getValue();
					builder.append(user.getId() + ",");
					sb.append("@" + user.getNickname() + ";");
					
					
				}
				if (builder.length() > 0) {
					builder = builder.deleteCharAt(builder.length() - 1);
				}
				this.superlist = builder.toString();
				toSomeone=sb.toString();
				mEtInput.setHeaderUnDelete(sb.toString());
				
			}
		}
		pickView.onActivityResult(requestCode, resultCode, intent);
	}

	boolean isAllowShare=true;
	
	protected void onMore() {
		int item; // 自己提的问题、举报 隐藏不显示
		if (ask.getUid()==AppContext.instance().getLoginUid()) {
			item= R.array.app_bar_item;
		}else {item = R.array.app_bar_items;}
		
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(this);
		dialog.setTitle(R.string.title_more);
		dialog.setItemsWithoutChk(
				getResources().getStringArray(item),
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						dialog.dismiss();
						if (position == 0) {
							NewsApi.getQuestionShareUrl(ask.getId(),
									new JsonHttpResponseHandler() {
										@Override
										public void onSuccess(int statusCode,
												Header[] headers,
												JSONObject response) {
											try {
												if (response.getInt("code") == 88) {
													JSONObject dataObject = new JSONObject(
															response.getString("data"));
													url = dataObject
															.optString("url");
												} else {
												}

											} catch (JSONException e1) {
												e1.printStackTrace();
											}
										}
									});
						}
						goToSelectItem(position);
					}
				});
		dialog.show();
	}

	private void goToSelectItem(int position) {

		switch (position) {
		case 0:
			final CommonDialog dialogShare = DialogHelper
					.getPinterestDialogCancelable(this);
			dialogShare.setTitle(R.string.title_share);
			dialogShare.setItemsWithoutChk(

			getResources().getStringArray(R.array.app_share_items),
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							if (position == 0) {
								shareHelper.shareToSinaWeibo(
										"分享问题：" + ask.getContent(),
										url,
										"http://dl.iteye.com/upload/picture/pic/133287/9b6f8a1d-fe2f-3858-9423-484447c41908.png");
							} else if (position == 1) {
								shareHelper.shareToWeiChat(
										"分享问题",
										ask.getContent(),
										url,
										"");
							} else if (position == 2) {
								shareHelper.shareToWeiChatCircle(
										"分享问题",
										ask.getContent(),
										url,
										R.drawable.share_icon);
							}else if (position == 3) {
								
								NewsApi.shareQuestion(ask.getId(),AppContext.instance().getLoginUid(),new JsonHttpResponseHandler() {
									@Override
									public void onSuccess(int statusCode, Header[] headers,
											JSONObject response) {
										try {
											int code=response.getInt("code");
											if(code==88){
												Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_SHORT).show();
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
								}

							dialogShare.dismiss();
							// goToSelectItem(position);
						}
					});
			dialogShare.show();
			break;
		case 1:
			if (!AppContext.instance().isLogin()) {
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("type", "login");
				startActivity(intent);
			} else {
				doReport();
			}
			break;
		}
	}
	
	protected void doReport() {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
		titleTv.setText("是否举报");
		// 设置监听
		Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
		zhichi.setText("确定");
		// 支持
		zhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!AppContext.instance().isLogin()) {
					Intent intent = new Intent(TweetDetailActivity.this,
							MainActivity.class);
					intent.putExtra("type", "login");
					startActivity(intent);
				} else {
					int uid = AppContext.instance().getLoginUid();
					NewsApi.reportQuestion(uid, ask.getId(),
							new JsonHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									String str="举报成功";
									
									try {
										int code =response.getInt("code");
										if(code!=88){
											str=response.getString("desc");
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									
									AppContext.showToast(str);
								}
							});
				}
				dialog.dismiss();
			}
		});
		// 查看支持者
		Button chakanzhichi = (Button) window
				.findViewById(R.id.ib_chakanzhichi);
		chakanzhichi.setText("取消");
		chakanzhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	private void fillUI() {
		mTvTime.setText(ask.getinputtime());
		mTvCommentCount
				.setText(getString(R.string.comment_count, ask.getanum()));

		String supper = ask.getsuperlist();
		if (!TextUtils.isEmpty(supper) && !"null".equals(supper)) {
			
			
			mTvAsk.setText(Html.fromHtml("邀请 <font color=#2FBDE7>" + supper
					+ "</font> 进行回答"));
			mTvAsk.setVisibility(View.VISIBLE);
		} else {
			mTvAsk.setVisibility(View.GONE);
		}
		String label = ask.getLabel();
		if (TextUtils.isEmpty(label)) {
			label = "暂无";
		}
		mTvRank.setText(Html.fromHtml("标签:<font color=#2FBDE7>" + label
				+ "</font>"));
		if (!StringUtils.isEmpty(ask.getImage()) && "null" != ask.getImage()) {
			mIvPic.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(ask.getImage()), mIvPic);

			mIvPic.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(TweetDetailActivity.this,
							ImageShowerActivity.class);
					intent.putExtra("imageUri", ask.getImage());
					startActivity(intent);
				}
			});

		} else {
			mIvPic.setVisibility(View.GONE);
		}
		if (AppContext.instance().isLogin()) {
			if (ask.getUid() == AppContext.instance().getLoginUid()) {
				mTvActionTitle.setText("我的提问");
				tip_layout.setVisibility(View.VISIBLE);
			} else {
				mTvActionTitle.setText(ask.getnickname() + "的提问");
			}
		} else {
			mTvActionTitle.setText(ask.getnickname() + "的提问");
		}

		if (ask.getreward() != 0) {
			//reward_layout.setVisibility(View.VISIBLE);
			//tv_reward.setText(String.valueOf(ask.getreward()));
			//mTvTitle.setText("" + ask.getContent());
			String content=" "+ask.getreward()+" "+ask.getContent();
			int length=(""+ask.getreward()).length();
			SpannableString spanString = new SpannableString(" "+ask.getreward()+ask.getContent());  
		    Drawable d = getResources().getDrawable(R.drawable.dollar);  
		    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());  
		    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);  
		    spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		    
		    ForegroundColorSpan forColor = new ForegroundColorSpan(getResources().getColor(R.color.reward));  
		    spanString.setSpan(forColor, 1, 1+length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		    mTvTitle.setText(spanString);
		    
		} else {
			reward_layout.setVisibility(View.GONE);
			mTvTitle.setText("" + ask.getContent());
		}

		// mContent.loadDataWithBaseURL(null, ask.getContent(), "text/html",
		// "utf-8", null);
		// mContent.setWebViewClient(UIHelper.getWebViewClient());
	}

	public void showTiWenMe() {
		mButton.setTag(2);
		mButton.setText("继续回答");
		mBottomView.setVisibility(View.GONE);
		mTextView.setVisibility(View.GONE);
	}

	public void bottomBack() {
		answerListFragment.handleBottomView();
	}

	/**
	 * 底部显示【回答】界面
	 */
	public void showAnswer() {
		if (ask.getUid() == AppContext.instance().getLoginUid()) {// 自己的提问
			mTextView.setVisibility(View.GONE);
			return;
		}
		mBtnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mEtInput.getText().toString();
				mEtInput.getText().clear(); //快速按确定按钮会出现多条回复
				if(!TextUtils.isEmpty(toSomeone)){
					text=text.replaceAll(toSomeone, "");
				}
				
				if (TextUtils.isEmpty(text)) {
					AppContext
							.showToastShort(R.string.tip_comment_content_empty);
					return;
				}
				if (!TDevice.hasInternet()) {
					AppContext.showToastShort(R.string.tip_network_error);
					return;
				}
				int id = ask.getId();
				int uid = AppContext.instance().getLoginUid();
				boolean relation;// 是否@高手
				if (TextUtils.isEmpty(superlist)) {
					relation = false;
				} else {
					relation = true;
				}
				
				NewsApi.subComment(id, uid, false, text, relation, superlist,
						msubHandler);
			}
		});
		pickView.setOnMediaPickerListener(new MyMediaPickerListener(1, null));
		tv_zhuiwen.setVisibility(View.GONE);
		mBottomView.setVisibility(View.GONE);
		mTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 底部显示【补充回答】界面
	 */
	public void showReAnswer(final Comment comment) {
		mButton.setText("补充回答");
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showReplyCommunicat(TweetDetailActivity.this, ask,
						comment);
			}
		});
		mBottomView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.GONE);
	}

	/**
	 * 底部显示【追问】界面
	 */
	public void showTraceAsk(final Comment comment) {
		showReTraceAsk(comment);

		// tv_zhuiwen.setVisibility(View.VISIBLE);
		// mBtnSend.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// String text = mEtInput.getText().toString();
		// if (TextUtils.isEmpty(text)) {
		// AppContext
		// .showToastShort(R.string.tip_comment_content_empty);
		// return;
		// }
		// if (!TDevice.hasInternet()) {
		// AppContext.showToastShort(R.string.tip_network_error);
		// return;
		// }
		// int id = ask.getId();
		// int uid = AppContext.instance().getLoginUid();
		// NewsApi.addCommentAfter(id, uid, comment.getId(), text,
		// mCommentAfterHandler);
		// }
		// });
		// pickView.setOnMediaPickerListener(new MyMediaPickerListener(2,
		// comment));
		// mBottomView.setVisibility(View.GONE);
		// mTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 底部显示【参与讨论】界面
	 */
	public void showReTraceAsk(final Comment comment) {
		mButton.setText("参与讨论");
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showReAnswerCommunicat(TweetDetailActivity.this, ask,
						comment);
			}
		});
		mBottomView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.GONE);
	}

	/**
	 * 底部显示【点击登录】界面
	 */
	public void showLogin() {
		mButton.setText("点击登录");
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showLoginView(TweetDetailActivity.this);
			}
		});
		mBottomView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.GONE);
	}

	/**
	 * 
	 */
	public void changeAskState() {
		ask.setissolveed(1);
		((TweetAnswerAdapter)answerListFragment.adapter).setAdoptState(1);
		refreshList();
	}

	class MyMediaPickerListener implements OnMediaPickerListener {
		private int type;// 1:回答,2:追问
		private Comment comment;

		public MyMediaPickerListener(int type, Comment comment) {
			this.type = type;
			this.comment = comment;
		}

		@Override
		public void onSelectedMediaChanged(String mediaUri) {
			NewsApi.uploadImage(3, mediaUri, new Handler() {
				@Override
				public void handleMessage(Message msg) {
					try {
						switch (msg.what) {
						case 1:
							String returnStr = msg.getData()
									.getString("return");
							JSONObject jsonObject = new JSONObject(returnStr);
							String imageUrl = jsonObject.getString("url");
							int id = ask.getId();
							int uid = AppContext.instance().getLoginUid();
							if (type == 1) {
								NewsApi.subComment(id, uid, false, null,
										imageUrl, false, null, msubHandler);
							} else if (type == 2) {
								NewsApi.addCommentAfter(id, uid, 0,
										comment.getId(), 0,null, imageUrl,
										mCommentAfterHandler);
							}
							break;
						case 2:
							AppContext.showToast("发送图像失败");
							break;
						default:
							break;
						}
						System.out.println(msg.what);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isFirstComming) {
		//	refreshList();
		}
		isFirstComming = false;
	};
	
	

	public void refreshList() {
		answerListFragment.adapter.clear();
		
		answerListFragment.sendRequestData();
	}
}
