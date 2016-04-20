package net.oschina.app.v2.activity.user.fragment;

import org.apache.http.Header;
import org.json.JSONObject;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.AnswerAftertTabPagerAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.event.MessageRefreshSingle;
import net.oschina.app.v2.ui.pagertab.PagerSlidingTabStrip;
import net.oschina.app.v2.utils.ActiveNumType;
import net.oschina.app.v2.utils.ViewFinder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

public class AnswerAfterPagerFragment extends Fragment implements
		OnPageChangeListener {
	
	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mViewPager;
	private AnswerAftertTabPagerAdapter mTabAdapter;
	private View newMessageContainer;//新消息提示
	private TextView myAskTip;//我的追问
	private TextView askMeTip;//追问我的
	private int mAskMeNum;//追问我的数量
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_viewpager, container,
				false);
		mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		mViewPager = (ViewPager) view.findViewById(R.id.main_tab_pager);

		if (mTabAdapter == null) {
			mTabAdapter = new AnswerAftertTabPagerAdapter(getChildFragmentManager(),
					getActivity(), mViewPager);
		}
		mViewPager.setOffscreenPageLimit(mTabAdapter.getCacheCount());
		mViewPager.setAdapter(mTabAdapter);
		mViewPager.setOnPageChangeListener(this);
		mTabStrip.setViewPager(mViewPager);
		
		ViewFinder viewFinder=new ViewFinder(view);
		newMessageContainer=viewFinder.find(R.id.newMessageContainer);
		myAskTip=viewFinder.find(R.id.tab_myask_tip);
		askMeTip=viewFinder.find(R.id.tab_askme_tip);

		Bundle bundle=getArguments();
		if(bundle!=null){
			mAskMeNum=bundle.getInt("askMeNum",0);
		}
		
		if(mAskMeNum>0){
			newMessageContainer.setVisibility(View.VISIBLE);
		}else{
			newMessageContainer.setVisibility(View.GONE);
		}
		
		updateMessageLabel(mAskMeNum,askMeTip);
		
		return view;
	}
	
	/**
	 * 根据类型,数量更新消息label
	 * @param type
	 * @param tv
	 * @param num
	 */
	public static void updateMessageLabel(int num,TextView tv){
		if (num > 0) {
			tv.setVisibility(View.VISIBLE);
			//大于99显示99+
			String findStr=num>99?num+"+":num+"";
			tv.setText(findStr);
		} else {
			tv.setText("");
			tv.setVisibility(View.GONE);
		}
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
						updateMessageLabel(0, tv);
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

	@Override
	public void onPageScrollStateChanged(int arg0) {
		mTabStrip.onPageScrollStateChanged(arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		mTabStrip.onPageScrolled(arg0, arg1, arg2);
		mTabAdapter.onPageScrolled(arg0);
	}

	@Override
	public void onPageSelected(int arg0) {
		mTabStrip.onPageSelected(arg0);
		mTabAdapter.onPageSelected(arg0);
		if(arg0==1){
			if(mAskMeNum>0)
				updateTime(askMeTip,ActiveNumType.askmeafter_type);
				mAskMeNum=0;
		}
	}


}
