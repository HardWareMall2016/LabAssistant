package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.adapter.WeeklyAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.Week;
import net.oschina.app.v2.model.WeekList;
import net.oschina.app.v2.utils.UIHelper;
import android.view.View;
import android.widget.AdapterView;

/**
 * 新闻资讯
 * 
 * @author william_sim
 */
public class WeeklyFragment extends BaseListFragment {

	protected static final String TAG = WeeklyFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "weeklist_";
	private int mCatalog;

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new WeeklyAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		WeekList list = WeekList.parseList(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((WeekList) seri);
	}

	@Override
	protected void sendRequestData() {
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		if(AppContext.instance().isLogin()){
			int uid = AppContext.instance().getLoginUid();
			NewsApi.getWeekList(uid, mCurrentPage, mJsonHandler);
		}else{
			NewsApi.getWeekList(1, mCurrentPage, mJsonHandler);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Week week = (Week) mAdapter.getItem(position - 1);
		if (week != null) {
			UIHelper.showWeekDetailRedirect(view.getContext(), week,week.isHeader() ? 1 : 2);
//			UIHelper.showWeekDetailRedirect(view.getContext(), week,1);
		}
	}
	
	
}
