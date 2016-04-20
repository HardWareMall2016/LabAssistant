package net.oschina.app.v2.activity.user.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Entity;
import net.oschina.app.v2.model.ListEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyInterstList extends Entity implements ListEntity{
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	//List，用来封装对象
	private List<Intersted> interstedilist = new ArrayList<Intersted>();
	
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
	
	public List<Intersted> getInterstedilist() {
		return interstedilist;
	}
	public void setInterstedilist(List<Intersted> interstedilist) {
		this.interstedilist = interstedilist;
	}
	public static MyInterstList parse(String jsonStr) throws IOException,
			AppException {
		MyInterstList interstedilist=new MyInterstList();
		Intersted intersted=null;
		try {
			// 把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
			/*
			 * xitonglist.setCode(json.getInt("code"));
			 * xitonglist.setDesc(json.getString("desc"));
			 */

			JSONObject jObj = null;
			JSONArray jArray = null;

			JSONArray dataList = new JSONArray(json.getString("data"));
			
			// 遍历jsonArray
			for (int i = 0; i < dataList.length(); i++) {
				jObj = dataList.getJSONObject(i);
				jArray = new JSONArray(jObj.getString("subcategory"));

				if (jArray.length() > 0) {
					
					intersted = Intersted.parse(jObj);
					intersted.setBigCategory(true);
					interstedilist.getInterstedilist().add(intersted);

					int categoryId = intersted.getId();
					
					for (int j = 0; j < jArray.length(); j++) {
						
						intersted = Intersted.parse(jArray.getJSONObject(j));
						intersted.setBigCategory(false);
						intersted.setCategoryId(categoryId);
						
						interstedilist.getInterstedilist().add(intersted);
					}
				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return interstedilist;
	}
	@Override
	public List<?> getList() {
		return interstedilist;
	}

}
