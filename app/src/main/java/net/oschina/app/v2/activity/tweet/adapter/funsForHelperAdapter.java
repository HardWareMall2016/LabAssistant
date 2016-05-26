package net.oschina.app.v2.activity.tweet.adapter;

import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.ui.text.MyLinkMovementMethod;
import net.oschina.app.v2.ui.text.TweetTextView;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.LabelUtils;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.shiyanzhushou.app.R;

public class funsForHelperAdapter extends ListBaseAdapter {
	private DisplayImageOptions options;
	Context context;
	
	private boolean isLocal=true;

	public funsForHelperAdapter(Context context) {
		this.context = context;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).postProcessor(new BitmapProcessor() {
					@Override
					public Bitmap process(Bitmap arg0) {
						return arg0;
					}
				}).build();
	}
	
	public funsForHelperAdapter(Context context,boolean isLocal) {
		this.context = context;
		options = BitmapLoaderUtil.loadDisplayImageOptions();
		this.isLocal=isLocal;
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

		if(item.getFrom()==2){
			vh.recommendQuetion.setVisibility(View.VISIBLE);
		}else{
			vh.recommendQuetion.setVisibility(View.GONE);
		}

		int answernum = item.getanum();
		vh.name.setText(item.getnickname());// 用户昵称
		vh.rank.setText("LV"+item.getrank());
		vh.tv_company.setText(item.getCompany());
		vh.title.setMovementMethod(MyLinkMovementMethod.a());
		vh.title.setFocusable(false);
		vh.title.setDispatchToParent(true);
		vh.title.setLongClickable(false);
		vh.title.setText(StringUtils.getShowSingleLineStr(item.getContent()));
		vh.time.setText(item.getinputtime());// 时间
		if(!StringUtils.isEmpty(item.getImage())&&!"null".equals(item.getImage())){
			vh.picIcon.setVisibility(View.VISIBLE);
		}else{
			vh.picIcon.setVisibility(View.GONE);
		}
		/*if(isLocal){
			if (item.getIsanswer()==1) {
				vh.iv_answered.setBackgroundResource(R.drawable.isanswered);
			} else {
				vh.iv_answered.setBackgroundResource(R.drawable.unresolved);
			}
		}else{
			if (answernum > 0) {
				vh.iv_answered.setBackgroundResource(R.drawable.isanswered);
			} else {
				vh.iv_answered.setBackgroundResource(R.drawable.unresolved);
			}
		}*/
		if (item.getIsanswer()==1) {
			vh.iv_answered.setBackgroundResource(R.drawable.isanswered);
		} else {
			vh.iv_answered.setBackgroundResource(R.drawable.unresolved);
		}
		
		
		
		String label = item.getLabel();
		if (TextUtils.isEmpty(label)) {
			vh.from.setVisibility(View.GONE);// 标签
			vh.fromContent.setVisibility(View.GONE);
		} else {
			/*vh.from.setText(Html.fromHtml("标签:<font color=#2FBDE7>" + label
					+ "</font>"));
			vh.from.setVisibility(View.VISIBLE);*/
			vh.fromContent.setText(label);
			int labelBackgroundId=LabelUtils.getBgResIdByLabel(label);
			vh.fromContent.setBackgroundResource(labelBackgroundId);
			vh.from.setVisibility(View.VISIBLE);
			vh.fromContent.setVisibility(View.VISIBLE);
		}

		String supper = item.getsuperlist();
		if (!TextUtils.isEmpty(supper) && !"null".equals(supper)) {
			vh.superMan.setText("邀请" + supper + "进行回答");
			vh.superMan.setVisibility(View.VISIBLE);
		} else {
			vh.superMan.setVisibility(View.GONE);
		}
		// 回答数量
		vh.commentCount.setText(String.format(vh.commentNum, item.getanum()));

		IvSignUtils.displayIvSignByType(item.getType(), vh.iv_sign,vh.avstarBg);
		/*if (item.getType() > 1) {
			vh.iv_sign.setVisibility(View.VISIBLE);
		} else {
			vh.iv_sign.setVisibility(View.GONE);
		}*/
		
		if (item.getissolveed() == 1) {
			vh.iv_solved.getLayoutParams().width = (int)TDevice.dpToPixel((float)36);
			vh.iv_solved.setVisibility(View.VISIBLE);
		} else {
			vh.iv_solved.getLayoutParams().width = 1;
			vh.iv_solved.setVisibility(View.INVISIBLE);
		}
		
		if (item.getreward() == 0) {
			vh.reward_layout.setVisibility(View.GONE);
		} else {
			vh.reward_layout.setVisibility(View.VISIBLE);
			vh.reward.setText(String.valueOf(item.getreward())+"分");
		}

		ImageLoader.getInstance().displayImage(
				ApiHttpClient.getImageApiUrl(item.gethead()), vh.avatar);

		vh.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UIHelper.showUserCenter(parent.getContext(), item.getUid(),
						item.getnickname());
			}
		});

		return convertView;
	}

	static class ViewHolder {
		public RelativeLayout reward_layout;
		public TextView name, from,fromContent, time, commentCount, rank, superMan,reward,iv_solved,tv_company;
		public TweetTextView title;
		public ImageView picIcon,pic, iv_answered, iv_sign;
		public ImageView avatar,avstarBg;
		public ImageView recommendQuetion;
		public String commentNum;

		public ViewHolder(View view) {
			reward_layout = (RelativeLayout) view.findViewById(R.id.reward_layout);
			name = (TextView) view.findViewById(R.id.tv_name);// 用户名
			rank = (TextView) view.findViewById(R.id.tv_rank);
			title = (TweetTextView) view.findViewById(R.id.tv_title);
			from = (TextView) view.findViewById(R.id.tv_from); // 标签
			fromContent= (TextView) view.findViewById(R.id.tv_from_content); // 标签内容
			time = (TextView) view.findViewById(R.id.tv_time);
			commentCount = (TextView) view.findViewById(R.id.tv_comment_count);
			avatar = (ImageView) view.findViewById(R.id.iv_avatar);
			pic = (ImageView) view.findViewById(R.id.iv_pic);
			picIcon = (ImageView) view.findViewById(R.id.pic_icon3);
			iv_answered = (ImageView) view.findViewById(R.id.iv_answered);
			iv_solved = (TextView) view.findViewById(R.id.iv_solved);// 是否回答
			avstarBg=(ImageView)view.findViewById(R.id.iv_avastarBg);
			superMan = (TextView) view.findViewById(R.id.tv_super);
			commentNum = view.getResources().getString(R.string.comment_count);
			iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
			reward = (TextView) view.findViewById(R.id.tv_reward);
			tv_company= (TextView) view.findViewById(R.id.tv_company);
			recommendQuetion= (ImageView) view.findViewById(R.id.recommend_quetion);
		}
	}
}
