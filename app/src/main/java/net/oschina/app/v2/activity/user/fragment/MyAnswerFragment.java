package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.activity.user.adapter.MyAnswerAdapter;
import net.oschina.app.v2.activity.user.model.Answer;
import net.oschina.app.v2.activity.user.model.AnswerList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.ListEntity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class MyAnswerFragment extends BaseListFragment {
	//我的问题的Fragment的tag
	protected static final String TAG = MyAnswerFragment.class.getSimpleName();
	//缓存的前缀
	private static final String CACHE_KEY_PREFIX_2 = "answerlist_";
	
	private int mUid;
	
	private boolean isCurrentUser=true;
	
	/**
	 * 是否个人页，否调getAnswerList接口，是调getPersonalAnswerList接口
	 */
	private boolean isPersonalPage;

	public void setPersonalPage(boolean isPersonalPage) {
		this.isPersonalPage = isPersonalPage;
	}

	public void setCurrentUser(boolean isCurrent){
		isCurrentUser=isCurrent;
	}
	
	public MyAnswerFragment(){
		
	}


	//适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new MyAnswerAdapter(getActivity(),isCurrentUser);
	}
	//缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_2;
	}
    
	@Override
	protected void initViews(View view) {
		super.initViews(view);
		mListView.setOnItemClickListener(null);
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
		mUid=getArguments().getInt("uid");
		int uid = 0;
		if(mUid!=0){
			uid = mUid; 
		}else{
			uid = AppContext.instance().getLoginUid();
		}
		if(isPersonalPage){
			NewsApi.getPersonalAnswerList(uid,mCurrentPage,mJsonHandler) ;
		}else{
			NewsApi.getAnswerList(uid,mCurrentPage,mJsonHandler) ;
		}
		
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Answer answer=(Answer) mAdapter.getItem(position-1);
		//UIHelper.redirectToAnswerDetail(getActivity(),answer);
		if (answer!=null) {
			Ask ask = new Ask();
			ask.setId(answer.getQid());
			ask.setanum(answer.getAnum());
			ask.setnickname(answer.getNickname());
			ask.setLabel(answer.getLabel());
			ask.setContent(answer.getTitle());
			ask.setinputtime(answer.getInputtime());
			ask.setsuperlist(answer.getSuperlist());
			
			Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("ask", ask);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		}
	}
}
