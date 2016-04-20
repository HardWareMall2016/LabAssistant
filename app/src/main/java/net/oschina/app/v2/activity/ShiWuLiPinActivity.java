package net.oschina.app.v2.activity;
import net.oschina.app.v2.activity.user.fragment.ShiWuLiPinFragment;
import net.oschina.app.v2.base.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.shiyanzhushou.app.R;
/**
 * 我的礼品
 */
public class ShiWuLiPinActivity extends BaseActivity {

	private ShiWuLiPinFragment mDisucussFragment;
	
	protected int getLayoutId() {
		return R.layout.a_discuss_ly;
	}

	@Override
	protected int getActionBarTitle() {
		
		return R.string.activity_wodewupin;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDisucussFragment = new ShiWuLiPinFragment();
		
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_container, mDisucussFragment);
		ft.commit();
	}
}
