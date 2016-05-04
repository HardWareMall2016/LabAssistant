package net.oschina.app.v2.activity.tweet.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.utils.DeviceUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuYue on 2016/5/4.
 */
public class TweetPopupListView implements View.OnClickListener {

    public static final int QUESTION_STATUS=1;//问题状态
    public static final int CHOOSE_CLASSIFY=2;//选择分类
    public static final int CHOOSE_SUB_CLASSIFY=3;//选择子类

    //Flags
    private int mCurFilterType=QUESTION_STATUS;

   //Views
    private ListView mFilterList;

    //params
    private OnFilterClickListener mOnClickListener;
    private Context mContext;
    private int isreward=0, issolveed=0;//悬赏、未解决
    //Tools
    private FilterAdapter mAdapter;
    private PopupWindow mPopupView;


    //问题状态
    private List<FilterItem> mQuestionStatus=new ArrayList<FilterItem>();
    //选择分类
    private List<FilterItem> mMainClassifyList=new ArrayList<FilterItem>();
    //选择子类
    private List<FilterItem> mSubClassifyList=new ArrayList<FilterItem>();
    //当前显示
    private List<FilterItem> mShowList=mQuestionStatus;

    private int mSelMainFilterId=-1;

    @Override
    public void onClick(View v) {
        isreward=mQuestionStatus.get(0).checked?1:0;
        issolveed=mQuestionStatus.get(1).checked?1:0;

        if (mOnClickListener!=null) {
            StringBuilder sb=new StringBuilder();
            for (FilterItem item : mSubClassifyList) {
                sb.append(item.id+",");
            }
            if (sb.length() > 0) {
                sb = sb.deleteCharAt(sb.length()-1);
            }
            mOnClickListener.onFilter(isreward, issolveed, sb.toString());
            //mPopupView.dismiss();
        }

        mPopupView.dismiss();
    }

    private class FilterItem{
        int id;
        boolean checked=false;
        String name;
    }

    public TweetPopupListView(Context context,PopupWindow.OnDismissListener onDismissListener) {
        mContext=context;
        View contentView = View.inflate(context, R.layout.tweet_popup_list_layout, null);
        mFilterList=(ListView)contentView.findViewById(R.id.filter_list);
        contentView.findViewById(R.id.btn_confirm).setOnClickListener(this);
        mPopupView = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupView.setFocusable(true);
        mPopupView.setOutsideTouchable(true);
        mPopupView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popmenu));
        mAdapter=new FilterAdapter();
        mFilterList.setAdapter(mAdapter);
        mPopupView.setOnDismissListener(onDismissListener);

        populateQuestionStatus();
    }

    private void populateQuestionStatus(){
        mQuestionStatus.clear();
        FilterItem item=new FilterItem();
        item.name="悬赏";
        mQuestionStatus.add(item);
        item=new FilterItem();
        item.name="未解决";
        mQuestionStatus.add(item);
    }

    public void setOnFilterClickListener(OnFilterClickListener mListener) {
        this.mOnClickListener=mListener;
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

        if(mMainClassifyList.size()>0){
            sendRequestLanmuChildData(mMainClassifyList.get(0).id);
        }
    }

    public void setSubClassifyList(List<Mulu> classifyList){
        mSubClassifyList.clear();
        if(classifyList!=null){
            for (Mulu mulu:classifyList){
                FilterItem item=new FilterItem();
                item.name=mulu.getcatname();
                item.id=mulu.getId();
                mSubClassifyList.add(item);
            }
        }
    }

    public void showPopup(View anchor,int filterType) {
        mCurFilterType=filterType;
        switch (filterType){
            case QUESTION_STATUS:
                mShowList=mQuestionStatus;
                break;
            case CHOOSE_CLASSIFY:
                mShowList=mMainClassifyList;
                for(FilterItem item:mMainClassifyList){
                    item.checked=item.id==mSelMainFilterId;
                }
                break;
            case CHOOSE_SUB_CLASSIFY:
                mShowList=mSubClassifyList;
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

    private void sendRequestLanmuChildData(int id) {
        NewsApi.getLanmu(id, mLanmuChildHandler);
    }

    private JsonHttpResponseHandler mLanmuChildHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                MuluList list = MuluList.parse(response.toString());
                setSubClassifyList(list.getMululist());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private class FilterAdapter extends BaseAdapter{
        private class ViewHolder{
            RadioButton rbSelector;
            CheckBox ckSelector;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mContext, R.layout.tweet_popup_list_item, null);
                holder=new ViewHolder();
                holder.name=(TextView)convertView.findViewById(R.id.name);
                holder.ckSelector =(CheckBox)convertView.findViewById(R.id.ck_selector);
                holder.rbSelector =(RadioButton)convertView.findViewById(R.id.rb_selector);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }

            final FilterItem data=mShowList.get(position);

            if(mCurFilterType==CHOOSE_CLASSIFY){
                holder.ckSelector.setVisibility(View.GONE);
                holder.rbSelector.setVisibility(View.VISIBLE);

                holder.rbSelector.setChecked(data.checked);
                holder.rbSelector.setId(position);
                holder.rbSelector.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos=v.getId();
                        for (int i = 0; i < getCount(); i++) {
                            FilterItem tempData = mShowList.get(i);
                            if (i != pos) {
                                tempData.checked = false;
                            } else {
                                tempData.checked = true;
                                mSelMainFilterId=tempData.id;
                                sendRequestLanmuChildData(tempData.id);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }else{
                holder.ckSelector.setVisibility(View.VISIBLE);
                holder.rbSelector.setVisibility(View.GONE);

                holder.ckSelector.setChecked(data.checked);

                holder.ckSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        data.checked = isChecked;
                    }
                });
            }

            holder.name.setText(data.name);

            return convertView;
        }
    }

    public interface OnFilterClickListener {
        void onFilter(int isreward, int issolveed, String catid);
    }
}
