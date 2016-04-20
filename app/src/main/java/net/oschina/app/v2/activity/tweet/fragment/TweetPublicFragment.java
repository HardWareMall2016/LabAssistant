package net.oschina.app.v2.activity.tweet.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker.OnMediaPickerListener;
import net.oschina.app.v2.activity.tweet.view.UIScroreView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.emoji.EmojiEditText;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import net.oschina.app.v2.emoji.SupperListActivity;
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

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

/*SoftKeyboardStateListener*/
public class TweetPublicFragment extends BaseFragment implements
		OnClickEmojiListener,SoftKeyboardStateListener {
	private static final int MAX_TEXT_LENGTH = 160;
	private static final String TEXT_ATME = "@请输入用户名 ";
	private static final String TEXT_SOFTWARE = "#请输入软件名#";
	private static final String TWEET_PUBLIC_SCREEN = "tweet_public_screen";

	private int reward;
	private String superlist="";
	
	private ExtendMediaPicker mPicker;
	private TextView tv_myjifen;
	private UIScroreView mScoreView;
	private PopupButton mButton;
	//private CirclePageIndicator mIndicator;
	private ImageButton mIbEmoji, mIbPicture, mIbMention;
	private TextView mTvClear;
	private View mLyEmoji, mLyImage;
	private ImageView mIvImage;
	private EmojiViewPagerAdapter mPagerAdapter;
	private SoftKeyboardStateHelper mKeyboardHelper;
	private boolean mIsKeyboardVisible;
	TextView textSupperlist;
	TextView textXuanshang;
	private boolean mNeedHideEmoji;
	private EmojiEditText mEtInput;
	private int mCurrentKeyboardHeigh;
    List<Mulu> pList = new ArrayList<Mulu>();
//    final List<List<String>> cList = new ArrayList<List<String>>();
	private String theLarge, theThumbnail;
	private File imgFile;
    private Mulu _selectMulu=null;
	   private LayoutInflater inflater;
	    private List<Mulu> cValues  = new ArrayList<Mulu>();
	    
	    PopupAdapter pAdapter=null;
	    PopupAdapter cAdapter=null;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		getActivity().getWindow().setSoftInputMode(mode);
		
		mPicker=new ExtendMediaPicker(getActivity());
		mPicker.setOnMediaPickerListener(new OnMediaPickerListener() {
			@Override
			public void onSelectedMediaChanged(String mediaUri) {
				imgFile=new File(mediaUri);
				
				try {
					int width=DeviceUtils.dip2px(getActivity(), 42);
					Bitmap source=BitmapFactory.decodeFile(mediaUri);
					Bitmap bitmap=ThumbnailUtils.extractThumbnail(source, width, width);
					mIvImage.setImageBitmap(bitmap);
					mLyImage.setVisibility(View.VISIBLE);
					
					if (!source.isRecycled()) {
						source.recycle();
						source=null;
					}
				} catch (Exception e) {
					
				}
			}
		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.public_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.public_menu_send:
			handleSubmit();
			break;
		default:
			break;
		}
		return true;
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
		if (_selectMulu==null) {
			AppContext.showToastShort(R.string.tip_label_select);
			return;
		}

		if (imgFile!=null && imgFile.exists()) {
			uploadImage(content, imgFile.getAbsolutePath());
		} else {
			submitQuestionData(
					AppContext.instance().getLoginUid(),
					_selectMulu.getId(),_selectMulu.
					getcatname(),content, "");
		}
		
		if (mIsKeyboardVisible) {
			TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
		}
		getActivity().finish();
	}
	
	private void uploadImage(final String content, final String imagePath) {
		NewsApi.updateImage(imagePath, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				if (response.optInt("code")==88) {
					String head=response.optString("url");
					submitQuestionData(AppContext.instance().getLoginUid(),
							_selectMulu.getId(),_selectMulu.
							getcatname(),content, head);
				} else {
					AppContext.showToast("修改图像失败");
				}
			}
		});
	}
	
	protected void submitQuestionData(int uid,int catid ,String label,String content,String image) {
		//NewsApi.submitQuestion(uid,catid,label,content,imagefile,mQuestionHander);
		NewsApi.submitQuestion(uid, catid, label, content, reward, superlist, image, mQuestionHander);
	}
	
	private JsonHttpResponseHandler mQuestionHander = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
				if(response.getInt("code")==88)
				{
			       AppContext.showToast("提问成功！");
			     }else
			     {
				   AppContext.showToast(response.getString("desc"));
                 }
				
				} catch (JSONException e1) {
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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_tweet_public,
				container, false);

		initView(view);
		mKeyboardHelper = new SoftKeyboardStateHelper(getActivity()
				.findViewById(R.id.container));
		mKeyboardHelper.addSoftKeyboardStateListener(this);
		return view;
	}

	private void initView(View view) {
		 textSupperlist=(TextView) view.findViewById(R.id.tv_superlist);
		 textXuanshang=(TextView)view.findViewById(R.id.tv_xuanshang);
		
		mButton=(PopupButton) view.findViewById(R.id.ib_separate);
		
		mScoreView=(UIScroreView) view.findViewById(R.id.item_scoreview);
		Integer[] data={0,5,10,20,50,80,100,150};
		User user = AppContext.instance().getLoginInfo();
		int jifen=0;
		if (user != null) {
			jifen = user.getIntegral();
		}
		
		mScoreView.updateList(Arrays.asList(data),jifen);
		mScoreView.setOnItemClickListener(new UIScroreView.OnItemClickListener() {
			@Override
			public void onItemClick(int score) {
				reward = score;
				textXuanshang.setText(score+"分");
				tryHideEmojiPanel();
			}
		});
		
		tv_myjifen=(TextView) view.findViewById(R.id.tv_myjifen);
		 
		tv_myjifen.setText(getActivity().getString(R.string.tv_wodejifen, jifen));
		
		mLyEmoji = view.findViewById(R.id.ly_emoji);
		mIbEmoji = (ImageButton) view.findViewById(R.id.ib_emoji_keyboard);
		mIbEmoji.setOnClickListener(this);
		mIbPicture = (ImageButton) view.findViewById(R.id.ib_picture);
		mIbPicture.setOnClickListener(this);
		mIbMention = (ImageButton) view.findViewById(R.id.ib_mention);
		mIbMention.setOnClickListener(this);
		mTvClear = (TextView) view.findViewById(R.id.tv_clear);
		mTvClear.setOnClickListener(this);
		mTvClear.setText(String.valueOf(MAX_TEXT_LENGTH));
		mLyImage = view.findViewById(R.id.rl_img);
		mIvImage = (ImageView) view.findViewById(R.id.iv_img);
		view.findViewById(R.id.iv_clear_img).setOnClickListener(this);
		mButton.setOnClickListener(this);
		mEtInput = (EmojiEditText) view.findViewById(R.id.et_content);
		mEtInput.addTextChangedListener(new SimpleTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!AppContext.instance().isLogin()) {
					UIHelper.showLoginView(getActivity());
				} else {
					mTvClear.setText((MAX_TEXT_LENGTH - s.length()) + "");
				}
			}
		});
		mEtInput.setText(AppContext.getTweetDraft());
		mEtInput.setSelection(mEtInput.getText().toString().length());
		
		inflater = LayoutInflater.from(getActivity());        
        View view2 = inflater.inflate(R.layout.popup,null);
        ListView pLv = (ListView) view2.findViewById(R.id.parent_lv);
        final ListView cLv = (ListView) view2.findViewById(R.id.child_lv);
      
      //  cValues.addAll(cList.get(0));
        pAdapter = new PopupAdapter(getActivity(),R.layout.popup_item,pList,R.color.white,R.color.grey_selected_normal);
        cAdapter = new PopupAdapter(getActivity(),R.layout.popup_item,cValues,R.color.grey_selected_normal,R.color.grey_selected_normal);
        pAdapter.setPressPostion(0);

        pLv.setAdapter(pAdapter);
        cLv.setAdapter(cAdapter);

        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pAdapter.setPressPostion(position);
                pAdapter.notifyDataSetChanged();
                cValues.clear();
               // cValues.addAll(cList.get(position));
                sendRequestLanmuChildData(pList.get(position).getId());
                cLv.setSelection(0);
            }
        });

        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cAdapter.setPressPostion(position);
                cAdapter.notifyDataSetChanged();
                mButton.setText(cValues.get(position).getcatname());
                _selectMulu=cValues.get(position);
                mButton.hidePopup();
            }
        });
        mButton.setPopupView(view2);
        sendRequestLanmuData();
	}
	

	protected void sendRequestLanmuData() {
		NewsApi.getLanmu(-1, mLanmuHandler);
	}
	protected void sendRequestLanmuChildData(int id) {
		NewsApi.getLanmu(id, mLanmuChildHandler);
	}
	private JsonHttpResponseHandler mLanmuHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
			 MuluList list = MuluList.parse(response.toString());
			  for(int i = 0; i <list.getMululist().size(); i ++) {
		            pList.add(list.getMululist().get(i));
		     
		        }
