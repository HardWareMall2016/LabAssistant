package net.oschina.app.v2.activity.tweet.view;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.friend.adapter.TweetUserAdapter;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.utils.DeviceUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class TweetReviewDialog {

	private Context context;
	private TweetUserAdapter mAdapter;
	
	public TweetReviewDialog(Context context) {
		this.context=context;
	}
	
	public void createReviceUserDialog(int aid) {
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		final Dialog dialog=builder.create();
		dialog.show();

		int height=DeviceUtils.getScreenHeight(context)/3;
		View view=View.inflate(context, R.layout.tweet_review_zhichi_view, null);
		dialog.setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, height));
		
		ImageView btnExit=(ImageView)dialog.findViewById(R.id.tweet_exit);
		ListView listview=(ListView)dialog.findViewById(R.id.tweet_list);
		
		mAdapter=new TweetUserAdapter(context);
		listview.setAdapter(mAdapter);
		
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		sendRequest(aid);
	}
	
	private void sendRequest(int aid) {
		int uid=AppContext.instance().getLoginUid();
		NewsApi.getSupportList(uid, aid, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code")==88) {
						List<UserBean> dataList=new ArrayList<UserBean>();
						JSONArray array = new JSONArray(response.getString("data"));
						for (int i=0;i<array.length();i++) {
							UserBean user=UserBean.parse(array.getJSONObject(i));
							dataList.add(user);
						}
						
						mAdapter.setItems(dataList);
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
