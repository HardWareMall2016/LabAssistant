package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.RenWuChengJiuAdapter;
import net.oschina.app.v2.activity.user.model.AnswerList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class RenWuChengJiuFragment extends BaseListFragment {
	protected static final String TAG = RenWuChengJiuFragment.class.getSimpleName();
	//缓存的前缀
	private static final String CACHE_KEY_PREFIX_6 = "renwuchengjiulist_";
	//适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new RenWuChengJiuAdapter();
	}
	//缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_6;
	}
    
	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		//流转换成为String
		String result=inStream2String(is).toString();
		//解析string得到集合。
		AnswerList list = AnswerList.parse(result);
		is.close();
		return list;
	}
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AnswerList) seri);
	}
	//发送请求的数据。
	@Override
	protected void sendRequestData() {
		NewsApi.getAnswerList(1,mCurrentPage,mJsonHandler) ;
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int uid=AppContext.instance().getLoginUid();
		NewsApi.getSubmittask(uid, "daylogin", new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				AppContext.showToast(response.optString("desc"));
			}
		});
	}
}
