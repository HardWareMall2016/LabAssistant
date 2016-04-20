package net.oschina.app.v2.activity.find.adapter;

//import net.oschina.app.v2.model.News;
//import net.oschina.app.v2.utils.DateUtil;
//import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Daily;
import net.oschina.app.v2.utils.DateUtil;
import net.oschina.app.v2.utils.UIHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hb.views.PinnedSectionListView.PinnedSectionListAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class DailyAdapter extends ListBaseAdapter implements PinnedSectionListAdapter{
	private Context mContext;
	
	public DailyAdapter(Context context){
		mContext = context;
	}

	@SuppressLint("InflateParams")
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.daily_list_item, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		final Daily daily = (Daily) _data.get(position);
		
		if(daily.getType()==Daily.SECTION){
			LinearLayout linearLayout = (LinearLayout) getLayoutInflater(
					parent.getContext()).inflate(
					R.layout.daily_list_group_item, null);
			TextView textView = (TextView) linearLayout.findViewById(R.id.group_tv);
			textView.setText(DateUtil.parseTime(daily.getInputtime()));
			return linearLayout;
		}
		if(daily.isTop()){
			View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.v2_list_header_context_detail, null);
			ImageView header_bg = (ImageView) header.findViewById(R.id.tv_header_bg);
			TextView context_date = (TextView) header.findViewById(R.id.context_date);
			TextView context_detail = (TextView) header.findViewById(R.id.tv_context_detail);
			
			String url = String.format(ApiHttpClient.DEV_API_IMAGE_URL, daily.getThumb());
			ImageLoader.getInstance().displayImage(url, header_bg);
			context_detail.setText(daily.getTitle());
			context_date.setText(DateUtil.parseTime(daily.getInputtime()));
			header.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (daily != null) {
						if (daily.isWenJuan()) {
							UIHelper.showQuestionCase(mContext, daily);
						} else {
							UIHelper.showDailyDetailRedirect(mContext, daily);
						}
					}
					
				}
			});
			return header;
		}
		
		vh.tv_title.setText(daily.getTitle());
		vh.tv_time.setText(DateUtil.parseTime(daily.getInputtime()));
		
		String url = String.format(ApiHttpClient.DEV_API_IMAGE_URL, daily.getThumb());
		ImageLoader.getInstance().displayImage(url, vh.iv_daily);
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (daily != null) {
					if (daily.isWenJuan()) {
						UIHelper.showQuestionCase(mContext, daily);
					} else {
						Intent intent = new Intent(mContext, ShowTitleDetailActivity.class);
						intent.putExtra("id", daily.getId());
						intent.putExtra("fromTitle", R.string.find_shoushouribao);
						intent.putExtra("img", daily.getThumb());
						mContext.startActivity(intent);
//						UIHelper.showDailyDetailRedirect(mContext, daily);
					}
				}
				
			}
		});
		

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

	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if(_data.size()>position){
			return ((Daily) _data.get(position)).getType();
		}else{
			return 0;
		}
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return Daily.SECTION==viewType;
	}

}
