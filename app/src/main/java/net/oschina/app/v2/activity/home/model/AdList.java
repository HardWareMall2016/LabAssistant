package net.oschina.app.v2.activity.home.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Entity;
import net.oschina.app.v2.model.ListEntity;

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
public class AdList extends Entity implements ListEntity {

	private static final long serialVersionUID = 1067118838408833362L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;

	private int code;
	private String desc;
	private List<Ad> adlist = new ArrayList<Ad>();

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

	public List<Ad> getAdlist() {
		return adlist;
	}
 
	public static AdList parse(String jsonStr) throws IOException,
			AppException {
		AdList adlist = new AdList();
		Ad ad = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			adlist.setCode(json.getInt("code"));
			adlist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
//			 JSONArray dataList = data.getJSONArray();
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	ad=ad.parse(dataList.getJSONObject(i));
	                adlist.getAdlist().add(ad);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return adlist;
	}

	@Override
	public List<?> getList() {
		return adlist;
	}
}
