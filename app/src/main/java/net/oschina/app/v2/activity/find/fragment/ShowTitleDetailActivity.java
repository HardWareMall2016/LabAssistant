package net.oschina.app.v2.activity.find.fragment;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.BaoMingActivity;
import net.oschina.app.v2.activity.DiscussActivity;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.news.fragment.EmojiFragmentControl;
import net.oschina.app.v2.activity.news.fragment.ToolbarFragment;
import net.oschina.app.v2.activity.news.fragment.ToolbarFragmentControl;
import net.oschina.app.v2.activity.news.view.ShareDialog;
import net.oschina.app.v2.activity.news.view.ShareDialog.OnSharePlatformClick;
import net.oschina.app.v2.activity.tweet.ShareHelper;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.Constants;
import net.oschina.app.v2.emoji.EmojiFragment;
import net.oschina.app.v2.emoji.EmojiFragment.EmojiTextListener;
import net.oschina.app.v2.utils.FileDownloadCallback;
import net.oschina.app.v2.utils.FileDownloadHandler;
import net.oschina.app.v2.utils.FileDownloadListener;
import net.oschina.app.v2.utils.FileUtils;
import net.oschina.app.v2.utils.HttpRequestUtils;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.pdfview.PDFView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;
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

import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;

import java.io.File;


