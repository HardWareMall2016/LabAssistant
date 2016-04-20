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
public class AskList extends Entity implements ListEntity {

	public final static int CATALOG_LASTEST = 0;
	public final static int CATALOG_HOT = -1;
	private int code;
	private String desc;
	private List<Ask> asklist = new ArrayList<Ask>();

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
	public List<Ask> getAsklist() {
		return asklist;
	}
	public static AskList parse(String jsonStr) throws IOException,
	AppException {
      AskList asklist = new AskList();
      Ask ask = null;
      try {
	  JSONObject json = new JSONObject(jsonStr);
//	tweetlist.setCode(json.getInt("code"));
//	tweetlist.setDesc(json.getString("desc"));
	 int code =json.getInt("code");
	 if(code!=88){
		 return asklist;
	 }
   	JSONArray dataList = new JSONArray(json.getString("data"));
//	 JSONArray dataList = data.getJSONArray();
        // 遍历jsonArray
        for (int i = 0; i < dataList.length(); i++)
        {
        	ask=ask.parse(dataList.getJSONObject(i));
        	asklist.getAsklist().add(ask);
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
