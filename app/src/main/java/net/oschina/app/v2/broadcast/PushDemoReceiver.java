package net.oschina.app.v2.broadcast;

import java.util.Random;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.MallActivity;
import net.oschina.app.v2.activity.common.SimpleBackActivity;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.activity.tweet.CommunicatActivity;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.SimpleBackPage;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.shiyanzhushou.app.R;

public class PushDemoReceiver extends BroadcastReceiver {

	private static final String CLEAR_NOTI_ACTION ="net.oschina.app.v2.CLEAR_NOTI_ACTION";
	
	/**
	 * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView ==
	 * null)
	 */
	public static StringBuilder payloadData = new StringBuilder();

	@Override
	public void onReceive(Context context, Intent intent) {
		if(CLEAR_NOTI_ACTION.equals(intent.getAction().toString())){
			
			try {
				NotificationManager mNotifiManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
				int notifyId=intent.getIntExtra("notifyId", 0);
				mNotifiManager.cancel(notifyId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return;
		}
		
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() " + bundle.toString());
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			byte[] payload = bundle.getByteArray("payload");

			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

			if (payload != null) {
				String data = new String(payload);

				Log.d("GetuiSdkDemo", "receiver payload : " + data);

				try {
					JSONObject json = new JSONObject(data);
					JSONObject contentJson = new JSONObject(json.getString("content"));
					String type = contentJson.optString("type");
					int enter = contentJson.optInt("enter", 1);
					if (type.equals("daily")) {
						// 1、助手日报
						Intent clickIntent=null;
						if(enter==1){
							clickIntent = new Intent(context,
									ShowTitleDetailActivity.class);
							clickIntent.putExtra("id", contentJson.optInt("id"));
							clickIntent.putExtra("fromTitle",
									R.string.find_shoushouribao);
						}else{
							clickIntent = new Intent(context,
									SimpleBackActivity.class);
							clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.DAILY.getValue());
						}
					
					
//						showNotification(context,"提示","助手日报有新消息",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("weekly")) {
						// 2、实验周刊
						Intent clickIntent=null;
						if(enter==1){
							clickIntent = new Intent(context,
									ShowTitleDetailActivity.class);
							clickIntent.putExtra("id", contentJson.optInt("id"));
							clickIntent.putExtra("fromTitle",
									R.string.find_shiyanzhoukan);
							clickIntent.putExtra("fromSource", 2);
							clickIntent.putExtra("type", 1);
						}else{
							clickIntent = new Intent(context,
									SimpleBackActivity.class);
							clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.WEEKLY.getValue());
						
						}
//						showNotification(context,"提示","实验周刊有新消息",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("documents")) {
						// 3、法规文献
						Intent clickIntent=null;
						if(enter==1){
							 clickIntent = new Intent(context,
										ShowTitleDetailActivity.class);
								clickIntent.putExtra("id", contentJson.optInt("id"));
								clickIntent.putExtra("fromTitle",
										R.string.find_faguiwenxian);
						}else{
							clickIntent = new Intent(context,
									SimpleBackActivity.class);
							clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.REFERENCES.getValue());
						}
//						showNotification(context,"提示","法规文献有新消息",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("train")) {
						// 4、活动中心
						Intent clickIntent=null;
						if(enter==1){
							clickIntent = new Intent(context,
									ShowTitleDetailActivity.class);
							clickIntent.putExtra("id", contentJson.optInt("id"));
							clickIntent.putExtra("fromTitle",
									R.string.find_huodongzhongxin);
						}else{
							clickIntent = new Intent(context,
									SimpleBackActivity.class);
							clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.ACTIVITYCENTER.getValue());
						}
				
//						showNotification(context,"提示","活动中心有新消息",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("party")) {
						// 5、培训信息
						Intent clickIntent=null;
						if(enter==1){
							clickIntent = new Intent(context,
									ShowTitleDetailActivity.class);
							clickIntent.putExtra("id", contentJson.optInt("id"));
							clickIntent.putExtra("fromTitle",
									R.string.find_peixunxinxi);
						}else{
							clickIntent = new Intent(context,
									SimpleBackActivity.class);
							clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.TRAIN.getValue());
						}
						
						
//						showNotification(context,"提示","培训信息有新消息",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("brands")) {
						// 6、品牌库
						Intent clickIntent=null;
						if(enter==1){
							 clickIntent = new Intent(context,
										ShowTitleDetailActivity.class);
								clickIntent.putExtra("id", contentJson.optInt("id"));
								clickIntent.putExtra("fromTitle",
										R.string.find_pinpaiku);
						}else{
							clickIntent = new Intent(context,
									SimpleBackActivity.class);
							clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.BRAND.getValue());
						}
					
//						showNotification(context,"提示","品牌库有新消息",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("product")) {
						// 7、积分商城动向
						Intent clickIntent = new Intent(context,
								MallActivity.class);
//						showNotification(context,"提示","积分商城有新动向",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("ph")) {
						// 8、培训动向
						Intent clickIntent = new Intent(context,
								SimpleBackActivity.class);
						clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.TRAIN.getValue());
