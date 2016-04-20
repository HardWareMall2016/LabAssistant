package net.oschina.app.v2.activity.favorite.fragment;

import net.oschina.app.v2.activity.favorite.fragment.QuestionModel.ProblemModel;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shiyanzhushou.app.R;

public class UIRadioGroup extends RadioGroup {

	public UIRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UIRadioGroup(Context context) {
		super(context);
	}

	public void updateView(ProblemModel itemModel) {
		if (itemModel == null) {
			setVisibility(View.GONE);
			return;
		}

		removeAllViews();
		setVisibility(View.VISIBLE);

		for (int i = 0; i < 4; i++) {
			View view = createView(i, itemModel);
			view.setTag(itemModel.getId());
			addView(view);
		}
	}

	private View createView(int i, ProblemModel item) {
		View contentView = View.inflate(getContext(),
				R.layout.question_child_item_view, null);
		RadioButton button = (RadioButton) contentView;

		if (i == 0) {
			button.setText(item.getOptiona());
		} else if (i == 1) {
			button.setText(item.getOptionb());
		} else if (i == 2) {
			button.setText(item.getOptionc());
		} else if (i == 3) {
			button.setText(item.getOptiond());
		}
		return contentView;
	}

}
