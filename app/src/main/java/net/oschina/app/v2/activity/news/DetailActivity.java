package net.oschina.app.v2.activity.news;

import java.lang.ref.WeakReference;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.activity.news.fragment.NewsDetailFragment;
import net.oschina.app.v2.activity.news.fragment.ToolbarFragment;
import net.oschina.app.v2.activity.news.fragment.ToolbarFragmentControl;
import net.oschina.app.v2.activity.tweet.ShareHelper;
import net.oschina.app.v2.activity.tweet.fragment.TweetDetailFragment;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.ui.dialog.CommonDialog;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.utils.TDevice;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

/**
 * 新闻资讯详情
 * 
 * @author william_sim
 * 
 */
public class DetailActivity extends BaseActivity implements
		ToolbarEmojiVisiableControl {

	public static final int DISPLAY_NEWS = 0;
	public static final int DISPLAY_BLOG = 1;
	public static final int DISPLAY_SOFTWARE = 2;
	public static final int DISPLAY_QUESTION = 3;
	public static final int DISPLAY_TWEET = 4;
	public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";
	// private static final String DETAIL_SCREEN = "detail_screen";

	private View mViewEmojiContaienr, mViewToolBarContaienr;
	private WeakReference<BaseFragment> mFragment, mEmojiFragment;

	private ShareHelper shareHelper;
	
	@Override
	protected int getLayoutId() {
		return R.layout.v2_activity_detail;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.actionbar_title_detail;
	}

	@Override
	protected int getActionBarCustomView() {
		return R.layout.v2_actionbar_custom_detail;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		shareHelper=new ShareHelper(this);
		
		int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE,
				DISPLAY_NEWS);
		BaseFragment fragment = null;
		int actionBarTitle = 0;
		switch (displayType) {
		case DISPLAY_NEWS:
			actionBarTitle = R.string.actionbar_title_news;
			fragment = new NewsDetailFragment();
			break;
		case DISPLAY_BLOG:
//			actionBarTitle = R.string.actionbar_title_blog;
//			fragment = new BlogDetailFragment();
			break;
		case DISPLAY_SOFTWARE:
//			actionBarTitle = R.string.actionbar_title_software;
//			fragment = new SoftwareDetailFragment();
			break;
		case DISPLAY_QUESTION:
//			actionBarTitle = R.string.actionbar_title_question;
//			fragment = new QuestionDetailFragment();
			break;
		case DISPLAY_TWEET:
			actionBarTitle = R.string.actionbar_title_tweet;
			fragment = new TweetDetailFragment();
			break;
		default:
			break;
		}
		setActionBarTitle(actionBarTitle);
		FragmentTransaction trans = getSupportFragmentManager()
				.beginTransaction();
		mFragment = new WeakReference<BaseFragment>(fragment);
		trans.replace(R.id.container, fragment);

		mViewEmojiContaienr = findViewById(R.id.emoji_container);
		mViewToolBarContaienr = findViewById(R.id.toolbar_container);
		
		if (fragment instanceof EmojiFragmentControl) {
			EmojiFragment f = new EmojiFragment();
			mEmojiFragment = new WeakReference<BaseFragment>(f);
			trans.replace(R.id.emoji_container, f);
			((EmojiFragmentControl) fragment).setEmojiFragment(f);
		}

		if (fragment instanceof ToolbarFragmentControl) {
			ToolbarFragment f = new ToolbarFragment();
			// mEmojiFragment = new WeakReference<BaseFragment>(f);
			trans.replace(R.id.toolbar_container, f);
			((ToolbarFragmentControl) fragment).setToolBarFragment(f);


			mViewEmojiContaienr.setVisibility(View.GONE);
			mViewToolBarContaienr.setVisibility(View.VISIBLE);
		}

		trans.commit();
	}
	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;
	
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			View view = inflateView( R.layout.v2_actionbar_custom_asktitle);
			View back = view.findViewById(R.id.btn_back);
			if (back == null) {
				throw new IllegalArgumentException(
						"can not find R.id.btn_back in customView");
			}
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});
			TextView mTvActionTitle = (TextView) view
					.findViewById(R.id.tv_ask_title);
			if (mTvActionTitle == null) {
				throw new IllegalArgumentException(
						"can not find R.id.tv_actionbar_title in customView");
			}
			int titleRes = getActionBarTitle();
			if (titleRes != 0) {
				mTvActionTitle.setText(titleRes);
			}
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(view, params);
		
	}
	
	@Override
	public void toggleToolbarEmoji() {
		if (mViewEmojiContaienr.getVisibility() == View.VISIBLE) {
			if (mEmojiFragment != null) {
				// mEmojiFragment.get().
			}
			TDevice.hideSoftKeyboard(getCurrentFocus());

			final Animation in = AnimationUtils.loadAnimation(this,
					R.anim.footer_menu_slide_in);
			Animation out = AnimationUtils.loadAnimation(this,
					R.anim.footer_menu_slide_out);
			mViewEmojiContaienr.clearAnimation();
			mViewToolBarContaienr.clearAnimation();
			mViewEmojiContaienr.startAnimation(out);
			out.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					//
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mViewEmojiContaienr.setVisibility(View.GONE);
					mViewToolBarContaienr.setVisibility(View.VISIBLE);
					mViewToolBarContaienr.clearAnimation();
					mViewToolBarContaienr.startAnimation(in);
				}
			});
		} else {
			final Animation in = AnimationUtils.loadAnimation(this,
					R.anim.footer_menu_slide_in);
			Animation out = AnimationUtils.loadAnimation(this,
					R.anim.footer_menu_slide_out);
			mViewToolBarContaienr.clearAnimation();
			mViewEmojiContaienr.clearAnimation();
			mViewToolBarContaienr.startAnimation(out);
			out.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mViewToolBarContaienr.setVisibility(View.GONE);
					mViewEmojiContaienr.setVisibility(View.VISIBLE);
					mViewEmojiContaienr.clearAnimation();
					mViewEmojiContaienr.startAnimation(in);
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		if (mEmojiFragment != null && mEmojiFragment.get() != null
				&& mViewEmojiContaienr.getVisibility() == View.VISIBLE) {
			if (mEmojiFragment.get().onBackPressed()) {
				return;
			}
		}
		if (mFragment != null && mFragment.get() != null) {
			if (mFragment.get().onBackPressed()) {
				return;
			}
		}
		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.main_menu_more:
			onMore();
			break;
		}
		return true;
	}
   protected void onMore()
   {
		final CommonDialog dialog = DialogHelper
				.getPinterestDialogCancelable(this);
		dialog.setTitle(R.string.title_more);
		dialog.setItemsWithoutChk(
				getResources().getStringArray(R.array.app_bar_items),
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						dialog.dismiss();
						goToSelectItem(position);
					}
				});
		dialog.show();
	   
   }
	private void goToSelectItem(int position) {
		
		switch (position) {
		case 0:
			final CommonDialog dialogShare = DialogHelper
			.getPinterestDialogCancelable(this);
			dialogShare.setTitle(R.string.title_share);
			dialogShare.setItemsWithoutChk(
			getResources().getStringArray(R.array.app_share_items),
			new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (position == 0) {
//						shareHelper.shareToSinaWeibo();
					} else if (position == 0) {
						shareHelper.shareToWeiChat("实验助手", "实验助手", "实验助手");
					} else if (position == 1) {
						shareHelper.shareToWeiChatCircle("实验助手", "实验助手", "实验助手");
					}
					
					dialogShare.dismiss();
					//goToSelectItem(position);
				}
			});
			dialogShare.show();
			break;
		case 1:
			if (!AppContext.instance().isLogin()) {
				Intent intent=new Intent(this, MainActivity.class);
				intent.putExtra("type", "login");
				startActivity(intent);
			} else {
				Ask ask=(Ask)getIntent().getSerializableExtra("ask");
				int uid=AppContext.instance().getLoginUid();
				NewsApi.reportQuestion(uid, ask.getId(), new JsonHttpResponseHandler(){
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String str="举报成功";
						
						try {
							int code =response.getInt("code");
							if(code!=88){
								str=response.getString("desc");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						AppContext.showToast(str);
					}
				});
			}
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// MobclickAgent.onPageStart(DETAIL_SCREEN);
		// MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// MobclickAgent.onPageEnd(DETAIL_SCREEN);
		// MobclickAgent.onPause(this);
	}
}
