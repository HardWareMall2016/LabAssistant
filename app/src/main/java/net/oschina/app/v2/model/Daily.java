package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新闻实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Daily extends Entity implements Comparable<Daily>{
	
	public static final int ITEM = 0;
	public static final int SECTION = 1;
	
	private int id;
	private String title;
	private String thumb;
	private int inputtime;
	private String date;
	private int allow_share;
	private int type;
	private boolean isTop;
	private int articalType;//文章分类
	
	private boolean isWenJuan;//是否是问卷调查
	
	public Daily(){
	
	}	
	
	
	public int getArticalType() {
		return articalType;
	}


	public void setArticalType(int articalType) {
		this.articalType = articalType;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public int getInputtime() {
		return inputtime;
	}
	public void setInputtime(int inputtime) {
		this.inputtime = inputtime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getAllowShare() {
		return allow_share;
	}
	public void setAllowShare(int allow_share) {
		this.allow_share = allow_share;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isTop() {
		return isTop;
	}
	public void setTop(boolean isTop) {
		this.isTop = isTop;
	}
	
public boolean isWenJuan() {
		return isWenJuan;
	}
	public void setWenJuan(boolean isWenJuan) {
		this.isWenJuan = isWenJuan;
	}
	//	public static Daily parse(InputStream inputStream) throws IOException, AppException {
//		Daily daily = null;
//        return daily;       
//	}
	public static Daily parse(JSONObject response) throws IOException, AppException {
		Daily daily = new Daily();
		try {
			daily.setId(response.getInt("id"));
			daily.setTitle(response.getString("title"));
			daily.setThumb(response.getString("thumb"));
			daily.setInputtime(response.optInt("inputtime",0));
			daily.setDate(response.optString("date"));
			daily.setAllowShare(response.getInt("allow_share"));
			daily.setArticalType(response.getInt("type"));
			
			int qnid = response.optInt("qnid", -1);
			daily.setWenJuan(qnid != -1);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
     
        return daily;       
	}
	
	
	@Override
	public int compareTo(Daily another) {

		if(null == another)
		{
			return 1;
		}
		
		return this.inputtime - another.getInputtime();
	}
}
