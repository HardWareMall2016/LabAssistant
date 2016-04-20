package net.oschina.app.v2.activity.common;

import android.view.View.OnClickListener;

/**
 * 带返回和右侧按钮的Activity
 * @author Johnny
 */
public class BtnBackActivity extends SimpleBackActivity {

	@Override
	protected boolean hasRightButton() {
		return true;
	}

	public void setmRightButtonOnClickListener(
			OnClickListener rightButtonOnClickListener) {
		rightMore.setOnClickListener(rightButtonOnClickListener);
	}
	
}
