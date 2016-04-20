package net.oschina.app.v2.activity.favorite.adapter;

import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.FavoriteList.Favorite;
import net.oschina.app.v2.ui.AvatarView;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.UIHelper;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class FavoriteAdapter extends ListBaseAdapter {

	private boolean isAttention;
	private OnClickListener mOnClickListener;
	private DisplayImageOptions options;
	
	public FavoriteAdapter(){
		 options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.ic_default_avatar);
	}
	@SuppressLint("InflateParams")
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.v2_list_cell_simple_text, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final Favorite item = (Favorite) _data.get(position);
		

		vh.itemName.setText(item.getNickname());
		vh.item_level.setText("LV" + item.getRank());
		String company=item.getCompany();
		if(TextUtils.isEmpty(company)||"null".equals(company)){
			company="";
		}
		vh.itemComany.setText(company);

		ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(item.getHead()), vh.itemImage,options);
		
		IvSignUtils.displayIvSignByType(item.getType(), vh.iv_sign,vh.avstarBg);

		/*if (item.getRealname_status()==1) {
			vh.itemResolved.setVisibility(View.VISIBLE);
		} else {
			vh.itemResolved.setVisibility(View.GONE);
		}*/
		
		if (isAttention) {
			if(item.getSame()==1){
				vh.itemAttention.setEnabled(false);
				vh.itemAttention.setText("已关注");
			}else{
				vh.itemAttention.setEnabled(true);
				vh.itemAttention.setText("关注");
			}
		} else {
			vh.itemAttention.setText("取消关注");
		}
		vh.itemAttention.setTag(item);
		vh.itemAttention.setOnClickListener(mOnClickListener);
		
		vh.itemImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAttention){
					UIHelper.showUserCenter(v.getContext(), item.getFuid(),
							item.getNickname());
				}else{
					UIHelper.showUserCenter(v.getContext(), item.getTuid(),
							item.getNickname());
				}
				
			}
		});
		return convertView;
	}

	public void setIsAttention(boolean isflag) {
		this.isAttention = isflag;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.mOnClickListener = onClickListener;
	}

	static class ViewHolder {
		private ImageView itemImage;
		private ImageView itemResolved,iv_sign,avstarBg;
		private TextView itemName;
		private TextView itemComany;
		private Button itemAttention;
		private TextView item_level;

		public ViewHolder(View view) {
			itemImage = (ImageView) view.findViewById(R.id.item_image);
			itemName = (TextView) view.findViewById(R.id.item_name);
			itemComany = (TextView) view.findViewById(R.id.item_company);
			item_level= (TextView) view.findViewById(R.id.item_level);
			itemAttention = (Button) view.findViewById(R.id.item_attention);
			itemResolved=(ImageView)view.findViewById(R.id.resolved);
			iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
		}
	}
}
