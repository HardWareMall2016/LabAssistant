package net.oschina.app.v2.activity.find.fragment;

import net.oschina.app.v2.activity.find.adapter.MallTabPagerAdapter;
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

public class MallViewPagerFragment extends Fragment implements
		OnPageChangeListener {

	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private MallTabPagerAdapter mTabaAdapter;

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
		View view = inflater.inflate(R.layout.v2_fragment_viewpager_bottom, container,
				false);
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
//		mTabStrip.setIndicatorColor(0xFFE07D5F);
//		mTabStrip.setCheckedTextColorResource(getResources().getColor(R.color.mark_indicate));
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);
		if (mTabaAdapter == null) {
			mTabaAdapter = new MallTabPagerAdapter(
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

//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//		LayoutInflater inflator1 = (LayoutInflater) getActivity()
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View v = inflator1.inflate(R.layout.billboard_actionbar, null);
//		ActionBar actionBar = getActivity().getActionBar();
//		actionBar.setDisplayHomeAsUpEnabled(false);
//		actionBar.setDisplayShowHomeEnabled(false);
//		actionBar.setDisplayShowCustomEnabled(true);
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setCustomView(v);
//		super.onCreateOptionsMenu(menu, inflater);
//		View back = v.findViewById(R.id.btn_back);
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				getActivity().finish();
//			}
//		});
//
//	}

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

}
