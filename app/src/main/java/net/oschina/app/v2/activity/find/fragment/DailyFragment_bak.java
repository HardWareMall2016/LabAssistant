package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.oschina.app.v2.activity.find.adapter.DailyAdapter;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.model.Daily;
import net.oschina.app.v2.model.DailyList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

/**
 * 新闻资讯
 * 
 * @author william_sim
 */
public class DailyFragment_bak extends BaseListFragment {

	protected static final String TAG = DailyFragment_bak.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "dailylist_";
	private int mCatalog;
	
	private ListView mListView;
	private ImageView header_bg;
	private TextView context_date, context_detail;
	private int mCurrentPage = 0;
	private DailyAdapter mAdapter;
	private EmojiFragment mEmojiFragment;
	private BroadcastReceiver mCommentReceiver;
	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (mAdapter != null
					&& mAdapter.getDataSize() > 0
					&& mListView.getLastVisiblePosition() == (mListView
							.getCount() - 1)) {
				if (mState == STATE_NONE
						&& mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
					mState = STATE_LOADMORE;
					mCurrentPage++;
					
					// sendRequestCommentData();
				}
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.daily_bak,
				container, false);
		
		
		initViews(view);
		
		return view;
	}
	
	@SuppressLint("InflateParams")
	protected void initViews(View view) {
		
		mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = 0;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});
		
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnScrollListener(mScrollListener);
		mListView.setOnItemClickListener(this);
		View header = LayoutInflater.from(getActivity()).inflate(R.layout.v2_list_header_context_detail, null);
		header_bg = (ImageView) header.findViewById(R.id.tv_header_bg);
		context_date = (TextView) header.findViewById(R.id.context_date);
		context_detail = (TextView) header.findViewById(R.id.tv_context_detail);
		mListView.addHeaderView(header);
		mAdapter = new DailyAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		context_date.setVisibility(View.GONE);
		
		if (requestDataIfViewCreated()) {
			mCurrentPage = 1;
			mState = STATE_REFRESH;
			mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
			requestData(false);
		} else {
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}
		
		if (mStoreEmptyState != -1) {
			mErrorLayout.setErrorType(mStoreEmptyState);
		}
		
		
	}
	private void executeOnLoadDailyDataSuccess(DailyList list) {
		if (mState == STATE_REFRESH)
		{
			mAdapter.clear();
		}
		
		List<Daily> data = list.getDailylist();
		mAdapter.addData(data);
		
		if(mAdapter.getCount() > 0 && null != mAdapter.getItem(0))
		{
			Daily firstDaily = (Daily) mAdapter.getItem(0);
			
			context_date.setVisibility(View.VISIBLE);
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日",Locale.CHINA);
			long time = firstDaily.getInputtime();
			Date date = new Date(time * 1000);
			String dateStr = sdf.format(date);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			switch(calendar.get(Calendar.DAY_OF_WEEK))
			{
			case 1:
				dateStr +="   星期一";
				break;
			case 2:
				dateStr +="   星期二";
				break;
			case 3:
				dateStr +="   星期三";
				break;
			case 4:
				dateStr +="   星期四";
				break;
			case 5:
				dateStr +="   星期五";
				break;
			case 6:
				dateStr +="   星期六";
				break;
			case 7:
				dateStr +="   星期日";
				break;
			}
			
			
			context_date.setText(dateStr);
			context_detail.setText(firstDaily.getTitle());
			
			ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(firstDaily.getThumb()),header_bg);
			
		}
		
		if(mAdapter.getCount() == 0)
		{
			mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
		} else
		{
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}
		
		if (data.size() == 0 && mState == STATE_REFRESH) {
			mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else if (data.size() < TDevice.getPageSize()) {
			if (mState == STATE_REFRESH)
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
			else
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else {
			mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
		}
	}
	private JsonHttpResponseHandler mdailyHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
				DailyList list = DailyList.parse(response.toString());
				executeOnLoadDailyDataSuccess(list);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}

		@Override
		 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			
			if(mAdapter.getCount() == 0)
			{
				mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
			} else
			{
				mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
			}
			
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
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
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		
		NewsApi.getDailyList(1, mCurrentPage, mdailyHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Daily daily = (Daily) mAdapter.getItem(position - 1);
		if (daily != null)
		{
			
			if(daily.isWenJuan())
			{
				UIHelper.showQuestionCase(view.getContext(), daily);
			} else
			{
				UIHelper.showDailyDetailRedirect(view.getContext(), daily);
			}
		}
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		return null;
	}

}
