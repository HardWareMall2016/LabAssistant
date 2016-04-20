package net.oschina.app.v2.actvity.user.tabs;

import net.oschina.app.v2.activity.user.fragment.ShiWuLiPinFragment;
import net.oschina.app.v2.activity.user.fragment.WodeziliaoFragment;
import net.oschina.app.v2.model.BlogList;
import net.oschina.app.v2.model.NewsList;

import com.shiyanzhushou.app.R;

public enum CargoTabs {
	LASTEST(0,NewsList.CATALOG_ALL, R.string.tabs_shiwuliping, ShiWuLiPinFragment.class),
    BLOG(1, BlogList.CATALOG_LATEST,R.string.tabs_xuexiziliao, WodeziliaoFragment.class);
    //类型，ID，标题，目录
    private Class<?> clz;
    private int idx;
    private int title;
    private int catalog;

    private CargoTabs(int idx,int catalog, int title, Class<?> clz) {
        this.idx = idx;
        this.clz = clz;
        this.setCatalog(catalog);
        this.setTitle(title);
    }

    public static CargoTabs getTabByIdx(int idx) {
        for (CargoTabs t : values()) {
            if (t.getIdx() == idx)
                return t;
        }
        return LASTEST;
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
