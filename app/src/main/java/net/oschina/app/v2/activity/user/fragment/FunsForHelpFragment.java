package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.adapter.funsForHelperAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.AskList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.event.FansTabEvent;
import net.oschina.app.v2.utils.UIHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import de.greenrobot.event.EventBus;

public class FunsForHelpFragment extends BaseListFragment {
	protected static final String TAG = FunsForHelpFragment.class
			.getSimpleName();
	private static final String CACHE_KEY_PREFIX_10 = "funsforhelplist_";
	private int mStatus =1;//1 全部  2 已回答 3 未回答

	//Tab fans views
	private TextView mViewAllQuestion;
	private TextView mViewAnsweredQuestion;
	private TextView mViewUnansweredQuestion;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		mViewAllQuestion=(TextView)view.findViewById(R.id.all_question);
		mViewAnsweredQuestion=(TextView)view.findViewById(R.id.answered_question);
		mViewUnansweredQuestion=(TextView)view.findViewById(R.id.unanswered_question);
		mViewAllQuestion.setOnClickListener(mFansTabClickListener);
		mViewAnsweredQuestion.setOnClickListener(mFansTabClickListener);
		mViewUnansweredQuestion.setOnClickListener(mFansTabClickListener);
	}

	private View.OnClickListener mFansTabClickListener=new View.OnClickListener(){
		@Override
		public void onClick(View v) {
			mViewAllQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewAllQuestion.setBackgroundResource(R.drawable.default_bg);
			mViewAnsweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewAnsweredQuestion.setBackgroundResource(R.drawable.default_bg);
			mViewUnansweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
			mViewUnansweredQuestion.setBackgroundResource(R.drawable.default_bg);

			switch (v.getId()){
				case R.id.all_question:
					mViewAllQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewAllQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					mStatus=1;
					break;
				case R.id.answered_question:
					mViewAnsweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewAnsweredQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					mStatus=3;
					break;
				case R.id.unanswered_question:
					mViewUnansweredQuestion.setTextColor(getResources().getColor(R.color.text_dark));
					mViewUnansweredQuestion.setBackgroundResource(R.drawable.bg_blue_underline);
					mStatus=2;
					break;
			}
			setRefresh();
		}
	};

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new funsForHelperAdapter(getActivity());
		//return new FunsForHelpAdapter();
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.my_fans_fragment_pull_refresh_listview;
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_10;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
//		FunsForHelpList list = FunsForHelpList.parse(result);
		AskList list = AskList.parse(result);
		is.close();
		return list;
	}
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AskList) seri);
		//return ((FunsForHelpList) seri);
	}
	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
//		int uid=AppContext.instance().getLoginUid();
//		NewsApi.getFunsForHelpList(uid, mCurrentPage, mJsonHandler);
		//Old changed by wuyue
		/*mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFunsForHelpList(AppContext.instance().getLoginUid(), mCurrentPage, mJsonHandler);*/

		mCurrentPage=mCurrentPage==0 ? 1 : mCurrentPage;
		NewsApi.getFansAskList(AppContext.instance().getLoginUid(), mCurrentPage, mStatus, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		FunsForHelp forHelp = (FunsForHelp) parent.getItemAtPosition(position);
//		if (forHelp != null) {
//			Ask ask = new Ask();
//			ask.setId(forHelp.getId());
//			ask.setanum(forHelp.getAnum());
//			ask.setLabel(forHelp.getLable());
//			ask.setContent(forHelp.getContent());
//			//Johnny
//			//ask.setinputtime(forHelp.getIntputtime());
//			ask.setsuperlist(forHelp.getSuperlist());
//			ask.setnickname(forHelp.getNickname());
//			UIHelper.showTweetDetail(view.getContext(), ask);
//		}
		Ask ask = (Ask) mAdapter.getItem(position - 1);
		if (ask != null)
			UIHelper.showTweetDetail(view.getContext(),ask);
	}
}
