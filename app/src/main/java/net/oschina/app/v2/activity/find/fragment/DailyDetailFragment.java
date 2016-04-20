package net.oschina.app.v2.activity.find.fragment;

import net.oschina.app.v2.activity.news.fragment.BaseDetailFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiyanzhushou.app.R;

public class DailyDetailFragment extends BaseDetailFragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.v2_fragment_news_detail,
				container, false);
		return view;
	}
}
