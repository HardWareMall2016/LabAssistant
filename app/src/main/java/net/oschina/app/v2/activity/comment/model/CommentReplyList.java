package net.oschina.app.v2.activity.comment.model;

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
 * 评论列表实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class CommentReplyList extends Entity implements ListEntity {

	public final static int CATALOG_NEWS = 1;
	public final static int CATALOG_POST = 2;
	public final static int CATALOG_TWEET = 3;
	public final static int CATALOG_ACTIVE = 4;
	public final static int CATALOG_MESSAGE = 4;// 动态与留言都属于消息中心

	
	private int code;
	private String desc;
	
	private int pageSize;
	private int allCount;
	private List<CommentReply> commentlist = new ArrayList<CommentReply>();

	
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
	
	public int getPageSize() {
		return pageSize;
	}

	public int getAllCount() {
		return allCount;
	}

	public List<CommentReply> getCommentlist() {
		return commentlist;
	}

	public static CommentReplyList parseReply(String jsonStr)
			throws IOException, AppException {
		CommentReplyList commentlist = new CommentReplyList();
		CommentReply comment = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			commentlist.setCode(json.getInt("code"));
			commentlist.setDesc(json.getString("desc"));
			JSONArray dataList = new JSONArray(json.getString("data"));
//			 JSONArray dataList = data.getJSONArray();
	            // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	comment=comment.parseReply(dataList.getJSONObject(i));
	            	commentlist.getCommentlist().add(comment);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return commentlist;
	}
	
	public static CommentReplyList parseAnswerAfter(String jsonStr)
			throws IOException, AppException {
		CommentReplyList commentlist = new CommentReplyList();
		CommentReply comment = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			commentlist.setCode(json.getInt("code"));
			commentlist.setDesc(json.getString("desc"));
			
			JSONArray dataList = new JSONArray(json.getString("data"));
			//JSONArray dataList = data.getJSONArray();
	        // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	comment=comment.parseAnswerAfter(dataList.getJSONObject(i));
	            	commentlist.getCommentlist().add(comment);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return commentlist;
	}
	
	public static CommentReplyList parseReplyAnswerAfter(String jsonStr)
			throws IOException, AppException {
		CommentReplyList commentlist = new CommentReplyList();
		CommentReply comment = null;
		try {
			JSONObject json = new JSONObject(jsonStr);
			commentlist.setCode(json.getInt("code"));
			commentlist.setDesc(json.getString("desc"));
			JSONObject dataObj=new JSONObject(json.getString("data"));
			int count=dataObj.optInt("count");
			JSONArray dataList = new JSONArray(dataObj.getString("objs"));
			//JSONArray dataList = data.getJSONArray();
	        // 遍历jsonArray
	            for (int i = 0; i < dataList.length(); i++)
	            {
	            	comment=comment.parseAnswerAfter(dataList.getJSONObject(i));
	            	commentlist.getCommentlist().add(comment);
	            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return commentlist;
	}

	@Override
	public List<?> getList() {
		return commentlist;
	}

}
