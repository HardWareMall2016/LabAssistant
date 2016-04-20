package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.RankActivity;
import net.oschina.app.v2.activity.find.adapter.WeekRankListAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.WeekRankList;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shiyanzhushou.app.R;

public class WeekRankFragment extends BaseListFragment {

	protected static final String TAG = WeekRankFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "weekranklist_";
	private int mCatalog;
	
	private boolean isNeedClearOldData = false;
	private int currentCategory = 1;

	@Override
	protected int getLayoutRes() {
		// TODO Auto-generated method stub
		return R.layout.a_weekrank;
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		// TODO Auto-generated method stub
		return new WeekRankListAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(getLayoutRes(), container, false);
		initViews(v);
		return v;
	}

	@Override
	protected void initViews(View view) {
		// TODO Auto-generated method stub
		mErrorLayout = (EmptyLayout) view
				.findViewById(R.id.error_layout_weekrank);
		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = 0;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.listview_weekrank);

		mListView.setMode(Mode.DISABLED);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnLastItemVisibleListener(this);

		if (mAdapter != null) {
			mListView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getListAdapter();
			// mListView.setRefreshing();
			mListView.setAdapter(mAdapter);

			if (requestDataIfViewCreated()) {
				mCurrentPage = 0;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(false);
			} else {
				mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
			}
		}
		if (mStoreEmptyState != -1) {
			mErrorLayout.setErrorType(mStoreEmptyState);
		}
	}

	@Override
	protected String getCacheKeyPrefix() {
		// TODO Auto-generated method stub
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		String result = inStream2String(is);
		WeekRankList wrlist = WeekRankList.parse(result);
		System.out.println(result.toString() + "！！！！！！本周排行榜");
		is.close();

		return wrlist;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		// TODO Auto-generated method stub
		return ((WeekRankList) seri);
	}

	@Override
	protected void sendRequestData() {
		
		
		isNeedClearOldData = (currentCategory != RankActivity.categoryType);
		
		currentCategory = RankActivity.categoryType;
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		
		if(isNeedClearOldData)
		{
			mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
		}
		
		NewsApi.getWeekRankList(AppContext.instance().getLoginUid(), mCurrentPage,RankActivity.categoryType, mJsonHandler);
	}
	
	public void onCategoryChange(int category)
	{
		sendRequestData();
	}
	
	@Override
	protected void executeOnLoadDataSuccess(List<?> data) {
		if(isNeedClearOldData)
		{
			mAdapter.clear();
		}
		
		if (mState == STATE_REFRESH)
		{
			mAdapter.clear();
		}
		
		mAdapter.addData(data);
		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		if (data.size() == 0 && mState == STATE_REFRESH) 
		{
			if(mAdapter.getCount() == 0)
			{
				if (isShowEmpty()) {
					mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
				}
			} else
			{
				AppContext.showToast("没有获取到新数据");
			}
		} else if (data.size() < TDevice.getPageSize()) 
		{
			if (mState == STATE_REFRESH)
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
			else
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else {
			mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		}
	}
}
