package net.oschina.app.v2.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shiyanzhushou.app.R;





/**
 * 公用帮助页面，包括viewpager +底部导航图
 * 
 * @author 
 * @version 1.3.3
 * @since 1.3.3
 * @createDate 2014-6-9
 */
public class GuideMainActivity extends Activity {

	/**
	 * viewpager
	 */
	protected ViewPager mViewPager;

	/**
	 * 适配器
	 */
	protected CommonHelpPagerAdapter mAdapter;

	/**
	 * 内容组件
	 */
	protected List<View> mContentPages;

	/**
	 * 底部导航图标
	 */
	protected List<View> mImageViews;

	/**
	 * 底部导航内容主体部分
	 */
	protected ViewGroup mBottomContentView;

	/**
	 * 资源加载器
	 */
	protected LayoutInflater mInflater;

	
	private View mLastSelectedImage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_guide_main);
		IsGuid.clearGuid(this);
		initBaseView();
	}

	// ***private
	// method******************************************************************************************

	/**
	 * 初始化内容控件
	 */
	private void initBaseView() {
		mInflater = LayoutInflater.from(this);
		// 初始化viewpager
		mViewPager = (ViewPager) findViewById(R.id.help_detail_viewpager);
		mViewPager.setOffscreenPageLimit(3);
		mContentPages = new ArrayList<View>();
		mImageViews = new ArrayList<View>();
		mBottomContentView = (ViewGroup) findViewById(R.id.help_detail_bottom_content);
		
		
		
		//初始化需要加载的页
		initPages();
		mAdapter=new CommonHelpPagerAdapter(mContentPages);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(listener);
		//初始化底部导航部分
		initBottomImages();
	}
	

	public void enter(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	protected void initPages() {
		View view = mInflater.inflate(R.layout.a_guide_main_pagetwo,
				mViewPager, false);
		
		mContentPages.add(view);

		// 第二页
		view = mInflater.inflate(R.layout.a_guide_main_pagethree,
				mViewPager, false);
		mContentPages.add(view);

		// 第三页
		view = mInflater.inflate(R.layout.a_guide_main_pagefour,
				mViewPager, false);
		Button button=(Button)view.findViewById(R.id.submit);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				enter(arg0);
			}
		});
		mContentPages.add(view);

	
	}

	/**
	 * 初始化底部导航，根据page的数目自动对应
	 */
	private void initBottomImages() {
		ImageView imageView;
		LayoutInflater inflater=LayoutInflater.from(this);
		// 加载底部dot
		for (int i = 0; i < mContentPages.size(); i++) {
			
			imageView = (ImageView)inflater.inflate(R.layout.a_guide_main_image, mBottomContentView,false);
			
			if (i == 0) {
				imageView.setBackgroundResource(R.drawable.btn_guide_orange);
				mLastSelectedImage=imageView;
			} else {
				imageView.setBackgroundResource(R.drawable.btn_guide_grey);
			}
			mBottomContentView.addView(imageView);
			mImageViews.add(imageView);
		}
		mLastSelectedImage=mImageViews.get(0);
	}

	// ***protected
	// method******************************************************************************************

	


	// ***public
	// method******************************************************************************************
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		recyleBitmap();
	}
	
	protected void recyleBitmap() {
		// 回收图片资源
		try {
			for(View tempView:mContentPages){
				((ViewGroup)tempView).removeAllViews();
			}
			mContentPages.clear();
			mViewPager.removeAllViews();
			
			System.gc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private OnPageChangeListener listener=new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int paramInt) {
			if(mLastSelectedImage!=null){
				mLastSelectedImage.setBackgroundResource(R.drawable.btn_guide_grey);
			}
			
			mLastSelectedImage=mImageViews.get(paramInt);
			mLastSelectedImage.setBackgroundResource(R.drawable.btn_guide_orange);
			if(paramInt==2){
				mBottomContentView.setVisibility(View.INVISIBLE);
			}else{
				mBottomContentView.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int paramInt) {
			// TODO Auto-generated method stub
			
		}
	};
}
