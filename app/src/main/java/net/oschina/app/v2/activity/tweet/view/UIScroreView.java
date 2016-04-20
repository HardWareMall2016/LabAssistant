
package net.oschina.app.v2.activity.tweet.view;

import java.util.List;

import net.oschina.app.v2.utils.DeviceUtils;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class UIScroreView extends RelativeLayout {

    private int itemSize = 4;
    private int itemSpace;
    private int itemPadding;
    private int itemWidth;
    private int itemHeight;
    
    private boolean isFinished;

    private OnItemClickListener mOnItemClickListener;

    public UIScroreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        itemSpace = DeviceUtils.dip2px(context, 30f);
        itemPadding= DeviceUtils.dip2px(context, 12f);
        
        int screenWidth = DeviceUtils.getScreenWidth(context);
        itemWidth = (screenWidth - (itemSize - 1) * itemSpace-itemPadding*2) / itemSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
     
        itemWidth = (getMeasuredWidth() - (itemSize - 1) * itemSpace-itemPadding*2) / itemSize;
        updateItemLayoutParams();
    }

    public void updateList(List<Integer> dataList,int score) {
    	if (dataList==null || dataList.size()==0) {
    		setVisibility(View.INVISIBLE);
    		return;
    	}
    	
        removeAllViews();
        LayoutParams params = null;
        setVisibility(View.VISIBLE);
        for (int i = 0; i < dataList.size(); i++) {
            View childView = createView(dataList.get(i));
            if(dataList.get(i)>score){
            	childView.setEnabled(false);
            }
            params = new LayoutParams(itemWidth, itemHeight);
            params.leftMargin = (itemWidth + itemSpace) * (i % itemSize);
            params.topMargin = (itemSpace + itemHeight) * (i / itemSize);
            addView(childView, params);
        }
    }

    private View createView(Integer data) {
        View convertView = View.inflate(getContext(), R.layout.choose_reward_item, null);
        TextView mRecommendTitle=(TextView)convertView.findViewById(R.id.tv);
//        mRecommendTitle.setTextColor(Color.RED);
        convertView.setTag(data);
        mRecommendTitle.setText(data+"分");
        
        if (!isFinished) {
            isFinished = true;
            convertView.measure(0, 0);
            itemHeight = convertView.getMeasuredHeight();
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(Integer.valueOf(v.getTag().toString()));
                }
            }
        });

        return convertView;
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

        void onItemClick(int score);
    }
}
