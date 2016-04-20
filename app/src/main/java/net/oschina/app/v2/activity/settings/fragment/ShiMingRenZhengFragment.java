package net.oschina.app.v2.activity.settings.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.TweetPublicActivity;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.event.MessageRefreshSingle;
import net.oschina.app.v2.utils.ActiveNumType;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

public class ShiMingRenZhengFragment extends BaseFragment implements
		SoftKeyboardStateListener, OnClickEmojiListener {

	private static final String SETTINGS_SCREEN = "settings_screen";
	private EditText newname, danwei, zhiwu, shouji, email, shimingrenzheng,
			renzhengxinxi;
	private Button renzheng, show_renzhengshuoming,show_user;
	private SoftKeyboardStateHelper mKeyboardHelper;
	private CheckBox checkBox;
	private RadioButton normalUser, commercialUser;
	private RelativeLayout tipLayout;
	private ImageView tipClose;
	private TextView renzhengNum;
	private int MAX_TEXT_LENGTH=40;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_shimingrenzheng,
				container, false);
		// mKeyboardHelper = new SoftKeyboardStateHelper(getActivity()
		// .findViewById(R.id.container));
		// mKeyboardHelper.addSoftKeyboardStateListener(this);
		initViews(view);
		return view;
	}

	@Override
	public void onDestroyView() {
		// UmengUpdateAgent.setUpdateListener(null);
		super.onDestroyView();
	}

	private void initViews(View view) {
		newname = (EditText) view.findViewById(R.id.et_username);
		danwei = (EditText) view.findViewById(R.id.et_danwei);
		zhiwu = (EditText) view.findViewById(R.id.et_zhiwu);
		shouji = (EditText) view.findViewById(R.id.et_phone);
		email = (EditText) view.findViewById(R.id.et_email);

		checkBox = (CheckBox) view.findViewById(R.id.check_box_ok);
		normalUser = (RadioButton) view.findViewById(R.id.radio_user_01);
		commercialUser = (RadioButton) view.findViewById(R.id.radio_user_02);
		
		renzhengNum=(TextView)view.findViewById(R.id.num);
		
		

		// shimingrenzheng =
		// (EditText)view.findViewById(R.id.et_shimingrenzheng);

		renzhengxinxi = (EditText) view.findViewById(R.id.et_renzhengxinxi);
		renzhengxinxi.addTextChangedListener(new SimpleTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!AppContext.instance().isLogin()) {
					UIHelper.showLoginView(getActivity());
				} else {
					renzhengNum.setText((MAX_TEXT_LENGTH - s.length()) + "");
				}
			}
		});
		
		show_renzhengshuoming = (Button) view
				.findViewById(R.id.show_renzhengshuoming);
		show_user=(Button)view.findViewById(R.id.show_user);
		show_renzhengshuoming.setOnClickListener(this);
		show_user.setOnClickListener(this);
		
		renzheng = (Button) view.findViewById(R.id.subBtn);
		renzheng.setOnClickListener(this);

		User user = AppContext.instance().getLoginInfo();
		
		
		if (user.getRealname_status() == 1) {
			newname.setEnabled(false);
			danwei.setEnabled(false);
			zhiwu.setEnabled(false);
			shouji.setEnabled(false);
			email.setEnabled(false);
			normalUser.setEnabled(false);
			commercialUser.setEnabled(false);
			
			newname.setTextColor(0xFF9A9A9A);
			danwei.setTextColor(0xFF9A9A9A);
			zhiwu.setTextColor(0xFF9A9A9A);
			shouji.setTextColor(0xFF9A9A9A);
			email.setTextColor(0xFF9A9A9A);
			normalUser.setTextColor(0xFF9A9A9A);
			commercialUser.setTextColor(0xFF9A9A9A);

			NewsApi.getRealName(user.getId(), getRealNameHandler);
		}
		
		tipLayout = (RelativeLayout) view.findViewById(R.id.tip_layout);
		tipClose = (ImageView) view.findViewById(R.id.tip_close);
		tipClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tipLayout.setVisibility(View.GONE);
			}
		});
	}

	// json响应的handle
	JsonHttpResponseHandler getRealNameHandler = new JsonHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			AppContext.showToast("认证获取失败！");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					JSONObject data = new JSONObject(response.getString("data"));
					newname.setText(data.optString("realname"));
					shouji.setText(data.optString("phone"));
					danwei.setText(data.optString("company"));
					zhiwu.setText(data.optString("position"));
					email.setText(data.optString("email"));
					renzhengxinxi.setText(data.optString("info"));
					int type = data.optInt("type");
					if(type==0){
						normalUser.setChecked(true);
						commercialUser.setChecked(false);
					}else{
						normalUser.setChecked(false);
						commercialUser.setChecked(true);
					}
				} else {
					AppContext.showToast(response.getString("desc"));
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.subBtn) {
			handleSubmit();
		} else if (id == R.id.show_renzhengshuoming) {
			UIHelper.showRenZhengShuoMing(getActivity());
		}else if (id == R.id.show_user) {
			UIHelper.showUserShuoMing(getActivity());
		}
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
		String realname = newname.getText().toString().trim();
		String danwei_info = danwei.getText().toString().trim();
		String zhiwu_info = zhiwu.getText().toString().trim();
		String shouji_info = shouji.getText().toString().trim();
		String email_info = email.getText().toString().trim();
		String renzhengxinxi_info = renzhengxinxi.getText().toString().trim();

		int type = 0;
		if (normalUser.isChecked()) {
			type = 0;
		} else {
			type = 1;
		}

		if (TextUtils.isEmpty(realname)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (TextUtils.isEmpty(danwei_info)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (TextUtils.isEmpty(zhiwu_info)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (TextUtils.isEmpty(shouji_info)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (TextUtils.isEmpty(email_info)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (TextUtils.isEmpty(renzhengxinxi_info)) {
			newname.requestFocus();
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if(renzhengxinxi_info.length()>20){
			renzhengxinxi.requestFocus();
			AppContext.showToastShort("认证信息过长，不得超过20个字！");
			return;
		}

		if (!checkBox.isChecked()) {
			AppContext.showToastShort("请勾选已阅读认证说明");
			checkBox.requestFocus();
			return;
		}

		int uid = AppContext.instance().getLoginUid();
		NewsApi.subShiMingRenZhen(uid, realname, danwei_info, zhiwu_info,
				shouji_info, email_info, shouji_info,renzhengxinxi_info, type, mJsonHander);
	}

	private JsonHttpResponseHandler mJsonHander = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					JSONObject data = new JSONObject(response.getString("data"));
				//	AppContext.showToast("认证成功！");
					AppContext.showToast("已提交，等待工作人员处理！");
					// AppContext.instance().saveNickname(data.getString("nickname"));
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
