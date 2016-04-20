package net.oschina.app.v2.activity;
import net.oschina.app.v2.activity.user.fragment.DisucussFragment;
import net.oschina.app.v2.base.BaseActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.shiyanzhushou.app.R;
/**
 * 隐私条款政策
 */
public class DiscussActivity extends BaseActivity {

	protected int getLayoutId() {
		return R.layout.a_discuss_ly;
	}


	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	protected int getActionBarTitle() {
		return R.string.discuss;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Intent intent = getIntent();
		int articleId = intent.getIntExtra("articleId", -1);
		
		DisucussFragment mDisucussFragment = new DisucussFragment();
		
		mDisucussFragment.setArticleId(articleId);
		
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_container, mDisucussFragment);
		ft.commit();
		
	}
	
	
}
