package net.oschina.app.v2.activity.user.activity;

import net.oschina.app.v2.activity.user.fragment.AnswerItemFragment;
import net.oschina.app.v2.base.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shiyanzhushou.app.R;

public class MyAnswerDetail extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myanswer_detail);

		AnswerItemFragment f = new AnswerItemFragment();
		f.setArguments(getIntent().getExtras());
		
		FragmentManager fm= getSupportFragmentManager();
		FragmentTransaction trans=fm.beginTransaction();
		trans.add(R.id.container, f);
		trans.commit();
	}


	@Override
	protected int getActionBarTitle() {
		return R.string.actionbar_title_myanswer;
	}
	
	@Override
	protected boolean hasBackButton() {
		return true;
	}
}
