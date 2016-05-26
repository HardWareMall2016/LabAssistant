package net.oschina.app.v2.activity.tweet.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.fragment.DailyFragment;
import net.oschina.app.v2.activity.tweet.adapter.funsForHelperAdapter;
import net.oschina.app.v2.activity.tweet.view.TweetPopupListView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.FansTabEvent;
import net.oschina.app.v2.model.event.ToggleFilterbarEvent;
import net.oschina.app.v2.model.event.TweetTabEvent;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.UIHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import de.greenrobot.event.EventBus;

public class FansFragment extends BaseListFragment implements TweetPopupListView.OnFilterClickListener {

	protected static final String TAG = DailyFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "fanslist_";
	private int mStatus =1;//1 全部  2 已回答 3 未回答


	int lastFirstVisibleItem = 0;
	int lastTop = 0;
	boolean isMoving = false;
	private ListView mMainListView;


	@Override
	protected void initViews(View view) {
		super.initViews(view);
		ViewGroup viewGroup=(ViewGroup)view;
		viewGroup.setClipChildren(false);
		viewGroup.setClipToPadding(false);


		mListView.setClipChildren(false);
		mListView.setClipToPadding(false);

		mMainListView=mListView.getRefreshableView();

		mMainListView.setClipChildren(false);
		mMainListView.setClipToPadding(false);
		mMainListView.setPadding(0, DeviceUtils.dip2px(getActivity(), 50), 0, 0);

		mMainListView.setOnScrollListener(new AbsListView.OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//scrollState = scrollState;

				if (scrollState == SCROLL_STATE_IDLE) {
					isMoving = false;
					lastTop = 0;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (mMainListView.getChildCount() == 0)
					return;

				if (isMoving) {
					View firstChild = view.getChildAt(0);

					if (firstVisibleItem == 0) {
						toggleFilterbarShown(true);
					}
					else if (firstVisibleItem > lastFirstVisibleItem) {
						toggleFilterbarShown(false);
					}
					else if (firstVisibleItem < lastFirstVisibleItem) {
						toggleFilterbarShown(true);
					}
					else {
						int height = firstChild.getHeight();
						if (height > DeviceUtils.dip2px(getActivity(), 200)) {
							if (lastTop == 0) {
								lastTop = firstChild.getTop();
							}
							else {
								int diffTop = firstChild.getTop() - lastTop;
								if (Math.abs(diffTop) >= DeviceUtils.dip2px(getActivity(), 150)) {
									toggleFilterbarShown(diffTop > 0);
								}
							}
						}
					}
				}

				lastFirstVisibleItem = firstVisibleItem;
			}
		});

		mMainListView.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				}
				else if (event.getAction() == MotionEvent.ACTION_MOVE) {
					isMoving = true;
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) {
				}
				return false;
			}
		});
	}

	private void toggleFilterbarShown(boolean shown) {
		lastTop = 0;
		EventBus.getDefault().post(new ToggleFilterbarEvent(shown));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onEventMainThread(TweetTabEvent event){
		if(event.tabIndex==1){
			setRefresh();
		}
	}

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new funsForHelperAdapter(getActivity(),false);
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result=inStream2String(is);
		AskList list = AskList.parse(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AskList) seri);
	}

	@Override
	protected void sendRequestData() {
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFansAskList(AppContext.instance().getLoginUid(), mCurrentPage, mStatus, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Ask ask = (Ask) mAdapter.getItem(position - 1);
		if (ask != null)
			UIHelper.showTweetDetail(view.getContext(),ask);
	}

	@Override
	public void onFilter(int isreward, int issolveed, String catid) {
		/*mState=STATE_REFRESH;
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFilterList(1, catid, isreward, issolveed, 1, mJsonHandler);*/
	}

	public void onEventMainThread(FansTabEvent event){
		if(event.tabIndex==0){
			mStatus=1;
		}else if(event.tabIndex==1){
			mStatus=3;
		}else{
			mStatus=2;
		}

		/*mCurrentPage = 1;
		mState = STATE_REFRESH;
		requestData(true);*/
		setRefresh();
		//NewsApi.getFansAskList(AppContext.instance().getLoginUid(), mCurrentPage,mStarus, mJsonHandler);
	}
}


