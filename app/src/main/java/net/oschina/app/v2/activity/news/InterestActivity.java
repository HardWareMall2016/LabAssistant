package net.oschina.app.v2.activity.news;

import java.lang.ref.WeakReference;

import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.activity.news.fragment.ToolbarFragment;
import net.oschina.app.v2.activity.news.fragment.ToolbarFragmentControl;
import net.oschina.app.v2.activity.tweet.fragment.TweetDetailFragment;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.utils.TDevice;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

/**
 * 新闻资讯详情
 * 
 * @author william_sim
 * 
 */
public class InterestActivity extends BaseActivity implements
		ToolbarEmojiVisiableControl {

	public static final int DISPLAY_NEWS = 0;
	public static final int DISPLAY_TWEET = 4;
	public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";
	// private static final String DETAIL_SCREEN = "detail_screen";

	private View mViewEmojiContaienr, mViewToolBarContaienr;
	private WeakReference<BaseFragment> mFragment, mEmojiFragment;

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
		int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE,
				DISPLAY_NEWS);
		BaseFragment fragment = null;
		int actionBarTitle = 0;
		switch (displayType) {
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
