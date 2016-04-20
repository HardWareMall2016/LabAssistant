package net.oschina.app.v2.activity.user.fragment;

import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter.OnOperationListener;
import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.emoji.EmojiFragment.EmojiTextListener;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentList;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class InterestFragment extends BaseFragment implements
		EmojiTextListener, EmojiFragmentControl, OnOperationListener,
		OnItemClickListener{
	protected static final String TAG = InterestFragment.class
			.getSimpleName();
	private static final int REQUEST_CODE = 0x1;
	private static final String TWEET_DETAIL_SCREEN = "tweet_detail_screen";
	private ListView mListView;
	private EmptyLayout mEmptyView;
	private Ask ask;
	private int mCurrentPage = 0;
	private CommentAdapter mAdapter;
	public EmojiFragment mEmojiFragment;
	private BroadcastReceiver mCommentReceiver;

	private OnScrollListener mScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (mAdapter != null
					&& mAdapter.getDataSize() > 0
					&& mListView.getLastVisiblePosition() == (mListView
							.getCount() - 1)) {
				if (mState == STATE_NONE
						&& mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
					mState = STATE_LOADMORE;
					mCurrentPage++;
					
					// sendRequestCommentData();
				}
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			Comment comment = data
					.getParcelableExtra(Comment.BUNDLE_KEY_COMMENT);
			if (comment != null && ask != null) {
				// mAdapter.addItem(0, comment);
				// mTweet.setCommentCount(mTweet.getCommentCount() + 1);
				// mTvCommentCount.setText(getString(R.string.comment_count,
				// mTweet.getCommentCount()));
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		if (mCommentReceiver != null) {
			getActivity().unregisterReceiver(mCommentReceiver);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_tweet_detail,
				container, false);
		initViews(view);
		mCurrentPage = 1;
		//executeOnLoadDataSuccess();
		return view;
	}

	@SuppressLint("InflateParams")
	private void initViews(View view) {
		mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnScrollListener(mScrollListener);
		mListView.setOnItemClickListener(this);
		mAdapter = new CommentAdapter(this, true,getActivity());
		mListView.setAdapter(mAdapter);
	}

	
	private void sendRequestCommentData(int qid) {
		NewsApi.getCommentList(qid,
				mCurrentPage, mCommentHandler);
	}

	@Override
	public void setEmojiFragment(EmojiFragment fragment) {
		mEmojiFragment = fragment;
		mEmojiFragment.setEmojiTextListener(this);
	}

	@Override
	public void onSendClick(String text) {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_network_error);
			return;
		}
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLogin(getActivity());
			mEmojiFragment.hideKeyboard();
			return;
		}
		if (TextUtils.isEmpty(text)) {
			AppContext.showToastShort(R.string.tip_comment_content_empty);
			mEmojiFragment.requestFocusInput();
			return;
		}
		
		int id = ask.getId();
		int uid = AppContext.instance().getLoginUid();
		boolean isZhuiwen = false; // 是否追问
		boolean relation = false; // 是否@高手
		NewsApi.subComment(id, uid, isZhuiwen, text, relation, "", msubHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		final Comment comment = (Comment) mAdapter.getItem(position - 1);
		if (comment == null)
			return;
			handleReplyComment(comment);
	}

	@Override
	public void onMoreClick(final Comment comment) {
	}

	private void handleReplyComment(Comment comment) {
	
		UIHelper.showReplyCommentForResult(getActivity(), REQUEST_CODE, false, ask.getId(),
				CommentList.CATALOG_TWEET, comment);
	}

	private JsonHttpResponseHandler mCommentHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
				CommentList list = CommentList.parse(response);
				executeOnLoadCommentDataSuccess(list);
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}

		@Override
		 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};
	private JsonHttpResponseHandler msubHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
				 if(response.getInt("code")==88)
				  {
					AppContext.showToast("回答成功");
			      }else
			      {
				   AppContext.showToast("回答失败");
			      }
				
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
		}

		@Override
		 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		}

		public void onFinish() {
			mState = STATE_NONE;
		}
	};


	private void executeOnLoadCommentDataSuccess(CommentList list) {
		if (mState == STATE_REFRESH)
			mAdapter.clear();
		List<Comment> data = list.getCommentlist();
		mAdapter.addData(data);
		mEmptyView.setErrorType(EmptyLayout.HIDE_LAYOUT);
		if (data.size() == 0 && mState == STATE_REFRESH) {
			mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else if (data.size() < TDevice.getPageSize()) {
			if (mState == STATE_REFRESH)
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
			else
				mAdapter.setState(ListBaseAdapter.STATE_NO_MORE);
		} else {
			mAdapter.setState(ListBaseAdapter.STATE_LOAD_MORE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TWEET_DETAIL_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TWEET_DETAIL_SCREEN);
		MobclickAgent.onPause(getActivity());
	}

}
