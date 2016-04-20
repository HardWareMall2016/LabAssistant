package net.oschina.app.v2.activity.user.activity;

import net.oschina.app.v2.activity.user.fragment.QuestionItemFragment;
import net.oschina.app.v2.base.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shiyanzhushou.app.R;

public class MyQuestionDetail extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// answer之中保存了数据
		setContentView(R.layout.myquestion_detail);
		
		QuestionItemFragment f = new QuestionItemFragment();
		f.setArguments(getIntent().getExtras());
		
		FragmentManager fm= getSupportFragmentManager();
		FragmentTransaction trans=fm.beginTransaction();
		trans.add(R.id.container, f);
		trans.commit();
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	protected int getActionBarTitle() {
		return R.string.questiondetail;
	}
}