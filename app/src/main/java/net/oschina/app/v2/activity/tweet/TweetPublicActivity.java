package net.oschina.app.v2.activity.tweet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.ImageShowerActivity;
import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker.OnMediaPickerListener;
import net.oschina.app.v2.activity.tweet.view.UIScroreView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.emoji.EmojiEditText;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import net.oschina.app.v2.emoji.SupperListActivity;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.popupbuttonlibrary.PopupButton;
import net.oschina.app.v2.model.popupbuttonlibrary.adapter.PopupAdapter;
import net.oschina.app.v2.ui.dialog.CommonDialog;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.ImageUtils;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class TweetPublicActivity extends BaseActivity implements
		OnClickListener, OnClickEmojiListener, SoftKeyboardStateListener {
	private static final String TWEET_PUBLIC_SCREEN = "tweet_public_screen";
	private static final int MAX_TEXT_LENGTH = 200;
	private static final int MIN_TEXT_LENGTH = 10;

	protected static final int STATE_NONE = 0;
	protected static final int STATE_REFRESH = 1;
	protected static final int STATE_LOADMORE = 2;
	protected int mState = STATE_NONE;

	private int reward;
	private String superlist = "";
	private Ask ask;

	private ExtendMediaPicker mPicker;
	private TextView tv_myjifen;
	private UIScroreView mScoreView;
	private PopupButton mButton;
	// private CirclePageIndicator mIndicator;
	private ImageButton mIbEmoji, mIbPicture, mIbMention;
	private TextView mTvClear;
	private View mLyEmoji, mLyImage;
	private ImageView mIvImage;
	private EmojiViewPagerAdapter mPagerAdapter;
	private SoftKeyboardStateHelper mKeyboardHelper;
	private boolean mIsKeyboardVisible;
	TextView textSupperlist;
	private ImageView imageXuanshang;
	TextView textXuanshang;
	private boolean mNeedHideEmoji;
	private EmojiEditText mEtInput;
	private TextView mEtInputCount;
	private LinearLayout popup_ll;
	private int mCurrentKeyboardHeigh;
	List<Mulu> pList = new ArrayList<Mulu>();
	HashMap<Integer, UserBean> userList;
	// final List<List<String>> cList = new ArrayList<List<String>>();
	private String theLarge, theThumbnail;
	private File imgFile;
	private Mulu _selectMulu = null;
	private Mulu pMulu=null;//一级分类
	
	private LayoutInflater inflater;
	private List<Mulu> cValues = new ArrayList<Mulu>();

	PopupAdapter pAdapter = null;
	PopupAdapter cAdapter = null;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				// 显示图片
				mIvImage.setImageBitmap((Bitmap) msg.obj);
				mLyImage.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	protected void onBeforeSetContentLayout() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.v2_fragment_tweet_public;
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TWEET_PUBLIC_SCREEN);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TWEET_PUBLIC_SCREEN);
		MobclickAgent.onPause(this);
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);

		// int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
		// | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		// this.getWindow().setSoftInputMode(mode);

		mPicker = new ExtendMediaPicker(this);
		if(savedInstanceState!=null&&savedInstanceState.get("imagePath")!=null){
			mPicker.setImagePath(savedInstanceState.get("imagePath").toString());
		}
		mPicker.setOnMediaPickerListener(new OnMediaPickerListener() {
			@Override
			public void onSelectedMediaChanged(final String mediaUri) {
				imgFile = new File(mediaUri);

				final String uri = mediaUri;
				try {
					int width = DeviceUtils
							.dip2px(TweetPublicActivity.this, 42);
					Bitmap source = BitmapFactory.decodeFile(mediaUri);
					Bitmap bitmap = ThumbnailUtils.extractThumbnail(source,
							width, width);
					final Bitmap showBigBitmap = ThumbnailUtils
							.extractThumbnail(source, source.getHeight() / 3,
									source.getWidth() / 3);
					mIvImage.setImageBitmap(bitmap);
					mLyImage.setVisibility(View.VISIBLE);
					mIvImage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(TweetPublicActivity.this,
									ImageShowerActivity.class);
							//intent.putExtra("bitmap", showBigBitmap);
							intent.putExtra("imagePath", mediaUri);
							TweetPublicActivity.this.startActivity(intent);
						}
					});

					if (!source.isRecycled()) {
						source.recycle();
						source = null;
					}
				} catch (Exception e) {

				}
			}
		});

		initView();
		// View view =
		// LayoutInflater.from(this).inflate(R.layout.myanswer_detail,
		// null);
		// mKeyboardHelper = new SoftKeyboardStateHelper(
		// view.findViewById(R.id.container));
		// mKeyboardHelper.addSoftKeyboardStateListener(this);

	}
	

	private void initView() {
		textSupperlist = (TextView) findViewById(R.id.tv_superlist);
		textXuanshang = (TextView) findViewById(R.id.tv_xuanshang);
		imageXuanshang = (ImageView) findViewById(R.id.iv_xuanshang);

		popup_ll = (LinearLayout) findViewById(R.id.ll_separete);
		mButton = (PopupButton) findViewById(R.id.ib_separate);
		mButton.setContainer(popup_ll);

		mScoreView = (UIScroreView) findViewById(R.id.item_scoreview);
		final Integer[] data = { 0, 5, 10, 20, 50, 80, 100, 150 };
		User user = AppContext.instance().getLoginInfo();
		int jifen=0;
		if (user != null) {
			jifen = user.getIntegral();
		}
		mScoreView.updateList(Arrays.asList(data),jifen);
		mScoreView
				.setOnItemClickListener(new UIScroreView.OnItemClickListener() {
					@Override
					public void onItemClick(int score) {
						User user = AppContext.instance().getLoginInfo();
						int jifen=1000000;
						if (user != null) {
							jifen = user.getIntegral();
						}
						if(jifen>=score){
							reward = score;
							tv_myjifen.setText(TweetPublicActivity.this.getString(R.string.tv_wodejifen, jifen-score));
							mScoreView.updateList(Arrays.asList(data),jifen-score);
							textXuanshang.setText(score + "分");
							imageXuanshang.setVisibility(View.VISIBLE);
							tryHideEmojiPanel();
						}else{
							AppContext.showToast("亲，您的积分不够！");
						}
					}
				});

		tv_myjifen = (TextView) findViewById(R.id.tv_myjifen);
		
		tv_myjifen.setText(this.getString(R.string.tv_wodejifen, jifen));

		mLyEmoji = findViewById(R.id.ly_emoji);
		mIbEmoji = (ImageButton) findViewById(R.id.ib_emoji_keyboard);
		mIbEmoji.setOnClickListener(this);
		mIbPicture = (ImageButton) findViewById(R.id.ib_picture);
		mIbPicture.setOnClickListener(this);
		mIbMention = (ImageButton) findViewById(R.id.ib_mention);
		mIbMention.setOnClickListener(this);
		mTvClear = (TextView) findViewById(R.id.tv_clear);
		mTvClear.setOnClickListener(this);
		mTvClear.setText(String.valueOf(MAX_TEXT_LENGTH));
		//mTvClear.setVisibility(View.VISIBLE);
		mLyImage = findViewById(R.id.rl_img);
		mIvImage = (ImageView) findViewById(R.id.iv_img);
		findViewById(R.id.iv_clear_img).setOnClickListener(this);
		mButton.setOnClickListener(this);
		
		mEtInputCount=(TextView)findViewById(R.id.numberCount);
		mEtInputCount.setText(String.valueOf(MAX_TEXT_LENGTH));
		mEtInput = (EmojiEditText) findViewById(R.id.et_content);
		mEtInput.addTextChangedListener(new SimpleTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!AppContext.instance().isLogin()) {
					UIHelper.showLoginView(TweetPublicActivity.this);
				} else {
					mEtInputCount.setText((MAX_TEXT_LENGTH - s.length()) + "");
				}
			}
		});
		mEtInput.setText(AppContext.getTweetDraft());
		mEtInput.setSelection(mEtInput.getText().toString().length());
		mEtInput.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					tryHideEmojiPanel();
				}
			}
		});
		mEtInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tryHideEmojiPanel();
			}
		});
		
	
	//	mEtInputCount.setOnClickListener(this);

		inflater = LayoutInflater.from(this);
		View view2 = inflater.inflate(R.layout.popup, null);
		ListView pLv = (ListView) view2.findViewById(R.id.parent_lv);
		final ListView cLv = (ListView) view2.findViewById(R.id.child_lv);

		// cValues.addAll(cList.get(0));
		 pAdapter = new PopupAdapter(this,R.layout.popup_item,pList,R.color.white,R.color.grey_selected_normal);
	        cAdapter = new PopupAdapter(this,R.layout.popup_item,cValues,R.color.grey_selected_normal,R.color.grey_selected_normal);
	       
		pAdapter.setPressPostion(0);
		pAdapter.isNeedLeft(true);
		
		pLv.setAdapter(pAdapter);
		cLv.setAdapter(cAdapter);

		pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pAdapter.setPressPostion(position);
				pAdapter.notifyDataSetChanged();
				cValues.clear();
				// cValues.addAll(cList.get(position));
				pMulu=pList.get(position);
				sendRequestLanmuChildData(pList.get(position).getId());
				cLv.setSelection(0);
			}
		});

		cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				cAdapter.setPressPostion(position);
				cAdapter.notifyDataSetChanged();
				_selectMulu = cValues.get(position);
				if(pMulu==null){
					pMulu=pList.get(0);
				}
				mButton.setText(pMulu.getcatname()+" "+_selectMulu.getcatname());
				
				mButton.hidePopup();
			}
		});
		mButton.setPopupView(view2);
		sendRequestLanmuData();
		

	}

	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = this
				.inflateView(R.layout.v2_actionbar_custom_back_title_btn);

		View back = view.findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		View tv_back_title = view.findViewById(R.id.tv_back_title);
		tv_back_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		TextView mTvActionTitle = (TextView) view
				.findViewById(R.id.tv_actionbar_title);
		mTvActionTitle.setText("提问");

		TextView rightMore = (TextView) view
				.findViewById(R.id.tv_actionbar_right_more);

		rightMore.setText("提交");

		rightMore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!AppContext.instance().isLogin()) {
					AppContext.showToast("请先登录");

					// Intent intent = new
					// Intent(MallActivity.this,MainActivity.class);
					// startActivity(intent);
					return;
				}
				showWaitDialog();
				handleSubmit();

			}
		});

		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}

	private void handleSubmit() {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_network_error);
			return;
		}
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLogin(this);
			return;
		}
		String content = mEtInput.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			mEtInput.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (content.length() > MAX_TEXT_LENGTH) {
			AppContext.showToastShort(R.string.tip_content_too_long);
			return;
		}
		if (content.length() < MIN_TEXT_LENGTH) {
			AppContext.showToastShort(R.string.tip_content_too_short);
			return;
		}
		if (_selectMulu == null) {
			AppContext.showToastShort(R.string.tip_label_select);
			return;
		}

		if (imgFile != null && imgFile.exists()) {
			uploadImage(content, imgFile.getAbsolutePath());
		} else {
			submitQuestionData(AppContext.instance().getLoginUid(),
					_selectMulu.getId(), _selectMulu.getcatname(), content, "");
		}

		if (mIsKeyboardVisible) {
			TDevice.hideSoftKeyboard(this.getCurrentFocus());
		}
		//this.finish();
	}

	private void uploadImage(final String content, final String imagePath) {
		NewsApi.uploadImage(2, imagePath, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				try {
					hideWaitDialog();
					switch (msg.what) {
					case 1:
						String returnStr = msg.getData().getString("return");
						JSONObject jsonObject = new JSONObject(returnStr);
						String imageUrl = jsonObject.getString("url");
						submitQuestionData(AppContext.instance().getLoginUid(),
								_selectMulu.getId(), _selectMulu.getcatname(),
								content, imageUrl);
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

	protected void submitQuestionData(int uid, int catid, String label,
			String content, String image) {
		// NewsApi.submitQuestion(uid,catid,label,content,imagefile,mQuestionHander);
		NewsApi.submitQuestion(uid, catid, label, content, reward, superlist,
				image, mQuestionHander);
		ask = new Ask();
		ask.setContent(content);
		ask.setImage(image);
		ask.setLabel(label);
		ask.setreward(reward);
		ask.setUid(AppContext.instance().getLoginUid());
	}

	private JsonHttpResponseHandler mQuestionHander = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				hideWaitDialog();
				if (response.getInt("code") == 88) {
					//AppContext.showToast("提问成功！");
					String string = response.getString("data");
					JSONObject jsonObj = new JSONObject(string);
					ask.setId(jsonObj.optInt("id"));
					ask.setinputtime("刚刚");
					NewsApi.refreshScore(AppContext.instance().getLoginUid(), mRefreshScoreHandler);
					/*
					UIHelper.showTweetDetail(TweetPublicActivity.this, ask);*/
					TweetPublicActivity.this.finish();
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
	
	private JsonHttpResponseHandler mRefreshScoreHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					AppContext.showToast("提问成功！");
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
					AppContext.instance().getLoginInfo().setIntegral(integral);
					UIHelper.showTweetDetail(TweetPublicActivity.this, ask);
					
					
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

	protected void sendRequestLanmuData() {
		NewsApi.getLanmu(-1, mLanmuHandler);
	}

	protected void sendRequestLanmuChildData(int id) {
		NewsApi.getLanmu(id, mLanmuChildHandler);
	}

	private JsonHttpResponseHandler mLanmuHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				MuluList list = MuluList.parse(response.toString());
				for (int i = 0; i < list.getMululist().size(); i++) {
					pList.add(list.getMululist().get(i));

				}
				
				pAdapter.setPressPostion(0);
				pAdapter.notifyDataSetChanged();
				cValues.clear();
				// cValues.addAll(cList.get(position));
				sendRequestLanmuChildData(pList.get(0).getId());
				
				// executeOnLoadCommentDataSuccess(list);
			} catch (Exception e) {
				e.printStackTrace();

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
	private JsonHttpResponseHandler mLanmuChildHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				MuluList list = MuluList.parse(response.toString());
				for (int i = 0; i < list.getMululist().size(); i++) {
					cValues.add(list.getMululist().get(i));
				}
				cAdapter.notifyDataSetChanged();
				cAdapter.setPressPostion(-1);
				// executeOnLoadCommentDataSuccess(list);
			} catch (Exception e) {
				e.printStackTrace();

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

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.ib_emoji_keyboard) {
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEtInput.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			if (mLyEmoji.getVisibility() == View.GONE) {
				mNeedHideEmoji = true;
				tryShowEmojiPanel();
			} else {
				tryHideEmojiPanel();
			}
		} else if (id == R.id.ib_picture) {
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEtInput.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			
			// handleSelectPicture();
			mPicker.showPickerView();
		} else if (id == R.id.ib_mention) {
			// insertMention();
			Intent intent = new Intent(this, SupperListActivity.class);
			intent.putExtra("supperList", userList);

			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mEtInput.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			startActivityForResult(intent, 1000);
		} else if (id == R.id.tv_clear||id==R.id.numberCount) {
			handleClearWords();
		} else if (id == R.id.iv_clear_img) {
			mIvImage.setImageBitmap(null);
			mLyImage.setVisibility(View.GONE);
			imgFile = null;
		} else if (id == R.id.ib_separate) {
			sendRequestLanmuData();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		try {
			outState.putString("imagePath", mPicker.getImagePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		try {
			if(mPicker!=null){
				mPicker.setImagePath(savedInstanceState.getString("imagePath").toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent imageReturnIntent) {
		if (imageReturnIntent != null) {
			userList = (HashMap<Integer, UserBean>) imageReturnIntent
					.getSerializableExtra("supperList");
			if (userList != null) {
				StringBuilder sb = new StringBuilder();
				StringBuilder builder = new StringBuilder();
				for (Map.Entry<Integer, UserBean> entry : userList.entrySet()) {
					UserBean bean = entry.getValue();
					builder.append(bean.getId() + "-");
					sb.append("@" + bean.getNickname() + ";");
				}
				if (builder.length() > 0) {
					builder = builder.deleteCharAt(builder.length() - 1);
				}

				this.superlist = builder.toString();
				textSupperlist.setText(sb.toString());
			}
		}

		mPicker.onActivityResult(requestCode, resultCode, imageReturnIntent);

		/*
		 * new Thread() { private String selectedImagePath;
		 * 
		 * public void run() { Bitmap bitmap = null;
		 * 
		 * if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) { if
		 * (imageReturnIntent == null) return; Uri selectedImageUri =
		 * imageReturnIntent.getData(); if (selectedImageUri != null) {
		 * selectedImagePath = ImageUtils.getImagePath( selectedImageUri,
		 * getActivity()); }
		 * 
		 * if (selectedImagePath != null) { theLarge = selectedImagePath; } else
		 * { bitmap = ImageUtils.loadPicasaImageFromGalley( selectedImageUri,
		 * getActivity()); }
		 * 
		 * if (AppContext
		 * .isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)) { String
		 * imaName = FileUtils.getFileName(theLarge); if (imaName != null)
		 * bitmap = ImageUtils.loadImgThumbnail(getActivity(), imaName,
		 * MediaStore.Images.Thumbnails.MICRO_KIND); } if (bitmap == null &&
		 * !StringUtils.isEmpty(theLarge)) bitmap = ImageUtils
		 * .loadImgThumbnail(theLarge, 100, 100); } else if (requestCode ==
		 * ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) { // 拍摄图片 if (bitmap ==
		 * null && !StringUtils.isEmpty(theLarge)) { bitmap = ImageUtils
		 * .loadImgThumbnail(theLarge, 100, 100); } }
		 * 
		 * if (bitmap != null) {// 存放照片的文件夹 String savePath =
		 * Environment.getExternalStorageDirectory() .getAbsolutePath() +
		 * "/OSChina/Camera/"; File savedir = new File(savePath); if
		 * (!savedir.exists()) { savedir.mkdirs(); }
		 * 
		 * String largeFileName = FileUtils.getFileName(theLarge); String
		 * largeFilePath = savePath + largeFileName; // 判断是否已存在缩略图 if
		 * (largeFileName.startsWith("thumb_") && new
		 * File(largeFilePath).exists()) { theThumbnail = largeFilePath; imgFile
		 * = new File(theThumbnail); } else { // 生成上传的800宽度图片 String
		 * thumbFileName = "thumb_" + largeFileName; theThumbnail = savePath +
		 * thumbFileName; if (new File(theThumbnail).exists()) { imgFile = new
		 * File(theThumbnail); } else { try { // 压缩上传的图片
		 * ImageUtils.createImageThumbnail(getActivity(), theLarge,
		 * theThumbnail, 800, 80); imgFile = new File(theThumbnail); } catch
		 * (IOException e) { e.printStackTrace(); } } } // 保存动弹临时图片 //
		 * ((AppContext) getApplication()).setProperty( // tempTweetImageKey,
		 * theThumbnail);
		 * 
		 * Message msg = new Message(); msg.what = 1; msg.obj = bitmap;
		 * handler.sendMessage(msg); } }; }.start();
		 */
	}

	private void handleClearWords() {
		if (TextUtils.isEmpty(mEtInput.getText().toString()))
			return;
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(this);
		dialog.setMessage(R.string.clearwords);
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mEtInput.getText().clear();
						if (mIsKeyboardVisible) {
							TDevice.showSoftKeyboard(mEtInput);
							// TDevice.hideSoftKeyboard(mEtInput);
						}
					}
				});
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.show();
	}

	private void handleSelectPicture() {
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(this);
		dialog.setTitle(R.string.choose_picture);
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.setItemsWithoutChk(
				getResources().getStringArray(R.array.choose_picture),
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						dialog.dismiss();
						goToSelectPicture(position);
					}
				});
		dialog.show();
	}

	private void goToSelectPicture(int position) {
		switch (position) {
		case 0:
			Intent intent;
			if (Build.VERSION.SDK_INT < 19) {
				intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "选择图片"),
						ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
			} else {
				intent = new Intent(Intent.ACTION_PICK,
						Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "选择图片"),
						ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
			}
			break;
		case 1:
			// 判断是否挂载了SD卡
			String savePath = "";
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/OSC-Happy/Camera/";
				File savedir = new File(savePath);
				if (!savedir.exists()) {
					savedir.mkdirs();
				}
			}

			// 没有挂载SD卡，无法保存文件
			if (StringUtils.isEmpty(savePath)) {
				AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
				return;
			}

			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
					.format(new Date());
			String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
			File out = new File(savePath, fileName);
			Uri uri = Uri.fromFile(out);

			theLarge = savePath + fileName;// 该照片的绝对路径

			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent,
					ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
			break;
		default:
			break;
		}
	}

	private void tryShowEmojiPanel() {
		if (mIsKeyboardVisible) {
			// TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
		} else {
			showEmojiPanel();
		}
	}

	private void showEmojiPanel() {
		mNeedHideEmoji = false;
		mLyEmoji.setVisibility(View.VISIBLE);
	}

	private void tryHideEmojiPanel() {
		if (!mIsKeyboardVisible) {
			// TDevice.showSoftKeyboard(mEtInput);
		}
		hideEmojiPanel();
	}

	private void hideEmojiPanel() {
		if (mLyEmoji.getVisibility() == View.VISIBLE) {
			mLyEmoji.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSoftKeyboardOpened(int keyboardHeightInPx) {
		int realKeyboardHeight = keyboardHeightInPx
				- TDevice.getStatusBarHeight();
		AppContext.setSoftKeyboardHeight(realKeyboardHeight);
		mIsKeyboardVisible = true;
		hideEmojiPanel();
	}

	@Override
	public void onSoftKeyboardClosed() {
		mIsKeyboardVisible = false;
		if (mNeedHideEmoji) {
			showEmojiPanel();
		}
	}

	@Override
	public void onEmojiClick(Emoji emoji) {
		mEtInput.insertEmoji(emoji);
	}

	@Override
	public void onDelete() {
		mEtInput.delete();
	}

}
