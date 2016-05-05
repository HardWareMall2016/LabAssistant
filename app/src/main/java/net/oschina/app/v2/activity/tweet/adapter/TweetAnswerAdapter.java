package net.oschina.app.v2.activity.tweet.adapter;

import java.io.Serializable;
import java.util.ArrayList;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.ImageShowerActivity;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.activity.tweet.fragment.TweetAnswerListFragment;
import net.oschina.app.v2.activity.tweet.view.CircleImageView;
import net.oschina.app.v2.activity.tweet.view.MoreCommentView;
import net.oschina.app.v2.activity.tweet.view.MoreCommentView.OnLoadMoreCompleteListener;
import net.oschina.app.v2.activity.tweet.view.TweetReviewDialog;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.CommentItem;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.event.AdoptSuccEvent;
import net.oschina.app.v2.ui.text.MyLinkMovementMethod;
import net.oschina.app.v2.ui.text.TweetTextView;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

public class TweetAnswerAdapter extends ListBaseAdapter {
	private Context context;
	private Comment currentItem;
	private Ask ask;

	public View currentExpandView;

	public TweetAnswerAdapter(Context context,Ask ask) {
		this.context = context;
		this.ask=ask;
	}
	
	public void setAdoptState(int adopt){
		ask.setissolveed(adopt);
	}

