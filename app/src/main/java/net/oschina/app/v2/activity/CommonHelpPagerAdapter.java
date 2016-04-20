package net.oschina.app.v2.activity;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 通用帮助pager 适配器
 * 
 * @author 
 * @version 1.3.3
 * @since 1.3.3
 * @createDate 2014-6-9
 */
public class CommonHelpPagerAdapter extends PagerAdapter {

	List<View> mViews;

	public CommonHelpPagerAdapter(List<View> views) {
		mViews = new ArrayList<View>();
		mViews.addAll(views);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mViews.get(position));
		return mViews.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mViews.get(position));
	}
}
