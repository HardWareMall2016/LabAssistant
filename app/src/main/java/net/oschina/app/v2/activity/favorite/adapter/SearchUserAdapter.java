package net.oschina.app.v2.activity.favorite.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
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

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.FavoriteList.Favorite;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

public class SearchUserAdapter extends ListBaseAdapter {
	private OnClickListener mOnClickListener;
	private DisplayImageOptions options;

	private String mHighLight;

	public SearchUserAdapter(){
		 options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.ic_default_avatar);
	}

	public void setHighLight(String highLight) {
		this.mHighLight = highLight;
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

		if (!StringUtils.isEmpty(mHighLight)) {
			vh.itemName.setText(Html.fromHtml(StringUtils.getShowSingleLineWithHighlight(item.getNickname(), mHighLight)));
		}else{
			vh.itemName.setText(item.getNickname());
		}

		vh.item_level.setText("LV" + item.getRank());
		String company=item.getCompany();
		if(TextUtils.isEmpty(company)||"null".equals(company)){
			company="";
		}

		if (!StringUtils.isEmpty(mHighLight)) {
			vh.itemComany.setText(Html.fromHtml(StringUtils.getShowSingleLineWithHighlight(company, mHighLight)));
		}else{
			vh.itemComany.setText(company);
		}

		ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(item.getHead()), vh.itemImage, options);
		
		IvSignUtils.displayIvSignByType(item.getType(), vh.iv_sign, vh.avstarBg);

		vh.itemAttention.setEnabled(AppContext.instance().isLogin());

		if (item.getSame() != 1) {
			vh.itemAttention.setText("关注");
		} else {
			vh.itemAttention.setEnabled(false);
			vh.itemAttention.setText("已关注");
		}
		vh.itemAttention.setTag(item);
		vh.itemAttention.setOnClickListener(mOnClickListener);
		
		vh.itemImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(item.getSame()==1){
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
