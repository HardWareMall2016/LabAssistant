package net.oschina.app.v2.activity.find.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**** NoScrollGrdView ***/
public class CustomGrdView extends GridView {

	public CustomGrdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomGrdView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomGrdView(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
