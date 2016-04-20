package net.oschina.app.v2.activity.tweet.fragment;

import net.oschina.app.v2.activity.tweet.adapter.TweetTabPagerAdapter;
import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.activity.tweet.view.TweetPopupView;
import net.oschina.app.v2.activity.tweet.view.TweetPopupView.OnFilterClickListener;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.ui.pagertab.PagerSlidingTabStrip;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class TweetViewPagerFragment extends Fragment implements
		OnPageChangeListener, OnFilterClickListener {

	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private TweetTabPagerAdapter mTabAdapter;
	private TweetPopupView mPopup;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_viewpager, container,
				false);
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);

		if (mTabAdapter == null) {
			mTabAdapter = new TweetTabPagerAdapter(getChildFragmentManager(),
					getActivity(), mViewPager);
		}
		mViewPager.setOffscreenPageLimit(mTabAdapter.getCacheCount());
		mViewPager.setAdapter(mTabAdapter);
		mViewPager.setOnPageChangeListener(this);
		mTabStrip.setViewPager(mViewPager);
		return view;
	}
	
	public void onDestroy(){
		super.onDestroy();
		if(mPopup!=null)
		mPopup.unregisterEventBus();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPopup = new TweetPopupView(getActivity());
		mPopup.setOnFilterClickListener(this);
		sendRequestLanmuData();
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
	
	private void sendRequestLanmuData() {
		NewsApi.getLanmu(-1, mLanmuHandler);
	}
	
	public void filterListData(View v) {
		mPopup.showPopup(v);
	}
	
	@Override
	public void onFilter(int isreward, int issolveed, String catid) {
		int item=mViewPager.getCurrentItem();
		Fragment f=mTabAdapter.getFragmentByPosition(item);
		if (f != null) {
			((OnFilterClickListener) f).onFilter(isreward, issolveed, catid);
		}
	}
	
	private JsonHttpResponseHandler mLanmuHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				MuluList list = MuluList.parse(response.toString());
				Mulu all=new Mulu();
				all.setId(-1);
				all.setcatname("全部");
				list.getMululist().add(0,all);
				mPopup.addList(list.getMululist());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}