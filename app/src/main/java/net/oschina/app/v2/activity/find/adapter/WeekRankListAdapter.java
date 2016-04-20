package net.oschina.app.v2.activity.find.adapter;

import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.WeekRank;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.UIHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class WeekRankListAdapter extends ListBaseAdapter {

	private DisplayImageOptions options;
	
	
	
	public WeekRankListAdapter(){
		options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.ic_default_avatar);
	}
	
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		
		
		if (position == 0) {
			//if (convertView == null || convertView.getTag() == null) {
				convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.ranktop, parent,false);

				
				View layout01 = convertView.findViewById(R.id.ll_weekrank1);
				View layout02 = convertView.findViewById(R.id.ll_weekrank2);
				View layout03 = convertView.findViewById(R.id.ll_weekrank3);
				
				
				TextView tv1 = (TextView) convertView.findViewById(R.id.tv_ranktop01);
				TextView tv2 = (TextView) convertView.findViewById(R.id.tv_ranktop02);
				TextView tv3 = (TextView) convertView.findViewById(R.id.tv_ranktop03);
				
				TextView name01 = (TextView) convertView.findViewById(R.id.tv_name01);
				TextView name02 = (TextView) convertView.findViewById(R.id.tv_name02);
				TextView name03 = (TextView) convertView.findViewById(R.id.tv_name03);
				
				
				ImageView img01 = (ImageView) convertView.findViewById(R.id.tv_img01);
				ImageView img02 = (ImageView) convertView.findViewById(R.id.tv_img02);
				ImageView img03 = (ImageView) convertView.findViewById(R.id.tv_img03);
				
				img01.setOnClickListener(listener);
				img02.setOnClickListener(listener);
				img03.setOnClickListener(listener);
				
				
				if(_data.size() >= 1)
				{
					WeekRank w = (WeekRank) _data.get(0);
					layout01.setVisibility(View.VISIBLE);
					tv1.setText(w.getNum()+ "");
					name01.setText(w.getUserName());
					
					img01.setTag(w);
					
					ImageLoader.getInstance().displayImage(w.getUserIcon(), img01,options);
					
				} else
				{
					layout01.setVisibility(View.GONE);
				}
				
				if(_data.size() >= 2)
				{
					WeekRank w = (WeekRank) _data.get(1);
					layout02.setVisibility(View.VISIBLE);
					tv2.setText(w.getNum()+ "");
					name02.setText(w.getUserName());
					
					img02.setTag(w);
					
					ImageLoader.getInstance().displayImage(w.getUserIcon(), img02,options);
					
				} else
				{
					layout02.setVisibility(View.GONE);
				}
				
				if(_data.size() >= 3)
				{
					WeekRank w = (WeekRank) _data.get(2);
					layout03.setVisibility(View.VISIBLE);
					tv3.setText(w.getNum()+ "");
					name03.setText(w.getUserName());
					
					img03.setTag(w);
					
					ImageLoader.getInstance().displayImage(w.getUserIcon(), img03,options);
					
				} else
				{
					layout03.setVisibility(View.GONE);
				}
				
			//}
			
			return convertView;
		}
		
		if(position>7){
			return null;
		}
		
		if (position > 0) {

			final ViewHolder vh;
			if (convertView == null || convertView.getTag() == null) 
			{
				convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.a_weekranklist_item, parent,false);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
				
				vh.userIcon.setOnClickListener(listener);
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						vh.userIcon.performClick();
					}
				});
				
			} else 
			{
				vh = (ViewHolder) convertView.getTag();
			}

			WeekRank weekrank = (WeekRank) _data.get(position + 2);
			
			vh.a_weeklist_order.setText((position + 3) + "");
			vh.weekrank_beicainashu.setText(weekrank.getNum() + "");
			String company=weekrank.getCompanyName();
			if(TextUtils.isEmpty(company)||"null".equals(company)){
				company="";
			}
			vh.danwei_mingceng.setText(company);
			vh.weekrank_level.setText("Lv "+weekrank.getRank()+"");
			vh.user_name.setText(weekrank.getUserName());
			
			String type="";
			switch(weekrank.getExplain()){
			case 1:
				type="被采纳数";
				break;
			case 2:
				type="回答数";
				break;
			case 3:
				type="邀请数";
				break;
			}
			vh.tmp_cai.setText(type);
			
			IvSignUtils.displayIvSignByType(weekrank.getType(), vh.iv_sign, vh.avstarBg);
			ImageLoader.getInstance().displayImage(weekrank.getUserIcon(), vh.userIcon,options);
			
			vh.userIcon.setTag(weekrank);
			
			
			return convertView;
		}
		
		return null;
	}

	static class ViewHolder {
		
		public TextView a_weeklist_order, weekrank_beicainashu,user_name,danwei_mingceng,weekrank_level,tmp_cai;
		public ImageView userIcon,avstarBg,iv_sign;
		
		public ViewHolder(View view) 
		{
			a_weeklist_order 	= (TextView) view.findViewById(R.id.a_weeklist_order);
			weekrank_beicainashu = (TextView) view.findViewById(R.id.weekrank_beicainashu);
			user_name = (TextView) view.findViewById(R.id.weekrank_name);
			danwei_mingceng = (TextView) view.findViewById(R.id.danwei_ming);
			weekrank_level = (TextView) view.findViewById(R.id.weekrank_level);
			userIcon = (ImageView) view.findViewById(R.id.user_icon);
			tmp_cai= (TextView) view.findViewById(R.id.tmp_cai);
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
			iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
		}
	}

	@Override
	public int getDataSize() {
		
		if(_data != null)
		{
			if(_data.isEmpty())
			{
				return 0;
			}
			if(_data.size() <=3 )
			{
				return 1;
			} else 
			{
				return _data.size() - 2;
			}
		}
		
		return 0;
	}

	
	private MyListener listener = new MyListener();
	
	private class MyListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {

			if(v.getTag() instanceof WeekRank)
			{
				WeekRank w = (WeekRank) v.getTag();
				
				UIHelper.showUserCenter(v.getContext(), w.getId(), w.getUserName());
			}
		}
		
	}
	
	
	
}
