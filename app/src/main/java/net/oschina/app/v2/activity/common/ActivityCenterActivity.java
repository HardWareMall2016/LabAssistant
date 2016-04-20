package net.oschina.app.v2.activity.common;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.activity.find.adapter.ActivityCenterViewPagerAdapter;
import net.oschina.app.v2.activity.find.view.CustomViewPager;
import net.oschina.app.v2.base.BaseActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shiyanzhushou.app.R;

public class ActivityCenterActivity extends BaseActivity {
	String url ="http://phpapi.ccjjj.net/index.php/Api/Found/party.html" ;
	private List<View> containerViews = new ArrayList<View>();// 承载广告位的容器
	private CustomViewPager mViewpager;
	private LinearLayout pointLinear;
	private ActivityCenterViewPagerAdapter viewPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_activitycenter);
//		init();
//		HttpClient client = new DefaultHttpClient();
//		UrlEncodedFormEntity tntiy=null;
//		entity = new UrlEncodedFormEntity()
	}

	public void init() {
		mViewpager = (CustomViewPager) findViewById(R.id.activitycenter_viewpager);
		pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
		View view = LayoutInflater.from(this).inflate(
				R.layout.a_activitycenter_vpitem, null);
		containerViews.add(view);
		viewPagerAdapter = new ActivityCenterViewPagerAdapter(containerViews);
		pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));
	}
}
