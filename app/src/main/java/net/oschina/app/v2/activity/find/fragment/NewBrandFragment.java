package net.oschina.app.v2.activity.find.fragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.adapter.NewBrandAdapter;
import net.oschina.app.v2.activity.find.adapter.NewBrandAdapter.onItemClickListener;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.Brand;
import net.oschina.app.v2.model.BrandList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.ui.empty.EmptyLayout;
import net.oschina.app.v2.utils.SimpleTextWatcher;
import net.oschina.app.v2.utils.UIHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.shiyanzhushou.app.R;

/**
 * 品牌库
 * 
 */
public class NewBrandFragment extends BaseListFragment {

	protected static final String TAG = NewBrandFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "brandlist_";
	private int mCatalog;

	private EditText searchEt;
	private ImageButton searchBtn;
	private RelativeLayout tipLayout;
	private ImageView tipClose;

	private ArrayList<Brand> dataList = new ArrayList<Brand>(12);
	private List<Brand> allDatas = new ArrayList<Brand>(12);
	private NewBrandAdapter mLawsAdapter;

	@Override
	protected int getLayoutRes() {
		// TODO Auto-generated method stub
		return R.layout.a_new_brand;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(getLayoutRes(), container, false);
		initViews(v);
		return v;
	}

	
	private TextWatcher searchEtWatcher = new SimpleTextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		 if (TextUtils.isEmpty(searchEt.getText())){
			  mAdapter.clear();
			  mCurrentPage=1;
			  sendRequestData(); //搜素输入文字，再删掉文字后，应该会显示所有内容（和刚进入一样）
		 }
		}
	};
	
	@Override
	protected void initViews(View view) {

		searchEt = (EditText) view.findViewById(R.id.et_content);
		searchEt.addTextChangedListener(searchEtWatcher);

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

		//mListView.setOnItemClickListener(this);
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
		NewBrandAdapter ada=(NewBrandAdapter)mAdapter;
		ada.setOnItemClickListener(onItemClickListener);
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
		// mCurrentPage=1;
		// sendRequestData() ;

		searchBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLawsAdapter.getCount() == 0) {
					AppContext.showToastShort("暂无数据");
					return;
				}

//				dataList.clear();
//				allDatas.clear();
				mAdapter.clear();

				String searchContent = searchEt.getText().toString();
//				if (TextUtils.isEmpty(searchContent)) {
//					dataList.addAll(allDatas);
//				} else {
//					for (Brand item : allDatas) {
//						if (!TextUtils.isEmpty(item.getTitle())
//								&& item.getTitle().contains(searchContent)) {
//							dataList.add(item);
//						}
//
//					}
//				}
//
//				mLawsAdapter.setData(dataList);
//				mLawsAdapter.notifyDataSetChanged();mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
				if (AppContext.instance().isLogin()) {
					int uid = AppContext.instance().getLoginUid();
					NewsApi.getBrandList(uid, searchContent, mCurrentPage, mJsonHandler);
				} else {
					NewsApi.getBrandList(1, searchContent, mCurrentPage, mJsonHandler);
				}

			}
		});

	}

	@Override
	protected ListBaseAdapter getListAdapter() {
		if (null == mLawsAdapter) {
			mLawsAdapter = new NewBrandAdapter();
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

		BrandList data = BrandList.parse(result);
		// allDatas.clear();

		if (null != data && null != data.getBrandlist())
			for (Brand item : data.getBrandlist()) {
				allDatas.add(item);
			}

		is.close();
		return data;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((BrandList) seri);
	}

	@Override
	protected void sendRequestData() {
		mCurrentPage = mCurrentPage < 1 ? 1 : mCurrentPage;
		if (AppContext.instance().isLogin()) {
			int uid = AppContext.instance().getLoginUid();
			NewsApi.getBrandList(uid, mCurrentPage, mJsonHandler);
		} else {
			NewsApi.getBrandList(1, mCurrentPage, mJsonHandler);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Brand laws = (Brand) mAdapter.getItem(position - 1);
		if (laws != null) {
			UIHelper.showBrandDetailRedirect(view.getContext(), laws);
		}
	}
	
	private onItemClickListener onItemClickListener=new onItemClickListener() {
		
		@Override
		public void onItemClick(View view ,int position) {
			try {
				Brand laws = (Brand) mAdapter.getItem(position);
				if (laws != null) {
					UIHelper.showBrandDetailRedirect(view.getContext(), laws);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
}
