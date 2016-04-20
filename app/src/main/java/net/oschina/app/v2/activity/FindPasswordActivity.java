package net.oschina.app.v2.activity;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

/**
 * @author acer
 * 跳转到页面找回的界面
 */
public class FindPasswordActivity extends BaseActivity {

	private StringEntity entity;
	
	private Button bt_obtain_cons;
	private EditText et_obatin_verify;
	private EditText et_cons;
	private EditText et_input_password;
	private Button btn_regist;
	
	public static final int REQUEST_CODE_INIT = 0;
	private int requestCode = REQUEST_CODE_INIT;
	private String verifycode;
	private static final String BUNDLE_KEY_REQUEST_CODE = "BUNDLE_KEY_REQUEST_CODE";
	
	protected int getLayoutId() {
		return R.layout.v2_activity_findpassword;
	}

	
	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		initComponet();
		setListener();
	}
		//设置监听
		private void setListener() {
		
	
			
		//注册按钮的监听
		btn_regist.setOnClickListener(new OnClickListener() {
			//点击获取所有的值，封装发送post请求，请求注册
			@Override
			public void onClick(View v) {
			//手机号码
			String phone=et_obatin_verify.getText().toString().trim();
			//验证码
			String verify=et_cons.getText().toString().trim();
			//输入密码
			String password=et_input_password.getText().toString().trim();
			
			if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(verify) || TextUtils.isEmpty(password))
			{
				AppContext.showToast("信息填写不完整");
				return;
			}
			
			NewsApi.findPassword(phone,verify,password,handler);
			
			}
	});
		
		
		//获取验证码的按钮监听
		bt_obtain_cons.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击获取验证码
				obtainParams();
				AsyncHttpClient client=new AsyncHttpClient();
				JSONObject jsonObject=new JSONObject();
				try {
					jsonObject.put("phone",verifycode);
					entity = new StringEntity(jsonObject.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				JsonHttpResponseHandler handler=new JsonHttpResponseHandler(){
				};
				//partUrl，请求的url
				String partUrl="index.php/Api/Member/forgotpassword.html";
				client.post(AppContext.instance(), ApiHttpClient.getAbsoluteApiUrl(partUrl),entity, "application/json", handler);
				//点击验证码延时
				if (!verifycode.matches("1[3|5|7|8|][0-9]{9}")) 
				{
					AppContext.showToast(getString(R.string.phonenumber_error));
					
				}else{
				//60000是总的延时的时间，1000是延时的间隔
				  TimeCount timeCount=new TimeCount(30000, 1000);
				  timeCount.start();
				}
			}
			//点击获取发送验证码的手机号码
			private void obtainParams() {
				verifycode = et_obatin_verify.getText().toString();
			}
		});
		
	
	}
	JsonHttpResponseHandler handler=new JsonHttpResponseHandler(){
			@Override
			public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				AppContext.showToast("找回失败");
			}

			@Override
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			super.onSuccess(statusCode, headers, response);

			try {
				if (response.getInt("code") == 88) {
					AppContext.showToast("修改成功");

					handleLoginSuccess();
				} else {
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
			return R.string.forget_password;
		}
	//拿到控件
	private void initComponet() {
		//验证码的图片控件
		bt_obtain_cons = (Button) findViewById(R.id.bt_obtain_cons);
		//手机号码
		et_obatin_verify = (EditText) findViewById(R.id.et_username);
		
		//验证码输入
		et_cons = (EditText) findViewById(R.id.et_cons);
		
		//密码输入
		et_input_password = (EditText) findViewById(R.id.et_password);
		
		//注册的按钮
		btn_regist = (Button) findViewById(R.id.btn_login);
	}
	//延时的内部类
	private class TimeCount extends CountDownTimer{

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		//结束的时候调用方法
		@Override
		public void onFinish() {
			//1.设置按钮点击为true
			bt_obtain_cons.setText("再次验证");
			bt_obtain_cons.setClickable(true);
			bt_obtain_cons.setBackgroundResource(R.drawable.red_rectangle);
		}
		//延时开始的时候调用的方法
		@SuppressLint("ResourceAsColor")
		@Override
		public void onTick(long millisUntilFinished) {
			bt_obtain_cons.setClickable(false);
			bt_obtain_cons.setBackgroundResource(R.drawable.verify_timecount);
			bt_obtain_cons.setText(millisUntilFinished/1000+"秒");
			bt_obtain_cons.setTextColor(R.color.blue);
		}
	}
}
