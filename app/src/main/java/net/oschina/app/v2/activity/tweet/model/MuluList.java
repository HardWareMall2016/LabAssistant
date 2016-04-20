package net.oschina.app.v2.activity.tweet.model;

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
public class MuluList extends Entity implements ListEntity {

	public final static int CATALOG_LASTEST = 0;
	public final static int CATALOG_HOT = -1;
	private int code;
	private String desc;
	private List<Mulu> asklist = new ArrayList<Mulu>();

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
	public List<Mulu> getMululist() {
		return asklist;
	}
	public static MuluList parse(String jsonStr) throws IOException,
	AppException {
      MuluList asklist = new MuluList();
      Mulu ask = null;
      try {
	  JSONObject json = new JSONObject(jsonStr);
	  asklist.setCode(json.getInt("code"));
	  asklist.setDesc(json.getString("desc"));
     	JSONArray dataList = new JSONArray(json.getString("data"));
        for (int i = 0; i < dataList.length(); i++)
        {
        	ask=ask.parse(dataList.getJSONObject(i));
        	asklist.getMululist().add(ask);
        }
       } catch (JSONException e) {
	    e.printStackTrace();
       }
     return asklist;
    }
	
	
	@Override
	public List<?> getList() {
		return asklist;
	}
	
	
 
	

	
}
