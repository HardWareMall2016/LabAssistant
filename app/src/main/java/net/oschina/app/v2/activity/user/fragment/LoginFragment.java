package net.oschina.app.v2.activity.user.fragment;

import java.io.IOException;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.FindPasswordActivity;
import net.oschina.app.v2.activity.RegistActivity;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

	public static final int REQUEST_CODE_INIT = 0;
	private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
	protected static final String TAG = LoginFragment.class.getSimpleName();
	private static final String LOGIN_SCREEN = "LoginScreen";
	private EditText mEtUserName, mEtPassword;
	private View mIvClearUserName, mIvClearPassword;
	private Button mBtnLogin;
	private int requestCode = REQUEST_CODE_INIT;
	private String mUserName, mPassword;
	private TextView tv_forget_regist;
	private TextView tv_forget_password;
	private CheckBox mCkRemenber;
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
		View view = inflater.inflate(R.layout.v2_fragment_login, container,
				false);
		initView(view);
		setClick();
		System.out.println("test");
		return view;
	}

	// 点击事件
	private void setClick() {
		tv_forget_regist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RegistActivity.class);
				startActivity(intent);
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
				obtainValues();// 获取账号和密码


				NewsApi.loginUser(AppContext.mClientId, nickname, password,
						handler);
				// 发送登陆的post请求
				// AsyncHttpClient client = new AsyncHttpClient();
				// JSONObject jsonObject = new JSONObject();
				// try {
				// jsonObject.put("phone", nickname);
				// jsonObject.put("password", password);
				// entity = new StringEntity(jsonObject.toString());
				// } catch (Exception e) {
				// }
				//
				// // partUrl，请求的url。
				// String partUrl = "index.php/Api/Member/login.html";
				// client.post(AppContext.instance(),
				// ApiHttpClient.getAbsoluteApiUrl(partUrl), entity,
				// "application/json", handler);
				//
				//
			}

			private void obtainValues() {
				nickname = mEtUserName.getText().toString();
				password = mEtPassword.getText().toString();
			}
		});
		tv_forget_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), FindPasswordActivity.class);
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
		public void onFailure(int statusCode,Header[] headers,String msg,Throwable throwable){
			super.onFailure(statusCode, headers, msg, throwable);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				if (response.getString("desc").equals("success")) {

					JSONObject userinfo = new JSONObject(
							response.getString("data"));
					User user;
					try {
						user = User.parse(userinfo);
						// 保存登录信息
						if(mCkRemenber.isChecked()){
							user.setPassword(mEtPassword.getText().toString());
							user.setPhone( mEtUserName.getText().toString());
							AppContext.instance().saveLoginInfo2(user);
						}else{
							user.setOnce(true);
							AppContext.instance().saveLoginInfo2(user);
						}
						// hideWaitDialog();
						handleLoginSuccess();

						AppContext.showToast("登录成功");
					} catch (IOException e) {
						e.printStackTrace();
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					int code = response.getInt("code");
					AppContext.instance().cleanLoginInfo();
					hideWaitDialog();
					AppContext.showToast(response.optString("desc"));

				}

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	};

	// 初始化控件
	private void initView(View view) {
		System.out.println("test");
		// 用户名
		mEtUserName = (EditText) view.findViewById(R.id.et_username);
		// 输入密码
		mEtPassword = (EditText) view.findViewById(R.id.et_password);
		// 登陆按钮
		mBtnLogin = (Button) view.findViewById(R.id.btn_login);
		tv_forget_regist = (TextView) view.findViewById(R.id.tv_forget_regist);
		tv_forget_password=(TextView) view.findViewById(R.id.tv_forget_password);
		
		mEtUserName.addTextChangedListener(mUserNameWatcher);
		mEtPassword.addTextChangedListener(mPassswordWatcher);
		mIvClearUserName = view.findViewById(R.id.iv_clear_username);
		mIvClearUserName.setOnClickListener(this);
		mIvClearPassword = view.findViewById(R.id.iv_clear_password);
		mIvClearPassword.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mCkRemenber = (CheckBox) view.findViewById(R.id.ck_remenber_login);

		String userName = AppContext.getPersistPreferences().getString(
				AppContext.LAST_INPUT_USER_NAME, "");
		mEtUserName.setText(userName);
		
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (id == R.id.iv_clear_username) {
			mEtUserName.getText().clear();
			mEtUserName.requestFocus();
		} else if (id == R.id.iv_clear_password) {
			mEtPassword.getText().clear();
			mEtPassword.requestFocus();
		} else if (id == R.id.btn_login) {
			// handleLogin();
		}else if(id==R.id.tv_forget_password){
			
		}
	}

	// private void handleLogin() {
	// if (!prepareForLogin()) {
	// return;
	// }
	// mUserName = mEtUserName.getText().toString();
	// mPassword = mEtPassword.getText().toString();
	// showWaitDialog(R.string.progress_login);
	// UserApi.login(mUserName, mPassword, mHandler);
	// }

	private boolean prepareForLogin() {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_no_internet);
			return false;
		}
		String uName = mEtUserName.getText().toString();
		if (TextUtils.isEmpty(uName)) {
			AppContext.showToastShort(R.string.tip_please_input_username);
			mEtUserName.requestFocus();
			return false;
		}
		String pwd = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			AppContext.showToastShort(R.string.tip_please_input_password);
			mEtPassword.requestFocus();
			return false;
		}
		return true;
	}

	private void handleLoginSuccess() {
		Editor editor = AppContext.getPersistPreferences().edit();
		editor.putString(AppContext.LAST_INPUT_USER_NAME,  mEtUserName.getText().toString());
		editor.commit();
		
		UIHelper.showLoginView(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(LOGIN_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(LOGIN_SCREEN);
		MobclickAgent.onPause(getActivity());
	}
}
