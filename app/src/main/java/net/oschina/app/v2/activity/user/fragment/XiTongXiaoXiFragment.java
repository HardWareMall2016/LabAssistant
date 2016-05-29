package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.XiTongXiaoXiAdapter;
import net.oschina.app.v2.activity.user.model.XiTongXiaoXiList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class XiTongXiaoXiFragment extends BaseListFragment {
	protected static final String TAG = XiTongXiaoXiFragment.class
			.getSimpleName();
	// 缓存的前缀
	private static final String CACHE_KEY_PREFIX_5 = "xitongcaches_4";

	// 适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new XiTongXiaoXiAdapter();
	}
	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_5;
	}
	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
		XiTongXiaoXiList list = XiTongXiaoXiList.parse(result);
		is.close();
		return list;
	}
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((XiTongXiaoXiList) seri);
	}
	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
		int uid=AppContext.instance().getLoginUid();
		NewsApi.getXiTongXiaoXi(uid, mCurrentPage, mJsonHandler);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
}
