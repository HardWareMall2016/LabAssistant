package net.oschina.app.v2.activity.find.fragment;

import net.oschina.app.v2.model.TweetList;

import com.shiyanzhushou.app.R;

public enum MallTab {
	MALL_SHANGPIN(0, TweetList.TYPE_WEEKBILLBOARD, R.string.mall_shangpin,
			GiftListFragment.class),

	MALL_XUEXI(1, TweetList.TYPE_TOTALBILLBOARD, R.string.mall_xuexi,
			VisualGiftListFragment.class);

	private Class<?> clz;
	private int idx;
	private int title;
	private int catalog;

	public static MallTab getTabByIdx(int idx) {
		for (MallTab t : values()) {
			if (t.getIdx() == idx)
				return t;
		}
		return MALL_SHANGPIN;
	}

	private MallTab(int idx, int catalog, int title, Class<?> clz) {
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
