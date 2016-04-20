package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.user.model.XiTongXiaoXi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.DateUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class XiTongXiaoXiAdapter extends ListBaseAdapter {
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.xirtongxiaoxi, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		/* Answer answer=(Answer) _data.get(position); */
		// AskAgain askAgain=(AskAgain) _data.get(position);
		/*
		 * vh.tv_content.setText(askAgain.getContent());
		 * vh.tv_answer_timebefore.setText(askAgain.getIntputtime()+"");
		 * vh.tv_answer_origin.setText(askAgain.getQuid()+"");
		 */
		XiTongXiaoXi xiTongXiaoXi = (XiTongXiaoXi) _data.get(position);
		vh.tv_isfinish_newtask.setText(xiTongXiaoXi.getDescription());// 设置系统消息的内容
		vh.tv_timebefore.setText(xiTongXiaoXi.getInputtime());//设置系统消息的时间
		vh.tv_show_sysinfo.setText(xiTongXiaoXi.getContent());
//		vh.tv_timebefore.setText(DateUtil.getFormatTime(xiTongXiaoXi
//				.getInputtime()));
		if("system".equals(xiTongXiaoXi.getValname())){
			vh.iv_xitongxiaoxi.setImageResource(R.drawable.systeminfo_system);
		}else if("attest".equals(xiTongXiaoXi.getValname())){
			vh.iv_xitongxiaoxi.setImageResource(R.drawable.systeminfo_attest);
		}else if("check".equals(xiTongXiaoXi.getValname())){
			vh.iv_xitongxiaoxi.setImageResource(R.drawable.systeminfo_check);
		}else if("activity".equals(xiTongXiaoXi.getValname())){
			vh.iv_xitongxiaoxi.setImageResource(R.drawable.systeminfo_activity);
		}else if("notify".equals(xiTongXiaoXi.getValname())){
			vh.iv_xitongxiaoxi.setImageResource(R.drawable.systeminfo_notify);
		}
		
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_xitongxiaoxi;
		private TextView tv_isfinish_newtask;
		private TextView tv_timebefore;
		private TextView tv_show_sysinfo;

		public ViewHolder(View view) {
			// 系统消息图标
			iv_xitongxiaoxi = (ImageView) view
					.findViewById(R.id.iv_xitongxiaoxi);
			// 回答问题的提示
			tv_isfinish_newtask = (TextView) view
					.findViewById(R.id.tv_isfinish_newtask);
			// 问题回答完成的时间fe
			tv_timebefore = (TextView) view.findViewById(R.id.tv_timebefore);
			// 完成任务的提示
			tv_show_sysinfo = (TextView) view
					.findViewById(R.id.tv_show_sysinfo);
		}
	}
}
