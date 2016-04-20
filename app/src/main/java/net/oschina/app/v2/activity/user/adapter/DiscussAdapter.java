package net.oschina.app.v2.activity.user.adapter;

//import net.oschina.app.v2.model.News;
//import net.oschina.app.v2.utils.DateUtil;
//import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.activity.user.model.DiscussPojo;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class DiscussAdapter extends ListBaseAdapter {

	@SuppressLint("InflateParams")
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.a_discuss_item_ly, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final DiscussPojo daily = (DiscussPojo) _data.get(position);

		vh.tv_title.setText(daily.getContent());
		vh.tv_time.setText(daily.getInputtime());
		vh.tv_ding.setText(daily.getHits() + "");
		vh.tv_ding.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ding(daily);
			}
		});
		vh.iv_ding.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ding(daily);
			}
		});
		if (daily.getIspraise() == 0) {
			vh.iv_ding.setImageResource(R.drawable.praise);
		} else {
			vh.iv_ding.setImageResource(R.drawable.module_taolu_ding);
		}
		vh.tv_name.setText(daily.getUserName());
		vh.tv_rank.setText("Lv" + daily.getRank());
		ImageLoader.getInstance().displayImage(
				String.format(ApiHttpClient.DEV_API_IMAGE_URL,
						daily.getUserIcon()), vh.iv_daily);

		vh.tv_ding.setTag(position);
		
		IvSignUtils.displayIvSignByType(daily.getType(), vh.iv_sign,vh.avstarBg);
	/*	if(daily.getType()==21){
			vh.iv_sign.setVisibility(View.VISIBLE);
		}else{
			vh.iv_sign.setVisibility(View.GONE);
		}*/

		return convertView;
	}

	static class ViewHolder {
		public TextView tv_title, tv_name, tv_time, tv_ding, tv_rank;
		public ImageView iv_daily, iv_ding,iv_sign,avstarBg;

		public ViewHolder(View view) {
			tv_title = (TextView) view.findViewById(R.id.content);
			tv_time = (TextView) view.findViewById(R.id.time);
			tv_ding = (TextView) view.findViewById(R.id.tv_ding);
			iv_ding = (ImageView) view.findViewById(R.id.iv_ding);

			tv_name = (TextView) view.findViewById(R.id.user_name);
			tv_rank = (TextView) view.findViewById(R.id.user_rank);

			iv_daily = (ImageView) view.findViewById(R.id.user_icon);
			iv_sign=(ImageView)view.findViewById(R.id.iv_sign);
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
		}
	}

	private void ding(DiscussPojo d) {

		if (AppContext.instance().isLogin()) {

			int uid = AppContext.instance().getLoginUid();
			int did = d.getId();

			NewsApi.doHit(uid, did, new JsonHttpResponseHandler() {
			});
			if(d.getIspraise()==1){
				d.setHits(d.getHits() - 1);
				d.setIspraise(0);
			}else{
				d.setHits(d.getHits() + 1);
				d.setIspraise(1);
			}
			this.notifyDataSetChanged();

		} else {
			AppContext.showToast("登录才能投票哦");
		}

	}

}
