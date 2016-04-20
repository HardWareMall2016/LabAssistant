package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.user.model.AskAgain;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.shiyanzhushou.app.R;

public class AskAgainAdapter extends ListBaseAdapter {
	private Context mContext;
	
	private DisplayImageOptions options;
	
	public AskAgainAdapter(Context context){
		mContext = context;
		options = BitmapLoaderUtil.loadDisplayImageOptions();
	}
	
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		// 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.askmeagain, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		final AskAgain askAgain = (AskAgain) _data.get(position);

		String path = ApiHttpClient.getImageApiUrl(askAgain.getHead());
		ImageLoader.getInstance().displayImage(path, vh.iv_defaulthead);

		vh.iv_defaulthead.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showUserCenter(mContext, askAgain.getAuid(),
						askAgain.getAskname());
			}
		});

		vh.tv_name_level.setText("Lv" + askAgain.getRank());
		vh.tv_who_answerafter.setText(askAgain.getZnickname() + "追问我：");
		vh.tv_answer_timebefore.setText(askAgain.getIntputtime());
		
		String content=askAgain.getAcontent();
		if(!StringUtils.isEmpty(askAgain.getAimage())){
			content="【图片】";
		}
		vh.tv_answer_content.setText(askAgain.getAnickname()+"的回答："+content);
		
		String Zcontent=askAgain.getZcontent();
		if(StringUtils.isEmpty(Zcontent)){
			Zcontent="【图片】";
		}
		vh.tv_answerafter_content.setText(Zcontent);
		
		
		
		//vh.tv_answer_content.setText(askAgain.getAcontent());
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
		private ImageView iv_defaulthead;
		private TextView tv_name_level;
		private TextView tv_who_answerafter;
		private TextView tv_answer_timebefore;
		private TextView tv_answerafter_content;
		private TextView tv_answer_content;
		private TextView tv_question_content;
		public ImageView image;

		public ViewHolder(View view) {
			iv_defaulthead = (ImageView) view.findViewById(R.id.iv_defaulthead);
			tv_name_level = (TextView) view.findViewById(R.id.tv_name_level);
			tv_who_answerafter = (TextView) view
					.findViewById(R.id.tv_who_answerafter);
			tv_answer_timebefore = (TextView) view
					.findViewById(R.id.tv_answer_timebefore);
			tv_answerafter_content = (TextView) view
					.findViewById(R.id.tv_answerafter_content);
			tv_answer_content = (TextView) view
					.findViewById(R.id.tv_answer_content);
			tv_question_content = (TextView) view
					.findViewById(R.id.tv_question_content);
			image=(ImageView) view.findViewById(R.id.qImage);
		}
	}
}
