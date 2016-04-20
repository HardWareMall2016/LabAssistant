package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.common.SearchBackActivity;
import net.oschina.app.v2.activity.user.adapter.DiscussAdapter;
import net.oschina.app.v2.activity.user.model.DiscussList;
import net.oschina.app.v2.activity.user.model.DiscussPojo;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.model.User;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
public class DisucussFragment extends BaseListFragment{
	protected static final String TAG = DisucussFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX_5= "mydiscuss_1";
	
	private DiscussAdapter mDiscussAdapter;
	private EditText discussContentEt;
	private int articleId = -1;
	
	@Override
	protected int getLayoutRes() {
		
		return R.layout.v2_fragment_discuss;
	}

	protected JsonHttpResponseHandler addInterestedHandler = new JsonHttpResponseHandler() {
		@Override
		//隐藏键盘
		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
			
			InputMethodManager imm=(InputMethodManager) getActivity().getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(discussContentEt.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);

			AppContext.showToast("评论成功");

		}

		@Override
		public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject errorResponse) {
			AppContext.showToast("添加失败了");
		}
	};

	@Override
	protected void initViews(View view) {
		super.initViews(view);
		
		mListView.setMode(Mode.PULL_FROM_START);
		
		discussContentEt = (EditText) view.findViewById(R.id.discuss_content);
		
		View commitView = view.findViewById(R.id.commit_discuss);
		commitView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(! AppContext.instance().isLogin())
				{
					AppContext.showToast("请先登录");
					
					Intent intent = new Intent(getActivity(),MainActivity.class);
					intent.putExtra("type", "login");
					getActivity().startActivity(intent);
					
					return;
				}
				
				String content = discussContentEt.getText().toString();
				if(TextUtils.isEmpty(content))
				{
					AppContext.showToast("请输入评论内容");
					return;
				}
				
				NewsApi.addDiscuss(articleId, content, addInterestedHandler);
				discussContentEt.setText("");
				
				DiscussPojo pojo = new DiscussPojo();
				
				User user = AppContext.instance().getLoginInfo();
				pojo.setUid(user.getId());
				pojo.setInputtime("刚刚");
				pojo.setUserName(user.getNickname());
				pojo.setUserIcon(user.getHead());
				pojo.setRank(user.getRank());
				pojo.setContent(content);
				
				
				mDiscussAdapter.getData().add(0, pojo);
				mDiscussAdapter.notifyDataSetChanged();
				
				if(mDiscussAdapter.getDataSize()==1){
					mDiscussAdapter.clear();
					ArrayList<DiscussPojo> list=new ArrayList<DiscussPojo>();
					list.add(pojo);
					
					executeOnLoadDataSuccess(list);
				}
				
			}
		});
		
	}


	@Override
	protected ListBaseAdapter getListAdapter() {
		if(null == mDiscussAdapter)
		{
			mDiscussAdapter = new DiscussAdapter();
		}
		
		return mDiscussAdapter;
	}
	//缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_5;
	}
	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		//流转换成为String
		String result=inStream2String(is).toString();
		//解析string得到集合。
		DiscussList myInterstList = DiscussList.parse(result);
		is.close();
		return myInterstList ;
	}
	
	@Override
	protected ListEntity readList(Serializable seri) {
		return ((DiscussList) seri);
	}
	
	//发送请求的数据。
	@Override
	protected void sendRequestData() {
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		NewsApi.getDiscuss(AppContext.instance().getLoginUid(),articleId, mCurrentPage, mJsonHandler);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}


	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	
	
	
}
