package net.oschina.app.v2.activity.tweet.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shiyanzhushou.app.R;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.model.Mulu;
import net.oschina.app.v2.activity.tweet.model.MuluList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.model.event.ClearFilterConditions;
import net.oschina.app.v2.utils.DeviceUtils;
import net.oschina.app.v2.utils.ShareUtil;
import net.oschina.app.v2.utils.TDevice;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by WuYue on 2016/5/4.
 */
public class TweetPopupListView implements View.OnClickListener {

    public static final int QUESTION_STATUS=1;//问题状态
    public static final int CHOOSE_CLASSIFY=2;//选择分类
    public static final int CHOOSE_SUB_CLASSIFY=3;//选择子类

    //Flags
    private int mCurFilterType=QUESTION_STATUS;
    private int isreward=0, issolveed=0;//悬赏、未解决
    private int mSelMainFilterId=-1;
    private ArrayList<Integer> mSelSubFilterIds=new ArrayList<Integer>();

    //Views
    private ListView mFilterList;
    private View mViewConfirmBtn;

    //params
    private OnFilterClickListener mOnClickListener;
    private Context mContext;

    //Tools
    private FilterAdapter mAdapter;
    private PopupWindow mPopupView;

    //Tab tweet views
    private TextView mViewQuestionStatus;
    private TextView mViewChooseClassify;
    private TextView mViewChooseSubClassify;

    private View mCover;

    //问题状态
    private List<FilterItem> mQuestionStatus=new ArrayList<FilterItem>();
    //选择分类
    private List<FilterItem> mMainClassifyList=new ArrayList<FilterItem>();
    //选择子类
    private List<FilterItem> mSubClassifyList=new ArrayList<FilterItem>();
    //当前显示
    private List<FilterItem> mShowList=mQuestionStatus;

    private boolean mIsActivityAlive=false;

    @Override
    public void onClick(View v) {
        if (mOnClickListener!=null) {
            if(mCurFilterType==QUESTION_STATUS){
                isreward=mQuestionStatus.get(0).checked?1:0;
                issolveed=mQuestionStatus.get(1).checked?1:0;
                if(mSelMainFilterId==-1){
                    ShareUtil.setValue(ShareUtil.SELECTED_CAT_STRS, "全部");
                }
                mOnClickListener.onFilter(isreward, issolveed, getSelectedCatids());
            } else{
                ArrayList<Integer> selSubFilterIds=new ArrayList<Integer>();
                String subclassStr="";
                for (FilterItem item : mSubClassifyList) {
                    if(item.checked){
                        selSubFilterIds.add(item.id);
                        if(TextUtils.isEmpty(subclassStr)){
                            subclassStr=item.name;
                        }else{
                            subclassStr+="/"+item.name;
                        }
                    }
                }
                if(mSelMainFilterId==-1){
                    ShareUtil.setValue(ShareUtil.SELECTED_CAT_STRS, "全部");
                }else{
                    if(selSubFilterIds.size()==0){
                        AppContext.showToast("您未选择子类");
                        return;
                    }
                    ShareUtil.setValue(ShareUtil.SELECTED_CAT_STRS, subclassStr);
                }
                mSelSubFilterIds=selSubFilterIds;
                mOnClickListener.onFilter(isreward, issolveed, getSelectedCatids());
            }
        }

        mPopupView.dismiss();

        refreshFilterViews();
    }

    private String getSelectedCatids(){
        if(mSelMainFilterId==-1){
            return "-1";
        }
        StringBuilder sb=new StringBuilder();
        for(Integer id:mSelSubFilterIds){
            sb.append(id+",");
        }
        if (sb.length() > 0) {
            sb = sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    private class FilterItem{
        int id;
        boolean checked=false;
        String name;
    }

    public TweetPopupListView(Context context) {

        mIsActivityAlive=true;

        isreward= ShareUtil.getIntValue(ShareUtil.IS_REWARD,0);
        issolveed= ShareUtil.getIntValue(ShareUtil.IS_SOLVEED,0);
        mSelMainFilterId= ShareUtil.getIntValue(ShareUtil.MAIN_FILTER_ID, -1);
        String selectedCatIds=ShareUtil.getStringValue(ShareUtil.SELECTED_CAT_IDS, "");
        String [] subFilterList=selectedCatIds.split(",");
        mSelSubFilterIds.clear();
        if(subFilterList!=null&&subFilterList.length>0){
            for(String id:subFilterList){
                try {
                    int catId=Integer.parseInt(id);
                    mSelSubFilterIds.add(catId);
                } catch (NumberFormatException e) {
                }
            }
        }

        mContext=context;
        View contentView = View.inflate(context, R.layout.tweet_popup_list_layout, null);
        mFilterList=(ListView)contentView.findViewById(R.id.filter_list);
        mViewConfirmBtn=contentView.findViewById(R.id.btn_confirm);
        mViewConfirmBtn.setOnClickListener(this);
        mPopupView = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupView.setFocusable(true);
        mPopupView.setOutsideTouchable(true);
        mPopupView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_popmenu));
        mAdapter= new FilterAdapter();
        mFilterList.setAdapter(mAdapter);
        mPopupView.setOnDismissListener(mOnDismissListener);

        populateQuestionStatus();

        EventBus.getDefault().register(this);
    }

