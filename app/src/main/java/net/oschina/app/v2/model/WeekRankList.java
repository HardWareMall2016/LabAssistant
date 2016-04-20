package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeekRankList extends Entity implements ListEntity {
	private static final long serialVersionUID = 2067118838408833362L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	private List<WeekRank> weekRankList = new ArrayList<WeekRank>();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<WeekRank> getWeekRankList() {
		return weekRankList;
	}

	public void setWeekRankList(List<WeekRank> weekRankList) {
		this.weekRankList = weekRankList;
	}

	@Override
	public List<?> getList() {
		// TODO Auto-generated method stub
		return weekRankList;
	}

	public static WeekRankList parse(String jsonStr) throws IOException,
			AppException {
		WeekRankList wrlist = new WeekRankList();
		WeekRank weekrank = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			wrlist.setCode(json.getInt("code"));
			wrlist.setDesc(json.getString("desc"));
			JSONArray datalist = new JSONArray(json.getString("data"));
			for (int i = 0; i < datalist.length(); i++) {
				weekrank = weekrank.parse(datalist.getJSONObject(i));
				wrlist.getWeekRankList().add(weekrank);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return wrlist;
	}
}
