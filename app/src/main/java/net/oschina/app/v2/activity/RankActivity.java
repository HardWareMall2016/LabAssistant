package net.oschina.app.v2.activity;
import net.oschina.app.v2.activity.find.fragment.BillBoaodViewPagerFragment;
import net.oschina.app.v2.base.BaseActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.View;
import android.widget.TextView;

import com.shiyanzhushou.app.R;
/**
 * 排行
 */
public class RankActivity extends BaseActivity implements View.OnClickListener{

	private TextView category01,category02,category03;
	private BillBoaodViewPagerFragment mDisucussFragment;
	
	public static int categoryType = 1;
	protected int getLayoutId() {
		return R.layout.a_discuss_ly;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDisucussFragment = new BillBoaodViewPagerFragment();
		
		categoryType=1;
		category01.setSelected(true);
		category02.setSelected(false);
		category03.setSelected(false);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_container, mDisucussFragment);
		ft.commit();
	}
	
	
	
	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;
        
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = this.inflateView(R.layout.billboard_actionbar);
		view.findViewById(R.id.btn_back).setOnClickListener(this);
		view.findViewById(R.id.tx_back).setOnClickListener(this);
		
		category01 = (TextView) view.findViewById(R.id.tv_actionbar_title01);
		category02 = (TextView) view.findViewById(R.id.tv_actionbar_title02);
		category03 = (TextView) view.findViewById(R.id.tv_actionbar_title03);
		
		category01.setOnClickListener(this);
		category02.setOnClickListener(this);
		category03.setOnClickListener(this);
		
		
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		if(v.getId() == R.id.tv_actionbar_title01)
		{
			category01.setSelected(true);
			category02.setSelected(false);
			category03.setSelected(false);
			
			categoryType = 1;
			mDisucussFragment.onRankCategoryChange(categoryType);
			
		}else if(v.getId() == R.id.tv_actionbar_title02)
		{
			
			category01.setSelected(false);
			category02.setSelected(true);
			category03.setSelected(false);
			
			categoryType = 2;
			mDisucussFragment.onRankCategoryChange(categoryType);
			
		}else if(v.getId() == R.id.tv_actionbar_title03)
		{
			category01.setSelected(false);
			category02.setSelected(false);
			category03.setSelected(true);
			
			categoryType = 3;
			mDisucussFragment.onRankCategoryChange(categoryType);
			
		} else if (v.getId()==R.id.btn_back || v.getId()==R.id.tx_back) {
			finish();
		}
		
	}
	
	
	
}
