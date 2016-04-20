package net.oschina.app.v2.activity.tweet.view;

import java.util.List;

import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.utils.DeviceUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class UIGroupView extends RelativeLayout {

	private int itemSize = 2;
	private int itemSpace;
	private int itemWidth;
	private int itemHeight;

	private boolean isFinished;
	private boolean isMultiChecked;

	private OnItemClickListener mOnItemClickListener;

	public UIGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		itemSpace = DeviceUtils.dip2px(context, 10f);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		itemWidth = (getMeasuredWidth() - (itemSize - 1) * itemSpace)
				/ itemSize;
		updateItemLayoutParams();
	}

	public void setChecked(boolean isChecked) {
		this.isMultiChecked = isChecked;
	}

	public void updateList(List<Mulu> dataList) {
		if (dataList == null || dataList.size() == 0) {
			setVisibility(View.INVISIBLE);
			return;
		}

		removeAllViews();
		LayoutParams params = null;
		setVisibility(View.VISIBLE);
		for (int i = 0; i < dataList.size(); i++) {
			View childView = createView(dataList.get(i));
			params = new LayoutParams(itemWidth, itemHeight);
			params.leftMargin = (itemWidth + itemSpace) * (i % itemSize);
			params.topMargin = (itemSpace + itemHeight) * (i / itemSize);
			addView(childView, params);
		}
	}
	

	void updateState(List<Mulu> dataList,String mulu) {
		int i=0;
		for(Mulu mul:dataList){
			if(mul.getcatname().equals(mulu)){
				View view = getChildAt(i);
				((TextView) view.findViewById(R.id.tv))
				.setTextColor(getResources().getColor(
						R.color.main_green));
				break;
			}
			i++;
		}
		
	}

	private View createView(Mulu quanBean) {
		View convertView = View
				.inflate(getContext(), R.layout.popup_item, null);
		TextView mRecommendTitle = (TextView) convertView.findViewById(R.id.tv);
		convertView.setTag(quanBean);
		mRecommendTitle.setText(quanBean.getcatname());

		if (!isFinished) {
			isFinished = true;
			convertView.measure(0, 0);
			itemHeight = convertView.getMeasuredHeight();
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnItemClickListener != null) {
					boolean isChecked=mOnItemClickListener.onItemClick((Mulu) v.getTag());
					updateState(v);
					if(isChecked==false){
						((TextView) v.findViewById(R.id.tv))
						.setTextColor(getResources().getColor(
								R.color.black));
					}else{
						((TextView) v.findViewById(R.id.tv))
						.setTextColor(getResources().getColor(
								R.color.main_green));
					}
				}
			}
		});

		return convertView;
	}

	private void updateState(View v) {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view == v) {
				((TextView) view.findViewById(R.id.tv))
						.setTextColor(getResources().getColor(
								R.color.main_green));
				// view.setBackgroundColor(getResources().getColor(R.color.main_green));
			} else {
				if (!isMultiChecked) {
					((TextView) view.findViewById(R.id.tv))
							.setTextColor(getResources().getColor(
									R.color.black));
					// view.setBackgroundColor(Color.TRANSPARENT);
				}else{
					
				}
			}
		}
	}

	private void updateItemLayoutParams() {
		LayoutParams params = null;
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			params = (LayoutParams) view.getLayoutParams();
			if (params == null) {
				params = new LayoutParams(itemWidth, itemHeight);
			}
			params.width = itemWidth;
			params.leftMargin = (itemWidth + itemSpace) * (i % itemSize);
			params.topMargin = (itemHeight + itemSpace) * (i / itemSize);
			view.setLayoutParams(params);
		}
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener {

		boolean onItemClick(Mulu quanBean);
	}
}
