package net.oschina.app.v2.activity.common;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.activity.comment.adapter.CommentAdapter.OnOperationListener;
import net.oschina.app.v2.activity.favorite.adapter.ArticleAdapter;
import net.oschina.app.v2.activity.favorite.adapter.SearchUserAdapter;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.activity.tweet.adapter.TweetAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.emoji.EmojiFragment.EmojiTextListener;
import net.oschina.app.v2.model.ArticleList;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.FavoriteList;
import net.oschina.app.v2.model.event.FavoriteRefreshOther;
import net.oschina.app.v2.ui.dialog.WaitDialog;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

public class SearchBackActivity extends BaseActivity implements
		EmojiTextListener, EmojiFragmentControl, OnOperationListener,
		OnItemClickListener{

	public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
	public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
	private static final String TAG = "FLAG_TAG";
	private EditText et_content;
	private WeakReference<Fragment> mFragment;
	private ListView mListView;
	private int mCurrentPage = 0;
	private TweetAdapter mQuestionAdapter;
	private SearchUserAdapter mUserAdapter;
	private ArticleAdapter mArticleAdapter;
	private View tip_layout;
	private View tip_close;

	private class SearchType{
		static final  int USERS=1;
		static final  int ARTICLES=2;
		static final  int QUESTIONS=3;
	}

	private TextView mTabQuestions;
	private TextView mTabUsers;
	private TextView mTabArticles;

	private int mSearchType=SearchType.QUESTIONS;

	private WaitDialog mDialog;

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	protected void sendRequestData(boolean isAutoSearch) {
		String searchContent = et_content.getText().toString();
		if (TextUtils.isEmpty(searchContent)) {
			if (!isAutoSearch) {
				AppContext.showToast("请输入查询内容", Toast.LENGTH_SHORT);
			}
		} else {
			mDialog=showWaitDialog("正在搜索中...");
			mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
			NewsApi.getSearchList(et_content.getText().toString(),AppContext.instance().getLoginUid(),mSearchType, mCurrentPage, new SearchHandler(mSearchType));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_detail_layout);
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

		mTabQuestions = (TextView) findViewById(R.id.questions);
		mTabQuestions.setOnClickListener(this);
		mTabUsers = (TextView) findViewById(R.id.users);
		mTabUsers.setOnClickListener(this);
		mTabArticles = (TextView) findViewById(R.id.articles);
		mTabArticles.setOnClickListener(this);

		refreshTabViews();

		mListView = (ListView) findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		mQuestionAdapter = new TweetAdapter();
		mListView.setAdapter(mQuestionAdapter);
		
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
			case R.id.questions:
				mSearchType=SearchType.QUESTIONS;
				refreshTabViews();
				if (!TextUtils.isEmpty(et_content.getText().toString())) {
					sendRequestData(false);
				}
				break;
			case R.id.users:
				mSearchType=SearchType.USERS;
				refreshTabViews();
				if (!TextUtils.isEmpty(et_content.getText().toString())) {
					sendRequestData(false);
				}
				break;
			case R.id.articles:
				mSearchType=SearchType.ARTICLES;
				refreshTabViews();
				if (!TextUtils.isEmpty(et_content.getText().toString())) {
					sendRequestData(false);
				}
				break;
		}
	}

	private void refreshTabViews(){
		mTabQuestions.setBackgroundResource(R.drawable.bg_search_left_unselected);
		mTabQuestions.setTextColor(0xff1daaf1);

		mTabUsers.setBackgroundResource(R.drawable.bg_search_center_unselected);
		mTabUsers.setTextColor(0xff1daaf1);

		mTabArticles.setBackgroundResource(R.drawable.bg_search_right_unselected);
		mTabArticles.setTextColor(0xff1daaf1);

		switch (mSearchType){
			case SearchType.QUESTIONS:
				mTabQuestions.setTextColor(Color.WHITE);
				mTabQuestions.setBackgroundResource(R.drawable.bg_search_left_selected);
				break;
			case SearchType.USERS:
				mTabUsers.setTextColor(Color.WHITE);
				mTabUsers.setBackgroundResource(R.drawable.bg_search_center_selected);
				break;
			case SearchType.ARTICLES:
				mTabArticles.setTextColor(Color.WHITE);
				mTabArticles.setBackgroundResource(R.drawable.bg_search_right_selected);
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

	private class SearchHandler extends JsonHttpResponseHandler{
		private int searchType;

		public SearchHandler(int searchType){
			this.searchType=searchType;
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			// 请求失败则解析errorResponse，返回错误信息给用户
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
				mDialog=null;
			}
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.dismiss();
				mDialog=null;
			}
			if(mSearchType!=searchType){
				return;
			}
			try {
				if (response.getInt("code") == 88) {
					try {
						switch (mSearchType){
							case SearchType.QUESTIONS:
								AskList askList = AskList.parse(response.toString());
								mQuestionAdapter = new TweetAdapter();
								String searchContent = et_content.getText().toString();
								mQuestionAdapter.setHighLight(searchContent);
								mListView.setAdapter(mQuestionAdapter);
								List<Ask> data = askList.getAsklist();
								mQuestionAdapter.addData(data);
								break;
							case SearchType.USERS:
								FavoriteList favoriteList = FavoriteList.parse(response.toString());
								mUserAdapter=new SearchUserAdapter();
								mUserAdapter.setHighLight(et_content.getText().toString());
								mUserAdapter.setOnClickListener(mOnAttachClickListener);
								mListView.setAdapter(mUserAdapter);
								mUserAdapter.addData(favoriteList.getFavoritelist());
								break;
							case SearchType.ARTICLES:
								mArticleAdapter = new ArticleAdapter();
								mArticleAdapter.setHighLight(et_content.getText().toString());
								mListView.setAdapter(mArticleAdapter);
								ArticleList articleList = ArticleList.parse(response.toString());
								mArticleAdapter.addData(articleList.getArticles());
								break;
						}
					} catch (IOException e) {

					} catch (AppException e) {

					}
					InputMethodManager imm = (InputMethodManager) SearchBackActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
				} else {
					hideWaitDialog();
					AppContext.showToast("查询失败");
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(mListView.getAdapter() instanceof TweetAdapter){
			Ask ask = (Ask) mQuestionAdapter.getItem(position);
			if (ask != null)
				UIHelper.showTweetDetail(view.getContext(), ask);
		}else if(mListView.getAdapter() instanceof ArticleAdapter){
			ArticleList.Article article = (ArticleList.Article ) mArticleAdapter.getItem(position);
			if(article!=null){
				Intent intent = new Intent(this, ShowTitleDetailActivity.class);
				intent.putExtra("id", Integer.valueOf(article.getId()));
				if("培训信息".equals(article.getCname())){
					intent.putExtra("fromTitle",  R.string.find_peixunxinxi);
					intent.putExtra("fromSource", 1);
				}
				startActivity(intent);
			}
		}
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

	private OnClickListener mOnAttachClickListener=new OnClickListener(){
		@Override
		public void onClick(View v) {
			FavoriteList.Favorite f = (FavoriteList.Favorite)v.getTag();
			if(f.getSame()==1){
				//doCancelAttention(f);
			}else{
				attention(f);
			}
		}
	};


	private void attention(final FavoriteList.Favorite f){
		int uid=AppContext.instance().getLoginUid();
		NewsApi.addAttention(uid, f.getId(), new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				AppContext.showToast(response.optString("desc", ""));
				sendRequestData(false);
			}
		});
	}


	private void doCancelAttention(final FavoriteList.Favorite f) {
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
		titleTv.setText("您确定要取消关注该用户吗？");
		// 设置监听
		Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
		zhichi.setText("确定");
		// 支持
		zhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int uid=AppContext.instance().getLoginUid();
				NewsApi.unAttention(uid, f.getTuid(), new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						AppContext.showToast(response.optString("desc", ""));
						sendRequestData(false);
					}
				});
				dialog.dismiss();
			}
		});
		// 查看支持者
		Button chakanzhichi = (Button) window.findViewById(R.id.ib_chakanzhichi);
		chakanzhichi.setText("取消");
		chakanzhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
