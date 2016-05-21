package net.oschina.app.v2.activity.tweet.adapter;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.ImageShowerActivity;
import net.oschina.app.v2.activity.comment.model.CommentReply;
import net.oschina.app.v2.activity.tweet.CommunicatActivity;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shiyanzhushou.app.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 互动交流Adapter
 *
 * @author Johnny
 */
public class CommunicatAdapter extends ListBaseAdapter implements
        OnClickListener {
    private Context context;

    private static final int LEFT_TYPE = 1;
    private static final int RIGHT_TYPE = 2;

    private int type;
    //private int aid ;

    public CommunicatAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
//        this.aid = aid ;
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

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView,
                               final ViewGroup parent) {
        final ViewHolder viewHolder;
        int viewType = getViewType(position);

        if (convertView != null && convertView.getTag() != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = getLayoutInflater(parent.getContext()).inflate(
                    R.layout.communicat_item, parent, false);

            viewHolder = new ViewHolder();

            // 左侧气泡
            viewHolder.LeftChatContentLayout = (RelativeLayout) convertView
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
            viewHolder.rightChatContentLayout = (RelativeLayout) convertView
                    .findViewById(R.id.right_chat_content_layout);
            viewHolder.rightChatLayout = (RelativeLayout) convertView
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
                showPopUp((TextView) arg0);
                return true;
            }
        });

        viewHolder.rightContentTxt.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                showPopUp((TextView) arg0);
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
        //aid = itemModel.getId();
        if (viewType == LEFT_TYPE) {
            viewHolder.leftTime.setVisibility(View.VISIBLE);
            viewHolder.leftChatLayout.setVisibility(View.VISIBLE);
            viewHolder.rightTime.setVisibility(View.GONE);
            viewHolder.rightChatLayout.setVisibility(View.GONE);

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
                            viewHolder.leftContentImage, new SimpleImageLoadingListener() {
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
                    viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_white_bg);
                } else {
                    if (position == 1) {
                        viewHolder.leftContentTxt.setText("回答：" + itemModel.getContent());
                        viewHolder.LeftChatContentLayout.setBackgroundResource(R.drawable.chat_left_blue_bg);
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
                            viewHolder.leftContentImage, new SimpleImageLoadingListener() {
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


            if (type == 2 && position != 0 && position != 1) {
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
                            viewHolder.rightContentImage, new SimpleImageLoadingListener() {
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
                            viewHolder.rightContentImage, new SimpleImageLoadingListener() {
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
                    } else {
                        if (position == 1) {
                            viewHolder.rightContentTxt.setText("回答：" + itemModel.getContent());
                        } else {
                            viewHolder.rightContentTxt.setText(itemModel.getContent());
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

    private void showPopUpImageView(ImageView v){
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

        CommentReply itemModel=(CommentReply)v.getTag();
        mdelect.setTag(itemModel);


        final PopupWindow popupWindow = new PopupWindow(layout, 120, itemModel==null?140:240);

        mdelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int uid = AppContext.instance().getLoginUid();
                final CommentReply itemModel=(CommentReply)v.getTag();
                Log.e("----->>><<<",itemModel.getId()+"");
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
                                    }else{
                                        removeDescItem(itemModel.getId());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                AppContext.showToast(str);
                            }
                        });
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

    private void showPopUp(TextView v) {
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

        CommentReply itemModel=(CommentReply)v.getTag();
        if(itemModel==null){
            mdelect.setVisibility(View.GONE);
        }else{
            mdelect.setVisibility(View.VISIBLE);
            mdelect.setTag(itemModel);
        }

        final PopupWindow popupWindow = new PopupWindow(layout, 120, itemModel==null?140:240);

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
            public void onClick(View v) {
                int uid = AppContext.instance().getLoginUid();
                /*NewsApi.delectAnswer(uid, aid,*/
                final CommentReply itemModel=(CommentReply)v.getTag();
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
                                    }else{
                                        removeDescItem(itemModel.getId());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                AppContext.showToast(str);
                            }
                        });
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

    private void removeDescItem(int aid){
        CommentReply removeItem = null;
        for(int i=0;i<_data.size();i++){
            CommentReply itemModel = (CommentReply) _data.get(i);
            if(itemModel.getId()==aid){
                removeItem=itemModel;
                break;
            }
        }
        if(removeItem!=null){
            _data.remove(removeItem);
        }
        notifyDataSetChanged();
    }


    static class ViewHolder {
        RelativeLayout leftChatLayout, rightChatLayout, LeftChatContentLayout, rightChatContentLayout;
        ImageView leftUserPhoto, rightUserPhoto;
        TextView leftUserName, rightUserName, leftContentTxt, rightContentTxt;
        ImageView leftContentImage, rightContentImage;
        TextView leftTime, rightTime;
        ProgressBar rightContentImageBar;
        ProgressBar leftContentImageBar;
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


