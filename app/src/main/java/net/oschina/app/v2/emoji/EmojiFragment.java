package net.oschina.app.v2.emoji;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.oschina.app.v2.AppContext;
import net.oschina.app.v2.activity.tweet.model.UserBean;
import net.oschina.app.v2.activity.tweet.view.ExtendMediaPicker;
import net.oschina.app.v2.base.BaseFragment;
import net.oschina.app.v2.emoji.EmojiViewPagerAdapter.OnClickEmojiListener;
import net.oschina.app.v2.emoji.SoftKeyboardStateHelper.SoftKeyboardStateListener;
import net.oschina.app.v2.utils.TDevice;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shiyanzhushou.app.R;

public class EmojiFragment extends BaseFragment implements
		SoftKeyboardStateListener, OnClickEmojiListener {

	private ImageButton mBtnEmoji, mBtnSend, mBtnMore;
	private EmojiEditText mEtInput;
	private EmojiViewPagerAdapter mPagerAdapter;
	private SoftKeyboardStateHelper mKeyboardHelper;
	private boolean mIsKeyboardVisible;
	private boolean mNeedHideEmoji;
	private int mCurrentKeyboardHeigh;
    private TextView tv_zhuiwen;
	private EmojiTextListener mListener;
	private int mMoreVisisable = View.GONE;
	private OnClickListener mMoreClickListener;

	public interface EmojiTextListener {
		public void onSendClick(String text);
	}

	public void setEmojiTextListener(EmojiTextListener lis) {
		mListener = lis;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.v2_fragment_emoji, container,
				false);

		initViews(view);
		mKeyboardHelper = new SoftKeyboardStateHelper(getActivity().getWindow()
				.getDecorView());// .findViewById(R.id.activity_root)
		mKeyboardHelper.addSoftKeyboardStateListener(this);
		return view;
	}

	@Override
	public void onDestroyView() {
		mKeyboardHelper.removeSoftKeyboardStateListener(this);
		super.onDestroyView();
	}

	private void initViews(View view) {

		tv_zhuiwen = (TextView) view.findViewById(R.id.tv_zhuiwen);
		mBtnMore = (ImageButton) view.findViewById(R.id.btn_more);
		//mBtnMore.setOnClickListener(mMoreClickListener);
		mBtnMore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ExtendMediaPicker pickView=new ExtendMediaPicker(getActivity());
				pickView.showPickerView();
			}
		});

		/*mBtnEmoji = (ImageButton) view.findViewById(R.id.btn_emoji);*/
		mBtnSend = (ImageButton) view.findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mEtInput = (EmojiEditText) view.findViewById(R.id.et_input);
