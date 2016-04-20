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

public class AnswerItemList extends Entity implements ListEntity{
	private static final long serialVersionUID = -269305284575421776L;
	public final static int CATALOG_ALL = 1;
	public final static int CATALOG_INTEGRATION = 2;
	public final static int CATALOG_SOFTWARE = 3;
	private int code;
	private String desc;
	//List，用来封装对象
	private List<SingleAnswer> answerItemList = new ArrayList<SingleAnswer>();
	
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


	public List<SingleAnswer> getAnswerItemList() {
		return answerItemList;
	}

	public void setAnswerItemList(List<SingleAnswer> answerItemList) {
		this.answerItemList = answerItemList;
	}

	public static AnswerItemList parse(String jsonStr) throws IOException,
			AppException {
		/*QuestionList questionList=new QuestionList();*/
	/*	AskAgainList askagainlist=new AskAgainList();
		AskAgain askAgain=null;*/
		AnswerItemList answerItemList=new AnswerItemList();
		SingleAnswer singleAnswer=null;
		try {
			//把解析的String转换成Json
			JSONObject json = new JSONObject(jsonStr);
		/*	answerItemList.setCode(json.getInt("code"));
			answerItemList.setDesc(json.getString("desc"));*/
			JSONArray dataList = new JSONArray(json.getString("data"));
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	singleAnswer=singleAnswer.parse(dataList.getJSONObject(i));
	            	answerItemList.getAnswerItemList().add(singleAnswer);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return answerItemList;
	}
	@Override
	public List<?> getList() {
		return answerItemList;
	}
}
