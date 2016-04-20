package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.fragment.GiftDetailInfoDialog;
import net.oschina.app.v2.activity.user.adapter.ShiWuLiPinAdapter;
import net.oschina.app.v2.activity.user.model.GiftPojo;
import net.oschina.app.v2.activity.user.model.ShiWu;
import net.oschina.app.v2.activity.user.model.ShiWuList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ShiWuLiPinFragment extends BaseListFragment {
	protected static final String TAG = MyQuestionFragment.class
			.getSimpleName();
	// 缓存的前缀
	private static final String CACHE_KEY_PREFIX_12 = "shiwulist_1";

	// 适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new ShiWuLiPinAdapter();
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_12;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
		ShiWuList list = ShiWuList.parse(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((ShiWuList) seri);
	}

	// 发送请求的数据。
	@Override
	protected void sendRequestData() {

		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;

		NewsApi.getShiWuList(AppContext.instance().getLoginUid(), mCurrentPage,
				1, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		ShiWu daily = (ShiWu) mAdapter.getItem(position - 1);
		if (daily != null) {
			requestGiftDetailInfo(daily.getProid());
		}
	}

	private void showGiftDetailInfo(GiftPojo daily) {
		if (!GiftDetailInfoDialog.getInstance().isOpen())
			GiftDetailInfoDialog.getInstance().createReviceUserDialog(
					getActivity(), daily);
	}

	private void requestGiftDetailInfo(int productId) {
		NewsApi.getGiftDetail(AppContext.instance().getLoginUid(), productId,
				addInterestedHandler);
	}

	protected JsonHttpResponseHandler addInterestedHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

			try {

				String str = response.optString("data");
				if (TextUtils.isEmpty(str)) {
					AppContext.showToast(response.optString("desc"));
					return;
				}

				JSONArray dataList = new JSONArray(response.getString("data"));
				if (dataList.length() > 0) {

					GiftPojo pojo = GiftPojo.parseDetail(dataList.getJSONObject(0));
					pojo.setRechargestatus(1);
					showGiftDetailInfo(pojo);

				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			AppContext.showToast("获取详情失败了");
		}
	};
}
