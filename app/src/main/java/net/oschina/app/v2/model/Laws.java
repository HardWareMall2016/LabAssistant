package net.oschina.app.v2.model;

import java.io.IOException;

import net.oschina.app.v2.AppException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 法律文献实体类
 * 
 * @author jaysi
 * 
 */
public class Laws extends Entity {
	private int id;
	private String title;
	private String description;
	private String thumb;
	private int allow_share;
	private int inputtime;

	public Laws() {

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getAllow_share() {
		return allow_share;
	}

	public void setAllow_share(int allow_share) {
		this.allow_share = allow_share;
	}

	public int getInputtime() {
		return inputtime;
	}

	public void setInputtime(int inputtime) {
		this.inputtime = inputtime;
	}

	public static Laws parse(JSONObject respose) throws IOException,
			AppException {
		Laws laws = new Laws();
		try {
			laws.setId(respose.getInt("id"));
			laws.setTitle(respose.getString("title"));
			laws.setDescription(respose.getString("description"));
			laws.setThumb(respose.getString("thumb"));
			laws.setAllow_share(respose.getInt("allow_share"));
			laws.setInputtime(respose.getInt("inputtime"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return laws;
	}
}
