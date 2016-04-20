package net.oschina.app.v2.activity.settings.fragment;

import net.oschina.app.v2.api.ApiClientHelper;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.BaseFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;

public class RenZhengShuoMingFragment extends BaseFragment {

	private static final String ABOUT_SCREEN = "about_screen";
	
	 
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_renzhengshuoming, container,
				false);
		setWebview(view);
		return view;
	}
	
	 WebView mWebView;
	    private void setWebview(View view) {

	        mWebView = (WebView) view.findViewById(R.id.webView);

	        mWebView.setBackgroundColor(Color.WHITE);
	        mWebView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
	       // mWebView.getSettings().setAllowContentAccess(true);



	        mWebView.getSettings().setDefaultTextEncodingName("utf-8"); // set
	       
	      //  mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
	        mWebView.getSettings().setAppCacheEnabled(true);
	        mWebView.getSettings().setDatabaseEnabled(true);
	        mWebView.getSettings().setDomStorageEnabled(true);

	    /*    mWebView.clearCache(true);
	        mWebView.clearHistory();*/

	     


	        mWebView.setOnKeyListener(new View.OnKeyListener() {
	            @Override
	            public boolean onKey(View v, int keyCode, KeyEvent event) {
	                if (event.getAction() == KeyEvent.ACTION_DOWN) {
	                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //表示按返回键时的操作
	                        mWebView.goBack();   //后退

	                        //webview.goForward();//前进
	                        return true;    //已处理
	                    }
	                }
	                return false;
	            }

	        });
	    
	        mWebView.loadUrl(ApiHttpClient.getAbsoluteApiUrl("Html/System/explain.html"));
	    }
	

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(ABOUT_SCREEN);
		MobclickAgent.onResume(getActivity());
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(ABOUT_SCREEN);
		MobclickAgent.onPause(getActivity());
	}
}
