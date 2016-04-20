package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.ui.AvatarView;
import net.oschina.app.v2.utils.DateUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class AnswerItemAdapter extends ListBaseAdapter {
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.singleansweritem, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		Comment answer = (Comment) _data.get(position);
		vh.tv_default_answer_origin.setText(answer.getTitle());
		vh.tv_default_name_level.setText(answer.getnickname() + " LV"
				+ answer.getRank());
		vh.tv_default_content.setText(answer.getcontent());
		vh.tv_answer_default_timebefore.setText(DateUtil.getFormatTime(answer
				.getinputtime()));
		vh.iv_defaulthead.setUserInfo(answer.getauid(), answer.getnickname());
		vh.iv_defaulthead.setAvatarUrl(ApiHttpClient.getImageApiUrl(answer.gethead()));
		
		if (answer.gettype() > 1) {
			vh.iv_sign.setVisibility(View.VISIBLE);
		} else {
			vh.iv_sign.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder {
		private AvatarView iv_defaulthead;
		private TextView tv_default_name_level;
		private TextView tv_default_content;
		private TextView tv_answer_default_timebefore;
		private TextView tv_default_answer_origin;
		private TextView tv_default_surportnumber;
		private TextView tv_default_ask_again;
		private ImageView iv_sign;

		public ViewHolder(View view) {
			// 默认的头像
			iv_defaulthead = (AvatarView) view.findViewById(R.id.iv_defaulthead);
			// 默认的名称和等级
			tv_default_name_level = (TextView) view
					.findViewById(R.id.tv_default_name_level);
			// 推荐的答案
			tv_default_content = (TextView) view
					.findViewById(R.id.tv_default_content);
			// 回答时间
			tv_answer_default_timebefore = (TextView) view
					.findViewById(R.id.tv_answer_default_timebefore);
			// 原始的问题
			tv_default_answer_origin = (TextView) view
					.findViewById(R.id.tv_default_answer_origin);
			// 支持的人数
			tv_default_surportnumber = (TextView) view
					.findViewById(R.id.tv_default_surportnumber);
			// 追问的人数
			tv_default_ask_again = (TextView) view
					.findViewById(R.id.tv_default_ask_again);
			
			iv_sign=(ImageView) view.findViewById(R.id.iv_sign);
		}
	}
}
