package net.oschina.app.v2.activity.favorite.fragment;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class SaleActivity extends BaseActivity {

	private final String USER_name = "sale_name";
	private final String USER_danwei = "sale_danwei";
	private final String USER_mobile = "sale_mobile";
	private final String USER_email = "sale_email";
	private final String USER_address = "sale_address";
	
	private EditText lianxiren, danwei, shouji, email, address;
	private Button renzheng;
	private View tip;

	private int catid = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		 catid = intent.getIntExtra("pid", -1);

		lianxiren = (EditText) findViewById(R.id.et_username);
		danwei = (EditText) findViewById(R.id.et_danwei);
		shouji = (EditText) findViewById(R.id.et_phone);
		email = (EditText) findViewById(R.id.et_email);
		renzheng = (Button) findViewById(R.id.subBtn);
		address = (EditText) findViewById(R.id.et_place);
		tip=findViewById(R.id.tip_layout);
		
		danwei.setText(AppContext.getPersistPreferences().getString(USER_danwei, ""));
		email.setText(AppContext.getPersistPreferences().getString(USER_email, ""));
		shouji.setText(AppContext.getPersistPreferences().getString(USER_mobile, ""));
		lianxiren.setText(AppContext.getPersistPreferences().getString(USER_name, ""));
		address.setText(AppContext.getPersistPreferences().getString(USER_address, ""));
		
		findViewById(R.id.tip_close).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tip.setVisibility(View.GONE);
			}
		});
		
		renzheng.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				handleSubmit();
			}
		});
	}

	@Override
	protected int getLayoutId() {
		return R.layout.sale_register_layout;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.actionbar_title_sale;
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
		String address_info = address.getText().toString().trim();

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

		if (TextUtils.isEmpty(address_info)) {
			AppContext.showToastShort(R.string.tip_address_empty);
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
		editor.putString(USER_name, realname);
		editor.putString(USER_email, email_info);
		editor.putString(USER_mobile, shouji_info);
		editor.putString(USER_danwei, danwei_info);
		editor.putString(USER_address, address_info);
		editor.commit();
		
		int uid = AppContext.instance().getLoginUid();

		NewsApi.recharge(uid, catid, realname, shouji_info, address_info,
				email_info, danwei_info, responseHandler);
	}

	protected JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			//AppContext.showToast("兑换成功.");
			if (response.optInt("code") == 88) {
				AppContext.showToast("已提交，请等待工作人员处理");
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
