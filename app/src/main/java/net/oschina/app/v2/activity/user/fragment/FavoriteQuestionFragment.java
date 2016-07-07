package net.oschina.app.v2.activity.user.fragment;

import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
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
 *我喜欢的
 */
public class FavoriteQuestionFragment extends BaseListFragment implements OnClickListener {

	protected static final String TAG = FavoriteFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "favorite_question_list";
	private static final String FAVORITE_SCREEN = "favorite_question_screen";

	@Override
	protected ListBaseAdapter getListAdapter() {
		FavoriteAdapter adapter=new FavoriteAdapter();
		adapter.setOnClickListener(this);
		EventBus.getDefault().register(this);
		return adapter;
	}
	
	/**
	 * 刷新列表
	 * @param message
	 */
	public void onEventMainThread(FavoriteRefreshMe message){
		onRefresh(mListView);
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
		int uid=AppContext.instance().getLoginUid();
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		NewsApi.getCollectQuestionList(uid,mCurrentPage, mJsonHandler);
	}
	
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Favorite item = (Favorite) mAdapter.getItem(position - 1);
//		if (item != null)
//			UIHelper.showUrlRedirect(view.getContext(), item.url);
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
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
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
	/*	
		int uid=AppContext.instance().getLoginUid();
		NewsApi.unAttention(uid, f.getTuid(), new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				AppContext.showToast(response.optString("desc", ""));
				mState=STATE_REFRESH;
				onRefresh(mListView);
				EventBus.getDefault().post(new FavoriteRefreshOther());
			}
		});*/
		
		doCancelAttention(f);
	}
	
	protected void doCancelAttention(final Favorite f) {
		final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
		titleTv.setText("您确定要取消关注该用户吗？");
		// 设置监听
		Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
		zhichi.setText("确定");
		// 支持
		zhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int uid=AppContext.instance().getLoginUid();
				NewsApi.unAttention(uid, f.getTuid(), new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						AppContext.showToast(response.optString("desc", ""));
						mState=STATE_REFRESH;
						onRefresh(mListView);
						EventBus.getDefault().post(new FavoriteRefreshOther());
					}
				});
				dialog.dismiss();
			}
		});
		// 查看支持者
		Button chakanzhichi = (Button) window
				.findViewById(R.id.ib_chakanzhichi);
		chakanzhichi.setText("取消");
		chakanzhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
