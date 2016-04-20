package net.oschina.app.v2.activity.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Entity;
import net.oschina.app.v2.model.ListEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionList extends Entity implements ListEntity{
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	//List，用来封装对象
	private List<Question> questionlist = new ArrayList<Question>();
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

	public List<Question> getQuesionlist() {
		return questionlist;
	}
 
	public static QuestionList parse(String jsonStr) throws IOException,
			AppException {
		QuestionList questionList=new QuestionList();
		Question question=null;
		try {
			//把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
			questionList.setCode(json.getInt("code"));
			questionList.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	question=question.parse(dataList.getJSONObject(i));
	                questionList.getQuesionlist().add(question);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return questionList;
	}
	@Override
	public List<?> getList() {
		return questionlist;
	}
}
