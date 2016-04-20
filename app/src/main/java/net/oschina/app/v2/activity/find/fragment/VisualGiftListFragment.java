package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.GiftAdapter;
import net.oschina.app.v2.activity.user.model.GiftList;
import net.oschina.app.v2.activity.user.model.GiftPojo;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

//虚拟礼物
public class VisualGiftListFragment extends BaseListFragment {

	private boolean isOpening = false;

	protected static final String TAG = VisualGiftListFragment.class
			.getSimpleName();
	// 缓存的前缀
	private static final String CACHE_KEY_PREFIX_12 = "giftlist_2";

	// 适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new GiftAdapter(getActivity());
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_12;
	}

	@Override
	protected void initViews(View view) {
		super.initViews(view);
		mListView.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		mListView.getRefreshableView().setBackgroundColor(
				getResources().getColor(R.color.transparent));
		mListView.setOnItemClickListener(null);
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
		GiftList list = GiftList.parse(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((GiftList) seri);
	}

	// 发送请求的数据。
	@Override
	protected void sendRequestData() {

		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		NewsApi.getGiftList(AppContext.instance().getLoginUid(), mCurrentPage,
				2, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (isOpening) {
			return;
		}
		isOpening = true;
		GiftPojo daily = (GiftPojo) mAdapter.getItem(position - 1);
		
		if (daily != null) {
			requestGiftDetailInfo(daily);
		}

	}

	private void showGiftDetailInfo(GiftPojo daily) {
		if (!GiftDetailInfoDialog.getInstance().isOpen())
			GiftDetailInfoDialog.getInstance().createReviceUserDialog(
					getActivity(), daily);

	}

	private void requestGiftDetailInfo(GiftPojo daily) {
		NewsApi.getGiftDetail(AppContext.instance().getLoginUid(),
				daily.getId(), addInterestedHandler);
	}

	protected JsonHttpResponseHandler addInterestedHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

			try {
				JSONArray dataList = new JSONArray(response.getString("data"));
				if (dataList.length() > 0) {

					GiftPojo pojo = GiftPojo.parse(dataList.getJSONObject(0));
					
					
					showGiftDetailInfo(pojo);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isOpening = false;

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			isOpening = false;
			AppContext.showToast("获取详情失败了");
		}
	};

}
