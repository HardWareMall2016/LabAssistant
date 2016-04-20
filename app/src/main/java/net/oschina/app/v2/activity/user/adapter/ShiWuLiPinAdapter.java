package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.user.model.ShiWu;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.StringUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class ShiWuLiPinAdapter extends ListBaseAdapter {
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.wodewupin, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		// ImageLoader加载.
		ShiWu shiWu = (ShiWu) _data.get(position);
		// 加载图片
		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(shiWu.getThumb()), vh.iv_mycargo);
		// 商品名称
		String str = StringUtils.getStringWithOmit(shiWu.getName(), 8);
		vh.tv_cargo_name.setText(str);

		vh.tv_money.setText(shiWu.getIntegral() + "");

		vh.tv_exchange_info.setText(shiWu.getDesc());
		return convertView;
	}

	static class ViewHolder {
		private ImageView iv_mycargo;
		private TextView tv_cargo_name;
		private TextView tv_money;
		private TextView tv_exchange_info;

		public ViewHolder(View view) {
			// 我的商品
			iv_mycargo = (ImageView) view.findViewById(R.id.iv_mycargo);
			// 商品名称
			tv_cargo_name = (TextView) view.findViewById(R.id.tv_cargo_name);
			// 钱的数目
			tv_money = (TextView) view.findViewById(R.id.tv_money);
			// 商品交换信息
			tv_exchange_info = (TextView) view
					.findViewById(R.id.tv_exchange_info);
		}
	}
}
