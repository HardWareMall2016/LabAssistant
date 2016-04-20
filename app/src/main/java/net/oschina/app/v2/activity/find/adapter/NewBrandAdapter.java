package net.oschina.app.v2.activity.find.adapter;

import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Brand;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class NewBrandAdapter extends ListBaseAdapter {
	
	
	@Override
	public int getDataSize() {
		
		if(_data.size() % 3 == 0)
		{
			return _data.size() / 3;
		} else
		{
			return _data.size() / 3 + 1;
		}
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		
		if(null == convertView||convertView.getTag() == null)
		{
			convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.a_new_brand_gvitem, parent,false);
		}
		
		if(convertView.getTag() instanceof ViewHolder)
		{
			vh = (ViewHolder) convertView.getTag();
		} else
		{
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}
		
		
		position = 3 * position;
		
		if(_data.size() > position)
		{
			Brand b = (Brand) _data.get(position);
			vh.layout01.setVisibility(View.VISIBLE);
			
			vh.tx01.setText(b.getTitle());
			
			vh.layout01.setTag(R.id.iv_brandgvitem01,position);
			vh.layout01.setOnClickListener(onClickListener);
			
			ImageLoader.getInstance().displayImage(String.format(ApiHttpClient.DEV_API_IMAGE_URL, b.getThumb()), vh.img01);
			
		} else
		{
			vh.layout01.setVisibility(View.INVISIBLE);
			vh.layout01.setOnClickListener(null);
		}
		
		if(_data.size() > position +1)
		{
			Brand b = (Brand) _data.get(position + 1);
			vh.layout02.setVisibility(View.VISIBLE);
			
			vh.tx02.setText(b.getTitle());
			
			ImageLoader.getInstance().displayImage(String.format(ApiHttpClient.DEV_API_IMAGE_URL, b.getThumb()), vh.img02);
			
			vh.layout02.setTag(R.id.iv_brandgvitem01,position+1);
			vh.layout02.setOnClickListener(onClickListener);
		} else
		{
			vh.layout02.setVisibility(View.INVISIBLE);
			vh.layout02.setOnClickListener(null);
		}
		
		if(_data.size() > position +2)
		{
			Brand b = (Brand) _data.get(position + 2);
			vh.layout03.setVisibility(View.VISIBLE);
			
			vh.img03.setImageResource(R.drawable.lv2x);
			vh.tx03.setText(b.getTitle());
			vh.layout03.setTag(R.id.iv_brandgvitem01,position+2);
			vh.layout03.setOnClickListener(onClickListener);
			ImageLoader.getInstance().displayImage(String.format(ApiHttpClient.DEV_API_IMAGE_URL, b.getThumb()), vh.img03);
			
		} else
		{
			vh.layout03.setVisibility(View.INVISIBLE);
			vh.layout03.setOnClickListener(null);
		}
		

		
		
		return convertView;
	}
	
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int position=(Integer) arg0.getTag(R.id.iv_brandgvitem01);
			onItemClickListener.onItemClick(arg0, position);
		}
	};

	static class ViewHolder {
		
		View layout01,layout02,layout03;
		
		
		 TextView tx01,tx02,tx03;
		 ImageView img01,img02,img03;
		 

		public ViewHolder(View view) {
			layout01 = view.findViewById(R.id.iv_brandgvitem01);
			layout02 = view.findViewById(R.id.iv_brandgvitem02);
			layout03 = view.findViewById(R.id.iv_brandgvitem03);
			
			tx01 = (TextView) view.findViewById(R.id.tv_brandgvitem_tx01);
			tx02 = (TextView) view.findViewById(R.id.tv_brandgvitem_tx02);
			tx03 = (TextView) view.findViewById(R.id.tv_brandgvitem_tx03);
			
			img01 = (ImageView) view.findViewById(R.id.iv_brandgvitem_img01);
			img02 = (ImageView) view.findViewById(R.id.iv_brandgvitem_img02);
			img03 = (ImageView) view.findViewById(R.id.iv_brandgvitem_img03);
			
		}
	}
	
	private onItemClickListener onItemClickListener;
	
	
	
	public onItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(onItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface onItemClickListener{
		void onItemClick(View view,int position);
	}
	
}
