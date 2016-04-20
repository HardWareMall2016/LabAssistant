package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新闻列表实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class DailyList extends Entity implements ListEntity {

	private static final long serialVersionUID = 1067118838408833362L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;

	private int code;
	private String desc;
	private List<Daily> dailylist = new ArrayList<Daily>();

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

	public List<Daily> getDailylist() {
		return dailylist;
	}

	public static DailyList parse(String jsonStr) throws IOException,
			AppException {
		DailyList dailylist = new DailyList();
		Daily daily = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			dailylist.setCode(json.getInt("code"));
			dailylist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			// JSONArray dataList = data.getJSONArray();
			// 遍历jsonArray
			for (int i = 0; i < dataList.length(); i++) {
				daily = Daily.parse(dataList.getJSONObject(i));
				dailylist.getDailylist().add(daily);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Collections.sort(dailylist.getDailylist(), new Comparator<Daily>() {
			@Override
			public int compare(Daily lhs, Daily rhs) {
				if (lhs.getInputtime() > rhs.getInputtime()) 
					return -1;
				else if (lhs.getInputtime() < rhs.getInputtime()) 
					return 1;
				return 0;
			}
		});
		
		return dailylist;
	}

	@Override
	public List<?> getList() {
		return dailylist;
	}
}
