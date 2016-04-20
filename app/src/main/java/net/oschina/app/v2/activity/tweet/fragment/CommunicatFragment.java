package net.oschina.app.v2.activity.tweet.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.comment.model.CommentReply;
import net.oschina.app.v2.activity.comment.model.CommentReplyList;
import net.oschina.app.v2.activity.tweet.CommunicatActivity;
import net.oschina.app.v2.activity.tweet.adapter.CommunicatAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class CommunicatFragment extends BaseListFragment {
	private static final String CACHE_KEY_PREFIX = "communicatList_";

	private CommunicatActivity mActivity;

	public ListBaseAdapter adapter;
	private int type;
	private Ask ask;
	private Comment comment;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (CommunicatActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		type = getActivity().getIntent().getIntExtra("type", 1);
		comment = (Comment) getActivity().getIntent().getSerializableExtra(
				"comment");
		ask = (Ask) getActivity().getIntent().getSerializableExtra("ask");
		NewsApi.getAskById(ask.getId(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.getString("desc").equals("success")) {

						try {
							ask = Ask.parse( new JSONObject(response
									.getString("data")));
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initViews(View view) {
		super.initViews(view);
		mListView.setBackgroundColor(getResources().getColor(
				R.color.transparent));
		mListView.getRefreshableView().setBackgroundColor(
				getResources().getColor(R.color.transparent));
		// mListView.getRefreshableView().setBackgroundDrawable(getResources().getDrawable(R.drawable.communicat_bg_shape));
	}
	
	public void selectlast(){
		int getItemCount=adapter.getCount()-1;
		if(getItemCount>=0){
			mListView.getRefreshableView().setSelection(getItemCount);
		}
	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		adapter = new CommunicatAdapter(getActivity(), type);
		return adapter;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);
		JSONObject json = new JSONObject(result);
		CommentReplyList list = null;
		if (type == 1) {
			list = CommentReplyList.parseReply(json.toString());
		} else {
			list = CommentReplyList.parseAnswerAfter(json.toString());
		}
		// 回复：将问题排到楼顶
		CommentReply commentReply = new CommentReply();
		commentReply.setAuid(ask.getUid());
		commentReply.setHead(ask.gethead());
		commentReply.setNickname(ask.getnickname());
		commentReply.setContent(ask.getContent());
		commentReply.setIntputtime(ask.getinputtime());
		commentReply.setImage(ask.getImage());
		list.getCommentlist().add(0, commentReply);

		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		ListEntity listEntity = (CommentReplyList) seri;
		return listEntity;
	}

	@Override
	public void sendRequestData() {
		if (type == 1) {
			NewsApi.getReplyCommunicatList(comment.getauid(), 0,
					comment.getqid(), comment.getId(),mJsonHandler);
		} else if (type == 2) {
			NewsApi.getCommentReplyList(comment.getId(), 0, mJsonHandler);
		}
	}

	@Override
	protected void executeOnLoadDataSuccess(List<?> data) {
		if (mState == STATE_REFRESH) {
			mAdapter.clear();
		}

		mAdapter.addData(data);
		mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		if (data.size() == 0 && mState == STATE_REFRESH) {
			if (mAdapter.getCount() == 0) {
				if (isShowEmpty()) {
					mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
				}
			} else {
				AppContext.showToast("没有获取到新数据");
			}
		} else if (data.size() < TDevice.getPageSize()) {
			if (mState == STATE_REFRESH)
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
			else
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else {
			mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		}
		mListView.getRefreshableView().setSelection(data.size() - 1);
		handleBottomView();
	}

	private void handleBottomView() {
		// 如果没有登录的话
		if (!AppContext.instance().isLogin()) {
			mActivity.showLoginView();
		} else {

			if (type == 1) {
				int uid = AppContext.instance().getLoginUid();
				if (uid == ask.getUid() || uid == comment.getauid()) {
					mActivity.showReplyView();
				}
			} else {
				mActivity.showTraceAskView();
			}
		}
	}

}
