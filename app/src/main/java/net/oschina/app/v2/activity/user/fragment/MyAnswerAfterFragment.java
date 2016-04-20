package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.MyAnswerAfterAdapter;
import net.oschina.app.v2.activity.user.model.AskAgain;
import net.oschina.app.v2.activity.user.model.AskAgainList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;

public class MyAnswerAfterFragment extends BaseListFragment {
	protected static final String TAG = MyAnswerAfterFragment.class
			.getSimpleName();
	private static final String CACHE_KEY_PREFIX_2 = "myanswerafterlist_";
	private int mListLastId;//请求数据用，可以把他看作页码

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new MyAnswerAfterAdapter(getActivity());
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_2;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		JSONObject json = new JSONObject(result);
		mListLastId = json.optInt("lastid");
		// 解析string得到集合。
		AskAgainList list = AskAgainList.parseMyAnswerAfter(result);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AskAgainList) seri);
	}
	
	protected void requestData(boolean refresh) {
		String key = getCacheKey();
		if (TDevice.hasInternet()){
//				&& (!CacheManager.isReadDataCache(getActivity(), key) || refresh)) {
			if(refresh){
				sendRequestData(0);
			}else{
				sendRequestData(mListLastId);
			}
		} else {
			readCacheData(key);
		}
		//sendRequestData();
	}

	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
		int uid = AppContext.instance().getLoginUid();
		NewsApi.getMyAnswerAfterList(uid,0, mCurrentPage, mJsonHandler);
	}
	
	private void sendRequestData(int lastid) {
		int uid = AppContext.instance().getLoginUid();
		NewsApi.getMyAnswerAfterList(uid,lastid, mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AskAgain askAgain = (AskAgain) parent.getItemAtPosition(position);
		if (askAgain != null) {
			Ask ask = new Ask();
			ask.setId(askAgain.getQid());
			ask.setContent(askAgain.getQtitle());
			ask.setImage(askAgain.getQimage());
			Comment comment = new Comment();
			comment.setId(askAgain.getAid());
			comment.setqid(askAgain.getQid());
			comment.setnickname(askAgain.getAftername());
			handleReplyComment(ask, comment);
		}
	}

	private void handleReplyComment(Ask ask, Comment comment) {
		comment.setZhuiWen(true);
		UIHelper.showReAnswerCommunicat(getActivity(), ask, comment);
	}
	
}
