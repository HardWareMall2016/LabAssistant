package net.oschina.app.v2.model.popupbuttonlibrary.adapter;

import java.util.List;

import net.oschina.app.v2.activity.tweet.model.Mulu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

/**
 * 自定义的弹出框列表适配器,类似于大众点评或美团,如果想要此种效果可以直接使用
 * Created by Chris on 2014/12/8.
 */
public class PopupAdapter extends ArrayAdapter<Mulu> {
    private int resource;
    private int normalBg;
    private int pressBg;
    private int selection;

//    public PopupAdapter(Context context, int resource, String[] objects, int normalBg, int pressBg) {
//        super(context, resource, objects);
//        initParams(resource, normalBg, pressBg);
//    }


    public PopupAdapter(Context context, int resource, List<Mulu> objects, int normalBg, int pressBg) {
        super(context, resource, objects);
        initParams(resource, normalBg, pressBg);
    }
    boolean isNeedLeft=false;
    
    public void isNeedLeft(boolean isNeedLeft){
    	this.isNeedLeft=isNeedLeft;
    }

  


	private void initParams(int resource, int normalBg, int pressBg){
        this.resource = resource;
        this.normalBg = normalBg;
        this.pressBg = pressBg;
        this.selection = -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String s = getItem(position).getcatname();
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource,null);
            holder = new ViewHolder();
            holder.tv = (TextView) view.findViewById(R.id.tv);
            holder.leftImage=(ImageView)view.findViewById(R.id.leftImage);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv.setText(s);
        if(position == selection) {
            holder.tv.setBackgroundResource(pressBg);
            if(isNeedLeft)
            holder.leftImage.setVisibility(View.VISIBLE);
            holder.tv.setSelected(true);
        } else {
            holder.tv.setBackgroundResource(normalBg);
            holder.leftImage.setVisibility(View.GONE);
            holder.tv.setSelected(false);
        }
        return view;
    }

    public void setPressPostion(int position) {
        this.selection = position;
    }
    class ViewHolder{
    	ImageView leftImage;
        TextView tv;
    }
}
