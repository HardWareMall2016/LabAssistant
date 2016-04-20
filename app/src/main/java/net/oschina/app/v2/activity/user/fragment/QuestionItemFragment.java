package net.oschina.app.v2.activity.user.fragment;

import java.io.InputStream;
import java.io.Serializable;

import net.oschina.app.v2.activity.model.Question;
import net.oschina.app.v2.activity.user.adapter.AnswerItemAdapter;
import net.oschina.app.v2.activity.user.model.AnswerItemList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.CommentList;
import net.oschina.app.v2.model.ListEntity;
import net.oschina.app.v2.utils.DateUtil;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class QuestionItemFragment extends BaseListFragment {
	protected static final String TAG = AnswerItemFragment.class
			.getSimpleName();
	private static final String CACHE_KEY_PREFIX_2 = "answeritemfragment_";

	private Question question;
	private TextView tv_detail_question;
	private ImageView iv_detail_isResolved;
	private TextView tv_detail_times;
	private TextView tv_detail_invite_who;
	private TextView tv_detail_mark;
	private TextView tv_detail_answernumber;

	@Override
	protected ListBaseAdapter getListAdapter() {
		return new AnswerItemAdapter();
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_2;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
		// AnswerItemList list = AnswerItemList.parse(result);
		CommentList list = CommentList.parse(new JSONObject(result));
		is.close();
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((AnswerItemList) seri);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		question = (Question) bundle.getSerializable("question");
	}

	@Override
	protected void addHeaderView(ListView listview) {
		View view = View.inflate(getActivity(), R.layout.myquestion_item_view,
				null);
		init(view);
		listview.addHeaderView(view);
	}

	@Override
	protected boolean isShowEmpty() {
		return false;
	}
	
	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
		NewsApi.getCommentList(question.getId(), mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	private void init(View view) {
		String content = question.getContent();
		tv_detail_question = (TextView) view
				.findViewById(R.id.tv_detail_question);
		tv_detail_question.setText(content != null ? content : "");

		iv_detail_isResolved = (ImageView) view
				.findViewById(R.id.iv_detail_isResolved);
		if (question.getIssolved() == 0) {
			iv_detail_isResolved.setVisibility(View.GONE);
		} else {
			iv_detail_isResolved.setVisibility(View.VISIBLE);
		}

		tv_detail_times = (TextView) view.findViewById(R.id.tv_detail_times);
		tv_detail_times
				.setText(DateUtil.getFormatTime(question.getIntputtime()));

		tv_detail_invite_who = (TextView) view
				.findViewById(R.id.tv_detail_invite_who);
		String superlist = question.getSuperlist();
		if (!TextUtils.isEmpty(superlist) && !"null".equals(superlist)) {
			tv_detail_invite_who.setText("邀请" + question.getSuperlist()
					+ "进行回答");
			tv_detail_invite_who.setVisibility(View.VISIBLE);
		} else {
			tv_detail_invite_who.setVisibility(View.GONE);
		}

		tv_detail_mark = (TextView) view.findViewById(R.id.tv_detail_mark);
		if (question.getLable() != null) {
			tv_detail_mark.setText("标签：" + question.getLable());
		} else {
			tv_detail_mark.setVisibility(View.GONE);
		}

		tv_detail_answernumber = (TextView) view
				.findViewById(R.id.tv_detail_answernumber);
		tv_detail_answernumber.setText(question.getAnum() + "人回答");

	}
}
