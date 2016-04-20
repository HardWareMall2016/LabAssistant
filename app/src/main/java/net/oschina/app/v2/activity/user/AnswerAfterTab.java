package net.oschina.app.v2.activity.user;

import net.oschina.app.v2.activity.user.fragment.AskMeAgainFragment;
import net.oschina.app.v2.activity.user.fragment.MyAnswerAfterFragment;
import net.oschina.app.v2.activity.user.model.AskAgainList;

import com.shiyanzhushou.app.R;

public enum AnswerAfterTab {
	MYANSWERAFTER(0, AskAgainList.TYPE_MY, R.string.frame_title_myanswerafter, MyAnswerAfterFragment.class), 
	BEANSWERAFTER(1, AskAgainList.TYPE_BE, R.string.frame_title_beanswerafter,AskMeAgainFragment.class);
	private Class<?> clz;
	private int idx;
	private int title;
	private int catalog;

	private AnswerAfterTab(int idx, int catalog, int title, Class<?> clz) {
		this.idx = idx;
		this.clz = clz;
		this.setCatalog(catalog);
		this.setTitle(title);
	}

	public static AnswerAfterTab getTabByIdx(int idx) {
		for (AnswerAfterTab t : values()) {
			if (t.getIdx() == idx)
				return t;
		}
		return MYANSWERAFTER;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}
}
