package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.adapter.DailyAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Daily;
import net.oschina.app.v2.model.DailyList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshPinnedSectionListView;
import com.hb.views.PinnedSectionListView;
import com.shiyanzhushou.app.R;

/**
 * 助手日报
 * 
 * @author JohnnyMeng
 */
public class DailyFragment extends BaseListFragment {

	protected static final String TAG = DailyFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "dailylist_";

	private PullToRefreshPinnedSectionListView mStickListView;

	@Override
	protected int getLayoutRes() {
		return R.layout.v2_fragment_pull_refresh_stick_listview;
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new DailyAdapter(getActivity());
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	protected void initViews(View view) {
		mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = 1;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});
		mStickListView = (PullToRefreshPinnedSectionListView) view
				.findViewById(R.id.listview);
		mStickListView.setBackgroundResource(R.color.white);
		mStickListView.setOnItemClickListener(this);
		mStickListView.setOnRefreshListener(this);
		mStickListView.setOnLastItemVisibleListener(this);

		addHeaderView(mStickListView.getRefreshableView());

		if (mAdapter != null) {
			mStickListView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getListAdapter();
			mStickListView.setAdapter(mAdapter);

			if (requestDataIfViewCreated()) {
				mCurrentPage = 1;
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

		((PinnedSectionListView) mStickListView.getRefreshableView())
				.setShadowVisible(false);
	}

	protected void executeOnLoadFinish() {
		mStickListView.onRefreshComplete();
		mState = STATE_NONE;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		DailyList list = DailyList.parse(result);
		// 以时间分组
		List<Daily> dailyList = (List<Daily>) list.getList();
		// if(dailyList.size()>0){
		// Daily daily = dailyList.get(0);
		// Daily titleDaily = new Daily();
		// titleDaily.setTop(true);
		// titleDaily.setInputtime(daily.getInputtime());
		// titleDaily.setDate(daily.getDate());
		// titleDaily.setId(daily.getId());
		// titleDaily.setThumb(daily.getThumb());
		// titleDaily.setTitle(daily.getTitle());
		// titleDaily.setWenJuan(daily.isWenJuan());
		// titleDaily.setNotice(daily.getNotice());
		// titleDaily.setAllowShare(daily.getAllowShare());
		// dailyList.add(0,titleDaily);
		// }
		for (int i = 0; i < dailyList.size();) {
			Daily daily = dailyList.get(i);
			int j = 1;// 累加器
			if (i + j == dailyList.size()) {// 当前已是末尾元素
				daily.setType(Daily.ITEM);
				Daily groupDaily = new Daily();
				groupDaily.setType(Daily.SECTION);
				groupDaily.setDate(daily.getDate());
				groupDaily.setInputtime(daily.getInputtime());
				dailyList.add(i, groupDaily);
				break;
			}
			Daily nextDaily = dailyList.get(i + j);
			while (daily.getDate().equals(nextDaily.getDate())) {// 把当前日报和后面的日报日期比较
				nextDaily.setType(Daily.ITEM);
				j++;
				if (i + j == dailyList.size()) {// 已比到末尾
					break;
				}
				nextDaily = dailyList.get(i + j);
			}
			daily.setType(Daily.ITEM);
			Daily groupDaily = new Daily();
			groupDaily.setType(Daily.SECTION);
			groupDaily.setDate(daily.getDate());
			groupDaily.setInputtime(daily.getInputtime());
			dailyList.add(i, groupDaily);
			i = i + j + 1;
		}
		if (dailyList.size() > 0) {
			Daily daily = dailyList.get(1);
			Daily titleDaily = new Daily();
			titleDaily.setTop(true);
			titleDaily.setInputtime(daily.getInputtime());
			titleDaily.setId(daily.getId());
			titleDaily.setThumb(daily.getThumb());
			titleDaily.setTitle(daily.getTitle());
			titleDaily.setWenJuan(daily.isWenJuan());
			titleDaily.setNotice(daily.getNotice());
			titleDaily.setAllowShare(daily.getAllowShare());
			titleDaily.setArticalType(daily.getArticalType());
			dailyList.add(0, titleDaily);
			dailyList.remove(2);
			dailyList.remove(1);
		}
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((DailyList) seri);
	}

	@Override
	protected void sendRequestData() {
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		if(AppContext.instance().isLogin()){
			int uid = AppContext.instance().getLoginUid();
			NewsApi.getDailyList(uid, mCurrentPage, mJsonHandler);
		}else{
			NewsApi.getDailyList(1, mCurrentPage, mJsonHandler);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Daily daily = (Daily) mAdapter.getItem(position - 1);
		// if (daily != null) {
		//
		// if (daily.isWenJuan()) {
		// UIHelper.showQuestionCase(view.getContext(), daily);
		// } else {
		// UIHelper.showDailyDetailRedirect(view.getContext(), daily);
		// }
		// }
	}

}
