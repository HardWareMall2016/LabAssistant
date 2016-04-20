package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.favorite.adapter.FavoriteAdapter;
import net.oschina.app.v2.activity.favorite.fragment.FavoriteFragment;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.FavoriteList;
import net.oschina.app.v2.model.FavoriteList.Favorite;
import net.oschina.app.v2.model.ListEntity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class XueXiZiLiaoFragment extends BaseListFragment{
	protected static final String TAG = FavoriteFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "favorite_list";
	private static final String FAVORITE_SCREEN = "favorite_screen";

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new FavoriteAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		FavoriteList list = FavoriteList.parse(result);
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((FavoriteList) seri);
	}

	@Override
	protected void sendRequestData() {
		NewsApi.getFavoriteList(AppContext.instance().getLoginUid(), mCatalog,
				mCurrentPage, mHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Favorite item = (Favorite) mAdapter.getItem(position - 1);
		//if (item != null)
		//	UIHelper.showUrlRedirect(view.getContext(), item.url);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FAVORITE_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FAVORITE_SCREEN);
		MobclickAgent.onPause(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.woguanzhude, null);
		return view;
	}
}
