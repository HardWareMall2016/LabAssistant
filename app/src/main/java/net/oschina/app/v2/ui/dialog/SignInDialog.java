package net.oschina.app.v2.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.ui.UISwitchButton;
import net.oschina.app.v2.ui.calendar.CalendarAdapter;
import net.oschina.app.v2.utils.DeviceUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SignInDialog extends Dialog implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

	//private TextView _messageTv;
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView mBtnOk = null;
	private UISwitchButton mUISwitchButton;

	private String signremind;
	private String integral;
	private String curmonth;
	private int continuecount;

	private Context mContext;

	/*public SignInDialog(Context context) {
		super(context);
		init(context);
	}*/

	public SignInDialog(Context context, int defStyle) {
		super(context, defStyle);
		mContext = context;
	}

	/*protected SignInDialog(Context context, boolean cancelable, OnCancelListener listener) {
		super(context, cancelable, listener);
		init(context);
	}*/

	public void setContinuecount(int continuecount) {
		this.continuecount = continuecount;
	}

	public void setCurmonth(String curmonth) {
		this.curmonth = curmonth;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public void setSignremind(String signremind) {
		this.signremind = signremind;
	}

	public static boolean dismiss(SignInDialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			return false;
		} else {
			return true;
		}
	}

	public static void hide(Context context) {
		if (context instanceof DialogControl)
			((DialogControl) context).hideWaitDialog();
	}

	public static boolean hide(SignInDialog dialog) {
		if (dialog != null) {
			dialog.hide();
			return false;
		} else {
			return true;
		}
	}

	public void prepareUI() {
		setCancelable(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(mContext).inflate(R.layout.calendar, null);
		gridView = (GridView) view.findViewById(R.id.gridview);
		calV = new CalendarAdapter(mContext, mContext.getResources(), curmonth);
		gridView.setAdapter(calV);
		setContentView(view);

		populateView(view);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (DeviceUtils.getScreenWidth(mContext) * 0.9);
		dialogWindow.setAttributes(lp);

		mBtnOk = (TextView) view.findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);

		view.findViewById(R.id.reward_rules).setOnClickListener(this);
	}

	private void populateView(View view) {
		TextView tvContinueCount = (TextView) view.findViewById(R.id.continuecount);
		tvContinueCount.setText("已连续签到" + continuecount + "天");
		TextView tvCurrentIntegral = (TextView) view.findViewById(R.id.current_integral);
		tvCurrentIntegral.setText(integral);
		mUISwitchButton = (UISwitchButton) view.findViewById(R.id.switchButton);
		mUISwitchButton.setChecked("1".endsWith(signremind));
		mUISwitchButton.setOnCheckedChangeListener(this);

		Calendar calendar = Calendar.getInstance();
		TextView tvYear = (TextView) view.findViewById(R.id.tv_year);
		tvYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));

		TextView tvMonth = (TextView) view.findViewById(R.id.tv_month);
		tvMonth.setText((calendar.get(Calendar.MONTH) + 1) + "月");


		IntegralUpgrade data=calculateIntegralUpgrade(continuecount);
		TextView tvSignInfo = (TextView) view.findViewById(R.id.sign_info);
		tvSignInfo.setText(String.format("再签到%d天，可以获得%d积分奖励",data.day,data.integral));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_ok:
				dismiss();
				break;
			case R.id.reward_rules:
				SignInRulesDialog dialog = new SignInRulesDialog(getContext(), R.style.Dialog);
				dialog.show();
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Log.i("wuyue", "isChecked " + isChecked);
		int remind=isChecked?1:0;
		NewsApi.signRemind(AppContext.instance().getLoginUid(),remind, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				boolean success=false;
				try {
					if (response!=null&&response.getInt("code")==88){
						success=true;
                    }
				} catch (JSONException e) {

				}
				if(success){
					signremind="1".endsWith(signremind)?"0":"1";
				}else{
					mUISwitchButton.setChecked("1".endsWith(signremind));
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				mUISwitchButton.setChecked("1".endsWith(signremind));
			}
		});
	}

	private IntegralUpgrade calculateIntegralUpgrade(int continuecount){
		IntegralUpgrade data=new IntegralUpgrade();
		if(continuecount<5){
			data.integral=50;
			data.day=5-continuecount;
		} else if (continuecount<10){
			data.integral=100;
			data.day=10-continuecount;
		}else if (continuecount<20){
			data.integral=500;
			data.day=20-continuecount;
		}else if (continuecount<30){
			data.integral=800;
			data.day=30-continuecount;
		}else if (continuecount<50){
			data.integral=1200;
			data.day=50-continuecount;
		}else if (continuecount<100){
			data.integral=3000;
			data.day=100-continuecount;
		}else if (continuecount<200){
			data.integral=5000;
			data.day=200-continuecount;
		}else if (continuecount<365){
			data.integral=10000;
			data.day=365-continuecount;
		}
		return  data;
	}


	private class IntegralUpgrade{
		int integral;
		int day;
	}

}
