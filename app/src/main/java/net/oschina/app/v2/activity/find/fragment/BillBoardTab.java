package net.oschina.app.v2.activity.find.fragment;

import net.oschina.app.v2.model.TweetList;

import com.shiyanzhushou.app.R;

public enum BillBoardTab {
	
	WEEKBILLBOARD(0, TweetList.TYPE_WEEKBILLBOARD,R.string.find_benzhoubaipang, WeekRankFragment.class), 
	TOTALBILLBOARD(1, TweetList.TYPE_TOTALBILLBOARD, R.string.find_zongpaihang,TotalRankFragment.class);
	
	private Class<?> clz;
	private int idx;
	private int title;
	private int catalog;

	public static BillBoardTab getTabByIdx(int idx) {
		for (BillBoardTab t : values()) {
			if (t.getIdx() == idx)
				return t;
		}
		return WEEKBILLBOARD;
	}

	private BillBoardTab(int idx, int catalog, int title, Class<?> clz) {
		this.clz = clz;
		this.idx = idx;
		this.title = title;
		this.catalog = catalog;
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
