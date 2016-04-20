package net.oschina.app.v2.activity.user.adapter;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.model.Intersted;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.User;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class MyInterstedAdapter extends ListBaseAdapter {

	private List<Intersted> choicedIds = new ArrayList<Intersted>();

	
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		Intersted intersted = (Intersted) _data.get(position);
		ViewHolder vh = null;
		int type = getItemViewType(position);
		if (0 == type) {
			if (convertView == null || convertView.getTag() == null) {
				convertView = getLayoutInflater(parent.getContext()).inflate(
						R.layout.a_simple_category_title, parent, false);
				vh = new ViewHolder(convertView, 0);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.simple_category_title.setText(intersted.getCatname()+ "    (至少选一项)");
		} else if (1 == type) {
			if (convertView == null || convertView.getTag() == null) {
				convertView = getLayoutInflater(parent.getContext()).inflate(
						R.layout.woganxingqude, parent, false);
				vh = new ViewHolder(convertView, 1);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tv_myinterst_content.setText(intersted.getCatname());
			ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(intersted.getImage()), vh.iv_myinterst);
			vh.iv_isIntest.setTag(intersted);
			// 如果注册已选兴趣标签，这里要默认选中
			if(isChecked(intersted.getCatname())){
				vh.iv_isIntest.setChecked(true);
				choicedIds.add(intersted);
			}else{
				vh.iv_isIntest.setChecked(false);
			}
			vh.iv_isIntest.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (buttonView.getTag() instanceof Intersted) {
								Intersted id = (Intersted) buttonView.getTag();
								
								if (isChecked) {
									if (!choicedIds.contains(id)) {
										choicedIds.add(id);
									}
								} else {
									if (choicedIds.contains(id)) {
										choicedIds.remove(id);
									}
								}
								onSelectedListener.onCheckedChanged();

							}

						}
					});
		}
		return convertView;
	}
	
	private boolean isChecked(String text) {
		User user=AppContext.instance().getLoginInfo();
		String interest=user.getInterest();
		if (!TextUtils.isEmpty(interest)) {
			String[] data=interest.split(" ");
			for (String item : data) {
				if (item.equals(text.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	static class ViewHolder {
		private ImageView iv_myinterst;
		private TextView tv_myinterst_content;
		private CheckBox iv_isIntest;
		private TextView simple_category_title;

		public ViewHolder(View view, int type) {

			if (0 == type) {
				simple_category_title = (TextView) view
						.findViewById(R.id.simple_category_tx01);

			} else {
				// 我感兴趣的
				iv_myinterst = (ImageView) view.findViewById(R.id.iv_myinterst);
				// 我感兴趣的分类
				tv_myinterst_content = (TextView) view
						.findViewById(R.id.tv_myinterst_content);
				// 是否感兴趣的图标
				iv_isIntest = (CheckBox) view.findViewById(R.id.iv_isIntest);
			}

		}
	}

	@Override
	public int getItemViewType(int position) {

		if (position < _data.size()) {
			Intersted intersted = (Intersted) _data.get(position);

			return intersted.isBigCategory() ? 0 : 1;

		}

		return 2;
	}

	@Override
	public int getViewTypeCount() {

		return 10;
	}

	public List<Intersted> getChoicedIds() {
		return choicedIds;
	}

	/**
	 * -3 没有数据； －2 没有选任何数据； －1 有分类没有选； 0 合法
	 * 
	 * @return
	 */
	public int isHaveChoiceInEveryCategory() {

		if (null == _data || _data.isEmpty()) {
			return -3;
		}

		if (choicedIds.isEmpty()) {
			return -2;
		}

		Intersted intersted = null;
		for (int i = 0; i < _data.size(); i++) {
			intersted = (Intersted) _data.get(i);

			if (intersted.isBigCategory()) {
				boolean isOk = false;
				for (Intersted item : choicedIds) {
					if (item.getCategoryId() == intersted.getId()) {
						isOk = true;
						break;
					}
				}

				if (!isOk) {
					return -1;
				}
			}
		}

		return 0;
	}
	
	private OnChoiceSelectedListener onSelectedListener;
	
	public OnChoiceSelectedListener getOnSelectedListener() {
		return onSelectedListener;
	}

	public void setOnSelectedListener(OnChoiceSelectedListener onSelectedListener) {
		this.onSelectedListener = onSelectedListener;
	}

	public interface OnChoiceSelectedListener{
		void onCheckedChanged();
	}

}
