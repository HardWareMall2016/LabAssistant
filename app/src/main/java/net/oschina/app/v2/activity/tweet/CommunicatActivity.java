package net.oschina.app.v2.activity.tweet;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.tweet.fragment.CommunicatFragment;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker.OnMediaPickerListener;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.emoji.EmojiEditText;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.event.AdoptSuccEvent;
import net.oschina.app.v2.ui.dialog.CommonDialog;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

/**
 * 回复互动界面
 * 
 * @author Johnny
 * 
 */
public class CommunicatActivity extends BaseActivity implements OnClickListener {

	private CommunicatFragment communicatFragment;

	private Ask ask;
	private Comment comment;
	private int type;// 1：回复，2：追问

	private ShareHelper shareHelper;
	private ExtendMediaPicker pickView;

	public String mediaUri = "";

	private View reportBtn;
	private View adoptBtn;

	private LinearLayout bottomLayout;
	private LinearLayout loginLayout;
	private Button loginBtn;
	private RelativeLayout communicatLayout;
	private ImageButton btnTakePhoto, btnSend;
	private EmojiEditText emojiEditText;
	private TextView mTvActionTitle;
	private RelativeLayout mTipLayout;
	private ImageView mTipClose;

	@Override
	protected int getLayoutId() {
		return R.layout.v2_activity_communicat;
	}

	protected void onBeforeSetContentLayout() {
	}

