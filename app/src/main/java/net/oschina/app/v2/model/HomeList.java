package net.oschina.app.v2.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 动弹列表实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class HomeList extends Entity implements ListEntity {

	private static final long serialVersionUID = 1067118838408833362L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;

	private int code;
	private String desc;
	private int lastid=0;
	private List<Home> homelist = new ArrayList<Home>();

	public int getCode() {
		return code;
	}
    public void setCode(int code)
    {
    	this.code=code;
    }
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc=desc;
	}
	public int getLastid() {
		return lastid;
	}
	public void setLastid(int lastid) {
		this.lastid = lastid;
	}
	public List<Home> getHomelist() {
		return homelist;
	}
 
	public static HomeList parse(String jsonStr) throws IOException,
			AppException {
		HomeList homelist = new HomeList();
		Home home = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			homelist.setCode(json.getInt("code"));
			homelist.setDesc(json.getString("desc"));
			homelist.setLastid(json.getInt("lastid"));
			JSONArray dataList = new JSONArray(json.getString("data"));
//			 JSONArray dataList = data.getJSONArray();
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	home=home.parse(dataList.getJSONObject(i));
	                homelist.getHomelist().add(home);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return homelist;
	}

	@Override
	public List<?> getList() {
		return homelist;
	}
}
