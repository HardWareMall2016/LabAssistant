package net.oschina.app.v2.activity.user.adapter;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.activity.user.model.XiTongXiaoXi;
import net.oschina.app.v2.activity.user.model.ZhiChiWoDe;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.utils.BitmapLoaderUtil;

public class ZhiChiWoDeAdapter extends ListBaseAdapter {
    private DisplayImageOptions options;

    public ZhiChiWoDeAdapter(){
        super();
        options = BitmapLoaderUtil.loadDisplayImageOptions();
    }

    protected View getRealView(int position, View convertView, ViewGroup parent) {
        // 定义一个条目，用来装item条目之中的每一个控件。条目的布局是myanswer_item
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.zhichiwode_item, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        ZhiChiWoDe data = (ZhiChiWoDe) _data.get(position);


        ImageLoader.getInstance().displayImage(data.getHead(),
                vh.iv_avatar, options);

        String str1="在\"";
        String str2=data.getTitle();
        String str3="\"问题里支持了你的回答";
        vh.summary.setText(str1);
        SpannableString spanString = new SpannableString(str2);
        ForegroundColorSpan span = new ForegroundColorSpan(0xff01a9f4);
        spanString.setSpan(span, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vh.summary.append(spanString);
        vh.summary.append(str3);

        vh.tv_rank.setText("Lv" + data.getRank());
        vh.tv_post.setText("");
        vh.tv_time.setText(data.getInputtime());
        vh.tv_name.setText(data.getNickname());

        IvSignUtils.displayIvSignByType(data.getType(), vh.iv_sign, vh.avstarBg);

        return convertView;
    }

    static class ViewHolder {
        private ImageView iv_avatar;
        private ImageView iv_sign;
        private TextView tv_name;
        private TextView tv_rank;
        private TextView tv_post;
        private TextView tv_time;
        private TextView summary;
        private ImageView avstarBg;

        public ViewHolder(View view) {
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            iv_sign = (ImageView) view.findViewById(R.id.iv_sign);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_rank = (TextView) view.findViewById(R.id.tv_rank);
            tv_post = (TextView) view.findViewById(R.id.tv_post);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            summary = (TextView) view.findViewById(R.id.summary);
            avstarBg = (ImageView) view.findViewById(R.id.iv_avastarBg);
        }
    }
}
