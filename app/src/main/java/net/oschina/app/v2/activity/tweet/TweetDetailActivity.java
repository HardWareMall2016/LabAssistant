package net.oschina.app.v2.activity.tweet;

import java.util.HashMap;
import java.util.Map;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.ImageShowerActivity;
import net.oschina.app.v2.activity.MainActivity;
import net.oschina.app.v2.activity.tweet.adapter.TweetAnswerAdapter;
import net.oschina.app.v2.activity.tweet.fragment.TweetAnswerListFragment;
import net.oschina.app.v2.activity.tweet.fragment.TweetDetailFragment;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker.OnMediaPickerListener;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseActivity;
import net.oschina.app.v2.base.Constants;
import net.oschina.app.v2.emoji.EmojiEditText;
import net.oschina.app.v2.emoji.SupperListActivity;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;
import net.oschina.app.v2.model.User;
import net.oschina.app.v2.model.event.AdoptSuccEvent;
import net.oschina.app.v2.ui.dialog.CommonDialog;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.ui.dialog.WaitDialog;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.LabelUtils;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.TDevice;
import net.oschina.app.v2.utils.UIHelper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMQQSsoHandler;

import de.greenrobot.event.EventBus;

/**
 * 问题详细界面
 *
 * @author Johnny
 */
public class TweetDetailActivity extends BaseActivity {

    private boolean isFirstComming = true;
    private boolean visibility_Flag = false;

    public String mediaUri = "";
    public String superlist = "";
    public String url;

    private ShareHelper shareHelper;
    private TweetDetailFragment f;
    private ExtendMediaPicker pickView;
    private TweetAnswerListFragment answerListFragment;

    private View mTextView;
    private View mBottomView;
    private Button mButton;
    private TextView tv_zhuiwen, mTvActionTitle;
    private LinearLayout ll_tweet_detail;
    private EmojiEditText mEtInput;
    private ImageButton mBtnEmoji, mBtnMore;
    private Button mBtnSend;
    private TextView mTvAsk, mTvRank, mTvTime, mTvTitle, mTvCommentCount,mTvHits,mTvFromContent;
    private ImageView mIvPic;
    private RelativeLayout reward_layout;
    private ImageView pic_reward;
    private TextView tv_reward;
    protected View tip_layout;
    protected View tip_close;
    private Ask ask;
    private LinearLayout mPopMenuContent;
    private PopupWindow mPopupWindow;
    private InputMethodManager imm ;
    private boolean isOpen ;

    private EmojiEditText editText;

    private HashMap<Integer, UserBean> userList;


