package net.oschina.app.v2.activity.find.fragment;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.MallActivity;
import net.oschina.app.v2.activity.common.SimpleBackActivity;
import net.oschina.app.v2.activity.favorite.fragment.SaleActivity;
import net.oschina.app.v2.activity.find.adapter.ActivityCenterViewPagerAdapter;
import net.oschina.app.v2.activity.find.view.CustomViewPager;
import net.oschina.app.v2.activity.user.model.GiftPojo;
import net.oschina.app.v2.model.User;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class GiftDetailInfoDialog {
	
	private static GiftDetailInfoDialog giftDetailInfoDialog;

	private ImageView close_img,iv_sign;
	private TextView name, money, condition, detail,img_count_tx;
	private Button exchange;
	
	private CustomViewPager mViewPager;
	private ActivityCenterViewPagerAdapter viewPagerAdapter;
	private List<View> containerViews = new ArrayList<View>();
	private  Dialog dialog = null;
	
	private GiftPojo gift;
	private Context context;
	
	private GiftDetailInfoDialog(){
		
	}
	
	public static GiftDetailInfoDialog getInstance(){
		if(null==giftDetailInfoDialog){
			giftDetailInfoDialog = new GiftDetailInfoDialog();
		}
		return giftDetailInfoDialog;
	}
	
	public void createReviceUserDialog(Context c, GiftPojo g) {
		gift = g;
		context = c;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		
		dialog = builder.create();
		dialog.show();

		
			
		View view = View.inflate(context, R.layout.giftdetail_info_ly, null);
		dialog.setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

		View container = view.findViewById(R.id.shang_ping_rong_qi);
		mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager_activitycenter);
		close_img = (ImageView) dialog.findViewById(R.id.close_img);
		img_count_tx = (TextView) dialog.findViewById(R.id.img_count_tx);
		iv_sign = (ImageView) dialog.findViewById(R.id.iv_sign);
		
		name = (TextView) dialog.findViewById(R.id.shang_pin_ming_cheng);
		money = (TextView) dialog.findViewById(R.id.shang_pin_jia_ge);
		condition = (TextView) dialog.findViewById(R.id.shang_pin_dui_huan_tiao_jian);
		detail = (TextView) dialog.findViewById(R.id.shang_pin_xiang_qing);
		exchange = (Button) dialog.findViewById(R.id.dui_huan_btn);

		viewPagerAdapter = new ActivityCenterViewPagerAdapter(containerViews);
		mViewPager.setAdapter(viewPagerAdapter);
		
		
		containerViews.clear();
		

		if(gift.getImgs().isEmpty())
		{
			gift.getImgs().add(gift.getThumb());
		}
		
		for(String str : gift.getImgs())
		{
			View view1 = LayoutInflater.from(context).inflate(R.layout.a_gift_mulit_img, null);
			ImageView iamgeView = (ImageView) view1.findViewById(R.id.iv_activitycenter_vpitem);
			ImageLoader.getInstance().displayImage(str,iamgeView);

			containerViews.add(view1);
		}
		
		viewPagerAdapter.notifyDataSetChanged();
		
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				img_count_tx.setText((arg0 + 1) + " / " + gift.getImgs().size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		img_count_tx.setText("1 / " + gift.getImgs().size());
		condition.setText("兑换条件：Lv"+gift.getRank() +" "+(gift.getType() == 1 ? "需要认证" : "不需要认证"));

		detail.setText(Html.fromHtml("奖品介绍: <br/>"+gift.getContent()));
		name.setText(gift.getTitle());
		money.setText(gift.getIntergal() + "");
		

		if(context.getClass().equals(MallActivity.class)){
			if(gift.getNum() == 0)
			{
				exchange.setEnabled(false);
				exchange.setText("兑完");
			} else{
				User user = AppContext.instance().getLoginInfo();
				if(gift.getRank()>user.getRank()){
					exchange.setEnabled(false);
					exchange.setText("等级不够");
				}else if (gift.getIntergal()>user.getIntegral()){
					exchange.setEnabled(false);
					exchange.setText("积分不足");
				}
				else if (gift.getType()==1 && user.getRealname_status() != 1 ){
					exchange.setEnabled(false);
					exchange.setText("需认证");
				}else{
					exchange.setEnabled(true);
					exchange.setText("兑换");
				}
			}
		}else{
			exchange.setEnabled(false);
			exchange.setText("已兑换");
		}
		
		close_img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();				
			}
		});
		
		
		if(gift.getIsHot() == 1)
		{
			iv_sign.setVisibility(View.VISIBLE);
			iv_sign.setBackgroundResource(R.drawable.hot_sign);
//			container.setBackgroundResource(R.drawable.mall_module_remen);
		}else if(gift.getRechargestatus() == 1)
		{
			iv_sign.setVisibility(View.VISIBLE);
			iv_sign.setBackgroundResource(R.drawable.exchanged_sign);
//			container.setBackgroundResource(R.drawable.mall_module_duiwan);
		} else
		{
			iv_sign.setVisibility(View.GONE);
//			container.setBackgroundResource(R.drawable.module_mall_ji_fen);
		}
		
		exchange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(context,SaleActivity.class);
				i.putExtra("pid", gift.getId());
				context.startActivity(i);
				
				dialog.dismiss();		
			}
		});
		
	}
	
	public boolean isOpen(){
		if(dialog==null){
			return false;
		}
		return dialog.isShowing();
	}

}
