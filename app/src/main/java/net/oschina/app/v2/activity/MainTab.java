package net.oschina.app.v2.activity;

import net.oschina.app.v2.activity.active.fragment.ActiveFragment;
import net.oschina.app.v2.activity.find.fragment.FindFragment;
import net.oschina.app.v2.activity.tweet.fragment.TweetPublicFragment;
import net.oschina.app.v2.activity.tweet.fragment.TweetViewPagerFragment;
import net.oschina.app.v2.activity.user.fragment.LoginFragment;

import com.shiyanzhushou.app.R;

public enum MainTab {
	/*
	 * HOME(0, R.string.tab_name_article, R.drawable.tab_icon_home,
	 * HomeFragment.class),
	 */
	QUESTION(0, R.string.tab_name_question,
			R.drawable.tab_icon_ask, TweetViewPagerFragment.class), CREATEQUESTION(
			1, R.string.tab_name_createquestion, R.drawable.tab_icon_question,
			TweetPublicFragment.class), TWEET(2, R.string.tab_name_tweet,
			R.drawable.tab_icon_find, FindFragment.class), ME(3,
			R.string.tab_name_me, R.drawable.tab_icon_me, ActiveFragment.class), LOGIN(
			4, R.string.tab_name_me, R.drawable.tab_icon_me,
			LoginFragment.class);
	// AppContext.instance().isLogin()?ActiveFragment.class:LoginFragment.class
	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
