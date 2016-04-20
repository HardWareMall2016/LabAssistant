package net.oschina.app.v2.activity.find.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Week;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class WeeklyAdapter extends ListBaseAdapter {

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView != null && convertView.getTag() != null) {
			viewHolder = (ViewHolder) convertView.getTag();
		} else {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.weekitem_header, parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}

		Week week = (Week) _data.get(position);
		if (week.isHeader()) {
			viewHolder.week_time.setVisibility(View.VISIBLE);
			viewHolder.head_layout.setVisibility(View.VISIBLE);
			viewHolder.sub_layout.setVisibility(View.GONE);

			viewHolder.week_time.setText(parseTime(week.getInputtime()));
			viewHolder.tv_week_title.setText(week.getTitle());
			String url = String.format(ApiHttpClient.DEV_API_IMAGE_URL,
					week.getThumb());
			ImageLoader.getInstance().displayImage(url.toString(),
					viewHolder.iv_week);
		} else {
			viewHolder.week_time.setVisibility(View.GONE);
			viewHolder.head_layout.setVisibility(View.GONE);
			viewHolder.sub_layout.setVisibility(View.VISIBLE);

			viewHolder.tv_week_title1.setText(week.getTitle());
			String url = String.format(ApiHttpClient.DEV_API_IMAGE_URL,
					week.getThumb());
			ImageLoader.getInstance().displayImage(url.toString(),
					viewHolder.iv_week_cover1);
		}

		return convertView;
	}

	static class ViewHolder {
		public FrameLayout head_layout;
		public RelativeLayout sub_layout;
		public TextView week_time, tv_week_title, tv_week_title1;
		public ImageView iv_week, iv_week_cover1;

		public ViewHolder(View view) {
			head_layout = (FrameLayout) view.findViewById(R.id.head_layout);
			iv_week = (ImageView) view.findViewById(R.id.iv_week);
			week_time = (TextView) view.findViewById(R.id.week_time);
			sub_layout = (RelativeLayout) view.findViewById(R.id.sub_layout);
			iv_week_cover1 = (ImageView) view.findViewById(R.id.iv_week_cover1);
			tv_week_title1 = (TextView) view.findViewById(R.id.tv_week_title1);
			tv_week_title = (TextView) view.findViewById(R.id.tv_week_title);
		}
	}

	private String parseTime(String timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日  HH:mm",
				Locale.CHINA);
		try {
			long time = Integer.parseInt(timeStamp);
			return sdf.format(new Date(time * 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