    public void setViewQuestionStatus(TextView viewQuestionStatus) {
        this.mViewQuestionStatus = viewQuestionStatus;
    }

    public void setViewChooseClassify(TextView viewChooseClassify) {
        this.mViewChooseClassify = viewChooseClassify;
    }

    public void setViewChooseSubClassify(TextView viewChooseSubClassify) {
        this.mViewChooseSubClassify = viewChooseSubClassify;
    }

    public void setViewCover(View viewCover) {
        this.mCover = viewCover;
    }

    private PopupWindow.OnDismissListener mOnDismissListener=new PopupWindow.OnDismissListener(){
        @Override
        public void onDismiss() {
            mCover.setVisibility(View.GONE);
        }
    };


    public void refreshFilterViews(){
        //问题状态
        String questionStatus;
        questionStatus=ShareUtil.getIntValue(ShareUtil.IS_REWARD, 0)==0?"":"悬赏";
        if(TextUtils.isEmpty(questionStatus)){
            questionStatus=ShareUtil.getIntValue(ShareUtil.IS_SOLVEED,0)==0?"":"未解决";
        }else{
            questionStatus+=ShareUtil.getIntValue(ShareUtil.IS_SOLVEED,0)==0?"":"/未解决";
        }

        if(TextUtils.isEmpty(questionStatus)){
            mViewQuestionStatus.setText("问题状态");
        }else{
            mViewQuestionStatus.setText(questionStatus);
        }

        //选择分类
        String chooseClassify=ShareUtil.getStringValue(ShareUtil.MAIN_FILTER_STR);
        if(TextUtils.isEmpty(chooseClassify)){
            chooseClassify="选择分类";
        }
        mViewChooseClassify.setText(chooseClassify);

        //选择子类
        //SELECTED_CAT_STRS
        String chooseSubClassify=ShareUtil.getStringValue(ShareUtil.SELECTED_CAT_STRS);
        if(TextUtils.isEmpty(chooseSubClassify)){
            chooseSubClassify="选择子类";
        }
        mViewChooseSubClassify.setText(chooseSubClassify);

    }

    public void onEventMainThread(ClearFilterConditions clearFilterConditions){

        isreward=0;
        issolveed=0;
        mSelMainFilterId=-1;

        mSelSubFilterIds.clear();
    }

