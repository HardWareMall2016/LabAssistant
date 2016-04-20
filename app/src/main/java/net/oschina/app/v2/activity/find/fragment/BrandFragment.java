package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.activity.find.adapter.BrandGridViewAdapter;
import net.oschina.app.v2.activity.find.adapter.MygridView;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.BrandList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.shiyanzhushou.app.R;

/**
 * 新闻资讯
 * @deprecated
 * @author william_sim
 */
public class BrandFragment extends BaseListFragment {

	protected static final String TAG = BrandFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "dailylist_";
	private int mcatalog;
	private MygridView mgridView;
	private BaseAdapter mAdapter;;

	@Override
	protected int getLayoutRes() {
		// TODO Auto-generated method stub
		return R.layout.a_brand;
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
//		mgridView = (MygridView) view.findViewById(R.id.gv_brand);
//
//		mgridView.setOnItemClickListener(this);
		
		if (mAdapter != null) {
			mgridView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getGridAdapter();
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
		mCurrentPage=1;
	}

	public BaseAdapter getGridAdapter() {
		// TODO Auto-generated method stub
		return new BrandGridViewAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		BrandList list = BrandList.parse(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((BrandList) seri);
	}

	@Override
	protected void sendRequestData() {
		NewsApi.getBrandList(1, mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		 Daily news = (Daily) mAdapter.getItem(position - 1);
//		 if (news != null)
//		 UIHelper.showNewsRedirect(view.getContext(), news);
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		// TODO Auto-generated method stub
		return null;
	}

}