	@SuppressLint("InflateParams")
	@Override
	protected View getRealView(int position, View convertView,
			final ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.v2_list_cell_comment, null);
			vh = new ViewHolder(convertView,position);
			convertView.setTag(vh);
		} else {
			
			vh = (ViewHolder) convertView.getTag();
		}

		final Comment item = (Comment) _data.get(position);

		vh.name.setText(item.getnickname());
		vh.rank.setText(" Lv" + item.getRank());
		vh.company.setText(item.getInfo());
		vh.title.setMovementMethod(MyLinkMovementMethod.a());
		vh.title.setFocusable(false);
		vh.title.setDispatchToParent(true);
		vh.title.setLongClickable(false);
		String attention=item.getAnsupperlist();
		if(!StringUtils.isEmpty(attention)
				&& !"null".equals(attention)){
			attention+=" ";
		}else{
			attention="";
		}
	//	vh.tv_ansupperlist.setText(attention);
		
		if (!StringUtils.isEmpty(item.getcontent())
				&& !"null".equals(item.getcontent())) {
			//回答@的样式
			vh.title.setText(Html.fromHtml("<font color='#53C1E5'>"+ attention + "</font>"
			+item.getcontent()));
			
			vh.pic_icon.setVisibility(View.GONE);
		} else {
			vh.title.setText("【图片】");
			vh.pic_icon.setVisibility(View.GONE);
			/*ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(item.getImage()), vh.pic_icon);
			vh.pic_icon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							ImageShowerActivity.class);
					intent.putExtra("imageUri", item.getImage());
					context.startActivity(intent);
				}
			});
			vh.pic_icon.setVisibility(View.VISIBLE);*/
		}
		
		IvSignUtils.displayIvSignByType(item.gettype(), vh.iv_sign,vh.avstarBg);
		vh.time.setText(item.getinputtime());
		vh.commentCount.setText(String.valueOf(item.getAnswerNum()));
		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(item.gethead()), vh.avatar);
		vh.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppContext.instance().isLogin()) {
					UIHelper.showUserCenter(context, item.getauid(),
							item.getnickname());
				}else{
					AppContext.showToast("您还没有登录");
				}
			}
		});
		vh.supportBt.setText(String.format(vh.zhichiNum, item.getsnum()));
		vh.reAnswerBt.setText(String.format(vh.zhuiwenNum, item.getanum()));
		vh.supportBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog dialog = new AlertDialog.Builder(context)
						.create();
				dialog.show();
				Window window = dialog.getWindow();
				window.setContentView(R.layout.zhichi_dialog);
				// 设置监听
				Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
				// 支持
				zhichi.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentItem = item;
						sendRequestCommentData(item.getId());
						dialog.dismiss();
					}
				});
				// 查看支持者
				Button chakanzhichi = (Button) window
						.findViewById(R.id.ib_chakanzhichi);
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
		});
		final ViewHolder holder = vh;
		vh.reAnswerBt.setOnClickListener(new OnClickListener() {// 展开追问
					@Override
					public void onClick(View v) {
						if (AppContext.instance().isLogin()) {
						if(item.getanum()==0){
							UIHelper.showReAnswerCommunicat(holder.reAnswerBt.getContext(), ask,
									item);
							return;
						}
						
						if (currentExpandView != null) {
							currentExpandView.setVisibility(View.GONE);
						}
						if (currentExpandView == holder.moreView) {
							currentExpandView = null;
							((TweetDetailActivity) context).bottomBack();
							return;
						}
						currentExpandView = holder.moreView;

						holder.moreView.setVisibility(View.VISIBLE);
						holder.textMore.setVisibility(View.GONE);
						// holder.commentView.setOnClickListener(onClickListener);
						final int itemSize=item.getItemList().size();
						holder.commentView.updateView(item, item.getItemList(),
								true,new OnLoadMoreCompleteListener() {
									
									@Override
									public void onloadMoreComplete(int count) {
										holder.textMore.setVisibility(count> 0 ? View.VISIBLE
												: View.GONE);
										holder.textMore.setText("查看全部"+itemSize+"条消息");
									}
								});
						
						// 判断是否是首次追问
						// boolean isLoginUser = false;
						// int uid=AppContext.instance().getLoginUid();
						// for(CommentItem commentItem:item.getItemList()){
						// if(Integer.parseInt(commentItem.getAuid())==uid){
						// isLoginUser = true;
						// break;
						// }
						// }
						// if(isLoginUser){
						((TweetDetailActivity) context).showReTraceAsk(item);
						// }else{
						// ((TweetDetailActivity)context).showTraceAsk(item);
						// }
						}else{
							AppContext.showToast("您还没有登录");
						}
					}
				});
		holder.commentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showReAnswerCommunicat(context, ask, item);
			}
		});
		holder.textMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showReAnswerCommunicat(context, ask, item);
			}
		});
		if (item.getisadopt() == 1) {
			vh.sign_huida.setVisibility(View.VISIBLE);
		} else {
			vh.sign_huida.setVisibility(View.GONE);
		}

		
		
		if (ask.getissolveed() != 1
				&& ask.getUid() == AppContext.instance().getLoginUid()) {// 楼主
			vh.sign_caina.setVisibility(View.VISIBLE);
			vh.sign_caina.setImageResource(R.drawable.caina);
			vh.sign_caina.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					doAdopt(item.getId());
					// int uid=AppContext.instance().getLoginUid();
					// NewsApi.userAttention(uid, item.getId(), new
					// JsonHttpResponseHandler(){
					// @Override
					// public void onSuccess(int statusCode, Header[] headers,
					// JSONObject response) {
					// if (response.optInt("code") == 88) {
					// AppContext.showToast("采纳成功");
					// ((TweetDetailActivity)context).changeAskState();
					// } else {
					// AppContext.showToast("采纳失败");
					// }
					// }
					// });
				}
			});

		} else {
			if(item.getisadopt()==1){
				vh.sign_caina.setImageResource(R.drawable.sign_beicaina);
				vh.sign_caina.setVisibility(View.VISIBLE);
			}else{
				vh.sign_caina.setVisibility(View.GONE);
			}
			vh.sign_caina.setOnClickListener(null);
		}

		if (AppContext.instance().isLogin()) {
			int uid = AppContext.instance().getLoginUid();
			if (uid == item.getauid()&&!hideList.contains(position)) {
				vh.relativeLayout
						.setBackgroundResource(R.drawable.common_list_item_me_bg);
			} else {
				vh.relativeLayout
						.setBackgroundResource(R.drawable.common_list_item_bg);
			}
		}

		return convertView;
	}
	
	private ArrayList<Integer> hideList=new ArrayList<Integer>();

	/**
	 * 采纳
	 */
	protected void doAdopt(final int aid) {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.zhichi_dialog);
		TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
		titleTv.setText("是否采纳");
		// 设置监听
		Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
		zhichi.setText("确定");
		// 支持
		zhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int uid = AppContext.instance().getLoginUid();
				NewsApi.userAttention(uid, aid, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (response.optInt("code") == 88) {
							AppContext.showToast("采纳成功");
							EventBus.getDefault().post(new AdoptSuccEvent(ask.getId()));
						} else {
							AppContext.showToast("采纳失败");
						}
					}
				});
				dialog.dismiss();
			}
		});
		Button chakanzhichi = (Button) window
				.findViewById(R.id.ib_chakanzhichi);
		chakanzhichi.setText("取消");
		chakanzhichi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
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
					// 刷新数据
					currentItem.setsnum(currentItem.getsnum() + 1);
					notifyDataSetChanged();
				} else {
					AppContext.showToast(response.optString("desc"));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
	};

	static class ViewHolder implements Serializable{
		public TextView name, company, time, commentCount, rank;
		public TweetTextView title,tv_ansupperlist;
		public CircleImageView avatar;
		public ImageView pic_icon;
		public String commentNum, zhichiNum, zhuiwenNum;
		public Button supportBt, reAnswerBt;
		public ImageView sign_huida, sign_caina,avstarBg,iv_sign;
		public View moreView;
		public MoreCommentView commentView;
		public TextView textMore;
		public RelativeLayout relativeLayout;

		public ViewHolder(View view,int position) {
			name = (TextView) view.findViewById(R.id.tv_name);
			rank = (TextView) view.findViewById(R.id.tv_rank);
			company = (TextView) view.findViewById(R.id.tv_company);
			title = (TweetTextView) view.findViewById(R.id.tv_title);
			tv_ansupperlist= (TweetTextView) view.findViewById(R.id.tv_ansupperlist);
			time = (TextView) view.findViewById(R.id.tv_time);
			commentCount = (TextView) view.findViewById(R.id.tv_comment_count);
			avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
			avatar.setBorderColor(Color.BLACK);
			avatar.setBorderWidth(1);
			supportBt = (Button) view.findViewById(R.id.bt_zhichi);
			reAnswerBt = (Button) view.findViewById(R.id.bt_zhuiwen);
			zhichiNum = view.getResources().getString(R.string.lb_zhichi);
			zhuiwenNum = view.getResources().getString(R.string.lb_zhuiwen);
			sign_huida = (ImageView) view.findViewById(R.id.sign_huida);
			sign_caina = (ImageView) view.findViewById(R.id.sign_caina);
			moreView = view.findViewById(R.id.item_collspe_view);
			commentView = (MoreCommentView) view.findViewById(R.id.commentView);
			textMore = (TextView) view.findViewById(R.id.item_more);
			pic_icon = (ImageView) view.findViewById(R.id.pic_icon);
			relativeLayout = (RelativeLayout) view
					.findViewById(R.id.relativeLayout1);
			
			iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
		}
	}

}
