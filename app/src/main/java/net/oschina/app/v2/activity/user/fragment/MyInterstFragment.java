package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.MyInterstedAdapter;
import net.oschina.app.v2.activity.user.adapter.MyInterstedAdapter.OnChoiceSelectedListener;
import net.oschina.app.v2.activity.user.model.Intersted;
import net.oschina.app.v2.activity.user.model.MyInterstList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.CommentList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.User;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class MyInterstFragment extends BaseListFragment {
	protected static final String TAG = MyInterstFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX_5 = "myinterst_1";

	private MyInterstedAdapter myInterstedAdapter;
	
	

	@Override
	protected int getLayoutRes() {
		return R.layout.v2_fragment_instersted_for_me;
	}

	protected JsonHttpResponseHandler addInterestedHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {

			String tip = response.optString("desc");
			
			
	
			User user = AppContext.instance().getLoginInfo();
			if(user==null){
				AppContext.showToast("添加失败了");
				return;
			}
			
			AppContext.showToast("添加成功！");

			StringBuffer sb = new StringBuffer();
			for (Intersted intersted : myInterstedAdapter.getChoicedIds()) {
				sb.append(intersted.getCatname()+" ");
			}
			user.setInterest(sb.toString());
			AppContext.instance().saveLoginInfo(user);
			Editor editor = AppContext.getPersistPreferences().edit();
			editor.putString(response.toString(), response.toString());
			editor.commit();
			if("success".equals(tip))
			{
				getActivity().finish();
			}
		//	AppContext.showToast(AppContext.getPersistPreferences().getString(response.toString(), ""));
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			AppContext.showToast("添加失败了");
		}
	};
	Button commitView;
	int isChoose=0;
	@Override
	protected void initViews(View view) {
		super.initViews(view);
		mListView.setMode(Mode.DISABLED);

		commitView = (Button)view.findViewById(R.id.commit_intersted);
		
		// 改变按钮文本和框架标题
		int key = getActivity().getIntent().getIntExtra("key", 0);
		if (key == 1) {
			//您感兴趣的
			((BaseActivity) getActivity()).setActionBarTitle("您感兴趣的 ");		
			commitView.setText("提交");
		}
		
		commitView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int status = myInterstedAdapter.isHaveChoiceInEveryCategory();
				if (-3 == status) {
					AppContext.showToast("没获取到数据哦");
				} else if (-2 == status) {
					AppContext.showToast("您还没选择哦");
				} else if (-1 == status) {
					AppContext.showToast("每个分类至少选择一个哦");
				} else if (0 == status) {
					NewsApi.addMyInterstedList(
							myInterstedAdapter.getChoicedIds(),
							addInterestedHandler);
				} else if( 0 == status)
				{
					//发送兴趣的请求，包含uid
					int id=AppContext.instance().getLoginInfo().getId();
				//	NewsApi.addMyInterstedList(myInterstedAdapter.getChoicedIds(), addInterestedHandler);
					NewsApi.addMyInterstedList(id, myInterstedAdapter.getChoicedIds(),addInterestedHandler);
				}
			}
		});

	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		if (null == myInterstedAdapter) {
			myInterstedAdapter = new MyInterstedAdapter();
			myInterstedAdapter.setOnSelectedListener(onSelectedListener);
		}

		return myInterstedAdapter;
	}
	
	@Override
	protected void executeOnLoadDataSuccess(List<?> data) {
		super.executeOnLoadDataSuccess(data);
		commitView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onSelectedListener.onCheckedChanged();
			}
		}, 600);
		
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_5;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
		// AskAgainList list = AskAgainList.parse(result);
		MyInterstList myInterstList = MyInterstList.parse(result);
		is.close();
		return myInterstList;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((MyInterstList) seri);
	}

	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
		NewsApi.getMyInterstedList(AppContext.instance().getLoginUid(),
				mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	
	
	private OnChoiceSelectedListener onSelectedListener=new OnChoiceSelectedListener() {
		
		@Override
		public void onCheckedChanged() {
			isChoose =isChoose+1;
			int status = myInterstedAdapter.isHaveChoiceInEveryCategory();
			if(status>=0 && isChoose>1){//invalid
				commitView.setEnabled(true);
			}else{
				commitView.setEnabled(false);
			}
			
		}
	};
}
