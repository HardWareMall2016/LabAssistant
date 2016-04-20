package net.oschina.app.v2.activity.user.adapter;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.fragment.MyAnswerFragment;
import net.oschina.app.v2.activity.user.fragment.MyQuestionFragment;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class UserCenterPagerAdapter extends FragmentStatePagerAdapter {

	private Context context;
	private List<String> mFragments;
	private int mUid;

	public UserCenterPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
		this.mFragments = new ArrayList<String>();
	}

	public UserCenterPagerAdapter(Context context, FragmentManager fm, int uid) {
		super(fm);
		this.context = context;
		this.mFragments = new ArrayList<String>();
		this.mUid = uid;
	}

	public void addTabs(Class<?> clazz) {
		mFragments.add(clazz.getClass().getName());
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			MyQuestionFragment q=new MyQuestionFragment(mUid);
			q.setPersonalInfo(true);
			return q;
		}
		MyAnswerFragment answerFragment=new MyAnswerFragment(mUid);
		answerFragment.setCurrentUser(mUid==AppContext.instance().getLoginUid());
		answerFragment.setPersonalPage(true);
		return answerFragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(mUid==AppContext.instance().getLoginUid()){
			if (position == 0) {
				return "我的提问";
			}
			return "我的回答";
		}else{
			if (position == 0) {
				return "TA的提问";
			}
			return "TA的回答";
		}
		
	}
}
