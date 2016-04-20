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

public class GiftList extends Entity implements ListEntity{
	private static final long serialVersionUID = 1L;
	
	private int code;
	private String desc;
	//List，用来封装对象
	private List<GiftPojo> discussDatas = new ArrayList<GiftPojo>();
	
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
	
	public List<GiftPojo> getDiscussDatas() {
		return discussDatas;
	}
	
	
	public static GiftList parse(String jsonStr) throws IOException,AppException {
		GiftList discussListObj = new GiftList();
		
		
		try {
			
			JSONObject json = new JSONObject(jsonStr);

			discussListObj.setCode(json.optInt("code"));
			discussListObj.setDesc(json.optString("desc"));
			
			JSONArray dataList = new JSONArray(json.getString("data"));
			
			JSONObject jObj = null;
			for (int i = 0; i < dataList.length(); i++) 
			{
				jObj = dataList.getJSONObject(i);
				
				discussListObj.getDiscussDatas().add(GiftPojo.parse(jObj));
	

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
