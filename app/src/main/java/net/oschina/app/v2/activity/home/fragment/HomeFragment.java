package net.oschina.app.v2.activity.home.fragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter.OnOperationListener;
import net.oschina.app.v2.activity.find.adapter.ActivityCenterViewPagerAdapter;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.activity.find.view.CustomViewPager;
import net.oschina.app.v2.activity.home.adapter.HomeAdapter;
import net.oschina.app.v2.activity.home.model.Ad;
import net.oschina.app.v2.activity.home.model.AdList;
import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.cache.CacheManager;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.emoji.EmojiFragment.EmojiTextListener;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.Home;
import net.oschina.app.v2.model.HomeList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.NewsList;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class HomeFragment extends HomeBaseFragment implements
		OnRefreshListener<ListView>, EmojiTextListener, EmojiFragmentControl,
		OnOperationListener, OnItemClickListener {
	protected static final String TAG = HomeFragment.class.getSimpleName();
	private static final int REQUEST_CODE = 0x1;
	private static final String CACHE_KEY_PREFIX = "newslist_";
	private static final String CACHE_KEY_TWEET_COMMENT = "tweet_comment_";
	private static final String TWEET_DETAIL_SCREEN = "tweet_detail_screen";
	private PullToRefreshListView mRefreshView;
	private ListView mListView;
	private EmptyLayout mEmptyView;
	private TextView loginBtn;
	private ActivityCenterViewPagerAdapter viewPagerAdapter;
	private MainActivity mMainActivity;

	private ArrayList<ImageView> imageList;
	protected int lastPosition;
	private boolean flag = false;
	private boolean isPullRefresh = false;

	private List<View> containerViews = new ArrayList<View>();
	private int mCurrentPage = 0;
	private int mLastId = 0;
	private HomeAdapter mAdapter;
	private EmojiFragment mEmojiFragment;
	private BroadcastReceiver mCommentReceiver;
	private LinearLayout group_login_hint;
	int positon = 0;// 默认广告位的索引
	private CustomViewPager mViewPager;
	private LinearLayout pointLinear;

	private List<Ad> homeAds = new ArrayList<Ad>();// 首页轮播海报

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
					if (AppContext.instance().isLogin())
						if (mLastId != 0) {
							sendRequestHomeLabData(false);
						}

				}
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				containerViews.clear();
				pointLinear.removeAllViews();

				if(getActivity()==null){
					return;
				}
				for (int i = 0; i < homeAds.size(); i++) {
					ImageView pointView = new ImageView(getActivity());
					if (i == 0) {
						pointView
								.setBackgroundResource(R.drawable.feature_point_cur);
					} else {
						pointView
								.setBackgroundResource(R.drawable.feature_point);
					}
					pointLinear.addView(pointView);
					View view1 = LayoutInflater.from(getActivity()).inflate(
							R.layout.home_pager_item, null);
					ImageView iamgeView = (ImageView) view1
							.findViewById(R.id.iv_activitycenter_vpitem);
					TextView tv_pic_title=(TextView) view1.findViewById(R.id.tv_pic_title);

//					ImageLoader.getInstance().displayImage(
//							homeAds.get(i).getimage(), iamgeView, options);
					
					ImageLoader.getInstance().displayImage(
							homeAds.get(i).getimage(), iamgeView);

					tv_pic_title.setText(homeAds.get(i).gettitle());
					view1.setTag(homeAds.get(i));
					// TODO
					view1.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (null != v.getTag() && v.getTag() instanceof Ad) {
								Ad ad = (Ad) v.getTag();
								Intent intent = new Intent(getActivity(),
										ShowTitleDetailActivity.class);
								intent.putExtra("id", ad.getArticleid());
								intent.putExtra("type", 1);
								// intent.putExtra("img", ad.getimage());

								getActivity().startActivity(intent);

							}
						}
					});

					containerViews.add(view1);
				}

				mhandler.removeMessages(2);
				viewPagerAdapter.changeData(containerViews);

				mhandler.sendEmptyMessageDelayed(2, 3000);
				break;
			case 2:
				int position = mViewPager.getCurrentItem();
				position++;

				if (position >= viewPagerAdapter.getCount()) {
					position = 0;
				}
				mViewPager.setCurrentItem(position);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mMainActivity = (MainActivity) activity;
	}

	@Override
	public void onDestroy() {

		mhandler.removeMessages(2);

		if (mCommentReceiver != null) {
			getActivity().unregisterReceiver(mCommentReceiver);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_home_detail,
				container, false);

		initViews(view);
		if (TDevice.hasInternet()) {
			sendRequestAdData();
		} else {
			new CacheTask(getActivity(), CacheTask.TYPE_HOMEAD)
					.execute(getCacheKey("homead_", 0));
		}
		if (mLastId == 0) {
			executeOnLoadDataSuccess(true);
		}

		return view;
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		executeOnLoadDataSuccess(true);
	}

	protected void executeOnLoadFinish() {
		mRefreshView.onRefreshComplete();
		mState = STATE_NONE;
	}

	@SuppressLint("InflateParams")
	private void initViews(View view) {
		mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
		mRefreshView = (PullToRefreshListView) view.findViewById(R.id.listview);
		mRefreshView.setOnRefreshListener(this);

		mListView = mRefreshView.getRefreshableView();
//		group_login_hint = (LinearLayout) view
//				.findViewById(R.id.group_login_hint);
//		loginBtn = (TextView) view.findViewById(R.id.iv_login);
//		loginBtn.setOnClickListener(new ImageButton.OnClickListener() {
//			public void onClick(View v) { // 使用匿名的Button实例
//				mMainActivity.changeContent(R.id.layout_me);
//			}
//		});

		mListView.setOnScrollListener(mScrollListener);

		// mListView.setOnItemClickListener(this);
		View header = LayoutInflater.from(getActivity()).inflate(
				R.layout.home_banner, null);
		mViewPager = (CustomViewPager) header
				.findViewById(R.id.viewpager_activitycenter);
		viewPagerAdapter = new ActivityCenterViewPagerAdapter(containerViews);
		mViewPager.setAdapter(viewPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				View view = pointLinear.getChildAt(positon);
				View curView = pointLinear.getChildAt(arg0);
				if (view != null && curView != null) {
					ImageView pointView = (ImageView) view;
					ImageView curPointView = (ImageView) curView;
					pointView.setBackgroundResource(R.drawable.feature_point);
					curPointView
							.setBackgroundResource(R.drawable.feature_point_cur);
					positon = arg0;
				}

				mhandler.removeMessages(2);
				mhandler.sendEmptyMessageDelayed(2, 5000);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		pointLinear = (LinearLayout) header
				.findViewById(R.id.gallery_point_linearcenter);
		group_login_hint = (LinearLayout) header
				.findViewById(R.id.group_login_hint);
		loginBtn = (TextView) header.findViewById(R.id.iv_login);
		loginBtn.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View v) { // 使用匿名的Button实例
				mMainActivity.changeContent(R.id.layout_me);
			}
		});
		mListView.addHeaderView(header);
		mAdapter = new HomeAdapter(getActivity());
		mListView.setAdapter(mAdapter);

	}

	private void executeOnLoadDataSuccess(boolean isRefresh) {

		if (AppContext.instance().isLogin()) {
			mState = STATE_REFRESH;
			mCurrentPage = 1;
			flag = false;
			if(!isPullRefresh){
				mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
			}
			sendRequestAdData();
			sendRequestHomeLabData(isRefresh);
		} else {
			mState = STATE_REFRESH;
			mCurrentPage = 1;
			flag = true;
			sendRequestAdData();
		}

	}

	private void sendRequestHomeLabData(boolean isRefresh) {
		User user = AppContext.instance().getLoginInfo();
		if (TDevice.hasInternet()) {
			if (isRefresh) {
				isPullRefresh = true;
				NewsApi.getHomeList(user.getId(), 0, mHomeHandler);
			} else {
				isPullRefresh = false;
				NewsApi.getHomeList(user.getId(), mLastId, mHomeHandler);
			}
		} else {
			new CacheTask(getActivity(), CacheTask.TYPE_HOMELIST)
					.execute(getCacheKey("homelist_", mLastId));
		}
	}

	private void sendRequestAdData() {
		NewsApi.getHomeAdList(1, mHomeAdHandler);
	}

	@Override
	public void setEmojiFragment(EmojiFragment fragment) {
		mEmojiFragment = fragment;
		mEmojiFragment.setEmojiTextListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Home post = (Home) parent.getItemAtPosition(position);
		if (post != null) {
			Ask ask = new Ask();
			// ask.setId(post.getvalue());
			// ask.setanum(post.getanum());
			// ask.setLabel(post.getlabel());
			// ask.setContent(post.getquestion());
			// johnny
			// ask.setinputtime(post.getinputtime());
			// ask.setsuperlist(post.getsuperlist());
			// ask.setnickname(post.getnickname());
			UIHelper.showTweetDetail(view.getContext(), ask);
		}
	}

	private JsonHttpResponseHandler mHomeHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			if(getActivity()==null){
				return;
			}
			try {
				HomeList list = HomeList.parse(response.toString());
				new SaveCacheTask(getActivity(), list, getCacheKey("homelist_",
						mLastId)).execute();
				executeOnLoadCommentDataSuccess(list);
				executeOnLoadFinish();
			} catch (Exception e) {
				e.printStackTrace();
				new CacheTask(getActivity(), CacheTask.TYPE_HOMELIST)
						.execute(getCacheKey("homelist_", mLastId));
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}

		public void onFinish() {
			if(isPullRefresh){
				mState = STATE_REFRESH;
			}
		}
	};

	private void executeOnLoadCommentDataSuccess(HomeList list) {
		if (mState == STATE_REFRESH)
			mAdapter.clear();
		List<Home> data = list.getHomelist();
		mLastId = list.getLastid();
		mAdapter.addData(data);
		mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
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
	
	private JsonHttpResponseHandler mHomeAdHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				AdList list = AdList.parse(response.toString());
				if (null != list.getAdlist() && !list.getAdlist().isEmpty()) {
					new SaveCacheTask(getActivity(), list, getCacheKey(
							"homead_", 0)).execute();

					homeAds.clear();
					homeAds.addAll(list.getAdlist());

					for (int i = 0; i < homeAds.size(); i++) {
						StringBuilder urlstl = new StringBuilder();

						urlstl.append(ApiHttpClient.getImageApiUrl(homeAds.get(
								i).getimage()));

						homeAds.get(i).setimage(urlstl.toString());
					}

					mhandler.sendEmptyMessageDelayed(1, 0);
				}
				if(flag){
					mRefreshView.onRefreshComplete();
				}
			} catch (Exception e) {
				e.printStackTrace();
				new CacheTask(getActivity(), CacheTask.TYPE_HOMEAD)
						.execute(getCacheKey("homead_", 0));
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}

		public void onFinish() {
			
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TWEET_DETAIL_SCREEN);
		MobclickAgent.onResume(getActivity());

		checkIsUserLogin();

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TWEET_DETAIL_SCREEN);
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onMoreClick(Comment comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendClick(String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserLogin() {
		checkIsUserLogin();
	}

	private void checkIsUserLogin() {
		if (AppContext.instance().isLogin()) {
			if(mAdapter.getCount()==0)
			 sendRequestHomeLabData(true);
			group_login_hint.setVisibility(View.GONE);
			
		
		} else {
			group_login_hint.setVisibility(View.VISIBLE);
			mAdapter.clear();
			mAdapter.notifyDataSetChanged();
		}
	}

	private String getCacheKey(String cacheKeyPrefix, int mCurrentPage) {
		return new StringBuffer(cacheKeyPrefix).append(NewsList.CATALOG_ALL)
				.append("_").append(mCurrentPage).append("_")
				.append(TDevice.getPageSize()).toString();
	}

	/**
	 * 写缓存
	 */
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

	/**
	 * 读缓存
	 */
	private class CacheTask extends AsyncTask<String, Void, ListEntity> {
		public static final int TYPE_HOMELIST = 1;
		public static final int TYPE_HOMEAD = 2;

		private WeakReference<Context> mContext;
		private int mType;

		private CacheTask(Context context, int type) {
			mContext = new WeakReference<Context>(context);
			mType = type;
		}

		@Override
		protected ListEntity doInBackground(String... params) {
			Serializable seri = CacheManager.readObject(mContext.get(),
					params[0]);
			if (seri == null) {
				return null;
			} else {
				if (mType == TYPE_HOMEAD) {
					return (AdList) seri;
				} else {
					return (HomeList) seri;
				}
			}
		}

		@Override
		protected void onPostExecute(ListEntity list) {
			super.onPostExecute(list);
			if (list != null) {
				if (mType == TYPE_HOMEAD) {
					homeAds.clear();
					homeAds.addAll(((AdList) list).getAdlist());

					for (int i = 0; i < homeAds.size(); i++) {
						// StringBuilder urlstl = new StringBuilder();

						// urlstl.append(ApiHttpClient.getImageApiUrl(homeAds.get(
						// i).getimage()));

						// homeAds.get(i).setimage(urlstl.toString());
					}

					mhandler.sendEmptyMessageDelayed(1, 0);
				} else if (mType == TYPE_HOMELIST) {
					executeOnLoadCommentDataSuccess((HomeList) list);
				}
			} else {
				// executeOnLoadDataError(null);
			}
			executeOnLoadFinish();
		}
	}

}
