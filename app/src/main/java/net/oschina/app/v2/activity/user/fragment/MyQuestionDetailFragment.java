package net.oschina.app.v2.activity.user.fragment;

import net.oschina.app.v2.base.BaseListFragment;
import net.oschina.app.v2.base.ListBaseAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiyanzhushou.app.R;

public class MyQuestionDetailFragment extends BaseListFragment{

	@Override
	protected ListBaseAdapter getListAdapter() {
		return null;
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.myquestion_detail, null);
		return view;
	}
}
