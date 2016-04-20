package net.oschina.app.v2.activity.friend.adapter;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class TweetUserAdapter extends BaseAdapter {
	private static final int ITEM_TITLE = 0;
	private static final int ITEM_NORMAL = 1;
	private static final int ITEM_SIZE  = ITEM_NORMAL + 1;
	
	private Context context;
	private List<UserBean> dataList=new ArrayList<UserBean>();
	
	private int uid;
	public TweetUserAdapter(Context context) {
		this.context=context;
		uid=AppContext.instance().getLoginUid();
	}
	
	public void setItems(List<UserBean> dataList) {
		this.dataList=dataList;
	}
	
	@Override
	public int getCount() {
		int total = dataList.size();
		if (total > 0) {
			return total + 1;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_SIZE;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ITEM_TITLE;
		}
		return ITEM_NORMAL;
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		int viewType=getItemViewType(position);
		
		if (convertView==null) {
			if (viewType==ITEM_NORMAL) {
				viewHolder=new ViewHolder();
				convertView=LayoutInflater.from(context).inflate(R.layout.tweet_review_zhichi_list_item, parent, false);
				viewHolder.itemImage=(ImageView) convertView.findViewById(R.id.item_image);
				viewHolder.itemLevel=(TextView) convertView.findViewById(R.id.item_level);
				viewHolder.itemVsign=(ImageView) convertView.findViewById(R.id.item_v);
				viewHolder.itemName=(TextView) convertView.findViewById(R.id.item_name);
				viewHolder.itemBuiness=(TextView) convertView.findViewById(R.id.item_buiness);
				//viewHolder.itemView= convertView.findViewById(R.id.item_view);
				viewHolder.rl_guanzhu=convertView.findViewById(R.id.rl_guanzhu);
				viewHolder.guanzhu_biaoqian=(TextView) convertView.findViewById(R.id.guanzhu_biaoqian);
				viewHolder.guanzhu_shenma=(TextView) convertView.findViewById(R.id.guanzhu_shenma);
				
				viewHolder.iv_sign = (ImageView) convertView.findViewById(R.id.iv_sign);
				viewHolder.avstarBg=(ImageView)convertView.findViewById(R.id.iv_avastarBg);
				
				viewHolder.rl_guanzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						addUserAttention(Integer.valueOf(v.getTag().toString()),position-1);
					}
				});
				convertView.setTag(viewHolder);
			} else {
				TextView textView=new TextView(context);
				textView.setPadding(10, 10, 10, 10);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				convertView=textView;
			}
		} else {
			if (viewType==ITEM_NORMAL) {
				viewHolder=(ViewHolder) convertView.getTag();
			} 
		}

		if (viewType==ITEM_TITLE) {
			((TextView) convertView).setText("共"+dataList.size()+"个支持者");
		} else {
			
			final UserBean item=dataList.get(position-1);
			
			viewHolder.itemLevel.setText("Lv"+item.getRank());
			viewHolder.itemName.setText(item.getNickname());
			if(!StringUtils.isEmail(item.getCompany())&&!"null".equals(item.getCompany())){
				viewHolder.itemBuiness.setText(item.getCompany());
			}else{
				viewHolder.itemBuiness.setText("");
			}
			viewHolder.guanzhu_shenma.setText(String.valueOf(item.getSupporNum()));
		//	viewHolder.itemView.setTag(item.getUid());
			viewHolder.rl_guanzhu.setTag(item.getUid());
		
			IvSignUtils.displayIvSignByType(item.getType(), viewHolder.iv_sign,viewHolder.avstarBg);
			
			ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(item.getHead()), 
					viewHolder.itemImage);
			
			viewHolder.itemImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIHelper.showUserCenter(context, item.getUid(), item.getNickname());
				}
			});
			
			// 已关注用户和当前用户
			if(item.getSame()==1 ||item.getUid()==uid){
				//viewHolder.guanzhu_biaoqian.setText("已关注");
				//viewHolder.guanzhu_biaoqian.setSelected(true);
				
				viewHolder.guanzhu_biaoqian.setBackgroundColor(android.graphics.Color.parseColor("#DEDEDE"));
				viewHolder.guanzhu_biaoqian.setTextColor(android.graphics.Color.parseColor("#A9A9A9"));
				viewHolder.guanzhu_shenma.setBackgroundColor(android.graphics.Color.parseColor("#F6F6F6"));
				viewHolder.guanzhu_shenma.setTextColor(android.graphics.Color.parseColor("#A9A9A9"));
			}else{
				viewHolder.guanzhu_biaoqian.setSelected(false);
			}
			
			
		}
		
		return convertView;
	}
	
	class ViewHolder  {
		ImageView itemImage,avstarBg,iv_sign;
		ImageView itemVsign;
		TextView itemName;
		TextView itemLevel;
		TextView itemBuiness;
		View itemView;
		TextView guanzhu_biaoqian;
		TextView guanzhu_shenma;
		View rl_guanzhu;
	}
	
	void addUserAttention(int tuid,final int position) {
		int uid=AppContext.instance().getLoginUid();
		NewsApi.addAttention(uid, tuid, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				AppContext.showToast(response.optString("desc", ""));
				if (response.optInt("code")==88) {
					final UserBean item=dataList.get(position);
					item.setSame(1);
					item.setSupporNum(item.getSupporNum()+1);
					notifyDataSetChanged();
				}
				
			}
		});
	}
}