    @Override
    protected int getLayoutId() {
        return R.layout.tweet_detail_layout;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    protected void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = inflateView(R.layout.v2_actionbar_custom_back_title_more);
        View back = view.findViewById(R.id.btn_back);
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View tv_back_title = view.findViewById(R.id.tv_back_title);
        if (null != tv_back_title) {

            tv_back_title.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        mTvActionTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);
        View moreBtn = view.findViewById(R.id.tv_actionbar_right_more);
        moreBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onMore();
            }
        });
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(view, params);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        shareHelper = new ShareHelper(this);
        pickView = new ExtendMediaPicker(this);

        mTvAsk = (TextView) findViewById(R.id.tv_byask);
        mTvRank = (TextView) findViewById(R.id.tv_byrank);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvPic = (ImageView) findViewById(R.id.iv_pic);
        mTvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        mTvHits = (TextView)findViewById(R.id.tv_readnum);
        mTvFromContent = (TextView)findViewById(R.id.tv_from_content);

        tv_zhuiwen = (TextView) findViewById(R.id.tv_zhuiwen);
        mBottomView = findViewById(R.id.bottomview);
        mButton = (Button) findViewById(R.id.button);

        intiPopMenu();

        /*LinearLayout contentView=(LinearLayout)findViewById(R.id.ll_tweet_detail);
        contentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener(){
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //如果弹框还在，判断在底部的view是否底部确定输入法是否关闭
                if(mPopupWindow!=null&&mPopupWindow.isShowing()){
                    int[] location = new int[2];
                    mTextView.getLocationOnScreen(location);
                    int y = location[1];
                    int bottomLocation=y+mTextView.getHeight();
                    if(DeviceUtils.getScreenHeight(getApplication())==bottomLocation){
                        closePopWin();
                    }
                }
            }
        });*/

        imm = (InputMethodManager)getSystemService(Service.INPUT_METHOD_SERVICE);

        mTextView = findViewById(R.id.ly_bottom);
        mBtnEmoji = (ImageButton) findViewById(R.id.btn_emoji);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnMore = (ImageButton) findViewById(R.id.btn_more);
        mEtInput = (EmojiEditText) findViewById(R.id.et_input);
        mEtInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {

                EmojiEditText editText = ((EmojiEditText) v);
                //if (null != editText.getmHeaderUnDelete()) {
                if (!TextUtils.isEmpty(editText.getmHeaderUnDelete())) {
                    int index = editText.getmHeaderUnDelete().length();
                    String text=editText.getText().toString();
                    if (index > editText.getSelectionStart()&&index<text.length()) {
                        editText.setSelection(index);
                    }
                }
            }

                /*mPopMenuContent = (LinearLayout)getLayoutInflater().inflate(R.layout.tweet_detail_editbox_layout, null);
                mPopupWindow.setContentView(mPopMenuContent);

                editText = (EmojiEditText) mPopMenuContent.findViewById(R.id.editbox_input);
                TextView mSend = (TextView) mPopMenuContent.findViewById(R.id.tv_dialog_send);
                TextView mBack = (TextView) mPopMenuContent.findViewById(R.id.tv_dialog_back);

                mBack.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closePopWin();
                    }
                });


                mSend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isFastClick()) {
                            return ;
                        }

                        String text = editText.getText().toString();
                        //editText.getText().clear();

                        if (!TextUtils.isEmpty(toSomeone)) {
                            text = text.replaceAll(toSomeone, "");
                        }
                        if (TextUtils.isEmpty(text)) {
                            AppContext.showToastShort(R.string.tip_comment_content_empty);
                            return;
                        }
                        if (!TDevice.hasInternet()) {
                            AppContext.showToastShort(R.string.tip_network_error);
                            return;
                        }
                        int id = ask.getId();
                        int uid = AppContext.instance().getLoginUid();
                        boolean relation;// 是否@高手
                        if (TextUtils.isEmpty(superlist)) {
                            relation = false;
                        } else {
                            relation = true;
                        }
                        showWaitDialog();
                        NewsApi.subComment(id, uid, false, text, relation, superlist,
                                msubHandler);
                        closePopWin();
                        hideWaitDialog();
                    }
                });

                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                showPopMenu();

                editText.requestFocus();*/
        });

        reward_layout = (RelativeLayout) findViewById(R.id.reward_layout);
        pic_reward = (ImageView) findViewById(R.id.pic_reward);
        tv_reward = (TextView) findViewById(R.id.tv_reward);

        // 填问题数据
        ask = (Ask) getIntent().getSerializableExtra("ask");
        NewsApi.getAskById(ask.getId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                try {
                    if (response.getString("desc").equals("success")) {

                        try {
                            ask = Ask.parse(new JSONObject(response
                                    .getString("data")));
                            fillUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//		fillUI();

        tip_layout = findViewById(R.id.tip_layout);
        tip_close = findViewById(R.id.tip_close);
        tip_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tip_layout.setVisibility(View.GONE);
            }
        });
        // 如果没有登录的话
        if (!AppContext.instance().isLogin()) {
            showLogin();
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();

        answerListFragment = new TweetAnswerListFragment();
        answerListFragment.setArguments(getIntent().getExtras());
        trans.add(R.id.container, answerListFragment);

        trans.commit();

        mBtnMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pickView.showPickerView();
            }
        });

        if (AppContext.instance().isLogin()) {
            User user = AppContext.instance().getLoginInfo();
            if (user.getRealname_status() == 1) {// 已认证
                mBtnEmoji.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TweetDetailActivity.this,
                                SupperListActivity.class);
                        intent.putExtra("supperList", userList);
                        startActivityForResult(intent, 1000);
                    }
                });
            } else {
                mBtnEmoji.setImageDrawable(getResources().getDrawable(
                        R.drawable.at_forbid));
                mBtnEmoji.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppContext.showToast("需要认证");
                    }
                });
            }
        } else {
            mBtnEmoji.setImageDrawable(getResources().getDrawable(
                    R.drawable.at_forbid));
            mBtnEmoji.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppContext.showToast("需要认证");
                }
            });
        }

        EventBus.getDefault().register(this);
    }

    private static long lastClickTime;

    private boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private void intiPopMenu() {
         /* 第一个参数弹出显示view 后两个是窗口大小 */
        mPopupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        /* 设置背景显示 */
        int bgColor = getResources().getColor(R.color.popup_background);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(bgColor));
        /* 设置触摸外面时消失 */
        mPopupWindow.setOutsideTouchable(false);
        /* 设置系统动画 */
        mPopupWindow.setAnimationStyle(R.style.pop_menu_animation);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.update();
        mPopupWindow.setTouchable(true);
        /* 设置点击menu以外其他地方以及返回键退出 */
        mPopupWindow.setFocusable(true);
    }

    public boolean closePopWin() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            return true;
        }
        return false;
    }

    private void showPopMenu() {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            /* 最重要的一步：弹出显示 在指定的位置(parent) 最后两个参数 是相对于 x / y 轴的坐标 */
            mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            backgroundAlpha(0.7f);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 刷新
     *
     * @param
     */
    public void onEventMainThread(AdoptSuccEvent adoptSuccEvent) {
        if (ask.getId() == adoptSuccEvent.askId) {
            changeAskState();
        }
    }

    private JsonHttpResponseHandler msubHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                if (response.getInt("code") == 88) {
                    reset();
                    answerListFragment.adapter.clear();
                    answerListFragment.sendRequestData();
                    AppContext.showToast("回答成功");
                } else {
                    AppContext.showToast("回答失败");
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        @Override
        public void onFinish() {
            closeSendWaitDialog();
            super.onFinish();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            closeSendWaitDialog();
            super.onSuccess(statusCode, headers, responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            closeSendWaitDialog();
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    };

    private JsonHttpResponseHandler mCommentAfterHandler = new JsonHttpResponseHandler() {

        @Override
        public void onSuccess(int statusCode, Header[] headers,
                              JSONObject response) {
            try {
                if (response.getInt("code") == 88) {
                    reset();
                    answerListFragment.adapter.clear();
                    answerListFragment.sendRequestData();
                    AppContext.showToast("追问成功");
                } else {
                    AppContext.showToast("追问失败");
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    };

    public void showTiWen() {
        mButton.setTag(2);
        mButton.setText("继续回答");
        mBottomView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
    }

    public void showZhuiwen() {
        mEtInput.setHint("追问");
        mEtInput.setHintTextColor(getResources().getColor(
                R.color.font_zhuiwen_back));
        mBottomView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }

    public void reset() {
        TDevice.hideSoftKeyboard(mEtInput);

        if (mEtInput != null) {
            //mEtInput.getText().clear();
            mEtInput.clearFocus();
            mEtInput.setHint(R.string.publish_comment);
            mEtInput.setHintTextColor(getResources().getColor(
                    R.color.font_zhuiwen_back));
            // mEtInput.setTextColor();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_more:
                onMore();
                break;
        }
        return true;
    }


    private String toSomeone;

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            userList = (HashMap<Integer, UserBean>) intent
                    .getSerializableExtra("supperList");
            if (userList != null) {
                StringBuilder builder = new StringBuilder();
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Integer, UserBean> entry : userList.entrySet()) {
                    UserBean user = entry.getValue();
                    builder.append(user.getId() + ",");
                    sb.append("@" + user.getNickname() + ";");


                }
                if (builder.length() > 0) {
                    builder = builder.deleteCharAt(builder.length() - 1);
                }
                this.superlist = builder.toString();
                toSomeone = sb.toString();
                mEtInput.setHeaderUnDelete(sb.toString());

            }
        }
        pickView.onActivityResult(requestCode, resultCode, intent);
    }

    boolean isAllowShare = true;

    protected void onMore() {
        int item; // 自己提的问题、举报 隐藏不显示
        if (ask.getUid() == AppContext.instance().getLoginUid()) {
            item = R.array.app_bar_item;
        } else {
            item = R.array.app_bar_items;
        }

        final CommonDialog dialog = DialogHelper
                .getPinterestDialogCancelable(this);
        dialog.setTitle(R.string.title_more);
        dialog.setItemsWithoutChk(
                getResources().getStringArray(item),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        dialog.dismiss();
                        if (position == 0) {
                            NewsApi.getQuestionShareUrl(ask.getId(),
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode,
                                                              Header[] headers,
                                                              JSONObject response) {
                                            try {
                                                if (response.getInt("code") == 88) {
                                                    JSONObject dataObject = new JSONObject(
                                                            response.getString("data"));
                                                    url = dataObject
                                                            .optString("url");

                                                } else {
                                                }

                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });
                        }
                        goToSelectItem(position);
                    }
                });
        dialog.show();
    }

    private void goToSelectItem(int position) {

        switch (position) {
            case 0:
                final CommonDialog dialogShare = DialogHelper
                        .getPinterestDialogCancelable(this);
                dialogShare.setTitle(R.string.title_share);
                dialogShare.setItemsWithoutChk(

                        getResources().getStringArray(R.array.app_share_items),
                        new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {

                                if (position == 0) {
                                    shareHelper.shareToSinaWeibo(
                                            "分享问题" + ask.getContent(),
                                            url,
                                            "http://ws.shiyanzhushou.com/Uploads/share_icon.png");
                                } else if (position == 1) {
                                    shareHelper.shareToWeiChat(
                                            "分享问题",
                                            ask.getContent(),
                                            url,
                                            "http://ws.shiyanzhushou.com/Uploads/share_icon.png");
                                } else if (position == 2) {
                                    shareHelper.shareToWeiChatCircle(
                                            "分享问题\n"+ask.getContent(),
                                            ask.getContent(),
                                            url,
                                            "http://ws.shiyanzhushou.com/Uploads/share_icon.png");
                                } else if(position == 3){
                                    shareHelper.shareToQQ(
                                            "分享问题",
                                            ask.getContent(),
                                            url,
                                            "http://ws.shiyanzhushou.com/Uploads/share_icon.png",
                                            TweetDetailActivity.this);
                                }else if(position == 4){
                                    shareHelper.shareToQZone(
                                            "分享问题",
                                            ask.getContent(),
                                            url,
                                            "http://ws.shiyanzhushou.com/Uploads/share_icon.png",
                                            TweetDetailActivity.this);

                                }/*else if (position == 5) {

                                    NewsApi.shareQuestion(ask.getId(), AppContext.instance().getLoginUid(), new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers,
                                                              JSONObject response) {
                                            try {
                                                int code = response.getInt("code");
                                                if (code == 88) {
                                                    Toast.makeText(getApplicationContext(), "分享成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "分享失败", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }*/

                                dialogShare.dismiss();
                                // goToSelectItem(position);
                            }
                        });
                dialogShare.show();
                break;
            case 1:
                if (!AppContext.instance().isLogin()) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("type", "login");
                    startActivity(intent);
                } else {
                    if (ask.getUid() == AppContext.instance().getLoginUid()) {
                        SharedPreferences sharedPreferences= getSharedPreferences("test",
                                Activity.MODE_PRIVATE);
                        //String name =sharedPreferences.getString("name", "");
                        //if("0".equals(name)){
                            dodelect();//直接删除
                       // }/*else{
                          //  commitPlayDelect();// 申请删除
                       // }*/

                    }else {
                        doReport();
                    }

                }
                break;
        }
    }


    protected void commitPlayDelect(){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.play_delect_dialog);
        TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
        titleTv.setText("申请删除");
        // 设置监听
        Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
        zhichi.setText("提交");
        // 支持
        zhichi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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


    protected void dodelect() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.zhichi_dialog);
        TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
        titleTv.setText("删除后将无法恢复，是否确定删除");
        // 设置监听
        Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
        zhichi.setText("确定");
        // 支持
        zhichi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int uid = AppContext.instance().getLoginUid();
                int qid = ask.getId();
                NewsApi.delectQuestion(uid, qid,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode,
                                                  Header[] headers, JSONObject response) {
                                String str = "删除成功！";
                                try {
                                    int code = response.getInt("code");
                                    if (code != 88) {
                                        str = response.getString("desc");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                AppContext.showToast(str);
                                finish();

                            }
                        });
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

    protected void doReport() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.zhichi_dialog);
        TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
        titleTv.setText("是否举报");
        // 设置监听
        Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
        zhichi.setText("确定");
        // 支持
        zhichi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppContext.instance().isLogin()) {
                    Intent intent = new Intent(TweetDetailActivity.this,
                            MainActivity.class);
                    intent.putExtra("type", "login");
                    startActivity(intent);
                } else {
                    int uid = AppContext.instance().getLoginUid();
                    NewsApi.reportQuestion(uid, ask.getId(),
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode,
                                                      Header[] headers, JSONObject response) {
                                    String str = "举报成功";

                                    try {
                                        int code = response.getInt("code");
                                        if (code != 88) {
                                            str = response.getString("desc");
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }


                                    AppContext.showToast(str);
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

    private void fillUI() {
        mTvTime.setText(ask.getinputtime());
        mTvCommentCount
                .setText(getString(R.string.comment_count, ask.getanum()));
        mTvHits.setText(ask.getHits()+"");

        String supper = ask.getsuperlist();
        if (!TextUtils.isEmpty(supper) && !"null".equals(supper)) {


            mTvAsk.setText(Html.fromHtml("邀请 <font color=#2FBDE7>" + supper
                    + "</font> 回答"));
            mTvAsk.setVisibility(View.VISIBLE);
        } else {
            mTvAsk.setVisibility(View.GONE);
        }
        String label = ask.getLabel();
        if (TextUtils.isEmpty(label)) {
            label = "暂无";
            mTvFromContent.setText(Html.fromHtml(":<font color=#2FBDE7>" + label
                    + "</font>"));
        }else{
            mTvFromContent.setText(LabelUtils.parseLable(label));
            int labelBackgroundId= LabelUtils.getBgResIdByLabel(label);
            mTvFromContent.setBackgroundResource(labelBackgroundId);
        }
       /* mTvRank.setText(Html.fromHtml("标签:<font color=#2FBDE7>" + label
                + "</font>"));*/
        if (!StringUtils.isEmpty(ask.getImage()) && "null" != ask.getImage()) {
            mIvPic.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(
                    ApiHttpClient.getImageApiUrl(ask.getImage()), mIvPic);

            mIvPic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TweetDetailActivity.this,
                            ImageShowerActivity.class);
                    intent.putExtra("imageUri", ask.getImage());
                    startActivity(intent);
                }
            });

        } else {
            mIvPic.setVisibility(View.GONE);
        }
        if (AppContext.instance().isLogin()) {
            if (ask.getUid() == AppContext.instance().getLoginUid()) {
                mTvActionTitle.setText("我的提问");
                tip_layout.setVisibility(View.VISIBLE);
            } else {
                mTvActionTitle.setText(ask.getnickname() + "的提问");
            }
        } else {
            mTvActionTitle.setText(ask.getnickname() + "的提问");
        }

        if (ask.getreward() != 0) {
            //reward_layout.setVisibility(View.VISIBLE);
            //tv_reward.setText(String.valueOf(ask.getreward()));
            //mTvTitle.setText("" + ask.getContent());
            String content = " " + ask.getreward() + " " + ask.getContent();
            int length = ("" + ask.getreward()).length();
            SpannableString spanString = new SpannableString(" " + ask.getreward() + ask.getContent());
            Drawable d = getResources().getDrawable(R.drawable.dollar);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan forColor = new ForegroundColorSpan(getResources().getColor(R.color.reward));
            spanString.setSpan(forColor, 1, 1 + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvTitle.setText(spanString);

        } else {
            reward_layout.setVisibility(View.GONE);
            mTvTitle.setText("" + ask.getContent());
        }

    }

    public void showTiWenMe() {
        mButton.setTag(2);
        mButton.setText("继续回答");
        mBottomView.setVisibility(View.GONE);
        mTextView.setVisibility(View.GONE);
    }

    public void bottomBack() {
        answerListFragment.handleBottomView();
    }

    /**
     * 底部显示【回答】界面
     */
    public void showAnswer() {
        if (ask.getUid() == AppContext.instance().getLoginUid()) {// 自己的提问
            mTextView.setVisibility(View.GONE);
            return;
        }
        mBtnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (visibility_Flag) {
                    mMoreLinear.setVisibility(View.GONE);
                    visibility_Flag = false;
                } else {
                    mMoreLinear.setVisibility(View.VISIBLE);
                    visibility_Flag = true;
                }*/
                String text = mEtInput.getText().toString();
				if(!TextUtils.isEmpty(toSomeone)){
					text=text.replaceAll(toSomeone, "");
				}
				
				if (TextUtils.isEmpty(text)) {
					AppContext
							.showToastShort(R.string.tip_comment_content_empty);
					return;
				}
				if (!TDevice.hasInternet()) {
					AppContext.showToastShort(R.string.tip_network_error);
					return;
				}

                mEtInput.getText().clear(); //快速按确定按钮会出现多条回复

				int id = ask.getId();
				int uid = AppContext.instance().getLoginUid();
				boolean relation;// 是否@高手
				if (TextUtils.isEmpty(superlist)) {
					relation = false;
				} else {
					relation = true;
				}
                showSendWaitDialog();
				NewsApi.subComment(id, uid, false, text, relation, superlist,
						msubHandler);
            }
        });
        pickView.setOnMediaPickerListener(new MyMediaPickerListener(1, null));
        tv_zhuiwen.setVisibility(View.GONE);
        mBottomView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
    }

    private WaitDialog mWaitDialog;

    private void showSendWaitDialog(){
        closeSendWaitDialog();
        mWaitDialog=DialogHelper.getWaitDialog(this,"正在发送中...");
        mWaitDialog.show();
    }

    private void closeSendWaitDialog(){
        if(mWaitDialog!=null&&mWaitDialog.isShowing()){
            mWaitDialog.dismiss();
            mWaitDialog=null;
        }
    }

    /**
     * 底部显示【补充回答】界面
     */
    public void showReAnswer(final Comment comment) {
        mButton.setText("补充回答");
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showReplyCommunicat(TweetDetailActivity.this, ask,
                        comment);
            }
        });
        mBottomView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }

    /**
     * 底部显示【追问】界面
     */
    public void showTraceAsk(final Comment comment) {
        showReTraceAsk(comment);

        // tv_zhuiwen.setVisibility(View.VISIBLE);
        // mBtnSend.setOnClickListener(new OnClickListener() {
        // @Override
        // public void onClick(View v) {
        // String text = mEtInput.getText().toString();
        // if (TextUtils.isEmpty(text)) {
        // AppContext
        // .showToastShort(R.string.tip_comment_content_empty);
        // return;
        // }
        // if (!TDevice.hasInternet()) {
        // AppContext.showToastShort(R.string.tip_network_error);
        // return;
        // }
        // int id = ask.getId();
        // int uid = AppContext.instance().getLoginUid();
        // NewsApi.addCommentAfter(id, uid, comment.getId(), text,
        // mCommentAfterHandler);
        // }
        // });
        // pickView.setOnMediaPickerListener(new MyMediaPickerListener(2,
        // comment));
        // mBottomView.setVisibility(View.GONE);
        // mTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 底部显示【参与讨论】界面
     */
    public void showReTraceAsk(final Comment comment) {
        mButton.setText("参与讨论");
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showReAnswerCommunicat(TweetDetailActivity.this, ask,
                        comment);
            }
        });
        mBottomView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }

    /**
     * 底部显示【点击登录】界面
     */
    public void showLogin() {
        mButton.setText("点击登录");
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLoginView(TweetDetailActivity.this);
            }
        });
        mBottomView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }

    /**
     *
     */
    public void changeAskState() {
        ask.setissolveed(1);
        ((TweetAnswerAdapter) answerListFragment.adapter).setAdoptState(1);
        refreshList();
    }

    class MyMediaPickerListener implements OnMediaPickerListener {
        private int type;// 1:回答,2:追问
        private Comment comment;

        public MyMediaPickerListener(int type, Comment comment) {
            this.type = type;
            this.comment = comment;
        }

        @Override
        public void onSelectedMediaChanged(String mediaUri) {
            showSendWaitDialog();
            NewsApi.uploadImage(3, mediaUri, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    try {
                        switch (msg.what) {
                            case 1:
                                String returnStr = msg.getData()
                                        .getString("return");
                                JSONObject jsonObject = new JSONObject(returnStr);
                                String imageUrl = jsonObject.getString("url");
                                int id = ask.getId();
                                int uid = AppContext.instance().getLoginUid();
                                if (type == 1) {
                                    NewsApi.subComment(id, uid, false, null,
                                            imageUrl, false, null, msubHandler);
                                } else if (type == 2) {
                                    NewsApi.addCommentAfter(id, uid, 0,
                                            comment.getId(), 0, null, imageUrl,
                                            mCommentAfterHandler);
                                }
                                break;
                            case 2:
                                AppContext.showToast("发送图像失败");
                                break;
                            default:
                                break;
                        }
                        closeSendWaitDialog();
                        System.out.println(msg.what);
                    } catch (Exception e) {
                        e.printStackTrace();
                        closeSendWaitDialog();
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstComming) {
            refreshList();
        }
        isFirstComming = false;
    }

    ;


    public void refreshList() {
        answerListFragment.adapter.clear();

        answerListFragment.sendRequestData();
    }
}
