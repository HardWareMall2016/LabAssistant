package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.model.Question;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.LabelUtils;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class MyQuestionAdapter extends ListBaseAdapter {
	
	private String newReplyStr;
	
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件
		
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.myquestion_item, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if(newReplyStr==null){
			newReplyStr=convertView.getContext().getResources().getString(R.string.newReply);
		}
		Question question = (Question) _data.get(position);
		vh.name.setText(question.getNickname());
		vh.rank.setText("Lv"+question.getRank());
		// 设置具体的问题
		vh.tv_question.setText(question.getContent());
		// 是否已经解决
		if (question.getIssolved() == 1) {
			vh.iv_isResolved.getLayoutParams().width = (int)TDevice.dpToPixel((float)36);
			vh.iv_isResolved.setVisibility(View.VISIBLE);
		} else {
			vh.iv_isResolved.getLayoutParams().width = 1;
			vh.iv_isResolved.setVisibility(View.INVISIBLE);
		}

		
		if (question.getReward() == 0) {
			vh.reward_layout.setVisibility(View.GONE);
		} else {
			vh.reward_layout.setVisibility(View.VISIBLE);
			vh.reward.setText(String.valueOf(question.getReward())+"分");
		}

		if (!StringUtils.isEmpty(question.getImage())
				&& !"null".equals(question.getImage())) {
			vh.picIcon.setVisibility(View.VISIBLE);
		} else {
			vh.picIcon.setVisibility(View.GONE);
		}

		String superlist = question.getSuperlist();
		if (!TextUtils.isEmpty(superlist) && !"null".equals(superlist)) {
			vh.tv_invite_who.setVisibility(View.VISIBLE);
			vh.tv_invite_who.setText("邀请了" + superlist + "进行回答");
		} else {
			vh.tv_invite_who.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(question.getLable())) {
			/*vh.tv_mark.setVisibility(View.VISIBLE);
			vh.tv_mark.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
					+ question.getLable() + "</font>"));*/
			vh.fromContent.setText(question.getLable());
			int labelBackgroundId= LabelUtils.getBgResIdByLabel(question.getLable());
			vh.fromContent.setBackgroundResource(labelBackgroundId);
			vh.tv_mark.setVisibility(View.VISIBLE);
			vh.fromContent.setVisibility(View.VISIBLE);
		} else {
			vh.tv_mark.setVisibility(View.INVISIBLE);
			vh.fromContent.setVisibility(View.INVISIBLE);
		}

		vh.readNum.setText(question.getHits());

		vh.tv_times.setText(question.getIntputtime());
		final int count=question.getNewreply();
		if(count==0){
			vh.tv_newreply.setText("");
		}else{
			vh.tv_newreply.setText(String.format(newReplyStr, count));
		}
		
		vh.tv_newreplyTime.setText(question.getNewtime());

		vh.tv_answernumber.setText(question.getAnum() + "人回答");
		return convertView;
	}

	static class ViewHolder {
		public TextView name, rank, reward;
		public RelativeLayout reward_layout;
		private TextView tv_question;
		private TextView iv_isResolved;
		private TextView tv_times;
		private TextView tv_invite_who;
		private TextView tv_mark;
		private TextView fromContent;
		private TextView readNum;
		private TextView tv_answernumber;
		private TextView tv_newreply;
		private TextView tv_newreplyTime;
		private ImageView picIcon;

		public ViewHolder(View view) {
			// 问题内容
			tv_question = (TextView) view.findViewById(R.id.tv_question);
			// 是否解决
			iv_isResolved = (TextView) view.findViewById(R.id.iv_isResolved);
			// 时间
			tv_times = (TextView) view.findViewById(R.id.tv_times);
			// 邀请了谁
			tv_invite_who = (TextView) view.findViewById(R.id.tv_invite_who);
			// 标签，展示的是问题的分类
			tv_mark = (TextView) view.findViewById(R.id.tv_mark);
			// 回答问题的数量
			tv_answernumber = (TextView) view
					.findViewById(R.id.tv_answernumber);
			reward_layout = (RelativeLayout) view.findViewById(R.id.reward_layout);
			// 用户名
			name = (TextView) view.findViewById(R.id.tv_name);
			rank = (TextView) view.findViewById(R.id.tv_rank);
			reward = (TextView) view.findViewById(R.id.tv_reward);
			tv_newreply = (TextView) view.findViewById(R.id.tv_newreply);
			tv_newreplyTime = (TextView) view.findViewById(R.id.tv_newreplyTime);
			picIcon = (ImageView) view.findViewById(R.id.pic_icon3);
			fromContent= (TextView) view.findViewById(R.id.tv_from_content); // 标签内容
			readNum = (TextView) view.findViewById(R.id.tv_readnum); // 阅读次数
		}
	}
}