public class ShowTitleDetailActivity extends BaseActivity implements
        EmojiTextListener, EmojiFragmentControl, ToolbarFragmentControl {
    private TextView tv_td_title;
    private TextView tv_td_time;

    private ImageView detail_img;
    // private ImageView iv_td;
    private WebView wv_td;
    private PDFView pdfView ;
    private int mNewsId;
    private int mNewsType = 1;
    private String mNewsImg = null;
    private int fromSource = 0;
    private int fromTitle = -1;

    private View woyaobaoming;
    private View taolun;
    private View fenxiangTx, baomingTx;

    private StringEntity entity;
    private String data;
    private String thumb;
    private String title;
    private String description;
    private int inputtime;
    protected String content;
    private ShareHelper shareHelper;
    private String url;
    private boolean isAllowShare;

    private Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected int getActionBarTitle() {

        if (fromTitle > 0) {
            return fromTitle;
        }
        return super.getActionBarTitle();
    }

    private TextView textTitle;
    private ImageView shareBtn;

    protected void initActionBar(ActionBar actionBar) {
        if (actionBar == null)
            return;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = null;
        view = inflateView(R.layout.v2_actionbar_custom_view);

        textTitle = (TextView) view.findViewById(R.id.tv_ask_title);

        View btn_back = view.findViewById(R.id.btn_back);

        shareBtn = (ImageView) view.findViewById(R.id.btn_share);

        TextView tv_back = (TextView) view.findViewById(R.id.tv_back);

        tv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (textTitle.getText().toString().trim().equals("培训信息")) {
                    handleShare();
                } else {
                    doShareToCircle();
                }

            }
        });

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(view, params);
    }

    //分享到实验圈
    protected void doShareToCircle() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.zhichi_dialog);
        TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
        titleTv.setText("分享到实验圈？");
        // 设置监听
        Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
        zhichi.setText("确定");
        // 支持
        zhichi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppContext.instance().isLogin()) {
                    Intent intent = new Intent(ShowTitleDetailActivity.this,
                            MainActivity.class);
                    intent.putExtra("type", "login");
                    startActivity(intent);
                } else {
                    NewsApi.shareToCircle(AppContext.instance().getLoginUid(), mNewsId, mNewsType,
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode,
                                                      Header[] headers, JSONObject response) {
                                    System.out.println(response);
                                    AppContext.showToast("分享成功");
                                }
                            });
                }
                dialog.dismiss();
            }
        });
        // 查看支持者
        Button chakanzhichi = (Button) window
                .findViewById(R.id.ib_chakanzhichi);
        chakanzhichi.setText("取消");
        chakanzhichi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titledetail);
        shareHelper = new ShareHelper(this);
        Intent intent = getIntent();
        mNewsId = intent.getIntExtra("id", 1);
        mNewsType = intent.getIntExtra("type", 1);
        mNewsImg = intent.getStringExtra("img");
        fromSource = intent.getIntExtra("fromSource", 0);
        fromTitle = intent.getIntExtra("fromTitle", R.string.app_name);
        textTitle.setText(getText(fromTitle));
        if (("培训信息").equals(getText(fromTitle))) {
            shareBtn.setVisibility(View.VISIBLE);
        }

        tv_td_title = (TextView) findViewById(R.id.tv_title);
        tv_td_time = (TextView) findViewById(R.id.tv_time);
        detail_img = (ImageView) findViewById(R.id.detail_img);

        woyaobaoming = findViewById(R.id.woyaobaoming);
        taolun = findViewById(R.id.taolun);
        fenxiangTx = findViewById(R.id.fenxiang);
        baomingTx = findViewById(R.id.baoming);

        woyaobaoming.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = null;

                if (fromSource == 1) {
                    if (!AppContext.instance().isLogin()) {
                        AppContext.showToast("登录用户方可参与报名");

                        return;
                    }

                    intent = new Intent(ShowTitleDetailActivity.this,
                            BaoMingActivity.class);
                    intent.putExtra("id", mNewsId);
                    startActivity(intent);
                } else {
                    handleShare();
                }

            }
        });

        taolun.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTitleDetailActivity.this,
                        DiscussActivity.class);
                intent.putExtra("articleId", mNewsId);
                startActivity(intent);
            }
        });

        fenxiangTx.setVisibility(fromSource != 1 ? View.VISIBLE : View.GONE);
        baomingTx.setVisibility(fromSource == 1 ? View.VISIBLE : View.GONE);

        if (TextUtils.isEmpty(mNewsImg)) {
            detail_img.setVisibility(View.GONE);
        } else {
            detail_img.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(mNewsImg, detail_img);
        }

        // iv_td = (ImageView) findViewById(R.id.iv_td);
       // wv_td = (WebView) findViewById(R.id.wv_td);
        pdfView = (PDFView) findViewById(R.id.pdfview);
       /* wv_td.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        wv_td.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        wv_td.getSettings().setBuiltInZoomControls(true);
       //扩大比例的缩放
        wv_td.getSettings().setUseWideViewPort(true);
       //自适应屏幕
        wv_td.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_td.getSettings().setLoadWithOverviewMode(true);*/
        sendRequestData();

        mController.getConfig().closeToast();
    }

    protected void sendRequestData() {
        // mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        int uid = 0;
        if (AppContext.instance().isLogin() == true) {
            uid = AppContext.instance().getLoginUid();
        }
        NewsApi.getQuestionnairelist(uid, mNewsId, mNewsType, handler);
    }

    JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {

            data = response.optString("data");

            if (TextUtils.isEmpty(data)) {
                String tipContent = response.optString("desc");
                showTipsDialog(tipContent);

                return;
            }

            try {

                JSONObject obj = new JSONObject(data);
                thumb = obj.getString("thumb");
                title = obj.getString("title");
                content = obj.getString("url");
                url = content + "?rn=1";
                tv_td_title.setText(title);

                if (hasDownloadUrl(mNewsId+"")) {
                    Log.e("-222222--","------------");
                    display(mNewsId + ".pdf");
                }else{
                    Log.e("--------","------------");
                    LoadUrl(content.toString() + "?APP_VERSION=1",mNewsId + "");
                }
                //屏蔽下载按钮
               // wv_td.loadUrl(content.toString() + "?APP_VERSION=1");
                isAllowShare = obj.optInt("allow_share", 1) == 0 ? false : true;

            } catch (JSONException e) {
                e.printStackTrace();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    private void display(String s) {
        Log.e("---------ssss", s);
        /*pdfView.fromAsset(s)
                .load();*/
        pdfView.fromAsset(s)
                .pages(0, 2, 1, 3, 3, 3)
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                //.onDraw(onDrawListener)
                //.onLoad(onLoadCompleteListener)
                //.onPageChange(onPageChangeListener)
                .load();
    }


    private boolean hasDownloadUrl(String objectId) {
        File audioFile = new File(getAudioPath(objectId));
        return audioFile.exists();
    }

    private String getAudioPath(String objectId) {
        String celebrityPath = FileUtils.getExternalCelebrityFilesDir().getAbsolutePath();
        return celebrityPath + "/" + objectId + ".pdf";
    }


    private void LoadUrl(String url,String path) {
        Log.e("-----url--->",url);
        Log.e("-----本地路径--->",getAudioPath(path));
        //HttpRequestUtils.downloadFile(url, getAudioPath(path), mAudioDownloadListener);
        HttpRequestUtils.downloadFile("http://www.zhan.com/uploadfile/2016/03/30902_201603041631222yYlq.pdf", getAudioPath(path), mAudioDownloadListener);
    }

    public FileDownloadHandler mAudioDownloadListener = new FileDownloadHandler() {

        @Override
        public void onDownloadFailed(String errorMsg) {
            Log.e("-----errorMsg",errorMsg);
            deleteAudioFile(mNewsId+"");
        }

        @Override
        public void onNoNetwork() {
            deleteAudioFile(mNewsId+"");
        }

        @Override
        public void onCanceled() {
            deleteAudioFile(mNewsId+"");
        }

        @Override
        public void onDownloadSuccess(File downFile) {
            if (hasDownloadUrl(mNewsId + "")) {
                display(mNewsId + ".pdf");
            }
        }
    };

    private void deleteAudioFile(String objectId) {
        File audioFile = new File(getAudioPath(objectId));
        if (audioFile.exists()) {
            audioFile.delete();
        }
    }


    @Override
    public void setToolBarFragment(ToolbarFragment fragment) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setEmojiFragment(EmojiFragment fragment) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendClick(String text) {
        // TODO Auto-generated method stub

    }

    private void showTipsDialog(String tipContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(tipContent);
        builder.setCancelable(true);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.show();
    }

    // ////////////////////////////////////////////////////////////////
    // //////// share
    // ////////////////////////////////////////////////////////////////

    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    protected void ShareCount(int mType) {
        NewsApi.shareCount(AppContext.instance().getLoginUid(), mNewsId, mType,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          Header[] headers, JSONObject response) {
                        System.out.println(response);
                    }
                });
    }

    protected void handleShare() {
        if (!isAllowShare) {
            Toast.makeText(this, "该内容无法分享.", Toast.LENGTH_SHORT).show();
            return;
        }
        final ShareDialog dialog = new ShareDialog(ShowTitleDetailActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitle(R.string.share_to);
        dialog.setOnPlatformClickListener(new OnSharePlatformClick() {

            @Override
            public void onPlatformClick(SHARE_MEDIA media) {
                switch (media) {
                    case SINA:
                        shareHelper.shareToSinaWeibo(title, url,
                                //	ApiHttpClient.getImageApiUrl(mNewsImg));
                                ApiHttpClient.getImageApiUrl(thumb));
                        ShareCount(1);
                        break;
                    case WEIXIN:
                        shareHelper.shareToWeiChat(title, title, url,
//							ApiHttpClient.getImageApiUrl(mNewsImg));
                                ApiHttpClient.getImageApiUrl(thumb));
                        ShareCount(2);
                        break;
                    case WEIXIN_CIRCLE:
                        shareHelper.shareToWeiChatCircle(title, title, url,
                                ApiHttpClient.getImageApiUrl(thumb));
                        ShareCount(3);
                        break;
                    case QQ:
                    /*shareToQQ(title, title, url,
                            ApiHttpClient.getImageApiUrl(thumb));*/
                        shareHelper.shareToQQ(
                                title,
                                title,
                                url,
                                ApiHttpClient.getImageApiUrl(thumb),
                                ShowTitleDetailActivity.this);
                        ShareCount(4);
                        break;
                    case QZONE:
                        shareToQZone(title, title, url,
                                ApiHttpClient.getImageApiUrl(thumb));
                        ShareCount(5);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void shareToQQ(String shareTitle, String shareContent,
                           String shareUrl, String shareImg) {

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
                Constants.QQ_APPID, Constants.QQ_APPKEY);
        qqSsoHandler.setTargetUrl(shareUrl);
        qqSsoHandler.setTitle(shareContent);


        qqSsoHandler.addToSocialSDK();
        mController.setShareContent(shareContent);

        mController.postShare(this, SHARE_MEDIA.QQ, new SnsPostListener() {

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

    private void shareToQZone(String shareTitle, String shareContent,
                              String shareUrl, String shareImg) {
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
                Constants.QQ_APPID, Constants.QQ_APPKEY);


        QZoneShareContent qzone = new QZoneShareContent();
        // 设置分享文字
        qzone.setShareContent(shareTitle);
        // 设置点击消息的跳转URL
        qzone.setTargetUrl(shareUrl);
        // 设置分享内容的标题
        qzone.setTitle(shareContent);
        // 设置分享图片
        qzone.setShareImage(new UMImage(this, shareImg));
        mController.setShareMedia(qzone);

        qZoneSsoHandler.addToSocialSDK();

        mController.postShare(this, SHARE_MEDIA.QZONE,
                new SnsPostListener() {

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


    protected String getShareUrl() {
        // TODO

        return "";
    }

    protected String getShareTitle() {

        return getString(R.string.share_title);
    }

    protected String getShareContent() {
        // TODO
        return "";
    }

    private void shareContent(SHARE_MEDIA media) {
        mController.setShareContent("实验助手分享");
        mController.directShare(this, media, new SnsPostListener() {

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

}
