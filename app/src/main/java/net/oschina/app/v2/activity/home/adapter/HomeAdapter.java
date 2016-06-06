package net.oschina.app.v2.activity.home.adapter;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.GlobalConstants;
import net.oschina.app.v2.activity.tweet.view.CircleImageView;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.Daily;
import net.oschina.app.v2.model.Home;
import net.oschina.app.v2.ui.text.MyLinkMovementMethod;
import net.oschina.app.v2.ui.text.TweetTextView;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.LabelUtils;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class HomeAdapter extends ListBaseAdapter {
	private Context context;
	private String packageName;
	private DisplayImageOptions options;

	public HomeAdapter(Context context) {
		this.context = context;
		packageName=context.getPackageName();
		options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.ic_default_avatar);
	}

	private void updatePhoto(String imagePath, ImageView imageView) {
		imagePath = ApiHttpClient.getImageApiUrl(imagePath);
		ImageLoader.getInstance().displayImage(imagePath, imageView);
	}
	
	private void updateHeadView(String imagePath, ImageView imageView) {
		imagePath = ApiHttpClient.getImageApiUrl(imagePath);
		ImageLoader.getInstance().displayImage(imagePath, imageView,options);
	}
	
	private void updatePhoto(int resId,ImageView imageView){
		Uri uri = Uri.parse("android.resource://"+packageName+"/raw/"+resId);
		ImageLoader.getInstance().displayImage(uri.toString(), imageView);
	}

	@Override
	protected View getRealView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView != null && convertView.getTag() != null) {
			vh = (ViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.v2_list_cell_home, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		}

		final Home item = (Home) _data.get(position);

		if (item == null)
			return convertView;

		vh.left_arrow.setVisibility(View.VISIBLE);
		vh.question_comment_rl.setVisibility(View.VISIBLE);
		vh.iv_answered.setVisibility(View.GONE);
		vh.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (item.getUid() == 0) {
					UIHelper.showUserCenter(context, item.getId(),
							item.getNickname());
				} else {
					UIHelper.showUserCenter(context, item.getUid(),
							item.getNickname());
				}
			}
		});
		vh.itemContent.setVisibility(View.VISIBLE);
		vh.comment_status.setVisibility(View.GONE);
		switch (item.getType()) {
		case GlobalConstants.HOME_TYPE_MY_QUESTION:
			
			
		
		case GlobalConstants.HOME_TYPE_SHAREFROMME:// 我的提问
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText("我");
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getContent());
			if (!StringUtils.isEmpty(item.getImage())
					&& !"null".equals(item.getImage())) {
				vh.pic.setVisibility(View.VISIBLE);
				updatePhoto(item.getImage(), vh.pic);
			} else {
				vh.pic.setVisibility(View.GONE);
			}

			if (!StringUtils.isEmpty(item.getSuperlist())
					&& !"null".equals(item.getSuperlist())) {
				vh.tv_byask
						.setText(String.format(
								context.getString(R.string.by_ask),
								item.getSuperlist()));
			} else {
				vh.tv_byask.setText("");
			}

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.VISIBLE);
			vh.commentCount.setText(String.valueOf(item.getAnum()));

			vh.iv_answered.setVisibility(View.GONE);
			break;
		case GlobalConstants.HOME_TYPE_MY_ANSWER:// 我的回答
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.VISIBLE);
			vh.group_comment.setVisibility(View.VISIBLE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText("我");
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getQuestiontitle());
			//vh.pic.setVisibility(View.GONE);
			
			if (!StringUtils.isEmpty(item.getQimage())
					&& !"null".equals(item.getQimage())) {
				vh.pic.setVisibility(View.VISIBLE);
				updatePhoto(item.getQimage(), vh.pic);
			} else {
				vh.pic.setVisibility(View.GONE);
			}
			

			if (!StringUtils.isEmpty(item.getSuperlist())
					&& !"null".equals(item.getSuperlist())) {
				vh.tv_byask
						.setText(String.format(
								context.getString(R.string.by_ask),
								item.getSuperlist()));
			} else {
				vh.tv_byask.setText("");
			}

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			vh.ta_ans_label.setText(Html
					.fromHtml("<font color=#F98E76>我的回答：</font>"
							+ item.getContent()));
			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			if (item.getIsadopt() == 1) {
				vh.comment_status.setVisibility(View.VISIBLE);
				vh.comment_status.setText("被采纳");
			} else {
				vh.comment_status.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.GONE);

			vh.iv_answered.setVisibility(View.GONE);
			break;
		case GlobalConstants.HOME_TYPE_BE_HELP:// 我的粉丝求助
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText(item.getNickname());
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getContent());
			vh.pic.setVisibility(View.GONE);

			if (!StringUtils.isEmpty(item.getSuperlist())
					&& !"null".equals(item.getSuperlist())) {
				vh.tv_byask
						.setText(String.format(
								context.getString(R.string.by_ask),
								item.getSuperlist()));
			} else {
				vh.tv_byask.setText("");
			}

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.VISIBLE);
			vh.commentCount.setText(String.valueOf(item.getAnum()));

			vh.iv_answered.setVisibility(View.VISIBLE);
			if (item.getIsanswer() == 1) {  //我回答过
				vh.iv_answered.setBackgroundResource(R.drawable.isanswered);
			} else {
				vh.iv_answered.setBackgroundResource(R.drawable.unresolved);
			}

			break;
		case GlobalConstants.HOME_TYPE_ANSWER_CHOICE:// 我的答案被采纳
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.VISIBLE);
			vh.group_comment.setVisibility(View.VISIBLE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText("我");
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getTitle());
			vh.pic.setVisibility(View.GONE);

			vh.tv_byask.setText("");

			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				/*vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));*/
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}
			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			vh.ta_ans_label.setText(Html
					.fromHtml("<font color=#FBB4A7>我的回答：</font>"
							+ item.getContent()));
			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			if (item.getIsadopt() == 1) {
				vh.comment_status.setVisibility(View.VISIBLE);
				vh.comment_status.setText("被采纳");
			} else {
				vh.comment_status.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.GONE);

			vh.iv_answered.setVisibility(View.GONE);
			break;
		case GlobalConstants.HOME_TYPE_ASKME://他的追问
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.VISIBLE);
			vh.group_comment.setVisibility(View.VISIBLE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText(item.getNickname());
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getQuestiontitle());
			vh.pic.setVisibility(View.GONE);

			vh.tv_byask.setText("");

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.GONE);
			
			
			if(item.getType()==GlobalConstants.HOME_TYPE_ASKME){
				vh.ta_ans_label.setText(Html
						.fromHtml("<font color=#FBB4A7>TA的追问：</font>"
								+ item.getContent()));
			}else{
				vh.ta_ans_label.setText(Html
						.fromHtml("<font color=#FBB4A7>我的追问：</font>"
								+ item.getContent()));
			}
			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			if (item.getIsadopt() == 1) {
				vh.comment_status.setVisibility(View.VISIBLE);
				vh.comment_status.setText("被采纳");
			} else {
				vh.comment_status.setVisibility(View.GONE);
			}

			vh.iv_answered.setVisibility(View.GONE);
			break;
			
		case GlobalConstants.HOME_TYPE_MY_ANSWERAFTER:// 我的追问
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.VISIBLE);
			vh.group_comment.setVisibility(View.VISIBLE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			//vh.name.setText(item.getNickname());
			vh.name.setText("我");
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getQuestiontitle());
			vh.pic.setVisibility(View.GONE);

			vh.tv_byask.setText("");

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.GONE);
			
			
			if(item.getType()==GlobalConstants.HOME_TYPE_ASKME){
				vh.ta_ans_label.setText(Html
						.fromHtml("<font color=#FBB4A7>TA的追问：</font>"
								+ item.getContent()));
			}else{
				vh.ta_ans_label.setText(Html
						.fromHtml("<font color=#FBB4A7>我的追问：</font>"
								+ item.getContent()));
			}
			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			if (item.getIsadopt() == 1) {
				vh.comment_status.setVisibility(View.VISIBLE);
				vh.comment_status.setText("被采纳");
			} else {
				vh.comment_status.setVisibility(View.GONE);
			}

			vh.iv_answered.setVisibility(View.GONE);
			break;
		case GlobalConstants.HOME_TYPE_MY_ATTENER_QUESTION:// 我关注的人提问
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText(item.getNickname());
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getContent());
			if (!StringUtils.isEmpty(item.getQimage())
					&& !"null".equals(item.getQimage())) {
				vh.pic.setVisibility(View.VISIBLE);
				updatePhoto(item.getQimage(), vh.pic);
			} else {
				vh.pic.setVisibility(View.GONE);
			}

			if (!StringUtils.isEmpty(item.getSuperlist())
					&& !"null".equals(item.getSuperlist())) {
				vh.tv_byask
						.setText(String.format(
								context.getString(R.string.by_ask),
								item.getSuperlist()));
			} else {
				vh.tv_byask.setText("");
			}

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.VISIBLE);
			vh.commentCount.setText(String.valueOf(item.getAnum()));

			vh.iv_answered.setVisibility(View.GONE);
			break;
		case GlobalConstants.HOME_TYPE_MY_ATTENER_ANSWER:// 我关注的人回答
			vh.group_question.setVisibility(View.VISIBLE);
			vh.question_comment_spliter.setVisibility(View.VISIBLE);
			vh.group_comment.setVisibility(View.VISIBLE);
			vh.group_push.setVisibility(View.GONE);

			updateHeadView(item.getHead(), vh.avatar);
			vh.name.setText(item.getNickname());
			vh.mark.setText(item.getBewrite());
			vh.time.setText(item.getInputtime());

			vh.title.setText(item.getTitle());
			if (!StringUtils.isEmpty(item.getQimage())
					&& !"null".equals(item.getQimage())) {
				vh.pic.setVisibility(View.VISIBLE);
				updatePhoto(item.getQimage(), vh.pic);
			} else {
				vh.pic.setVisibility(View.GONE);
			}

			vh.tv_byask.setText("");

			/*if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>"
						+ item.getLable() + "</font>"));
			} else {
				vh.from.setText("");
			}*/
			if (!StringUtils.isEmpty(item.getLable())
					&& !"null".equals(item.getLable())) {
				vh.fromContent.setText(LabelUtils.parseLable(item.getLable()));
				int labelBackgroundId= LabelUtils.getBgResIdByLabel(item.getLable());
				vh.fromContent.setBackgroundResource(labelBackgroundId);
				vh.from.setVisibility(View.VISIBLE);
				vh.fromContent.setVisibility(View.VISIBLE);
			} else {
				vh.from.setText("");
				vh.from.setVisibility(View.GONE);// 标签
				vh.fromContent.setVisibility(View.GONE);
			}

			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			vh.ta_ans_label.setText(Html
					.fromHtml("<font color=#FBB4A7>TA的回答：</font>"
							+ item.getContent()));
			if (StringUtils.isEmpty(item.getContent())) {
				vh.comment_pic.setVisibility(View.VISIBLE);
			} else {
				vh.comment_pic.setVisibility(View.GONE);
			}

			if (item.getIsadopt() == 1) {
				vh.comment_status.setVisibility(View.VISIBLE);
				vh.comment_status.setText("被采纳");
			} else {
				vh.comment_status.setVisibility(View.GONE);
			}

			vh.commentCount.setVisibility(View.GONE);

			vh.iv_answered.setVisibility(View.GONE);
			break;
		case GlobalConstants.HOME_TYPE_BE_ATTENTION:// XX关注了我
			vh.itemContent.setVisibility(View.GONE);
			vh.group_question.setVisibility(View.GONE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.GONE);
			vh.left_arrow.setVisibility(View.GONE);

			vh.name.setText(item.getNickname());
			vh.mark.setText("关注了我");
			vh.time.setText(item.getInputtime());

			updateHeadView(item.getHead(), vh.avatar);

			break;
		case GlobalConstants.HOME_TYPE_MY_ATTENTION:// 我关注了XX
			vh.itemContent.setVisibility(View.GONE);
			vh.group_question.setVisibility(View.GONE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.GONE);
			vh.left_arrow.setVisibility(View.GONE);

			vh.name.setText("我");
			vh.mark.setText("关注了" + item.getNickname());
			vh.time.setText(item.getInputtime());

			updateHeadView(item.getHead(), vh.avatar);

			break;
		case GlobalConstants.HOME_TYPE_LESS_AND_MORE:// //XXX受我邀请注册了实验助手并关注了我
			vh.group_question.setVisibility(View.GONE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.VISIBLE);

			vh.name.setText(item.getBewrite());
			vh.mark.setText(item.getTitle());
			vh.time.setText(item.getInputtime());

			updateHeadView(item.getThumb(), vh.push_iv);
			break;
		case GlobalConstants.HOME_TYPE_PUSH:// 活动中心培训信息的主动推送内容
			vh.question_comment_rl.setVisibility(View.GONE);
			vh.group_question.setVisibility(View.GONE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.VISIBLE);

			vh.name.setText(item.getBewrite());
			vh.mark.setText("");
			vh.time.setText(item.getInputtime());

			updatePhoto(item.getThumb(), vh.push_iv);
			setAvastar(item.getCatid(),vh.avatar);
			/*if(item.getBewrite().contains("助手日报")){
				vh.avatar.setImageResource(R.drawable.find_1);
			}else if(item.getBewrite().contains("活动中心")){
				vh.avatar.setImageResource(R.drawable.find_4);
			}*/
			vh.push_tv.setText(item.getTitle());
			
			final Daily daily = new Daily();
			daily.setId(item.getFindid());
			vh.avatar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIHelper.showDailyDetailRedirect(context, daily);
				}
			});
			vh.group_push.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIHelper.showDailyDetailRedirect(context, daily);
				}
			});
			break;
		case GlobalConstants.HOME_TYPE_SHARETOCIRCLE://分享到实验去圈
			vh.question_comment_rl.setVisibility(View.GONE);
			vh.group_question.setVisibility(View.GONE);
			vh.question_comment_spliter.setVisibility(View.GONE);
			vh.group_comment.setVisibility(View.GONE);
			vh.group_push.setVisibility(View.VISIBLE);

			vh.name.setText(item.getBewrite());
			vh.mark.setText("");
			vh.time.setText(item.getInputtime());

			updatePhoto(item.getThumb(), vh.push_iv);
			
			setAvastar(item.getCatid(),vh.avatar);
			/*if(item.getBewrite().contains("助手日报")){
				vh.avatar.setImageResource(R.drawable.find_1);
			}else if(item.getBewrite().contains("活动中心")){
				vh.avatar.setImageResource(R.drawable.find_4);
			}*/
			vh.push_tv.setText(item.getTitle());
			
			final Daily newDaily = new Daily();
			newDaily.setId(item.getFindid());
			vh.avatar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIHelper.showDailyDetailRedirect(context, newDaily);
				}
			});
			vh.group_push.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIHelper.showDailyDetailRedirect(context, newDaily);
				}
			});
			break;
		default:
			break;
		}

		// 组建Ask对象
		final Ask ask = new Ask();
		if (item.getQid() != 0) {
			ask.setId(item.getQid());
		} else {
			ask.setId(item.getId());
		}
		if (item.getQuid() != 0) {
			ask.setUid(item.getQuid());
		} else {
			ask.setUid(item.getUid());
		}
		ask.setanum(item.getAnum());
		ask.setLabel(item.getLable());
		if (!StringUtils.isEmpty(item.getQuestiontitle())
				&& !"null".equals(item.getQuestiontitle())) {
			ask.setContent(item.getQuestiontitle());
		} else {
			ask.setContent(item.getContent());
		}
		if (!StringUtils.isEmpty(item.getQinputtime())
				&& !"null".equals(item.getQinputtime())) {
			ask.setinputtime(item.getQinputtime());
		} else {
			ask.setinputtime(item.getInputtime());
		}
		ask.setsuperlist(item.getSuperlist());
		if (!StringUtils.isEmpty(item.getQnickname())
				&& !"null".equals(item.getQnickname())) {
			ask.setnickname(item.getQnickname());
		} else {
			ask.setnickname(item.getNickname());
		}
		ask.setImage(item.getImage());
		ask.sethead(item.getQimage());
		if (item.getIsadopt() == 1 || item.getIssolveed() == 1) {
			ask.setissolveed(1);
		} else {
			ask.setissolveed(0);
		}

		vh.group_question.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showTweetDetail(context, ask);
			}
		});

		final Comment comment = new Comment();
		if(item.getType()==GlobalConstants.HOME_TYPE_MY_ATTENER_ANSWER){
			comment.setAid(item.getFindid());
			comment.setId(item.getFindid());
		}else{
			comment.setAid(item.getAid());
			comment.setId(item.getId());
		}
		comment.setAid(item.getAid());
	
		comment.setauid(item.getAuid());
		comment.setqid(item.getQid());
		
		if (!StringUtils.isEmpty(item.getAnickname())
				&& !"null".equals(item.getAnickname())) {
			comment.setnickname(item.getAnickname());
		} else {
			comment.setnickname(item.getNickname());
		}
		
		if(item.getType()==GlobalConstants.HOME_TYPE_MY_ANSWER ){
			if(item.getUid()==AppContext.instance().getLoginUid()){
				comment.setnickname("我");	
			}
		}
		
		if(item.getType()==GlobalConstants.HOME_TYPE_MY_ANSWERAFTER //我的追问、追问我的
				|| item.getType()==GlobalConstants.HOME_TYPE_ASKME ){
			if(item.getUid()==AppContext.instance().getLoginUid()){
				comment.setnickname("我");	
			}else{
				comment.setnickname(item.getAnswernickname());
			}
		}
		
		vh.group_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (item.getType()) {
				case GlobalConstants.HOME_TYPE_MY_ANSWERAFTER:
				case GlobalConstants.HOME_TYPE_ASKME:
					comment.setId(item.getAid());
					UIHelper.showReAnswerCommunicat(context, ask, comment);
					break;
				default:
					//comment.setId(item.getAid());
					UIHelper.showReplyCommunicat(context, ask, comment);
					break;
				}
			}
		});

		return convertView;
	}
	
	public void setAvastar(int catid,ImageView imageView){
		int resId=0;
		switch(catid){
		case 4:		//助手日报
			resId=R.drawable.find_1;
			break;
		case 5:		//实验周刊
			resId=R.drawable.find_2;
			break;
		case 6:		//法规文献
			resId=R.drawable.find_3;
			break;
		case 7:		//培训信息
			resId=R.drawable.find_6;
			break;
		case 8:		//活动中心
			resId=R.drawable.find_4;
			break;
		case 9:		//品牌库
			resId=R.drawable.find_5;
			break;
			default:
				break;
		}
		if(resId>0){
			imageView.setImageResource(resId);
		}
	}

	static class ViewHolder {
		public TextView name, tv_byask, from,fromContent, time, commentCount, ta_ans_label,
				mark, push_tv, comment_status,comment_pic;
		public TweetTextView title;
		public CircleImageView avatar;
		public ImageView pic, push_iv, iv_answered,
				left_arrow;
		public View group_question, group_comment, group_push,question_comment_rl,
				question_comment_spliter,itemContent;
	

		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.tv_name);
			from = (TextView) view.findViewById(R.id.tv_from);
			fromContent= (TextView) view.findViewById(R.id.tv_from_content); // 标签内容
			tv_byask = (TextView) view.findViewById(R.id.tv_byask);
			time = (TextView) view.findViewById(R.id.tv_time);
			commentCount = (TextView) view.findViewById(R.id.tv_comment_count);
			avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
			avatar.setBorderColor(Color.RED);
			avatar.setBorderWidth(1);
			
			pic = (ImageView) view.findViewById(R.id.iv_pic);
			mark = (TextView) view.findViewById(R.id.tv_rank);
			group_question = view.findViewById(R.id.group_question);
			question_comment_spliter = view
					.findViewById(R.id.question_comment_spliter);
			group_comment = view.findViewById(R.id.group_comment);
			ta_ans_label = (TextView) view.findViewById(R.id.ta_ans);
			comment_pic = (TextView) view.findViewById(R.id.comment_pic);
			comment_status = (TextView) view.findViewById(R.id.comment_status);
			left_arrow = (ImageView) view.findViewById(R.id.left_arrow);

			title = (TweetTextView) view.findViewById(R.id.tv_title);
			title.setMovementMethod(MyLinkMovementMethod.a());
			title.setFocusable(false);
			title.setDispatchToParent(true);
			title.setLongClickable(false);

			question_comment_rl = view.findViewById(R.id.question_comment_rl);
			group_push = view.findViewById(R.id.group_push);
			push_iv = (ImageView) view.findViewById(R.id.push_iv);
			push_tv = (TextView) view.findViewById(R.id.push_tv);

			iv_answered = (ImageView) view.findViewById(R.id.iv_answered);
			
			itemContent=view.findViewById(R.id.itemContent);
		}
	}
}
