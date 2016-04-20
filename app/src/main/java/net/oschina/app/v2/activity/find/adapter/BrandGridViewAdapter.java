package net.oschina.app.v2.activity.find.adapter;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class BrandGridViewAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;

	public BrandGridViewAdapter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 30;
	}

	public BrandGridViewAdapter(Context c) {
		super();
		this.context = c;
		
		inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		holder = new ViewHolder();
		if (position % 3 == 0) {
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.a_brand_gvitem, parent,false);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.iv_brandgvitem);
				holder.tv = (TextView) convertView
						.findViewById(R.id.tv_brandgvitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv.setImageResource(R.drawable.cctv22x);
			holder.tv.setText("cctv");
			return convertView;
		}

		if (position % 3 == 1) {
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.a_brand_gvitem, parent,false);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.iv_brandgvitem);
				holder.tv = (TextView) convertView
						.findViewById(R.id.tv_brandgvitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv.setImageResource(R.drawable.lv2x);
			holder.tv.setText("lv");
			return convertView;
		}
		if (position % 3 == 2) {
			if (convertView == null) {

				convertView = this.inflater.inflate(R.layout.a_brand_gvitem,
						parent,false);
				holder.iv = (ImageView) convertView
						.findViewById(R.id.iv_brandgvitem);
				holder.tv = (TextView) convertView
						.findViewById(R.id.tv_brandgvitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.iv.setImageResource(R.drawable.bm2x);
			holder.tv.setText("宝马");
			return convertView;
		}
		return null;
	}

	private class ViewHolder {

		ImageView iv;

		TextView tv;

	}
}