//						showNotification(context,"提示","积分商城有新动向",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("realname")) {
						// 9、实名认证
						Intent clickIntent = new Intent(context,
								SimpleBackActivity.class);
						Bundle args = new Bundle();
						args.putInt("his_id", AppContext.instance().getLoginUid());
						args.putInt(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.xitongxiaoxi.getValue());
						clickIntent.putExtras(args);
//						showNotification(context,"提示","实名认证审核通",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("giveintegral")) {
						//10、赠送积分通知
						Intent clickIntent = new Intent(context,
								MallActivity.class);
//						showNotification(context,"提示","积分商城有新动向",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("report")) {
						//11、举报
						Intent clickIntent = new Intent(context,
								MallActivity.class);
						
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("market")) {
						//12、积分商城
						Intent clickIntent = new Intent(context,
								MallActivity.class);
//						showNotification(context,"提示","积分商城有新动向",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("addanswer")) {
						// 13、我提问有新回答  14、我的提问 的回答有新互动 16、我的回答 有新互动 OK
						Intent clickIntent = new Intent(context, CommunicatActivity.class);
						/*Bundle args=new Bundle();*/
						//args.putInt("type", 1);
						int qid = contentJson.optInt("qid");
						int aid = contentJson.optInt("aid");
						String nickname=contentJson.optString("nickname");
						Ask ask = new Ask();
						ask.setId(qid);
						int uid=AppContext.instance().getLoginUid();
						ask.setUid(uid);
						ask.setnickname(nickname);
						
						Comment comment = new Comment();
						comment.setId(aid);
						comment.setAid(aid); 
						comment.setqid(qid);
						comment.setauid(contentJson.optInt("auid"));
						comment.setnickname(nickname);
						clickIntent.putExtra("type", 1);
						clickIntent.putExtra("ask",ask);
						clickIntent.putExtra("comment", comment);
						//clickIntent.putExtras(bundle);
//						showNotification(context,"提示","有新回答",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("addanswerafter")) {
						 // 15、我的提问 的回答有新追问 // 17、我的回答 有新追问 OK
						Intent clickIntent = new Intent(context, CommunicatActivity.class);
						clickIntent.putExtra("type", 2);
						Bundle args=new Bundle();
						args.putInt("type", 2);
						int aid = contentJson.optInt("aid");
						int qid = contentJson.optInt("qid");
						int auid=contentJson.optInt("auid");
						String nickname=contentJson.optString("nickname");
						
						Ask ask = new Ask();
						ask.setId(qid);
						int uid=AppContext.instance().getLoginUid();
						ask.setUid(uid);
						ask.setnickname(nickname);
						
						Comment comment = new Comment();
						comment.setId(aid);
						comment.setAid(aid);
						comment.setqid(qid);
						comment.setauid(auid);
						comment.setnickname(nickname);
						
						clickIntent.putExtra("title", contentJson.optString("aftername", ""));
						clickIntent.putExtra("ask",ask);
						clickIntent.putExtra("comment", comment);
						
//						showNotification(context,"提示","有新追问",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("adopted")) {
						 // 18、我的回答 被采纳 
						Intent clickIntent = new Intent(context, SimpleBackActivity.class);
						clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.Answer.getValue());
						
//						showNotification(context,"提示","回答被采纳",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					}
					else if (type.equals("adopt")) {
						 // 18、我的回答 被采纳 
					//	Intent clickIntent = new Intent(context, SimpleBackActivity.class);
						Ask ask = new Ask();
						ask.setId(contentJson.optInt("id",0));
						
						Intent clickIntent = new Intent(context, TweetDetailActivity.class);
						clickIntent.putExtra("ask", ask);
						
						/*clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.Question.getValue());
						intent.putExtra("aid", json.optInt("id",0));
						intent.putExtra("isPersonalInfo", false);*/
						
//						showNotification(context,"提示","回答被采纳",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("signjob")) {
						showNotification(context,"签到提醒","亲，今天还没有签到哦！");
					}else if (type.equals("adoptjob")) {//add by wuyue
						// 18、我的回答 被采纳
						Ask ask = new Ask();
						ask.setId(contentJson.optInt("id",0));
						Intent clickIntent = new Intent(context, TweetDetailActivity.class);
						clickIntent.putExtra("ask", ask);

						showNotification(context,"采纳提醒","有人回复你的问题，请采纳!",clickIntent);
					}
					else if(type.equals("questionkey")||type.equals("questionadd")){
						//27、提问后，问题涉及某个关键词 // 22、用户提问未通过审核通知
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"));
					}else if (type.equals("fanshelp")) {
						// 19、我有新的粉丝求助
						Intent clickIntent = new Intent(context, SimpleBackActivity.class);
						clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.Fensiqiuzhu.getValue());

						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("assistantinfo")) {
						// 20、注册登陆成功后
						Intent clickIntent = new Intent(context, SimpleBackActivity.class);
						clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.MYZHUSHOUID.getValue());

						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("upgrade")) {
						// 21、用户升级，发送系统消息
						Intent clickIntent = new Intent(context,
								MallActivity.class);
						
//						showNotification(context,"提示","实名认证审核通",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					} else if (type.equals("exchange")||type.equals("attentionlist")
							||type.equals("registration")) {
						// 23、积分商城，用户兑换学习资料
						// 24、系统消息：若用户的关注对象 ≤ 10人
						// 25、培训信息-我要报名，提交报名信息后
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"));
					}  else if (type.equals("invitation")) {
						// 26、某人填写某个助手号注册成功后
						Intent clickIntent = new Intent(context, SimpleBackActivity.class);
						clickIntent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, SimpleBackPage.guanzhuxiaoxi.getValue());
