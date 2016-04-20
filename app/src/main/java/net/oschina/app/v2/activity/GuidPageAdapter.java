package net.oschina.app.v2.activity;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiyanzhushou.app.R;

public class GuidPageAdapter extends PagerAdapter {
	List<View> views;
	Context context;

	public GuidPageAdapter(List<View> views, Context context) {
		this.views = views;
		this.context = context;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View guider = null;
		if (position == 0) {
			guider = LayoutInflater.from(context).inflate(
					R.layout.welcome_one, null);
		} else if (position == 1) {
			guider = LayoutInflater.from(context).inflate(
					R.layout.welcome_two, null);
		} else if (position == 2) {
			guider = LayoutInflater.from(context).inflate(
					R.layout.welcome_four, null);
		}
		((ViewPager) container).addView(guider);
		return guider;
	}
}
