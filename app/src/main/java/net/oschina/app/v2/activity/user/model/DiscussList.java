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

public class DiscussList extends Entity implements ListEntity{
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String desc;
	//List，用来封装对象
	private List<DiscussPojo> discussDatas = new ArrayList<DiscussPojo>();
	
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
	
	public List<DiscussPojo> getDiscussDatas() {
		return discussDatas;
	}
	
	
	public static DiscussList parse(String jsonStr) throws IOException,AppException {
		DiscussList discussListObj = new DiscussList();
		
		
		try {
			
			JSONObject json = new JSONObject(jsonStr);

			discussListObj.setCode(json.optInt("code"));
			discussListObj.setDesc(json.optString("desc"));
			
			JSONArray dataList = new JSONArray(json.getString("data"));
			
			JSONObject jObj = null;
			for (int i = 0; i < dataList.length(); i++) 
			{
				jObj = dataList.getJSONObject(i);
				
				discussListObj.getDiscussDatas().add(DiscussPojo.parse(jObj));
	

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return discussListObj;
	}
	
	
	@Override
	public List<?> getList() {
		return discussDatas;
	}

}
