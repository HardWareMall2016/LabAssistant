package net.oschina.app.v2.activity;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.MessageNum;
import net.oschina.app.v2.model.event.MessageRefreshTotal;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import de.greenrobot.event.EventBus;

import android.os.Handler;

/**
 * 刷新消息
 * @author 95
 *
 */
public class RefreshMessageUtil {

	public static final int internal=10*1000;
	
	private Handler mHandler;
	
	private boolean isStop;
	
	public void start(Handler handler){
		mHandler=handler;
		mHandler.postDelayed(runnable, internal);
		sendActiveNum();
	}
	
	public void stop(){
		isStop=true;
		mHandler=null;
	}
	
	private Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			if(isStop==true||mHandler==null){
				return;
			}
			sendActiveNum();
			if(isStop!=true&&mHandler!=null){
				mHandler.postDelayed(runnable, internal);
			}
		}
	};
	
	private void sendActiveNum() {
		// 如果未登录过,不发获取数据协议。
		if (0 == AppContext.instance().getLoginUid()) {
			return;
		}
		NewsApi.getActiveNum(AppContext.instance().getLoginUid(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						try {
							if (response.optInt("code") == 88) {
								JSONObject json = new JSONObject(response
										.optString("data"));
								MessageNum message = MessageNum.parse(json);
								AppContext.instance().setMessageNum(message);
								MessageRefreshTotal totalMessage=new MessageRefreshTotal();
								totalMessage.messageNum=message;
								//通知更新UI
								EventBus.getDefault().post(totalMessage);
							} else {
								//AppContext.showToast(response.optString("desc"));
							}
						} catch (Exception e) {
						}
					}
				});
	}
}
