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

public class FunsForHelpList extends Entity implements ListEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6073681980127090214L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	//List，用来封装对象
	private List<FunsForHelp> funsforhelplist = new ArrayList<FunsForHelp>();
	
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


	public List<FunsForHelp> getFunsforhelplist() {
		return funsforhelplist;
	}

	public void setFunsforhelplist(List<FunsForHelp> funsforhelplist) {
		this.funsforhelplist = funsforhelplist;
	}

	public static FunsForHelpList parse(String jsonStr) throws IOException,
			AppException {
/*		AskAgainList askagainlist=new AskAgainList();*/
		FunsForHelpList funsforhelplist=new FunsForHelpList();
		/*AskAgain askAgain=new AskAgain();*/
		FunsForHelp funsforhelp=null;
		try {
			//把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
			funsforhelplist.setCode(json.getInt("code"));
			funsforhelplist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	funsforhelp=funsforhelp.parse(dataList.getJSONObject(i));
	            	funsforhelplist.getFunsforhelplist().add(funsforhelp);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return funsforhelplist;
	}
	@Override
	public List<?> getList() {
		return funsforhelplist;
	}
}