//				executeOnLoadCommentDataSuccess(list);
			} catch (Exception e) {
				e.printStackTrace();
				
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
	private JsonHttpResponseHandler mLanmuChildHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
			 MuluList list = MuluList.parse(response.toString());
			  for(int i = 0; i <list.getMululist().size(); i ++) {
				  cValues.add(list.getMululist().get(i));
		        }
			  cAdapter.notifyDataSetChanged();
              cAdapter.setPressPostion(-1);
//				executeOnLoadCommentDataSuccess(list);
			} catch (Exception e) {
				e.printStackTrace();
				
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
	public boolean onBackPressed() {
		if (mLyEmoji.getVisibility() == View.VISIBLE) {
			hideEmojiPanel();
			return true;
		}
		final String tweet = mEtInput.getText().toString();
		if (!TextUtils.isEmpty(tweet)) {
			CommonDialog dialog = DialogHelper
					.getPinterestDialogCancelable(getActivity());
			dialog.setMessage(R.string.draft_tweet_message);
			dialog.setNegativeButton(R.string.cancle, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppContext.setTweetDraft("");
					getActivity().finish();
				}
			});
			dialog.setPositiveButton(R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					AppContext.setTweetDraft(tweet);
					getActivity().finish();
				}
			});
			dialog.show();
			return true;
		}
		return super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.ib_emoji_keyboard) {
			if (mLyEmoji.getVisibility() == View.GONE) {
				mNeedHideEmoji = true;
				tryShowEmojiPanel();
			} else {
				tryHideEmojiPanel();
			}
		} else if (id == R.id.ib_picture) {
			//handleSelectPicture();
			mPicker.showPickerView();
		} else if (id == R.id.ib_mention) {
			//insertMention();
			startActivityForResult(new Intent(getActivity(), SupperListActivity.class), 1000);
		} else if (id == R.id.tv_clear) {
			handleClearWords();
		} else if (id == R.id.iv_clear_img) {
			mIvImage.setImageBitmap(null);
			mLyImage.setVisibility(View.GONE);
			imgFile = null;
		}else if (id == R.id.ib_separate) {
			sendRequestLanmuData();
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent imageReturnIntent) {
		if (imageReturnIntent!=null) {
				HashMap<Integer, UserBean> userList=(HashMap<Integer, UserBean>) imageReturnIntent.getSerializableExtra("supperList");
				if (userList!=null) {
					StringBuilder sb = new StringBuilder();
					StringBuilder builder=new StringBuilder();
					for (Map.Entry<Integer, UserBean> entry : userList.entrySet()) {
						UserBean bean=entry.getValue();
						builder.append(bean.getId()+"-");
						sb.append("@"+bean.getNickname()+";");
					}
					if (builder.length()>0) {
						builder=builder.deleteCharAt(builder.length()-1);
					}
					
					this.superlist = builder.toString();
					textSupperlist.setText(sb.toString());
				}
		}
		
		mPicker.onActivityResult(requestCode, resultCode, imageReturnIntent);
		
		/*
		new Thread() {
			private String selectedImagePath;

			public void run() {
				Bitmap bitmap = null;

				if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD) {
					if (imageReturnIntent == null)
						return;
					Uri selectedImageUri = imageReturnIntent.getData();
					if (selectedImageUri != null) {
						selectedImagePath = ImageUtils.getImagePath(
								selectedImageUri, getActivity());
					}

					if (selectedImagePath != null) {
						theLarge = selectedImagePath;
					} else {
						bitmap = ImageUtils.loadPicasaImageFromGalley(
								selectedImageUri, getActivity());
					}

					if (AppContext
							.isMethodsCompat(android.os.Build.VERSION_CODES.ECLAIR_MR1)) {
						String imaName = FileUtils.getFileName(theLarge);
						if (imaName != null)
							bitmap = ImageUtils.loadImgThumbnail(getActivity(),
									imaName,
									MediaStore.Images.Thumbnails.MICRO_KIND);
					}
					if (bitmap == null && !StringUtils.isEmpty(theLarge))
						bitmap = ImageUtils
								.loadImgThumbnail(theLarge, 100, 100);
				} else if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA) {
					// 拍摄图片
					if (bitmap == null && !StringUtils.isEmpty(theLarge)) {
						bitmap = ImageUtils
								.loadImgThumbnail(theLarge, 100, 100);
					}
				}

				if (bitmap != null) {// 存放照片的文件夹
					String savePath = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/OSChina/Camera/";
					File savedir = new File(savePath);
					if (!savedir.exists()) {
						savedir.mkdirs();
					}

					String largeFileName = FileUtils.getFileName(theLarge);
					String largeFilePath = savePath + largeFileName;
					// 判断是否已存在缩略图
					if (largeFileName.startsWith("thumb_")
							&& new File(largeFilePath).exists()) {
						theThumbnail = largeFilePath;
						imgFile = new File(theThumbnail);
					} else {
						// 生成上传的800宽度图片
						String thumbFileName = "thumb_" + largeFileName;
						theThumbnail = savePath + thumbFileName;
						if (new File(theThumbnail).exists()) {
							imgFile = new File(theThumbnail);
						} else {
							try {
								// 压缩上传的图片
								ImageUtils.createImageThumbnail(getActivity(),
										theLarge, theThumbnail, 800, 80);
								imgFile = new File(theThumbnail);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					// 保存动弹临时图片
					// ((AppContext) getApplication()).setProperty(
					// tempTweetImageKey, theThumbnail);

					Message msg = new Message();
					msg.what = 1;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}
			};
		}.start();
		*/
	}

	private void handleClearWords() {
		if (TextUtils.isEmpty(mEtInput.getText().toString()))
			return;
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(getActivity());
		dialog.setMessage(R.string.clearwords);
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mEtInput.getText().clear();
						if (mIsKeyboardVisible) {
							TDevice.showSoftKeyboard(mEtInput);
							//TDevice.hideSoftKeyboard(mEtInput);
						}
					}
				});
		dialog.setNegativeButton(R.string.cancle, null);
		dialog.show();
	}

	private void handleSelectPicture() {
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(getActivity());
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
		//	TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
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
			TDevice.showSoftKeyboard(mEtInput);
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

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TWEET_PUBLIC_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TWEET_PUBLIC_SCREEN);
		MobclickAgent.onPause(getActivity());
	}
}
