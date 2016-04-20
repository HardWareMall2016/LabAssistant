package net.oschina.app.v2.activity.tweet.fragment;

import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter.OnOperationListener;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.base.Constants;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentItem;
import net.oschina.app.v2.model.CommentList;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.DateUtil;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class TweetDetailFragment extends BaseFragment implements OnClickListener,
		OnOperationListener, OnItemClickListener {
	protected static final String TAG = TweetDetailFragment.class
			.getSimpleName();
	private static final int REQUEST_CODE = 0x1;
	private static final String CACHE_KEY_PREFIX = "tweet_";
	private static final String CACHE_KEY_TWEET_COMMENT = "tweet_comment_";
	private static final String TWEET_DETAIL_SCREEN = "tweet_detail_screen";
	private ListView mListView;
	private EmptyLayout mEmptyView;
	private TextView mTvAsk, mTvRank, mTvTime, mTvTitle, mTvCommentCount;
	//private WebView mContent;
	private Ask ask;
	private int mCurrentPage = 0;
	private CommentAdapter mAdapter;
	//public EmojiFragment mEmojiFragment;
	private TweetDetailActivity mActivity;
	private BroadcastReceiver mCommentReceiver;

	class CommentChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int opt = intent.getIntExtra(Comment.BUNDLE_KEY_OPERATION, 0);
			int id = intent.getIntExtra(Comment.BUNDLE_KEY_ID, 0);
			int catalog = intent.getIntExtra(Comment.BUNDLE_KEY_CATALOG, 0);
			boolean isBlog = intent.getBooleanExtra(Comment.BUNDLE_KEY_BLOG,
					false);
			Comment comment = intent
					.getParcelableExtra(Comment.BUNDLE_KEY_COMMENT);
			//onCommentChanged(opt, id, catalog, isBlog, comment);
		}
	}

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
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	    mActivity = (TweetDetailActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter(
				Constants.INTENT_ACTION_COMMENT_CHANGED);
		mCommentReceiver = new CommentChangeReceiver();
		getActivity().registerReceiver(mCommentReceiver, filter);
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
		//mTweetId = getActivity().getIntent().getIntExtra("tweet_id", 0);
		ask=(Ask)getActivity().getIntent().getSerializableExtra("ask");
		mActivity.setActionBarTitle(ask.getnickname()+"的提问");
		
		initViews(view);
		executeOnLoadDataSuccess();
		
		return view;
	}

	@SuppressLint("InflateParams")
	private void initViews(View view) {
		mEmptyView = (EmptyLayout) view.findViewById(R.id.error_layout);
		mListView = (ListView) view.findViewById(R.id.listview);
		mListView.setOnScrollListener(mScrollListener);
		mListView.setOnItemClickListener(this);
		//ListView的头
//		View header = LayoutInflater.from(getActivity()).inflate(
//				R.layout.v2_list_header_tweet_detail, null);
//		mTvAsk = (TextView) header.findViewById(R.id.tv_byask);
//		mTvRank = (TextView) header.findViewById(R.id.tv_byrank);
//		mTvTime = (TextView) header.findViewById(R.id.tv_time);
//		mTvTitle = (TextView) header.findViewById(R.id.tv_title);
//		mTvCommentCount = (TextView) header.findViewById(R.id.tv_comment_count);
//		//mContent = (WebView) header.findViewById(R.id.webview);
//		//initWebView(mContent);
//		mListView.addHeaderView(header);
		
		mAdapter = new CommentAdapter(this, true,getActivity());
		mAdapter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CommentItem item=(CommentItem)v.getTag();
				Comment comment=new Comment();
				comment.setqid(item.getQid());
				comment.setnickname(item.getNickname());
				comment.setId(StringUtils.toInt(item.getAid()));
				comment.setauid(StringUtils.toInt(item.getAuid()));
				comment.setZhuiWen(true);
				handleReplyComment(comment);
			}
		});
		mAdapter.setOnRefreshListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapter.clear();
				sendRequestCommentData(ask.getId());
			}
		});
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.button) {
			int tag = Integer.valueOf(v.getTag().toString());
			if (tag == 1) {  //登录
				UIHelper.showLoginView(getActivity());
			}
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initWebView(WebView webView) {
		WebSettings settings = webView.getSettings();
		settings.setDefaultFontSize(20);
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		int sysVersion = Build.VERSION.SDK_INT;
		if (sysVersion >= 11) {
			settings.setDisplayZoomControls(false);
		} else {
			ZoomButtonsController zbc = new ZoomButtonsController(webView);
			zbc.getZoomControls().setVisibility(View.GONE);
		}
		UIHelper.addWebImageShow(getActivity(), webView);
	}
	private void executeOnLoadDataSuccess() {
		if (ask != null && ask.getId() > 0) {
//			fillUI();
		  mState = STATE_REFRESH;
			mCurrentPage = 1;
			//mEmptyView.setErrorType(EmptyLayout.NETWORK_LOADING);
			//requestTweetCommentData(true);
			sendRequestCommentData(ask.getId());
		} else {
			throw new RuntimeException("load detail error");
		}
	}
	private void fillUI() {
		mTvTime.setText(DateUtil.getFormatTime(ask.getinputtime()));
		mTvCommentCount.setText(getString(R.string.comment_count, ask.getanum()));
		
		String supper=ask.getsuperlist();
		if (!TextUtils.isEmpty(supper) && !"null".equals(supper)) {
			mTvAsk.setText(getString(R.string.by_ask, supper));
			mTvAsk.setVisibility(View.VISIBLE);
		} else {
			mTvAsk.setVisibility(View.GONE);
		}
		String label = ask.getLabel();
		if (TextUtils.isEmpty(label)) {
			label = "暂无";
		}
		mTvRank.setText(getString(R.string.rank_detail, label));
		mTvTitle.setText(ask.getContent());
		
		//mContent.loadDataWithBaseURL(null, ask.getContent(), "text/html", "utf-8", null);
		//mContent.setWebViewClient(UIHelper.getWebViewClient());
	}


	private void sendRequestCommentData(int qid) {
		NewsApi.getCommentList(qid, mCurrentPage, mCommentHandler);
	}

//	@Override
//	public void setEmojiFragment(EmojiFragment fragment) {
//		mEmojiFragment = fragment;
//		mEmojiFragment.setEmojiTextListener(this);
//	}

	public void onSendClick(String text) {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_network_error);
			return;
		}
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLogin(getActivity());
			//mEmojiFragment.hideKeyboard();
			return;
		}
		if (TextUtils.isEmpty(text)) {
			AppContext.showToastShort(R.string.tip_comment_content_empty);
			//mEmojiFragment.requestFocusInput();
			return;
		}
		
		// 追问
		if (mAdapter!=null && mAdapter.getZhuiwenModel()!=null) {
			addZhuiwen(text, mAdapter.getZhuiwenModel());
			mAdapter.setZhuiwenModel(null);
		} else {
			// 回答
			addQuestion(text);
		}
	}

	
	private void addQuestion(String text) {
		int id = ask.getId();
		int uid = AppContext.instance().getLoginUid();
		boolean isZhuiwen = false; // 是否追问
		boolean relation = false; // 是否@高手
		NewsApi.subComment(id, uid, isZhuiwen,text, relation, mActivity.superlist, msubHandler);
	}
		
	private void addZhuiwen(String text, Comment comment) {
		int uid = AppContext.instance().getLoginUid();
		if (uid == comment.getauid()) {
			AppContext.showToast("不能追问自己");
			return;
		}
		NewsApi.addCommentAfter(comment.getqid(), 0,uid, comment.getId(), 0,text, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				if (response.optInt("code") == 88) {
					AppContext.showToast("回答成功");
					mActivity.reset();
					mAdapter.clear();
					sendRequestCommentData(ask.getId());
				} else {
					AppContext.showToast(response.optString("desc"));
				}
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		final Comment comment = (Comment) mAdapter.getItem(position - 1);
		if (comment == null)
			return;
		
		comment.setZhuiWen(false);
		handleReplyComment(comment);
	}

	@Override
	public void onMoreClick(final Comment comment) {
	}

	private void handleTiWen(List<Comment> list) {
		if (!AppContext.instance().isLogin()) {
			return;
		}
		
		boolean isFlag=false;
		int uid=AppContext.instance().getLoginUid();
		for (Comment comment : list) {
			//如果回答问题的人的id,是登陆的id
			if (comment.getauid()==uid) {
				isFlag=true;
				break;
			}
		}
		if (isFlag) {
			//mActivity.showTiWen();
			//mActivity.showReAnswer();
		}else{
			//mActivity.showTiWenMe();
			mActivity.showAnswer();
		}
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
				list.sortList();
				executeOnLoadCommentDataSuccess(list);
				handleTiWen(list.getCommentlist());
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
					 mActivity.reset();
					 mAdapter.clear();
					 sendRequestCommentData(ask.getId());
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
