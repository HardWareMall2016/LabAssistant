package net.oschina.app.v2.activity.comment.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.comment.adapter.CommentReplyAdapter;
import net.oschina.app.v2.activity.comment.model.CommentModel;
import net.oschina.app.v2.activity.common.SimpleBackActivity;

import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.emoji.EmojiFragment.EmojiTextListener;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class CommentReplyFragment extends BaseFragment implements
		EmojiTextListener {

	public static final String BUNDLE_KEY_ID = "bundle_key_id";
	public static final String BUNDLE_KEY_IS_BLOG = "bundle_key_is_blog";
	public static final String BUNDLE_KEY_CATALOG = "bundle_key_catalog";
	public static final String BUNDLE_KEY_COMMENT = "bundle_key_comment";
	private static final String COMMENT_REPLY_SCREEN = "comment_reply_screen";

	private int page = 1;
	private int aid = 0;

	private BaseActivity act;
	private Comment comment;
	private EmojiFragment mEmojiFragment;
	private CommentReplyAdapter mAdapter;
	private FragmentTransaction trans;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		act = ((BaseActivity) activity);
		trans = act.getSupportFragmentManager().beginTransaction();
		mEmojiFragment = new EmojiFragment();
		mEmojiFragment.setEmojiTextListener(this);
		trans.replace(R.id.emoji_container, mEmojiFragment);
		trans.commit();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
		getActivity().getWindow().setSoftInputMode(mode);

		mAdapter = new CommentReplyAdapter(getActivity());

		Bundle bundle = getArguments();
		if (bundle != null) {
			comment = (Comment) bundle
					.getSerializable(SimpleBackActivity.BUNDLE_KEY_ARGS);
			aid = comment.getId();
			
			String title="";
			if (comment.getauid()==AppContext.instance().getLoginUid()) {
				title = "我的回答";
			} else {
				title = comment.getnickname()+"的回答";
				trans.hide(mEmojiFragment);
			}
			act.setActionBarTitle(title);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_reply_comment,
				container, false);
		initView(view);
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.comment_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()==R.id.btnok) {
			int uid=AppContext.instance().getLoginUid();
			NewsApi.userAttention(uid, aid, new JsonHttpResponseHandler(){
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						JSONObject response) {
					if (response.optInt("code") == 88) {
						AppContext.showToast("采纳成功");
					} else {
						AppContext.showToast("采纳失败");
					}
				}
			});
		}
		return true;
	}

	private void initView(View view) {
		ListView listView = (ListView) view.findViewById(R.id.comment_list);
		listView.setAdapter(mAdapter);

		sendRequest();
	}

	private void sendRequest() {
		NewsApi.getChatRecord(aid, page, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						CommentModel model = CommentModel.parse(response);
						mAdapter.setCommentModel(model);
						mAdapter.notifyDataSetChanged();
					} 
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	public void onSendClick(String text) {
		if (!TDevice.hasInternet()) {
			AppContext.showToastShort(R.string.tip_network_error);
			return;
		}
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLogin(getActivity());
			return;
		}
		if (TextUtils.isEmpty(text.trim())) {
			AppContext.showToastShort(R.string.tip_comment_content_empty);
			return;
		}

		if (!comment.isZhuiWen()) {
			addQuestion(text);
		} else {
			addZhuiwen(text);
		}
	}

	
	private void addQuestion(String text) {
		int id = comment.getqid();
		int uid = AppContext.instance().getLoginUid();
		boolean isZhuiwen = false; // 是否追问
		boolean relation = false; // 是否@高手
		NewsApi.subComment(id, uid, isZhuiwen, text, relation, "",
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (response.optInt("code") == 88) {
								AppContext.showToast("回答成功");
								
								JSONObject json=new JSONObject(response.optString("data"));
								aid = json.optInt("id");
								mEmojiFragment.reset();
								sendRequest();
							} else {
								AppContext.showToast(response.optString("desc"));
							}
						} catch (JSONException e) {}
					}
				});
	}
	
	private void addZhuiwen(String text) {
		int uid = AppContext.instance().getLoginUid();
	    
		NewsApi.addCommentAfter(comment.getqid(), 0,uid, comment.getId(),mEmojiFragment.getId(), 
				text, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						AppContext.showToast("回答成功");
						
						JSONObject json=new JSONObject(response.optString("data"));
						aid = json.optInt("id");
						mEmojiFragment.reset();
						sendRequest();
					} else {
						AppContext.showToast(response.optString("desc"));
					}
				} catch (JSONException e) {}
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(COMMENT_REPLY_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(COMMENT_REPLY_SCREEN);
		MobclickAgent.onPause(getActivity());
	}

	@Override
	public void onDestroy() {
		if (mEmojiFragment != null) {
			mEmojiFragment.hideKeyboard();
		}
		super.onDestroy();
	}

}
