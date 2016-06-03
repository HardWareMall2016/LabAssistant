package net.oschina.app.v2.emoji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.utils.TDevice;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class SupperListActivity extends BaseActivity {

	private ListView mListView;
	private RelativeLayout mTipLayout;
	private ImageView mTipClose;
	private SupperAdapter mAdapter;

	private EditText searchEt;
	private ImageButton searchBtn;

	private String keyword="";
	
	private HashMap<Integer, UserBean> userList;

	private List<UserBean> mAllDataList = new ArrayList<UserBean>();
	@Override
	protected void init(Bundle savedInstanceState) {
		userList = (HashMap<Integer, UserBean>) getIntent()
				.getSerializableExtra("supperList");
		
		mListView = (ListView) findViewById(R.id.super_list);
		mAdapter = new SupperAdapter(this);
		mAdapter.setUserList(userList);
		mListView.setAdapter(mAdapter);

		searchEt = (EditText) findViewById(R.id.et_content);
		searchBtn = (ImageButton) findViewById(R.id.btn_search);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TDevice.hideSoftKeyboard(searchBtn);
				keyword=searchEt.getText().toString();
				sendRequest();
			}
		});
		
		mTipLayout = (RelativeLayout) findViewById(R.id.tip_layout);
		mTipClose = (ImageView) findViewById(R.id.tip_close);
		mTipClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mTipLayout.setVisibility(View.GONE);
			}
		});

		sendRequest();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.super_user_layout;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.actionbar_title_supperlist;
	}

	protected void initActionBar(ActionBar actionBar) {
		if (actionBar == null)
			return;

		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = inflateView(R.layout.v2_actionbar_custom_asktitle);
		View back = view.findViewById(R.id.btn_back);
		if (back == null) {
			throw new IllegalArgumentException(
					"can not find R.id.btn_back in customView");
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
		View confirm=view.findViewById(R.id.btn_confirm);
		confirm.setVisibility(View.VISIBLE);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.putExtra("supperList", mAdapter.getUserList());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		
		TextView mTvActionTitle = (TextView) view
				.findViewById(R.id.tv_ask_title);
		if (mTvActionTitle == null) {
			throw new IllegalArgumentException(
					"can not find R.id.tv_actionbar_title in customView");
		}
		int titleRes = getActionBarTitle();
		if (titleRes != 0) {
			mTvActionTitle.setText(titleRes);
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		actionBar.setCustomView(view, params);

	}

	private void sendRequest() {
		int uid = AppContext.instance().getLoginUid();
		NewsApi.getSupperlist(uid,keyword,new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						List<UserBean> dataList = new ArrayList<UserBean>();
						JSONArray array = new JSONArray(response
								.getString("data"));
						for (int i = 0; i < array.length(); i++) {
							UserBean user = UserBean.parse(array
									.getJSONObject(i));
							dataList.add(user);
						}

						mAllDataList=dataList;
						mAdapter.setItems(mAllDataList);
						mAdapter.notifyDataSetChanged();
					}else if(!TextUtils.isEmpty(response.optString("desc"))){
						AppContext.showToast(response.optString("desc"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
