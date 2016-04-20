package net.oschina.app.v2.activity.common;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

public class BrandActivity extends Activity {
	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	public static AsyncHttpClient client;
	String url = "http://phpapi.ccjjj.net/index.php/Api/Found/brands.html";
	private StringEntity se;
	private GridView mGridView;
	private PullToRefreshGridView mPullRefreshGridView;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_brand);
		AsyncHttpClient client = new AsyncHttpClient();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("pid", 1);
			jsonObject.put("num", 30);
			StringEntity entity = new StringEntity(jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			private String data;

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				try {
					data = response.getString("data");
					Toast.makeText(BrandActivity.this,
							response.getString("data").toString(), 10).show();
					System.out.println(data);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// Toast.makeText(MainActivity.this, "异常1".toString(), 10)
					// .show();
					e.printStackTrace();
				}
				try {

					JSONObject obj = new JSONObject(data);
					String thumb = obj.getString("thumb");
					String title = obj.getString("title");
					String description = obj.getString("description");
					String inputtime = obj.getString("inputtime");
					System.out.println(thumb + "!!!!!!!!!!");

					Toast.makeText(BrandActivity.this, thumb, 10).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(BrandActivity.this, "异常2".toString(), 10)
							.show();
				}

			}
		};

	}
}
