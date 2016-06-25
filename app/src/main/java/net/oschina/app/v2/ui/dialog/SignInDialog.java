package net.oschina.app.v2.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.ui.calendar.CalendarAdapter;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.TDevice;

public class SignInDialog extends Dialog implements View.OnClickListener{

	//private TextView _messageTv;
	private CalendarAdapter calV = null;
	private GridView gridView = null;
	private TextView mBtnOk = null;

	public SignInDialog(Context context) {
		super(context);
		init(context);
	}

	public SignInDialog(Context context, int defStyle) {
		super(context, defStyle);
		init(context);
	}

	protected SignInDialog(Context context, boolean cancelable, OnCancelListener listener) {
		super(context, cancelable, listener);
		init(context);
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

	private void init(Context context) {
		setCancelable(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.calendar, null);
		gridView =(GridView)view.findViewById(R.id.gridview);
		calV = new CalendarAdapter(context,context.getResources());
		gridView.setAdapter(calV);
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
