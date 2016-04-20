package net.oschina.app.v2.activity.common;

import net.oschina.app.v2.activity.find.adapter.WeekAdapter;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.utils.ListViewUtils;
import android.os.Bundle;
import android.widget.ListView;

import com.shiyanzhushou.app.R;

public class WeeklyActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_week);
		ListView weekly = (ListView) findViewById(R.id.lv_week);
		WeekAdapter adapter = new WeekAdapter();
		weekly.setAdapter(adapter);
		ListViewUtils lvutils = new ListViewUtils();
		lvutils.setListViewHeightBasedOnChildren(weekly);
	}
}
