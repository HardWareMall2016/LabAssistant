package net.oschina.app.v2.emoji;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EmojiEditText extends EditText {

	public static final Pattern EMOJI = Pattern
			.compile("\\[(([\u4e00-\u9fa5]+)|([a-zA-z]+))\\]");
	public static final Pattern EMOJI_PATTERN = Pattern
			.compile("\\[[(0-9)]+\\]");

	private String mHeader;
	private int mAskUidId;
	private String mHeaderUnDelete;

	public EmojiEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmojiEditText(Context context) {
		super(context);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		Spannable sp = getText();
		String str = getText().toString();
		if (mHeader != null) {
			if (mHeader.subSequence(0, mHeader.length() - 1).equals(str)) {
				setText("");
				mHeader = null;
			}
		}
		if(mHeaderUnDelete!=null){
			if (mHeaderUnDelete.subSequence(0, mHeaderUnDelete.length() - 1).equals(str)) {
				setText(Html.fromHtml("<font color=#2FBDE7>" + mHeaderUnDelete + "</font>"));
				setSelection(mHeaderUnDelete.length());
			}
		}
		Matcher m = EMOJI_PATTERN.matcher(str);
		while (m.find()) {
			int s = m.start();
			int e = m.end();
			String value = m.group();
			Emoji emoji = EmojiHelper.getEmojiByNumber(value);
			if (emoji != null) {
				sp.setSpan(new EmojiSpan(value, 30, 1), s, e,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}

		Matcher m2 = EMOJI.matcher(str);
		while (m2.find()) {
			int s = m2.start();
			int e = m2.end();
			String value = m2.group();
			Emoji emoji = EmojiHelper.getEmoji(value);
			if (emoji != null) {
				sp.setSpan(new EmojiSpan(value, 30, 0), s, e,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
	}

	public void insertEmoji(Emoji emoji) {
		if (emoji == null)
			return;
		int start = getSelectionStart();
		int end = getSelectionEnd();
		String value = emoji.getValue2();
		if (start < 0) {
			append(value);
		} else {
			getText().replace(Math.min(start, end), Math.max(start, end),
					value, 0, value.length());
		}
	}

	public void delete() {
		KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0,
				0, KeyEvent.KEYCODE_ENDCALL);
		dispatchKeyEvent(event);
	}

	public void clearHeader(){
		mHeader=null;
	}

	public void setHeader(String text) {
		//old
		/*mHeader = text;
		this.setText(Html.fromHtml("<font color=#2FBDE7>" + text + "</font>"));*/

		String originalText=getText().toString();

		//new by wuyue
		boolean insertHeader=false;
		if(TextUtils.isEmpty(mHeader)){
			insertHeader=true;
		}else{
			if(mHeader.equals(text)){
				return;
			}
		}

		if(insertHeader){
			this.setText(Html.fromHtml("<font color=#2FBDE7>" + text + "</font>"));
			this.append(originalText);
		}else{
			originalText=originalText.substring(mHeader.length());
			this.setText(Html.fromHtml("<font color=#2FBDE7>" + text + "</font>"));
			this.append(originalText);
		}

		mHeader = text;
		//this.setText(Html.fromHtml("<font color=#2FBDE7>" + text + "</font>"));
	}
	
	public void setAskUid(int askUid) {
		mAskUidId = askUid;
	}
	
	public int getAskUid(){
		return mAskUidId;
	}
	
	public String getHeader() {
		return mHeader;
	}

	public void setHeaderUnDelete(String text){
		mHeaderUnDelete = text;
		this.setText(Html.fromHtml("<font color=#2FBDE7>" + text + "</font>"));
		this.setSelection(text.length());
	}

	public String getmHeaderUnDelete() {
		return mHeaderUnDelete;
	}
	
}
