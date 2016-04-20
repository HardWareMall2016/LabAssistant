package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 周刊列表实体类
 * 
 * @author Administrator
 * 
 */
public class WeekList extends Entity implements ListEntity {
	private static final long serialVersionUID = 1067118838408833363L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;

	private int code;
	private String desc;
	private List<Week> weekList = new ArrayList<Week>();

	@Override
	public List<?> getList() {
		// TODO Auto-generated method stub
		return weekList;
	}

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

	public List<Week> getWeekList() {
		return weekList;
	}

	public void setWeekList(List<Week> weekList) {
		this.weekList = weekList;
	}

	public static WeekList parseList(String jsonStr) throws IOException,
			AppException {
		WeekList weeklist = new WeekList();
		Week week = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			weeklist.setCode(json.getInt("code"));
			weeklist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			for (int i = 0; i < dataList.length(); i++) 
			
			{
				week = Week.parse(dataList.getJSONObject(i));
				week.setHeader(true);
				weeklist.getWeekList().add(week);
				
				String subData = dataList.getJSONObject(i).optString("subdata");
				if(TextUtils.isEmpty(subData)||subData.equals("null"))
				{
					continue;
				}
				
				JSONArray subJarray = new JSONArray(subData);
				for(int j = 0; j < subJarray.length(); j++)
				{
					week = Week.parseSubData(subJarray.getJSONObject(j));
					week.setHeader(false);
					weeklist.getWeekList().add(week);
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return weeklist;
	}
}
