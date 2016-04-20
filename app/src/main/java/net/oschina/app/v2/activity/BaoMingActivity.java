package net.oschina.app.v2.activity;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.utils.RegularValidUtil;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

/**
 * 报名
 */
public class BaoMingActivity extends BaseActivity {

	private final String USER_NAME = "baoming_name";
	private final String USER_DANWEI = "baoming_danwei";
	private final String USER_MOBILE = "baoming_shouji";
	private final String USER_EMAIL = "baoming_email";
	
	private EditText lianxiren, danwei, shouji, email;
	private Button renzheng;
	
	private int id;

	protected int getLayoutId() {
		return R.layout.a_baoming_ly;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.baoming_zhiliao;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		id = intent.getIntExtra("id", -1);
		
		
		lianxiren = (EditText) findViewById(R.id.et_username);
		danwei = (EditText) findViewById(R.id.et_danwei);
		shouji = (EditText) findViewById(R.id.et_phone);
		email = (EditText) findViewById(R.id.et_email);
		renzheng = (Button) findViewById(R.id.subBtn);

		
		
		danwei.setText(AppContext.getPersistPreferences().getString(USER_DANWEI, ""));
		email.setText(AppContext.getPersistPreferences().getString(USER_EMAIL, ""));
		shouji.setText(AppContext.getPersistPreferences().getString(USER_MOBILE, ""));
		lianxiren.setText(AppContext.getPersistPreferences().getString(USER_NAME, ""));
		
		renzheng.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				handleSubmit();
			}
		});
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

		String realname = lianxiren.getText().toString().trim();
		String danwei_info = danwei.getText().toString().trim();
		String shouji_info = shouji.getText().toString().trim();
		String email_info = email.getText().toString().trim();

		if (TextUtils.isEmpty(danwei_info)) {
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}

		if (TextUtils.isEmpty(shouji_info)) {
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		if (TextUtils.isEmpty(email_info)) {
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}

		if (TextUtils.isEmpty(realname)) {
			AppContext.showToastShort(R.string.tip_content_empty);
			return;
		}
		
		if(!RegularValidUtil.isMobileNO(shouji_info)){
			AppContext.showToastShort("手机号码格式有误，请重新输入.");
			return;
		}
		
		if(!RegularValidUtil.isEmail(email_info)){
			AppContext.showToastShort("邮箱格式有误，请重新输入.");
			return;
		}

		Editor editor = AppContext.getPersistPreferences().edit();
		editor.putString(USER_NAME, realname);
		editor.putString(USER_EMAIL, email_info);
		editor.putString(USER_MOBILE, shouji_info);
		editor.putString(USER_DANWEI, danwei_info);
		editor.commit();
		
		NewsApi.baoming(id, realname, danwei_info, shouji_info, email_info,
				responseHandler);

	}

	protected JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

//			AppContext.showToast(response.optString("desc"));
			int code =response.optInt("code");
			if(code==88){
				AppContext.showToast("您的申请已提交.");
			}else{
				AppContext.showToast(response.optString("desc"));
			}
			finish();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			AppContext.showToast("报名失败了");
		}
	};
}
