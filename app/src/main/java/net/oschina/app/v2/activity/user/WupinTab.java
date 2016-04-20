package net.oschina.app.v2.activity.user;


import net.oschina.app.v2.activity.user.fragment.ShiWuLiPinFragment;
import net.oschina.app.v2.activity.user.fragment.VirsualShiWuLiPinFragment;
import net.oschina.app.v2.model.FavoriteList;

import com.shiyanzhushou.app.R;

public enum WupinTab{
	
	SOFTWARE(0, FavoriteList.TYPE_SOFTWARE, R.string.frame_title_shiwulipin, ShiWuLiPinFragment.class), 
	POST(1, FavoriteList.TYPE_POST, R.string.frame_title_xuexiziliao,VirsualShiWuLiPinFragment.class);
	/*CODE(2, FavoriteList.TYPE_CODE, R.string.frame_title_favorite_code,FavoriteFragment.class),
	BLOG(3, FavoriteList.TYPE_BLOG, R.string.frame_title_favorite_blog,FavoriteFragment.class),
	NEWS(4, FavoriteList.TYPE_NEWS, R.string.frame_title_favorite_news,FavoriteFragment.class);*/
	private Class<?> clz;
	private int idx;
	private int title;
	private int catalog;

	private WupinTab(int idx, int catalog, int title, Class<?> clz) {
		this.idx = idx;
		this.clz = clz;
		this.setCatalog(catalog);
		this.setTitle(title);
	}

	public static WupinTab getTabByIdx(int idx) {
		for (WupinTab t : values()) {
			if (t.getIdx() == idx)
				return t;
		}
		return SOFTWARE;
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
