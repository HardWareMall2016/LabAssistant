package net.oschina.app.v2.activity.tweet.fragment;

import net.oschina.app.v2.activity.tweet.adapter.TweetTabPagerAdapter;
import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.activity.tweet.view.TweetPopupListView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.event.FansTabEvent;
import net.oschina.app.v2.model.event.ToggleFilterbarEvent;
import net.oschina.app.v2.model.event.TweetTabEvent;

import org.apache.http.Header;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

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
	private View mFilerLayout;

	//Tab fans views
	private TextView mViewAllQuestion;
	private TextView mViewAnsweredQuestion;
	private TextView mViewUnansweredQuestion;


	private TweetPopupListView mPopupList;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_viewpager, container,
				false);
		mCover=view.findViewById(R.id.cover);
		mViewTweetTab=view.findViewById(R.id.tweet_tab);
		mViewFansTab=view.findViewById(R.id.fans_tab);
		mFilerLayout=view.findViewById(R.id.tabs);

		view.findViewById(R.id.question_status).setOnClickListener(this);
		view.findViewById(R.id.choose_classify).setOnClickListener(this);
		view.findViewById(R.id.choose_sub_classify).setOnClickListener(this);

		mViewAllQuestion=(TextView)view.findViewById(R.id.all_question);
		mViewAnsweredQuestion=(TextView)view.findViewById(R.id.answered_question);
		mViewUnansweredQuestion=(TextView)view.findViewById(R.id.unanswered_question);
		mViewAllQuestion.setOnClickListener(mFansTabClickListener);
		mViewAnsweredQuestion.setOnClickListener(mFansTabClickListener);
		mViewUnansweredQuestion.setOnClickListener(mFansTabClickListener);

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
		showFilterbar();
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

	private View.OnClickListener mFansTabClickListener=new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			mViewAllQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewAllQuestion.setBackgroundResource(R.drawable.default_bg);
			mViewAnsweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewAnsweredQuestion.setBackgroundResource(R.drawable.default_bg);
			mViewUnansweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewUnansweredQuestion.setBackgroundResource(R.drawable.default_bg);

			switch (v.getId()){
				case R.id.all_question:
					mViewAllQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewAllQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					EventBus.getDefault().post(new FansTabEvent(0));
					break;
				case R.id.answered_question:
					mViewAnsweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewAnsweredQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					EventBus.getDefault().post(new FansTabEvent(1));
					break;
				case R.id.unanswered_question:
					mViewUnansweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewUnansweredQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					EventBus.getDefault().post(new FansTabEvent(2));
					break;
			}
		}
	};

	@Override
	public void onDismiss() {
		mCover.setVisibility(View.GONE);
	}

	//筛选栏显示/隐藏
	public void onEventMainThread(ToggleFilterbarEvent event){
		if(event.showFilterBar){
			showFilterbar();
		}else{
			hideFilterbar();
		}
	}

	/*Tool bar is show */
	private boolean isFilterbarShown() {
		return mFilerLayout.getTranslationY() >= 0;
	}

	public void hideFilterbar() {
		if (isFilterbarShown()) {
			toggleFilterbarShown(false);
		}
	}

	public void showFilterbar() {
		if (!isFilterbarShown()) {
			toggleFilterbarShown(true);
		}
	}

	private ObjectAnimator filterBarObjectAnim;

	public void toggleFilterbarShown(boolean shown) {

		if (filterBarObjectAnim != null && filterBarObjectAnim.isRunning())
			return;

		if (isFilterbarShown() && shown)
			return;
		else if (!isFilterbarShown() && !shown)
			return;

		PropertyValuesHolder filterHolder = null;
		if (shown) {
			filterHolder = PropertyValuesHolder.ofFloat("translationY", -1 * mFilerLayout.getHeight(), 0);
		} else {
			filterHolder = PropertyValuesHolder.ofFloat("translationY", 0, -1 * mFilerLayout.getHeight());
		}
		filterBarObjectAnim = ObjectAnimator.ofPropertyValuesHolder(mFilerLayout, filterHolder);
		filterBarObjectAnim.setDuration(150);

		filterBarObjectAnim.start();
	}
}