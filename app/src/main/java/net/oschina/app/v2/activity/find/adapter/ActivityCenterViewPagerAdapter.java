package net.oschina.app.v2.activity.find.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ActivityCenterViewPagerAdapter extends PagerAdapter {
	private List<View> list;

	public ActivityCenterViewPagerAdapter(List<View> list) {
		super();
		this.list = list;
	}

	public void changeData(List<View> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(list.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = list.get(position);
		ViewGroup parent = (ViewGroup) v.getParent();
		if (parent != null){ parent.removeAllViews();}
		((ViewPager) container).addView(v);
		return v;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
