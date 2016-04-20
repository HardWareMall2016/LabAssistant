package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.fragment.GiftDetailInfoDialog;
import net.oschina.app.v2.activity.user.model.GiftPojo;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.StringUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class GiftAdapter extends ListBaseAdapter {
	private Context mContext;
	private boolean isOpening = false;
	
	public GiftAdapter(Context context){
		mContext = context;
	}

	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.gift_item_ly, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if (_data.size() > position * 2) {
			final GiftPojo g = (GiftPojo) _data.get(position * 2);
			vh.layout01.setVisibility(View.VISIBLE);
			String title = StringUtils.getStringWithOmit(g.getTitle(), 10);
			vh.tv_cargo_name01.setText(title);
			vh.tv_money01.setText(String.valueOf(g.getIntergal()));
			if (g.getIsHot() == 1) {
				vh.goods_sign01.setVisibility(View.VISIBLE);
				vh.goods_sign01.setBackgroundResource(R.drawable.hot_sign);
			} else if (g.getRechargestatus() == 1) {
				vh.goods_sign01.setVisibility(View.VISIBLE);
				vh.goods_sign01.setBackgroundResource(R.drawable.exchanged_sign);
			} else {
				vh.goods_sign01.setVisibility(View.GONE);
			}
			// String prePath = ApiHttpClient.getImageApiUrl(g.getThumb());
			// String str = prePath.subSequence(23,
			// prePath.length()).toString();
			// // 第一列的圖片的加載
			// ImageLoader.getInstance().displayImage(str, vh.iv_mycargo01);
			ImageLoader.getInstance().displayImage(g.getThumb(),
					vh.iv_mycargo01);
			
			vh.layout01.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isOpening) {
						return;
					}
					isOpening = true;
					requestGiftDetailInfo(g);
				}
			});
		} else {
			vh.layout01.setVisibility(View.INVISIBLE);
			vh.goods_sign01.setVisibility(View.GONE);
		}

		if (_data.size() > position * 2 + 1) {
			final GiftPojo g = (GiftPojo) _data.get(position * 2 + 1);
			vh.layout02.setVisibility(View.VISIBLE);
			String title = StringUtils.getStringWithOmit(g.getTitle(), 10);
			vh.tv_cargo_name02.setText(title);
			vh.tv_money02.setText(String.valueOf(g.getIntergal()));
			if (g.getIsHot() == 1) {
				vh.goods_sign02.setVisibility(View.VISIBLE);
				vh.goods_sign02.setBackgroundResource(R.drawable.hot_sign);
			} else if (g.getRechargestatus() == 1) {
				vh.goods_sign02.setVisibility(View.VISIBLE);
				vh.goods_sign02.setBackgroundResource(R.drawable.exchanged_sign);
			} else {
				vh.goods_sign02.setVisibility(View.GONE);
			}
			// String prePath = ApiHttpClient.getImageApiUrl(g.getThumb());
			// String str = prePath.subSequence(23,
			// prePath.length()).toString();
			// ImageLoader.getInstance().displayImage(str, vh.iv_mycargo02);
			ImageLoader.getInstance().displayImage(g.getThumb(),
					vh.iv_mycargo02);
			
			vh.layout02.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isOpening) {
						return;
					}
					isOpening = true;
					requestGiftDetailInfo(g);
				}
			});
		} else {
			vh.layout02.setVisibility(View.INVISIBLE);
			vh.goods_sign02.setVisibility(View.GONE);
		}
		// ShiWu shiWu=(ShiWu) _data.get(position);
		// String basePath=ApiHttpClient.DEV_API_IMAGE_URL.substring(0,
		// ApiHttpClient.DEV_API_IMAGE_URL.length()-1);

		return convertView;
	}

	@Override
	public int getDataSize() {
		if (_data != null) {
			if (_data.size() % 2 == 0) {
				return _data.size() / 2;
			} else {
				return _data.size() / 2 + 1;
			}
		}
		return 0;
	}

	static class ViewHolder {

		private View layout01, layout02;
		private ImageView iv_mycargo01, iv_mycargo02;
		private ImageView goods_sign01, goods_sign02;
		private TextView tv_cargo_name01, tv_cargo_name02;
		private TextView tv_money01, tv_money02;

		public ViewHolder(View view) {

			layout01 = view.findViewById(R.id.ll_myanswer01);
			// 我的商品
			iv_mycargo01 = (ImageView) view.findViewById(R.id.iv_mycargo01);
			// 商品名称
			tv_cargo_name01 = (TextView) view
					.findViewById(R.id.tv_cargo_name01);
			// 钱的数目
			tv_money01 = (TextView) view.findViewById(R.id.tv_money01);

			layout02 = view.findViewById(R.id.ll_myanswer02);
			// 我的商品
			iv_mycargo02 = (ImageView) view.findViewById(R.id.iv_mycargo02);
			// 商品名称
			tv_cargo_name02 = (TextView) view
					.findViewById(R.id.tv_cargo_name02);
			// 钱的数目
			tv_money02 = (TextView) view.findViewById(R.id.tv_money02);
			
			goods_sign01 = (ImageView) view.findViewById(R.id.goods_sign01);
			goods_sign02 = (ImageView) view.findViewById(R.id.goods_sign02);
		}
	}

	private void showGiftDetailInfo(GiftPojo daily) {
		if (!GiftDetailInfoDialog.getInstance().isOpen())
			GiftDetailInfoDialog.getInstance().createReviceUserDialog(
					mContext, daily);
	}

	private void requestGiftDetailInfo(GiftPojo daily) {
		NewsApi.getGiftDetail(AppContext.instance().getLoginUid(),
				daily.getId(), addInterestedHandler);
	}

	protected JsonHttpResponseHandler addInterestedHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				JSONArray dataList = new JSONArray(response.getString("data"));
				if (dataList.length() > 0) {

					GiftPojo pojo = GiftPojo.parseDetail(dataList
							.getJSONObject(0));
					
					
					showGiftDetailInfo(pojo);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isOpening = false;
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			isOpening = false;
			AppContext.showToast("获取详情失败了");
		}
	};
}
