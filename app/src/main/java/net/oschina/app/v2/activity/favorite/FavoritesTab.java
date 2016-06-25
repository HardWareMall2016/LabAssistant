package net.oschina.app.v2.activity.favorite;

import com.shiyanzhushou.app.R;

import net.oschina.app.v2.activity.user.fragment.FavoriteMeFragment;
import net.oschina.app.v2.activity.user.fragment.MyFavoriteFragment;
import net.oschina.app.v2.model.FavoriteList;

public enum FavoritesTab {

	SOFTWARE(0, FavoriteList.TYPE_SOFTWARE, R.string.frame_title_favorites_answer, MyFavoriteFragment.class),
	POST(1, FavoriteList.TYPE_POST, R.string.frame_title_favorites_article,FavoriteMeFragment.class);

	private Class<?> clz;
	private int idx;
	private int title;
	private int catalog;

	private FavoritesTab(int idx, int catalog, int title, Class<?> clz) {
		this.idx = idx;
		this.clz = clz;
		this.setCatalog(catalog);
		this.setTitle(title);
	}

	public static FavoritesTab getTabByIdx(int idx) {
		for (FavoritesTab t : values()) {
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
