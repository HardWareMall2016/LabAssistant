package net.oschina.app.v2.activity.find.adapter;

import java.util.Iterator;
import java.util.List;

import net.oschina.app.v2.activity.find.fragment.BillBoardTab;
import net.oschina.app.v2.activity.find.fragment.BillboardFragment;
import net.oschina.app.v2.base.BaseTabFragment;
import net.oschina.app.v2.ui.pagertab.SlidingTabPagerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

public class BillBoardTabPagerAdapter extends SlidingTabPagerAdapter {

	public BillBoardTabPagerAdapter(FragmentManager mgr, Context context,
			ViewPager vp) {
		super(mgr, BillBoardTab.values().length, context
				.getApplicationContext(), vp);
		// TODO Auto-generated constructor stub
		BillBoardTab[] values = BillBoardTab.values();
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
			if (tabFragment == null) {
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
					args.putInt(BillboardFragment.BUNDLE_KEY_CATALOG,
							values[i].getCatalog());
					tabFragment.setArguments(args);
				}
				fragments[values[i].getIdx()] = tabFragment;
			}

		}
	}

	@Override
	public int getCacheCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return BillBoardTab.values().length;
	}

	@Override
	public CharSequence getPageTitle(int i) {
		// TODO Auto-generated method stub
		BillBoardTab tab = BillBoardTab.getTabByIdx(i);
		int resId = 0;
		CharSequence title = "";
		if (tab != null) {
			resId = tab.getTitle();
		}
		if (resId != 0) {
			title = context.getText(resId);
		}
		return title;
	}

}
