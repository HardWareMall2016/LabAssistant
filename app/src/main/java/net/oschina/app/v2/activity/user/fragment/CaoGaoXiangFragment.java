package net.oschina.app.v2.activity.user.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.CommunicatActivity;
import net.oschina.app.v2.activity.tweet.TweetDetailActivity;
import net.oschina.app.v2.db.DBHelper;
import net.oschina.app.v2.db.DraftBean;
import net.oschina.app.v2.model.Ask;
import net.oschina.app.v2.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CaoGaoXiangFragment extends Fragment implements AdapterView.OnItemClickListener {
    protected static final String TAG = CaoGaoXiangFragment.class.getSimpleName();
    //View
    private ListView mListView;

    //Tools
    private LayoutInflater mLayoutInflater;
    private SimpleDateFormat mTimeFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CANADA);
    private CaoGaoXiangAdapter mAdapter;

    //data
    private List<DraftBean> mDraftList=new ArrayList<DraftBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater=inflater;
        View contentView = inflater.inflate(R.layout.common_fragment_listview, null);

        mListView = (ListView) contentView.findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
        mAdapter=new CaoGaoXiangAdapter();
        mListView.setAdapter(mAdapter);
        //refreshView();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    private void refreshView(){
        mDraftList= DBHelper.findData(DraftBean.class);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DraftBean draftBean=  mDraftList.get(position);
        if (draftBean != null&&draftBean.getDraftType()==1 ) {
            Ask ask = new Ask();
            ask.setId(draftBean.getQuestionId());
            //ask.setUid(Integer.parseInt(xiaoXi.getQuid()));

            Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ask", ask);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }else   if (draftBean != null&&draftBean.getDraftType()==2 ) {
            Comment comment = new Comment();
            comment.setId(draftBean.getCommentId());
            comment.setAid(draftBean.getCommentAid());
            comment.setqid(draftBean.getCommentQid());
            comment.setauid(draftBean.getCommentAuid());
            comment.setnickname(draftBean.getCommentNickName());

            Ask ask = new Ask();
            ask.setId(draftBean.getAskId());
            ask.setUid(draftBean.getAskUid());
            ask.setnickname(draftBean.getAskNickname());
            ask.sethead(draftBean.getAskHead());
            ask.setContent(draftBean.getAskContent());
            ask.setinputtime(draftBean.getAskIntPutTime());
            ask.setImage(draftBean.getAskImage());
            ask.setNewcontent(draftBean.getAskNewContent());

            Intent intent = new Intent(getActivity(), CommunicatActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("type", draftBean.getType());
            bundle.putSerializable("ask",ask);
            bundle.putSerializable("comment", comment);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);

        }
    }

    /*@Override
    protected void initViews(View view) {
        super.initViews(view);
        mListView.getRefreshableView().setDividerHeight(DeviceUtils.dip2px(getActivity(), 16));
    }*/

    // 适配器
    /*@Override
    protected ListBaseAdapter getListAdapter() {
        return new CaoGaoXiangAdapter();
    }*/

    // 缓存前缀
    /*@Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX_5;
    }*/

    /*@Override
    protected ListEntity parseList(InputStream is) throws Exception {
        // 流转换成为String
        String result = inStream2String(is).toString();
        // 解析string得到集合。
        XiTongXiaoXiList list = XiTongXiaoXiList.parse(result);
        is.close();
        return list;
    }*/

    /*@Override
    protected ListEntity readList(Serializable seri) {
        return ((XiTongXiaoXiList) seri);
    }*/

    // 发送请求的数据。
    /*@Override
    protected void sendRequestData() {
        int uid = AppContext.instance().getLoginUid();
        NewsApi.getXiTongXiaoXi(uid, mCurrentPage, mJsonHandler);
    }*/

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XiTongXiaoXi xiaoXi = (XiTongXiaoXi) mAdapter.getItem(position - 1);
        if (xiaoXi != null && "1".equals(xiaoXi.getType())) {
            Ask ask = new Ask();
            ask.setId(Integer.parseInt(xiaoXi.getQid()));
            ask.setUid(Integer.parseInt(xiaoXi.getQuid()));

            Intent intent = new Intent(getActivity(), TweetDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ask", ask);
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }*/


    private class CaoGaoXiangAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDraftList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDraftList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null || convertView.getTag() == null) {
                convertView = mLayoutInflater.inflate(R.layout.caogaoxiang, null);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.bindData(mDraftList.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        private ImageView btnDel;
        private TextView tvSaveTime;
        private TextView tvDraftContent;
        private TextView tvQuestion;

        public ViewHolder(View view) {
            btnDel=(ImageView)view.findViewById(R.id.btn_delete);
            tvSaveTime=(TextView)view.findViewById(R.id.save_time);
            tvDraftContent=(TextView)view.findViewById(R.id.draft_content);
            tvQuestion=(TextView)view.findViewById(R.id.question);
        }

        public void bindData(DraftBean data){
            btnDel.setTag(data);
            btnDel.setOnClickListener(mOnClickListener);
            tvSaveTime.setText(mTimeFormat.format(data.getSaveTime()));
            SpannableString spanText = new SpannableString("[草稿]");
            spanText.setSpan(new ForegroundColorSpan(0xffCB4444), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvDraftContent.setText(spanText);
            tvDraftContent.append(data.getDraftContent());
            tvQuestion.setText(data.getQuestionTitle());
        }
    }

    private View.OnClickListener mOnClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            DraftBean data=(DraftBean) v.getTag();
            DBHelper.deleteData(data);
            refreshView();
        }
    };
}
