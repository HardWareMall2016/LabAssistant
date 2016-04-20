package net.oschina.app.v2.activity.tweet.view;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.activity.tweet.view.UIGroupView.OnItemClickListener;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.event.ClearFilterConditions;
import net.oschina.app.v2.utils.DeviceUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

public class TweetPopupView implements OnClickListener {

	private TextView mConfirmBtn;
	private UIGroupView mLanMuView;
	private UIGroupView mSubLanMuView;
	private PopupWindow mPopupView;
	private CheckBox ckXuanShang;
	private CheckBox ckUnSolved;
	
	private int isreward, issolveed;
	private List<Integer> params=new ArrayList<Integer>();
	
	private OnFilterClickListener mOnClickListener;

	Mulu selectedMulu;
	public TweetPopupView(Context context) {
		int screenWidth = DeviceUtils.getScreenWidth(context);
//		screenWidth = screenWidth - DeviceUtils.dip2px(context, 50);
		View contentView = View.inflate(context, R.layout.tweet_popup_layout,
				null);

		ckXuanShang=(CheckBox) contentView.findViewById(R.id.ck_xuanshagn_btn);
		ckUnSolved=(CheckBox) contentView.findViewById(R.id.ck_unsolved_btn);
		mConfirmBtn = (TextView) contentView.findViewById(R.id.btn_confirm);
		mLanMuView = (UIGroupView) contentView.findViewById(R.id.tweet_grid);
		mSubLanMuView = (UIGroupView) contentView.findViewById(R.id.tweet_sub_grid);
		mSubLanMuView.setChecked(true);

		ckXuanShang.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isreward = isChecked ? 1 : 0;
			}
		});
		
		ckUnSolved.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				issolveed = isChecked ? 1 : 0;
			}
		});
		
		mConfirmBtn.setOnClickListener(this);
		mLanMuView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public boolean onItemClick(Mulu quanBean) {
				selectedMulu=quanBean;
				params.clear();
				if(quanBean.getId()<0){
					addSubList(null);
				}else{
					sendRequestLanmuChildData(quanBean.getId());
				}
				return true;
			}
		});
		mSubLanMuView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public boolean onItemClick(Mulu quanBean) {
				int id =quanBean.getId();
				boolean isChecked=false;
						
				if (!params.contains(id)) {
					params.add(id);
					isChecked=true;
				}else{
					params.remove((Integer)id);
				}
				return isChecked;
			}
		});

		mPopupView = new PopupWindow(contentView, screenWidth,
				LayoutParams.MATCH_PARENT);
		mPopupView.setFocusable(true);
		mPopupView.setOutsideTouchable(true);
		mPopupView.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
		
		EventBus.getDefault().register(this);
	}
	
	public void unregisterEventBus(){
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(ClearFilterConditions clearFilterConditions){
		ckXuanShang.setChecked(false);
		ckUnSolved.setChecked(false);
		isreward=0;
		issolveed=0;
		selectedMulu=null;
		
		mLanMuView.updateList(new ArrayList<Mulu>());
		mSubLanMuView.updateList(new ArrayList<Mulu>() );
	}
	public void showPopup(View anchor) {
		if (!mPopupView.isShowing()) {
			mLanMuView.updateList(lanItems);
			if(selectedMulu!=null){
				mLanMuView.updateState(lanItems,selectedMulu.getcatname());
			}
			mPopupView.showAsDropDown(anchor);
		} else {
			mPopupView.dismiss();
		}
	}
	
	private List<Mulu> lanItems;

	public void addList(List<Mulu> items) {
		lanItems=items;
		mLanMuView.updateList(items);
		if (items != null && items.size() > 0) {
			sendRequestLanmuChildData(items.get(0).getId());
		}
	}

	public void addSubList(List<Mulu> items) {
		mSubLanMuView.updateList(items);
	}

	@Override
	public void onClick(View v) {
		if (mOnClickListener!=null) {
			StringBuilder sb=new StringBuilder();
			for (Integer item : params) {
				sb.append(item+",");
			}
			if (sb.length() > 0) {
				sb = sb.deleteCharAt(sb.length()-1);
			}
			mOnClickListener.onFilter(isreward, issolveed, sb.toString());
			mPopupView.dismiss();
		}
	}

	private void sendRequestLanmuChildData(int id) {
		NewsApi.getLanmu(id, mLanmuChildHandler);
	}
	
	public void setOnFilterClickListener(OnFilterClickListener mListener) {
		this.mOnClickListener=mListener;
	}

	private JsonHttpResponseHandler mLanmuChildHandler = new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			try {
				MuluList list = MuluList.parse(response.toString());
				addSubList(list.getMululist());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public interface OnFilterClickListener {
		
		void onFilter(int isreward, int issolveed, String catid);
	}
}
