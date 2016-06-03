package net.oschina.app.v2.emoji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class SupperAdapter extends BaseAdapter {

	private Context context;
	private List<UserBean> dataList = new ArrayList<UserBean>();
	private HashMap<Integer, UserBean> userList;

	public SupperAdapter(Context context) {
		this.context = context;
	}
	
	public void setUserList(HashMap<Integer, UserBean> userList) {
		if(userList==null){
			userList = new HashMap<Integer, UserBean>();
		}
		this.userList = userList;
	}

	public void setItems(List<UserBean> dataList) {
		this.dataList = dataList;
	}

	public HashMap<Integer, UserBean> getUserList() {
		return userList;
	}
	
	@Override
	public int getCount() {
		int total = dataList.size();
		return total;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.supper_user_list_item, parent, false);
			viewHolder.itemImage = (ImageView) convertView
					.findViewById(R.id.item_image);
			viewHolder.itemLevel = (TextView) convertView
					.findViewById(R.id.item_level);
			viewHolder.itemVsign = (ImageView) convertView
					.findViewById(R.id.item_v);
			viewHolder.itemName = (TextView) convertView
					.findViewById(R.id.item_name);
			viewHolder.itemBuiness = (TextView) convertView
					.findViewById(R.id.item_buiness);
			viewHolder.item_ckbox = (CheckBox) convertView
					.findViewById(R.id.item_ckbox);
			viewHolder.avstarBg=(ImageView)convertView.findViewById(R.id.iv_avastarBg);
			viewHolder.iv_sign = (ImageView) convertView.findViewById(R.id.iv_sign);

			viewHolder.item_ckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					UserBean user=(UserBean) buttonView.getTag();
					int key = user.getId();
					
					if (isChecked) {
						if (!userList.containsKey(key)) {
							if(userList.size()==3){
								AppContext.showToast("@邀请回答不能超过3人");
								buttonView.setChecked(false);
								return;
							}
							userList.put(key, user);
						} 
					} else {
						if (userList.containsKey(key)) {
							userList.remove(key);
						} 
					}
				}
			});
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final UserBean item = dataList.get(position);

		viewHolder.itemLevel.setText("Lv" + item.getRank());
		viewHolder.itemName.setText(item.getNickname());
		if(!StringUtils.isEmpty(item.getCompany())&&!"null".equals(item.getCompany())){
			viewHolder.itemBuiness.setText(item.getCompany());
		}else{
			viewHolder.itemBuiness.setText("");
		}
		viewHolder.item_ckbox.setTag(item);
		
		IvSignUtils.displayIvSignByType(item.getType(), viewHolder.iv_sign,viewHolder.avstarBg);
		
		if(userList.containsKey(item.getId())){
			viewHolder.item_ckbox.setChecked(true);
		}else{
			viewHolder.item_ckbox.setChecked(false);
		}

		if (item.getRealname_status() == 1) {
			viewHolder.itemVsign.setVisibility(View.VISIBLE);
		} else {
			viewHolder.itemVsign.setVisibility(View.GONE);
		}

		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(item.getHead()),
				viewHolder.itemImage);
		viewHolder.itemImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showUserCenter(context, item.getId(),
						item.getNickname());
			}
		});

		return convertView;
	}

	class ViewHolder {
		ImageView itemImage;
		ImageView itemVsign,iv_sign,avstarBg;
		TextView itemName;
		TextView itemLevel;
		TextView itemBuiness;
		CheckBox item_ckbox;
	}

}
