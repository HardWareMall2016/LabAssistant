package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.adapter.ActivityCenterAdapter;
import net.oschina.app.v2.activity.find.adapter.ActivityCenterViewPagerAdapter;
import net.oschina.app.v2.activity.find.view.CustomViewPager;
import net.oschina.app.v2.activity.home.model.Ad;
import net.oschina.app.v2.activity.home.model.AdList;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ActivityCenter;
import net.oschina.app.v2.model.ActivityCenterList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

/**
 * 新闻资讯
 * 
 * @author william_sim
 */
public class ActivityCenterFragment extends BaseListFragment {

	protected static final String TAG = ActivityCenterFragment.class
			.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "activiycenterlist_";
	private View v;
	private int mCatalog;
	private ArrayList<View> views;
	private CustomViewPager mViewPager;
	private LinearLayout pointLinear;
	private List<View> containerViews = new ArrayList<View>();
	private ActivityCenterViewPagerAdapter viewPagerAdapter;
	private ActivityCenterList list;
	int itemNums;// 动态加载的图片数目
	private int CurrentItem;// 当前显示的广告位下标索引
	private List<Map<String, String>> couverslist;
	int positon = 0;// 默认广告位的索引
	public static final int CHANGE_VIEW_PAGER_ITEM = 2;
	
	private View ll_activity;
	
	private Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				
				containerViews.clear();
				pointLinear.removeAllViews();
				
				for (int i = 0; i < homeAds.size(); i++) {
					ImageView pointView = new ImageView(getActivity());
					if (i == 0) {
						pointView.setBackgroundResource(R.drawable.feature_point_cur);
					} else {
						pointView.setBackgroundResource(R.drawable.feature_point);
					}
					pointLinear.addView(pointView);
					View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.a_activitycenter_vpitem, null);
					ImageView iamgeView = (ImageView) view1.findViewById(R.id.iv_activitycenter_vpitem);
					TextView tv_pic_title=(TextView) view1.findViewById(R.id.tv_pic_title);
					
					ImageLoader.getInstance().displayImage(homeAds.get(i).getimage(),iamgeView);
					view1.setTag(homeAds.get(i));
					view1.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(null != v.getTag() && v.getTag() instanceof Ad)
							{
								Ad ad = (Ad) v.getTag();
								Intent intent = new Intent(getActivity(), ShowTitleDetailActivity.class);
								intent.putExtra("id", ad.getArticleid());
								intent.putExtra("fromTitle", R.string.find_huodongzhongxin);
								getActivity().startActivity(intent);
							}
						}
					});
					tv_pic_title.setText(homeAds.get(i).gettitle());
					
					containerViews.add(view1);
				}

				viewPagerAdapter.changeData(containerViews);
				
				mhandler.removeMessages(2);
				mhandler.sendEmptyMessageDelayed(2, 3000);
				
				break;
		
			case 2:
				
				if(viewPagerAdapter.getCount() == mViewPager.getCurrentItem() + 1)
				{
					mViewPager.setCurrentItem(0);

				} else
				{
					mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
				}
				
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected int getLayoutRes() {
		// TODO Auto-generated method stub
		return R.layout.activitycenter;
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new ActivityCenterAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		v = inflater.inflate(getLayoutRes(), container, false);
		
		mListView = (PullToRefreshListView) v
				.findViewById(R.id.listview_activitycenter);
		ll_activity = LayoutInflater.from(getActivity()).inflate(R.layout.activitycenter_head,null);
		
		mViewPager = (CustomViewPager) ll_activity
				.findViewById(R.id.viewpager_activitycenter);
		viewPagerAdapter = new ActivityCenterViewPagerAdapter(containerViews);
		mViewPager.setAdapter(viewPagerAdapter);

		pointLinear = (LinearLayout) ll_activity
				.findViewById(R.id.gallery_point_linearcenter);
		//pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));

		initViews(v);
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
					curPointView.setBackgroundResource(R.drawable.feature_point_cur);
					positon = arg0;
				}
				
				mhandler.removeMessages(2);
				mhandler.sendEmptyMessageDelayed(2, 3000);

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
		// new Thread(this).start();
		
		sendRequestAdData();

		return v;
	}

	/**
	 * 切换指示器的方法
	 * 
	 * @param cur
	 */
	public void changePointView(int cur) {

	}

	//

	@Override
	protected void initViews(View view) {

		// TODO Auto-generated method stub
		mErrorLayout = (EmptyLayout) view
				.findViewById(R.id.error_layout_activitycenter);
		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = 0;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.listview_activitycenter);
		mListView.getRefreshableView().addHeaderView(ll_activity);
		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnLastItemVisibleListener(this);

		if (mAdapter != null) {
			mListView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getListAdapter();
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

	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		is.close();
		list = ActivityCenterList.parse(result);
		
		
//		String[] imageurl = list.getAclist().get(0).getImglist().toString()
//				.split("\\|");
//
//		str = new ArrayList<String>();
//		for (int i = 0; i < imageurl.length - 1; i++) {
//
//			StringBuilder urlstl = new StringBuilder();
//			urlstl.append(ApiHttpClient.getImageApiUrl(imageurl[i]));
//			str.add(urlstl.toString());
//
//		}
//		itemNums = str.size();
		
		mhandler.sendEmptyMessageDelayed(1, 0);
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((ActivityCenterList) seri);
	}

	@Override
	protected void sendRequestData() {
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		if(AppContext.instance().isLogin()){
			int uid = AppContext.instance().getLoginUid();
			NewsApi.getActivityCenterList(uid, mCurrentPage, mJsonHandler);
		}else{
			NewsApi.getActivityCenterList(1, mCurrentPage, mJsonHandler);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try {
			ActivityCenter ac = (ActivityCenter) mAdapter.getItem(position-2);
			if (ac != null)
				UIHelper.showActivityCenterDetailRedirect(view.getContext(), ac,R.string.find_huodongzhongxin, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//////////////////// 
	private List<Ad> homeAds = new ArrayList<Ad>();//首页轮播海报
	
	private void sendRequestAdData() {
		NewsApi.getHomeAdList(8, mHomeAdHandler);
	}

	
	private JsonHttpResponseHandler mHomeAdHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			boolean ishaveAd = false;
			
			try {
				AdList list = AdList.parse(response.toString());
				if(null != list.getAdlist() && !list.getAdlist().isEmpty())
				{
					ishaveAd = true;
					
					homeAds.clear();
					homeAds.addAll(list.getAdlist());
					
					for (int i = 0; i < homeAds.size(); i++)
					{
						StringBuilder urlstl = new StringBuilder();
						
						urlstl.append(ApiHttpClient.getImageApiUrl(homeAds.get(i).getimage()));
						
						homeAds.get(i).setimage(urlstl.toString());
					}
					
					mhandler.sendEmptyMessageDelayed(1, 0);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			
			ll_activity.setVisibility(ishaveAd ? View.VISIBLE : View.GONE);
		}

	};
}
