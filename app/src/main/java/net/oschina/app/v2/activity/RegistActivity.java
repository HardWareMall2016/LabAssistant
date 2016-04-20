package net.oschina.app.v2.activity;

import java.io.IOException;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

/**
 * @author acer 注册的activity
 */
public class RegistActivity extends BaseActivity implements
		View.OnClickListener {
	private Button bt_obtain_cons;
	private ListBaseAdapter adapter;
	private StringEntity entity;
	private EditText et_obatin_verify;
	private EditText et_cons;
	private EditText et_input_password;
	private EditText et_nickname;
	private TextView tv_gender;
	private RadioGroup rg_gender;
	private RadioButton bt_man;
	private RadioButton bt_woman;
	private EditText et_recommender;
	private Button btn_regist;
	private TextView tv_agreement_append;
	public static final int REQUEST_CODE_INIT = 0;
	private int requestCode = REQUEST_CODE_INIT;
	private String verifycode;
	private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";

	private String phone;
	private String password;

	protected int getLayoutId() {
		return R.layout.v2_fragment_regist;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		initComponet();
		setListener();
	}

	// 设置监听
	private void setListener() {

		tv_agreement_append.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(RegistActivity.this,
						YinSiTiaoKuanActivity.class);
				startActivity(intent);

			}
		});

		// 注册按钮的监听
		btn_regist.setOnClickListener(new OnClickListener() {
			// 点击获取所有的值，封装发送post请求，请求注册
			@Override
			public void onClick(View v) {
				// 手机号码
				phone = null;
				if (et_obatin_verify.getText() != null)
					phone = et_obatin_verify.getText().toString();
				else {
					AppContext.showToast("请输入手机号码！");
					return;
				}
				// 验证码
				String verify = null;
				if (et_cons.getText() != null)
					verify = et_cons.getText().toString();
				else {
					AppContext.showToast("请输入验证码！");
					return;
				}
				// 输入密码
				password = null;
				if (et_input_password.getText() != null)
					password = et_input_password.getText().toString();
				else {
					AppContext.showToast("请输入密码！");
					return;
				}
				// 昵称
				String nickname = null;
				if (et_nickname.getText() != null)
					nickname = et_nickname.getText().toString();
				else {
					AppContext.showToast("请输入昵称！");
					return;
				}
				// 性别
				int radioButtonId = rg_gender.getCheckedRadioButtonId();
				// 推荐人
				String tuijian = null;
				if (et_recommender.getText() != null)
					tuijian = et_recommender.getText().toString();
				// 注册协议
				String agreement = null;
				if (tv_agreement_append.getText() != null)
					agreement = tv_agreement_append.getText().toString();
				// 发送一个Post请求，请求注册，在注册之前，验证字段
				// 发送Post请求注册
				
				int sex = 0;
				if (radioButtonId==R.id.bt_man) {
					sex = 1;
				} else {
					sex = 2;
				}

				if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(verify)
						|| TextUtils.isEmpty(password)
						|| TextUtils.isEmpty(nickname)) {
					AppContext.showToast("信息不完整");
					return;
				}

				int tuijianId = 0;
				try {
					tuijianId = Integer.parseInt(tuijian);
				} catch (Exception e) {
					e.printStackTrace();
				}

				NewsApi.registerUser(AppContext.mClientId, nickname, password,
						verify, sex, phone, tuijianId, handler);

			}
		});

		// 获取验证码的按钮监听
		bt_obtain_cons.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击获取验证码
				obtainParams();
				// Toast.makeText(RegistActivity.this, verifycode, 1).show();
				AsyncHttpClient client = new AsyncHttpClient();
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("phone", verifycode);
					entity = new StringEntity(jsonObject.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
				};
				// partUrl，请求的url
				String partUrl = "index.php/Api/Member/sendchecknum.html";
				client.post(AppContext.instance(),
						ApiHttpClient.getAbsoluteApiUrl(partUrl), entity,
						"application/json", handler);
				// 点击验证码延时
				if (!verifycode.matches("1[3|5|7|8|][0-9]{9}")) {
					Toast.makeText(RegistActivity.this,
							R.string.phonenumber_error, Toast.LENGTH_SHORT)
							.show();
				} else {
					// 60000是总的延时的时间，1000是延时的间隔
					TimeCount timeCount = new TimeCount(60000, 1000);
					timeCount.start();
				}
			}

			// 点击获取发送验证码的手机号码
			private void obtainParams() {
				verifycode = et_obatin_verify.getText().toString();
			}
		});

	}

	JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);

			try {
				if (response.getInt("code") == 88) {
					AppContext.showToast("注册成功");

					// 发送登录请求
					NewsApi.loginUser(AppContext.mClientId, phone, password,
							new JsonHttpResponseHandler() {

								@Override
								public void onSuccess(int statusCode,
										Header[] headers, JSONObject response) {
									super.onSuccess(statusCode, headers,
											response);
									try {
										if (response.getString("desc").equals(
												"success")) {

											JSONObject userinfo = new JSONObject(
													response.getString("data"));
											User user;
											try {
												user = User.parse(userinfo);
												// 保存登录信息
												AppContext.instance()
														.saveLoginInfo(user);
												Editor editor = AppContext
														.getPersistPreferences()
														.edit();
												editor.putString(
														AppContext.LAST_INPUT_USER_NAME,
														phone);
												editor.commit();

												UIHelper.showLoginView(RegistActivity.this);

												Intent intent = new Intent(
														RegistActivity.this,
														WoGanXingQuActivity.class);
												
												intent.putExtra("key", 1);
												startActivity(intent);
												finish();

											} catch (IOException e) {
												e.printStackTrace();
											} catch (AppException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}

									} catch (JSONException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

								}
							});

				} else {
					AppContext.instance().cleanLoginInfo();
					AppContext.showToast(response.getString("desc"));

				}

			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	private void handleLoginSuccess() {
		Intent data = new Intent();
		data.putExtra(BUNDLE_KEY_REQUEST_CODE, requestCode);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.register;
	}

	// 拿到控件
	private void initComponet() {
		// 验证码的图片控件
		bt_obtain_cons = (Button) findViewById(R.id.bt_obtain_cons);
		// 手机号码
		et_obatin_verify = (EditText) findViewById(R.id.et_obatin_verify);
		// 验证码输入
		et_cons = (EditText) findViewById(R.id.et_cons);
		// 密码输入
		et_input_password = (EditText) findViewById(R.id.et_input_password);
		// 昵称
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		// 性别的TextView
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		// 性别的按钮组
		rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
		// 性别为男的单选按钮
		bt_man = (RadioButton) findViewById(R.id.bt_man);
		// 性别为女的单选按钮
		bt_woman = (RadioButton) findViewById(R.id.bt_woman);
		// 推荐人按钮
		et_recommender = (EditText) findViewById(R.id.et_recommender);
		// 注册的按钮
		btn_regist = (Button) findViewById(R.id.btn_regist);
		// 追加的协议
		tv_agreement_append = (TextView) findViewById(R.id.tv_agreement_append);
	}

	// 延时的内部类
	private class TimeCount extends CountDownTimer {

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		// 结束的时候调用方法
		@Override
		public void onFinish() {
			// 1.设置按钮点击为true
			bt_obtain_cons.setText("再次验证");
			bt_obtain_cons.setClickable(true);
			bt_obtain_cons
					.setBackgroundResource(R.drawable.common_btn_selector);
		}

		// 延时开始的时候调用的方法
		@SuppressLint("ResourceAsColor")
		@Override
		public void onTick(long millisUntilFinished) {
			bt_obtain_cons.setClickable(false);
			bt_obtain_cons.setBackgroundResource(R.drawable.verify_timecount);
			bt_obtain_cons.setText(millisUntilFinished / 1000 + "秒");
			// bt_obtain_cons.setTextColor(R.color.blue);
		}
	}
}
