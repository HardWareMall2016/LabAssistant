package net.oschina.app.v2.activity.tweet.fragment;

import net.oschina.app.v2.activity.tweet.adapter.TweetTabPagerAdapter;
import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.activity.tweet.view.TweetPopupListView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.event.TweetTabEvent;

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
import android.widget.PopupWindow;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

public class TweetViewPagerFragment extends Fragment implements
		OnPageChangeListener, TweetPopupListView.OnFilterClickListener,View.OnClickListener,PopupWindow.OnDismissListener {

	//private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private View mCover;
	private TweetTabPagerAdapter mTabAdapter;
	//private TweetPopupView mPopup;
	private View mViewTweetTab;
	private View mViewFansTab;

	private TweetPopupListView mPopupList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_viewpager, container,
				false);
		mCover=view.findViewById(R.id.cover);
		mViewTweetTab=view.findViewById(R.id.tweet_tab);
		mViewFansTab=view.findViewById(R.id.fans_tab);

		view.findViewById(R.id.question_status).setOnClickListener(this);
		view.findViewById(R.id.choose_classify).setOnClickListener(this);
		view.findViewById(R.id.choose_sub_classify).setOnClickListener(this);
		//mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);

		if (mTabAdapter == null) {
			mTabAdapter = new TweetTabPagerAdapter(getChildFragmentManager(),
					getActivity(), mViewPager);
		}
		mViewPager.setOffscreenPageLimit(mTabAdapter.getCacheCount());
		mViewPager.setAdapter(mTabAdapter);
		mViewPager.setOnPageChangeListener(this);
		//mTabStrip.setViewPager(mViewPager);
		EventBus.getDefault().register(this);
		return view;
	}

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	public void onDestroy(){
		super.onDestroy();
		/*if(mPopup!=null)
		mPopup.unregisterEventBus();*/
		if(mPopupList!=null)
			mPopupList.unregisterEventBus();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPopupList=new TweetPopupListView(getActivity(),this);
		mPopupList.setOnFilterClickListener(this);
		/*mPopup = new TweetPopupView(getActivity());
		mPopup.setOnFilterClickListener(this);*/
		sendRequestLanmuData();
	}

	public void onEventMainThread(TweetTabEvent event){
		if(event.byUser){
			mViewPager.setCurrentItem(event.tabIndex);
		}
		mViewTweetTab.setVisibility(event.tabIndex==0?View.VISIBLE:View.GONE);
		mViewFansTab.setVisibility(event.tabIndex==1?View.VISIBLE:View.GONE);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		//mTabStrip.onPageScrollStateChanged(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		//mTabStrip.onPageScrolled(arg0, arg1, arg2);
		mTabAdapter.onPageScrolled(arg0);
	}

	@Override
	public void onPageSelected(int arg0) {
		//mTabStrip.onPageSelected(arg0);
		mTabAdapter.onPageSelected(arg0);
		EventBus.getDefault().post(new TweetTabEvent(arg0, false));
	}
	
	private void sendRequestLanmuData() {
		NewsApi.getLanmu(-1, mLanmuHandler);
	}
	
	/*public void filterListData(View v) {
		mPopup.showPopup(v);
	}*/
	
	@Override
	public void onFilter(int isreward, int issolveed, String catid) {
		int item=mViewPager.getCurrentItem();
		Fragment f=mTabAdapter.getFragmentByPosition(item);
		if (f != null) {
			((TweetPopupListView.OnFilterClickListener) f).onFilter(isreward, issolveed, catid);
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
				mPopupList.setMainClassifyList(list.getMululist());
				/*Mulu all=new Mulu();
				all.setId(-1);
				all.setcatname("全部");
				list.getMululist().add(0,all);
				mPopup.addList(list.getMululist());*/

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		mCover.setVisibility(View.VISIBLE);
		switch (v.getId()){
			case R.id.question_status:
				mPopupList.showPopup(v,TweetPopupListView.QUESTION_STATUS);
				break;
			case R.id.choose_classify:
				mPopupList.showPopup(v,TweetPopupListView.CHOOSE_CLASSIFY);
				break;
			case R.id.choose_sub_classify:
				mPopupList.showPopup(v,TweetPopupListView.CHOOSE_SUB_CLASSIFY);
				break;
		}
	}

	@Override
	public void onDismiss() {
		mCover.setVisibility(View.GONE);
	}
}