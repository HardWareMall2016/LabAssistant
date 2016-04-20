package net.oschina.app.v2.activity.tweet.model;

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
public class Mulu extends Entity{
	
	private int id;
	private String catname;
	public Mulu(){
	
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getcatname() {
		return catname;
	}
	public void setcatname(String catname) {
		this.catname = catname;
	}
//	public static Daily parse(InputStream inputStream) throws IOException, AppException {
//		Daily daily = null;
//        return daily;       
//	}
	public static Mulu parse(JSONObject response) throws IOException, AppException {
		Mulu ask = new Mulu();
		try {
			ask.setId(response.getInt("id"));
			ask.setcatname(response.getString("catname"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
        return ask;       
	}
}