    public void unregisterEventBus(){
        EventBus.getDefault().unregister(this);
        mIsActivityAlive=false;
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
            boolean exist=false;
            for (FilterItem item:mMainClassifyList){
                if(item.id==mSelMainFilterId){
                    exist=true;
                    break;
                }
            }
            //默认第一项
            if(!exist){
                mSelMainFilterId=mMainClassifyList.get(0).id;
            }
            sendRequestLanmuChildData(mSelMainFilterId);
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
        if(!mIsActivityAlive){
            if(mPopupView!=null&&mPopupView.isShowing()){
                mPopupView.dismiss();
            }
            return;
        }
        if(mPopupView!=null&&mPopupView.isShowing()){
            return;
        }

        //如果是全部，就不用显示第三个
        if(filterType==CHOOSE_SUB_CLASSIFY&&mSelMainFilterId==-1){
            return;
        }

        mCover.setVisibility(View.VISIBLE);
        mPopupView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_popmenu));
        mCurFilterType=filterType;
        mFilterList.setOnItemClickListener(onItemClickListener);
        mViewConfirmBtn.setVisibility(View.VISIBLE);
        switch (filterType){
            case QUESTION_STATUS:
                mShowList=mQuestionStatus;
                //初始化
                mQuestionStatus.get(0).checked= isreward == 1;
                mQuestionStatus.get(1).checked= issolveed == 1;
                break;
            case CHOOSE_CLASSIFY:
                mViewConfirmBtn.setVisibility(View.GONE);
                mShowList=mMainClassifyList;
                //初始化
                /*for(FilterItem item:mMainClassifyList){
                    item.checked=item.id==mSelMainFilterId;
                }*/
                break;
            case CHOOSE_SUB_CLASSIFY:
                mPopupView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_popmenu_right));
                for (FilterItem subItem : mSubClassifyList) {
                    subItem.checked = false;
                    for (Integer id : mSelSubFilterIds) {
                        if (id == subItem.id) {
                            subItem.checked = true;
                            break;
                        }
                    }
                }
                mShowList=mSubClassifyList;
                break;
        }
        int maxWidth= 0;
        for (int i=0;i<mAdapter.getCount();i++){
            View item = mAdapter.getView(i, null, mFilterList);
            item.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int width=item.getMeasuredWidth();
            if(maxWidth<width){
                maxWidth=width;
            }
        }
        if(maxWidth<(TDevice.getScreenWidth()/5)){
            maxWidth=(int) (TDevice.getScreenWidth()/3);
        }
        mPopupView.setWidth(DeviceUtils.dip2px(mContext,30)+maxWidth);
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

    private AdapterView.OnItemClickListener onItemClickListener=new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            FilterItem data = mShowList.get(position);
            if(mCurFilterType==CHOOSE_CLASSIFY){
                if(data.id==mSelMainFilterId){
                    mPopupView.dismiss();
                    showPopup(mViewChooseSubClassify, TweetPopupListView.CHOOSE_SUB_CLASSIFY);
                    return;
                }

                mPopupView.dismiss();

                mSelMainFilterId=data.id;
                sendRequestLanmuChildData(data.id);

                ShareUtil.setIntValue(ShareUtil.MAIN_FILTER_ID, mSelMainFilterId);
                ShareUtil.setValue(ShareUtil.MAIN_FILTER_STR, data.name);
                ShareUtil.setValue(ShareUtil.SELECTED_CAT_STRS, null);
                if(mSelMainFilterId==-1){//如果大类是全部，小类也显示全部
                    mOnClickListener.onFilter(isreward, issolveed, "-1");
                    ShareUtil.setValue(ShareUtil.SELECTED_CAT_STRS, data.name);
                }

                refreshFilterViews();
            }else{
                data.checked=!data.checked;
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private void sendRequestLanmuChildData(int id) {
        if(id==-1){
            mSubClassifyList.clear();
        }else {
            NewsApi.getLanmu(id, mLanmuChildHandler);
        }
    }

    private JsonHttpResponseHandler mLanmuChildHandler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            try {
                MuluList list = MuluList.parse(response.toString());
                setSubClassifyList(list.getMululist());
                if(mCurFilterType==CHOOSE_CLASSIFY){
                    showPopup(mViewChooseSubClassify, TweetPopupListView.CHOOSE_SUB_CLASSIFY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private class FilterAdapter extends BaseAdapter{
        private class ViewHolder{
            //RadioButton rbSelector;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=View.inflate(mContext, R.layout.tweet_popup_list_item, null);
                holder=new ViewHolder();
                holder.name=(TextView)convertView.findViewById(R.id.name);
                holder.ckSelector =(CheckBox)convertView.findViewById(R.id.ck_selector);
                convertView.setTag(holder);
            }else{
                holder=(ViewHolder)convertView.getTag();
            }
            final FilterItem data=mShowList.get(position);

            //convertView.setBackgroundColor(Color.TRANSPARENT);
            holder.name.setTextColor(0xaf000000);
            holder.name.setBackgroundColor(Color.TRANSPARENT);

            if(mCurFilterType==CHOOSE_CLASSIFY){
                holder.name.setGravity(Gravity.CENTER);
                holder.ckSelector.setVisibility(View.GONE);
                if(data.id==mSelMainFilterId){
                    //convertView.setBackgroundResource(R.drawable.bg_filter_selected_rounded_corner);
                    holder.name.setTextColor(Color.WHITE);
                    holder.name.setBackgroundResource(R.drawable.bg_filter_selected_rounded_corner);
                }
            } else {
                holder.name.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                holder.ckSelector.setVisibility(View.VISIBLE);
                holder.ckSelector.setChecked(data.checked);
            }

            holder.name.setText(data.name);

            return convertView;
        }
    }

    public interface OnFilterClickListener {
        void onFilter(int isreward, int issolveed, String catid);
    }
}
