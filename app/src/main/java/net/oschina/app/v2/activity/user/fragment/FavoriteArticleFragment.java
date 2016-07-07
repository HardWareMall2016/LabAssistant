package net.oschina.app.v2.activity.user.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.favorite.adapter.FavoriteAdapter;
import net.oschina.app.v2.activity.favorite.fragment.FavoriteFragment;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.FavoriteList;
import net.oschina.app.v2.model.FavoriteList.Favorite;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.FavoriteRefreshMe;
import net.oschina.app.v2.model.event.FavoriteRefreshOther;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;

import de.greenrobot.event.EventBus;

/**
 * @author acer
 * 喜欢我的
 */
public class FavoriteArticleFragment extends BaseListFragment implements OnClickListener {

	protected static final String TAG = FavoriteFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "favorite_article_list";
	private static final String FAVORITE_SCREEN = "favorite_article_screen";

	@Override
	protected ListBaseAdapter getListAdapter() {
		FavoriteAdapter adapter=new FavoriteAdapter();
		adapter.setIsAttention(true);
		adapter.setOnClickListener(this);
		EventBus.getDefault().register(this);
		return adapter;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}
	
	/**
	 * 刷新列表
	 * @param message
	 */
	public void onEventMainThread(FavoriteRefreshOther message){
		onRefresh(mListView);
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
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
		// 响应结果存在问题，多返回了一个data节点
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		NewsApi.getCollectArticleList(AppContext.instance().getLoginUid(),mCurrentPage, mJsonHandler);
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
//	@Override
//	public View onCreateView(LayoutInflater inflater,
//			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//		View view = inflater.inflate(R.layout.woguanzhude, null);
//		return view;
//	}
	
	@Override
	public void onClick(View v) {
		Favorite f = (Favorite)v.getTag();
		int uid=AppContext.instance().getLoginUid();
		NewsApi.addAttention(uid, f.getFuid(), new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				AppContext.showToast(response.optString("desc", ""));
				mState=STATE_REFRESH;
				onRefresh(mListView);
				EventBus.getDefault().post(new FavoriteRefreshMe());
			}
		});
	}
}
