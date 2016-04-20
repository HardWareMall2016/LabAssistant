package net.oschina.app.v2.activity.find.adapter;

import net.oschina.app.v2.base.ListBaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class WeekAdapter extends ListBaseAdapter {
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.a_week_listitem, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		// Daily daily = (Daily) _data.get(position);
		vh.iv_week_cover.setImageResource(R.drawable.daily_item);

		vh.tv_week_title.setText("秋季如何保水");
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_week_title;
		public ImageView iv_week_cover;

		public ViewHolder(View view) {
			tv_week_title = (TextView) view.findViewById(R.id.tv_week_title);

			iv_week_cover = (ImageView) view.findViewById(R.id.iv_week_cover);
		}
	}
}
