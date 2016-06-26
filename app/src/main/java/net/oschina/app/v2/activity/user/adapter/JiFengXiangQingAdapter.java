package net.oschina.app.v2.activity.user.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.user.model.XiTongXiaoXi;
import net.oschina.app.v2.base.ListBaseAdapter;

public class JiFengXiangQingAdapter extends ListBaseAdapter {
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.jifenxiangqing_item, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		XiTongXiaoXi xiTongXiaoXi = (XiTongXiaoXi) _data.get(position);
		return convertView;
	}

	static class ViewHolder {
		private TextView tv_summary;
		private TextView tv_time;
		private TextView tv_score;
		private TextView tv_cur_jifen;

		public ViewHolder(View view) {
			tv_summary = (TextView) view.findViewById(R.id.tv_summary);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_score = (TextView) view.findViewById(R.id.tv_score);
			tv_cur_jifen = (TextView) view.findViewById(R.id.tv_cur_jifen);
		}
	}
}
