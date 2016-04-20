package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.user.model.AskAgain;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.StringUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.shiyanzhushou.app.R;

public class MyAnswerAfterAdapter extends ListBaseAdapter {
	private Context mContext;

	private DisplayImageOptions options;
	
	public MyAnswerAfterAdapter(Context context) {
		mContext = context;
		options = BitmapLoaderUtil.loadDisplayImageOptions();
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.myanswerafter_item, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		AskAgain askAgain = (AskAgain) _data.get(position);
		
		
		vh.tv_answer_after_who.setText("我追问"+askAgain.getAnickname());
		vh.tv_answer_after_time.setText(askAgain.getIntputtime());
		
		String Zcontent=askAgain.getZcontent();
		if(StringUtils.isEmpty(Zcontent)){
			Zcontent="【图片】";
		}
		vh.tv_answer_after_content.setText(Zcontent);
		
		String Acontent=askAgain.getAcontent();
		if(!StringUtils.isEmpty(askAgain.getAimage())){
			Acontent="【图片】";
		}
		vh.tv_answer_content.setText(askAgain.getAnickname()+"的回答："+Acontent);
		vh.tv_question_content.setText(askAgain.getQtitle());

		if (!StringUtils.isEmpty(askAgain.getQimage())) {
			vh.image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(askAgain.getQimage()),
					vh.image, options);
		} else {
			vh.image.setVisibility(View.GONE);
			vh.image.setImageBitmap(null);
		}
		
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_answer_after_who;
		public TextView tv_answer_after_time;
		public TextView tv_answer_after_content;
		
		public TextView tv_answer_content;
		public TextView tv_question_content;
		public ImageView image;

		public ViewHolder(View view) {
			tv_answer_after_who = (TextView) view.findViewById(R.id.tv_answer_after_who);
			tv_answer_after_time = (TextView) view.findViewById(R.id.tv_answer_after_time);
			tv_answer_after_content = (TextView) view.findViewById(R.id.tv_answer_after_content);
			tv_answer_content = (TextView) view.findViewById(R.id.tv_answer_content);
			tv_question_content = (TextView) view.findViewById(R.id.tv_question_content);
			image=(ImageView) view.findViewById(R.id.qImage);
		}
	}
}
