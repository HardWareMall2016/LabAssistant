package net.oschina.app.v2.activity.common;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter.OnOperationListener;
import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.activity.tweet.adapter.TweetAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.emoji.EmojiFragment.EmojiTextListener;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class SearchBackActivity extends BaseActivity implements
		EmojiTextListener, EmojiFragmentControl, OnOperationListener,
		OnItemClickListener {

	public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
	public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
	private static final String TAG = "FLAG_TAG";
	private EditText et_content;
	private WeakReference<Fragment> mFragment;
	private ListView mListView;
	private int mCurrentPage = 0;
	private TweetAdapter mAdapter;
	private View tip_layout;
	private View tip_close;
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
				// if (mState == STATE_NONE
				// && mAdapter.getState() == ListBaseAdapter.STATE_LOAD_MORE) {
				// mState = STATE_LOADMORE;
				// mCurrentPage++;
				//
				// // sendRequestCommentData();
				// }
			}
		}
	};

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	protected void sendRequestData(boolean isAutoSearch) {

		String searchContent = et_content.getText().toString();
		mAdapter.setHighLight(searchContent);
		if (TextUtils.isEmpty(searchContent)) {
			if (!isAutoSearch) {
				AppContext.showToast("请输入查询内容", Toast.LENGTH_SHORT);
			}
		} else {

			mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;

			NewsApi.getSearchList(et_content.getText().toString(),
					mCurrentPage, handler);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.v2_fragment_tweet_detail);

		Bundle bundle = getIntent().getExtras();

		initViews();
		mCurrentPage = 1;

		if (null != bundle) {
			String searchContent = bundle.getString("KeyWord");
			if (!TextUtils.isEmpty(searchContent)) {
				et_content.setText(searchContent);
				sendRequestData(true);
			}
		}

	}

	private void initViews() {

		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnScrollListener(mScrollListener);
		mListView.setOnItemClickListener(this);
		mAdapter = new TweetAdapter();
		mListView.setAdapter(mAdapter);
		
		tip_layout=findViewById(R.id.tip_layout);
		tip_close=findViewById(R.id.tip_close);
		tip_close.setOnClickListener(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_search:
			sendRequestData(false);
			break;
		case R.id.tip_close:
			tip_layout.setVisibility(View.GONE);
			break;
		}

	}

	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = null;
		view = inflateView(R.layout.v2_actionbar_search_home);
		ImageButton searchBtn = (ImageButton) view
				.findViewById(R.id.btn_search);
		searchBtn.setOnClickListener(this);

		View tx_back = view.findViewById(R.id.tx_back);
		tx_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		view.findViewById(R.id.btn_back).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		et_content = (EditText) view.findViewById(R.id.et_content);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}

	JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			// 请求失败则解析errorResponse，返回错误信息给用户
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			try {
				if (response.getInt("code") == 88) {

					AskList list;
					try {
						list = AskList.parse(response.toString());
						executeOnLoadCommentDataSuccess(list);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AppException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					InputMethodManager imm = (InputMethodManager) SearchBackActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et_content.getWindowToken(),
							0);

				} else {
					// AppContext.instance().cleanLoginInfo();
					hideWaitDialog();
					AppContext.showToast("查询失败");
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void executeOnLoadCommentDataSuccess(AskList list) {
		mAdapter.clear();
		List<Ask> data = list.getAsklist();
		mAdapter.addData(data);

	}

	@Override
	public void onBackPressed() {
		if (mFragment != null && mFragment.get() != null
				&& mFragment.get() instanceof BaseFragment) {
			BaseFragment bf = (BaseFragment) mFragment.get();
			if (!bf.onBackPressed()) {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Ask ask = (Ask) mAdapter.getItem(position);
		if (ask != null)
			UIHelper.showTweetDetail(view.getContext(), ask);
	}

	@Override
	public void onMoreClick(Comment comment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEmojiFragment(EmojiFragment fragment) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSendClick(String text) {
		// TODO Auto-generated method stub

	}
}
