package net.oschina.app.v2.activity.user.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.user.adapter.ZhiChiWoDeAdapter;
import net.oschina.app.v2.activity.user.model.XiTongXiaoXiList;
import net.oschina.app.v2.activity.user.model.ZhiChiWoDeList;
import net.oschina.app.v2.api.remote.NewsApi;
import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import net.oschina.app.v2.model.ListEntity;

import java.io.InputStream;
import java.io.Serializable;

public class ZhiChiWoDeFragment extends BaseListFragment {
	protected static final String TAG = ZhiChiWoDeFragment.class.getSimpleName();
	// 缓存的前缀
	private static final String CACHE_KEY_PREFIX_5 = "zhichiwode";

	private TextView mVtSummary;

	@Override
	protected int getLayoutRes() {
		return R.layout.zhi_chi_wo_de;
	}

	@Override
	protected void initViews(View view) {
		super.initViews(view);
		mVtSummary=(TextView)view.findViewById(R.id.summary);
		populateView("0","0");
	}

	// 适配器
	@Override
	protected ListBaseAdapter getListAdapter() {
		return new ZhiChiWoDeAdapter();
	}

	// 缓存前缀
	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX_5;
	}

	@Override
	protected ListEntity parseList(InputStream is) throws Exception {
		// 流转换成为String
		String result = inStream2String(is).toString();
		// 解析string得到集合。
		ZhiChiWoDeList list = ZhiChiWoDeList.parse(result);
		is.close();
		Message message=mHandler.obtainMessage();
		message.what=100;
		message.obj=list;
		mHandler.sendMessage(message);
		return list;
	}

	@Override
	protected ListEntity readList(Serializable seri) {
		return ((XiTongXiaoXiList) seri);
	}

	// 发送请求的数据。
	@Override
	protected void sendRequestData() {
		int uid=AppContext.instance().getLoginUid();
		NewsApi.getSupportedinfo(uid, mCurrentPage, mJsonHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 100:
					ZhiChiWoDeList list=(ZhiChiWoDeList)msg.obj;
					populateView(list.getSupportednum(),list.getTodaysupportednum());
					break;
			}
		}
	};

	private void populateView(String supportednum,String todaysupportednum){
		if(TextUtils.isEmpty(supportednum)){
			supportednum="0";
		}

		if(TextUtils.isEmpty(todaysupportednum)){
			todaysupportednum="0";
		}

		String str1="共";
		String str2="收到";
		String str3=supportednum;
		String str4="个支持,";
		String str5="今日";
		String str6="收到";
		String str7=todaysupportednum;
		String str8="个支持";
		SpannableString spanString = new SpannableString(str1);
		AbsoluteSizeSpan asSpan = new AbsoluteSizeSpan(18, true);
		spanString.setSpan(asSpan, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mVtSummary.setText(spanString);
		mVtSummary.append(str2);
		spanString = new SpannableString(str3);
		ForegroundColorSpan span = new ForegroundColorSpan(0xff01a9f4);
		spanString.setSpan(span, 0, str3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mVtSummary.append(spanString);
		mVtSummary.append(str4);
		spanString = new SpannableString(str5);
		asSpan = new AbsoluteSizeSpan(18, true);
		spanString.setSpan(asSpan, 0, str5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mVtSummary.append(spanString);
		mVtSummary.append(str6);
		spanString = new SpannableString(str7);
		span = new ForegroundColorSpan(0xff01a9f4);
		spanString.setSpan(span, 0, str7.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mVtSummary.append(spanString);
		mVtSummary.append(str8);
	}
}
