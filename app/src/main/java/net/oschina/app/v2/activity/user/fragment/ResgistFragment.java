package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.activity.model.Question;
import net.oschina.app.v2.activity.model.QuestionList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.utils.UIHelper;
import android.view.View;
import android.widget.AdapterView;

public class ResgistFragment extends BaseListFragment{
	protected static final String TAG = MyQuestionFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX_11 = "questionlist_1";
	//缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_11;
	}
	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		//流转换成为String
		String result=inStream2String(is).toString();
		//解析string得到集合。
		QuestionList list =QuestionList.parse(result);
		is.close();
		return list;
	}
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((QuestionList) seri);
	}
	//发送请求的数据。
	@Override
	protected void sendRequestData() {
		NewsApi.getQustionList(1,mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//拿到条目的实体
		Question question=(Question) mAdapter.getItem(position-1);	
		if (question!=null) {
			UIHelper.showQuestionsRedirect(getActivity(), question);
		}
	}
	@Override
	protected ListBaseAdapter getListAdapter() {
		return null;
	}
}
