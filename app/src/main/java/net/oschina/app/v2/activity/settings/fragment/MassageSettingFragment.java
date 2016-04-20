package net.oschina.app.v2.activity.settings.fragment;

import net.oschina.app.v2.SettingsUtils;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.ui.tooglebutton.ToggleButton;
import net.oschina.app.v2.ui.tooglebutton.ToggleButton.OnToggleChanged;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiyanzhushou.app.R;

public class MassageSettingFragment extends BaseFragment implements OnToggleChanged {

	private ToggleButton tb_load_huodong, tb_load_ask, tb_load_caina,
			tb_load_zhuiwen, tb_load_systemmessage, tb_load_fensiqiuzhu;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_messagesettings,
				container, false);
		initViews(view);
		readSettings();
		return view;
	}

	private void initViews(View view) {
		tb_load_huodong=(ToggleButton) view.findViewById(R.id.tb_load_huodong);
		tb_load_ask=(ToggleButton) view.findViewById(R.id.tb_load_ask);
		tb_load_caina=(ToggleButton) view.findViewById(R.id.tb_load_caina);
		tb_load_zhuiwen=(ToggleButton) view.findViewById(R.id.tb_load_zhuiwen);
		tb_load_systemmessage=(ToggleButton) view.findViewById(R.id.tb_load_systemmessage); 
		tb_load_fensiqiuzhu=(ToggleButton) view.findViewById(R.id.tb_load_fensiqiuzhu);
		
		tb_load_huodong.setOnToggleChanged(this);
		tb_load_ask.setOnToggleChanged(this);
		tb_load_caina.setOnToggleChanged(this);
		tb_load_zhuiwen.setOnToggleChanged(this);
		tb_load_systemmessage.setOnToggleChanged(this);
		tb_load_fensiqiuzhu.setOnToggleChanged(this);
	}

	@Override
	public void onToggle(View view, boolean on) {
		switch (view.getId()) {
		case R.id.tb_load_huodong:
			SettingsUtils.writeBoolean(getActivity(), SettingsUtils.PREFERENCE_HUODONG, on);
			break;
		case R.id.tb_load_ask:
			SettingsUtils.writeBoolean(getActivity(), SettingsUtils.PREFERENCE_ANSWER, on);
			break;
		case R.id.tb_load_caina:
			SettingsUtils.writeBoolean(getActivity(), SettingsUtils.PREFERENCE_CANAED, on);
			break;
		case R.id.tb_load_zhuiwen:
			SettingsUtils.writeBoolean(getActivity(), SettingsUtils.PREFERENCE_ZHUIWEN, on);
			break;
		case R.id.tb_load_systemmessage:
			SettingsUtils.writeBoolean(getActivity(), SettingsUtils.PREFERENCE_SYSTEM, on);
			break;
		case R.id.tb_load_fensiqiuzhu:
			SettingsUtils.writeBoolean(getActivity(), SettingsUtils.PREFERENCE_FANS, on);
			break;
		}
	}
	
	private void readSettings() {
		boolean huodong=SettingsUtils.readBoolean(getActivity(), SettingsUtils.PREFERENCE_HUODONG);
		boolean answer=SettingsUtils.readBoolean(getActivity(), SettingsUtils.PREFERENCE_ANSWER);
		boolean canaed=SettingsUtils.readBoolean(getActivity(), SettingsUtils.PREFERENCE_CANAED);
		boolean zhuiwen=SettingsUtils.readBoolean(getActivity(), SettingsUtils.PREFERENCE_ZHUIWEN);
		boolean system=SettingsUtils.readBoolean(getActivity(), SettingsUtils.PREFERENCE_SYSTEM);
		boolean fans=SettingsUtils.readBoolean(getActivity(), SettingsUtils.PREFERENCE_FANS);
		
		tb_load_huodong.toggle(huodong);
		tb_load_ask.toggle(answer);
		tb_load_caina.toggle(canaed);
		tb_load_zhuiwen.toggle(zhuiwen);
		tb_load_systemmessage.toggle(system);
		tb_load_fensiqiuzhu.toggle(fans);
	}
}
