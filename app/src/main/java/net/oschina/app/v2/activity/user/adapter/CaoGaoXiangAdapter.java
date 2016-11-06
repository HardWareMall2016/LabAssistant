package net.oschina.app.v2.activity.user.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.user.model.XiTongXiaoXi;
import net.oschina.app.v2.base.ListBaseAdapter;

public class CaoGaoXiangAdapter extends ListBaseAdapter {
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.caogaoxiang, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        XiTongXiaoXi xiTongXiaoXi = (XiTongXiaoXi) _data.get(position);
        return convertView;
    }

    static class ViewHolder {
        private ImageView iv_xitongxiaoxi;
        private TextView tv_isfinish_newtask;
        private TextView tv_timebefore;
        private TextView tv_show_sysinfo;

        public ViewHolder(View view) {

        }
    }
}
