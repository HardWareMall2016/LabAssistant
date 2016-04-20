package net.oschina.app.v2.activity.settings.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.common.BtnBackActivity;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class EditNicknameFragment extends BaseFragment implements
		SoftKeyboardStateListener, OnClickEmojiListener {

	private static final String SETTINGS_SCREEN = "settings_screen";
	private EditText newname;
	private SoftKeyboardStateHelper mKeyboardHelper;
	private View mTvClear;
	private RelativeLayout tipLayout;
	private ImageView tipClose;
	private String mNickName;
	private TextView mNum;
	private int MAX_TEXT_LENGTH=20;

	private TextWatcher mUserNameWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mTvClear.setVisibility(TextUtils.isEmpty(s) ? View.GONE
					: View.VISIBLE);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_editnickname,
				container, false);
		// mKeyboardHelper = new SoftKeyboardStateHelper(getActivity()
		// .findViewById(R.id.container));
		// mKeyboardHelper.addSoftKeyboardStateListener(this);
		initViews(view);
		((BtnBackActivity) getActivity())
				.setmRightButtonOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						handleSubmit();
					}
				});
		return view;
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.ib_emoji_keyboard) {

		} else if (id == R.id.iv_clear_username) {
			handleClearWords();
		}
	}

	private void handleClearWords() {
		if (TextUtils.isEmpty(newname.getText().toString()))
			return;
		newname.getText().clear();
		// if (mIsKeyboardVisible) {
		// TDevice.showSoftKeyboard(mEtInput);
		// }
	}

	@Override
	public void onDestroyView() {
		// UmengUpdateAgent.setUpdateListener(null);
		super.onDestroyView();
	}
	
	public boolean onBackPressed() {
		TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
		return false;
	}

	private void initViews(View view) {
		tipLayout = (RelativeLayout) view.findViewById(R.id.tip_layout);
		tipClose = (ImageView) view.findViewById(R.id.tip_close);
		tipClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tipLayout.setVisibility(View.GONE);
			}
		});

		newname = (EditText) view.findViewById(R.id.et_username);
		newname.addTextChangedListener(mUserNameWatcher);
		mTvClear = view.findViewById(R.id.iv_clear_username);
		mNum=(TextView)view.findViewById(R.id.num);   //计数
		
		mTvClear.setOnClickListener(this);
		
		newname.addTextChangedListener(new SimpleTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!AppContext.instance().isLogin()) {
					UIHelper.showLoginView(getActivity());
				} else {
					mNum.setText((MAX_TEXT_LENGTH - s.length()) + "");
				}
			}
		});
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		getActivity().getWindow().setSoftInputMode(mode);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// inflater.inflate(R.menu.menu_save, menu);
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
		mNickName = newname.getText().toString().trim();
		if (TextUtils.isEmpty(mNickName)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}

		TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());

		submitEditNickname(AppContext.instance().getLoginUid(), mNickName);

	}

	protected void submitEditNickname(int uid, String content) {
		NewsApi.subEditNickname(uid, content, mJsonHander);
	}

	private JsonHttpResponseHandler mJsonHander = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					JSONObject data = new JSONObject(response.getString("data"));
					AppContext.showToast("修改成功！");
					User user = AppContext.instance().getLoginInfo();
					user.setNickname(mNickName);
					AppContext.instance().saveLoginInfo(user);
					getActivity().finish();
				} else {
					AppContext.showToast(response.getString("desc"));
				}

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
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
