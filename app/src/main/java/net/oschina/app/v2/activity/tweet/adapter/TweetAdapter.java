package net.oschina.app.v2.activity.tweet.adapter;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.ui.text.MyLinkMovementMethod;
import net.oschina.app.v2.ui.text.TweetTextView;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;
import android.annotation.SuppressLint;
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

public class TweetAdapter extends ListBaseAdapter {

	private String mHighLight;

	private DisplayImageOptions options;
	public TweetAdapter() {
		 options = BitmapLoaderUtil.loadDisplayImageOptions(R.drawable.ic_default_avatar);
		
	}

	public void setHighLight(String highLight) {
		this.mHighLight = highLight;
	}

	@SuppressLint("InflateParams")
	@Override
	protected View getRealView(int position, View convertView,
			final ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = getLayoutInflater(parent.getContext()).inflate(
					R.layout.v2_list_cell_tweet, null);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final Ask item = (Ask) _data.get(position);
		vh.name.setText(item.getnickname());// 用户昵称
		if(item.getrank()==0){
			item.setrank(1);
		}
		vh.rank.setText(" Lv" + item.getrank());
		if(!StringUtils.isEmpty(item.getCompany())&&!"null".equals(item.getCompany())){
			vh.company.setText(item.getCompany());
		}else{
			vh.company.setText("");
		}
		vh.title.setMovementMethod(MyLinkMovementMethod.a());
		vh.title.setFocusable(false);
		vh.title.setDispatchToParent(true);
		vh.title.setLongClickable(false);
		if (!StringUtils.isEmpty(item.getContent())
				&& !"null".equals(item.getContent())) {
			final int isTop=item.getIstop();
			if(isTop>=1){
				String tip="【置顶】";
				if(isTop==2){
					tip="【活动】";
				}else if(isTop==3){
					tip="【公告】";
				}
				vh.question_layout.setBackgroundResource(R.drawable.common_list_item_spec_bg);
				String topContent = "<font color=#FF0000>"+tip+"</font>"+item.getContent();
				if (!StringUtils.isEmpty(mHighLight)) {
					vh.title.setText(Html.fromHtml(StringUtils
							.getShowSingleLineWithHighlight(topContent,
									mHighLight)));
				}else{
					vh.title.setText(Html.fromHtml(topContent));
				}
			}else{
				vh.question_layout.setBackgroundResource(R.drawable.common_list_item_bg);
				if (!StringUtils.isEmpty(mHighLight)) {
					vh.title.setText(Html.fromHtml(StringUtils
							.getShowSingleLineWithHighlight(item.getContent(),
									mHighLight)));
				}else{
					vh.title.setText(StringUtils.getShowSingleLineStr(item
							.getContent()));
				}
			}
		} else {
			vh.title.setText(item.getContent());// 设置问题内容
		}
		vh.time.setText(item.getinputtime());// 时间
		if (!StringUtils.isEmpty(item.getImage())
				&& !"null".equals(item.getImage())) {
			vh.picIcon.setVisibility(View.VISIBLE);
		} else {
			vh.picIcon.setVisibility(View.GONE);
		}

		if (item.getissolveed() == 1) {
			vh.iv_solved.getLayoutParams().width = (int)TDevice.dpToPixel((float)36);
			vh.iv_solved.setVisibility(View.VISIBLE);
		} else {
			vh.iv_solved.getLayoutParams().width = 1;
			vh.iv_solved.setVisibility(View.INVISIBLE);
		}

		String label = item.getLabel();
		if (TextUtils.isEmpty(label)) {
			vh.from.setVisibility(View.GONE);// 标签
		} else {
			// vh.from.setText("标签："+label);//Html.fromHtml("出发地点<font color=#2FBDE7>*</font>")
			vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>" + label
					+ "</font>"));
			vh.from.setVisibility(View.VISIBLE);
		}

		String supper = item.getsuperlist();
		if (!TextUtils.isEmpty(supper) && !"null".equals(supper)) {
			vh.superMan.setText("邀请" + supper + "进行回答");
			vh.superMan.setVisibility(View.VISIBLE);
		} else {
			vh.superMan.setVisibility(View.GONE);
		}

		IvSignUtils.displayIvSignByType(item.getType(), vh.iv_sign,vh.avstarBg);
	/*	if (item.getType() > 1) {
			vh.iv_sign.setVisibility(View.VISIBLE);
		} else {
			vh.iv_sign.setVisibility(View.GONE);
		}*/

		if (item.getreward() == 0) {
			vh.reward_layout.setVisibility(View.GONE);
		} else {
			vh.reward_layout.setVisibility(View.VISIBLE);
			vh.reward.setText(String.valueOf(item.getreward())+"分");
		}

		// 回答数量
		vh.commentCount.setText(String.format(vh.commentNum, item.getanum()));

	/*	if(TextUtils.isEmpty(item.gethead())){
			//ImageLoader.getInstance().displayImage();
			ImageLoader.getInstance().displayImage("", vh.avatar, options);
		}else{
			ImageLoader.getInstance().displayImage(
					ApiHttpClient.getImageApiUrl(item.gethead()), vh.avatar);
		}*/
		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(item.gethead()), vh.avatar,options);

		
		vh.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AppContext.instance().isLogin()) {
					UIHelper.showUserCenter(parent.getContext(), item.getUid(),
							item.getnickname());
				}else{
					AppContext.showToast("您还没有登录");
				}
			}
		});
		
		/*if (!TextUtils.isEmpty(item.getImage())) {
			vh.pic.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(ApiHttpClient.getImageApiUrl(item.getImage()), vh.pic,
					options);
		} else {
			vh.pic.setVisibility(View.GONE);
			vh.pic.setImageBitmap(null);
		}*/

		return convertView;
	}

	static class ViewHolder {
		public RelativeLayout reward_layout,question_layout;
		public TextView name,company, from, time, commentCount, rank, superMan, reward;
		public TweetTextView title;
		public ImageView picIcon, pic, iv_answered, iv_sign;
		public TextView iv_solved;
		public ImageView avatar,avstarBg;
		public String commentNum;

		public ViewHolder(View view) {
			question_layout = (RelativeLayout) view.findViewById(R.id.question_rl);
			reward_layout = (RelativeLayout) view.findViewById(R.id.reward_layout);
			name = (TextView) view.findViewById(R.id.tv_name);// 用户名
			rank = (TextView) view.findViewById(R.id.tv_rank);
			company = (TextView) view.findViewById(R.id.tv_company);
			title = (TweetTextView) view.findViewById(R.id.tv_title);
			from = (TextView) view.findViewById(R.id.tv_from); // 标签
			time = (TextView) view.findViewById(R.id.tv_time);
			commentCount = (TextView) view.findViewById(R.id.tv_comment_count);
			avatar = (ImageView) view.findViewById(R.id.iv_avatar);
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
			iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
			picIcon = (ImageView) view.findViewById(R.id.pic_icon3);
			pic = (ImageView) view.findViewById(R.id.iv_pic);
			iv_answered = (ImageView) view.findViewById(R.id.iv_answered);
			iv_solved = (TextView) view.findViewById(R.id.iv_solved);// 是否解决
			superMan = (TextView) view.findViewById(R.id.tv_super);
			
			reward = (TextView) view.findViewById(R.id.tv_reward);

			commentNum = view.getResources().getString(R.string.comment_count);
		}
	}
}
