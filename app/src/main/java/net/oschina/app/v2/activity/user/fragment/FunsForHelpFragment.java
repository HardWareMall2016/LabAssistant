package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.adapter.funsForHelperAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.utils.UIHelper;
import android.view.View;
import android.widget.AdapterView;

public class FunsForHelpFragment extends BaseListFragment {
	protected static final String TAG = FunsForHelpFragment.class
			.getSimpleName();
	private static final String CACHE_KEY_PREFIX_10 = "funsforhelplist_";

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new funsForHelperAdapter(getActivity());
		//return new FunsForHelpAdapter();
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_10;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
//		FunsForHelpList list = FunsForHelpList.parse(result);
		AskList list = AskList.parse(result);
		is.close();
		return list;
	}
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AskList) seri);
		//return ((FunsForHelpList) seri);
	}
	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
//		int uid=AppContext.instance().getLoginUid();
//		NewsApi.getFunsForHelpList(uid, mCurrentPage, mJsonHandler);
		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFunsForHelpList(AppContext.instance().getLoginUid(), mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		FunsForHelp forHelp = (FunsForHelp) parent.getItemAtPosition(position);
//		if (forHelp != null) {
//			Ask ask = new Ask();
//			ask.setId(forHelp.getId());
//			ask.setanum(forHelp.getAnum());
//			ask.setLabel(forHelp.getLable());
//			ask.setContent(forHelp.getContent());
//			//Johnny
//			//ask.setinputtime(forHelp.getIntputtime());
//			ask.setsuperlist(forHelp.getSuperlist());
//			ask.setnickname(forHelp.getNickname());
//			UIHelper.showTweetDetail(view.getContext(), ask);
//		}
		Ask ask = (Ask) mAdapter.getItem(position - 1);
		if (ask != null)
			UIHelper.showTweetDetail(view.getContext(),ask);
	}
}
