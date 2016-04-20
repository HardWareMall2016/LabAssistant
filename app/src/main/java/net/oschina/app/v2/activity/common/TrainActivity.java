package net.oschina.app.v2.activity.common;

import java.util.ArrayList;

import net.oschina.app.v2.activity.find.adapter.ActivityCenterViewPagerAdapter;
import net.oschina.app.v2.activity.find.adapter.DailyAdapter;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.utils.ListViewUtils;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.shiyanzhushou.app.R;

public class TrainActivity extends BaseActivity {
	private ArrayList<View> views;
	private ViewPager vp_train;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_train);
		views = new ArrayList<View>();
		for (int i = 0; i < 5; i++) {
			View v = View.inflate(getApplicationContext(),
					R.layout.a_activitycenter_vpitem, null);
			ImageView iv_activitycenter_vpitem = (ImageView) v
					.findViewById(R.id.iv_activitycenter_vpitem);
			iv_activitycenter_vpitem.setImageResource(R.drawable.trainvp);
			views.add(v);

		}
		vp_train = (ViewPager) findViewById(R.id.vp_train);
		ActivityCenterViewPagerAdapter adapter = new ActivityCenterViewPagerAdapter(
				views);
		vp_train.setAdapter(adapter);
		vp_train.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		ListView lv_train = (ListView) findViewById(R.id.lv_train);
		lv_train.setAdapter(new DailyAdapter(TrainActivity.this));
		ListViewUtils lvutils = new ListViewUtils();
		lvutils.setListViewHeightBasedOnChildren(lv_train);
	}
}