	@Override
	protected void initActionBar(ActionBar actionBar) {
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = inflateView(R.layout.v2_actionbar_for_comunicat);
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

		reportBtn = view.findViewById(R.id.iv_report);			//举报
		adoptBtn = view.findViewById(R.id.iv_adopt);  			//采纳
		reportBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doReport();
			}
		});
		adoptBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doAdopt();
			}
		});

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}
	
	Handler handler=new Handler();

	@Override
	protected void init(Bundle savedInstanceState) {
		ask = (Ask) getIntent().getSerializableExtra("ask");
		comment = (Comment) getIntent().getSerializableExtra("comment");
		type = getIntent().getIntExtra("type", 1);
		
		String title=getIntent().getStringExtra("title");

		shareHelper = new ShareHelper(this);
		pickView = new ExtendMediaPicker(this);

		bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		loginLayout = (LinearLayout) findViewById(R.id.bottomview);
		loginBtn = (Button) findViewById(R.id.button);
		communicatLayout = (RelativeLayout) findViewById(R.id.ly_bottom);
		btnTakePhoto = (ImageButton) findViewById(R.id.btn_takephoto);
		emojiEditText = (EmojiEditText) findViewById(R.id.et_input);
		emojiEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EmojiEditText editText = ((EmojiEditText) v);
				if (null != editText.getHeader()) {
					int index = editText.getHeader().length();
					if (index > editText.getSelectionStart()) {
				//		editText.setSelection(index);
					}
				}
				handler.postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						communicatFragment.selectlast();
					}
					
				}, 200);
				
			}
		});
		btnSend = (ImageButton) findViewById(R.id.btn_send);

		int uid = AppContext.instance().getLoginUid();
		if (uid != ask.getUid()) {
			adoptBtn.setVisibility(View.GONE);
		}
		if (comment.getauid()==uid){ //自己的回答、举报 隐藏
			reportBtn.setVisibility(View.GONE);
		}

		//回答、追问的标题
		String	strnickname = comment.getnickname();

		if(comment.getnickname() != null){
			if (comment.getnickname().length() > 7){
				strnickname = (new StringBuilder()).append(comment.getnickname().substring(0, 6)).append("...")
						.toString();
			}
		}
		
		if(type==1){
			if (comment.getauid()==uid){ //自己的回答
				mTvActionTitle.setText("我" + "的回答");
			}else{
				mTvActionTitle.setText(strnickname + "的回答");
			}
		}else{
			if(TextUtils.isEmpty(title)){
				if (comment.getauid()==uid){ //自己的
					mTvActionTitle.setText("我" + "的回答的追问讨论");
				}else{
					mTvActionTitle.setText(strnickname + "的回答的追问讨论");
				}
			}else{
				mTvActionTitle.setText(title + "的回答的追问讨论");
			}
			
		}
		

		btnTakePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pickView.setOnMediaPickerListener(new OnMediaPickerListener() {
					@Override
					public void onSelectedMediaChanged(String imageUrl) {
						uploadImage(imageUrl);
					}
				});
				pickView.showPickerView();
			}
		});

		mTipLayout = (RelativeLayout) findViewById(R.id.tip_layout);
		if (type == 1) {
			mTipLayout.setVisibility(View.GONE);
		}
		mTipClose = (ImageView) findViewById(R.id.tip_close);
		mTipClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTipLayout.setVisibility(View.GONE);
			}
		});

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction trans = fm.beginTransaction();
		communicatFragment = new CommunicatFragment();
		communicatFragment.setArguments(getIntent().getExtras());
		trans.add(R.id.container, communicatFragment);
		trans.commit();

	}

	/**
	 * 更多对话框
	 */
	protected void onMore() {
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(this);
		dialog.setTitle(R.string.title_more);
		dialog.setItemsWithoutChk(
				getResources().getStringArray(R.array.app_bar_items),
				new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						dialog.dismiss();
						goToSelectItem(position);
					}
				});
		dialog.show();
	}

	protected void doAdopt() {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
		titleTv.setText("是否采纳");
		// 设置监听
		Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
		zhichi.setText("确定");
		// 支持
		zhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int uid = AppContext.instance().getLoginUid();
				NewsApi.userAttention(uid, comment.getId(),
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode,
									Header[] headers, JSONObject response) {
								if (response.optInt("code") == 88) {
									AppContext.showToast("采纳成功");
									EventBus.getDefault().post(new AdoptSuccEvent(ask.getId()));
								} else {
									AppContext.showToast("采纳失败");
								}
							}
						});
				dialog.dismiss();
			}
		});
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

	protected void doReport() {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
		titleTv.setText("提示");
		// 设置监听
		Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
		zhichi.setText("确定");
		TextView messageTv= (TextView) window.findViewById(R.id.tv_message);
		messageTv.setVisibility(View.VISIBLE);
		messageTv.setText("您确定举报该答案吗？");
		// 支持
		zhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!AppContext.instance().isLogin()) {
					Intent intent = new Intent(CommunicatActivity.this,
							MainActivity.class);
					intent.putExtra("type", "login");
					startActivity(intent);
				} else {
					int uid = AppContext.instance().getLoginUid();
					NewsApi.reportAnswer(uid,comment.getId(),
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

	private void goToSelectItem(int position) {

		switch (position) {
		case 0:// 分享
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
								// shareHelper.shareToSinaWeibo();
							} else if (position == 0) {
								shareHelper.shareToWeiChat("实验助手", "实验助手",
										"实验助手");
							} else if (position == 1) {
								shareHelper.shareToWeiChatCircle("实验助手",
										"实验助手", "实验助手");
							}/*else if(position ==2){
								AppContext.showToast("2");
							}else if(position ==3){
								AppContext.showToast("3");
							}else if(position ==4){
								AppContext.showToast("4");
							}*/
							dialogShare.dismiss();
						}
					});
			dialogShare.show();
			break;
		case 1:// 举报
			if (!AppContext.instance().isLogin()) {
				Intent intent = new Intent(this, MainActivity.class);
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
			break;
		}
	}

	private void uploadImage(String imagePath) {
		NewsApi.uploadImage(3, imagePath, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					switch (msg.what) {
						case 1:
							String returnStr = msg.getData().getString("return");
							JSONObject jsonObject = new JSONObject(returnStr);
							String imageUrl = jsonObject.getString("url");
							int id = ask.getId();
							int uid = AppContext.instance().getLoginUid();
							if (type == 1) {
								NewsApi.subComment(id, uid, false, comment.getId(), null, imageUrl,
										false, null, msubHandler);
							} else if (type == 2) {
								if (TextUtils.isEmpty(emojiEditText.getHeader())) {
									AppContext
											.showToastShort(R.string.tip_null_direct_person_empty);
									return;
								}
								NewsApi.addCommentAfter(id,
										emojiEditText.getAskUid(), uid,
										comment.getId(), emojiEditText.getId(), "", imageUrl,
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

	public void showReplyView() {
		loginLayout.setVisibility(View.GONE);
		communicatLayout.setVisibility(View.VISIBLE);

		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = emojiEditText.getText().toString();
				emojiEditText.getText().clear();  //快速按确定按钮会出现多条回复
				emojiEditText.clearHeader();
				if (TextUtils.isEmpty(text)) {
					AppContext
							.showToastShort(R.string.tip_comment_content_empty);
					return;
				}
				if (!TDevice.hasInternet()) {
					AppContext.showToastShort(R.string.tip_network_error);
					return;
				}
				reset();//2015-02-28，添加，隐藏输入法
				int id = ask.getId();
				int uid = AppContext.instance().getLoginUid();
				NewsApi.subComment(id, uid, false, comment.getId(), text, false, null,
						msubHandler);
			}
		});
	}

	public void showTraceAskView() {
		loginLayout.setVisibility(View.GONE);
		communicatLayout.setVisibility(View.VISIBLE);

		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(emojiEditText.getHeader())) {
					AppContext
							.showToastShort(R.string.tip_null_direct_person_empty);
					return;
				}
				String text = emojiEditText.getText().toString();
				emojiEditText.getText().clear();  //快速按确定按钮会出现多条回复
				text = text.substring(emojiEditText.getHeader().length());
				emojiEditText.clearHeader();
				int askUid = emojiEditText.getAskUid();
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

				NewsApi.addCommentAfter(id, askUid, uid, comment.getId(), emojiEditText.getId(),
						text, mCommentAfterHandler);
			}
		});

	}

	public void directPerson(String nickname, int askUid, int id) {
		emojiEditText.setHeader("回复" + nickname + "：");
		emojiEditText.setAskUid(askUid);
		emojiEditText.setId(id);
		emojiEditText.setSelection(emojiEditText.getHeader().length());
	}

	public void showLoginView() {
		loginLayout.setVisibility(View.VISIBLE);
		communicatLayout.setVisibility(View.GONE);

		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showLoginView(CommunicatActivity.this);
			}
		});

	}

	private JsonHttpResponseHandler msubHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					reset();
					communicatFragment.adapter.clear();
					communicatFragment.sendRequestData();
					// AppContext.showToast("回答成功");
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
					communicatFragment.adapter.clear();
					communicatFragment.sendRequestData();
					// AppContext.showToast("追问成功");
				} else {
					AppContext.showToast("追问失败");
				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	public void reset() {
		TDevice.hideSoftKeyboard(emojiEditText);
		if (emojiEditText != null) {
			emojiEditText.getText().clear();
			emojiEditText.clearFocus();
			emojiEditText.clearHeader();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		pickView.onActivityResult(requestCode, resultCode, data);
	}
}
