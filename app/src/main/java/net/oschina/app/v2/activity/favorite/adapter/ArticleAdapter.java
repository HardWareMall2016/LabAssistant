package net.oschina.app.v2.activity.favorite.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.image.IvSignUtils;
import net.oschina.app.v2.api.ApiHttpClient;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ArticleList;
import net.oschina.app.v2.model.FavoriteList.Favorite;
import net.oschina.app.v2.utils.BitmapLoaderUtil;
import net.oschina.app.v2.utils.StringUtils;
import net.oschina.app.v2.utils.UIHelper;

import java.text.SimpleDateFormat;

public class ArticleAdapter extends ListBaseAdapter {
    private String mHighLight;
    private SimpleDateFormat mDateFormat=new SimpleDateFormat("yyyy/MM/dd");

    public void setHighLight(String highLight) {
        this.mHighLight = highLight;
    }

    @SuppressLint("InflateParams")
    @Override
    protected View getRealView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = getLayoutInflater(parent.getContext()).inflate(R.layout.v2_list_cell_article, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final ArticleList.Article item = (ArticleList.Article) _data.get(position);

        if (!StringUtils.isEmpty(mHighLight)) {
            /*vh.tv_title.setText(Html.fromHtml(StringUtils.getShowSingleLineWithHighlight(item.getTitle(), mHighLight)));
            vh.tv_content.setText(Html.fromHtml(StringUtils.getShowSingleLineWithHighlight(item.getDescription(), mHighLight)));*/
            vh.tv_title.setText(StringUtils.highlight(item.getTitle(), mHighLight));
            vh.tv_content.setText(StringUtils.highlight(item.getDescription(), mHighLight));
        } else {
            vh.tv_title.setText(StringUtils.getShowSingleLineStr(item.getTitle()));
            vh.tv_content.setText(StringUtils.getShowSingleLineStr(item.getDescription()));
        }

        vh.tv_cname.setText(item.getCname());
        vh.tv_time.setText(item.getInputtime());

        return convertView;
    }


    static class ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_cname;
        private TextView tv_time;

        public ViewHolder(View view) {
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_cname = (TextView) view.findViewById(R.id.tv_cname);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
