package net.oschina.app.v2.activity.home.model;

import android.util.Log;

import java.io.IOException;

import net.oschina.app.v2.AppException;
import net.oschina.app.v2.model.Entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 新闻实体类
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Ad extends Entity{
	
	private int id;
	private String title;
	private String inputtime;
	private String image;
	private String url;
	private int type;
	private int articleid;
	
	public Ad(){
	
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String gettitle() {
		return title;
	}
	public void settitle(String title) {
		this.title = title;
	}
	public String getinputtime() {
		return inputtime;
	}
	public void setinputtime(String inputtime) {
		this.inputtime = inputtime;
	}
	
	
	public String getimage() {
		return image;
	}
	public void setimage(String image) {
		this.image = image;
	}
	
	public String geturl() {
		return url;
	}
	public void seturl(String url) {
		this.url = url;
	}
	
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getArticleid() {
		return articleid;
	}
	public void setArticleid(int articleid) {
		this.articleid = articleid;
	}
	public static Ad parse(JSONObject response) throws IOException, AppException {
		Ad daily = new Ad();
		
		
		try {
			daily.setId(response.getInt("id"));
			daily.settitle(response.getString("title"));
			daily.setinputtime(response.getString("inputtime"));
			daily.setimage(response.getString("image"));
			daily.seturl(response.getString("url"));
			daily.setArticleid(response.optInt("articleid"));
			daily.setType(response.optInt("type"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
     
        return daily;       
	}
}
