package net.oschina.app.v2.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.ui.calendar.CalendarAdapter;
import net.oschina.app.v2.utils.DeviceUtils;

public class SignInRulesDialog extends Dialog implements View.OnClickListener{
	private TextView mBtnOk = null;

	public SignInRulesDialog(Context context) {
		super(context);
		init(context);
	}

	public SignInRulesDialog(Context context, int defStyle) {
		super(context, defStyle);
		init(context);
	}

	protected SignInRulesDialog(Context context, boolean cancelable, OnCancelListener listener) {
		super(context, cancelable, listener);
		init(context);
	}

	public static boolean dismiss(SignInRulesDialog dialog) {
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

	public static boolean hide(SignInRulesDialog dialog) {
		if (dialog != null) {
			dialog.hide();
			return false;
		} else {
			return true;
		}
	}

	private void init(Context context) {
		setCancelable(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.signin_rules, null);
		setContentView(view);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (DeviceUtils.getScreenWidth(context) * 0.9);
		dialogWindow.setAttributes(lp);

		mBtnOk = (TextView) view.findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case  R.id.btn_ok:
				dismiss();
				break;
		}
	}
}
