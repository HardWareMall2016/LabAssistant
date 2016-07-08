package net.oschina.app.v2.activity.user.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import com.umeng.analytics.MobclickAgent;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.favorite.adapter.FavoriteArticleAdapter;
import net.oschina.app.v2.activity.favorite.fragment.FavoriteFragment;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.FavoriteArticleList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.FavoriteRefreshOther;

import java.io.InputStream;
import java.io.Serializable;

import de.greenrobot.event.EventBus;

/**
 * @author acer
 * 喜欢我的
 */
public class FavoriteArticleFragment extends BaseListFragment{

	protected static final String TAG = FavoriteFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "favorite_article_list";
	private static final String FAVORITE_SCREEN = "favorite_article_screen";

	@Override
	protected ListBaseAdapter getListAdapter() {
		FavoriteArticleAdapter adapter=new FavoriteArticleAdapter();
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
		FavoriteArticleList list = FavoriteArticleList.parse(result);
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((FavoriteArticleList) seri);
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
		/*Favorite item = (Favorite) mAdapter.getItem(position - 1);*/
		//if (item != null)
		//	UIHelper.showUrlRedirect(view.getContext(), item.url);
		FavoriteArticleList.FavoriteArticle article = (FavoriteArticleList.FavoriteArticle ) mAdapter.getItem(position - 1);
		if(article!=null){
			Intent intent = new Intent(getActivity(), ShowTitleDetailActivity.class);
			intent.putExtra("id", Integer.valueOf(article.getId()));
			startActivity(intent);
		}
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
}
