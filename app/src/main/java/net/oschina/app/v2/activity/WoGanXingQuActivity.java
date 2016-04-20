package net.oschina.app.v2.activity;
import net.oschina.app.v2.base.BaseActivity;

import com.shiyanzhushou.app.R;
/**
 * 隐私条款政策
 */
public class WoGanXingQuActivity extends BaseActivity {

	protected int getLayoutId() {
		return R.layout.myintersted;
	}


	@Override
	protected boolean hasBackButton() {
		return true;
	}
	
	@Override
	protected int getActionBarTitle() {
		return R.string.active_woganxingqu;
	}
}
