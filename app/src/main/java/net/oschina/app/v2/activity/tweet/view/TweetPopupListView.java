package net.oschina.app.v2.activity.tweet.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuYue on 2016/5/4.
 */
public class TweetPopupListView{

    public static final int QUESTION_STATUS=1;//问题状态
    public static final int CHOOSE_CLASSIFY=2;//选择分类
    public static final int CHOOSE_SUB_CLASSIFY=3;//选择子类

    private PopupWindow mPopupView;
    private ListView mFilterList;
    private Context mContext;

    private FilterAdapter mAdapter;

    //问题状态
    private List<FilterItem> mQuestionStatus=new ArrayList<FilterItem>();
    //选择分类
    private List<FilterItem> mMainClassifyList=new ArrayList<FilterItem>();;
    //选择子类
    private List<FilterItem> mSubClassifyList=new ArrayList<FilterItem>();;

    //当前显示
    private List<FilterItem> mShowList=mQuestionStatus;


    private class FilterItem{
        int id;
        boolean checked=false;
        String name;
    }

    public TweetPopupListView(Context context,PopupWindow.OnDismissListener onDismissListener) {
        mContext=context;
        View contentView = View.inflate(context, R.layout.tweet_popup_list_layout, null);
        mFilterList=(ListView)contentView.findViewById(R.id.filter_list);
        mPopupView = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupView.setFocusable(true);
        mPopupView.setOutsideTouchable(true);
        //mPopupView.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        mPopupView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popmenu));
        mAdapter=new FilterAdapter();
        mFilterList.setAdapter(mAdapter);
        mPopupView.setOnDismissListener(onDismissListener);

        initData();
    }

    private void initData(){
        mQuestionStatus.clear();
        FilterItem item=new FilterItem();
        item.name="悬赏";
        mQuestionStatus.add(item);
        item=new FilterItem();
        item.name="未解决";
        mQuestionStatus.add(item);
    }


    public void setMainClassifyList(List<Mulu> classifyList){
        mMainClassifyList.clear();
        if(classifyList!=null){
            for (Mulu mulu:classifyList){
                FilterItem item=new FilterItem();
                item.name=mulu.getcatname();
                item.id=mulu.getId();
                mMainClassifyList.add(item);
            }
        }
    }

    public void showPopup(View anchor,int filterType) {
        switch (filterType){
            case QUESTION_STATUS:
                mShowList=mQuestionStatus;
                break;
            case CHOOSE_CLASSIFY:
                mShowList=mQuestionStatus;
                break;
            case CHOOSE_SUB_CLASSIFY:
                mShowList=mQuestionStatus;
                break;
        }
        int maxWidth=0;
        for (int i=0;i<mAdapter.getCount();i++){
            View item = mAdapter.getView(i, null, mFilterList);
            item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int width=item.getMeasuredWidth();
            if(maxWidth<width){
                maxWidth=width;
            }
        }
        mPopupView.setWidth(DeviceUtils.dip2px(mContext,40)+maxWidth);
        mPopupView.showAsDropDown(anchor);
        /*if (!mPopupView.isShowing()) {
            mLanMuView.updateList(lanItems);
            if(selectedMulu!=null){
                mLanMuView.updateState(lanItems,selectedMulu.getcatname());
            }
            mPopupView.showAsDropDown(anchor);
        } else {
            mPopupView.dismiss();
        }*/
    }


    private class FilterAdapter extends BaseAdapter{
        private class ViewHolder{
            CheckBox selector;
            TextView name;
        }

        @Override
        public int getCount() {
            return mShowList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mContext, R.layout.tweet_popup_list_item, null);
                holder=new ViewHolder();
                holder.name=(TextView)convertView.findViewById(R.id.name);
                holder.selector=(CheckBox)convertView.findViewById(R.id.ck_selector);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }

            holder.name.setText(mShowList.get(position).name);

            return convertView;
        }
    }
}