//						showNotification(context,"提示","由我推荐的注册成功",clickIntent);
						showNotification(context,json.optString("notifytitle"),json.optString("notifycontent"),clickIntent);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				payloadData.append(data);
				payloadData.append("\n");
			}
			break;
		// 获取透传数据
		// String appid = bundle.getString("appid");
		// byte[] payload = bundle.getByteArray("payload");
		//
		// String taskid = bundle.getString("taskid");
		// String messageid = bundle.getString("messageid");
		//
		// // smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
		// boolean result =
		// PushManager.getInstance().sendFeedbackMessage(context, taskid,
		// messageid, 90001);
		// System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));
		//
		// if (payload != null) {
		// String data = new String(payload);
		// Log.d("GetuiSdkDemo", "Got Payload:" + data);
		//
		// Bundle b = null;
		// String title = "", content="";
		//
		// try {
		// //
		// {"notifytitle":"1111","notifycontent":"2222","content":"{"type":"daliy"}"}
		//
		// JSONObject json=new JSONObject(data);
		// title=json.optString("notifytitle",
		// context.getString(R.string.app_name));
		// content=json.optString("notifycontent", "收到一条消息");
		// // 解析内容部分
		// json = new JSONObject(json.optString("content"));
		// String type=json.optString("type");
		// String id=json.optString("id");
		//
		// b = new Bundle();
		// b.putString("id", id);
		// b.putString("type", type);
		// } catch (JSONException e) {}
		//
		// // 显示推送通知
		// showNotification(context, title, content, b);
		// }

		// String type = bundle.getString("type");
		// Toast.makeText(context, type, Toast.LENGTH_SHORT).show();
		// System.out.println(type);

		// break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			if (TextUtils.isEmpty(cid)){
				cid = bundle.getString("clientid");
				AppContext.mClientId = cid;
			}else{
 				AppContext.mClientId = cid;
			}
			

			Log.i("GetuiSdkDemo", "Got ClientID:" + cid);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp");
			 * 
			 * Log.d("GetuiSdkDemo", "appid = " + appid); Log.d("GetuiSdkDemo",
			 * "taskid = " + taskid); Log.d("GetuiSdkDemo", "actionid = " +
			 * actionid); Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;
		default:
			break;
		}
	}

	public void showNotification(Context context, String title, String content,
			Bundle bundle) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.app_icon)
				.setContentTitle(title).setContentText(content);

		Intent intent = new Intent(context, MainActivity.class);
		intent.putExtras(bundle);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);

		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);

		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify((int) System.currentTimeMillis(), mBuilder.build());
	}
	
	public void showNotification(Context context, String title, String content) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.app_icon)
				.setContentTitle(title).setContentText(content);

		int notifyId=(int) System.currentTimeMillis();
		
		Intent resultIntent = new Intent();
	    resultIntent.setAction(CLEAR_NOTI_ACTION);
	    resultIntent.putExtra("notifyId", notifyId);
	    
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,  
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);  

		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);

		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify(notifyId, mBuilder.build());
	}

	public void showNotification(Context context, String title, String content,
			Intent intent) {
/*	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.app_icon)
				.setContentTitle(title).setContentText(content);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,  
                intent, PendingIntent.FLAG_UPDATE_CURRENT);  
		
		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify((int) System.currentTimeMillis(), mBuilder.build());
*/
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.app_icon)
				.setContentTitle(title).setContentText(content);
		Random random = new Random();
		int num =random.nextInt(Integer.MAX_VALUE);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(context,num,  
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		mBuilder.setContentIntent(pendingIntent);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		
		NotificationManager mgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mgr.notify((int) System.currentTimeMillis(), mBuilder.build());
		
	}

}
