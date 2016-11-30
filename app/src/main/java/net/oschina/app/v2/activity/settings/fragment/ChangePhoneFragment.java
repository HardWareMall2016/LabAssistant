package net.oschina.app.v2.activity.settings.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.Emoji;
import net.oschina.app.v2.utils.TDevice;

public class ChangePhoneFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.v2_fragment_change_phone, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
    }

    public boolean onBackPressed() {
        TDevice.hideSoftKeyboard(getActivity().getCurrentFocus());
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
