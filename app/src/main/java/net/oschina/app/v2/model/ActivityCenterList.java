package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityCenterList extends Entity implements ListEntity {
	private static final long serialVersionUID = 1067118838408832362L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	private List<ActivityCenter> aclist = new ArrayList<ActivityCenter>();

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

	public List<ActivityCenter> getAclist() {
		return aclist;
	}

	public void setAclist(List<ActivityCenter> aclist) {
		this.aclist = aclist;
	}

	@Override
	public List<?> getList() {
		// TODO Auto-generated method stub
		return aclist;
	}

	public static ActivityCenterList parse(String jsonStr) throws IOException,
			AppException {
		ActivityCenterList aclist = new ActivityCenterList();
		ActivityCenter ac = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			aclist.setCode(json.getInt("code"));
			aclist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
			for (int i = 0; i < dataList.length(); i++) {
				ac = ac.parse(dataList.getJSONObject(i));
				aclist.getAclist().add(ac);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return aclist;
	}
}
