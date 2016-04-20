package net.oschina.app.v2.activity.find.fragment;


import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.activity.find.adapter.DailyAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.DailyList;
import net.oschina.app.v2.model.ListEntity;
import android.view.View;
import android.widget.AdapterView;

/**
 * 新闻资讯
 * 
 * @author william_sim
 */
public class BillboardFragment extends BaseListFragment {

	protected static final String TAG = BillboardFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "newslist_";

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new DailyAdapter(getActivity());
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result=inStream2String(is);
		DailyList list = DailyList.parse(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((DailyList) seri);
	}

	@Override
	protected void sendRequestData() {
		NewsApi.getDailyList(1, mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		Daily news = (Daily) mAdapter.getItem(position - 1);
//		if (news != null)
//			UIHelper.showNewsRedirect(view.getContext(), news);
	}
}
