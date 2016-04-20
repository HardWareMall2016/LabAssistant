package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import de.greenrobot.event.EventBus;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.model.Question;
import net.oschina.app.v2.activity.model.QuestionList;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.activity.user.adapter.MyQuestionAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.AdoptSuccEvent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
/**
 * @author acer
 */
public class MyQuestionFragment extends BaseListFragment{
	//我的问题的Fragment的tag
	protected static final String TAG = MyQuestionFragment.class.getSimpleName();
	//缓存的前缀
	private static final String CACHE_KEY_PREFIX_11 = "questionlist_1";
	private int mUid;
	
	public MyQuestionFragment(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle=getArguments();
		if(bundle!=null){
			isPersonalInfo=bundle.getBoolean("isPersonalInfo");
		}
		EventBus.getDefault().register(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	
	//是否个人中心
	private boolean isPersonalInfo;
	
	
	
	public boolean isPersonalInfo() {
		return isPersonalInfo;
	}
	public void setPersonalInfo(boolean isPersonalInfo) {
		this.isPersonalInfo = isPersonalInfo;
	}
	public MyQuestionFragment(int uid){
		mUid = uid;
	}

	

@Override
public void onDestroy(){
	super.onDestroy();
	EventBus.getDefault().unregister(this);
}

/**
 * 刷新
 * @param totalMessage
 */
public void onEventMainThread(AdoptSuccEvent adoptSuccEvent){
	mCurrentPage = 1;
	mState = STATE_REFRESH;
	requestData(true);
}

	//适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new MyQuestionAdapter();
	}
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
		int uid = 0;
		if(mUid!=0){
			uid = mUid; 
		}else{
			uid = AppContext.instance().getLoginUid();
		}
		if(isPersonalInfo){
			NewsApi.getPersonalQustionList(uid,mCurrentPage, mJsonHandler);
		}else{
			NewsApi.getQustionList(uid,mCurrentPage, mJsonHandler);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	   /*Question question=(Question) mAdapter.getItem(position-1);*/
		//拿到条目的实体
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
}
