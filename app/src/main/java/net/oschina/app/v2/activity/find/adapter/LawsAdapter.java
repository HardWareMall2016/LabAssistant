package net.oschina.app.v2.activity.find.adapter;

import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Laws;
import net.oschina.app.v2.utils.DateUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class LawsAdapter extends ListBaseAdapter {
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.daily_list_item, parent,false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Laws laws = (Laws) _data.get(position);
		vh.tv_time.setText(DateUtil.parseTime((long)laws.getInputtime()));
		
		vh.tv_title.setText(laws.getTitle());
		ImageLoader.getInstance().displayImage(String.format(ApiHttpClient.DEV_API_IMAGE_URL, laws.getThumb()), vh.iv_daily);
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_title, tv_time;
		public ImageView iv_daily;

		public ViewHolder(View view) {
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			iv_daily = (ImageView) view.findViewById(R.id.iv_daily);
		}
	}
}
