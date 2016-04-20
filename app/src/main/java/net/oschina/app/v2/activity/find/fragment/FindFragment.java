package net.oschina.app.v2.activity.find.fragment;

import org.apache.http.Header;
import org.json.JSONObject;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MallActivity;
import net.oschina.app.v2.activity.RankActivity;
import net.oschina.app.v2.activity.active.fragment.ActiveFragment;
import net.oschina.app.v2.activity.user.fragment.LoginFragment;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.model.MessageNum;
import net.oschina.app.v2.model.event.MessageRefreshSingle;
import net.oschina.app.v2.model.event.MessageRefreshTotal;
import net.oschina.app.v2.ui.tooglebutton.ToggleButton;
import net.oschina.app.v2.utils.ActiveNumType;
import net.oschina.app.v2.utils.UIHelper;
import net.oschina.app.v2.utils.ViewFinder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;

import de.greenrobot.event.EventBus;

public class FindFragment extends BaseFragment {

	private static final String FIND_SCREEN = "find_screen";
	private TextView mTvPicPath;
	private String mCachePicPath;
	private ToggleButton mTbLoadImage;
	private TextView mTvCacheSize;
	private TextView mTvVersionName;
	private UpdateResponse mUpdateInfo;
	private boolean mIsCheckingUpdate;
	private View mBtnLogout;
	
	/**
	 * 提示消息
	 */
	private TextView zushouNotice;
	private TextView shiyanNotice;
	private TextView huodongNotice;
	private TextView peixunNotice;
	private TextView paihangNotice;
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_find, container,
				false);
		initViews(view);
		initData();
		return view;
	}

	@Override
	public void onDestroyView() {
		UmengUpdateAgent.setUpdateListener(null);
		super.onDestroyView();
	}

	private void initViews(View view) {
		view.findViewById(R.id.find_shoushouribao).setOnClickListener(this);
		view.findViewById(R.id.find_shiyanzhoukan).setOnClickListener(this);
		view.findViewById(R.id.find_faguiwenxian).setOnClickListener(this);
		view.findViewById(R.id.find_huodongzhongxin).setOnClickListener(this);
		view.findViewById(R.id.find_pinpaiku).setOnClickListener(this);
		view.findViewById(R.id.find_peixunxinxi).setOnClickListener(this);
		view.findViewById(R.id.find_jifenshangcheng).setOnClickListener(this);
		view.findViewById(R.id.find_paihangbang).setOnClickListener(this);
		
		
		ViewFinder viewFinder=new ViewFinder(view);
		zushouNotice=viewFinder.find(R.id.ribao_tab_notice);
		shiyanNotice=viewFinder.find(R.id.zhoukan_tab_notice);
		huodongNotice=viewFinder.find(R.id.huodong_tab_notice);
		peixunNotice=viewFinder.find(R.id.peixun_tab_notice);
		paihangNotice=viewFinder.find(R.id.paihang_tab_notice);
		
		//更新
				MessageNum message=AppContext.instance().getMessageNum();
				if(message==null){
					message=new MessageNum();
				}
		updateAllMessage(message);
		try {
			EventBus.getDefault().register(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 刷新
	 * @param totalMessage
	 */
	public void onEventMainThread(MessageRefreshTotal totalMessage){
		updateAllMessage(totalMessage.messageNum);
	}

	private void updateAllMessage(MessageNum message) {
		
		ActiveNumType.updateMessageLabel(message.getDnum(), zushouNotice);
		ActiveNumType.updateMessageLabel(message.getWnum(), shiyanNotice);
		ActiveNumType.updateMessageLabel(message.getTnum(), peixunNotice);
		ActiveNumType.updateMessageLabel(message.getPnum(), huodongNotice);
		ActiveNumType.updateMessageLabel(message.getChartnum(), paihangNotice);
	}
	
	/**
	 * 更新消息状态为已读
	 * @param tv
	 * @param type
	 */
	public void updateTime(final TextView tv,final int type){
		
		NewsApi.updateTime(AppContext.instance().getLoginUid(), type, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						ActiveNumType.updateMessageLabel(0, tv);
						ActiveNumType.updateMessageNum(0, type, AppContext.instance().getMessageNum());
						EventBus.getDefault().post(new MessageRefreshSingle());
					} else {
						AppContext.showToast(response.optString("desc"));
					}
				} catch (Exception e) {
				}
			}
		});
	}


	@SuppressWarnings("deprecation")
	private void initData() {
	}

	@Override
	public void onClick(View v) {
		
		final int id = v.getId();
		if (id == R.id.find_shoushouribao) {
			UIHelper.showDaily(getActivity());
			updateTime(zushouNotice,ActiveNumType.assist_type);
		} else if (id == R.id.find_shiyanzhoukan) {
			UIHelper.showWeekly(getActivity());
			updateTime(shiyanNotice,ActiveNumType.lab_type);
		} else if (id == R.id.find_faguiwenxian) {
			if (AppContext.instance().isLogin()) {
				UIHelper.showReferences(getActivity());
			}else{
				//UIHelper.showLogin(getActivity());
				AppContext.showToast("登录才能查看！");
			}
			
		} else if (id == R.id.find_huodongzhongxin) {
			UIHelper.showActivityCenter(getActivity());
			updateTime(huodongNotice,ActiveNumType.active_type);
		} else if (id == R.id.find_pinpaiku) {
			UIHelper.showBrand(getActivity());
			
		} else if (id == R.id.find_peixunxinxi) {
			UIHelper.showTrain(getActivity());
			updateTime(peixunNotice,ActiveNumType.study_type);
		} else if (id == R.id.find_jifenshangcheng) {
			if (AppContext.instance().isLogin()) {
				UIHelper.showMall(getActivity());
			}else{
				//UIHelper.showLogin(getActivity());
				AppContext.showToast("登录才能查看！");
			}
			
		} else if (id == R.id.find_paihangbang) {
			UIHelper.showBillboard(getActivity());
			updateTime(paihangNotice,ActiveNumType.top_type);
		}
	}



	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(FIND_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(FIND_SCREEN);
		MobclickAgent.onPause(getActivity());
	}
	
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
