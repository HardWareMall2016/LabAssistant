package net.oschina.app.v2.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shiyanzhushou.app.R;



public class GuidActivity extends Activity {
	private Context context;
	private ViewPager viewPager;
	private GuidPageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_guider);
		context = this;
		IsGuid.clearGuid(context);

		initViewPager();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		final List<View> guiderViews = new ArrayList<View>();
		guiderViews.add(null);
		guiderViews.add(null);
		guiderViews.add(null); 
		adapter = new GuidPageAdapter(guiderViews, context);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);
	}

	public void enter(View view) {
//		Intent intent;
//		intent = new Intent(context, MainActivity.class);
//		startActivity(intent);
//		TongzheSpf.setIsGuid(context, "false");
		//finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();

	}

}
