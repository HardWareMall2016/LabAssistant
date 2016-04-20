package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.adapter.LawsAdapter;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Laws;
import net.oschina.app.v2.model.LawsList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.UIHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shiyanzhushou.app.R;

/**
 * 新闻资讯
 * 
 * @author william_sim
 */
public class ReferencesFragment extends BaseListFragment {

	protected static final String TAG = ReferencesFragment.class
			.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "lawslist_";
	private int mCatalog;

	private EditText searchEt;
	private ImageButton searchBtn;
	private RelativeLayout tipLayout;
	private ImageView tipClose;

	private ArrayList<Laws> dataList = new ArrayList<Laws>(12);
	private List<Laws> allDatas = new ArrayList<Laws>();
	private LawsAdapter mLawsAdapter;

	@Override
	protected int getLayoutRes() {
		// TODO Auto-generated method stub
		return R.layout.a_laws;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(getLayoutRes(), container, false);
		initViews(v);
		return v;
	}

	@Override
	protected void initViews(View view) {

		searchEt = (EditText) view.findViewById(R.id.et_content);
		searchBtn = (ImageButton) view.findViewById(R.id.btn_search);
		tipLayout = (RelativeLayout) view.findViewById(R.id.tip_layout);
		tipClose = (ImageView) view.findViewById(R.id.tip_close);
		tipClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tipLayout.setVisibility(View.GONE);
			}
		});

		mErrorLayout = (EmptyLayout) view.findViewById(R.id.error_layout_laws);
		mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = 0;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(true);
			}
		});
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.listview_laws);

		mListView.setOnItemClickListener(this);
		mListView.setOnRefreshListener(this);
		mListView.setOnLastItemVisibleListener(this);

		if (mAdapter != null) {
			mListView.setAdapter(mAdapter);
			mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		} else {
			mAdapter = getListAdapter();
			mListView.setAdapter(mAdapter);

			if (requestDataIfViewCreated()) {
				mCurrentPage = 1;
				mState = STATE_REFRESH;
				mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
				requestData(false);
			} else {
				mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
			}
		}
		if (mStoreEmptyState != -1) {
			mErrorLayout.setErrorType(mStoreEmptyState);
		}

		// if (mAdapter != null) {
		// mListView.setAdapter(mAdapter);
		// mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		// } else {
		// mAdapter = getListAdapter();
		// mListView.setAdapter(mAdapter);
		//
		// mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
		//
		// }
		//
		// mCurrentPage=1;
		// sendRequestData() ;

		searchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentPage = -1; //页数是-1
				mState = STATE_REFRESH;
				//dataList.clear();//清掉
				//allDatas.clear();
				sendRequestData();
			}
		});

	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		if (null == mLawsAdapter) {
			mLawsAdapter = new LawsAdapter();
		}

		return mLawsAdapter;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		String result = inStream2String(is);

		LawsList data = LawsList.parseList(result);
		if(mCurrentPage==1){
			allDatas.clear();
		}
		// allDatas.clear();

		if (null != data && null != data.getLawsList())
			for (Laws item : data.getLawsList()) {
				allDatas.add(item);
			}

		is.close();
		return data;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((LawsList) seri);
	}

	@Override
	protected void sendRequestData() {
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		String searchContent = searchEt.getText().toString();
		if (TextUtils.isEmpty(searchContent)) {    //搜全部
			if (AppContext.instance().isLogin()) {
				int uid = AppContext.instance().getLoginUid();
				NewsApi.getLawsList(uid, mCurrentPage, mJsonHandler);
			} else {
				NewsApi.getLawsList(1, mCurrentPage, mJsonHandler);
			}
		} else {									//关键字
			mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
			NewsApi.searchLawsList(mCurrentPage,searchContent,mJsonHandler);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Laws laws = (Laws) mAdapter.getItem(position - 1);
		if (laws != null)
			UIHelper.showLawsDetailRedirect(view.getContext(), laws);
	}
}
