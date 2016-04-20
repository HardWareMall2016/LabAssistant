package net.oschina.app.v2.activity.user;

import java.io.IOException;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.RegistActivity;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.TDevice;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;
public class LoginActivity extends BaseActivity implements View.OnClickListener {
	public static final int REQUEST_CODE_INIT = 0;
	private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
	protected static final String TAG = LoginActivity.class.getSimpleName();
	private static final String LOGIN_SCREEN = "LoginScreen";
	private EditText mEtUserName, mEtPassword;
	private View mIvClearUserName, mIvClearPassword;
	private Button mBtnLogin;
	private int requestCode = REQUEST_CODE_INIT;
	private String mUserName, mPassword;
	private TextView tv_forget_regist;
	private TextWatcher mUserNameWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			//用户名
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

	protected int getLayoutId() {
		return R.layout.v2_fragment_login;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.login;
	}
	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		Intent data = getIntent();
		if (data != null) {
			requestCode = data.getIntExtra(BUNDLE_KEY_REQUEST_CODE,
					REQUEST_CODE_INIT);
		}
		initViews();
		setClick();
	}
	// 点击事件
	private void setClick() {
		tv_forget_regist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
				//UIHelper.showInterest(LoginActivity.this);
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
				NewsApi.loginUser(AppContext.mClientId, nickname,password,handler);
			}
			private void obtainValues() {
				nickname = mEtUserName.getText().toString();
				password = mEtPassword.getText().toString();
			}
		});
/*		
*/	}
	//json响应的handle
	JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			//请求失败则解析errorResponse，返回错误信息给用户
		}
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
			if(response.getInt("code")==88)
			{
				JSONObject userinfo = new JSONObject(response.getString("data"));
			    User user;
			    try {
				user = User.parse(userinfo);
				user.setPassword(mEtPassword.getText().toString());
				// 保存登录信息
				AppContext.instance().saveLoginInfo(user);
				//hideWaitDialog();
				handleLoginSuccess();
				  AppContext.showToast("登录成功");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (AppException e) {
				e.printStackTrace();
			}
		   }else
		   {
			   AppContext.instance().cleanLoginInfo();
				hideWaitDialog();
			   AppContext.showToast("登录失败");
		   }
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};
	// 初始化控件
	private void initViews(){
		// 用户名
		mEtUserName = (EditText) findViewById(R.id.et_username);
		// 输入密码
		mEtPassword = (EditText)findViewById(R.id.et_password);
		// 登陆按钮
		mBtnLogin = (Button)findViewById(R.id.btn_login);
		tv_forget_regist = (TextView)findViewById(R.id.tv_forget_regist);
		mEtUserName.addTextChangedListener(mUserNameWatcher);
		mEtPassword.addTextChangedListener(mPassswordWatcher);
		mIvClearUserName =findViewById(R.id.iv_clear_username);
		mIvClearUserName.setOnClickListener(this);
		mIvClearPassword =findViewById(R.id.iv_clear_password);
		mIvClearPassword.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
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
			//handleLogin();
		}
	}
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
		Intent data = new Intent();
		data.putExtra(BUNDLE_KEY_REQUEST_CODE, requestCode);
		setResult(RESULT_OK, data);
		finish();
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(LOGIN_SCREEN);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(LOGIN_SCREEN);
		MobclickAgent.onPause(this);
	}
}
