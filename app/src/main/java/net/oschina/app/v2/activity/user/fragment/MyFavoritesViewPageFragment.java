package net.oschina.app.v2.activity.user.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.favorite.adapter.FavoritesTabPagerAdapter;
import net.oschina.app.v2.ui.pagertab.PagerSlidingTabStrip;


public class MyFavoritesViewPageFragment extends Fragment implements
		OnPageChangeListener {
	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private FavoritesTabPagerAdapter mTabAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_viewpager, container,
				false);
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);

		if (mTabAdapter == null) {
			mTabAdapter = new FavoritesTabPagerAdapter(getChildFragmentManager(), getActivity(), mViewPager);
		}
		mViewPager.setOffscreenPageLimit(mTabAdapter.getCacheCount());
		mViewPager.setAdapter(mTabAdapter);
		mViewPager.setOnPageChangeListener(this);
		mTabStrip.setViewPager(mViewPager);
		
		/*ViewFinder viewFinder=new ViewFinder(view);
		newMessageContainer=viewFinder.find(R.id.newMessageContainer);
		myAskTip=viewFinder.find(R.id.tab_myask_tip);
		askMeTip=viewFinder.find(R.id.tab_askme_tip);
		Bundle bundle=getArguments();
		if(bundle!=null){
			mAskMeNum=bundle.getInt("num",0);
		}
		
		if(mAskMeNum>0){
			newMessageContainer.setVisibility(View.VISIBLE);
		}else{
			newMessageContainer.setVisibility(View.GONE);
		}*/
		return view;
	}
	


	@Override
	public void onPageScrollStateChanged(int arg0) {
		mTabStrip.onPageScrollStateChanged(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		mTabStrip.onPageScrolled(arg0, arg1, arg2);
		mTabAdapter.onPageScrolled(arg0);
	}

	@Override
	public void onPageSelected(int arg0) {
		mTabStrip.onPageSelected(arg0);
		mTabAdapter.onPageSelected(arg0);
	}
}