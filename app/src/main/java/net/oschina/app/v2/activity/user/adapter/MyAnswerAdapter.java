package net.oschina.app.v2.activity.user.adapter;

import net.oschina.app.v2.activity.user.model.Answer;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.utils.LabelUtils;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class MyAnswerAdapter extends ListBaseAdapter{
	private Context mContext;
	
	
	
	private String contentTip="<font color=#FBB4A7>我的回答：</font>";

	private String newReplyStr;
	

	private String commentTitle;
	
	public void setCommentTitle(String title){
		commentTitle=title;
	}
	
	
	public MyAnswerAdapter(Context context,boolean isCurrent){
		mContext = context;
		if(isCurrent==false){
			contentTip="<font color=#FBB4A7>TA的回答：</font>";
			commentTitle="TA";
		}else{commentTitle="我";}
		if(newReplyStr==null){
			newReplyStr=context.getResources().getString(R.string.newReply);
		}
		
	}
	
	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		//定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.myanswer_item, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		Answer answer=(Answer) _data.get(position);
		
		vh.question_title_tv.setText(answer.getTitle());
		vh.question_time_tv.setText(answer.getInputtime());

		/*vh.question_category_tv.setText(Html.fromHtml("<font color=#2FBDE7>" + answer.getLabel()
				+ "</font>"));*/


		String label = answer.getLabel();
		if (TextUtils.isEmpty(label)) {
			label = "暂无";
			vh.question_category_tv.setText(Html.fromHtml("<font color=#2FBDE7>" + label
					+ "</font>"));
		}else{
			//vh.question_category_tv.setText(label);
			vh.question_category_tv.setText(LabelUtils.parseLable(label));
			int labelBackgroundId= LabelUtils.getBgResIdByLabel(label);
			vh.question_category_tv.setBackgroundResource(labelBackgroundId);
		}
		
		
		if(!StringUtils.isEmpty(answer.getContent())&&!"null".equals(answer.getContent())){
			vh.comment_rl.setVisibility(View.VISIBLE);
			vh.comment_spliter.setVisibility(View.VISIBLE);
			vh.comment_content_tv.setText(Html.fromHtml(contentTip+answer.getContent()));
			vh.pic_icon.setVisibility(View.GONE);
		}else{
			vh.comment_content_tv.setText(Html.fromHtml(contentTip));
			vh.comment_rl.setVisibility(View.GONE);
			vh.comment_spliter.setVisibility(View.GONE);
			vh.pic_icon.setVisibility(View.VISIBLE);
		}
		
		if(TextUtils.isEmpty(answer.getImage())){
			vh.pic_icon.setVisibility(View.GONE);
		}else{
			vh.comment_rl.setVisibility(View.VISIBLE);
			vh.pic_icon.setVisibility(View.VISIBLE);
		}
		
		
		if(answer.getIsadopt()==1){
			vh.comment_status_iv.setText("被采纳");
		}else{
			//vh.comment_status_iv.setText("待采纳");
			vh.comment_status_iv.setVisibility(View.GONE);
		}
		
		final Ask ask = new Ask();
		ask.setId(answer.getQid());
		ask.setanum(answer.getAnum());
		ask.setnickname(answer.getQnickname());
		ask.setLabel(answer.getLabel());
		ask.setContent(answer.getTitle());
		ask.setinputtime(answer.getInputtime());
		ask.setsuperlist(answer.getSuperlist());
		vh.question_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showTweetDetail(mContext, ask);
			}
		});
		
		final Comment comment = new Comment();
		
		//取该回答的全部记录
		if (answer.getAid()==0){
			comment.setAid(answer.getAid());
			comment.setId(answer.getId());
		}else{
			comment.setAid(0);
			comment.setId(answer.getAid());
		}
		comment.setauid(answer.getAuid());
		comment.setqid(answer.getQid());
		comment.setnickname(commentTitle);
		vh.comment_rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showReplyCommunicat(mContext,ask,comment);
			}
		});
		
		//底部最新回答
		final int newreply=answer.getNewreply();
		if(newreply<=0){
			vh.newreplyContent.setVisibility(View.GONE);
		}else{
			//changed by wuyue no need to show
			vh.newreplyContent.setVisibility(View.GONE);
			//vh.newreplyContent.setVisibility(View.VISIBLE);
			vh.newreply.setText(String.format(newReplyStr, newreply));
			vh.newreplyTime.setText(answer.getNewtime());
		}
		
		return convertView;
	}
	
	private void updatePhoto(String imagePath, ImageView imageView) {
		imagePath = ApiHttpClient.getImageApiUrl(imagePath);
		ImageLoader.getInstance().displayImage(imagePath, imageView);
	}

	static class ViewHolder {
		private RelativeLayout question_rl,comment_rl,newreplyContent;
		private View comment_spliter;
		private TextView question_title_tv;
		private TextView question_time_tv;
		private TextView question_category_tv;
		private TextView comment_content_tv;
		private TextView newreply,pic_icon;
		private TextView newreplyTime,comment_status_iv;
		private ImageView iv_pic;

		public ViewHolder(View view) {
			question_rl = (RelativeLayout) view.findViewById(R.id.question_rl);
			comment_rl = (RelativeLayout) view.findViewById(R.id.comment_rl);
			newreplyContent = (RelativeLayout) view.findViewById(R.id.newreplyContent);
			comment_spliter = view.findViewById(R.id.comment_spliter);
			comment_status_iv=(TextView)view.findViewById(R.id.comment_status_iv);
			question_title_tv = (TextView) view.findViewById(R.id.question_title_tv);
			question_time_tv = (TextView) view.findViewById(R.id.question_time_tv);
			question_category_tv = (TextView) view.findViewById(R.id.question_category_tv);
			comment_content_tv = (TextView) view.findViewById(R.id.comment_content_tv);
			newreply = (TextView) view.findViewById(R.id.newreply);
			newreplyTime = (TextView) view.findViewById(R.id.newreplyTime);
			pic_icon  = (TextView) view.findViewById(R.id.pic_icon);
			
		}
	}
}
