package net.oschina.app.v2.activity.tweet.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import net.oschina.app.v2.activity.comment.model.CommentReply;
import net.oschina.app.v2.activity.comment.model.CommentReplyList;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentItem;
import net.oschina.app.v2.utils.StringUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class MoreCommentView extends LinearLayout {

	private Context mContext;

	private OnClickListener mOnClickListener;

	private CommentReplyList mList;
	
	private int nameColor;

	public MoreCommentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public MoreCommentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	public MoreCommentView(Context context) {
		this(context, null);
		mContext = context;
	}
	
	private int count;
	
	public interface OnLoadMoreCompleteListener{
		void onloadMoreComplete(int count);
	}
	
	public void updateView(Comment comment, List<CommentItem> dataList,
			boolean showMore) {
		if(nameColor==0){
			nameColor=getResources().getColor(R.color.reply_name);
		}
		if (dataList == null || dataList.size() == 0) {
			setVisibility(View.GONE);
			return;
		}
		if (mList == null) {
			removeAllViews();
			setVisibility(View.VISIBLE);
			NewsApi.getCommentReplyList(comment.getId(), 0, 3,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							String responseStr = response.toString();
							byte[] responseBytes = null;
							try {
								responseBytes = responseStr.getBytes("UTF8");
								String result = inStream2String(new ByteArrayInputStream(
										responseBytes));
								JSONObject json = new JSONObject(result);
								mList = CommentReplyList.parseAnswerAfter(json
										.toString());
								removeAllViews();
								for (int i = 0; i < mList.getCommentlist().size(); i++) {
									if (i > 2) {
										break;
									}
									CommentReply commentReply = mList
											.getCommentlist().get(i);
									View view = createView(commentReply);
									addView(view, i);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

			int index = 0;
			for (CommentItem item : dataList) {
				item.setQid(comment.getqid());
				item.setAid(comment.getId());
				if (showMore && index == 3) {
					break;
				}
				View view = createView(item);
				addView(view);

				if (showMore && index == 2) {

				} else {
					// TextView separator = new TextView(mContext);
					// separator.setLayoutParams(new
					// LayoutParams(LayoutParams.MATCH_PARENT,
					// DeviceUtils.dip2px(mContext, 1)));
					// separator.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
					// addView(separator);
				}
				index++;
			}
		}
	}

	public void updateView(final Comment comment, List<CommentItem> dataList,
			final boolean showMore,final OnLoadMoreCompleteListener onLoadMoreCompleteListener) {
		if(nameColor==0){
			nameColor=getResources().getColor(R.color.reply_name);
		}
		/*if (dataList == null || dataList.size() == 0) {
			setVisibility(View.GONE);
			return;
		}*/
	//	if (mList == null) {
		/*	removeAllViews();
			setVisibility(View.VISIBLE);*/
			
			NewsApi.getCommentReplyList(comment.getId(), 0, 3,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {

							String responseStr = response.toString();
							byte[] responseBytes = null;
							try {
								responseBytes = responseStr.getBytes("UTF8");
								String result = inStream2String(new ByteArrayInputStream(
										responseBytes));
								JSONObject json = new JSONObject(result);
								mList = CommentReplyList.parseReplyAnswerAfter(json
										.toString());
								JSONObject dataObj=new JSONObject(json.getString("data"));
								count=dataObj.optInt("count");
								comment.setCount(count);
								onLoadMoreCompleteListener.onloadMoreComplete(count);
								removeAllViews();
								setVisibility(View.VISIBLE);
								for (int i = 0; i < mList.getCommentlist()
										.size(); i++) {
									if (i > 2) {
										break;
									}
									CommentReply commentReply = mList
											.getCommentlist().get(i);
									View view = createView(commentReply);
									addView(view, i);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		//}
	}

	private View createView(CommentItem item) {
		TextView convertView = (TextView) View.inflate(getContext(),
				R.layout.tweet_comment_list_item, null);
		convertView.setTag(item);
		// convertView.setText(Html.fromHtml(item.getNickname()+"<font color=#2FBDE7> 追问： </font>"+item.getContent()));
		convertView.setOnClickListener(mOnClickListener);
		convertView.setText("");
		return convertView;
	}

	private View createView(CommentReply commentReply) {
		LinearLayout convertView = (LinearLayout) View.inflate(getContext(),
				R.layout.tweet_answer_more_list_item, null);
		ImageView head = (ImageView) convertView.findViewById(R.id.iv_avatar);
		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(commentReply.getHead()), head);
		TextView textView = (TextView) convertView
				.findViewById(R.id.item_text1);
		
		ImageView mIvPic = (ImageView) convertView.findViewById(R.id.iv_pic);
		
		if (!StringUtils.isEmpty(commentReply.getImage()) ) {
			ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(commentReply.getImage()), mIvPic);
			mIvPic.setVisibility(View.VISIBLE);
		}
		
		
		final String nickName=commentReply.getNickname();
		final String askName=commentReply.getAskname();
		final String text=nickName +" 回复 "+ askName
				+ " : " + commentReply.getContent();
		
	
	//	textView.setText(text);
		
		final int p=(nickName +" 回复 ").length();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);  
		ForegroundColorSpan redSpan = new ForegroundColorSpan(nameColor);  
		ForegroundColorSpan redSpan2 = new ForegroundColorSpan(nameColor);  
		
		builder.setSpan(redSpan, 0,nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setSpan(redSpan2, p, p+askName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		textView.setText(builder);
		
		convertView.setOnClickListener(mOnClickListener);
		return convertView;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.mOnClickListener = listener;
	}

	public String inStream2String(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = -1;
		while ((len = is.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		return new String(baos.toByteArray());
	}
}
