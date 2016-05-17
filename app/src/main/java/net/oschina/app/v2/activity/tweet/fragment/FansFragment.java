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
import net.oschina.app.v2.utils.UIHelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import de.greenrobot.event.EventBus;

public class FansFragment extends BaseListFragment implements TweetPopupListView.OnFilterClickListener {

	protected static final String TAG = DailyFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "fanslist_";
	private int mStatus =1;//1 全部  2 已回答 3 未回答

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		return super.onCreateView(inflater, container, savedInstanceState);
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


