package net.oschina.app.v2.activity.favorite.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.oschina.app.v2.activity.favorite.fragment.QuestionModel.ProblemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class UIPanelView extends LinearLayout {

	private OnClickListener mOnClickListener;
	private Map<String, String> hashMaps = new HashMap<String, String>();

	public UIPanelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public UIPanelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public UIPanelView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void updateView(QuestionModel model) {
		if (model == null) {
			setVisibility(View.GONE);
			return;
		}

		removeAllViews();
		setVisibility(View.VISIBLE);
		setOrientation(VERTICAL);

		List<ProblemModel> dataList = model.getDataList();
		if (dataList != null) {
			for (ProblemModel item : dataList) {
				View child = createView(item);
				addView(child);
			}

			View view = LayoutInflater.from(getContext()).inflate(
					R.layout.question_page_item_button, this, false);
			view.setOnClickListener(mOnClickListener);
			addView(view);
		}
	}

	private View createView(ProblemModel item) {
		View contentView = View.inflate(getContext(),
				R.layout.question_page_item_view, null);
		TextView textView = (TextView) contentView
				.findViewById(R.id.item_content);
		UIRadioGroup radioGroup = (UIRadioGroup) contentView
				.findViewById(R.id.itemGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton button = (RadioButton) group
						.findViewById(checkedId);
				String key = button.getTag().toString();
				String value = button.getText().toString();
				hashMaps.put(key, value);
			}
		});

		textView.setText(item.getTitle());
		radioGroup.updateView(item);

		return contentView;
	}

	public String getOptions() {
		JSONArray array = new JSONArray();

		try {
			JSONObject json = null;
			for (Map.Entry<String, String> entry : hashMaps.entrySet()) {
				json = new JSONObject();
				json.put(entry.getKey(), entry.getValue());
				array.put(json.toString());
			}
		} catch (JSONException e) {
		}

		return array.toString();
	}

	public void setOnClickListener(OnClickListener onListener) {
		this.mOnClickListener = onListener;
	}
}
