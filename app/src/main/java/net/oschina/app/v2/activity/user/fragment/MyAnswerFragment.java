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
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class MyAnswerFragment extends BaseListFragment {
	//我的问题的Fragment的tag
	protected static final String TAG = MyAnswerFragment.class.getSimpleName();
	//缓存的前缀
	private static final String CACHE_KEY_PREFIX_2 = "answerlist_";
	
	private int mUid;
	
	private boolean isCurrentUser=true;


	private int mStatus =-1;//0未采纳  1采纳

	//Tab fans views
	private TextView mViewAllQuestion;
	private TextView mViewAnsweredQuestion;
	private TextView mViewUnansweredQuestion;


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

	private View.OnClickListener mFansTabClickListener=new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			mViewAllQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewAllQuestion.setBackgroundResource(R.drawable.default_bg);
			mViewAnsweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewAnsweredQuestion.setBackgroundResource(R.drawable.default_bg);
			mViewUnansweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewUnansweredQuestion.setBackgroundResource(R.drawable.default_bg);

			switch (v.getId()){
				case R.id.all_question:
					mViewAllQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewAllQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					mStatus=-1;
					break;
				case R.id.answered_question:
					mViewAnsweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewAnsweredQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					mStatus=1;
					break;
				case R.id.unanswered_question:
					mViewUnansweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewUnansweredQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					mStatus=0;
					break;
			}
			setRefresh();
		}
	};

	//适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new MyAnswerAdapter(getActivity(),isCurrentUser,getArguments().getInt("uid")==0);
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

		View answerTab=view.findViewById(R.id.answer_tab);
		if(isPersonalPage){
			answerTab.setVisibility(View.GONE);
		}else{
			answerTab.setVisibility(View.VISIBLE);
		}

		mViewAllQuestion=(TextView)view.findViewById(R.id.all_question);
		mViewAnsweredQuestion=(TextView)view.findViewById(R.id.answered_question);
		mViewUnansweredQuestion=(TextView)view.findViewById(R.id.unanswered_question);
		mViewAllQuestion.setOnClickListener(mFansTabClickListener);
		mViewAnsweredQuestion.setOnClickListener(mFansTabClickListener);
		mViewUnansweredQuestion.setOnClickListener(mFansTabClickListener);
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.my_answer_fragment_pull_refresh_listview;
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
			NewsApi.getAnswerList(mStatus,uid,mCurrentPage,mJsonHandler) ;
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
