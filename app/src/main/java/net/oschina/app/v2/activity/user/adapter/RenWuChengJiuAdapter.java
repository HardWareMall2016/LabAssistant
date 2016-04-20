package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.base.ListBaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class RenWuChengJiuAdapter extends ListBaseAdapter{
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		//定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.renwuchengjiu, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		//AskAgain askAgain=(AskAgain) _data.get(position);
	/*	vh.tv_content.setText(askAgain.getContent());
		vh.tv_answer_timebefore.setText(askAgain.getIntputtime()+"");
		vh.tv_answer_origin.setText(askAgain.getQuid()+"");*/
		return convertView;
	}
	static class ViewHolder {
		private ImageView iv_defaulthead;
		private TextView tv_name_level;
		private TextView tv_content;
		private TextView tv_answer_timebefore;
		private TextView tv_answer_origin;
		public ViewHolder(View view) {
		//默认的头像
		iv_defaulthead = (ImageView) view.findViewById(R.id.iv_defaulthead);
		//名字和等级
		tv_name_level = (TextView) view.findViewById(R.id.tv_name_level);
		//推荐的答案
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		//问题回答的时间
		tv_answer_timebefore = (TextView) view.findViewById(R.id.tv_answer_timebefore);
		//原来的问题
//		tv_answer_origin = (TextView) view.findViewById(R.id.tv_answer_origin);
		}
	}
}
