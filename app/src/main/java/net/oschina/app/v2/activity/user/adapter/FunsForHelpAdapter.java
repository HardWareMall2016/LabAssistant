package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.user.model.FunsForHelp;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.ui.AvatarView;
import net.oschina.app.v2.utils.DateUtil;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class FunsForHelpAdapter extends ListBaseAdapter {
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.fensiqiuzhu, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		FunsForHelp forHelp = (FunsForHelp) _data.get(position);

		vh.iv_head_default.setUserInfo(forHelp.getUid(), forHelp.getNickname());
		vh.iv_head_default.setAvatarUrl(ApiHttpClient.getImageApiUrl(forHelp.getHead()));
		
		vh.tv_name.setText(forHelp.getNickname());
		vh.tv_time_before.setText(DateUtil.getFormatTime(forHelp
				.getIntputtime()));
		// 求助的问题
		vh.tv_question_default.setText(forHelp.getContent());

		if (forHelp.getIssolveed() == 1) {
			vh.iv_isresolved.setVisibility(View.VISIBLE);
		} else {
			vh.iv_isresolved.setVisibility(View.GONE);
		}

		String superlist = forHelp.getSuperlist();
		if (!TextUtils.isEmpty(superlist) && !"null".equals(superlist)) {
			vh.tv_funs_invite.setVisibility(View.VISIBLE);
			vh.tv_funs_invite.setText("邀请了" + superlist + "进行回答");
		} else {
			vh.tv_funs_invite.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(forHelp.getLable())) {
			vh.tv_funs_mark.setVisibility(View.VISIBLE);
			vh.tv_funs_mark.setText("标签：" + forHelp.getLable());
		} else {
			vh.tv_funs_mark.setVisibility(View.GONE);
		}

		vh.tv_funs_answerno.setText(forHelp.getAnum() + "人回答");
		return convertView;
	}

	static class ViewHolder {
		private AvatarView iv_head_default;
		private TextView tv_name;
		private ImageView iv_isresolved;
		private TextView tv_time_before;
		private TextView tv_question_default;
		private TextView tv_funs_invite;
		private TextView tv_funs_mark;
		private ImageView iv_funs_answerno;
		private TextView tv_funs_answerno;

		public ViewHolder(View view) {
			// 头像
			iv_head_default = (AvatarView) view
					.findViewById(R.id.iv_head_default);
			// 提问者的名字
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			// 是否已经解决的图片提示
			iv_isresolved = (ImageView) view.findViewById(R.id.iv_isresolved);
			// 解决的时间
			tv_time_before = (TextView) view.findViewById(R.id.tv_time_before);
			// 默认粉丝求助的问题
			tv_question_default = (TextView) view
					.findViewById(R.id.tv_question_default);
			// 邀请谁来回答了问题
			tv_funs_invite = (TextView) view.findViewById(R.id.tv_funs_invite);
			// 问题的标签类型
			tv_funs_mark = (TextView) view.findViewById(R.id.tv_funs_mark);
			// 粉丝回答的数量的图标，固定不动的
			iv_funs_answerno = (ImageView) view
					.findViewById(R.id.iv_funs_answerno);
			// 粉丝回答的数量
			tv_funs_answerno = (TextView) view
					.findViewById(R.id.tv_funs_answerno);
		}
	}
}
