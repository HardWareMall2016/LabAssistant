package net.oschina.app.v2.activity.comment.adapter;

import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.comment.model.CommentModel;
import net.oschina.app.v2.activity.comment.model.CommentReply;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.utils.DateUtil;
import net.oschina.app.v2.utils.UIHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

public class CommentReplyAdapter extends BaseAdapter {
	private static final int ITEM_SIZE = 3;
	private static final int LEFT_TYPE = 1;
	private static final int RIGHT_TYPE = 2;

	private Context context;
	private LayoutInflater mInflater;
	private CommentModel commentModel;

	public CommentReplyAdapter(Context context) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}

	public void setCommentModel(CommentModel data) {
		this.commentModel = data;
	}

	@Override
	public int getCount() {
		if (commentModel != null) {
			List<CommentReply> data = commentModel.getDataList();
			return data.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		int uid=AppContext.instance().getLoginUid();
		CommentReply c = commentModel.getDataList().get(position);
		if (c != null && c.getAuid()==uid) {
			return RIGHT_TYPE;
		}
		return LEFT_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return ITEM_SIZE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int viewType = getItemViewType(position);

		if (convertView == null) {
			if (viewType == LEFT_TYPE) {
				convertView = mInflater.inflate(R.layout.comment_reply_left_list_item, parent, false);
			} else {
				convertView = mInflater.inflate(R.layout.comment_reply_right_list_item, parent, false);
			}

			viewHolder = new ViewHolder();
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.chat_content);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.chat_listitem_time);
			viewHolder.userPhoto = (ImageView) convertView
					.findViewById(R.id.chat_userPhoto);
			convertView.setTag(viewHolder);
			viewHolder.userPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					CommentReply c = (CommentReply) v.getTag();
					UIHelper.showUserCenter(context, c.getAuid(), c.getNickname());
				}
			});
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/*if (viewType == LEFT_TYPE) {
			viewHolder.content.setText(commentModel.getQuestion());
			//viewHolder.time.setText(DateUtil.getFormatTime(commentModel));
			String imagePath = ApiHttpClient.getImageApiUrl(commentModel.getQhead());
			ImageLoader.getInstance().displayImage(imagePath, viewHolder.userPhoto);
		} else {*/
			CommentReply itemModel = commentModel.getDataList().get(position);
			viewHolder.content.setText(itemModel.getContent());
			viewHolder.time.setText(DateUtil.getFormatTime(itemModel.getIntputtime()));
			String imagePath = ApiHttpClient.getImageApiUrl(itemModel.getHead());
			ImageLoader.getInstance().displayImage(imagePath, viewHolder.userPhoto);
			viewHolder.userPhoto.setTag(itemModel);
		//}

		return convertView;
	}

	static class ViewHolder {
		TextView content, time;
		ImageView userPhoto;
	}
}
