package net.oschina.app.v2.activity.common;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LearnActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView tv = new TextView(this);

		tv.setText("学习资料");
		setContentView(tv);

	}
}
