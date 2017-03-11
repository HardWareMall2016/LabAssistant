package net.oschina.app.v2.activity.tweet.adapter;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.ImageShowerActivity;
import net.oschina.app.v2.activity.comment.model.CommentReply;
import net.oschina.app.v2.activity.tweet.CommunicatActivity;
import net.oschina.app.v2.activity.tweet.fragment.CommunicatFragment;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.ui.dialog.DialogHelper;
import net.oschina.app.v2.ui.dialog.WaitDialog;
import net.oschina.app.v2.utils.FileDownloadHandler;
import net.oschina.app.v2.utils.HttpRequestUtils;
import net.oschina.app.v2.utils.ImageLoderOptionUtil;
import net.oschina.app.v2.utils.PathUtils;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shiyanzhushou.app.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigDecimal;

/**
 * 互动交流Adapter
 *
 * @author Johnny
 */
public class CommunicatAdapter extends ListBaseAdapter implements
        OnClickListener {
    private Activity context;
    private CommunicatFragment mFrag;

    private static final int LEFT_TYPE = 1;
    private static final int RIGHT_TYPE = 2;

    private DisplayImageOptions options;
    private int type;
    //private int aid ;
    private int mIsadopt;

    private WaitDialog mWaitDialog;

    private final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    private String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end.equals("")) return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private void openFile(File file) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        try {
            context.startActivity(intent);
        } catch (Exception ex) {
            AppContext.showToast("该文件无法打开");
        }
    }

    public WaitDialog showWaitDialog(Activity context, String message) {

        if (mWaitDialog == null) {
            mWaitDialog = DialogHelper.getWaitDialog(context, message);
        }
        if (mWaitDialog != null) {
            mWaitDialog.setMessage(message);
            mWaitDialog.show();
        }
        return mWaitDialog;
    }

    public CommunicatAdapter(Activity context, CommunicatFragment frag, int type, int isadopt) {
        this.context = context;
        this.type = type;
        this.mIsadopt = isadopt;
        mFrag = frag;
    }

    public int getViewType(int position) {
        int uid = AppContext.instance().getLoginUid();
        CommentReply c = (CommentReply) _data.get(position);
        if (c != null && c.getAuid() == uid) {
            return RIGHT_TYPE;
        }
        return LEFT_TYPE;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private String getExtName(String fileName) {
        String extention = "";
        if (!StringUtils.isEmpty(fileName)) {  //--截取文件名
            int i = fileName.lastIndexOf(".");
            if (i > -1 && i < fileName.length()) {
                extention = fileName.substring(i + 1); //--扩展名
            }
        }
        return extention;
    }

    private void showAttachment(CommentReply itemModel) {
        String extention = getExtName(itemModel.getFilename());
        if (TextUtils.isEmpty(extention)) {
            return;
        }

        String filePath = getAttachmentFilePath(itemModel.getId(), extention);
        if (filePath == null) {
            return;
        }
        File attachmentFile = new File(filePath);
        if (attachmentFile.exists()) {
            openFile(attachmentFile);
            return;
        }

        mWaitDialog = showWaitDialog(context, "正在下载...");
        String fileUrl = ApiHttpClient.getImageApiUrl(itemModel.getFileurl());
        HttpRequestUtils.downloadFile(fileUrl, filePath, new FileDownloadHandler() {
            @Override
            public void onDownloadFailed(String errorMsg) {
                super.onDownloadFailed(errorMsg);
                AppContext.showToast("下载失败");
            }

            @Override
            public void onDownloadSuccess(File downFile) {
                if (downFile != null && downFile.exists()) {
                    openFile(downFile);
                }
                super.onDownloadSuccess(downFile);
            }

            @Override
            public void onRequestFinished() {
                super.onRequestFinished();
                if (mWaitDialog != null) {
                    mWaitDialog.dismiss();
                    mWaitDialog = null;
                }
            }
        });
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        options = ImageLoderOptionUtil.buldDisplayImageOptionsForAvatar();
        final ViewHolder viewHolder;
        int viewType = getViewType(position);

        if (convertView != null && convertView.getTag() != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.communicat_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.leftAttachmentsContent = convertView.findViewById(R.id.left_attachments_content);
            viewHolder.leftAttachmentsName = (TextView) convertView.findViewById(R.id.left_attachments_name);
            viewHolder.leftAttachmentsSize = (TextView) convertView.findViewById(R.id.left_attachments_size);
            viewHolder.leftAttachmentsType = (TextView) convertView.findViewById(R.id.left_attachments_type);

            viewHolder.rightAttachmentsContent = convertView.findViewById(R.id.right_attachments_content);
            viewHolder.rightAttachmentsName = (TextView) convertView.findViewById(R.id.right_attachments_name);
            viewHolder.rightAttachmentsSize = (TextView) convertView.findViewById(R.id.right_attachments_size);
            viewHolder.rightAttachmentsType = (TextView) convertView.findViewById(R.id.right_attachments_type);


            // 左侧气泡
            viewHolder.LeftChatContentLayout = convertView
                    .findViewById(R.id.left_chat_content_layout);
            viewHolder.leftChatLayout = (RelativeLayout) convertView
                    .findViewById(R.id.left_chat_layout);
            viewHolder.leftUserPhoto = (ImageView) convertView
                    .findViewById(R.id.left_chat_userPhoto);
            viewHolder.leftUserName = (TextView) convertView
                    .findViewById(R.id.left_user_name);
            viewHolder.leftContentTxt = (TextView) convertView
                    .findViewById(R.id.left_chat_content_txt);
            viewHolder.leftContentImage = (ImageView) convertView
                    .findViewById(R.id.left_chat_conent_image);
            viewHolder.leftTime = (TextView) convertView
                    .findViewById(R.id.left_chat_listitem_time);
            viewHolder.leftContentImageBar = (ProgressBar) convertView.findViewById(R.id.left_chat_conent_image_progressbar);

            // 右侧气泡
            viewHolder.rightChatContentLayout = convertView
                    .findViewById(R.id.right_chat_content_layout);
            viewHolder.rightChatLayout = convertView
                    .findViewById(R.id.right_chat_layout);
            viewHolder.rightUserPhoto = (ImageView) convertView
                    .findViewById(R.id.right_chat_userPhoto);
            viewHolder.rightUserName = (TextView) convertView
                    .findViewById(R.id.right_user_name);
            viewHolder.rightContentTxt = (TextView) convertView
                    .findViewById(R.id.right_chat_content_txt);
            viewHolder.rightContentImage = (ImageView) convertView
                    .findViewById(R.id.right_chat_conent_image);
            viewHolder.rightTime = (TextView) convertView
                    .findViewById(R.id.right_chat_listitem_time);
            viewHolder.rightContentImageBar = (ProgressBar) convertView.findViewById(R.id.right_chat_conent_image_progressbar);

            viewHolder.leftUserPhoto.setOnClickListener(this);
            viewHolder.rightUserPhoto.setOnClickListener(this);

            convertView.setTag(viewHolder);

        }


        viewHolder.leftContentTxt.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                showPopUp((TextView) arg0, 1);
                return true;
            }
        });

        //0 为第一条。1不是第一条
        viewHolder.rightContentTxt.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                showPopUp((TextView) arg0, viewHolder.flag);
                return true;
            }
        });

        viewHolder.rightContentImage.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                showPopUpImageView((ImageView) arg0);
                return true;
            }
        });

        final CommentReply itemModel = (CommentReply) _data.get(position);

        viewHolder.rightContentTxt.setTag(itemModel);
        viewHolder.rightContentImage.setTag(itemModel);
        //aid = itemModel.getId();
        if (viewType == LEFT_TYPE) {
            viewHolder.leftTime.setVisibility(View.VISIBLE);
            viewHolder.leftChatLayout.setVisibility(View.VISIBLE);
            viewHolder.rightTime.setVisibility(View.GONE);
            viewHolder.rightChatLayout.setVisibility(View.GONE);

            if (StringUtils.isEmpty(itemModel.getFileurl())) {
                viewHolder.leftAttachmentsContent.setVisibility(View.GONE);
            } else {
                viewHolder.leftAttachmentsContent.setVisibility(View.VISIBLE);
                viewHolder.leftAttachmentsContent.setTag(itemModel);
                viewHolder.leftAttachmentsName.setText(itemModel.getFilename());

                String extention = getExtName(itemModel.getFilename());
                viewHolder.leftAttachmentsType.setText(extention);
                String filePath = getAttachmentFilePath(itemModel.getId(), extention);
                if (filePath != null) {
                    File file = new File(filePath);
                    String size = bytes2kb(file.length());
                    viewHolder.leftAttachmentsSize.setText(size);
                } else {
                    viewHolder.leftAttachmentsSize.setText(null);
                }

                viewHolder.leftAttachmentsContent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentReply itemModel = (CommentReply) v.getTag();
                        showAttachment(itemModel);
                    }
                });
            }

            if (type == 2 && position != 0 && position != 1) {
                viewHolder.leftContentTxt.setVisibility(View.VISIBLE);
                viewHolder.leftContentTxt.setText("");
                viewHolder.leftContentTxt.append("回复");

                final String name = itemModel.getAskname();
                SpannableString sb = new SpannableString(name);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
                sb.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.leftContentTxt.append(sb);
                viewHolder.leftContentTxt.append("： " + itemModel.getContent());


                viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_green_bg);
                if (StringUtils.isEmpty(itemModel.getImage())) {
                    viewHolder.leftContentImage.setVisibility(View.GONE);
                } else {
                    viewHolder.leftContentImage.setVisibility(View.VISIBLE);
                    String contentImg = ApiHttpClient.getImageApiUrl(itemModel
                            .getImage());
                    ImageLoader.getInstance().displayImage(contentImg,
                            viewHolder.leftContentImage, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    viewHolder.leftContentImageBar.setProgress(0);
                                    viewHolder.leftContentImageBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    viewHolder.leftContentImageBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    viewHolder.leftContentImageBar.setVisibility(View.GONE);
                                }
                            }
                    );
                    viewHolder.leftContentImage
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context,
                                            ImageShowerActivity.class);
                                    intent.putExtra("imageUri",
                                            itemModel.getImage());
                                    context.startActivity(intent);
                                }
                            });
                }
            } else {
                viewHolder.leftContentTxt.setVisibility(View.VISIBLE);

                if (position == 0) {
                    viewHolder.leftContentTxt.setText("问题：" + itemModel.getContent());
                    if(!TextUtils.isEmpty(itemModel.getNewcontent())){
                        Spanned content= Html.fromHtml("<br/><font color=#2FBDE7>问题补充：" + itemModel.getNewcontent() + "</font>");
                        viewHolder.leftContentTxt.append(content);
                    }
                    viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_white_bg);
                } else {
                    if (position == 1) {
                        viewHolder.leftContentTxt.setText("回答：" + itemModel.getContent());
                        viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_blue_bg);
                        if(!TextUtils.isEmpty(itemModel.getNewcontent())){
                            Spanned content= Html.fromHtml("<br/><font color=#2FBDE7>问题补充：" + itemModel.getNewcontent() + "</font>");
                            viewHolder.leftContentTxt.append(content);
                        }
                    } else {
                        viewHolder.leftContentTxt.setText(itemModel.getContent());

                        CommentReply c = (CommentReply) _data.get(position);

                        if (itemModel.getSign() == 1) {
                            viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_white_bg);
                        } else if (itemModel.getSign() == 2) {
                            viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_blue_bg);
                        }


                    }
                }
                if (!StringUtils.isEmpty(itemModel.getImage())) {
                    viewHolder.leftContentImage.setVisibility(View.VISIBLE);
                    String contentImg = ApiHttpClient.getImageApiUrl(itemModel
                            .getImage());
                    ImageLoader.getInstance().displayImage(contentImg,
                            viewHolder.leftContentImage, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    viewHolder.leftContentImageBar.setProgress(0);
                                    viewHolder.leftContentImageBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    viewHolder.leftContentImageBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    viewHolder.leftContentImageBar.setVisibility(View.GONE);
                                }
                            }
                    );
                    viewHolder.leftContentImage
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context,
                                            ImageShowerActivity.class);
                                    intent.putExtra("imageUri",
                                            itemModel.getImage());
                                    context.startActivity(intent);
                                }
                            });
                } else {
                    viewHolder.leftContentImage.setVisibility(View.GONE);
                }
            }

            viewHolder.leftTime.setText(itemModel.getIntputtime());
            viewHolder.leftUserName.setText(itemModel.getNickname());
            String imagePath = ApiHttpClient
                    .getImageApiUrl(itemModel.getHead());
            ImageLoader.getInstance().displayImage(imagePath,
                    viewHolder.leftUserPhoto);

            viewHolder.leftUserPhoto.setTag(itemModel);


            if (type == 2) {
                if (position == 0) {
                    viewHolder.leftChatLayout
                            .setOnClickListener(null);
                } else {
                    //viewHolder.leftChatLayout
                    viewHolder.leftContentTxt
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ((CommunicatActivity) context).directPerson(
                                            itemModel.getNickname(),
                                            itemModel.getAuid(), itemModel.getId());

                                }
                            });
                }

            } else {
                viewHolder.leftChatLayout
                        .setOnClickListener(null);
            }

        } else if (viewType == RIGHT_TYPE) {
            viewHolder.leftTime.setVisibility(View.GONE);
            viewHolder.leftChatLayout.setVisibility(View.GONE);
            viewHolder.rightTime.setVisibility(View.VISIBLE);
            viewHolder.rightChatLayout.setVisibility(View.VISIBLE);

            if (StringUtils.isEmpty(itemModel.getFileurl())) {
                viewHolder.rightAttachmentsContent.setVisibility(View.GONE);
            } else {
                viewHolder.rightAttachmentsContent.setVisibility(View.VISIBLE);
                viewHolder.rightAttachmentsContent.setTag(itemModel);
                viewHolder.rightAttachmentsName.setText(itemModel.getFilename());

                String extention = getExtName(itemModel.getFilename());
                viewHolder.rightAttachmentsType.setText(extention);
                String filePath = getAttachmentFilePath(itemModel.getId(), extention);
                if (filePath != null) {
                    File file = new File(filePath);
                    String size = bytes2kb(file.length());
                    viewHolder.rightAttachmentsSize.setText(size);
                } else {
                    viewHolder.rightAttachmentsSize.setText(null);
                }

                viewHolder.rightAttachmentsContent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommentReply itemModel = (CommentReply) v.getTag();
                        showAttachment(itemModel);
                    }
                });
            }

            if (type == 2 && position != 0 && position != 1) {
                viewHolder.flag = 1;
                viewHolder.rightChatContentLayout.setBackgroundResource(R.drawable.chat_right_green_bg);
                viewHolder.rightContentTxt.setVisibility(View.VISIBLE);
                viewHolder.rightContentTxt.setText("");
                viewHolder.rightContentTxt.append("回复");
                final String name = itemModel.getAskname();
                SpannableString sb = new SpannableString(name);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLUE);
                sb.setSpan(colorSpan, 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.rightContentTxt.append(sb);
                viewHolder.rightContentTxt.append("： " + itemModel.getContent());

                if (StringUtils.isEmpty(itemModel.getImage())) {
                    viewHolder.rightContentImage.setVisibility(View.GONE);
                } else {
                    viewHolder.rightContentImage.setVisibility(View.VISIBLE);

                    String contentImg = ApiHttpClient.getImageApiUrl(itemModel
                            .getImage());
                    ImageLoader.getInstance().displayImage(contentImg,
                            viewHolder.rightContentImage, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    viewHolder.rightContentImageBar.setProgress(0);
                                    viewHolder.rightContentImageBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    viewHolder.rightContentImageBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    viewHolder.rightContentImageBar.setVisibility(View.GONE);
                                }
                            }
                    );
                    viewHolder.rightContentImage
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context,
                                            ImageShowerActivity.class);
                                    intent.putExtra("imageUri",
                                            itemModel.getImage());
                                    context.startActivity(intent);
                                }
                            });


                }
            } else {
                viewHolder.rightChatContentLayout.setBackgroundResource(R.drawable.chat_right_blue_bg);
                if (!StringUtils.isEmpty(itemModel.getImage())) {
                    viewHolder.rightContentImage.setVisibility(View.VISIBLE);
                    String contentImg = ApiHttpClient.getImageApiUrl(itemModel
                            .getImage());
                    ImageLoader.getInstance().displayImage(contentImg,
                            viewHolder.rightContentImage, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    viewHolder.rightContentImageBar.setProgress(0);
                                    viewHolder.rightContentImageBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    viewHolder.rightContentImageBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    viewHolder.rightContentImageBar.setVisibility(View.GONE);
                                }
                            }
                    );
                    viewHolder.rightContentImage
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context,
                                            ImageShowerActivity.class);
                                    intent.putExtra("imageUri",
                                            itemModel.getImage());
                                    context.startActivity(intent);
                                }
                            });
                }
                if (StringUtils.isEmpty(itemModel.getContent())) {
                    viewHolder.rightContentTxt.setVisibility(View.GONE);

                } else {
                    viewHolder.rightContentTxt.setVisibility(View.VISIBLE);
                    if (StringUtils.isEmpty(itemModel.getImage())) {
                        viewHolder.rightContentImage.setVisibility(View.GONE);
                    } else {
                        viewHolder.rightContentImage.setVisibility(View.VISIBLE);
                    }


                    if (position == 0) {
                        viewHolder.rightContentTxt.setText("问题：" + itemModel.getContent());
                        if(!TextUtils.isEmpty(itemModel.getNewcontent())){
                            Spanned content= Html.fromHtml("<br/><font color=#2FBDE7>问题补充：" + itemModel.getNewcontent() + "</font>");
                            viewHolder.rightContentTxt.append(content);
                        }
                        viewHolder.flag = 1;
                    } else {
                        if (position == 1) {
                            viewHolder.rightContentTxt.setText("回答：" + itemModel.getContent());
                            if(!TextUtils.isEmpty(itemModel.getNewcontent())){
                                Spanned content= Html.fromHtml("<br/><font color=#2FBDE7>问题补充：" + itemModel.getNewcontent() + "</font>");
                                viewHolder.rightContentTxt.append(content);
                            }
                            viewHolder.flag = 0;
                        } else {
                            viewHolder.rightContentTxt.setText(itemModel.getContent());
                            viewHolder.flag = 1;
                        }
                    }
                }
            }
            if (position == 0) {
                viewHolder.rightChatContentLayout.setBackgroundResource(R.drawable.chat_right_white_bg);
            }

            viewHolder.rightTime.setText(itemModel.getIntputtime());
            viewHolder.rightUserName.setText(itemModel.getNickname());
            String imagePath = ApiHttpClient
                    .getImageApiUrl(itemModel.getHead());
            ImageLoader.getInstance().displayImage(imagePath,
                    viewHolder.rightUserPhoto);
            viewHolder.rightUserPhoto.setTag(itemModel);
        }

        return convertView;
    }

    private void showPopUpImageView(ImageView v) {
        LinearLayout layout = new LinearLayout(v.getContext());
        layout.setBackgroundResource(R.drawable.popbackground);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, 20, 0, 0);

        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.v2_activity_communicat_popup, null);
        layout.addView(view);

        TextView tv = (TextView) view.findViewById(R.id.pop_layout_copy);
        tv.setVisibility(View.GONE);
        TextView mdelect = (TextView) view.findViewById(R.id.pop_layout_delet);

        CommentReply itemModel = (CommentReply) v.getTag();
        mdelect.setTag(itemModel);


        final PopupWindow popupWindow = new PopupWindow(layout, 120, 140);

        mdelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsadopt == 1) {
                    AppContext.showToast("被采纳回答无法删除");
                } else {
                    int uid = AppContext.instance().getLoginUid();
                    final CommentReply itemModel = (CommentReply) v.getTag();
                    NewsApi.delectAnswer(uid, itemModel.getId(),
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode,
                                                      Header[] headers, JSONObject response) {
                                    String str = "删除成功！";

                                    try {
                                        int code = response.getInt("code");
                                        if (code != 88) {
                                            str = response.getString("desc");
                                        } else {
                                            removeDescItem(itemModel.getId());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    AppContext.showToast(str);
                                }
                            });
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + (v.getWidth() >> 1), location[1] - popupWindow.getHeight());
    }

    private void showPopUp(TextView v, final int flag) {
        final String textContent = v.getText().toString();
        LinearLayout layout = new LinearLayout(v.getContext());
        layout.setBackgroundResource(R.drawable.popbackground);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, 20, 0, 0);

        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.v2_activity_communicat_popup, null);
        layout.addView(view);

        TextView tv = (TextView) view.findViewById(R.id.pop_layout_copy);
        TextView mdelect = (TextView) view.findViewById(R.id.pop_layout_delet);
        /*TextView modify = (TextView) view.findViewById(R.id.pop_layout_modify);*/

        CommentReply itemModel = (CommentReply) v.getTag();
        if (itemModel == null) {
            mdelect.setVisibility(View.GONE);
        } else {
            mdelect.setVisibility(View.VISIBLE);
            mdelect.setTag(itemModel);
        }

        final PopupWindow popupWindow = new PopupWindow(layout, 120, itemModel == null ? 140 : 240);

        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                //将文本数据复制到剪贴板
                cm.setText(textContent);

                Toast.makeText(arg0.getContext(), "复制成功！", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });

        mdelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mIsadopt == 1) {
                    AppContext.showToast("被采纳回答无法删除");
                } else {
                    if (flag == 0) {
                        final AlertDialog dialog = new AlertDialog.Builder(context).create();
                        dialog.show();
                        Window window = dialog.getWindow();
                        window.setContentView(R.layout.play_delect_dialog);
                        TextView titleTv = (TextView) window.findViewById(R.id.tv_title);
                        titleTv.setText("是否确认删除所有回答内容");
                        // 设置监听
                        Button zhichi = (Button) window.findViewById(R.id.ib_zhichi);
                        zhichi.setText("确定");
                        zhichi.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view_zhichi) {
                                int uid = AppContext.instance().getLoginUid();
                                final CommentReply itemModel = (CommentReply) v.getTag();
                                NewsApi.delectAnswer(uid, itemModel.getId(),
                                        new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode,
                                                                  Header[] headers, JSONObject response) {
                                                String str = "删除成功！";
                                                try {
                                                    int code = response.getInt("code");
                                                    if (code != 88) {
                                                        str = response.getString("desc");
                                                    } else {
                                                        //removeDescItem(itemModel.getId());
                                                        mFrag.refreshCommunicat();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                AppContext.showToast(str);
                                            }
                                        });
                                dialog.dismiss();
                            }
                        });
                        Button chakanzhichi = (Button) window
                                .findViewById(R.id.ib_chakanzhichi);
                        chakanzhichi.setText("取消");
                        chakanzhichi.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        popupWindow.dismiss();
                    } else {

                        int uid = AppContext.instance().getLoginUid();
                        final CommentReply itemModel = (CommentReply) v.getTag();
                        NewsApi.delectAnswer(uid, itemModel.getId(),
                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode,
                                                          Header[] headers, JSONObject response) {
                                        String str = "删除成功！";
                                        try {
                                            int code = response.getInt("code");
                                            if (code != 88) {
                                                str = response.getString("desc");
                                                //removeDescItem(aid);
                                            } else {
                                                removeDescItem(itemModel.getId());
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        AppContext.showToast(str);
                                    }
                                });
                    }
                }
                popupWindow.dismiss();
            }
        });
      /*  modify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + (v.getWidth() >> 1), location[1] - popupWindow.getHeight());
    }

    private void removeDescItem(int aid) {
        CommentReply removeItem = null;
        for (int i = 0; i < _data.size(); i++) {
            CommentReply itemModel = (CommentReply) _data.get(i);
            if (itemModel.getId() == aid) {
                removeItem = itemModel;
                break;
            }
        }
        if (removeItem != null) {
            _data.remove(removeItem);
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
        View leftChatLayout, rightChatLayout, LeftChatContentLayout, rightChatContentLayout;
        ImageView leftUserPhoto, rightUserPhoto;
        TextView leftUserName, rightUserName, leftContentTxt, rightContentTxt;
        ImageView leftContentImage, rightContentImage;
        TextView leftTime, rightTime;
        ProgressBar rightContentImageBar;
        ProgressBar leftContentImageBar;

        View leftAttachmentsContent;
        TextView leftAttachmentsName, leftAttachmentsSize, leftAttachmentsType;

        View rightAttachmentsContent;
        TextView rightAttachmentsName, rightAttachmentsSize, rightAttachmentsType;

        int flag;
    }

    private static File pdfPathDir = null;

    private String getAttachmentFilePath(int id, String fileExt) {
        if (pdfPathDir == null) {
            pdfPathDir = PathUtils.getExternalPDFFilesDir();
        }
        if (pdfPathDir != null) {
            String pdfPath = pdfPathDir.getAbsolutePath();
            return String.format("%s/%d.%s", pdfPath, id, fileExt);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        CommentReply c = (CommentReply) v.getTag();
        UIHelper.showUserCenter(context, c.getAuid(), c.getNickname());
    }

    /**
     * 设置可复制状态
     */
    public void setSelected(TextView textView) {
        textView.setFocusableInTouchMode(true);
        textView.setFocusable(true);
        textView.setClickable(true);
        textView.setLongClickable(true);
        textView.setMovementMethod(ArrowKeyMovementMethod.getInstance());
        textView.setText(textView.getText(), BufferType.SPANNABLE);
    }

}