/*
		mBtnEmoji.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(), SupperListActivity.class), 1000);
			}
		});*/

		Map<String, Emoji> emojis = EmojiHelper.qq_emojis_nos;
	
		List<Emoji> allEmojis = new ArrayList<Emoji>();
		Iterator<String> itr1 = emojis.keySet().iterator();
		while (itr1.hasNext()) {
			Emoji ej = emojis.get(itr1.next());
			allEmojis.add(new Emoji(ej.getResId(), ej.getValue(),
					ej.getValueNo(), ej.getIndex()));
		}
		Collections.sort(allEmojis);

		List<List<Emoji>> pagers = new ArrayList<List<Emoji>>();
		List<Emoji> es = null;
		int size = 0;
		boolean justAdd = false;
		for (Emoji ej : allEmojis) {
			if (size == 0) {
				es = new ArrayList<Emoji>();
			}
			es.add(new Emoji(ej.getResId(), ej.getValue(), ej.getValueNo()));
			size++;
			if (size == 20) {
				pagers.add(es);
				size = 0;
				justAdd = true;
			} else {
				justAdd = false;
			}
		}
		if (!justAdd && es != null) {
			pagers.add(es);
		}


	
	}

	private int caculateEmojiPanelHeight() {
		mCurrentKeyboardHeigh = AppContext.getSoftKeyboardHeight();
		if (mCurrentKeyboardHeigh == 0) {
			mCurrentKeyboardHeigh = (int) TDevice.dpToPixel(180);
		}
		int emojiPanelHeight = (int) (mCurrentKeyboardHeigh - TDevice
				.dpToPixel(20));
		int emojiHeight = (int) (emojiPanelHeight / 3);

	
		if (mPagerAdapter != null) {
			mPagerAdapter.setEmojiHeight(emojiHeight);
		}
		return emojiHeight;
	}

	@Override
	public boolean onBackPressed() {
		return super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
        if (id == R.id.btn_send) {
			if (mListener != null) {
				mListener.onSendClick(mEtInput.getText().toString());
			}
		}
	}


	public void showZhuiwen() {
		//mEtInput.setHint("追问");
		mEtInput.setHint("追问");
		mEtInput.setHintTextColor(getResources().getColor(R.color.font_zhuiwen_back));
	//	mEtInput.setTextColor();
		//tv_zhuiwen.setVisibility(View.VISIBLE);
	}
	private void showEmojiPanel() {
		mNeedHideEmoji = false;
	/*	mBtnEmoji.setBackgroundResource(R.drawable.btn_emoji_pressed);*/
	}
	
	public void hideEmojiPanel() {
		mBtnEmoji.setVisibility(View.GONE);
	}


	@Override
	public void onSoftKeyboardOpened(int keyboardHeightInPx) {
		int realKeyboardHeight = keyboardHeightInPx
				- TDevice.getStatusBarHeight();

		AppContext.setSoftKeyboardHeight(realKeyboardHeight);
		if (mCurrentKeyboardHeigh != realKeyboardHeight) {
			caculateEmojiPanelHeight();
		}

		mIsKeyboardVisible = true;
	}

	@Override
	public void onSoftKeyboardClosed() {
		mIsKeyboardVisible = false;
		if (mNeedHideEmoji) {
			showEmojiPanel();
		}
	}

	@Override
	public void onEmojiClick(Emoji emoji) {
		mEtInput.insertEmoji(emoji);
	}

	@Override
	public void onDelete() {
		mEtInput.delete();
	}

	public void requestFocusInput() {
		if (mEtInput != null) {
			mEtInput.requestFocus();
			if (!mIsKeyboardVisible) {
				TDevice.toogleSoftKeyboard(getActivity().getCurrentFocus());
				//TDevice.showSoftKeyboard(getActivity().getCurrentFocus());
			}
		}
	}

	public void hideKeyboard() {
		if (mIsKeyboardVisible) {
			TDevice.toogleSoftKeyboard(getActivity().getCurrentFocus());
		}
	}

	public void setInputHint(String hint) {
		if (mEtInput != null) {
			mEtInput.setHint(hint);
		}
	}


	public void setButtonMoreClickListener(View.OnClickListener lis) {
		if (mBtnMore != null)
			mBtnMore.setOnClickListener(lis);
		else
			mMoreClickListener = lis;
	}

	public void reset() {
		if (mIsKeyboardVisible) {
			TDevice.hideSoftKeyboard(mEtInput);
		}

		if (mEtInput != null) {
			mEtInput.getText().clear();
			mEtInput.clearFocus();
			//mEtInput.setHint(R.string.publish_comment);
			mEtInput.setHint(R.string.publish_comment);
			mEtInput.setHintTextColor(getResources().getColor(R.color.font_zhuiwen_back));
		//	mEtInput.setTextColor();
		}
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode==Activity.RESULT_OK) {
			HashMap<Integer, UserBean> userList=(HashMap<Integer, UserBean>) intent.getSerializableExtra("supperList");
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<Integer, UserBean> entry : userList.entrySet()) {
				sb.append("@"+entry.getValue().getNickname()+";");
			}
			mEtInput.setText(sb.toString());
		}
	}
}
