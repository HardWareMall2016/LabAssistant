package net.oschina.app.v2.activity;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.fragment.MallViewPagerFragment;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.activity.home.model.Ad;
import net.oschina.app.v2.activity.home.model.AdList;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class MallActivity extends BaseActivity {

	private ImageView iv_mallhead;
	private TextView tv_mall_jifen;
	private ImageView iv_indentify;

	private MallViewPagerFragment mMallViewPagerFragment;

	@Override
	protected int getLayoutId() {
		return R.layout.mall;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.ji_fen_shang_cheng;
	}

	
	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;
        
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = this.inflateView(R.layout.v2_actionbar_custom_back_title_btn);
		
		
		View back = view.findViewById(R.id.btn_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		View tv_back_title = view.findViewById(R.id.tv_back_title);
		tv_back_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		
		TextView mTvActionTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
		int titleRes = getActionBarTitle();
		if (titleRes != 0) 
		{
			mTvActionTitle.setText(titleRes);
		}
		
		TextView rightMore = (TextView) view.findViewById(R.id.tv_actionbar_right_more);
		rightMore.getLayoutParams().width = DeviceUtils.dip2px(MallActivity.this, 70);
		rightMore.setText("我的物品");
		
		rightMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!AppContext.instance().isLogin())
				{
					AppContext.showToast("请先登录");
					
//					Intent intent = new Intent(MallActivity.this,MainActivity.class);
//					startActivity(intent);
					return;
				}
				
				UIHelper.wodewupin(MallActivity.this);
			}
		});
		
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		iv_mallhead = (ImageView) findViewById(R.id.iv_mallhead);
		tv_mall_jifen = (TextView) findViewById(R.id.tv_mall_jifen);
		iv_indentify = (ImageView) findViewById(R.id.iv_indentify);

		View tv_answer = findViewById(R.id.tv_answer);
		View tv_answer_two = findViewById(R.id.tv_answer_two);
		
		tv_answer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MallActivity.this,MainActivity.class);
				i.putExtra("type", "answer");
				startActivity(i);

			}
		});
	
		tv_answer_two.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				UIHelper.showShiMingRenZheng(MallActivity.this);
				
			}
		});
		
		
		if(null != AppContext.instance().getLoginInfo())
		{
			final User u = AppContext.instance().getLoginInfo();
			tv_mall_jifen.setText("我的积分："+u.getIntegral());
			iv_indentify.setImageResource(u.getRealname_status() == 1 ? R.drawable.indentity :  R.drawable.noidentify);
			iv_indentify.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (u.getRealname_status()!=1) {
						//跳转到实名认证的页面
						UIHelper.showShiMingRenZheng(MallActivity.this);
					}
				}
			});
		}
		
		iv_mallhead.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null != v.getTag() && v.getTag() instanceof Ad)
				{
					Ad ad = (Ad) v.getTag();
					Intent intent = new Intent(MallActivity.this, ShowTitleDetailActivity.class);
					intent.putExtra("id", ad.getArticleid());
					startActivity(intent);
				}
			}
		});
		
		
		mMallViewPagerFragment = new MallViewPagerFragment();

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.realtabcontent, mMallViewPagerFragment);
		ft.commit();

		sendRequestAdData();
		
	}
	
	/////////////////
	private JsonHttpResponseHandler mHomeAdHandler = new JsonHttpResponseHandler() {

		@Override
		 public void onSuccess(int statusCode, Header[] headers, JSONObject response)
		 {
			try {
				AdList list = AdList.parse(response.toString());
				if(null != list.getAdlist() && !list.getAdlist().isEmpty())
				{
					
					Ad a = list.getAdlist().get(0);
					iv_mallhead.setTag(a);
					ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(a.getimage()), iv_mallhead);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	};
	
	
	private void sendRequestAdData() {
		
		NewsApi.getHomeAdList(9, mHomeAdHandler);
	}
	
	
	

}
