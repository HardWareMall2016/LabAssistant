package net.oschina.app.v2.activity.tweet;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.find.fragment.ShowTitleDetailActivity;
import net.oschina.app.v2.activity.news.view.ShareDialog;
import net.oschina.app.v2.activity.news.view.ShareDialog.OnSharePlatformClick;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shiyanzhushou.app.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.socialize.media.UMImage;
public class ShareHelper {

	private Context context;

	private UMSocialService mController;

	public ShareHelper(Context context) {
		this.context = context;
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.getConfig().closeToast();
	}

	/**
	 * 
	 * 发送短信
	 * 
	 * @param smsBody
	 */
	public void shareToSms(String smsBody) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		context.startActivity(intent);
	}

	public void shareContent(SHARE_MEDIA media, String url) {
		// mController.setShareContent("实验助手分享");
		mController.setShareContent(url);
		mController.directShare(context, media, new SnsPostListener() {

			@Override
			public void onStart() {
				//AppContext.showToastShort(R.string.tip_start_share);
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				//AppContext.showToastShort(R.string.tip_share_done);
			}
		});
	}

	public void shareContent(SHARE_MEDIA media, String title, String url,
			String img) {
		mController.setShareContent(title + url);
		mController.setShareImage(new UMImage(context, img));
		mController.directShare(context, media, new SnsPostListener() {

			@Override
			public void onStart() {
//				AppContext.showToastShort(R.string.tip_start_share);
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
//				AppContext.showToastShort(R.string.tip_share_done);
			}
		});
	}
	
	public void handleShare(final String title,final String url,
			final String thumb,final Activity activity) {
		final ShareDialog dialog = new ShareDialog(context);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle(R.string.share_to);
		dialog.setOnPlatformClickListener(new OnSharePlatformClick() {

			@Override
			public void onPlatformClick(SHARE_MEDIA media) {
				switch (media) {
				case SINA:
					shareToSinaWeibo(title, url,
					ApiHttpClient.getImageApiUrl(thumb));
					break;
				case WEIXIN:
					shareToWeiChat(title, title, url,
							ApiHttpClient.getImageApiUrl(thumb));
					break;
				case WEIXIN_CIRCLE:
					shareToWeiChatCircle(title, title, url,
							ApiHttpClient.getImageApiUrl(thumb));
					break;
			    case QQ:
			    	shareToQQ(title, title, url,
							ApiHttpClient.getImageApiUrl(thumb),activity);
					break;
				case QZONE:
					shareToQZone(title, title, url,
							ApiHttpClient.getImageApiUrl(thumb),activity);
					break;  
				default:
					break;
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public void shareToQQ(String shareTitle, String shareContent,
			String shareUrl, String shareImg,Activity activity) { 

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
				Constants.QQ_APPID, Constants.QQ_APPKEY);
		qqSsoHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		//设置分享文字
		qqShareContent.setShareContent(shareContent);
		//设置分享title
		qqShareContent.setTitle(shareTitle);
		//设置分享图片
		qqShareContent.setShareImage(new UMImage(context, shareImg));
		//设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(shareUrl);
		mController.setShareMedia(qqShareContent);

		mController.postShare(context, SHARE_MEDIA.QQ, new SnsPostListener() {
			@Override
			public void onStart() {
				AppContext.showToastShort(R.string.tip_start_share);
			}

			@Override
			public void onComplete(SHARE_MEDIA arg0, int arg1,
					SocializeEntity arg2) {
				AppContext.showToastShort(R.string.tip_share_done);
			}
		});
		
	}


	public void shareToQZone(String shareTitle, String shareContent,
			String shareUrl, String shareImg,Activity activity) {
	
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				Constants.QQ_APPID, Constants.QQ_APPKEY);
		qZoneSsoHandler.addToSocialSDK();
		
		QZoneShareContent qzone = new QZoneShareContent();
		//设置分享文字
		qzone.setShareContent(shareContent);
		//设置点击消息的跳转URL
		qzone.setTargetUrl(shareUrl);
		//设置分享内容的标题
		qzone.setTitle(shareTitle);
		//设置分享图片
		qzone.setShareImage(new UMImage(context, shareImg));
		mController.setShareMedia(qzone);
		
		mController.postShare(context, SHARE_MEDIA.QZONE,
				new SnsPostListener() {

					@Override
					public void onStart() {
						AppContext.showToastShort(R.string.tip_start_share);
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							SocializeEntity arg2) {
						AppContext.showToastShort(R.string.tip_share_done);
					}
				});
				
	}

	public void shareToSinaWeibo(final String url) {
		// 设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		if (OauthHelper.isAuthenticated(context, SHARE_MEDIA.SINA)) {
			shareContent(SHARE_MEDIA.SINA, url);
		} else {
			mController.doOauthVerify(context, SHARE_MEDIA.SINA,
					new UMAuthListener() {

						@Override
						public void onStart(SHARE_MEDIA arg0) {
						}

						@Override
						public void onError(SocializeException arg0,
								SHARE_MEDIA arg1) {
						}

						@Override
						public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
							shareContent(SHARE_MEDIA.SINA, url);
						}

						@Override
						public void onCancel(SHARE_MEDIA arg0) {
						}
					});
		}
	}

	public void shareToSinaWeibo(final String content, final String url, final String img) {
		// 设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		if (OauthHelper.isAuthenticated(context, SHARE_MEDIA.SINA)) {
			shareContent(SHARE_MEDIA.SINA, content,url,img);
		} else {
			mController.doOauthVerify(context, SHARE_MEDIA.SINA,
					new UMAuthListener() {

						@Override
						public void onStart(SHARE_MEDIA arg0) {
						}

						@Override
						public void onError(SocializeException arg0,
								SHARE_MEDIA arg1) {
						}

						@Override
						public void onComplete(Bundle arg0, SHARE_MEDIA arg1) {
							shareContent(SHARE_MEDIA.SINA, content,url,img);
						}

						@Override
						public void onCancel(SHARE_MEDIA arg0) {
						}
					});
		}
	}

	public void shareToWeiChat(String shareTitle, String shareContent,
			String shareUrl) {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context,
				Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET);
		wxHandler.addToSocialSDK();
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(shareContent);
		// 设置title
		weixinContent.setTitle(shareTitle);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(shareUrl);
		// 设置分享图片
		// weixinContent.setShareImage(new UMImage(context,
		// "http://www.cn-csec.com/templates/default/images/at5.jpg"));
		mController.setShareMedia(weixinContent);
		mController.postShare(context, SHARE_MEDIA.WEIXIN,
				new SnsPostListener() {

					@Override
					public void onStart() {
						 AppContext.showToastShort(R.string.tip_start_share);
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							SocializeEntity arg2) {
						if (arg1 == 200) {
							AppContext.showToastShort(R.string.tip_share_done);
						} else {
							Toast.makeText(context,"分享失败 : error code : " + arg1, Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	public void shareToWeiChat(String shareTitle, String shareContent,
			String shareUrl, String shareImg) {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context,
				Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET);
		wxHandler.addToSocialSDK();
		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(shareContent);
		// 设置title
		weixinContent.setTitle(shareTitle);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(shareUrl);
		// 设置分享图片
		weixinContent.setShareImage(new UMImage(context, shareImg));
		mController.setShareMedia(weixinContent);
		mController.postShare(context, SHARE_MEDIA.WEIXIN,
				new SnsPostListener() {

					@Override
					public void onStart() {
						AppContext.showToastShort(R.string.tip_start_share);
					}


					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
										   SocializeEntity arg2) {
						Log.e("------->>>>",arg1+"");
						if (arg1 == 200) {
							AppContext.showToastShort(R.string.tip_share_done);
						} else {
							Toast.makeText(context,"分享失败 : error code : " + arg1, Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

	public void shareToWeiChatCircle(String shareTitle, String shareContent,
			String shareUrl) {
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContent);
		// 设置朋友圈title
		circleMedia.setTitle(shareTitle);
		// circleMedia.setShareImage(localImage);
		circleMedia.setTargetUrl(shareUrl);
		mController.setShareMedia(circleMedia);
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {

					@Override
					public void onStart() {
						// AppContext.showToastShort(R.string.tip_start_share);
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							SocializeEntity arg2) {
						// AppContext.showToastShort(R.string.tip_share_done);
					}
				});
	}

	public void shareToWeiChatCircle(String shareTitle, String shareContent,
			String shareUrl, String shareImg) {
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContent);
		// 设置朋友圈title
		circleMedia.setTitle(shareTitle);
		circleMedia.setShareImage(new UMImage(context, shareImg));
		circleMedia.setTargetUrl(shareUrl);
		mController.setShareMedia(circleMedia);
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {

					@Override
					public void onStart() {
						// AppContext.showToastShort(R.string.tip_start_share);
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							SocializeEntity arg2) {
						// AppContext.showToastShort(R.string.tip_share_done);
					}
				});
	}
	
	public void shareToWeiChatCircle(String shareTitle, String shareContent,
			String shareUrl, int shareImg) {
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context,
				Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareContent);
		// 设置朋友圈title
		circleMedia.setTitle(shareTitle);
		circleMedia.setShareImage(new UMImage(context, shareImg));
		circleMedia.setTargetUrl(shareUrl);
		mController.setShareMedia(circleMedia);
		mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
				new SnsPostListener() {

					@Override
					public void onStart() {
						// AppContext.showToastShort(R.string.tip_start_share);
					}

					@Override
					public void onComplete(SHARE_MEDIA arg0, int arg1,
							SocializeEntity arg2) {
						// AppContext.showToastShort(R.string.tip_share_done);
					}
				});
	}


	


	
	
}
