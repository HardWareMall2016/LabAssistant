package net.oschina.app.v2.activity.tweet.fragment;

import java.io.InputStream;
import java.io.Serializable;

import de.greenrobot.event.EventBus;

import net.oschina.app.v2.activity.find.fragment.DailyFragment;
import net.oschina.app.v2.activity.tweet.adapter.TweetAdapter;
import net.oschina.app.v2.activity.tweet.view.TweetPopupView.OnFilterClickListener;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.ClearFilterConditions;
import net.oschina.app.v2.utils.UIHelper;
import android.view.View;
import android.widget.AdapterView;

public class TweetFragment extends BaseListFragment implements
		OnFilterClickListener {

	protected static final String TAG = DailyFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "tweetslist_";

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
	protected void sendRequestData() {
		if(STATE_REFRESH==mState){//下拉刷新清空筛选条件
			EventBus.getDefault().post(new ClearFilterConditions());
		}
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getAskList(mCurrentPage, mJsonHandler);
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
		mState=STATE_REFRESH;
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFilterList(mCurrentPage, catid, isreward, issolveed, 2, mJsonHandler);
	}
}
