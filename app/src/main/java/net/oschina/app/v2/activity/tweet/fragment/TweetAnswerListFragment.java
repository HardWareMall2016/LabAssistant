package net.oschina.app.v2.activity.tweet.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter.OnOperationListener;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.activity.tweet.adapter.TweetAnswerAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * 问题答复列表
 * 
 * @author JohnnyMeng
 * 
 */
public class TweetAnswerListFragment extends BaseListFragment implements
		OnOperationListener {

	private static final String CACHE_KEY_PREFIX = "commentlist_";

	private TweetDetailActivity mActivity;
	public ListBaseAdapter adapter;
	private Ask ask;
	private List<Comment> data;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    mActivity = (TweetDetailActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ask = (Ask) getActivity().getIntent().getSerializableExtra("ask");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		adapter  = new TweetAnswerAdapter(getActivity(),ask);
		return adapter;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return ask.getId()+CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		JSONObject json = new JSONObject(result);
		CommentList list = CommentList.parse(json);
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		ListEntity listEntity = (CommentList) seri;
		return listEntity;
	}
	
	
	
	@Override
	protected void executeOnLoadDataSuccess(List<?> data) {
		if (mState == STATE_REFRESH)
		{
			mAdapter.clear();
		}
		
		mAdapter.addData(data);
		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);

		SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();

		if (data.size() == 0 && mState == STATE_REFRESH) 
		{

			if(mAdapter.getCount() == 0)
			{
				if(ask.getUid() == AppContext.instance().getLoginUid()){

					editor.putString("name", "0");
					editor.commit();
				}

				if (isShowEmpty()) {
					mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK,"还没有人回答~\n快点帮帮TA吧 ");
				}
			} else
			{
				AppContext.showToast("没有获取到新数据");
			}
		} else if (data.size() < TDevice.getPageSize()) 
		{
			if(ask.getUid() == AppContext.instance().getLoginUid()){
				editor.putString("name", "1");
				editor.commit();
			}
			if (mState == STATE_REFRESH)
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
			else
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else {
			mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
		}
		this.data = (List<Comment>) data;
		handleBottomView();
	}

	@Override
	public void sendRequestData() {
		mCurrentPage = mCurrentPage == 0 ? 1 : mCurrentPage;
		NewsApi.getCommentList(ask.getId(), mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final Comment comment = (Comment) mAdapter.getItem(position - 1);
		if (comment == null)
			return;
		
		comment.setZhuiWen(false);
		UIHelper.showReplyCommunicat(getActivity(), ask,comment);
	}

	@Override
	public void onMoreClick(Comment comment) {
		
	}
	
	//底部操作栏控制
	public void handleBottomView() {
		if (!AppContext.instance().isLogin()) {
			return;
		}
		
		Comment loginComment = null;
		boolean isFlag=false;
		int uid=AppContext.instance().getLoginUid();
		for (Comment comment : data) {
			//如果回答问题的人的id,是登陆的id
			if (comment.getauid()==uid) {
				isFlag=true;
				loginComment = comment;
				break;
			}
		}
		if (isFlag) {
			mActivity.showReAnswer(loginComment);
		}else{
			mActivity.showAnswer();
		}
	}
	
}
