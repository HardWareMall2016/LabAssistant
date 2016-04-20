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

public class RenWuChengJiuList extends Entity implements ListEntity{
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	//List，用来封装对象
	private List<XiTongXiaoXi> xitongxiaoxilist = new ArrayList<XiTongXiaoXi>();
	
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
	public List<XiTongXiaoXi> getXitongxiaoxilist() {
		return xitongxiaoxilist;
	}
	public void setXitongxiaoxilist(List<XiTongXiaoXi> xitongxiaoxilist) {
		this.xitongxiaoxilist = xitongxiaoxilist;
	}
	public static XiTongXiaoXiList parse(String jsonStr) throws IOException,
			AppException {
		/*AnswerList answerList=new AnswerList();*/
		XiTongXiaoXiList xitongxiaoxilist=new XiTongXiaoXiList();
		XiTongXiaoXi xiTongXiaoXi=null;
		try {
			//把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
			/*xitonglist.setCode(json.getInt("code"));
			xitonglist.setDesc(json.getString("desc"));*/
			JSONArray dataList = new JSONArray(json.getString("data"));
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	xiTongXiaoXi=xiTongXiaoXi.parse(dataList.getJSONObject(i));
	            	xitongxiaoxilist.getXitongxiaoxilist().add(xiTongXiaoXi);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return xitongxiaoxilist;
	}
	@Override
	public List<?> getList() {
		return xitongxiaoxilist;
	}
}
