package net.oschina.app.v2.activity.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.favorite.fragment.FavoriteFragment;
import net.oschina.app.v2.activity.model.Question;
import net.oschina.app.v2.activity.model.QuestionList;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.activity.user.adapter.MyQuestionAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.ListEntity;
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

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new MyQuestionAdapter(false);
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
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

	@Override
	protected void sendRequestData() {
		int uid=AppContext.instance().getLoginUid();
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		NewsApi.getCollectQuestionList(uid,mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Question question=(Question) mAdapter.getItem(position-1);
		if (question!=null) {
			//UIHelper.showQuestionsRedirect(getActivity(), question);
			Ask ask = new Ask();
			ask.setId(question.getId());
			ask.setUid(question.getUid());
			ask.setanum(question.getAnum());
			ask.setnickname(question.getNickname());
			ask.setLabel(question.getLable());
			ask.setContent(question.getContent());
			ask.setinputtime(question.getIntputtime());
			ask.setsuperlist(question.getSuperlist());
			ask.setissolveed(question.getIssolved());

			Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ask", ask);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
