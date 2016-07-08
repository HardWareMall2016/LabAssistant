package net.oschina.app.v2.activity.favorite.adapter;

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

import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.FavoriteArticleList;
import net.oschina.app.v2.model.FavoriteList.Favorite;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.UIHelper;

public class FavoriteArticleAdapter extends ListBaseAdapter {

	private DisplayImageOptions options;

	public FavoriteArticleAdapter(){
		 options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.pic_bg);
	}
	@SuppressLint("InflateParams")
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.v2_list_cell_fav_article, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final FavoriteArticleList.FavoriteArticle item = (FavoriteArticleList.FavoriteArticle) _data.get(position);

		ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(item.getThumb()), vh.itemImage, options);
		vh.title.setText(item.getTitle());
		vh.time.setText(item.getDate());

		return convertView;
	}

	static class ViewHolder {
		private ImageView itemImage;
		private TextView title;
		private TextView summary;
		private TextView come_from;
		private TextView time;

		public ViewHolder(View view) {
			itemImage = (ImageView) view.findViewById(R.id.img);
			title = (TextView) view.findViewById(R.id.title);
			summary = (TextView) view.findViewById(R.id.summary);
			come_from= (TextView) view.findViewById(R.id.come_from);
			time = (TextView) view.findViewById(R.id.time);
		}
	}
}
