package net.oschina.app.v2.activity.favorite.fragment;

import java.io.IOException;

import net.oschina.app.v2.AppConfig;
import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.AppException;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.model.Daily;
import net.oschina.app.v2.utils.DateUtil;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class QuestionPageActivity extends BaseActivity {

	/*private TextView textTime;
	private UIPanelView panelView;
	private TextView textContent;*/
	private WebView webView;

	private int catId = 0;
	private int qnid = 0;
	private int type=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		catId = getIntent().getIntExtra("catid", 0);
		type=getIntent().getIntExtra("type",1);

		webView=(WebView)findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
	/*	textContent = (TextView) findViewById(R.id.item_content);
		textTime = (TextView) findViewById(R.id.item_time);
		panelView = (UIPanelView) findViewById(R.id.panelview);
		panelView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addRequest();
			}
		});*/

		 sendRequest();
	}

	/*private void updateView(QuestionModel model) {
		qnid = model.getId();
		textContent.setText(model.getContent().replaceAll("</?[^>]+>", ""));
		panelView.updateView(model);

		String time = DateUtil.getFormatTime(model.getInputtime());
		textTime.setText(time);
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.question_page_layout_web;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.find_shoushouribao;
	}

	private void sendRequest() {
		int uid=0;
		if(AppContext.instance().isLogin()==true){
			uid=AppContext.instance().getLoginUid();
		}
		NewsApi.getQuestionnairelist(uid,catId,type, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					if (response.optInt("code") == 88) {
						JSONObject jsonObj = new JSONObject(response
								.optString("data"));
						String url = jsonObj.optString("url");
						webView.loadUrl(url);
						//updateView(QuestionModel.parse(response));
					} else {
						AppContext.showToast("请求数据失败");
					}
				} /*catch (IOException e) {
				} catch (AppException e) {
				}*/ catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/*private void addRequest() {
		String options = panelView.getOptions();
		int uid = AppContext.instance().getLoginUid();

		if (!AppContext.instance().isLogin()) {
			UIHelper.showLoginView(this);
			return;
		}

		if (qnid == 0 || TextUtils.isEmpty(options)) {
			AppContext.showToast("请填写问卷内容");
			return;
		}

		NewsApi.addQuestionnairelist(uid, qnid, options,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						if (response.optInt("code") == 88) {
							finish();
							AppContext.showToast("提交成功");
						} else {
							AppContext.showToast("提交失败");
						}
					}
				});
	}*/
}
