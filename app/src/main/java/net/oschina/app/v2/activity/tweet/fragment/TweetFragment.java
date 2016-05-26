package net.oschina.app.v2.activity.tweet.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.activity.find.fragment.DailyFragment;
import net.oschina.app.v2.activity.tweet.adapter.TweetAdapter;
import net.oschina.app.v2.activity.tweet.view.TweetPopupListView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.ToggleFilterbarEvent;
import net.oschina.app.v2.model.event.TweetTabEvent;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.ShareUtil;
import net.oschina.app.v2.utils.UIHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import de.greenrobot.event.EventBus;

public class TweetFragment extends BaseListFragment implements
		TweetPopupListView.OnFilterClickListener {

	protected static final String TAG = DailyFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "tweetslist_";

	private int isreward=0, issolveed=0;//悬赏、未解决
	private int mSelMainFilterId=-1;
	private String selectedCatIds;


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
	protected ListBaseAdapter getListAdapter() {
		return new TweetAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		AskList list = AskList.parse(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AskList) seri);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isreward= ShareUtil.getIntValue(ShareUtil.IS_REWARD, 0);
		issolveed= ShareUtil.getIntValue(ShareUtil.IS_SOLVEED,0);
		mSelMainFilterId= ShareUtil.getIntValue(ShareUtil.MAIN_FILTER_ID, -1);
		selectedCatIds=ShareUtil.getStringValue(ShareUtil.SELECTED_CAT_IDS, "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void onEventMainThread(TweetTabEvent event){
		if(event.tabIndex==0){
			setRefresh();
		}
	}

	@Override
	public void onDestroyView() {
		EventBus.getDefault().unregister(this);
		super.onDestroyView();
	}

	@Override
	protected void sendRequestData() {
		/*if(STATE_REFRESH==mState){//下拉刷新清空筛选条件
			EventBus.getDefault().post(new ClearFilterConditions());
		}
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getAskList(mCurrentPage, mJsonHandler);*/
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		if(isreward==0&&issolveed==0&&(TextUtils.isEmpty(selectedCatIds)||selectedCatIds.contains("-1"))){
			NewsApi.getAskList(mCurrentPage, mJsonHandler);
		}else{
			NewsApi.getFilterList(mCurrentPage, selectedCatIds, isreward, issolveed, 2, mJsonHandler);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Ask ask = (Ask) mAdapter.getItem(position - 1);
		if (ask != null)
			UIHelper.showTweetDetail(view.getContext(), ask);
	}

	@Override
	public void onFilter(int isreward, int issolveed, String catid) {
		/*mState=STATE_REFRESH;
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFilterList(mCurrentPage, catid, isreward, issolveed, 2, mJsonHandler);*/

		mState=STATE_REFRESH;

		//mListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
		mListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在筛选...");
		//mListView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");

		this.isreward=isreward;
		this.issolveed=issolveed;
		selectedCatIds=catid;

		ShareUtil.setIntValue(ShareUtil.IS_REWARD, isreward);
		ShareUtil.setIntValue(ShareUtil.IS_SOLVEED, issolveed);
		ShareUtil.setValue(ShareUtil.SELECTED_CAT_IDS, selectedCatIds);

		setRefresh();
	}
}
