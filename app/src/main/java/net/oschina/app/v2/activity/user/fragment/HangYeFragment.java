package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.activity.user.model.AskAgainList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;
import android.view.View;
import android.widget.AdapterView;

public class HangYeFragment extends BaseListFragment{
	protected static final String TAG = HangYeFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX_3 = "hangyelist_";
//	@Override
	protected ListBaseAdapter getListAdapter() {
		return mAdapter;
	}
	//缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_3;
	}
	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		//流转换成为String
		String result=inStream2String(is).toString();
		//解析string得到集合。
//		AskAgainList list = AskAgainList.parse(result);
		is.close();
//		return list ;
		return null;
	}
	
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AskAgainList) seri);
	}
	//发送请求的数据。
	@Override
	protected void sendRequestData() {
		NewsApi.getAskAgainList(1,mCurrentPage,mJsonHandler);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

}
