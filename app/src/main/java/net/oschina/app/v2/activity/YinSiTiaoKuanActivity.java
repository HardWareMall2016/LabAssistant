package net.oschina.app.v2.activity;

import net.oschina.app.v2.base.BaseActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.shiyanzhushou.app.R;

/**
 * 隐私条款政策
 */
public class YinSiTiaoKuanActivity extends BaseActivity {

	protected int getLayoutId() {
		return R.layout.v2_activity_authorize;
	}

	@Override
	protected boolean hasBackButton() {
		return true;
	}

	@Override
	protected int getActionBarTitle() {
		return R.string.yin_si_zheng_ce;
	}

	// 拿到控件
	// 延时的内部类

	@Override
	protected void init(Bundle savedInstanceState) {
		super.init(savedInstanceState);
		WebView webview = (WebView) findViewById(R.id.webview);
		WebSettings ws = webview.getSettings();
		ws.setDefaultTextEncodingName("utf-8"); //设置文本编码
		webview.loadUrl("file:///android_asset/register_tiao_kuan.html");
//		webview.loadDataWithBaseURL("file:///android_asset/register_tiao_kuan.html", null, "text/html","UTF-8", null);
	}
}
