package net.oschina.app.v2.activity.user.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;

import org.apache.http.Header;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class TaskChengjiuFragment extends BaseFragment implements
		OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater
				.inflate(R.layout.task_chengjiu_layout, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.click_obtain1).setOnClickListener(this);
		view.findViewById(R.id.click_obtain2).setOnClickListener(this);
		view.findViewById(R.id.click_obtain3).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.click_obtain1:
			sendRequest("dayanswer", v);
			break;
		case R.id.click_obtain2:
			sendRequest("dayquestion", v);
			break;
		case R.id.click_obtain3:
			sendRequest("daylogin", v);
			break;
		}
	}
	
	private void sendRequest(String type, final View v) {
		int uid=AppContext.instance().getLoginUid();
		NewsApi.getSubmittask(uid, type, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				v.setVisibility(View.INVISIBLE);
				AppContext.showToast(response.optString("desc"));
			}
		});
	}
}
