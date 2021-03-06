package net.oschina.app.v2.activity.user.adapter;

import java.util.Iterator;
import java.util.List;

import net.oschina.app.v2.activity.tweet.fragment.TweetFragment;
import net.oschina.app.v2.activity.user.AnswerAfterTab;
import net.oschina.app.v2.base.BaseTabFragment;
import net.oschina.app.v2.ui.pagertab.SlidingTabPagerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

public class AnswerAftertTabPagerAdapter extends SlidingTabPagerAdapter {

	public AnswerAftertTabPagerAdapter(FragmentManager mgr, Context context,
			ViewPager viewpager) {
		super(mgr, AnswerAfterTab.values().length, context
				.getApplicationContext(), viewpager);
		AnswerAfterTab[] values = AnswerAfterTab.values();
		for (int i = 0; i < values.length; i++) {
			Fragment fragment = null;
			List<Fragment> list = mgr.getFragments();
			if (list != null) {
				Iterator<Fragment> iterator = list.iterator();
				while (iterator.hasNext()) {
					fragment = iterator.next();
					if (fragment.getClass() == values[i].getClz()) {
						break;
					}
				}
			}

			BaseTabFragment tabFragment = (BaseTabFragment) fragment;
			if (tabFragment == null)
				try {
					tabFragment = (BaseTabFragment) values[i].getClz()
							.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			tabFragment.a(this);
			if (!tabFragment.isAdded()) {
				Bundle args = new Bundle();
				args.putInt(TweetFragment.BUNDLE_KEY_CATALOG,
						values[i].getCatalog());
				tabFragment.setArguments(args);
			}
			fragments[values[i].getIdx()] = tabFragment;
		}
	}

	@Override
	public int getCacheCount() {
		return 1;
	}

	@Override
	public int getCount() {
		return AnswerAfterTab.values().length;
	}

	@Override
	public CharSequence getPageTitle(int i) {
		AnswerAfterTab tab = AnswerAfterTab.getTabByIdx(i);
		int resId = 0;
		CharSequence title = "";
		if (tab != null)
			resId = tab.getTitle();
		if (resId != 0)
			title = context.getText(resId);
		return title;
	}

}
