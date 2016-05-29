package net.oschina.app.v2.activity.settings.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.news.view.ShareDialog;
import net.oschina.app.v2.activity.news.view.ShareDialog.OnSharePlatformClick;
import net.oschina.app.v2.activity.tweet.ShareHelper;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.base.Constants;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.ui.dialog.CommonDialog;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.utils.DownloadHelper;
import net.oschina.app.v2.utils.UIHelper;

import android.util.Log;
import android.view.View;

import org.apache.http.Header;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.logging.Handler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.app.Dialog;
import android.app.AlertDialog;
import android.view.ViewGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;


import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.umeng.update.UmengUpdateAgent;

public class SettingsFragment extends BaseFragment {

    Handler m_mainHandler;

    ShareHelper sp = null;

    private static final String SETTINGS_SCREEN = "settings_screen";
    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v2_fragment_settings, container,
                false);
        initViews(view);
        sp = new ShareHelper(getActivity());
        mController.getConfig().closeToast();
        return view;
    }

    @Override
    public void onDestroyView() {
        //UmengUpdateAgent.setUpdateListener(null);
        super.onDestroyView();
    }

    private void initViews(View view) {

        view.findViewById(R.id.ly_xiugainicheng).setOnClickListener(this);
        view.findViewById(R.id.ly_shimingrenzheng).setOnClickListener(this);
        view.findViewById(R.id.ly_xiugaimima).setOnClickListener(this);
        view.findViewById(R.id.ly_xiaoxishezhi).setOnClickListener(this);
        view.findViewById(R.id.ly_fenxianggeipengyou).setOnClickListener(this);
        //view.findViewById(R.id.ly_saoyisaoxiazai).setOnClickListener(this);
        view.findViewById(R.id.ly_bangdingshoujihao).setOnClickListener(this);
        view.findViewById(R.id.ly_guanyuruanjian).setOnClickListener(this);
        view.findViewById(R.id.ly_bangzhu).setOnClickListener(this);
        view.findViewById(R.id.ly_jianchaxinbanben).setOnClickListener(this);
        view.findViewById(R.id.ly_tuichuzhanghao).setOnClickListener(this);


    }

    protected String getShareUrl() {
        return "http://a.app.qq.com/o/simple.jsp?pkgname=com.shiyanzhushou.app&g_f=991653";
    }

    protected String getShareTitle() {
        return getString(R.string.share_title);
    }

    protected String getShareContent() {
        User u = AppContext.instance().getLoginInfo();
        if (null != u) {
            //sp.shareToWeiChat("", "我的助手ID号：" + u.getInvitation(), "");
            return "实验助手，实验室从业人员的专属社区，邀请码" + u.getInvitation() + "，赶快来加入我们吧！";
        }
        return "";
    }

    private void shareContent(SHARE_MEDIA media) {
        mController.setShareContent(getShareContent());
        mController.directShare(getActivity(), media, new SnsPostListener() {

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

    private void shareToWeiChatCircle() {
        sp.shareToWeiChatCircle(getShareContent(), getShareContent(), getShareUrl(), "");
    /*	// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),
				Constants.WEICHAT_APPID, Constants.WEICHAT_SECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(getShareContent());
		// 设置朋友圈title
		circleMedia.setTitle(getShareTitle());
		// circleMedia.setShareImage(localImage);
		circleMedia.setTargetUrl(getShareUrl());
		mController.setShareMedia(circleMedia);
		mController.postShare(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE,
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
				});*/
    }

    private void shareToWeiChat() {
        sp.shareToWeiChat("实验助手", getShareContent(), getShareUrl(), "");
    }

    private void shareToSinaWeibo() {
        // 设置腾讯微博SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        if (OauthHelper.isAuthenticated(getActivity(), SHARE_MEDIA.SINA)) {
            shareContent(SHARE_MEDIA.SINA);
        } else {
            mController.doOauthVerify(getActivity(), SHARE_MEDIA.SINA,
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
                            shareContent(SHARE_MEDIA.SINA);
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA arg0) {
                        }
                    });
        }
    }

    private void shareQQ() {
        sp.shareToQQ("实验助手", getShareContent(), getShareUrl(),
                "http://dl.iteye.com/upload/picture/pic/133287/9b6f8a1d-fe2f-3858-9423-484447c41908.png", getActivity());
    }

    private void shareQZONE(){
        sp.shareToQZone("实验助手", getShareContent(), getShareUrl(),
                "http://dl.iteye.com/upload/picture/pic/133287/9b6f8a1d-fe2f-3858-9423-484447c41908.png", getActivity());
    }
    protected void handleShare() {
//		if (TextUtils.isEmpty(getShareContent())
//				|| TextUtils.isEmpty(getShareUrl())) {
//			return;
//		}
        final ShareDialog dialog = new ShareDialog(getActivity());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setOnPlatformClickListener(new OnSharePlatformClick() {

            @Override
            public void onPlatformClick(SHARE_MEDIA media) {
                switch (media) {
                    case SINA:
                        shareToSinaWeibo();
                        break;
                    case WEIXIN:
                        shareToWeiChat();
                        break;
                    case WEIXIN_CIRCLE:
                        shareToWeiChatCircle();
                        break;
                    case QQ:
                        shareQQ();
                        break;
                    case QZONE:
                        shareQZONE();
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    @Override
    public void onClick(View v) {
        final int id = v.getId();


        if (id == R.id.ly_xiugainicheng) {
            UIHelper.showEditNickname(getActivity());
        } else if (id == R.id.ly_shimingrenzheng) {
            UIHelper.showShiMingRenZheng(getActivity());
        } else if (id == R.id.ly_xiugaimima) {
            UIHelper.showEditPassword(getActivity());
        } else if (id == R.id.ly_xiaoxishezhi) {
            UIHelper.showMessageSeting(getActivity());
        } else if (id == R.id.ly_fenxianggeipengyou) {
            handleShare();
        }/* else if (id == R.id.ly_saoyisaoxiazai) {
			UIHelper.showSaoYiSao(getActivity());
		} */ else if (id == R.id.ly_bangdingshoujihao) {
            UIHelper.showBangDingPhone(getActivity());
        } else if (id == R.id.ly_guanyuruanjian) {
            UIHelper.showAboutSoft(getActivity());
        } else if (id == R.id.ly_bangzhu) {
            UIHelper.showHelp(getActivity());
        } else if (id == R.id.ly_jianchaxinbanben) {
            UmengUpdateAgent.forceUpdate(getActivity());
        } else if (id == R.id.ly_tuichuzhanghao) {
            handleLogout();
        }
    }


    private void checkUpdate() {
        NewsApi.checkUpdate(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                if (response.optInt("code") == 88) {
                    String version = response.optString("version");
                    JSONObject json;
                    try {
                        json = new JSONObject(response.optString("data"));
                        int VerCode = json.optInt("vcode");
                        String fileUrl = json.optString("url");
                        // 比较版本更新
                        if (VerCode > AppContext.instance().getVerCode()) {
                            downFile(fileUrl);
                        } else {
                            AppContext.showToast("已经是最新版本");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void download(String fileUrl) {
        DownloadHelper download = new DownloadHelper(getActivity());
        download.startDownload("shiyanzhushou.apk", fileUrl);
    }

    private void downFile(final String url) {
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();

                    //  m_progressDlg.setMax((int)length);//设置进度条的最大值

                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(
                                Environment.getExternalStorageDirectory(),
                                "shiyanzhushou.apk");
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                                //        m_progressDlg.setProgress(count);
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 告诉HANDER已经下载完成了，可以安装了
     */
    private void down() {
        //	m_mainHandler.post(new Runnable() {
        //         public void run() {
        //      m_progressDlg.cancel();
        update();
        //         }
        //      });
    }

    /**
     * 安装程序
     */
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "shiyanzhushou.apk")),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


    private void handleLogout() {
        CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(getActivity());
        dialog.setMessage(R.string.message_logout);
        dialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Editor editor = AppContext.getPersistPreferences().edit();
                        editor.remove(AppContext.LAST_INPUT_PASSWORD);
                        editor.commit();

                        AppContext.instance().Logout();
                        AppContext.showToastShort(R.string.tip_logout_success);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("type", "login");
                        getActivity().startActivity(intent);

//						getActivity().finish();


                    }
                });
        dialog.setNegativeButton(R.string.cancle, null);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(SETTINGS_SCREEN);
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(SETTINGS_SCREEN);
        MobclickAgent.onPause(getActivity());
    }
}
