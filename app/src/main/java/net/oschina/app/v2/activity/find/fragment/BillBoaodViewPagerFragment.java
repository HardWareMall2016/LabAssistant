package net.oschina.app.v2.activity.find.fragment;

import net.oschina.app.v2.activity.find.adapter.BillBoardTabPagerAdapter;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.ui.pagertab.PagerSlidingTabStrip;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiyanzhushou.app.R;

public class BillBoaodViewPagerFragment extends BaseFragment implements
		OnPageChangeListener {

	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private BillBoardTabPagerAdapter mTabaAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.v2_fragment_viewpager, container,
				false);
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);
		if (mTabaAdapter == null) {
			mTabaAdapter = new BillBoardTabPagerAdapter(
					getChildFragmentManager(), getActivity(), mViewPager);
		}
		mViewPager.setOffscreenPageLimit(mTabaAdapter.getCacheCount());

		mViewPager.setAdapter(mTabaAdapter);
		mViewPager.setOnPageChangeListener(this);
		mTabStrip.setViewPager(mViewPager);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}


	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		mTabStrip.onPageScrollStateChanged(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		mTabStrip.onPageScrolled(arg0, arg1, arg2);
		mTabaAdapter.onPageScrolled(arg0);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mTabStrip.onPageSelected(arg0);
		mTabaAdapter.onPageSelected(arg0);
	}

	public void onRankCategoryChange(int categoryType)
	{
		if(null != mTabaAdapter)
		{
			if(mTabaAdapter.getCount() > 0)
			{
				Fragment f = mTabaAdapter.getItem(0);
				if(f instanceof WeekRankFragment)
				{
					WeekRankFragment wf = (WeekRankFragment) f;
					wf.onCategoryChange(categoryType);
				}
			}
			
			if(mTabaAdapter.getCount() > 1)
			{
				Fragment f = mTabaAdapter.getItem(1);
				if(f instanceof TotalRankFragment)
				{
					TotalRankFragment wf = (TotalRankFragment) f;
					wf.onCategoryChange(categoryType);
				}
			}
			
		}
		
		
	}
}
