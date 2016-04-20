package net.oschina.app.v2.activity.comment.adapter;

import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.activity.tweet.view.MoreCommentView;
import net.oschina.app.v2.activity.tweet.view.TweetReviewDialog;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentItem;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.ui.AvatarView;
import net.oschina.app.v2.ui.text.MyLinkMovementMethod;
import net.oschina.app.v2.ui.text.TweetTextView;
import net.oschina.app.v2.utils.DateUtil;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class CommentAdapter extends ListBaseAdapter {

	private boolean showSplit;
	private Context context;
	
	private Comment commentModel;
	private OnClickListener onClickListener;
	private OnClickListener onRefreshListener;
	private SparseBooleanArray mData=new SparseBooleanArray();
	
	public interface OnOperationListener {
		void onMoreClick(Comment comment);
	}

	private OnOperationListener mListener;

	public CommentAdapter(OnOperationListener lis) {
		mListener = lis;
	}

	public CommentAdapter(OnOperationListener lis, boolean showSplit) {
		this.showSplit = showSplit;
		mListener = lis;
	}

	public CommentAdapter(OnOperationListener lis, boolean showSplit,
			Context context) {
		this.showSplit = showSplit;
		mListener = lis;
		this.context = context;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mData.clear();
	}

	@SuppressLint({ "InflateParams", "CutPasteId" })
	@Override
	protected View getRealView(final int position, View convertView,
			final ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.v2_list_cell_comment, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final Comment item = (Comment) _data.get(position);
		vh.name.setText(item.getnickname());
		vh.rank.setText(" Lv" + item.getRank());
		vh.title.setMovementMethod(MyLinkMovementMethod.a());
		vh.title.setFocusable(false);
		vh.title.setDispatchToParent(true);
		vh.title.setLongClickable(false);
		// Spanned span = Html.fromHtml(item.getBody());
		vh.title.setText(item.getcontent());
		// MyURLSpan.parseLinkText(vh.title, span);
		vh.time.setText(DateUtil.getFormatTime(item.getinputtime()));

	/*	vh.tv_zhichi.setTag(item.getId());
		vh.tv_zhichi.setText(String.format(vh.zhichiNum, item.getsnum()));
		vh.tv_zhichi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onMore(item);
			}
		});*/
		
		
		/*vh.tv_zhuiwen.setText(String.format(vh.zhuiwenNum, item.getanum()));*/
		IvSignUtils.displayIvSignByType(item.gettype(), vh.iv_sign,vh.avstarBg);
		vh.commentCount.setText(context.getString(R.string.comment_count, item.getanum() + ""));

		vh.avatar.setUserInfo(item.getauid(), item.getnickname());
		vh.avatar.setAvatarUrl(ApiHttpClient.getImageApiUrl(item.gethead()));
		if (item.getisadopt() == 1) {
			vh.sign_huida.setVisibility(View.VISIBLE);
		} else {
			vh.sign_huida.setVisibility(View.GONE);
		}
		
		final ViewHolder holder = vh;
		holder.moreView.setVisibility(mData.get(position) ? View.VISIBLE : View.GONE);
		/*vh.tv_zhuiwen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mData.get(position)) {
					mData.put(position, true);
					showMoreView(holder, item);
					if (context instanceof TweetDetailActivity) {
						((TweetDetailActivity) context).showZhuiwen();
					}
				} else {
					mData.put(position, false);
					holder.moreView.setVisibility(View.GONE);
					if (context instanceof TweetDetailActivity) {
						((TweetDetailActivity) context).reset();
					}
				}
			}
		});*/
		
		vh.textMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				holder.commentView.updateView(item, item.getItemList(), false);
			}
		});
		
		if (item.getauid()==AppContext.instance().getLoginUid()) {
			convertView.findViewById(R.id.relativeLayout1).setSelected(true);
		} else {
			convertView.findViewById(R.id.relativeLayout1).setSelected(false);
		}
		
		return convertView;
	}

	public void setZhuiwenModel(Comment comment) {
		this.commentModel=comment;
	}
	
	public Comment getZhuiwenModel() {
		return commentModel;
	}
	
	private void sendRequestCommentData(int aid) {
		User user = AppContext.instance().getLoginInfo();
		if(user==null){
			Toast.makeText(context, "您还没有登录，快去登录吧~", Toast.LENGTH_SHORT).show();
			return;
		}
		NewsApi.setCommentZhiChi(user.getId(), aid, mAddZhiChiHandler);
	}

	private JsonHttpResponseHandler mAddZhiChiHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				if (response.getInt("code") == 88) {
					AppContext.showToast("支持成功");
					onRefreshListener.onClick(null);
				} else {
					AppContext.showToast(response.optString("desc"));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	protected void onMore(final Comment item) {
		if (!AppContext.instance().isLogin()) {
			UIHelper.showLoginView(context);
			return;
		}
/*
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("支持TA");
		builder.setPositiveButton("支持", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				sendRequestCommentData(item.getId());
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("查看支持者",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (item.getsnum() > 0) {
							TweetReviewDialog tweetReview = new TweetReviewDialog(
									context);
							tweetReview.createReviceUserDialog(item.getId());
							dialog.dismiss();
						} else {
							AppContext.showToast("还没有支持的用户");
						}
					}
				});
		builder.create().show();*/
		final AlertDialog dialog=new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		//设置监听
		Button zhichi=(Button) window.findViewById(R.id.ib_zhichi);
		//支持
		zhichi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendRequestCommentData(item.getId());
				dialog.dismiss();
			}
		});
		//查看支持者
		Button chakanzhichi=(Button) window.findViewById(R.id.ib_chakanzhichi);
		chakanzhichi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (item.getsnum() > 0) {
					TweetReviewDialog tweetReview = new TweetReviewDialog(
							context);
					tweetReview.createReviceUserDialog(item.getId());
					dialog.dismiss();
				} else {
					AppContext.showToast("还没有支持的用户");
				}
			}
		});
	}
	
	public void setOnClickListener(OnClickListener onListener) {
		this.onClickListener=onListener;
	}
	
	public void setOnRefreshListener(OnClickListener onListener) {
		this.onRefreshListener=onListener;
	}
	
	private void showMoreView(ViewHolder holder, Comment comment) {
		setZhuiwenModel(comment);
		List<CommentItem> dataList=comment.getItemList();
		if (dataList==null || dataList.isEmpty()) {
			holder.moreView.setVisibility(View.GONE);
			return;
		} 
		holder.moreView.setVisibility(View.VISIBLE);
		holder.textMore.setVisibility(View.GONE);
		holder.commentView.setOnClickListener(onClickListener);
		holder.commentView.updateView(comment, dataList, true);
		holder.textMore.setVisibility(dataList.size() > 3 ? View.VISIBLE : View.GONE);
	}
	
	static class ViewHolder {
		public TextView name, time, commentCount, rank;
		public TweetTextView title;
		public AvatarView avatar;
		public String commentNum, zhichiNum, zhuiwenNum;
		public ImageView sign_huida,iv_sign,avstarBg;
/*		public Button tv_zhichi, tv_zhuiwen;*/
		public View moreView;
		public MoreCommentView commentView;
		public TextView textMore;
		
		public ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.tv_name);
			rank = (TextView) view.findViewById(R.id.tv_rank);
			title = (TweetTextView) view.findViewById(R.id.tv_title);
			time = (TextView) view.findViewById(R.id.tv_time);
			commentCount = (TextView) view.findViewById(R.id.tv_comment_count);
			avatar = (AvatarView) view.findViewById(R.id.iv_avatar);
			commentNum = view.getResources().getString(R.string.comment_count);
			zhichiNum = view.getResources().getString(R.string.lb_zhichi);
			zhuiwenNum = view.getResources().getString(R.string.lb_zhuiwen);
			/*tv_zhichi = (Button) view.findViewById(R.id.tv_zhichi);*/
			/*tv_zhuiwen = (Button) view.findViewById(R.id.tv_zhuiwen);*/
			sign_huida = (ImageView) view.findViewById(R.id.sign_huida);
			moreView=view.findViewById(R.id.item_collspe_view);
			commentView=(MoreCommentView) view.findViewById(R.id.commentView);
			textMore=(TextView) view.findViewById(R.id.item_more);
			
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
			iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
		}
	}
}
