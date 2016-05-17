package net.oschina.app.v2.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.cache.CacheManager;
import net.oschina.app.v2.model.Base;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.NewsList;
import net.oschina.app.v2.model.Notice;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public abstract class BaseListFragment extends BaseTabFragment implements
		OnRefreshListener<ListView>, OnLastItemVisibleListener,
		OnItemClickListener {
	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
	protected PullToRefreshListView mListView;
	protected ListBaseAdapter mAdapter;
	protected EmptyLayout mErrorLayout;

	protected int mStoreEmptyState = -1;
	protected int mCurrentPage = 1;
	protected int mCatalog = NewsList.CATALOG_ALL;
	private AsyncTask<String, Void, ListEntity> mCacheTask;
	private ParserTask mParserTask;
	protected int getLayoutRes() {
		return R.layout.v2_fragment_pull_refresh_listview;
	}

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mCatalog = args.getInt(BUNDLE_KEY_CATALOG);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(getLayoutRes(), container, false);
		initViews(view);
		return view;
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
		mListView = (PullToRefreshListView) view.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnLastItemVisibleListener(this);

		addHeaderView(mListView.getRefreshableView());
		
		if (mAdapter != null) {
			mListView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getListAdapter();
			mListView.setAdapter(mAdapter);

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
		
	
	}
	
	protected void addHeaderView(ListView listview) {
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		cancelReadCacheTask();
		cancelParserTask();
		super.onDestroy();
	}

	protected abstract ListBaseAdapter getListAdapter();

	protected boolean requestDataIfViewCreated() {
		return true;
	}

	protected String getCacheKeyPrefix() {
		return null;
	}

	protected ListEntity parseList(InputStream is) throws Exception {
		return null;
	}

	protected ListEntity readList(Serializable seri) {
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mCurrentPage = 1;
		mState = STATE_REFRESH;
		requestData(true);
	}

	public void setRefresh(){
		mCurrentPage = 1;
		mState = STATE_REFRESH;
		mListView.setRefreshing();
	}

	@Override
	public void onLastItemVisible() {
		if (mState == STATE_NONE) {
			if (mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
				mCurrentPage++;
				mState = STATE_LOADMORE;
				requestData(false);
			}
		}
	}

	protected String getCacheKey() {
		return new StringBuffer(getCacheKeyPrefix()).append(mCatalog)
				.append("_").append(mCurrentPage).append("_")
				.append(TDevice.getPageSize()).toString();
	}

	protected void requestData(boolean refresh) {
		String key = getCacheKey();
		if (TDevice.hasInternet()){
//				&& (!CacheManager.isReadDataCache(getActivity(), key) || refresh)) {
			sendRequestData();
		} else {
			readCacheData(key);
		}
		//sendRequestData();
	}

	protected void sendRequestData() {
	}

	protected void readCacheData(String cacheKey) {
		cancelReadCacheTask();
		mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
	}

	private void cancelReadCacheTask() {
		if (mCacheTask != null) {
			mCacheTask.cancel(true);
			mCacheTask = null;
		}
	}

	public String inStream2String(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return new String(baos.toByteArray());
	}

	private class CacheTask extends AsyncTask<String, Void, ListEntity> {
		private WeakReference<Context> mContext;

		private CacheTask(Context context) {
			mContext = new WeakReference<Context>(context);
		}

		@Override
		protected ListEntity doInBackground(String... params) {
			Serializable seri = CacheManager.readObject(mContext.get(),
					params[0]);
			if (seri == null) {
				return null;
			} else {
				return readList(seri);
			}
		}

		@Override
		protected void onPostExecute(ListEntity list) {
			super.onPostExecute(list);
			if (list != null) {
				executeOnLoadDataSuccess(list.getList());
			} else {
				executeOnLoadDataError(null);
			}
			executeOnLoadFinish();
		}
	}

	private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
		private WeakReference<Context> mContext;
		private Serializable seri;
		private String key;

		private SaveCacheTask(Context context, Serializable seri, String key) {
			mContext = new WeakReference<Context>(context);
			this.seri = seri;
			this.key = key;
		}

		@Override
		protected Void doInBackground(Void... params) {
			CacheManager.saveObject(mContext.get(), seri, key);
			return null;
		}
	}

	protected AsyncHttpResponseHandler mHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBytes) {
			if (isAdded()) {
				if (mState == STATE_REFRESH) {
					onRefreshNetworkSuccess();
				}
				executeParserTask(responseBytes);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			if (isAdded()) {
				readCacheData(getCacheKey());
			}
		}
	};
	//获取分类
	protected JsonHttpResponseHandler mJsonHandler = new JsonHttpResponseHandler() {
		@SuppressWarnings("static-access")
		public void onSuccess(JSONObject response) {
		
			if (isAdded()) {
				String responseStr = response.toString();
				byte[] responseBytes = null;
				try {
					responseBytes = responseStr.getBytes("UTF8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (mState == STATE_REFRESH) {
					onRefreshNetworkSuccess();
				}
				executeParserTask(responseBytes);
			/*	try {
					int catid = response.getInt("catid");
					Editor edit = AppContext.instance().getPersistPreferences().edit();
					edit.putInt("catid", catid);
					AppContext.showToast(AppContext.instance().getPersistPreferences().getInt("catid", 0)+"");
					} catch (JSONException e) {
				}*/
			}
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			//显示主要分类
			if (isAdded()) {
				//显示主要分类
				String responseStr = response.toString();
				byte[] responseBytes = null;
				try {
					responseBytes = responseStr.getBytes("UTF8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mState == STATE_REFRESH) {
					onRefreshNetworkSuccess();
				}
				executeParserTask(responseBytes);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (isAdded()) {
				String responseStr = errorResponse != null ? errorResponse.toString() : "";
				byte[] responseBytes = null;
				try {
					responseBytes = responseStr.getBytes("UTF8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (mState == STATE_REFRESH) {
					onRefreshNetworkSuccess();
				}
				executeParserTask(responseBytes);
			}
		}
	};

	protected void executeOnLoadDataSuccess(List<?> data) {
		if (mState == STATE_REFRESH)
		{
			mAdapter.clear();
		}
		
		mAdapter.addData(data);
		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		if (data.size() == 0 && mState == STATE_REFRESH) 
		{
			if(mAdapter.getCount() == 0)
			{
				if (isShowEmpty()) {
					mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
				}
			} else
			{
				AppContext.showToast("没有获取到新数据");
			}
		} else if (data.size() < TDevice.getPageSize()) 
		{
			if (mState == STATE_REFRESH)
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
			else
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else {
			mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
		}
	}

	protected boolean isShowEmpty() {
		return true;
	}
	
	protected void onRefreshNetworkSuccess() {
	}

	protected void executeOnLoadDataError(String error) {
		if (mCurrentPage == 1) {
			mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
		} else {
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
			mAdapter.setState(ListBaseAdapter.STATE_NETWORK_ERROR);
			mAdapter.notifyDataSetChanged();
		}
	}

	protected void executeOnLoadFinish() {
		mListView.onRefreshComplete();
		mState = STATE_NONE;
	}

	private void executeParserTask(byte[] data) {
		cancelParserTask();
		mParserTask = new ParserTask(data);
		mParserTask.execute();
	}

	private void cancelParserTask() {
		if (mParserTask != null) {
			mParserTask = null;
		}
	}

	class ParserTask extends AsyncTask<Void, Void, String> {

		private byte[] reponseData;
		private boolean parserError;
		private List<?> list;

		public ParserTask(byte[] data) {
			this.reponseData = data;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				ListEntity data = parseList(new ByteArrayInputStream(
						reponseData)); 
				if (data instanceof Base) {
					Notice notice = ((Base) data).getNotice();
					if (notice != null) {
						UIHelper.sendBroadCast(getActivity(), notice);
					}
				}
				new SaveCacheTask(getActivity(), data, getCacheKey()).execute();
				list = data.getList();
			} catch (Exception e) {
				e.printStackTrace();
				parserError = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (parserError) {
				readCacheData(getCacheKey());
			} else {
				executeOnLoadDataSuccess(list);
				executeOnLoadFinish();
			}
		}
	}
}
